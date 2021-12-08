package de.unileipzig.irpact.core.process2.modular.ca.ra.modules.evalra;

import de.unileipzig.irpact.core.logging.IRPLogging;
import de.unileipzig.irpact.core.process2.PostAction2;
import de.unileipzig.irpact.core.process2.modular.ca.ConsumerAgentData2;
import de.unileipzig.irpact.core.process2.modular.ca.ra.RAStage2;
import de.unileipzig.irpact.core.process2.modular.ca.ra.RAHelperAPI2;
import de.unileipzig.irpact.core.process2.modular.ca.ra.modules.core.AbstractUniformCAMultiModule1_2;
import de.unileipzig.irpact.core.process2.modular.ca.ra.modules.core.RAEvaluationModule2;
import de.unileipzig.irpact.core.simulation.SimulationEnvironment;
import de.unileipzig.irptools.util.log.IRPLogger;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Daniel Abitz
 */
public class YearBasedAdoptionDeciderModule2
        extends AbstractUniformCAMultiModule1_2<RAStage2, RAStage2, RAEvaluationModule2<ConsumerAgentData2>>
        implements RAEvaluationModule2<ConsumerAgentData2>, RAHelperAPI2 {

    private static final IRPLogger LOGGER = IRPLogging.getLogger(YearBasedAdoptionDeciderModule2.class);

    protected Map<ConsumerAgentData2, Integer> adoptionYears;
    protected boolean enabled = true;
    protected double factor = 1.0;
    protected double base = 1.0;

    public YearBasedAdoptionDeciderModule2() {
        this(new ConcurrentHashMap<>());
    }

    public YearBasedAdoptionDeciderModule2(Map<ConsumerAgentData2, Integer> adoptionYears) {
        this.adoptionYears = adoptionYears;
    }

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

    @Override
    protected void setupSelf(SimulationEnvironment environment) throws Throwable {
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

    protected double getThreshold(int delta) {
        return base * Math.pow(factor, delta);
    }

    protected int getAdoptionYear(ConsumerAgentData2 input) {
        Integer year = adoptionYears.get(input);
        if(year == null) {
            year = getCurrentYear(input);
            adoptionYears.put(input, year);
        }
        return year;
    }

    @Override
    public RAStage2 apply(ConsumerAgentData2 input, List<PostAction2> actions) throws Throwable {
        traceModuleCall(input);

        RAStage2 stage = getNonnullSubmodule().apply(input, actions);
        if(isEnabled() && stage == RAStage2.ADOPTED) {
            int adoptionYear = getAdoptionYear(input);
            int currentYear = getCurrentYear(input);
            int delta = currentYear - adoptionYear;
            double threshold = getThreshold(delta);

            if(threshold < 0.0) {
                trace("[{}]@[{}] threshold == {}: (auto) IMPEDED", input.getAgentName(), getName(), threshold);
                return RAStage2.IMPEDED;
            }

            if(threshold >= 1.0) {
                trace("[{}]@[{}] threshold == {}: (auto) ADOPTED", input.getAgentName(), getName(), threshold);
                return RAStage2.ADOPTED;
            }

            double adoptDraw = input.rnd().nextDouble();
            boolean doAdopt = adoptDraw < threshold;
            trace(
                    "[{}]@[{}] adoptDraw < threshold ({} < {} (={}*pow({},{}), currentYear={}, adoptionYear={})): {}",
                    input.getAgentName(), getName(),
                    adoptDraw, threshold,
                    base, factor, delta,
                    currentYear, adoptionYear,
                    doAdopt
            );
            return doAdopt
                    ? RAStage2.ADOPTED
                    : RAStage2.IMPEDED;
        } else {
            return stage;
        }
    }
}
