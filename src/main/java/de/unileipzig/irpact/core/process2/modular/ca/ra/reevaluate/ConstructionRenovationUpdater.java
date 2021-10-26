package de.unileipzig.irpact.core.process2.modular.ca.ra.reevaluate;

import de.unileipzig.irpact.core.logging.IRPLogging;
import de.unileipzig.irpact.core.process2.PostAction2;
import de.unileipzig.irpact.core.process2.modular.SharedModuleData;
import de.unileipzig.irpact.core.process2.modular.ca.ConsumerAgentData2;
import de.unileipzig.irpact.core.process2.modular.ca.ra.RAHelperAPI2;
import de.unileipzig.irpact.core.process2.modular.reevaluate.AbstractReevaluator;
import de.unileipzig.irpact.core.simulation.SimulationEnvironment;
import de.unileipzig.irptools.util.log.IRPLogger;

import java.util.List;

/**
 * @author Daniel Abitz
 */
public class ConstructionRenovationUpdater
        extends AbstractReevaluator<ConsumerAgentData2>
        implements RAHelperAPI2 {

    private static final IRPLogger LOGGER = IRPLogging.getLogger(ConstructionRenovationUpdater.class);

    @Override
    public IRPLogger getDefaultLogger() {
        return LOGGER;
    }

    @Override
    public SharedModuleData getSharedData() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void initializeReevaluator(SimulationEnvironment environment) {
    }

    @Override
    public void reevaluate(ConsumerAgentData2 input, List<PostAction2> actions) {
        double renovationRate = getRenovationRate(input);
        double renovationDraw = input.rnd().nextDouble();
        boolean doRenovation = renovationDraw < renovationRate;
        trace("[{}] agent '{}' now under renovation? {} ({} < {})", getName(), input.getAgentName(), doRenovation, renovationDraw, renovationRate);
        input.setUnderRenovation(doRenovation);

        double constructionRate = getConstructionRate(input);
        double constructionDraw = input.rnd().nextDouble();
        boolean doConstruction = constructionDraw < constructionRate;
        trace("[{}] agent '{}' now under construction? {} ({} < {})", getName(), input.getAgentName(), doConstruction, constructionDraw, constructionRate);
        input.setUnderConstruction(doConstruction);

        if(doConstruction) {
            applyUnderConstruction(input);
        }
    }
}
