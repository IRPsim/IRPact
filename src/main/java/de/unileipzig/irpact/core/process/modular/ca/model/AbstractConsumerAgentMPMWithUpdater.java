package de.unileipzig.irpact.core.process.modular.ca.model;

import de.unileipzig.irpact.commons.time.Timestamp;
import de.unileipzig.irpact.core.misc.MissingDataException;
import de.unileipzig.irpact.core.process.modular.ModulePlan;
import de.unileipzig.irpact.core.process.modular.ca.ConsumerAgentData;
import de.unileipzig.irpact.core.process.modular.ca.SimpleConsumerAgentData;
import de.unileipzig.irpact.core.process.modular.ca.updater.EndOfYearEvaluator;
import de.unileipzig.irpact.core.process.modular.ca.updater.MidYearUpdater;
import de.unileipzig.irpact.core.process.modular.ca.updater.StartOfYearAdjuster;
import de.unileipzig.irpact.core.simulation.SimulationEnvironment;
import de.unileipzig.irpact.core.simulation.tasks.SyncTask;
import de.unileipzig.irpact.core.util.AdoptionPhase;

import java.util.*;

/**
 * @author Daniel Abitz
 */
public abstract class AbstractConsumerAgentMPMWithUpdater extends AbstractConsumerAgentMPM {

    protected static final long WEEK27 = 27;

    protected final StartOfYearAdjuster startOfYear = new StartOfYearAdjuster("startOfYear");
    protected final MidYearUpdater midYear = new MidYearUpdater("midYear");
    protected final EndOfYearEvaluator endOfYear = new EndOfYearEvaluator("endOfYear");

    protected final Map<Integer, Timestamp> week27Map = new HashMap<>();
    protected final Set<ConsumerAgentData> consumerAgentData = new HashSet<>();

    protected boolean yearChange = false;

    protected AbstractConsumerAgentMPMWithUpdater() {
    }

    @Override
    public void setEnvironment(SimulationEnvironment environment) {
        super.setEnvironment(environment);
        startOfYear.setEnvironment(environment);
        midYear.setEnvironment(environment);
        endOfYear.setEnvironment(environment);
    }

    @Override
    protected void peekNewPlan(SimpleConsumerAgentData data) {
        consumerAgentData.add(data);
    }

    @Override
    public void handleRestoredPlan(ModulePlan plan) {
        if(isValidPlanForThisModule(plan)) {
            handleValidPlanForThisModule(plan);
        }
    }

    @Override
    public AdoptionPhase determine(Timestamp ts) {
        if(isYearChange()) {
            return AdoptionPhase.END_START;
        } else {
            if(isBeforeWeek27(ts)) {
                return AdoptionPhase.START_MID;
            } else {
                return AdoptionPhase.MID_END;
            }
        }
    }

    protected void setYearChange(boolean value) {
        this.yearChange = value;
    }

    public boolean isYearChange() {
        return yearChange;
    }

    public boolean isBeforeWeek27(Timestamp ts) {
        Timestamp currentYearWeek27 = getCurrentWeek27(ts);
        return ts.isBefore(currentYearWeek27);
    }

    private Timestamp getCurrentWeek27(Timestamp ts) {
        Timestamp w27 = week27Map.get(ts.getYear());
        if(w27 == null) {
            return syncGetCurrentWeek27(ts);
        } else {
            return w27;
        }
    }
    private synchronized Timestamp syncGetCurrentWeek27(Timestamp ts) {
        Timestamp w27 = week27Map.get(ts.getYear());
        if(w27 == null) {
            w27 = getTimeModel().atWeek(ts.getYear(), WEEK27);
            week27Map.put(ts.getYear(), w27);
        }
        return w27;
    }

    protected boolean isValidPlanForThisModule(ModulePlan plan) {
        return plan instanceof ConsumerAgentData;
    }

    protected void handleValidPlanForThisModule(ModulePlan plan) {
        consumerAgentData.add((ConsumerAgentData) plan);
    }

    protected Collection<ConsumerAgentData> getConsumerAgentData() {
        return consumerAgentData;
    }

    @Override
    public void preSimulationStart() throws MissingDataException {
        super.preSimulationStart();
        setupTasks();
    }

    protected void setupTasks() {
        int firstyear = getTimeModel().getFirstSimulationYear();
        int lastYear = getTimeModel().getLastSimulationYear();

        trace("create syncronisation tasks for process model '{}' ", getName());

        SyncTask startOfYearTask = createStartOfYearTask("StartOfYearTask_" + getName());
        trace("create start of year task '{}'", startOfYearTask.getName());
        getLifeCycleControl().registerSyncTaskAsFirstAnnualAction(startOfYearTask);

        SyncTask endOfYearTask = createEndOfYear("EndOfYearTask_" + getName());
        trace("create end of year task '{}'", endOfYearTask.getName());
        getLifeCycleControl().registerSyncTaskAsLastAnnualAction(endOfYearTask);

        for(int y = firstyear; y <= lastYear; y++) {
            Timestamp tsWeek27 = getTimeModel().atWeek(y, WEEK27);
            SyncTask taskWeek27 = createWeek27Task("MidYear_" + getName() + "_" + y);
            trace("created mid year task (27th week) '{}'", taskWeek27.getName());
            environment.getLifeCycleControl().registerSyncTaskAsFirstAction(tsWeek27, taskWeek27);
        }
    }

    protected StartOfYearAdjuster getStartOfYear() {
        return startOfYear;
    }

    protected MidYearUpdater getMidYear() {
        return midYear;
    }

    protected EndOfYearEvaluator getEndOfYear() {
        return endOfYear;
    }

    protected SyncTask createStartOfYearTask(String name) {
        return new SyncTask() {
            @Override
            public String getName() {
                return name;
            }

            @Override
            public void run() {
                trace("run 'createStartOfYearTask'");

                StartOfYearAdjuster adjuster = getStartOfYear();
                Collection<ConsumerAgentData> dataSet = getConsumerAgentData();
                for(ConsumerAgentData data: dataSet) {
                    try {
                        adjuster.update(data);
                    } catch (Throwable t) {
                        error("'createStartOfYearTask' failed for agent: " + data.getAgent().getName(), t);
                    }
                }
            }
        };
    }

    protected SyncTask createWeek27Task(String name) {
        return new SyncTask() {
            @Override
            public String getName() {
                return name;
            }

            @Override
            public void run() {
                trace("run 'createWeek27Task'");

                MidYearUpdater updater = getMidYear();
                Collection<ConsumerAgentData> dataSet = getConsumerAgentData();
                for(ConsumerAgentData data: dataSet) {
                    try {
                        updater.update(data);
                    } catch (Throwable t) {
                        error("'createWeek27Task' failed for agent: " + data.getAgent().getName(), t);
                    }
                }
            }
        };
    }

    protected SyncTask createEndOfYear(String name) {
        return new SyncTask() {
            @Override
            public String getName() {
                return name;
            }

            @Override
            public void run() {
                trace("run 'createEndOfYear'");

                EndOfYearEvaluator evaluator = getEndOfYear();
                Collection<ConsumerAgentData> dataSet = getConsumerAgentData();
                setYearChange(true);
                for(ConsumerAgentData data: dataSet) {
                    try {
                        evaluator.update(data);
                    } catch (Throwable t) {
                        error("'createEndOfYear' failed for agent: " + data.getAgent().getName(), t);
                    }
                }
                setYearChange(false);
            }
        };
    }
}
