package de.unileipzig.irpact.core.process2.modular.reevaluate;

import de.unileipzig.irpact.core.logging.IRPLogging;
import de.unileipzig.irpact.core.logging.LoggingHelper;
import de.unileipzig.irpact.core.process2.PostAction2;
import de.unileipzig.irpact.core.process2.uncert.UncertaintySupplier;
import de.unileipzig.irpact.core.simulation.SimulationEnvironment;
import de.unileipzig.irptools.util.log.IRPLogger;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Daniel Abitz
 */
public class UncertaintyReevaluator<I> extends AbstractReevaluator<I> implements LoggingHelper {

    private static final IRPLogger LOGGER = IRPLogging.getLogger(UncertaintyReevaluator.class);

    protected List<UncertaintySupplier> suppliers = new ArrayList<>();

    public UncertaintyReevaluator() {
    }

    @Override
    public IRPLogger getDefaultLogger() {
        return LOGGER;
    }

    public void addSupplier(UncertaintySupplier supplier) {
        suppliers.add(supplier);
    }

    @Override
    public void initializeReevaluator(SimulationEnvironment environment) throws Throwable {
    }

    @Override
    public boolean reevaluateGlobal() {
        return true;
    }

    @Override
    public void reevaluate() throws Throwable {
        trace("[{}] update {} suppliers", getName(), suppliers.size());
        for(UncertaintySupplier supplier: suppliers) {
            trace("[{}] update '{}'" , getName(), supplier.getName());
            supplier.update();
        }
    }

    @Override
    public boolean reevaluateIndividual() {
        return false;
    }

    @Override
    public void reevaluate(I input, List<PostAction2> actions) throws Throwable {
    }
}
