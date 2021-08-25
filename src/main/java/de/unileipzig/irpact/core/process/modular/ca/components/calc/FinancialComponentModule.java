package de.unileipzig.irpact.core.process.modular.ca.components.calc;

import de.unileipzig.irpact.commons.checksum.Checksums;
import de.unileipzig.irpact.commons.util.MathUtil;
import de.unileipzig.irpact.core.agent.consumer.ConsumerAgent;
import de.unileipzig.irpact.core.logging.IRPLogging;
import de.unileipzig.irpact.core.misc.MissingDataException;
import de.unileipzig.irpact.core.process.modular.ca.ConsumerAgentData;
import de.unileipzig.irpact.core.process.modular.ca.components.ConsumerAgentCalculationModule;
import de.unileipzig.irpact.core.process.modular.ca.components.base.AbstractConsumerAgentModule;
import de.unileipzig.irpact.core.process.ra.npv.NPVData;
import de.unileipzig.irpact.core.process.ra.npv.NPVDataSupplier;
import de.unileipzig.irpact.core.simulation.SimulationEnvironment;
import de.unileipzig.irptools.util.log.IRPLogger;

/**
 * @author Daniel Abitz
 */
public class FinancialComponentModule extends AbstractConsumerAgentModule implements ConsumerAgentCalculationModule {

    private static final IRPLogger LOGGER = IRPLogging.getLogger(FinancialComponentModule.class);

    protected final NPVDataSupplier npvDataSupplier = new NPVDataSupplier();

    public FinancialComponentModule() {
    }

    @Override
    public IRPLogger getDefaultLogger() {
        return LOGGER;
    }

    @Override
    public int getChecksum() {
        return Checksums.SMART.getChecksum(
                getName(),
                getWeightFT(),
                getWeightNPV(),
                getLogisticFactor(),
                getWeight()
        );
    }

    protected double weight;
    public void setWeight(double weight) {
        this.weight = weight;
    }
    public double getWeight() {
        return weight;
    }

    public NPVDataSupplier getNPVDataSupplier() {
        return npvDataSupplier;
    }

    protected NPVData npvData;
    public void setNPVData(NPVData npvData) {
        this.npvData = npvData;
    }
    public NPVData getNPVData() {
        return npvData;
    }
    protected NPVData getValidNPVData() {
        NPVData data = getNPVData();
        if(data == null) {
            throw new NullPointerException("NPVData");
        }
        return data;
    }

    protected double logisticFactor;
    public void setLogisticFactor(double logisticFactor) {
        this.logisticFactor = logisticFactor;
    }
    public double getLogisticFactor() {
        return logisticFactor;
    }

    protected double weightFT;
    public void setWeightFT(double weightFT) {
        this.weightFT = weightFT;
    }
    public double getWeightFT() {
        return weightFT;
    }

    protected double weightNPV;
    public void setWeightNPV(double weightNPV) {
        this.weightNPV = weightNPV;
    }
    public double getWeightNPV() {
        return weightNPV;
    }

    @Override
    public void setEnvironment(SimulationEnvironment environment) {
        super.setEnvironment(environment);
        getNPVDataSupplier().setAttributeHelper(getAttributeHelper());
    }

    @Override
    public void preAgentCreation() throws MissingDataException {
        super.preAgentCreation();
        if(npvData == null) {
            throw new MissingDataException("missing npv data");
        } else {
            initNPVDataSupplier(getValidNPVData(), getNPVDataSupplier());
        }
    }

    @Override
    public double calculate(ConsumerAgentData input) throws Throwable {
        double value = getFinancialComponent(input.getAgent());
        return getWeight() * value;
    }

    protected double getFinancialComponent(ConsumerAgent agent) {
        double avgNPV = getAverageNPV();
        double agentNPV = getNPV(agent);

        double avgFT = getAverageFinancialPurchasePower();
        double agentFT = getFinancialPurchasePower(agent);

        double ft = getLogisticFactor() * (agentFT - avgFT);
        double npv = getLogisticFactor() * (agentNPV - avgNPV);

        double logisticFT = MathUtil.logistic(ft);
        double logisticNPV = MathUtil.logistic(npv);

        double weightedFT = getWeightFT() * logisticFT;
        double weightedNPV = getWeightNPV() * logisticNPV;

        return weightedFT + weightedNPV;
    }

    protected double getAverageNPV() {
        return getAverageNPV(getNPVDataSupplier());
    }

    protected double getNPV(ConsumerAgent agent) {
        return getNPV(getNPVDataSupplier(), agent);
    }

    protected double getAverageFinancialPurchasePower() {
        return getAverageFinancialPurchasePower(getNPVDataSupplier());
    }
}
