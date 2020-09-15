package de.unileipzig.irpact.core.agent.consumer;

import de.unileipzig.irpact.dev.ToDo;
import de.unileipzig.irpact.core.agent.SpatialInformationAgentData;
import de.unileipzig.irpact.core.need.Need;
import de.unileipzig.irpact.core.preference.Preference;
import de.unileipzig.irpact.core.product.perception.ProductAttributePerceptionSchemeManager;
import de.unileipzig.irpact.core.spatial.SpatialInformation;

import java.util.Set;

/**
 * @author Daniel Abitz
 */
public class ConsumerAgentData extends SpatialInformationAgentData {

    public ConsumerAgentData() {
    }

    public ConsumerAgentData(
            String name,
            SpatialInformation spatialInformation,
            ConsumerAgentGroup group,
            Set<ConsumerAgentAttribute> attributes,
            Set<Preference> preferences,
            ProductAttributePerceptionSchemeManager perceptionSchemeManager,
            Set<Need> initialNeeds) {
        setName(name);
        setSpatialInformation(spatialInformation);
        setGroup(group);
        setAttributes(attributes);
        setPreferences(preferences);
        setPerceptionSchemeManager(perceptionSchemeManager);
        setInitialNeeds(initialNeeds);
    }

    protected ConsumerAgentGroup group;
    public ConsumerAgentGroup getGroup() {
        return group;
    }
    public void setGroup(ConsumerAgentGroup group) {
        this.group = group;
    }

    @ToDo("koennen die sich aendern?")
    protected Set<ConsumerAgentAttribute> attributes;
    public Set<ConsumerAgentAttribute> getAttributes() {
        return attributes;
    }
    public void setAttributes(Set<ConsumerAgentAttribute> attributes) {
        this.attributes = attributes;
    }

    @ToDo("koennen die sich aendern?")
    protected Set<Preference> preferences;
    public Set<Preference> getPreferences() {
        return preferences;
    }
    public void setPreferences(Set<Preference> preferences) {
        this.preferences = preferences;
    }

    protected ProductAttributePerceptionSchemeManager perceptionSchemeManager;
    public ProductAttributePerceptionSchemeManager getPerceptionSchemeManager() {
        return perceptionSchemeManager;
    }
    public void setPerceptionSchemeManager(ProductAttributePerceptionSchemeManager perceptionSchemeManager) {
        this.perceptionSchemeManager = perceptionSchemeManager;
    }

    protected Set<Need> initialNeeds;
    public Set<Need> getInitialNeeds() {
        return initialNeeds;
    }
    public void setInitialNeeds(Set<Need> initialNeeds) {
        this.initialNeeds = initialNeeds;
    }
}
