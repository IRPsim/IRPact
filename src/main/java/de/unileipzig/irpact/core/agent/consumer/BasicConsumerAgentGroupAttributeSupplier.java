package de.unileipzig.irpact.core.agent.consumer;

import de.unileipzig.irpact.commons.NameableBase;
import de.unileipzig.irpact.commons.distribution.UnivariateDoubleDistribution;
import de.unileipzig.irpact.core.log.IRPLogging;
import de.unileipzig.irpact.core.log.IRPSection;
import de.unileipzig.irptools.util.log.IRPLogger;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Daniel Abitz
 */
public class BasicConsumerAgentGroupAttributeSupplier extends NameableBase implements ConsumerAgentGroupAttributeSupplier {

    private static final IRPLogger LOGGER = IRPLogging.getLogger(BasicConsumerAgentGroupAttributeSupplier.class);

    protected String attributeName;
    protected Map<ConsumerAgentGroup, UnivariateDoubleDistribution> distMapping;
    protected UnivariateDoubleDistribution general;

    public BasicConsumerAgentGroupAttributeSupplier() {
        this(null);
    }

    public BasicConsumerAgentGroupAttributeSupplier(String attributeName) {
        this(attributeName, new HashMap<>());
    }

    public BasicConsumerAgentGroupAttributeSupplier(String attributeName, UnivariateDoubleDistribution general) {
        this(attributeName, new HashMap<>());
        setGeneral(general);
    }

    public BasicConsumerAgentGroupAttributeSupplier(String attributeName, Map<ConsumerAgentGroup, UnivariateDoubleDistribution> distMapping) {
        this.attributeName = attributeName;
        this.distMapping = distMapping;
    }

    public void setAttributeName(String attributeName) {
        this.attributeName = attributeName;
    }

    public void put(ConsumerAgentGroup cag, UnivariateDoubleDistribution dist) {
        distMapping.put(cag, dist);
    }

    public void setGeneral(UnivariateDoubleDistribution general) {
        this.general = general;
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

        UnivariateDoubleDistribution dist = general == null
                ? distMapping.get(cag)
                : general;
        if(dist == null) {
            throw new NullPointerException("no distribution for consumer group '" + cag.getName() + "'");
        }

        BasicConsumerAgentGroupAttribute grpAttr = new BasicConsumerAgentGroupAttribute();
        grpAttr.setName(attributeName);
        grpAttr.setDistribution(dist);
        cag.addGroupAttribute(grpAttr);

        LOGGER.trace(IRPSection.INITIALIZATION_PARAMETER, "added '{}={}' to group '{}'", attributeName, dist.getName(), cag.getName());
    }
}
