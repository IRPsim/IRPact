package de.unileipzig.irpact.core.process2.modular.reevaluate;

import de.unileipzig.irpact.core.process2.PostAction2;
import de.unileipzig.irpact.core.simulation.SimulationEnvironment;

import java.util.List;

/**
 * @author Daniel Abitz
 */
public class ReevaluatorWrapper<I> extends AbstractReevaluator<I> {

    protected Reevaluator<I> reevaluator;

    public void setReevaluator(Reevaluator<I> reevaluator) {
        this.reevaluator = reevaluator;
    }

    public Reevaluator<I> getReevaluator() {
        return reevaluator;
    }

    @Override
    public void initializeReevaluator(SimulationEnvironment environment) throws Throwable {
        reevaluator.initializeReevaluator(environment);
    }

    @Override
    public boolean reevaluateGlobal() {
        return reevaluator.reevaluateGlobal();
    }

    @Override
    public void reevaluate() throws Throwable {
        reevaluator.reevaluate();
    }

    @Override
    public boolean reevaluateIndividual() {
        return reevaluator.reevaluateIndividual();
    }

    @Override
    public void reevaluate(I input, List<PostAction2> actions) throws Throwable {
        reevaluator.reevaluate(input, actions);
    }
}
