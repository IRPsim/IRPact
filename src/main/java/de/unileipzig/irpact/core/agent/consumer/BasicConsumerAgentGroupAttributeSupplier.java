package de.unileipzig.irpact.core.agent.consumer;

import de.unileipzig.irpact.commons.ChecksumComparable;
import de.unileipzig.irpact.commons.NameableBase;
import de.unileipzig.irpact.commons.distribution.UnivariateDoubleDistribution;
import de.unileipzig.irpact.core.log.IRPLogging;
import de.unileipzig.irpact.core.log.IRPSection;
import de.unileipzig.irptools.util.log.IRPLogger;

/**
 * @author Daniel Abitz
 */
public class BasicConsumerAgentGroupAttributeSupplier extends NameableBase implements ConsumerAgentGroupAttributeSupplier {

    private static final IRPLogger LOGGER = IRPLogging.getLogger(BasicConsumerAgentGroupAttributeSupplier.class);

    protected String attributeName;
    protected UnivariateDoubleDistribution dist;

    public BasicConsumerAgentGroupAttributeSupplier() {
        this(null);
    }

    public BasicConsumerAgentGroupAttributeSupplier(String attributeName) {
        this(attributeName, null);
    }

    public BasicConsumerAgentGroupAttributeSupplier(String attributeName, UnivariateDoubleDistribution defaultDist) {
        this.attributeName = attributeName;
        setDistribution(defaultDist);
    }

    public void setAttributeName(String attributeName) {
        this.attributeName = attributeName;
    }

    public String getAttributeName() {
        return attributeName;
    }

    public boolean hasDistribution() {
        return dist != null;
    }

    public void setDistribution(UnivariateDoubleDistribution dist) {
        this.dist = dist;
    }

    public UnivariateDoubleDistribution getDistribution() {
        return dist;
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

        if(dist == null) {
            throw new NullPointerException("no distribution for consumer group '" + cag.getName() + "'");
        }

        BasicConsumerAgentGroupAttribute grpAttr = new BasicConsumerAgentGroupAttribute();
        grpAttr.setName(attributeName);
        grpAttr.setUnivariateDoubleDistributionValue(dist);
        grpAttr.setArtificial(true);
        cag.addGroupAttribute(grpAttr);

        LOGGER.trace(IRPSection.INITIALIZATION_PARAMETER, "add artificial cag attribute '{}' with '{}' to group '{}'", attributeName, dist.getName(), cag.getName());
    }

    @Override
    public int getChecksum() {
        return ChecksumComparable.getChecksum(
                getName(),
                getAttributeName(),
                dist
        );
    }
}
