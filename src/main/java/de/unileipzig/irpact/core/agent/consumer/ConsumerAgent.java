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

    void addNeed(Need need);

    ProductAttributePerceptionScheme getScheme(ProductAttribute attribute);
}
