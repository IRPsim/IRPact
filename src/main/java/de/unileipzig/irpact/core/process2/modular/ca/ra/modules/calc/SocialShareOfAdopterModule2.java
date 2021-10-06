package de.unileipzig.irpact.core.process2.modular.ca.ra.modules.calc;

import de.unileipzig.irpact.core.logging.IRPLogging;
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
public class SocialShareOfAdopterModule2
        extends AbstractCAConstructionModule2
        implements RAHelperAPI2 {

    private static final IRPLogger LOGGER = IRPLogging.getLogger(SocialShareOfAdopterModule2.class);

    @Override
    public IRPLogger getDefaultLogger() {
        return LOGGER;
    }

    @Override
    public void validate() throws Throwable {
    }

    @Override
    public void initialize(SimulationEnvironment environment) throws Throwable {
    }

    @Override
    public double calculate(ConsumerAgentData2 input, List<PostAction2> actions) throws Throwable {
        traceModuleInfo(input);

        return getShareOfAdopterInSocialNetwork(input);
    }
}
