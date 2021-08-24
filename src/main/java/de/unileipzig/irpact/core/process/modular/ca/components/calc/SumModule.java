package de.unileipzig.irpact.core.process.modular.ca.components.calc;

import de.unileipzig.irpact.commons.checksum.Checksums;
import de.unileipzig.irpact.core.logging.IRPLogging;
import de.unileipzig.irpact.core.process.modular.ca.ConsumerAgentData;
import de.unileipzig.irpact.core.process.modular.ca.components.ConsumerAgentCalculationModule;
import de.unileipzig.irpact.core.process.modular.ca.components.base.AbstractConsumerAgentModule;
import de.unileipzig.irptools.util.log.IRPLogger;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Daniel Abitz
 */
public class SumModule extends AbstractConsumerAgentModule implements ConsumerAgentCalculationModule {

    private static final IRPLogger LOGGER = IRPLogging.getLogger(SumModule.class);

    protected final List<ConsumerAgentCalculationModule> MODULE_LIST = new ArrayList<>();
    protected double weight = 1;

    public SumModule() {
        super();
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
                getModules()
        );
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }
    public double getWeight() {
        return weight;
    }

    public void addModule(ConsumerAgentCalculationModule module) {
        MODULE_LIST.add(module);
    }

    public List<ConsumerAgentCalculationModule> getModules() {
        return MODULE_LIST;
    }

    @Override
    public double calculate(ConsumerAgentData input) throws Throwable {
        double sum = 0.0;
        for(ConsumerAgentCalculationModule module: getModules()) {
            sum += module.calculate(input);
        }
        return getWeight() * sum;
    }
}
