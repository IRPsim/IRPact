package de.unileipzig.irpact.start.irpact.modes;

import com.fasterxml.jackson.databind.node.ObjectNode;
import de.unileipzig.irpact.commons.util.IRPactJson;
import de.unileipzig.irpact.core.logging.IRPLogging;
import de.unileipzig.irpact.core.logging.IRPSection;
import de.unileipzig.irpact.start.irpact.IRPact;
import de.unileipzig.irpact.start.irpact.IRPactExecutor;
import de.unileipzig.irptools.defstructure.Converter;
import de.unileipzig.irptools.util.log.IRPLogger;

/**
 * @author Daniel Abitz
 */
public final class PrintInput implements IRPactExecutor {

    private static final IRPLogger LOGGER = IRPLogging.getLogger(PrintInput.class);

    public static final int ID = 110;
    public static final String ID_STR = "110";
    public static final RunMinimalSimulation INSTANCE = new RunMinimalSimulation();

    @Override
    public int id() {
        return ID;
    }

    @Override
    public void execute(IRPact irpact) throws Exception {
        LOGGER.info(IRPSection.GENERAL, "execute PrintInput");

        ObjectNode inRootNode = irpact.getInRootNode();
        if(inRootNode == null) {
            LOGGER.info(IRPSection.GENERAL, "input json not found, convert data");
            Converter converter = IRPact.getInputConverter(irpact.getOptions());
            inRootNode = converter.toGamsJson(irpact.getInRoot());
        } else {
            LOGGER.info(IRPSection.GENERAL, "input json found");
        }

        IRPLogging.logResult("[PrintInput]", IRPactJson.toLazyString(inRootNode, IRPactJson.DEFAULT));

        irpact.postSimulationWithDummyOutput();
    }
}
