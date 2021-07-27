package de.unileipzig.irpact.core.process.modular.ca.model;

import de.unileipzig.irpact.commons.checksum.Checksums;
import de.unileipzig.irpact.commons.util.Rnd;
import de.unileipzig.irpact.core.logging.IRPLogging;
import de.unileipzig.irpact.core.process.modular.ca.components.base.DummyModule;
import de.unileipzig.irpact.core.product.Product;
import de.unileipzig.irptools.util.log.IRPLogger;

/**
 * @author Daniel Abitz
 */
public class SimpleConsumerAgentMPM extends AbstractConsumerAgentMPMWithUpdater {

    private static final IRPLogger LOGGER = IRPLogging.getLogger(SimpleConsumerAgentMPM.class);

    protected Rnd rnd;

    public SimpleConsumerAgentMPM() {
    }

    public void setRnd(Rnd rnd) {
        this.rnd = rnd;
    }

    public Rnd getRnd() {
        return rnd;
    }

    @Override
    public IRPLogger getDefaultLogger() {
        return LOGGER;
    }

    @Override
    public int getChecksum() {
        return Checksums.SMART.getChecksum(
                getName(),
                getRnd()
        );
    }

    @Override
    public void handleNewProduct(Product newProduct) {
        super.handleNewProduct(newProduct);
        DummyModule module = new DummyModule();
        module.handleNewProduct(newProduct, getRnd());
    }
}
