package de.unileipzig.irpact.core.process2.modular.ca;

import de.unileipzig.irpact.commons.NameableBase;
import de.unileipzig.irpact.commons.exception.InitializationException;
import de.unileipzig.irpact.commons.time.Timestamp;
import de.unileipzig.irpact.commons.util.SetSupplier;
import de.unileipzig.irpact.core.agent.Agent;
import de.unileipzig.irpact.core.agent.consumer.ConsumerAgent;
import de.unileipzig.irpact.core.logging.IRPLogging;
import de.unileipzig.irpact.core.logging.LoggingHelper;
import de.unileipzig.irpact.core.misc.MissingDataException;
import de.unileipzig.irpact.core.need.Need;
import de.unileipzig.irpact.core.process2.PostAction2;
import de.unileipzig.irpact.core.process2.ProcessPlanResult2;
import de.unileipzig.irpact.core.process2.handler.InitializationHandler;
import de.unileipzig.irpact.core.process2.modular.MapBasedSharedModuleData;
import de.unileipzig.irpact.core.process2.modular.ModularProcessPlan2;
import de.unileipzig.irpact.core.process2.modular.SharedModuleData;
import de.unileipzig.irpact.core.process2.modular.ca.ra.BasicConsumerAgentData2;
import de.unileipzig.irpact.core.process2.modular.ca.ra.RAHelperAPI2;
import de.unileipzig.irpact.core.process2.modular.modules.core.Module2;
import de.unileipzig.irpact.core.process2.modular.modules.core.PlanEvaluationModule2;
import de.unileipzig.irpact.core.process2.modular.reevaluate.Reevaluator;
import de.unileipzig.irpact.core.product.Product;
import de.unileipzig.irpact.core.product.handler.NewProductHandler;
import de.unileipzig.irpact.core.simulation.SimulationEnvironment;
import de.unileipzig.irpact.core.simulation.tasks.SyncTask;
import de.unileipzig.irpact.core.util.AdoptionPhase;
import de.unileipzig.irptools.util.log.IRPLogger;

import java.util.*;

/**
 * @author Daniel Abitz
 */
