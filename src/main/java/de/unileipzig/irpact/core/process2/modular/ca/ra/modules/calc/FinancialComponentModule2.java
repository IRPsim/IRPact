package de.unileipzig.irpact.core.process2.modular.ca.ra.modules.calc;

import de.unileipzig.irpact.core.logging.IRPLogging;
import de.unileipzig.irpact.core.process2.PostAction2;
import de.unileipzig.irpact.core.process2.modular.ca.ConsumerAgentData2;
import de.unileipzig.irpact.core.process2.modular.ca.ra.modules.RAHelperAPI2;
import de.unileipzig.irpact.core.process2.modular.ca.ra.modules.core.AbstractUniformCAMultiModule2_2;
import de.unileipzig.irpact.core.process2.modular.modules.core.CalculationModule2;
import de.unileipzig.irpact.core.simulation.SimulationEnvironment;
import de.unileipzig.irptools.util.log.IRPLogger;

import java.util.List;

/**
 * @author Daniel Abitz
 */
public class FinancialComponentModule2
        extends AbstractUniformCAMultiModule2_2<Number, Number, CalculationModule2<ConsumerAgentData2>>
        implements CalculationModule2<ConsumerAgentData2>, RAHelperAPI2 {

    private static final IRPLogger LOGGER = IRPLogging.getLogger(FinancialComponentModule2.class);

    protected double npvWeight = 0.5;
    protected double finWeight = 0.5;

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
    protected void validateSelf() throws Throwable {
    }

    @Override
    protected void initializeSelf(SimulationEnvironment environment) throws Throwable {
    }

    @Override
    public double calculate(ConsumerAgentData2 input, List<PostAction2> actions) throws Throwable {
        traceModuleInfo(input);

        double npv = getNonnullSubmodule1().calculate(input, actions);
        double fin = getNonnullSubmodule2().calculate(input, actions);
        return npvWeight * npv + finWeight * fin;
    }
}
