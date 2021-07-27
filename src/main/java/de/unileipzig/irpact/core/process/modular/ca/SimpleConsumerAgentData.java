package de.unileipzig.irpact.core.process.modular.ca;

import de.unileipzig.irpact.commons.checksum.Checksums;
import de.unileipzig.irpact.commons.util.Rnd;
import de.unileipzig.irpact.core.agent.consumer.ConsumerAgent;
import de.unileipzig.irpact.core.need.Need;
import de.unileipzig.irpact.core.process.ProcessModel;
import de.unileipzig.irpact.core.process.ProcessPlan;
import de.unileipzig.irpact.core.process.ProcessPlanResult;
import de.unileipzig.irpact.core.process.modular.ModulePlan;
import de.unileipzig.irpact.core.process.modular.ca.components.ConsumerAgentEvaluationModule;
import de.unileipzig.irpact.core.process.modular.ca.model.ConsumerAgentMPM;
import de.unileipzig.irpact.core.product.Product;

/**
 * @author Daniel Abitz
 */
public class SimpleConsumerAgentData implements ConsumerAgentData, ModulePlan {

    public SimpleConsumerAgentData() {
    }

    protected ConsumerAgentMPM model;
    public void setModel(ConsumerAgentMPM model) {
        this.model = model;
    }
    public ConsumerAgentMPM getModel() {
        return model;
    }
    protected ConsumerAgentMPM getValidModel() {
        ConsumerAgentMPM model = getModel();
        if(model == null) {
            throw new NullPointerException("ConsumerAgentMPM");
        }
        return model;
    }
    @Override
    public boolean isModel(ProcessModel model) {
        return getValidModel() == model;
    }

    @Override
    public ProcessPlan getPlan() {
        return this;
    }

    protected ConsumerAgent agent;
    @Override
    public ConsumerAgent getAgent() {
        return agent;
    }
    public void setAgent(ConsumerAgent agent) {
        this.agent = agent;
    }

    protected Need need;
    @Override
    public Need getNeed() {
        return need;
    }
    public void setNeed(Need need) {
        this.need = need;
    }

    protected Product product;
    @Override
    public Product getProduct() {
        return product;
    }
    public void setProduct(Product product) {
        this.product = product;
    }

    protected Rnd rnd;
    @Override
    public Rnd rnd() {
        return rnd;
    }
    public void setRnd(Rnd rnd) {
        this.rnd = rnd;
    }

    protected Stage stage = Stage.PRE_INITIALIZATION;
    @Override
    public Stage currentStage() {
        return stage;
    }
    @Override
    public void updateStage(Stage newStage) {
        this.stage = newStage;
    }

    protected boolean underConstruction = false;
    @Override
    public void setUnderConstruction(boolean underConstruction) {
        this.underConstruction = underConstruction;
    }
    @Override
    public boolean isUnderConstruction() {
        return underConstruction;
    }

    protected boolean underRenovation = false;
    @Override
    public void setUnderRenovation(boolean underRenovation) {
        this.underRenovation = underRenovation;
    }
    @Override
    public boolean isUnderRenovation() {
        return underRenovation;
    }

    @Override
    public ProcessPlanResult execute() throws Throwable {
        ConsumerAgentEvaluationModule module = getValidModel().getStartModule();
        if(module == null) {
            throw new NullPointerException("ConsumerAgentEvaluationModule");
        }
        AdoptionResult result = module.evaluate(this);
        if(result == null) {
            throw new NullPointerException("AdoptionResult");
        }
        return result.toPlanResult();
    }

    @Override
    public int getChecksum() {
        return Checksums.SMART.getChecksum(
                currentStage(),
                isUnderConstruction(),
                isUnderRenovation(),

                getNeed(),
                getProduct(),
                Checksums.SMART.getNamedChecksum(getAgent()),
                rnd(),
                Checksums.SMART.getNamedChecksum(getModel())
        );
    }
}
