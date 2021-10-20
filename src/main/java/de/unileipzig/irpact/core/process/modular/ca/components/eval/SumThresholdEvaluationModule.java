package de.unileipzig.irpact.core.process.modular.ca.components.eval;

import de.unileipzig.irpact.commons.checksum.Checksums;
import de.unileipzig.irpact.core.logging.IRPLogging;
import de.unileipzig.irpact.core.process.modular.ca.AdoptionResult;
import de.unileipzig.irpact.core.process.modular.ca.ConsumerAgentData;
import de.unileipzig.irpact.core.process.modular.ca.components.ConsumerAgentCalculationModule;
import de.unileipzig.irpact.core.process.modular.ca.components.ConsumerAgentEvaluationModule;
import de.unileipzig.irpact.core.process.modular.ca.components.base.AbstractConsumerAgentModuleWithGenericSubModules;
import de.unileipzig.irpact.core.process.PostAction;
import de.unileipzig.irptools.util.log.IRPLogger;

import java.util.List;

/**
 * @author Daniel Abitz
 */
public class SumThresholdEvaluationModule
        extends AbstractConsumerAgentModuleWithGenericSubModules<ConsumerAgentCalculationModule>
        implements ConsumerAgentEvaluationModule {

    private static final IRPLogger LOGGER = IRPLogging.getLogger(SumThresholdEvaluationModule.class);

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
                isAcceptIfBelowThreshold(),
                getSubModules()
        );
    }

    protected double threshold = 0;
    public void setThreshold(double threshold) {
        this.threshold = threshold;
    }
    public double getThreshold() {
        return threshold;
    }

    protected boolean acceptIfBelowThreshold = true;
    public void setAcceptIfBelowThreshold(boolean acceptIfBelowThreshold) {
        this.acceptIfBelowThreshold = acceptIfBelowThreshold;
    }
    public boolean isAcceptIfBelowThreshold() {
        return acceptIfBelowThreshold;
    }

    protected boolean adoptIfAccepted = true;
    public void setAdoptIfAccepted(boolean adoptIfAccepted) {
        this.adoptIfAccepted = adoptIfAccepted;
    }
    public boolean isAdoptIfAccepted() {
        return adoptIfAccepted;
    }

    protected boolean impededIfFailed = true;
    public void setImpededIfFailed(boolean impededIfFailed) {
        this.impededIfFailed = impededIfFailed;
    }
    public boolean isImpededIfFailed() {
        return impededIfFailed;
    }

    public List<ConsumerAgentCalculationModule> getSubModules() {
        return MODULE_LIST;
    }
    @Override
    public void addSubModule(ConsumerAgentCalculationModule subModule) {
        super.addSubModule(subModule);
    }

    @Override
    public AdoptionResult evaluate(ConsumerAgentData data, List<PostAction> postActions) throws Throwable {
        double sum = 0.0;
        for(ConsumerAgentCalculationModule subModule: getSubModules()) {
            sum += subModule.calculate(data);
        }

        return determineResult(sum);
    }

    protected AdoptionResult determineResult(double sum) {
        if(isAcceptIfBelowThreshold()) {
            if(sum < getThreshold()) {
                return getPositiveAdoptionResult();
            } else {
                return getNegativeAdoptionResult();
            }
        } else {
            if(sum > getThreshold()) {
                return getPositiveAdoptionResult();
            } else {
                return getNegativeAdoptionResult();
            }
        }
    }

    protected AdoptionResult getPositiveAdoptionResult() {
        return isAdoptIfAccepted() ? AdoptionResult.ADOPTED : AdoptionResult.IN_PROCESS;
    }

    protected AdoptionResult getNegativeAdoptionResult() {
        return isImpededIfFailed() ? AdoptionResult.IMPEDED : AdoptionResult.IN_PROCESS;
    }
}
