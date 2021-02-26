package de.unileipzig.irpact.core.agent.consumer;

import de.unileipzig.irpact.commons.IsEquals;
import de.unileipzig.irpact.commons.NameableBase;
import de.unileipzig.irpact.commons.distribution.UnivariateDoubleDistribution;
import de.unileipzig.irpact.core.log.IRPLogging;
import de.unileipzig.irpact.core.log.IRPSection;
import de.unileipzig.irptools.util.log.IRPLogger;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * @author Daniel Abitz
 */
public class BasicConsumerAgentGroupAttributeSupplier extends NameableBase implements ConsumerAgentGroupAttributeSupplier {

    private static final IRPLogger LOGGER = IRPLogging.getLogger(BasicConsumerAgentGroupAttributeSupplier.class);

    protected String attributeName;
    protected Map<ConsumerAgentGroup, UnivariateDoubleDistribution> distMapping;
    protected UnivariateDoubleDistribution defaultDist; //wird genutzt

    public BasicConsumerAgentGroupAttributeSupplier() {
        this(null);
    }

    public BasicConsumerAgentGroupAttributeSupplier(String attributeName) {
        this(attributeName, new HashMap<>());
    }

    public BasicConsumerAgentGroupAttributeSupplier(String attributeName, UnivariateDoubleDistribution defaultDist) {
        this(attributeName, new HashMap<>());
        setDefaultDisttribution(defaultDist);
    }

    public BasicConsumerAgentGroupAttributeSupplier(String attributeName, Map<ConsumerAgentGroup, UnivariateDoubleDistribution> distMapping) {
        this.attributeName = attributeName;
        this.distMapping = distMapping;
    }

    public void setAttributeName(String attributeName) {
        this.attributeName = attributeName;
    }

    public String getAttributeName() {
        return attributeName;
    }

    public Map<ConsumerAgentGroup, UnivariateDoubleDistribution> getMapping() {
        return distMapping;
    }

    public void put(ConsumerAgentGroup cag, UnivariateDoubleDistribution dist) {
        distMapping.put(cag, dist);
    }

    public boolean hasDefaultDisttribution() {
        return defaultDist != null;
    }

    public void setDefaultDisttribution(UnivariateDoubleDistribution defaultDist) {
        this.defaultDist = defaultDist;
    }

    public UnivariateDoubleDistribution getDefaultDisttribution() {
        return defaultDist;
    }

    public void putAll(BasicConsumerAgentGroupAttributeSupplier other) {
        for(Map.Entry<ConsumerAgentGroup, UnivariateDoubleDistribution> entry: other.distMapping.entrySet()) {
            if(distMapping.containsKey(entry.getKey())) {
                throw new IllegalArgumentException("cag '" + entry.getKey().getName() + "' already exists");
            }
            distMapping.put(entry.getKey(), entry.getValue());
        }
    }

    @Override
    public boolean hasGroupAttribute(ConsumerAgentGroup cag) {
        return cag.hasGroupAttribute(attributeName);
    }

    @Override
    public void addGroupAttributeTo(ConsumerAgentGroup cag) {
        if(cag.hasGroupAttribute(attributeName)) {
            throw new IllegalArgumentException("agent group '" + cag.getName() + "' already has '" + attributeName + "'");
        }

        UnivariateDoubleDistribution dist = defaultDist == null
                ? distMapping.get(cag)
                : defaultDist;
        if(dist == null) {
            throw new NullPointerException("no distribution for consumer group '" + cag.getName() + "'");
        }

        BasicConsumerAgentGroupAttribute grpAttr = new BasicConsumerAgentGroupAttribute();
        grpAttr.setName(attributeName);
        grpAttr.setDistribution(dist);
        cag.addGroupAttribute(grpAttr);

        LOGGER.trace(IRPSection.INITIALIZATION_PARAMETER, "added '{}={}' to group '{}'", attributeName, dist.getName(), cag.getName());
    }

    @Override
    public int getHashCode() {
        return Objects.hash(
                getName(),
                attributeName,
                IsEquals.getHashCode(defaultDist),
                IsEquals.getMapHashCode(distMapping)
        );
    }
}
