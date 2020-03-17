package de.unileipzig.irpact.core.agent.consumer;

import de.unileipzig.irpact.commons.Check;
import de.unileipzig.irpact.commons.annotation.ToDo;
import de.unileipzig.irpact.core.agent.SpatialInformationAgentBase;
import de.unileipzig.irpact.core.need.Need;
import de.unileipzig.irpact.core.preference.Preference;
import de.unileipzig.irpact.core.product.Product;
import de.unileipzig.irpact.core.product.ProductGroup;
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

    public ConsumerAgentBase(
            SimulationEnvironment environment,
            String name,
            SpatialInformation spatialInformation,
            ConsumerAgentGroup group,
            Set<ConsumerAgentAttribute> attributes,
            Set<Preference> preferences) {
        super(environment, name, group.getInformationAuthority(), spatialInformation);
        this.group = Check.requireNonNull(group, "group");
        this.attributes = Check.requireNonNull(attributes, "attributes");
        this.preferences = Check.requireNonNull(preferences, "preferences");
    }

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
    public Set<ProductGroup> getKnownProductGroups() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Set<Preference> getPreferences() {
        return preferences;
    }

    @Override
    public void addNeed(Need need) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean is(EntityType type) {
        throw new UnsupportedOperationException();
    }
}
