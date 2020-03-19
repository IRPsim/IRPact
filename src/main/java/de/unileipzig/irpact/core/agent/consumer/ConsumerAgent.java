package de.unileipzig.irpact.core.agent.consumer;

import de.unileipzig.irpact.core.GroupEntity;
import de.unileipzig.irpact.core.agent.SpatialInformationAgent;
import de.unileipzig.irpact.core.need.Need;
import de.unileipzig.irpact.core.preference.Preference;
import de.unileipzig.irpact.core.product.Product;
import de.unileipzig.irpact.core.product.ProductAttribute;
import de.unileipzig.irpact.core.product.ProductGroup;
import de.unileipzig.irpact.core.product.perception.ProductAttributePerceptionScheme;

import java.util.Set;

/**
 * @author Daniel Abitz
 */
public interface ConsumerAgent extends SpatialInformationAgent, GroupEntity<ConsumerAgent> {

    @Override
    ConsumerAgentGroup getGroup();

    Set<ConsumerAgentAttribute> getAttributes();

    Set<Need> getNeeds();

    Set<Product> getKnownProducts();

    Set<Preference> getPreferences();

    ProductAttributePerceptionScheme getScheme(ProductAttribute attribute);

    //=========================
    //...
    //=========================

    void addNeed(Need need);

    boolean isAwareOf(Product product);

    void makeAwareOf(Product product);

    void updateProductAttributePerception(ProductAttribute attribute, double perceptionValue, double informationWeight);
}
