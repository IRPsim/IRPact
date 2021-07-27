package de.unileipzig.irpact.core.process.modular.ca.components.base;

import de.unileipzig.irpact.commons.checksum.ChecksumComparable;
import de.unileipzig.irpact.commons.util.Rnd;
import de.unileipzig.irpact.core.logging.IRPLogging;
import de.unileipzig.irpact.core.product.Product;
import de.unileipzig.irptools.util.log.IRPLogger;

/**
 * @author Daniel Abitz
 */
public final class DummyModule extends AbstractConsumerAgentModule {

    private static final IRPLogger LOGGER = IRPLogging.getLogger(DummyModule.class);

    @Override
    public IRPLogger getDefaultLogger() {
        return LOGGER;
    }

    @Override
    public int getChecksum() {
        return ChecksumComparable.DEFAULT_NONNULL_CHECKSUM;
    }

    @Override
    public void handleNewProduct(Product product, Rnd rnd) {
        super.handleNewProduct(product, rnd);
    }
}
