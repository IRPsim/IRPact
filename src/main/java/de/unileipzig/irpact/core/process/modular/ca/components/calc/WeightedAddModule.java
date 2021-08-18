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
public class WeightedAddModule
        extends AbstractConsumerAgentModuleWithNSubModules
        implements ConsumerAgentCalculationModule {

    private static final IRPLogger LOGGER = IRPLogging.getLogger(WeightedAddModule.class);

    protected static final int NUMBER_OF_MODULES = 2;
    protected static final int INDEX_MODULE1 = 0;
    protected static final int INDEX_MODULE2 = 1;

    protected double weight = 1;
    protected double weight1 = 1;
    protected double weight2 = 1;

    protected ConsumerAgentCalculationModule module1;
    protected ConsumerAgentCalculationModule module2;

    public WeightedAddModule() {
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
                getWeight1(),
                getWeight2(),
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

    public void setWeight1(double weight1) {
        this.weight1 = weight1;
    }
    public double getWeight1() {
        return weight1;
    }

    public void setWeight2(double weight2) {
        this.weight2 = weight2;
    }
    public double getWeight2() {
        return weight2;
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

        double weightedResult1 = getWeight1() * result1;
        double weightedResult2 = getWeight2() * result2;

        double sum = weightedResult1 + weightedResult2;

        return getWeight() * sum;
    }
}
