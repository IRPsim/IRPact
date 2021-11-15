package de.unileipzig.irpact.core.process2.handler;

import de.unileipzig.irpact.core.logging.IRPLogging;
import de.unileipzig.irpact.core.logging.LoggingHelper;
import de.unileipzig.irpact.core.process2.uncert.UncertaintySupplier;
import de.unileipzig.irpact.core.simulation.SimulationEnvironment;
import de.unileipzig.irptools.util.log.IRPLogger;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Daniel Abitz
 */
public class UncertaintySupplierInitializer
        extends AbstractInitializationHandler
        implements InitializationHandler, LoggingHelper {

    private static final IRPLogger LOGGER = IRPLogging.getLogger(UncertaintySupplierInitializer.class);

    protected List<UncertaintySupplier> suppliers = new ArrayList<>();

    @Override
    public IRPLogger getDefaultLogger() {
        return LOGGER;
    }

    public void add(UncertaintySupplier supplier) {
        suppliers.add(supplier);
    }

    @Override
    public void initalize(SimulationEnvironment environment) throws Throwable {
        trace("[{}] initalize {} suppliers", getName(), suppliers.size());
        for(UncertaintySupplier supplier: suppliers) {
            trace("[{}] initalize '{}'" , getName(), supplier.getName());
            supplier.initalize();
        }
    }
}
