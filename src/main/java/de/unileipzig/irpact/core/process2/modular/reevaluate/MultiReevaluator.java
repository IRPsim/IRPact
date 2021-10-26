package de.unileipzig.irpact.core.process2.modular.reevaluate;

import de.unileipzig.irpact.core.process2.PostAction2;
import de.unileipzig.irpact.core.simulation.SimulationEnvironment;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Daniel Abitz
 */
public class MultiReevaluator<I> extends AbstractReevaluator<I> {

    protected List<Reevaluator<I>> reevaluators;

    public MultiReevaluator() {
        this(new ArrayList<>());
    }

    public MultiReevaluator(List<Reevaluator<I>> reevaluators) {
        this.reevaluators = reevaluators;
    }

    public boolean addReevaluator(Reevaluator<I> reevaluator) {
        return reevaluators.add(reevaluator);
    }

    public List<Reevaluator<I>> getReevaluators() {
        return reevaluators;
    }

    @Override
    public void initializeReevaluator(SimulationEnvironment environment) throws Throwable {
        for(Reevaluator<I> reevaluator: getReevaluators()) {
            reevaluator.initializeReevaluator(environment);
        }
    }

    @Override
    public void reevaluate(I input, List<PostAction2> actions) throws Throwable {
        for(Reevaluator<I> reevaluator: getReevaluators()) {
            reevaluator.reevaluate(input, actions);
        }
    }
}
