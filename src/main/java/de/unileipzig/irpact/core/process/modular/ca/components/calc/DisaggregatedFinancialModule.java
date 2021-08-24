package de.unileipzig.irpact.core.process.modular.ca.components.calc;

import de.unileipzig.irpact.commons.checksum.Checksums;
import de.unileipzig.irpact.commons.util.MathUtil;
import de.unileipzig.irpact.commons.util.data.MutableDouble;
import de.unileipzig.irpact.core.logging.IRPLogging;
import de.unileipzig.irpact.core.process.modular.ca.ConsumerAgentData;
import de.unileipzig.irpact.core.process.modular.ca.components.ConsumerAgentCalculationModule;
import de.unileipzig.irpact.core.process.modular.ca.components.base.AbstractConsumerAgentModule;
import de.unileipzig.irptools.util.log.IRPLogger;

/**
 * @author Daniel Abitz
 */
public class DisaggregatedFinancialModule extends AbstractConsumerAgentModule implements ConsumerAgentCalculationModule {

    private static final IRPLogger LOGGER = IRPLogging.getLogger(DisaggregatedFinancialModule.class);

    protected MutableDouble avgFT = MutableDouble.NaN();
    protected double weight;
    protected double logisticFactor;

    public DisaggregatedFinancialModule() {
    }

    @Override
    public IRPLogger getDefaultLogger() {
        return LOGGER;
    }

    @Override
    public int getChecksum() {
        return Checksums.SMART.getChecksum(
                getName(),
                getLogisticFactor(),
                getWeight()
        );
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }
    public double getWeight() {
        return weight;
    }

    public void setLogisticFactor(double logisticFactor) {
        this.logisticFactor = logisticFactor;
    }
    public double getLogisticFactor() {
        return logisticFactor;
    }

    @Override
    public double calculate(ConsumerAgentData input) throws Throwable {
        double avgFT = getAverageFinancialPurchasePower();
        double agentFT = getFinancialPurchasePower(input.getAgent());
        double ft = getLogisticFactor() * (agentFT - avgFT);
        double logisticFT = MathUtil.logistic(ft);
        return getWeight() * logisticFT;
    }

    protected double getAverageFinancialPurchasePower() {
        return getAverageFinancialPurchasePower(getAgentManager().streamConsumerAgents(), avgFT);
    }
}
