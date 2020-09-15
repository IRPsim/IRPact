package de.unileipzig.irpact.temp.jadex;

import de.unileipzig.irpact.jadex.util.JadexUtil;
import de.unileipzig.irpact.start.irpact.input.need.Need;
import de.unileipzig.irpact.start.irpact.input.product.FixedProduct;
import de.unileipzig.irpact.start.irpact.input.product.ProductGroup;
import de.unileipzig.irpact.temp.SatisfiedNeed;
import de.unileipzig.irpact.temp.TConsumerAgentData;
import jadex.bdiv3.BDIAgentFactory;
import jadex.bdiv3.annotation.*;
import jadex.bdiv3.features.IBDIAgentFeature;
import jadex.bdiv3.runtime.impl.PlanFailureException;
import jadex.bridge.IInternalAccess;
import jadex.bridge.component.IArgumentsResultsFeature;
import jadex.bridge.component.IExecutionFeature;
import jadex.bridge.service.ServiceScope;
import jadex.bridge.service.annotation.OnEnd;
import jadex.bridge.service.annotation.OnInit;
import jadex.bridge.service.annotation.OnStart;
import jadex.bridge.service.component.IRequiredServicesFeature;
import jadex.bridge.service.search.ServiceQuery;
import jadex.bridge.service.types.clock.IClockService;
import jadex.commons.future.IFuture;
import jadex.commons.future.IntermediateDefaultResultListener;
import jadex.micro.annotation.Agent;
import jadex.micro.annotation.AgentFeature;
import jadex.micro.annotation.RequiredService;
import jadex.micro.annotation.RequiredServices;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.ZonedDateTime;
import java.util.*;

/**
 * @author Daniel Abitz
 */
@Agent(type = BDIAgentFactory.TYPE)
@RequiredServices(
        @RequiredService(type = TimerService.class)
)
public class TConsumerAgentBDI {

    private static final Logger logger = LoggerFactory.getLogger(TConsumerAgentBDI.class);

    @Agent
    protected IInternalAccess agent;
    @AgentFeature
    protected IArgumentsResultsFeature resultsFeature;
    @AgentFeature
    protected IExecutionFeature execFeature;
    @AgentFeature
    protected IRequiredServicesFeature reqFeature;
    @AgentFeature
    protected IBDIAgentFeature bdiFeature;

    protected IClockService clock;
    protected TConsumerAgentData data;
    protected TimerService timerService;
    protected Set<SatisfiedNeed> satisfiedNeedSet = new LinkedHashSet<>();
    protected Map<Need, Integer> counter = new HashMap<>();

    @Belief
    protected Set<Need> needSet = new LinkedHashSet<>();
    @Belief
    protected int handleNeedRecurTrigger = 0;

    public TConsumerAgentBDI() {
    }

    private String getName() {
        return data.getName();
    }

    private void log(String msg) {
        logger.trace("[{}] " + msg, getName());
    }

    private ZonedDateTime now() {
        if(data.getContinuousConverter() == null) {
            double nowTicks = clock.getTick();
            return data.getTickConverter().tickToTime(nowTicks);
        } else {
            long nowMs = clock.getTime();
            return data.getContinuousConverter().toTime(nowMs);
        }
    }

    private void init() {
        execFeature.waitForDelay(0, ia -> {
            searchTimerService();
            return IFuture.DONE;
        });
    }

    private void searchTimerService() {
        reqFeature.searchServices(new ServiceQuery<>(TimerService.class, ServiceScope.PLATFORM))
                .addResultListener(new IntermediateDefaultResultListener<TimerService>() {
                    @Override
                    public void intermediateResultAvailable(TimerService result) {
                        log("timerService found");
                        timerService = result;
                        fireNeeds();
                        if(data.getContinuousConverter() == null) {
                            waitTick();
                        } else {
                            waitDelay();
                        }
                    }
                });
    }

    private void fireNeeds() {
        for(Need need: data.getNeeds()) {
            counter.put(need, 0);
            needSet.add(need);
        }
    }

    private void waitDelay() {
        long delay = data.getDelay();
        execFeature.waitForDelay(delay, ia -> {
            if(needSet.size() > 0 && timerService.isValid2().get()) {
                handleNeedRecurTrigger++;
                waitDelay();
            } else {
                finished();
            }
            return IFuture.DONE;
        });
    }

    private void waitTick() {
        long delay = data.getDelay();
        double ticks = data.getTickConverter().delayToTick(delay);
        if(ticks > 1.0) {
            for(double i = 0.0; i < ticks - 1.0; i++) {
                execFeature.waitForTick().get();
            }
        }
        execFeature.waitForTick(ia -> {
            if(needSet.size() > 0 && timerService.isValid2().get()) {
                handleNeedRecurTrigger++;
                waitTick();
            } else {
                finished();
            }
            return IFuture.DONE;
        });
    }

    private AgentGroupWrapper wrapper;
    private void finished() {
        log("finished");
        if(wrapper == null) {
            wrapper = new AgentGroupWrapper(data.getGroup());
        }
        timerService.finished(wrapper);
    }

    //=========================
    //lifecycle
    //=========================

    @OnInit
    protected void onInit() {
        data = (TConsumerAgentData) resultsFeature.getArguments().get("data");
        clock = JadexUtil.getClockService(agent);
        init();
        log("onInit");
    }

    @OnStart
    protected void onStart() {
        log("onStart");
    }

    @OnEnd
    protected void onEnd() {
        resultsFeature.getResults().put("satisfiedNeedSet", new LinkedHashSet<>(satisfiedNeedSet));
        log("onEnd");
    }

    //=========================
    //BDI
    //=========================

    @Goal(recur = true, retry = false)
    protected class NewNeedGoal {

        protected Need need;

        @GoalCreationCondition(factadded = "needSet")
        public NewNeedGoal(Need need) {
            this.need = need;
        }

        public Need getNeed() {
            return need;
        }

        @GoalTargetCondition(beliefs = "handleNeedRecurTrigger")
        public boolean checkTarget() {
            return !needSet.contains(need);
        }

        @GoalRecurCondition(beliefs = "handleNeedRecurTrigger")
        public boolean checkRecur() {
            return needSet.contains(need);
        }
    }

    @Plan(trigger = @Trigger(goals = NewNeedGoal.class))
    protected void handleNewNeedGoal(NewNeedGoal goal) {
        Need need = goal.getNeed();
        ZonedDateTime now = now();
        counter.put(need, counter.get(need) + 1);
        log("(" + need._name + ") try find product... (counter: " + counter.get(need) + ", time: " + now + ")");
        if(!tryFindProduct()) {
            log("(" + need._name + ") try find product failed");
            throw new PlanFailureException();
        }
        FixedProduct product = findProduct(need);
        if(product == null) {
            log("(" + need._name + ") no product found");
            throw new PlanFailureException();
        } else {
            log("(" + need._name + ") product found: " + product._name);
            SatisfiedNeed satisfiedNeed = new SatisfiedNeed(now, need, product, getName(), data.getGroup());
            satisfiedNeedSet.add(satisfiedNeed);
            needSet.remove(need);
        }
    }

    private boolean tryFindProduct() {
        double v = data.getRandom().nextDouble();
        return v < data.getGroup().adoptionRate;
    }

    private FixedProduct findProduct(Need need) {
        Set<FixedProduct> products = data.getProducts();
        for(FixedProduct product: products) {
            ProductGroup group = product.group;
            for(Need satNeed: group.needsSatisfied) {
                if(Objects.equals(satNeed, need)) {
                    return product;
                }
            }
        }
        return null;
    }
}
