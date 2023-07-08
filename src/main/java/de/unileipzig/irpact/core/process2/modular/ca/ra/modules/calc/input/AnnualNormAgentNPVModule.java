package de.unileipzig.irpact.core.process2.modular.ca.ra.modules.calc.input;

import de.unileipzig.irpact.commons.util.MathUtil;
import de.unileipzig.irpact.core.logging.IRPLogging;
import de.unileipzig.irpact.core.process.ra.npv.NPVData;
import de.unileipzig.irpact.core.process.ra.npv.NPVDataSupplier;
import de.unileipzig.irpact.core.process2.PostAction2;
import de.unileipzig.irpact.core.process2.modular.ca.ConsumerAgentData2;
import de.unileipzig.irpact.core.process2.modular.ca.ra.RAHelperAPI2;
import de.unileipzig.irpact.core.process2.modular.ca.ra.modules.core.AbstractCACalculationModule2;
import de.unileipzig.irpact.core.simulation.SimulationEnvironment;
import de.unileipzig.irptools.util.log.IRPLogger;
import java.util.List;

/**
 * @author Daniel Abitz
 */
public class AnnualNormAgentNPVModule
        extends AbstractCACalculationModule2
        implements RAHelperAPI2 {

    private static final IRPLogger LOGGER = IRPLogging.getLogger(AnnualNormAgentNPVModule.class);

    protected NPVDataSupplier dataSupplier;
    protected NPVData data;

    public void setData(NPVData data) {
        this.data = data;
    }

    @Override
    public IRPLogger getDefaultLogger() {
        return LOGGER;
    }

    @Override
    public void validate() throws Throwable {
        traceModuleValidation();
        if(data == null) {
            throw new NullPointerException("missing NPVData");
        }
    }

    @Override
    public void initialize(SimulationEnvironment environment) throws Throwable {
        if(alreadyInitalized()) {
            return;
        }

        traceModuleInitalization();
        dataSupplier = getNPVDataSupplier(environment, data);
        setInitalized();
    }

    @Override
    public void initializeNewInput(ConsumerAgentData2 input) throws Throwable {
    }

    @Override
    public void setup(SimulationEnvironment environment) throws Throwable {
        if(alreadySetupCalled()) {
            return;
        }

        traceModuleSetup();
        setSetupCalled();
    }

    @Override
    public double calculate(ConsumerAgentData2 input, List<PostAction2> actions) throws Throwable {
        traceModuleCall(input);
        double min = dataSupplier.annualMinAgentNPV(input.streamConsumerAgents(), getCurrentYear(input));
        double max = dataSupplier.annualMaxAgentNPV(input.streamConsumerAgents(), getCurrentYear(input));
        double npv = dataSupplier.NPV(input.getAgent(), getCurrentYear(input));
        double normalized = MathUtil.normalize(npv, min, max);
        trace("[{}]@[{}] npv={}, min={}, max={}, normalized={}", getName(), printInputInfo(input),
            npv, min, max, normalized);
        return normalized;
    }
}
