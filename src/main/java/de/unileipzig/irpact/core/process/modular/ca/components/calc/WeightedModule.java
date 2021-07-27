package de.unileipzig.irpact.core.process.modular.ca.components.calc;

import de.unileipzig.irpact.commons.checksum.Checksums;
import de.unileipzig.irpact.core.logging.IRPLogging;
import de.unileipzig.irpact.core.process.modular.ca.ConsumerAgentData;
import de.unileipzig.irpact.core.process.modular.ca.components.ConsumerAgentCalculationModule;
import de.unileipzig.irpact.core.process.modular.ca.components.base.AbstractConsumerAgentModuleWithNSubModules;
import de.unileipzig.irptools.util.log.IRPLogger;

/**
 * @author Daniel Abitz
 */
public class WeightedModule
        extends AbstractConsumerAgentModuleWithNSubModules
        implements ConsumerAgentCalculationModule {

    private static final IRPLogger LOGGER = IRPLogging.getLogger(WeightedModule.class);

    protected static final int NUMBER_OF_MODULES = 1;
    protected static final int INDEX_MODULE = 0;

    protected double weight = 1;

    protected ConsumerAgentCalculationModule module;

    public WeightedModule() {
        super(NUMBER_OF_MODULES);
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
                getModule()
        );
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }
    public double getWeight() {
        return weight;
    }

    public void setModule(ConsumerAgentCalculationModule module) {
        this.module = module;
        updateModuleList(INDEX_MODULE, module);
    }
    public ConsumerAgentCalculationModule getModule() {
        return module;
    }
    protected ConsumerAgentCalculationModule getValidModule() {
        ConsumerAgentCalculationModule module = getModule();
        if(module == null) {
            throw new NullPointerException("ConsumerAgentCalculationModule");
        }
        return module;
    }

    @Override
    public double calculate(ConsumerAgentData input) throws Throwable {
        double result = getValidModule().calculate(input);
        return getWeight() * result;
    }
}
