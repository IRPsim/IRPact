package de.unileipzig.irpact.mock;

import de.unileipzig.irpact.core.agent.consumer.ConsumerAgent;
import de.unileipzig.irpact.core.agent.consumer.ConsumerAgentAttribute;
import de.unileipzig.irpact.core.agent.consumer.ConsumerAgentGroup;
import de.unileipzig.irpact.core.agent.consumer.ConsumerAgentIdentifier;
import de.unileipzig.irpact.core.need.Need;
import de.unileipzig.irpact.core.product.Product;
import de.unileipzig.irpact.core.product.ProductGroup;
import de.unileipzig.irpact.core.simulation.Identifier;
import de.unileipzig.irpact.core.simulation.SimulationEnvironment;
import de.unileipzig.irpact.core.spatial.SpatialInformation;

import java.util.Set;

/**
 * @author Daniel Abitz
 */
public class MockConsumerAgent implements ConsumerAgent {

    private String name;

    public MockConsumerAgent(String name) {
        this.name = name;
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
    public ConsumerAgentIdentifier getIdentifier() {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean is(Identifier identifier) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean isType(Identifier identifier) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean isType(Class<? extends Identifier> type) {
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
        throw new UnsupportedOperationException();
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public SimulationEnvironment getEnvironment() {
        throw new UnsupportedOperationException();
    }
}
