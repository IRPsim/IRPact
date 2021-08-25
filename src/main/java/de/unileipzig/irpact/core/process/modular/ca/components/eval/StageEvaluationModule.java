package de.unileipzig.irpact.core.process.modular.ca.components.eval;

import de.unileipzig.irpact.commons.checksum.Checksums;
import de.unileipzig.irpact.core.logging.IRPLogging;
import de.unileipzig.irpact.core.logging.IRPSection;
import de.unileipzig.irpact.core.process.modular.ca.AdoptionResult;
import de.unileipzig.irpact.core.process.modular.ca.ConsumerAgentData;
import de.unileipzig.irpact.core.process.modular.ca.Stage;
import de.unileipzig.irpact.core.process.modular.ca.components.ConsumerAgentEvaluationModule;
import de.unileipzig.irpact.core.process.modular.ca.components.base.AbstractConsumerAgentModuleWithNSubModules;
import de.unileipzig.irpact.core.process.PostAction;
import de.unileipzig.irptools.util.log.IRPLogger;

import java.util.List;

/**
 * @author Daniel Abitz
 */
public class StageEvaluationModule
        extends AbstractConsumerAgentModuleWithNSubModules
        implements ConsumerAgentEvaluationModule {

    private static final IRPLogger LOGGER = IRPLogging.getLogger(StageEvaluationModule.class);

    protected static final int NUMBER_OF_MODULES = 5;
    protected static final int INDEX_AWARENESS_MODULE = 0;
    protected static final int INDEX_FEASIBILITY_MODULE = 1;
    protected static final int INDEX_DECISION_MAKING_MODULE = 2;
    protected static final int INDEX_ADOPTED_MODULE = 3;
    protected static final int INDEX_IMPEDED_MODULE = 4;

    public StageEvaluationModule() {
        super(NUMBER_OF_MODULES);
    }

    @Override
    public IRPLogger getDefaultLogger() {
        return LOGGER;
    }

    @Override
    public IRPSection getDefaultResultSection() {
        return IRPSection.SIMULATION_PROCESS;
    }

    @Override
    public int getChecksum() {
        return Checksums.SMART.getChecksum(
                getAwarenessModule(),
                getFeasibilityModule(),
                getDecisionMakingModule(),
                getAdoptedModule(),
                getImpededModule()
        );
    }

    public void setAwarenessModule(ConsumerAgentEvaluationModule awarenessModule) {
        setSubModule(INDEX_AWARENESS_MODULE, awarenessModule);
    }
    public ConsumerAgentEvaluationModule getAwarenessModule() {
        return getSubModuleAs(INDEX_AWARENESS_MODULE);
    }

    public void setFeasibilityModule(ConsumerAgentEvaluationModule feasibilityModule) {
        setSubModule(INDEX_FEASIBILITY_MODULE, feasibilityModule);
    }
    public ConsumerAgentEvaluationModule getFeasibilityModule() {
        return getSubModuleAs(INDEX_FEASIBILITY_MODULE);
    }

    public void setDecisionMakingModule(ConsumerAgentEvaluationModule decisionMakingModule) {
        setSubModule(INDEX_DECISION_MAKING_MODULE, decisionMakingModule);
    }
    public ConsumerAgentEvaluationModule getDecisionMakingModule() {
        return getSubModuleAs(INDEX_DECISION_MAKING_MODULE);
    }

    public void setAdoptedModule(ConsumerAgentEvaluationModule adoptedModule) {
        setSubModule(INDEX_ADOPTED_MODULE, adoptedModule);
    }
    public ConsumerAgentEvaluationModule getAdoptedModule() {
        return getSubModuleAs(INDEX_ADOPTED_MODULE);
    }

    public void setImpededModule(ConsumerAgentEvaluationModule impededModule) {
        setSubModule(INDEX_IMPEDED_MODULE, impededModule);
    }
    public ConsumerAgentEvaluationModule getImpededModule() {
        return getSubModuleAs(INDEX_IMPEDED_MODULE);
    }

    @Override
    public AdoptionResult evaluate(ConsumerAgentData data, List<PostAction<?>> postActions) throws Throwable {
        Stage stage = data.currentStage();
        if(stage == null) {
            throw new NullPointerException("Stage");
        }

        trace("[{}] start stage evaluation for '{}'", data.getAgent().getName(), stage);

        switch (stage) {
            case PRE_INITIALIZATION:
                throw new IllegalArgumentException("unsupported stage: " + stage);

            case AWARENESS:
                return getAwarenessModule().evaluate(data, postActions);

            case FEASIBILITY:
                return getFeasibilityModule().evaluate(data, postActions);

            case DECISION_MAKING:
                return getDecisionMakingModule().evaluate(data, postActions);

            case ADOPTED:
                return getAdoptedModule().evaluate(data, postActions);

            case IMPEDED:
                return getImpededModule().evaluate(data, postActions);

            default:
                throw new IllegalStateException("unknown stage: " + stage);
        }
    }
}
