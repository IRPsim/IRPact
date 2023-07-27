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
public class GlobalNormExistingAssetNPVModule
        extends AbstractCACalculationModule2
        implements RAHelperAPI2 {

    private static final IRPLogger LOGGER = IRPLogging.getLogger(GlobalNormExistingAssetNPVModule.class);

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
        SimulationEnvironment environment = input.getEnvironment();
        int start = environment.getSettings().getFirstSimulationYear();
        int end = environment.getSettings().getLastSimulationYear();
        double min = dataSupplier.globalMinExistingAssetNPV(input::streamConsumerAgents, start, end);
        double max = dataSupplier.globalMaxExistingAssetNPV(input::streamConsumerAgents, start, end);
        double npv = dataSupplier.NPV(input.getAgent(), getCurrentYear(input));
        double normalized = MathUtil.normalize(npv, min, max);
        trace("[{}]@[{}] npv={}, min={}, max={}, normalized={}", getName(), printInputInfo(input),
            npv, min, max, normalized);
        return normalized;
    }
}