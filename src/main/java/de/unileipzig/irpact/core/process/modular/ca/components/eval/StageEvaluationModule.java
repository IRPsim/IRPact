package de.unileipzig.irpact.core.process.modular.ca.components.eval;

import de.unileipzig.irpact.commons.checksum.Checksums;
import de.unileipzig.irpact.core.logging.IRPLogging;
import de.unileipzig.irpact.core.process.modular.ca.AdoptionResult;
import de.unileipzig.irpact.core.process.modular.ca.ConsumerAgentData;
import de.unileipzig.irpact.core.process.modular.ca.Stage;
import de.unileipzig.irpact.core.process.modular.ca.components.ConsumerAgentEvaluationModule;
import de.unileipzig.irpact.core.process.modular.ca.components.ConsumerAgentMultiModule;
import de.unileipzig.irpact.core.process.modular.ca.components.base.AbstractConsumerAgentModuleWithNSubModules;
import de.unileipzig.irptools.util.log.IRPLogger;

/**
 * @author Daniel Abitz
 */
public class StageEvaluationModule
        extends AbstractConsumerAgentModuleWithNSubModules
        implements ConsumerAgentEvaluationModule, ConsumerAgentMultiModule {

    private static final IRPLogger LOGGER = IRPLogging.getLogger(StageEvaluationModule.class);

    protected static final int NUMBER_OF_MODULES = 5;
    protected static final int INDEX_AWARENESS_MODULE = 0;
    protected static final int INDEX_FEASIBILITY_MODULE = 1;
    protected static final int INDEX_DECISION_MAKING_MODULE = 2;
    protected static final int INDEX_ADOPTED_MODULE = 3;
    protected static final int INDEX_IMPEDED_MODULE = 4;

    protected ConsumerAgentEvaluationModule awarenessModule;
    protected ConsumerAgentEvaluationModule feasibilityModule;
    protected ConsumerAgentEvaluationModule decisionMakingModule;
    protected ConsumerAgentEvaluationModule adoptedModule;
    protected ConsumerAgentEvaluationModule impededModule;

    public StageEvaluationModule() {
        super(NUMBER_OF_MODULES);
    }

    @Override
    public IRPLogger getDefaultLogger() {
        return LOGGER;
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
        this.awarenessModule = awarenessModule;
        updateModuleList(INDEX_AWARENESS_MODULE, awarenessModule);
    }
    public ConsumerAgentEvaluationModule getAwarenessModule() {
        return awarenessModule;
    }

    public void setFeasibilityModule(ConsumerAgentEvaluationModule feasibilityModule) {
        this.feasibilityModule = feasibilityModule;
        updateModuleList(INDEX_FEASIBILITY_MODULE, feasibilityModule);
    }
    public ConsumerAgentEvaluationModule getFeasibilityModule() {
        return feasibilityModule;
    }

    public void setDecisionMakingModule(ConsumerAgentEvaluationModule decisionMakingModule) {
        this.decisionMakingModule = decisionMakingModule;
        updateModuleList(INDEX_DECISION_MAKING_MODULE, decisionMakingModule);
    }
    public ConsumerAgentEvaluationModule getDecisionMakingModule() {
        return decisionMakingModule;
    }

    public void setAdoptedModule(ConsumerAgentEvaluationModule adoptedModule) {
        this.adoptedModule = adoptedModule;
        updateModuleList(INDEX_ADOPTED_MODULE, adoptedModule);
    }
    public ConsumerAgentEvaluationModule getAdoptedModule() {
        return adoptedModule;
    }

    public void setImpededModule(ConsumerAgentEvaluationModule impededModule) {
        this.impededModule = impededModule;
        updateModuleList(INDEX_IMPEDED_MODULE, impededModule);
    }
    public ConsumerAgentEvaluationModule getImpededModule() {
        return impededModule;
    }

    @Override
    public AdoptionResult evaluate(ConsumerAgentData data) throws Throwable {
        Stage stage = data.currentStage();
        if(stage == null) {
            throw new NullPointerException("Stage");
        }

        switch (stage) {
            case AWARENESS:
                return evaluateAwareness(data);

            case FEASIBILITY:
                return evaluateFeasibility(data);

            case DECISION_MAKING:
                return evaluateDecisionMaking(data);

            case ADOPTED:
                return evaluateAdopted(data);


            case IMPEDED:
                return evaluateImpeded(data);

            default:
                throw new IllegalStateException("unknown stage: " + stage);
        }
    }

    public AdoptionResult evaluateAwareness(ConsumerAgentData data) throws Throwable {
        return getAwarenessModule().evaluate(data);
    }

    public AdoptionResult evaluateFeasibility(ConsumerAgentData data) throws Throwable {
        return getFeasibilityModule().evaluate(data);
    }

    public AdoptionResult evaluateDecisionMaking(ConsumerAgentData data) throws Throwable {
        return getDecisionMakingModule().evaluate(data);
    }

    public AdoptionResult evaluateAdopted(ConsumerAgentData data) throws Throwable {
        return getAdoptedModule().evaluate(data);
    }

    public AdoptionResult evaluateImpeded(ConsumerAgentData data) throws Throwable {
        return getImpededModule().evaluate(data);
    }
}
