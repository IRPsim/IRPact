package de.unileipzig.irpact.jadex.examples.deprecated.old.adoptProductWithService;

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
import jadex.bridge.service.annotation.Service;
import jadex.bridge.service.component.IRequiredServicesFeature;
import jadex.bridge.service.search.ServiceQuery;
import jadex.commons.Tuple2;
import jadex.commons.future.*;
import jadex.micro.annotation.*;
import org.slf4j.Logger;

import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Service
@ProvidedServices(
        @ProvidedService(type = AdoptProductService.class)
)
@RequiredServices(
        @RequiredService(type = OfferProductService.class)
)
@Agent(type = BDIAgentFactory.TYPE)
public class ConsumerAgentBDI implements AdoptProductService {

    @Agent
    protected IInternalAccess agent;
    @AgentFeature
    protected IArgumentsResultsFeature resultsFeature;
    @AgentFeature
    protected IBDIAgentFeature bdiFeature;
    @AgentFeature
    protected IExecutionFeature execFeature;
    @AgentFeature
    protected IRequiredServicesFeature reqFeature;

    @Belief
    protected Set<String> needs = new HashSet<>();
    @Belief
    protected Set<String> anouncedProducts = new HashSet<>();
    //args
    protected String name;
    protected Logger logger;

    //=========================
    //Adopt
    //=========================

    @Goal(recur = true)
    public class AdoptProductGoal {

        @GoalParameter
        protected String product;

        @GoalCreationCondition(factadded = "needs")
        public AdoptProductGoal(String product) {
            this.product = product;
        }

        public String getProduct() {
            return product;
        }

        @GoalRecurCondition(beliefs = "anouncedProducts")
        public boolean checkRecur() {
            //return true; //vllt contains nutzen?
            return anouncedProducts.contains(product); //funzt!
        }
    }

    @Plan(trigger = @Trigger(goals = AdoptProductGoal.class))
    protected void adoptProduct(String need) {
        logger.debug("{} try adopt need '{}'", sayMyName(), need);
        Collection<OfferProductService> companies = reqFeature.searchServices(new ServiceQuery<>(OfferProductService.class, ServiceScope.PLATFORM))
                .get();
        logger.debug("{} found offerer {}", sayMyName(), companies.size());
        if(companies.isEmpty()) {
            logger.debug("{} no offer found", sayMyName());
            throw new PlanFailureException(); //recure
        }
        //hol alle potentiellen kaeufer
        Future<Collection<Tuple2<OfferProductService, Boolean>>> cfp = new Future<>();
        CollectionResultListener<Tuple2<OfferProductService, Boolean>> crl = new CollectionResultListener<>(companies.size(), new DelegationResultListener<>(cfp));
        for(OfferProductService company: companies) {
            company.hasProduct(need).addResultListener(new IResultListener<Boolean>() {
                @Override
                public void exceptionOccurred(Exception exception) {
                    crl.exceptionOccurred(exception);
                }

                @Override
                public void resultAvailable(Boolean result) {
                    logger.debug("{} offer found: '{}'", sayMyName(), company.getOfferName());
                    crl.resultAvailable(new Tuple2<>(company, result));
                }
            });
        }
        @SuppressWarnings("unchecked")
        Tuple2<OfferProductService, Boolean>[] comps = cfp.get().toArray(new Tuple2[0]);
        logger.debug("{} comps: {}", sayMyName(), comps.length);
        for(Tuple2<OfferProductService, Boolean> tuple: comps) {
            if(tuple.getSecondEntity()) {
                logger.debug("{}, offer accepted, need '{}' removed, offerer {}", sayMyName(), need, tuple.getFirstEntity().getOfferName());
                needs.remove(need);
                return;
            }
        }
        logger.debug("{} no offer...", sayMyName());
        throw new PlanFailureException();
    }

    //=========================
    //Agent
    //=========================

    public ConsumerAgentBDI() {
    }

    private String sayMyName() {
        return "[" + name + "]";
    }

    protected void initArgs(Map<String, Object> args) {
        name = (String) args.get("name");
        logger = (Logger) args.get("logger");
    }

    @OnInit
    protected void created() {
        initArgs(resultsFeature.getArguments());
        logger.debug("{} created", sayMyName());
    }

    @OnStart
    protected void body() {
        logger.debug("{} body", sayMyName());

        String initialNeed = (String) resultsFeature.getArguments()
                .get("need");
        if(initialNeed != null) {
            needs.add(initialNeed);
        }

        //erstes neues need
        /*
        execFeature.waitForDelay(7000, ia -> {
            needs.add("baum");
            return IFuture.DONE;
        });
        */
    }

    @OnEnd
    protected void killed() {
        logger.debug("{} killed", sayMyName());

        resultsFeature.getResults()
                .put("needs", needs);
    }

    @Override
    public String getAdopterName() {
        return name;
    }

    @Override
    public IFuture<Void> anounced(String product) {
        logger.debug("{} OH! new product '{}'", sayMyName(), product);
        anouncedProducts.add(product);
        return IFuture.DONE;
    }
}
