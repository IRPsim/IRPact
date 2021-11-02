package de.unileipzig.irpact.core.process2.modular.reevaluate;

import de.unileipzig.irpact.core.logging.IRPLogging;
import de.unileipzig.irpact.core.process2.PostAction2;
import de.unileipzig.irpact.core.process2.modular.HelperAPI2;
import de.unileipzig.irpact.core.process2.modular.SharedModuleData;
import de.unileipzig.irpact.core.simulation.SimulationEnvironment;
import de.unileipzig.irptools.util.log.IRPLogger;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Daniel Abitz
 */
public class MultiReevaluator<I> extends AbstractReevaluator<I> implements HelperAPI2 {

    private static final IRPLogger LOGGER = IRPLogging.getLogger(MultiReevaluator.class);

    protected List<Reevaluator<I>> reevaluators;

    public MultiReevaluator() {
        this(new ArrayList<>());
    }

    public MultiReevaluator(List<Reevaluator<I>> reevaluators) {
        this.reevaluators = reevaluators;
    }

    @Override
    public IRPLogger getDefaultLogger() {
        return LOGGER;
    }

    public boolean addReevaluator(Reevaluator<I> reevaluator) {
        return reevaluators.add(reevaluator);
    }

    public List<Reevaluator<I>> getReevaluators() {
        return reevaluators;
    }

    @Override
    public void setSharedData(SharedModuleData sharedData) {
        super.setSharedData(sharedData);
        for(Reevaluator<I> reevaluator: getReevaluators()) {
            reevaluator.setSharedData(sharedData);
        }
    }

    @Override
    public void initializeReevaluator(SimulationEnvironment environment) throws Throwable {
        traceModuleInitalization();
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
