package de.unileipzig.irpact.core.process.ra.attributes;

import de.unileipzig.irpact.commons.checksum.ChecksumComparable;
import de.unileipzig.irpact.commons.NameableBase;
import de.unileipzig.irpact.commons.distribution.UnivariateDoubleDistribution;
import de.unileipzig.irpact.core.agent.consumer.ConsumerAgentGroup;
import de.unileipzig.irpact.core.agent.consumer.attribute.ConsumerAgentDoubleGroupAttribute;
import de.unileipzig.irpact.core.agent.consumer.attribute.ConsumerAgentGroupAttribute;
import de.unileipzig.irpact.core.logging.IRPLogging;
import de.unileipzig.irpact.core.logging.IRPSection;
import de.unileipzig.irpact.core.process.ra.RAConstants;
import de.unileipzig.irptools.util.log.IRPLogger;

import java.util.*;

/**
 * @author Daniel Abitz
 */
public class BasicUncertaintyGroupAttributeSupplier extends NameableBase implements UncertaintyGroupAttributeSupplier {

    private static final IRPLogger LOGGER = IRPLogging.getLogger(BasicUncertaintyGroupAttributeSupplier.class);

    protected String attrName;
    protected UnivariateDoubleDistribution uncertDist;
    protected UnivariateDoubleDistribution convDist;

    public BasicUncertaintyGroupAttributeSupplier() {
        this(null, null, null);
    }

    public BasicUncertaintyGroupAttributeSupplier(
            String name,
            String attrName,
            UnivariateDoubleDistribution uncertDist) {
        this(name, attrName, uncertDist, null);
    }

    public BasicUncertaintyGroupAttributeSupplier(
            String name,
            String attrName,
            UnivariateDoubleDistribution uncertDist,
            UnivariateDoubleDistribution convDist) {
        setName(name);
        this.attrName = attrName;
        this.uncertDist = uncertDist;
        this.convDist = convDist;
    }

    public void setAttributeName(String attrName) {
        this.attrName = attrName;
    }

    public String getAttributeName() {
        return attrName;
    }

    public void setUncertaintyDistribution(UnivariateDoubleDistribution uncertDist) {
        this.uncertDist = uncertDist;
    }

    public UnivariateDoubleDistribution getUncertaintyDistribution() {
        return uncertDist;
    }

    public void setConvergenceDistribution(UnivariateDoubleDistribution convDist) {
        this.convDist = convDist;
    }

    public UnivariateDoubleDistribution getConvergenceDistribution() {
        return convDist;
    }

    public boolean hasConvergenceDistribution() {
        return convDist != null;
    }

    @Override
    public boolean isSupported(ConsumerAgentGroup cag) {
        return true;
    }

    @Override
    public boolean hasGroupAttribute(ConsumerAgentGroup cag) {
        String uncertName = RAConstants.getUncertaintyAttributeName(attrName);
        return cag.hasGroupAttribute(uncertName);
    }

    @Override
    public void addGroupAttributeTo(ConsumerAgentGroup cag) {
        String uncertName = RAConstants.getUncertaintyAttributeName(attrName);
        if(cag.hasGroupAttribute(uncertName)) {
            LOGGER.debug(IRPSection.INITIALIZATION_PARAMETER, "cag '{}' already has '{}'", cag.getName(), uncertName);
            return;
        }

        UncertaintyGroupAttribute cagAttr = createAttribute(cag, uncertName, uncertDist, convDist, false);
        LOGGER.trace(IRPSection.INITIALIZATION_PARAMETER, "add uncertainty attribute '{}' for '{}->{}'", cagAttr.getName(), cag.getName(), cagAttr.getName());
        cag.addGroupAttribute(cagAttr);
    }

    protected static UncertaintyGroupAttribute createAttribute(
            ConsumerAgentGroup cag,
            String attrName,
            UnivariateDoubleDistribution uncert,
            UnivariateDoubleDistribution conv,
            @SuppressWarnings("SameParameterValue") boolean autoAdjust) {
        if(conv == null) {
            LOGGER.trace(IRPSection.INITIALIZATION_PARAMETER, "create uncertainty '{}' with linked convergence '{}->{}'", uncert.getName(), cag.getName(), RAConstants.RATE_OF_CONVERGENCE);
            ConsumerAgentGroupAttribute cagConv = cag.getGroupAttribute(RAConstants.RATE_OF_CONVERGENCE);
            if(cagConv == null) {
                throw new NoSuchElementException("cag '" + cag.getName() + "' has no '" + RAConstants.RATE_OF_CONVERGENCE + "'");
            }
            if(!cagConv.is(ConsumerAgentDoubleGroupAttribute.class)) {
                throw new IllegalArgumentException("cag '" + cag.getName() + "' does not supports distributions.");
            }
            ConsumerAgentDoubleGroupAttribute cagConfD = cagConv.as(ConsumerAgentDoubleGroupAttribute.class);

            LinkedUncertaintyGroupAttribute cagAttr = new LinkedUncertaintyGroupAttribute();
            cagAttr.setName(attrName);
            cagAttr.setUncertainty(uncert);
            cagAttr.setConvergence(cagConfD);
            cagAttr.setAutoAdjustment(autoAdjust);
            return cagAttr;
        } else {
            LOGGER.trace(IRPSection.INITIALIZATION_PARAMETER, "create uncertainty '{}' with convergence '{}'", uncert.getName(), conv.getName());
            UncertaintyWithConvergenceGroupAttribute cagAttr = new UncertaintyWithConvergenceGroupAttribute();
            cagAttr.setName(attrName);
            cagAttr.setUncertainty(uncert);
            cagAttr.setConvergence(conv);
            return cagAttr;
        }
    }

    @Override
    public int getChecksum() {
        return ChecksumComparable.getChecksum(
                getName(),
                attrName,
                uncertDist,
                convDist
        );
    }
}
