package de.unileipzig.irpact.core.agent.consumer;

import de.unileipzig.irpact.core.agent.Agent;
import de.unileipzig.irpact.core.agent.SpatialInformationAgentBase;
import de.unileipzig.irpact.core.message.Message;
import de.unileipzig.irpact.core.need.Need;
import de.unileipzig.irpact.core.preference.Preference;
import de.unileipzig.irpact.core.product.AdoptedProduct;
import de.unileipzig.irpact.core.product.Product;
import de.unileipzig.irpact.core.product.ProductAttribute;
import de.unileipzig.irpact.core.product.perception.ProductAttributePerceptionScheme;
import de.unileipzig.irpact.core.simulation.EntityType;
import de.unileipzig.irpact.core.simulation.SimulationEnvironment;

import java.util.Set;

/**
 * @author Daniel Abitz
 */
public class ConsumerAgentBase extends SpatialInformationAgentBase implements ConsumerAgent {

    protected ConsumerAgentData data;

    public ConsumerAgentBase(
            SimulationEnvironment environment,
            ConsumerAgentData data) {
        super(environment, data.getName(), data.getInformationAuthority(), data.getSpatialInformation());
        this.data = data;
    }

    //=========================
    //...
    //=========================

    @Override
    public double getInformationAuthority() {
        return data.getInformationAuthority();
    }

    @Override
    public ConsumerAgentGroup getGroup() {
        return data.getGroup();
    }

    @Override
    public Set<ConsumerAgentAttribute> getAttributes() {
        return data.getAttributes();
    }

    @Override
    public Set<Need> getNeeds() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Set<Product> getKnownProducts() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Set<AdoptedProduct> getAdoptedProducts() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Set<Preference> getPreferences() {
        return data.getPreferences();
    }

    @Override
    public boolean is(EntityType type) {
        throw new UnsupportedOperationException();
    }

    @Override
    public ProductAttributePerceptionScheme getScheme(ProductAttribute attribute) {
        return data.getPerceptionSchemeManager().getScheme(attribute);
    }

    @Override
    public boolean isHandling(Agent sender, Message msg) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void handleMessage(Agent sender, Message msg) {
        throw new UnsupportedOperationException();
    }

    //=========================
    //...
    //=========================

    @Override
    public void addNeed(Need need) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean isAwareOf(Product product) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void makeAwareOf(Product product) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void updateProductAttributePerception(ProductAttribute attribute, double perceptionValue, double informationWeight) {
        throw new UnsupportedOperationException();
    }
}
