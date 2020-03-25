package de.unileipzig.irpact.mock;

import de.unileipzig.irpact.commons.distribution.UnivariateDistribution;
import de.unileipzig.irpact.core.agent.consumer.*;
import de.unileipzig.irpact.core.need.NeedDevelopmentScheme;
import de.unileipzig.irpact.core.need.NeedExpirationScheme;
import de.unileipzig.irpact.core.need.NeedSatisfyScheme;
import de.unileipzig.irpact.core.preference.Value;
import de.unileipzig.irpact.core.simulation.EntityType;
import de.unileipzig.irpact.core.simulation.SimulationEnvironment;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

/**
 * @author Daniel Abitz
 */
public class MockConsumerAgentGroup implements ConsumerAgentGroup {

    private String name;

    public MockConsumerAgentGroup() {
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public double getInformationAuthority() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Set<ConsumerAgentGroupAttribute> getAttributes() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Map<Value, UnivariateDistribution> getValuePreferences() {
        throw new UnsupportedOperationException();
    }

    @Override
    public ProductFindingScheme getFindingScheme() {
        throw new UnsupportedOperationException();
    }

    @Override
    public ProductAdoptionDecisionScheme getAdoptionDecisionScheme() {
        throw new UnsupportedOperationException();
    }

    @Override
    public NeedDevelopmentScheme getNeedDevelopmentScheme() {
        throw new UnsupportedOperationException();
    }

    @Override
    public NeedExpirationScheme getNeedExpirationScheme() {
        throw new UnsupportedOperationException();
    }

    @Override
    public NeedSatisfyScheme getNeedSatisfyScheme() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Collection<ConsumerAgent> getEntities() {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean addEntity(ConsumerAgent entitiy) {
        throw new UnsupportedOperationException();
    }

    @Override
    public SimulationEnvironment getEnvironment() {
        throw new UnsupportedOperationException();
    }

    @Override
    public String getName() {
        if(name == null) {
            throw new UnsupportedOperationException();
        }
        return name;
    }

    @Override
    public boolean is(EntityType type) {
        throw new UnsupportedOperationException();
    }

    @Override
    public String toString() {
        return name == null
                ? super.toString()
                : name;
    }
}
