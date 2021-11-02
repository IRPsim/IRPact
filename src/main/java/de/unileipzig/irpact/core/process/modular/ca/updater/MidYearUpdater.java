package de.unileipzig.irpact.core.process.modular.ca.updater;

import de.unileipzig.irpact.core.agent.consumer.ConsumerAgent;
import de.unileipzig.irpact.core.logging.IRPLogging;
import de.unileipzig.irpact.core.logging.IRPSection;
import de.unileipzig.irpact.core.logging.LoggingHelper;
import de.unileipzig.irpact.core.process.modular.ca.ConsumerAgentData;
import de.unileipzig.irpact.core.process.ra.RAConstants;
import de.unileipzig.irpact.core.simulation.SimulationEntityBase;
import de.unileipzig.irptools.util.log.IRPLogger;

/**
 * @author Daniel Abitz
 */
public class MidYearUpdater extends SimulationEntityBase implements ConsumerAgentDataUpdater, LoggingHelper {

    private static final IRPLogger LOGGER = IRPLogging.getLogger(MidYearUpdater.class);

    public MidYearUpdater() {
    }

    public MidYearUpdater(String name) {
        setName(name);
    }

    @Override
    public IRPLogger getDefaultLogger() {
        return LOGGER;
    }

    @Override
    public IRPSection getDefaultSection() {
        return IRPSection.SIMULATION_PROCESS;
    }

    @Override
    public boolean update(ConsumerAgentData data) {
        ConsumerAgent agent = data.getAgent();

        double renovationRate = getRenovationRate(agent);
        double renovationDraw = data.rnd().nextDouble();
        boolean doRenovation = renovationDraw < renovationRate;
        trace("agent '{}' now under renovation? {} ({} < {})", agent.getName(), doRenovation, renovationDraw, renovationRate);
        data.setUnderRenovation(doRenovation);

        double constructionRate = getConstructionRate(agent);
        double constructionDraw = data.rnd().nextDouble();
        boolean doConstruction = constructionDraw < constructionRate;
        trace("agent '{}' now under construction? {} ({} < {})", agent.getName(), doConstruction, constructionDraw, constructionRate);
        data.setUnderConstruction(doConstruction);

        if(doConstruction) {
            applyUnderConstruction(agent);
        }

        return doRenovation || doConstruction;
    }

    protected void applyUnderConstruction(ConsumerAgent agent) {
        if(!isShareOf1Or2FamilyHouse(agent)) {
            setShareOf1Or2FamilyHouse(agent);
            trace("agent '{}' is now share of 1 or 2 family house");
        }

        if(!isHouseOwner(agent)) {
            setHouseOwner(agent);
            trace("agent '{}' is now house owner");
        }
    }

    protected double getRenovationRate(ConsumerAgent agent) {
        return getAttributeHelper().getDouble(agent, RAConstants.RENOVATION_RATE, true);
    }

    protected double getConstructionRate(ConsumerAgent agent) {
        return getAttributeHelper().getDouble(agent, RAConstants.CONSTRUCTION_RATE, true);
    }

    protected boolean isShareOf1Or2FamilyHouse(ConsumerAgent agent) {
        return getAttributeHelper().getBoolean(agent, RAConstants.SHARE_1_2_HOUSE, true);
    }

    protected void setShareOf1Or2FamilyHouse(ConsumerAgent agent) {
        getAttributeHelper().setBoolean(agent, RAConstants.SHARE_1_2_HOUSE, true, true);
    }

    protected boolean isHouseOwner(ConsumerAgent agent) {
        return getAttributeHelper().getBoolean(agent, RAConstants.HOUSE_OWNER, true);
    }

    public void setHouseOwner(ConsumerAgent agent) {
        getAttributeHelper().setBoolean(agent, RAConstants.HOUSE_OWNER, true, true);
    }
}
