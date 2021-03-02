package de.unileipzig.irpact.core.agent.consumer;

import de.unileipzig.irpact.commons.IsEquals;
import de.unileipzig.irpact.commons.NameableBase;
import de.unileipzig.irpact.commons.distribution.UnivariateDoubleDistribution;
import de.unileipzig.irpact.core.log.IRPLogging;
import de.unileipzig.irpact.core.log.IRPSection;
import de.unileipzig.irpact.core.spatial.SpatialInformation;
import de.unileipzig.irpact.core.spatial.attribute.SpatialDoubleAttributeBase;
import de.unileipzig.irptools.util.log.IRPLogger;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

/**
 * @author Daniel Abitz
 */
public class BasicConsumerAgentSpatialAttributeSupplier extends NameableBase implements ConsumerAgentSpatialAttributeSupplier {

    private static final IRPLogger LOGGER = IRPLogging.getLogger(BasicConsumerAgentSpatialAttributeSupplier.class);

    protected String attributeName;
    protected Map<ConsumerAgentGroup, UnivariateDoubleDistribution> distMapping;

    public BasicConsumerAgentSpatialAttributeSupplier() {
        this(null);
    }

    public BasicConsumerAgentSpatialAttributeSupplier(String attributeName) {
        this(attributeName, new LinkedHashMap<>());
    }

    public BasicConsumerAgentSpatialAttributeSupplier(String attributeName, Map<ConsumerAgentGroup, UnivariateDoubleDistribution> distMapping) {
        this.attributeName = attributeName;
        this.distMapping = distMapping;
    }

    public void setAttributeName(String attributeName) {
        this.attributeName = attributeName;
    }

    public void put(ConsumerAgentGroup cag, UnivariateDoubleDistribution dist) {
        distMapping.put(cag, dist);
    }

    public void putAll(BasicConsumerAgentSpatialAttributeSupplier other) {
        for(Map.Entry<ConsumerAgentGroup, UnivariateDoubleDistribution> entry: other.distMapping.entrySet()) {
            if(distMapping.containsKey(entry.getKey())) {
                throw new IllegalArgumentException("cag '" + entry.getKey().getName() + "' already exists");
            }
            distMapping.put(entry.getKey(), entry.getValue());
        }
    }

    public Map<ConsumerAgentGroup, UnivariateDoubleDistribution> getMapping() {
        return distMapping;
    }

    @Override
    public boolean hasAttribute(ConsumerAgent ca) {
        SpatialInformation info = ca.getSpatialInformation();
        if(info == null) {
            throw new NullPointerException("agent '" + ca.getName() + "' has no info");
        }
        return info.hasAttribute(attributeName);
    }

    @Override
    public void addAttributeTo(ConsumerAgent ca) {
        SpatialInformation info = ca.getSpatialInformation();
        if(info == null) {
            throw new NullPointerException("agent '" + ca.getName() + "' has no info");
        }
        if(info.hasAttribute(attributeName)) {
            throw new IllegalArgumentException("agent '" + ca.getName() + "' already has '" + attributeName + "'");
        }
        UnivariateDoubleDistribution dist = distMapping.get(ca.getGroup());
        if(dist == null) {
            throw new NullPointerException("no distribution for consumer group '" + ca.getGroup().getName() + "'");
        }
        SpatialDoubleAttributeBase attr = new SpatialDoubleAttributeBase();
        attr.setName(attributeName);
        attr.setDoubleValue(dist.drawDoubleValue());
        info.addAttribute(attr);

        LOGGER.trace(IRPSection.INITIALIZATION_PARAMETER, "added '{}={}' to agent '{}'", attributeName, attr.getDoubleValue(), ca.getName());
    }

    @Override
    public int getHashCode() {
        return Objects.hash(
                getName(),
                attributeName,
                IsEquals.getMapHashCode(distMapping)
        );
    }
}
