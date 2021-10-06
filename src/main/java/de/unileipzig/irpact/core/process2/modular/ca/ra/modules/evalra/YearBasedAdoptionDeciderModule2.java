package de.unileipzig.irpact.core.process2.modular.ca.ra.modules.evalra;

import de.unileipzig.irpact.core.logging.IRPLogging;
import de.unileipzig.irpact.core.process2.PostAction2;
import de.unileipzig.irpact.core.process2.modular.ca.ConsumerAgentData2;
import de.unileipzig.irpact.core.process2.modular.ca.ra.RAStage2;
import de.unileipzig.irpact.core.process2.modular.ca.ra.modules.RAHelperAPI2;
import de.unileipzig.irpact.core.process2.modular.ca.ra.modules.core.AbstractUniformCAMultiModule1_2;
import de.unileipzig.irpact.core.process2.modular.ca.ra.modules.core.RAEvaluationModule2;
import de.unileipzig.irpact.core.simulation.SimulationEnvironment;
import de.unileipzig.irptools.util.log.IRPLogger;

import java.util.List;

/**
 * @author Daniel Abitz
 */
public class YearBasedAdoptionDeciderModule2
        extends AbstractUniformCAMultiModule1_2<RAStage2, RAStage2, RAEvaluationModule2<ConsumerAgentData2>>
        implements RAEvaluationModule2<ConsumerAgentData2>, RAHelperAPI2 {

    private static final IRPLogger LOGGER = IRPLogging.getLogger(YearBasedAdoptionDeciderModule2.class);

    protected boolean enabled = false;
    protected double base = 1.0;
    protected double factor = 1.0;

    @Override
    public IRPLogger getDefaultLogger() {
        return LOGGER;
    }

    @Override
    protected void validateSelf() throws Throwable {
    }

    @Override
    protected void initializeSelf(SimulationEnvironment environment) throws Throwable {
    }

    public void setBase(double base) {
        this.base = base;
    }

    public double getBase() {
        return base;
    }

    public void setFactor(double factor) {
        this.factor = factor;
    }

    public double getFactor() {
        return factor;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public boolean isEnabled() {
        return enabled;
    }

    protected double getThreshold(ConsumerAgentData2 input) {
        return base * Math.pow(factor, getYearDelta(input));
    }

    @Override
    public RAStage2 apply(ConsumerAgentData2 input, List<PostAction2> actions) throws Throwable {
        traceModuleInfo(input);

        RAStage2 stage = getNonnullSubmodule().apply(input, actions);
        if(isEnabled() && stage == RAStage2.ADOPTED) {
            double threshold = getThreshold(input);

            if(threshold < 0.0) {
                trace("[{}]@[{}] threshold == {}: IMPEDED", input.getAgentName(), getName(), threshold);
                return RAStage2.IMPEDED;
            }
            if(threshold >= 1.0) {
                trace("[{}]@[{}] threshold == {}: ADOPTED", input.getAgentName(), getName(), threshold);
                return RAStage2.ADOPTED;
            }

            double adoptDraw = input.rnd().nextDouble();
            boolean doAdopt = adoptDraw < threshold;
            trace("[{}]@[{}] adoptDraw < threshold ({} < {}): {}", input.getAgentName(), getName(), adoptDraw, threshold, doAdopt);
            return doAdopt
                    ? RAStage2.ADOPTED
                    : RAStage2.IMPEDED;
        } else {
            return stage;
        }
    }
}
