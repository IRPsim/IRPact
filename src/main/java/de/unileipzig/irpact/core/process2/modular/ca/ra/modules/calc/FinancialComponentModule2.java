package de.unileipzig.irpact.core.process2.modular.ca.ra.modules.calc;

import de.unileipzig.irpact.core.logging.IRPLogging;
import de.unileipzig.irpact.core.process.ra.npv.NPVData;
import de.unileipzig.irpact.core.process2.PostAction2;
import de.unileipzig.irpact.core.process2.modular.ca.ConsumerAgentData2;
import de.unileipzig.irpact.core.process2.modular.ca.ra.modules.RAHelperAPI2;
import de.unileipzig.irpact.core.process2.modular.ca.ra.modules.core.AbstractCAConstructionModule2;
import de.unileipzig.irpact.core.simulation.SimulationEnvironment;
import de.unileipzig.irptools.util.log.IRPLogger;

import java.util.List;

/**
 * @author Daniel Abitz
 */
public class FinancialComponentModule2
        extends AbstractCAConstructionModule2
        implements RAHelperAPI2 {

    private static final IRPLogger LOGGER = IRPLogging.getLogger(FinancialComponentModule2.class);

    protected final DisaggregatedNPVModule2 npvModule = new DisaggregatedNPVModule2();
    protected final DisaggregatedFinancialModule2 finModule = new DisaggregatedFinancialModule2();
    protected double npvWeight = 0.5;
    protected double finWeight = 0.5;

    public void setData(NPVData data) {
        npvModule.setData(data);
    }

    public NPVData getData() {
        return npvModule.getData();
    }

    public void setLogisticFactor(double logisticFactor) {
        npvModule.setLogisticFactor(logisticFactor);
        finModule.setLogisticFactor(logisticFactor);
    }

    public double getLogisticFactor() {
        return npvModule.getLogisticFactor();
    }

    public void setNpvWeight(double npvWeight) {
        this.npvWeight = npvWeight;
    }

    public double getNpvWeight() {
        return npvWeight;
    }

    public void setFinWeight(double finWeight) {
        this.finWeight = finWeight;
    }

    public double getFinWeight() {
        return finWeight;
    }

    @Override
    public IRPLogger getDefaultLogger() {
        return LOGGER;
    }

    @Override
    public void validate() throws Throwable {
        npvModule.validate();
        finModule.validate();
    }

    @Override
    public void initialize(SimulationEnvironment environment) throws Throwable {
        npvModule.initialize(environment);
        finModule.initialize(environment);
    }

    @Override
    public double calculate(ConsumerAgentData2 input, List<PostAction2> actions) throws Throwable {
        traceModuleInfo(input);
        double npv = npvModule.calculate(input, actions);
        double fin = finModule.calculate(input, actions);
        return npvWeight * npv + finWeight * fin;
    }
}
