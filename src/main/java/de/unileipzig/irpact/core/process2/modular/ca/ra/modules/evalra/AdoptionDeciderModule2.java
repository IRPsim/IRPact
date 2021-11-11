package de.unileipzig.irpact.core.process2.modular.ca.ra.modules.evalra;

import de.unileipzig.irpact.core.logging.IRPLogging;
import de.unileipzig.irpact.core.process2.PostAction2;
import de.unileipzig.irpact.core.process2.modular.ca.ConsumerAgentData2;
import de.unileipzig.irpact.core.process2.modular.ca.ra.RAHelperAPI2;
import de.unileipzig.irpact.core.process2.modular.ca.ra.RAStage2;
import de.unileipzig.irpact.core.process2.modular.ca.ra.modules.core.AbstractUniformCAMultiModule1_2;
import de.unileipzig.irpact.core.process2.modular.ca.ra.modules.core.RAEvaluationModule2;
import de.unileipzig.irpact.core.simulation.SimulationEnvironment;
import de.unileipzig.irptools.util.log.IRPLogger;

import java.util.List;

/**
 * @author Daniel Abitz
 */
public class AdoptionDeciderModule2
        extends AbstractUniformCAMultiModule1_2<RAStage2, RAStage2, RAEvaluationModule2<ConsumerAgentData2>>
        implements RAEvaluationModule2<ConsumerAgentData2>, RAHelperAPI2 {

    private static final IRPLogger LOGGER = IRPLogging.getLogger(AdoptionDeciderModule2.class);

    protected boolean enabled = false;
    protected double threshold = -1.0;

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

    @Override
    protected ConsumerAgentData2 castInput(ConsumerAgentData2 input) {
        return input;
    }

    @Override
    protected void initializeNewInputSelf(ConsumerAgentData2 input) throws Throwable {
    }

    public void setThreshold(double threshold) {
        this.threshold = threshold;
    }

    public double getThreshold() {
        return threshold;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public boolean isEnabled() {
        return enabled;
    }

    @Override
    public RAStage2 apply(ConsumerAgentData2 input, List<PostAction2> actions) throws Throwable {
        traceModuleCall(input);

        RAStage2 stage = getNonnullSubmodule().apply(input, actions);
        if(isEnabled() && stage == RAStage2.ADOPTED) {
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
