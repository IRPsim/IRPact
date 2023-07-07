package de.unileipzig.irpact.core.process.modular.ca.components.calc;

import de.unileipzig.irpact.commons.checksum.Checksums;
import de.unileipzig.irpact.core.logging.IRPLogging;
import de.unileipzig.irpact.core.process.modular.ca.ConsumerAgentData;
import de.unileipzig.irpact.core.process.modular.ca.components.ConsumerAgentCalculationModule;
import de.unileipzig.irpact.core.process.modular.ca.components.base.AbstractConsumerAgentModule;
import de.unileipzig.irptools.util.log.IRPLogger;

/**
 * @author Daniel Abitz
 */
public class AttributeInputModule extends AbstractConsumerAgentModule implements ConsumerAgentCalculationModule {

    private static final IRPLogger LOGGER = IRPLogging.getLogger(AttributeInputModule.class);

    protected String attributeName;
    protected double weight = 1.0;

    public AttributeInputModule() {
    }

    @Override
    public IRPLogger getDefaultLogger() {
        return LOGGER;
    }

    @Override
    public int getChecksum() {
        return Checksums.SMART.getChecksum(
                getName(),
                getWeight(),
                getAttributeName()
        );
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }
    public double getWeight() {
        return weight;
    }

    public void setAttributeName(String attributeName) {
        this.attributeName = attributeName;
    }

    public String getAttributeName() {
        return attributeName;
    }

    public String getValidAttributeName() {
        String name = getAttributeName();
        if(name == null) {
            throw new NullPointerException("AttributeName");
        }
        return name;
    }

    @Override
    public double calculate(ConsumerAgentData input) throws Throwable {
        String name = getValidAttributeName();
        double value = getAttributeHelper().getDouble(input.getAgent(), name, true);
        return getWeight() * value;
    }
}