public class BasicCAModularProcessModel2
        extends NameableBase
        implements CAModularProcessModel2, LoggingHelper, RAHelperAPI2 {

    private static final IRPLogger LOGGER = IRPLogging.getLogger(BasicCAModularProcessModel2.class);

    protected static final long WEEK27 = 27;

    protected List<NewProductHandler> newProductHandlers = new ArrayList<>();
    protected List<InitializationHandler> initializationHandlers = new ArrayList<>();
    protected SharedModuleData sharedData = new MapBasedSharedModuleData();
    protected SimulationEnvironment environment;
    protected PlanEvaluationModule2<ConsumerAgentData2> startModule;

    protected Map<Integer, Timestamp> week27Map = new HashMap<>();
    protected boolean yearChange = false;

    protected Set<BasicConsumerAgentData2> plans = SetSupplier.CONCURRENT_HASH.newSet();
    protected List<Reevaluator<ConsumerAgentData2>> startOfYearTasks = new ArrayList<>();
    protected List<Reevaluator<ConsumerAgentData2>> midOfYearTasks = new ArrayList<>();
    protected List<Reevaluator<ConsumerAgentData2>> endOfYearTasks = new ArrayList<>();

    public BasicCAModularProcessModel2() {
    }

    public void setEnvironment(SimulationEnvironment environment) {
        this.environment = environment;
    }

    public void setStartModule(PlanEvaluationModule2<ConsumerAgentData2> startModule) {
        this.startModule = startModule;
    }

    public void addNewProductHandler(NewProductHandler handler) {
        newProductHandlers.add(handler);
    }

    public void addInitializationHandler(InitializationHandler handler) {
        initializationHandlers.add(handler);
    }

    public void addStartOfYearTask(Reevaluator<ConsumerAgentData2> reevaluator) {
        startOfYearTasks.add(reevaluator);
    }

    public void addMidOfYearTask(Reevaluator<ConsumerAgentData2> reevaluator) {
        midOfYearTasks.add(reevaluator);
    }

    public void addEndOfYearTask(Reevaluator<ConsumerAgentData2> reevaluator) {
        endOfYearTasks.add(reevaluator);
    }

    @Override
    public SharedModuleData getSharedData() {
        return sharedData;
    }

    @Override
    public IRPLogger getDefaultLogger() {
        return LOGGER;
    }

    @Override
    public void handleNewProduct2(Product product) {
        trace("[{}] number of InitialAdoptionHandler: {}", getName(), newProductHandlers.size());
        for(NewProductHandler handler: newProductHandlers) {
            trace("[{}] apply InitialAdoptionHandler '{}'", getName(), handler.getName());
            handler.handleProduct(getEnvironment(), product);
        }
    }

    protected ConsumerAgent validateAgent(Agent agent) {
        if(agent instanceof ConsumerAgent) {
            return (ConsumerAgent) agent;
        } else {
            throw new IllegalArgumentException("requires consumer agents");
        }
    }

    @Override
    public ModularProcessPlan2 newPlan2(Agent agent, Need need, Product product) {
        ConsumerAgent consumerAgent = validateAgent(agent);
        createUncertainty(consumerAgent);
        BasicConsumerAgentData2 plan = new BasicConsumerAgentData2(
                environment,
                this,
                environment.getSimulationRandom().deriveInstance(),
                consumerAgent,
                product,
                need
        );
        plans.add(plan);
        return plan;
    }

    protected void createUncertainty(ConsumerAgent agent) {
        getUncertaintyCache().createUncertainty(
                agent,
                getUncertaintyManager()
        );
    }

    @Override
    public AdoptionPhase determinePhase(Timestamp ts) {
        if(yearChange) {
            return AdoptionPhase.END_START;
        } else {
            if(isBeforeWeek27(ts)) {
                return AdoptionPhase.START_MID;
            } else {
                return AdoptionPhase.MID_END;
            }
        }
    }

    protected boolean isBeforeWeek27(Timestamp ts) {
        return ts.isBefore(getCurrentWeek27(ts));
    }

    protected Timestamp getCurrentWeek27(Timestamp ts) {
        Timestamp w27 = week27Map.get(ts.getYear());
        if(w27 == null) {
            return syncGetCurrentWeek27(ts);
        } else {
            return w27;
        }
    }

    protected synchronized Timestamp syncGetCurrentWeek27(Timestamp ts) {
        Timestamp w27 = week27Map.get(ts.getYear());
        if(w27 == null) {
            w27 = environment.getTimeModel().atWeek(ts.getYear(), WEEK27);
            week27Map.put(ts.getYear(), w27);
        }
        return w27;
    }

    @Override
    public SimulationEnvironment getEnvironment() {
        return environment;
    }

    @Override
    public ProcessPlanResult2 execute(ConsumerAgentData2 data, List<PostAction2> actions) throws Throwable {
        return startModule.apply(data, actions);
    }

    @Override
    public void remove(ModularProcessPlan2 plan) {
        if(plan instanceof BasicConsumerAgentData2) {
            plans.remove((BasicConsumerAgentData2) plan);
        }
    }

    @Override
    public void postAgentCreation() throws MissingDataException, InitializationException {
        runInitializationHandler();
        initModules();
    }

    protected void runInitializationHandler() throws InitializationException {
        trace("run initialization handler: {}", initializationHandlers.size());
        for(InitializationHandler handler: initializationHandlers) {
            try {
                trace("run '{}'", handler.getName());
                handler.initalize(environment);
            } catch(InitializationException e) {
                throw e;
            } catch (Throwable e) {
                throw new InitializationException(e);
            }
        }
    }

    protected void initModules() throws InitializationException, MissingDataException {
        if(startModule == null) {
            throw new InitializationException("missing start module");
        }
        if(environment == null) {
            throw new InitializationException("missing environment");
        }

        try {
            trace("test loop...");
            Deque<Module2<?, ?>> modulePath = new LinkedList<>();
            Set<Module2<?, ?>> allModules = new HashSet<>();
            Module2<?, ?> duplicate = startModule.containsLoop(modulePath, allModules);
            trace("... contains loop: {}, modules found: {}", duplicate != null, allModules.size());
            if(!modulePath.isEmpty()) {
                throw new IllegalStateException("path not empty");
            }
            if(duplicate != null) {
                throw new InitializationException("process model contains loop: {}", duplicate.getName());
            }
            trace("start module validation");
            startModule.validate();
            trace("set shared data");
            startModule.setSharedData(sharedData);
            trace("start module initialization");
            startModule.initialize(environment);
        } catch (MissingDataException | InitializationException e) {
            throw e;
        } catch (Throwable e) {
            throw new InitializationException(e);
        }
    }

    @Override
    public void preSimulationStart() throws MissingDataException {
        setupTasks();
    }

    protected void setupTasks() {
        int firstYear = environment.getTimeModel().getFirstSimulationYear();
        int lastYear = environment.getTimeModel().getLastSimulationYear();

        trace("create syncronisation tasks for process model '{}' ", getName());

        SyncTask firstAnnualTask = createStartOfYearTask(getName() + "_FirstAnnual");
        trace("create first annual task '{}'", firstAnnualTask.getName());
        environment.getLifeCycleControl().registerSyncTaskAsFirstAnnualAction(firstAnnualTask);

        SyncTask lastAnnualTask = createEndOfYearTask(getName() + "_LastAnnual");
        trace("create last annual task '{}'", lastAnnualTask.getName());
        environment.getLifeCycleControl().registerSyncTaskAsLastAnnualAction(lastAnnualTask);

        for(int y = firstYear; y <= lastYear; y++) {
            Timestamp tsWeek27 = environment.getTimeModel().atWeek(y, WEEK27);
            SyncTask taskWeek27 = createWeek27Task(getName() + "_MidYear_" + y);
            trace("created mid year task (27th week) '{}'", taskWeek27.getName());
            environment.getLifeCycleControl().registerSyncTaskAsFirstAction(tsWeek27, taskWeek27);
        }
    }


    protected SyncTask createStartOfYearTask(String name) {
        return new SyncTask() {
            @Override
            public String getName() {
                return name;
            }

            @Override
            public void run() {
                trace("run 'createStartOfYearTask': {}", getName());
                try {
                    runStartOfYear();
                } catch (Throwable e) {
                    getEnvironment().getLifeCycleControl().handleFatalError(e);
                }
            }
        };
    }

    protected void runStartOfYear() throws Throwable {
        if(startOfYearTasks.size() > 0) {
            for(Reevaluator<ConsumerAgentData2> task: startOfYearTasks) {
                trace("run task '{}'", task.getName());
                for(BasicConsumerAgentData2 plan: plans) {
                    task.reevaluate(plan, null);
                }
            }
        }
    }

    protected SyncTask createWeek27Task(String name) {
        return new SyncTask() {
            @Override
            public String getName() {
                return name;
            }

            @Override
            public void run() {
                trace("run 'createWeek27Task': {}", getName());
                try {
                    runMidOfYear();
                } catch (Throwable e) {
                    getEnvironment().getLifeCycleControl().handleFatalError(e);
                }
            }
        };
    }

    protected void runMidOfYear() throws Throwable {
        if(midOfYearTasks.size() > 0) {
            for(Reevaluator<ConsumerAgentData2> task: midOfYearTasks) {
                trace("run task '{}'", task.getName());
                for(BasicConsumerAgentData2 plan: plans) {
                    task.reevaluate(plan, null);
                }
            }
        }
    }

    protected SyncTask createEndOfYearTask(String name) {
        return new SyncTask() {
            @Override
            public String getName() {
                return name;
            }

            @Override
            public void run() {
                trace("run 'createEndOfYearTask': {}", getName());
                try {
                    runEndOfYear();
                } catch (Throwable e) {
                    getEnvironment().getLifeCycleControl().handleFatalError(e);
                }
            }
        };
    }

    protected void runEndOfYear() throws Throwable {
        yearChange = true;
        if(endOfYearTasks.size() > 0) {
            for(Reevaluator<ConsumerAgentData2> task: endOfYearTasks) {
                trace("run task '{}'", task.getName());
                for(BasicConsumerAgentData2 plan: plans) {
                    task.reevaluate(plan, null);
                }
            }
        }
        yearChange = false;
    }
}
