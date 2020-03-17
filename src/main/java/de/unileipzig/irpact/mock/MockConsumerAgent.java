package de.unileipzig.irpact.mock;

import de.unileipzig.irpact.core.agent.consumer.ConsumerAgent;
import de.unileipzig.irpact.core.agent.consumer.ConsumerAgentAttribute;
import de.unileipzig.irpact.core.agent.consumer.ConsumerAgentGroup;
import de.unileipzig.irpact.core.need.Need;
import de.unileipzig.irpact.core.product.Product;
import de.unileipzig.irpact.core.product.ProductGroup;
import de.unileipzig.irpact.core.simulation.EntityType;
import de.unileipzig.irpact.core.simulation.SimulationEnvironment;
import de.unileipzig.irpact.core.spatial.SpatialInformation;

import java.util.Set;

/**
 * @author Daniel Abitz
 */
public class MockConsumerAgent implements ConsumerAgent {

    private String name;
    private SimulationEnvironment env;
    private SpatialInformation spatialInformation;

    public MockConsumerAgent(String name) {
        this.name = name;
    }

    public MockConsumerAgent(String name, SimulationEnvironment env, SpatialInformation spatialInformation) {
        this(name);
        this.env = env;
        this.spatialInformation = spatialInformation;
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
    public ConsumerAgentGroup getGroup() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Set<ConsumerAgentAttribute> getAttributes() {
        throw new UnsupportedOperationException();
    }

    @Override
    public double getInformationAuthority() {
        throw new UnsupportedOperationException();
    }

    @Override
    public SpatialInformation getSpatialInformation() {
        if(spatialInformation == null) {
            throw new UnsupportedOperationException();
        }
        return spatialInformation;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public SimulationEnvironment getEnvironment() {
        if(env == null) {
            throw new UnsupportedOperationException();
        }
        return env;
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
