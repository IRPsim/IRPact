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
public class DisaggregatedNPVModule extends AbstractConsumerAgentModule implements ConsumerAgentCalculationModule {

    private static final IRPLogger LOGGER = IRPLogging.getLogger(DisaggregatedNPVModule.class);

    protected final NPVDataSupplier npvDataSupplier = new NPVDataSupplier();
    protected NPVData npvData;
    protected double weight;
    protected double logisticFactor;

    public DisaggregatedNPVModule() {
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

    public NPVDataSupplier getNPVDataSupplier() {
        return npvDataSupplier;
    }

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

    @Override
    public void setEnvironment(SimulationEnvironment environment) {
        super.setEnvironment(environment);
        getNPVDataSupplier().setAttributeHelper(getAttributeHelper());
    }

    @Override
    public void preAgentCreation() throws MissingDataException {
        super.preAgentCreation();
        if(npvData != null) {
            initNPVMatrixWithFile();
        }
    }

    protected void initNPVMatrixWithFile() {
        initNPVMatrixWithFile(getValidNPVData(), getNPVDataSupplier());
    }

    @Override
    public double calculate(ConsumerAgentData input) {
        double avgNPV = getAverageNPV();
        double agentNPV = getNPV(input.getAgent());
        double npv = getLogisticFactor() * (agentNPV - avgNPV);
        double logisticNPV = MathUtil.logistic(npv);
        return getWeight() * logisticNPV;
    }

    protected double getAverageNPV() {
        return getAverageNPV(getNPVDataSupplier());
    }

    protected double getNPV(ConsumerAgent agent) {
        return getNPV(getNPVDataSupplier(), agent);
    }
}
