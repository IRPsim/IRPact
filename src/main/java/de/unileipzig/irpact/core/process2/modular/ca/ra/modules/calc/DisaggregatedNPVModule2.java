package de.unileipzig.irpact.core.process2.modular.ca.ra.modules.calc;

import de.unileipzig.irpact.commons.util.MathUtil;
import de.unileipzig.irpact.core.logging.IRPLogging;
import de.unileipzig.irpact.core.process.ra.npv.NPVData;
import de.unileipzig.irpact.core.process.ra.npv.NPVDataSupplier;
import de.unileipzig.irpact.core.process2.PostAction2;
import de.unileipzig.irpact.core.process2.modular.ca.ConsumerAgentData2;
import de.unileipzig.irpact.core.process2.modular.ca.ra.modules.RAHelperAPI2;
import de.unileipzig.irpact.core.process2.modular.ca.ra.modules.core.AbstractUniformCAMultiModule1_2;
import de.unileipzig.irpact.core.process2.modular.modules.core.CalculationModule2;
import de.unileipzig.irpact.core.simulation.SimulationEnvironment;
import de.unileipzig.irptools.util.log.IRPLogger;

import java.util.List;

/**
 * @author Daniel Abitz
 */
public class DisaggregatedNPVModule2
        extends AbstractUniformCAMultiModule1_2<Number, Number, CalculationModule2<ConsumerAgentData2>>
        implements CalculationModule2<ConsumerAgentData2>, RAHelperAPI2 {

    private static final IRPLogger LOGGER = IRPLogging.getLogger(DisaggregatedNPVModule2.class);

    protected NPVDataSupplier dataSupplier;
    protected NPVData data;
    protected double logisticFactor;

    public void setData(NPVData data) {
        this.data = data;
    }

    public NPVData getData() {
        return data;
    }

    public void setLogisticFactor(double logisticFactor) {
        this.logisticFactor = logisticFactor;
    }

    public double getLogisticFactor() {
        return logisticFactor;
    }

    @Override
    public IRPLogger getDefaultLogger() {
        return LOGGER;
    }

    @Override
    protected void validateSelf() throws Throwable {
        if(data == null) {
            throw new NullPointerException("missing NPVData");
        }
    }

    @Override
    protected void initializeSelf(SimulationEnvironment environment) throws Throwable {
        dataSupplier = getNPVDataSupplier(environment, data);
    }

    @Override
    public double calculate(ConsumerAgentData2 input, List<PostAction2> actions) throws Throwable {
        traceModuleInfo(input);
        double avgNpv = getNonnullSubmodule().calculate(input, actions);
        double npv = dataSupplier.NPV(input.getAgent(), getCurrentYear(input));
        return MathUtil.logistic(1, logisticFactor, npv, avgNpv);
    }
}
