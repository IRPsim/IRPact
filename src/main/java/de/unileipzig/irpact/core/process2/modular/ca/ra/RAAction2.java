package de.unileipzig.irpact.core.process2.modular.ca.ra;

import de.unileipzig.irpact.core.logging.IRPLogging;
import de.unileipzig.irpact.core.logging.IRPSection;
import de.unileipzig.irpact.core.process2.PostAction2;
import de.unileipzig.irpact.core.process2.modular.ca.ConsumerAgentData2;
import de.unileipzig.irpact.core.process2.modular.ca.ra.modules.RAHelperAPI2;
import de.unileipzig.irptools.util.log.IRPLogger;

import java.util.List;

/**
 * @author Daniel Abitz
 */
public final class RAAction2 implements RAHelperAPI2 {

    private static final IRPLogger LOGGER = IRPLogging.getLogger(RAAction2.class);

    private static final RAAction2 INSTANCE = new RAAction2();

    public static void execute(
            ConsumerAgentData2 input,
            List<PostAction2> actions) {
        INSTANCE.execute0(input, actions);
    }

    @Override
    public String getName() {
        return "__RAAction__";
    }

    @Override
    public int getChecksum() {
        return 42;
    }

    @Override
    public IRPLogger getDefaultLogger() {
        return LOGGER;
    }

    @Override
    public IRPSection getDefaultSection() {
        return IRPSection.SIMULATION_PROCESS;
    }

    private void execute0(
            ConsumerAgentData2 input,
            List<PostAction2> actions) {
        if(true) throw new RuntimeException();
    }
}
