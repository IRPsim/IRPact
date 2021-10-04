package de.unileipzig.irpact.core.process2.modular.ca.ra.modules.evalra;

import de.unileipzig.irpact.core.logging.IRPLogging;
import de.unileipzig.irpact.core.process2.PostAction2;
import de.unileipzig.irpact.core.process2.modular.ca.ConsumerAgentData2;
import de.unileipzig.irpact.core.process2.modular.ca.ra.modules.RAHelperAPI2;
import de.unileipzig.irpact.core.process2.modular.ca.ra.RAStage2;
import de.unileipzig.irpact.core.process2.modular.ca.ra.modules.core.AbstractUniformCAMultiModule1_2;
import de.unileipzig.irpact.core.process2.modular.ca.ra.modules.core.RAEvaluationModule2;
import de.unileipzig.irpact.core.process2.modular.modules.core.CalculationModule2;
import de.unileipzig.irpact.core.process2.util.Sign;
import de.unileipzig.irpact.core.simulation.SimulationEnvironment;
import de.unileipzig.irptools.util.log.IRPLogger;

import java.util.List;

/**
 * @author Daniel Abitz
 */
public class AdoptionDeciderModule
        extends AbstractUniformCAMultiModule1_2<RAStage2, Number, CalculationModule2<ConsumerAgentData2>>
        implements RAEvaluationModule2<ConsumerAgentData2>, RAHelperAPI2 {

    private static final IRPLogger LOGGER = IRPLogging.getLogger(AdoptionDeciderModule.class);

    protected double threshold;
    protected Sign sign;
    protected RAStage2 validResult;
    protected RAStage2 invalidResult;

    @Override
    public IRPLogger getDefaultLogger() {
        return LOGGER;
    }

    @Override
    protected void validateSelf() throws Throwable {
        if(Sign.isNotValid(sign)) {
            throw new IllegalArgumentException("invalid sign: " + sign);
        }
    }

    @Override
    protected void initalizeSelf(SimulationEnvironment environment) throws Throwable {
    }

    public void setThreshold(double threshold) {
        this.threshold = threshold;
    }

    public double getThreshold() {
        return threshold;
    }

    public void setValidResult(RAStage2 validResult) {
        this.validResult = validResult;
    }

    public RAStage2 getValidResult() {
        return validResult;
    }

    public void setInvalidResult(RAStage2 invalidResult) {
        this.invalidResult = invalidResult;
    }

    public RAStage2 getInvalidResult() {
        return invalidResult;
    }

    @Override
    public RAStage2 apply(ConsumerAgentData2 input, List<PostAction2> actions) throws Throwable {
        traceModuleInfo(input);

        double value = getNonnullSubmodule().calculate(input, actions);
        boolean eval = sign.eval(value, threshold);
        if(eval) {
            return validResult;
        } else {
            return invalidResult;
        }
    }
}
