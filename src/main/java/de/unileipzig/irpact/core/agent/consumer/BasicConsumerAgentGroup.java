package de.unileipzig.irpact.core.agent.consumer;

import de.unileipzig.irpact.commons.Check;
import de.unileipzig.irpact.commons.annotation.ToImpl;
import de.unileipzig.irpact.commons.distribution.UnivariateDistribution;
import de.unileipzig.irpact.core.AbstractGroup;
import de.unileipzig.irpact.core.need.NeedDevelopmentScheme;
import de.unileipzig.irpact.core.need.NeedExpirationScheme;
import de.unileipzig.irpact.core.need.NeedSatisfyScheme;
import de.unileipzig.irpact.core.preference.BasicPreference;
import de.unileipzig.irpact.core.preference.Preference;
import de.unileipzig.irpact.core.preference.Value;
import de.unileipzig.irpact.core.simulation.EntityType;
import de.unileipzig.irpact.core.simulation.SimulationEnvironment;
import de.unileipzig.irpact.core.spatial.SpatialDistribution;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * @author Daniel Abitz
 */
@ToImpl("perception")
public class BasicConsumerAgentGroup extends AbstractGroup<ConsumerAgent> implements ConsumerAgentGroup {

    protected Set<ConsumerAgentGroupAttribute> attributes;
    protected Map<Value, UnivariateDistribution> valuePreferences;
    protected SpatialDistribution spatialDistribution;
    protected ProductFindingScheme findingScheme;
    protected ProductAdoptionDecisionScheme adoptionDecisionScheme;
    protected NeedDevelopmentScheme needDevelopmentScheme;
    protected NeedExpirationScheme needExpirationScheme;
    protected NeedSatisfyScheme needSatisfyScheme;
    protected double informationAuthority;

    public BasicConsumerAgentGroup(
            SimulationEnvironment environment,
            String name,
            Set<ConsumerAgent> agents,
            Set<ConsumerAgentGroupAttribute> attributes,
            Map<Value, UnivariateDistribution> valuePreferences,
            double informationAuthority,
            SpatialDistribution spatialDistribution,
            ProductFindingScheme findingScheme,
            ProductAdoptionDecisionScheme adoptionDecisionScheme,
            NeedDevelopmentScheme needDevelopmentScheme,
            NeedExpirationScheme needExpirationScheme,
            NeedSatisfyScheme needSatisfyScheme) {
        super(environment, name, agents);
        this.informationAuthority = informationAuthority;
        this.attributes = Check.requireNonNull(attributes, "attributes");
        this.valuePreferences = Check.requireNonNull(valuePreferences, "valuePreferences");
        this.spatialDistribution = Check.requireNonNull(spatialDistribution, "spatialDistribution");
        this.findingScheme = Check.requireNonNull(findingScheme, "findingScheme");
        this.adoptionDecisionScheme = Check.requireNonNull(adoptionDecisionScheme, "adoptionDecisionScheme");
        this.needDevelopmentScheme = Check.requireNonNull(needDevelopmentScheme, "needDevelopmentScheme");
        this.needExpirationScheme = Check.requireNonNull(needExpirationScheme, "needExpirationScheme");
        this.needSatisfyScheme = Check.requireNonNull(needSatisfyScheme, "needSatisfyScheme");
    }

    @Override
    public boolean is(EntityType type) {
        switch (type) {
            case AGENT_GROUP:
            case CONSUMER_AGENT_GROUP:
                return true;

            default:
                return false;
        }
    }

    @Override
    public double getInformationAuthority() {
        return informationAuthority;
    }

    @Override
    public Set<ConsumerAgentGroupAttribute> getAttributes() {
        return attributes;
    }

    @Override
    public Map<Value, UnivariateDistribution> getValuePreferences() {
        return valuePreferences;
    }

    @Override
    public boolean addEntity(ConsumerAgent entitiy) {
        if(entitiy.getGroup() != this) {
            return false;
        }
        return entities.add(entitiy);
    }

    @Override
    public ProductFindingScheme getFindingScheme() {
        return findingScheme;
    }

    @Override
    public ProductAdoptionDecisionScheme getAdoptionDecisionScheme() {
        return adoptionDecisionScheme;
    }

    @Override
    public NeedDevelopmentScheme getNeedDevelopmentScheme() {
        return needDevelopmentScheme;
    }

    @Override
    public NeedExpirationScheme getNeedExpirationScheme() {
        return needExpirationScheme;
    }

    @Override
    public NeedSatisfyScheme getNeedSatisfyScheme() {
        return needSatisfyScheme;
    }

    protected int productId = 0;
    protected synchronized String deriveName() {
        String name = getName() + "#" + productId;
        productId++;
        return name;
    }

    protected Set<ConsumerAgentAttribute> deriveAttributes() {
        Set<ConsumerAgentAttribute> attributes = new HashSet<>();
        for(ConsumerAgentGroupAttribute groupAttribute: getAttributes()) {
            attributes.add(groupAttribute.derive());
        }
        return attributes;
    }

    protected Set<Preference> derivePreferences() {
        Set<Preference> preferences = new HashSet<>();
        for(Map.Entry<Value, UnivariateDistribution> entry: valuePreferences.entrySet()) {
            Value value = entry.getKey();
            double strength = entry.getValue().drawValue();
            Preference preference = new BasicPreference(value, strength);
            preferences.add(preference);
        }
        return preferences;
    }

    public ConsumerAgent deriveAgent() {
        String derivedName = deriveName();
        return new ConsumerAgentBase(
                getEnvironment(),
                derivedName,
                spatialDistribution.drawValue(),
                this,
                deriveAttributes(),
                derivePreferences()
        );
    }
}
