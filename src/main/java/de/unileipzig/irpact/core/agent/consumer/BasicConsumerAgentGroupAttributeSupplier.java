package de.unileipzig.irpact.core.agent.consumer;

import de.unileipzig.irpact.commons.IsEquals;
import de.unileipzig.irpact.commons.NameableBase;
import de.unileipzig.irpact.commons.distribution.UnivariateDoubleDistribution;
import de.unileipzig.irpact.core.log.IRPLogging;
import de.unileipzig.irpact.core.log.IRPSection;
import de.unileipzig.irptools.util.log.IRPLogger;

import java.util.Objects;

/**
 * @author Daniel Abitz
 */
public class BasicConsumerAgentGroupAttributeSupplier extends NameableBase implements ConsumerAgentGroupAttributeSupplier {

    private static final IRPLogger LOGGER = IRPLogging.getLogger(BasicConsumerAgentGroupAttributeSupplier.class);

    protected String attributeName;
    protected UnivariateDoubleDistribution defaultDist;

    public BasicConsumerAgentGroupAttributeSupplier() {
        this(null);
    }

    public BasicConsumerAgentGroupAttributeSupplier(String attributeName) {
        this(attributeName, null);
    }

    public BasicConsumerAgentGroupAttributeSupplier(String attributeName, UnivariateDoubleDistribution defaultDist) {
        this.attributeName = attributeName;
        setDefaultDisttribution(defaultDist);
    }

    public void setAttributeName(String attributeName) {
        this.attributeName = attributeName;
    }

    public String getAttributeName() {
        return attributeName;
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

    @Override
    public boolean hasGroupAttribute(ConsumerAgentGroup cag) {
        return cag.hasGroupAttribute(attributeName);
    }

    @Override
    public void addGroupAttributeTo(ConsumerAgentGroup cag) {
        if(cag.hasGroupAttribute(attributeName)) {
            throw new IllegalArgumentException("agent group '" + cag.getName() + "' already has '" + attributeName + "'");
        }

        if(defaultDist == null) {
            throw new NullPointerException("no distribution for consumer group '" + cag.getName() + "'");
        }

        BasicConsumerAgentGroupAttribute grpAttr = new BasicConsumerAgentGroupAttribute();
        grpAttr.setName(attributeName);
        grpAttr.setDistribution(defaultDist);
        cag.addGroupAttribute(grpAttr);

        LOGGER.trace(IRPSection.INITIALIZATION_PARAMETER, "added '{}={}' to group '{}'", attributeName, defaultDist.getName(), cag.getName());
    }

    @Override
    public int getHashCode() {
        return Objects.hash(
                getName(),
                attributeName,
                IsEquals.getHashCode(defaultDist)
        );
    }
}
