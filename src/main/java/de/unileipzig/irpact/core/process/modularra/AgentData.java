package de.unileipzig.irpact.core.process.modularra;

import de.unileipzig.irpact.commons.util.Rnd;
import de.unileipzig.irpact.core.agent.AgentManager;
import de.unileipzig.irpact.core.need.Need;
import de.unileipzig.irpact.core.network.SocialGraph;
import de.unileipzig.irpact.core.network.SocialNetwork;
import de.unileipzig.irpact.core.process.ra.RAModelData;
import de.unileipzig.irpact.core.process.ra.RAStage;
import de.unileipzig.irpact.core.process.ra.alg.RelativeAgreementAlgorithm;
import de.unileipzig.irpact.core.process.ra.uncert.Uncertainty;
import de.unileipzig.irpact.core.product.Product;
import de.unileipzig.irpact.core.simulation.Settings;
import de.unileipzig.irpact.core.simulation.SimulationEnvironment;
import de.unileipzig.irpact.core.time.TimeModel;

import java.util.Random;

/**
 * @author Daniel Abitz
 */
public class AgentData {

    protected ModularRAProcessModel model;
    protected Uncertainty uncertainty;

    protected Need need;
    protected Product product;
    protected Rnd rnd;

    protected RAStage stage = RAStage.PRE_INITIALIZATION;
    protected boolean underRenovation = false;
    protected boolean underConstruction = false;

    public AgentData(
            ModularRAProcessModel model,
            Uncertainty uncertainty,
            Need need,
            Product product,
            Rnd rnd) {
        this.model = model;
        this.uncertainty = uncertainty;
        this.need = need;
        this.product = product;
        this.rnd = rnd;
    }

    public RAStage getStage() {
        return stage;
    }

    public Need getNeed() {
        return need;
    }

    public void updateStage(RAStage stage) {
        this.stage = stage;
    }

    public Rnd getRnd() {
        return rnd;
    }
    public Random getRandom() {
        return getRnd().getRandom();
    }

    public ModularRAProcessModel getModel() {
        return model;
    }

    public RAModelData getModelData() {
        return getModel().getModelData();
    }

    public RelativeAgreementAlgorithm getRelativeAgreementAlgorithm() {
        return getModel().getRelativeAgreementAlgorithm();
    }

    public SimulationEnvironment getEnvironment() {
        return getModel().getEnvironment();
    }

    public TimeModel getTimeModel() {
        return getEnvironment().getTimeModel();
    }

    public int getCurrentYear() {
        return getTimeModel().getCurrentYear();
    }

    public AgentManager getAgents() {
        return getEnvironment().getAgents();
    }

    public Settings getSettings() {
        return getEnvironment().getSettings();
    }

    public SocialNetwork getNetwork() {
        return getEnvironment().getNetwork();
    }

    public SocialGraph getGraph() {
        return getNetwork().getGraph();
    }

    public Product getProduct() {
        return product;
    }

    public String getProductName() {
        return getProduct().getName();
    }

    public Uncertainty getUncertainty() {
        return uncertainty;
    }

    public boolean isUnderConstruction() {
        return underConstruction;
    }

    public boolean isUnderRenovation() {
        return underRenovation;
    }
}
