package de.unileipzig.irpact.core.agent.consumer;

import de.unileipzig.irpact.commons.Check;
import de.unileipzig.irpact.commons.annotation.ToDo;
import de.unileipzig.irpact.core.agent.Agent;
import de.unileipzig.irpact.core.agent.SpatialInformationAgentBase;
import de.unileipzig.irpact.core.message.Message;
import de.unileipzig.irpact.core.need.Need;
import de.unileipzig.irpact.core.preference.Preference;
import de.unileipzig.irpact.core.product.AdoptedProduct;
import de.unileipzig.irpact.core.product.Product;
import de.unileipzig.irpact.core.product.ProductAttribute;
import de.unileipzig.irpact.core.product.perception.ProductAttributePerceptionScheme;
import de.unileipzig.irpact.core.product.perception.ProductAttributePerceptionSchemeManager;
import de.unileipzig.irpact.core.simulation.EntityType;
import de.unileipzig.irpact.core.simulation.SimulationEnvironment;
import de.unileipzig.irpact.core.spatial.SpatialInformation;

import java.util.Set;

/**
 * @author Daniel Abitz
 */
public class ConsumerAgentBase extends SpatialInformationAgentBase implements ConsumerAgent {

    protected ConsumerAgentGroup group;
    @ToDo("koennen die sich aendern?")
    protected Set<ConsumerAgentAttribute> attributes;
    @ToDo("koennen die sich aendern?")
    protected Set<Preference> preferences;
    protected ProductAttributePerceptionSchemeManager perceptionSchemeManager;
    protected Set<Need> initialNeeds;

    public ConsumerAgentBase(
            SimulationEnvironment environment,
            String name,
            SpatialInformation spatialInformation,
            ConsumerAgentGroup group,
            Set<ConsumerAgentAttribute> attributes,
            Set<Preference> preferences,
            ProductAttributePerceptionSchemeManager perceptionSchemeManager,
            Set<Need> initialNeeds) {
        super(environment, name, group.getInformationAuthority(), spatialInformation);
        this.group = Check.requireNonNull(group, "group");
        this.attributes = Check.requireNonNull(attributes, "attributes");
        this.preferences = Check.requireNonNull(preferences, "preferences");
        this.perceptionSchemeManager = Check.requireNonNull(perceptionSchemeManager, "perceptionSchemeManager");
    }

    //=========================
    //...
    //=========================

    public Set<Need> getInitialNeeds() {
        return initialNeeds;
    }

    //=========================
    //...
    //=========================

    @Override
    public double getInformationAuthority() {
        return group.getInformationAuthority();
    }

    @Override
    public ConsumerAgentGroup getGroup() {
        return group;
    }

    @Override
    public Set<ConsumerAgentAttribute> getAttributes() {
        return attributes;
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
        return preferences;
    }

    @Override
    public boolean is(EntityType type) {
        throw new UnsupportedOperationException();
    }

    @Override
    public ProductAttributePerceptionScheme getScheme(ProductAttribute attribute) {
        return perceptionSchemeManager.getScheme(attribute);
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
