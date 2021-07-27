package de.unileipzig.irpact.core.process.modular.ca.components.eval;

import de.unileipzig.irpact.commons.checksum.Checksums;
import de.unileipzig.irpact.commons.time.Timestamp;
import de.unileipzig.irpact.core.logging.IRPLogging;
import de.unileipzig.irpact.core.process.modular.ca.AdoptionResult;
import de.unileipzig.irpact.core.process.modular.ca.ConsumerAgentData;
import de.unileipzig.irpact.core.process.modular.ca.Stage;
import de.unileipzig.irpact.core.process.modular.ca.components.ConsumerAgentCalculationModule;
import de.unileipzig.irpact.core.process.modular.ca.components.ConsumerAgentEvaluationModule;
import de.unileipzig.irpact.core.process.modular.ca.components.ConsumerAgentModule;
import de.unileipzig.irpact.core.process.modular.ca.components.base.AbstractConsumerAgentModule;
import de.unileipzig.irpact.core.process.modular.ca.model.ConsumerAgentMPM;
import de.unileipzig.irpact.core.process.modular.ca.util.AdoptionPhaseDeterminer;
import de.unileipzig.irpact.core.util.AdoptionPhase;
import de.unileipzig.irptools.util.log.IRPLogger;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Daniel Abitz
 */
public class SumThresholdEvaluationModule extends AbstractConsumerAgentModule implements ConsumerAgentEvaluationModule {

    private static final IRPLogger LOGGER = IRPLogging.getLogger(SumThresholdEvaluationModule.class);

    protected final List<ConsumerAgentCalculationModule> subModules = new ArrayList<>();
    protected double threshold;
    protected boolean adoptIfBelowThreshold = true;
    protected AdoptionPhaseDeterminer phaseDeterminer;

    public SumThresholdEvaluationModule() {
    }

    @Override
    public IRPLogger getDefaultLogger() {
        return LOGGER;
    }

    @Override
    public int getChecksum() {
        return Checksums.SMART.getChecksum(
                getName(),
                getThreshold(),
                isAdoptIfBelowThreshold(),
                getSubModules(),
                Checksums.SMART.getNamedChecksum(getPhaseDeterminer())
        );
    }

    @Override
    public void handleMissingParametersRecursively(ConsumerAgentMPM model) {
        for(ConsumerAgentModule subModule: getSubModules()) {
            subModule.handleMissingParametersRecursively(model);
        }

        if(phaseDeterminer == null && model instanceof AdoptionPhaseDeterminer) {
            setPhaseDeterminer((AdoptionPhaseDeterminer) model);
        }
    }

    public void setThreshold(double threshold) {
        this.threshold = threshold;
    }
    public double getThreshold() {
        return threshold;
    }

    public void setAdoptIfBelowThreshold(boolean adoptIfBelowThreshold) {
        this.adoptIfBelowThreshold = adoptIfBelowThreshold;
    }
    public boolean isAdoptIfBelowThreshold() {
        return adoptIfBelowThreshold;
    }

    public List<ConsumerAgentCalculationModule> getSubModules() {
        return subModules;
    }
    public void add(ConsumerAgentCalculationModule subModule) {
        subModules.add(subModule);
    }

    public void setPhaseDeterminer(AdoptionPhaseDeterminer phaseDeterminer) {
        this.phaseDeterminer = phaseDeterminer;
    }
    public AdoptionPhaseDeterminer getPhaseDeterminer() {
        return phaseDeterminer;
    }

    @Override
    public AdoptionResult evaluate(ConsumerAgentData data) throws Throwable {
        double sum = 0.0;
        for(ConsumerAgentCalculationModule subModule: getSubModules()) {
            sum += subModule.calculate(data);
        }

        if(doAdopt(sum)) {
            Timestamp now = now();
            data.getAgent().adopt(data.getNeed(), data.getProduct(), now, determinePhase(now));
            data.updateStage(Stage.ADOPTED);
            return AdoptionResult.ADOPTED;
        } else {
            data.updateStage(Stage.IMPEDED);
            return AdoptionResult.IMPEDED;
        }
    }

    protected boolean doAdopt(double sum) {
        if(isAdoptIfBelowThreshold()) {
            return sum < getThreshold();
        } else {
            return getThreshold() < sum;
        }
    }

    protected AdoptionPhase determinePhase(Timestamp ts) {
        AdoptionPhaseDeterminer determiner = getPhaseDeterminer();
        if(determiner == null) {
            throw new NullPointerException("AdoptionPhaseDeterminer");
        }
        return determiner.determine(ts);
    }
}
