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
public class AddModule
        extends AbstractConsumerAgentModuleWithNSubModules
        implements ConsumerAgentCalculationModule {

    private static final IRPLogger LOGGER = IRPLogging.getLogger(AddModule.class);

    protected static final int NUMBER_OF_MODULES = 2;
    protected static final int INDEX_MODULE1 = 0;
    protected static final int INDEX_MODULE2 = 1;

    protected double weight = 1;

    protected ConsumerAgentCalculationModule module1;
    protected ConsumerAgentCalculationModule module2;

    public AddModule() {
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
                getModule1(),
                getModule2()
        );
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }
    public double getWeight() {
        return weight;
    }

    public void setModule1(ConsumerAgentCalculationModule module1) {
        this.module1 = module1;
        setSubModule(INDEX_MODULE1, module1);
    }
    public ConsumerAgentCalculationModule getModule1() {
        return module1;
    }
    protected ConsumerAgentCalculationModule getValidModule1() {
        ConsumerAgentCalculationModule module1 = getModule1();
        if(module1 == null) {
            throw new NullPointerException("ConsumerAgentCalculationModule");
        }
        return module1;
    }

    public void setModule2(ConsumerAgentCalculationModule module2) {
        this.module2 = module2;
        setSubModule(INDEX_MODULE2, module2);
    }
    public ConsumerAgentCalculationModule getModule2() {
        return module2;
    }
    protected ConsumerAgentCalculationModule getValidModule2() {
        ConsumerAgentCalculationModule module1 = getModule2();
        if(module1 == null) {
            throw new NullPointerException("ConsumerAgentCalculationModule");
        }
        return module1;
    }

    @Override
    public double calculate(ConsumerAgentData input) throws Throwable {
        double result1 = getValidModule1().calculate(input);
        double result2 = getValidModule2().calculate(input);

        double sum = result1 + result2;

        return getWeight() * sum;
    }
}
