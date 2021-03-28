package de.unileipzig.irpact.core.process.ra.attributes;

import de.unileipzig.irpact.commons.ChecksumComparable;
import de.unileipzig.irpact.commons.NameableBase;
import de.unileipzig.irpact.commons.distribution.UnivariateDoubleDistribution;
import de.unileipzig.irpact.core.agent.consumer.ConsumerAgentGroup;
import de.unileipzig.irpact.core.agent.consumer.ConsumerAgentGroupAttribute;
import de.unileipzig.irpact.core.log.IRPLogging;
import de.unileipzig.irpact.core.log.IRPSection;
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
        LOGGER.debug(IRPSection.INITIALIZATION_PARAMETER, "add uncertainty attribute '{}' for '{}->{}'", cagAttr, cag.getName(), cagAttr.getName());
        cag.addGroupAttribute(cagAttr);
    }

    protected static UncertaintyGroupAttribute createAttribute(
            ConsumerAgentGroup cag,
            String attrName,
            UnivariateDoubleDistribution uncert,
            UnivariateDoubleDistribution conv,
            @SuppressWarnings("SameParameterValue") boolean autoAdjust) {
        if(conv == null) {
            LOGGER.debug(IRPSection.INITIALIZATION_PARAMETER, "create uncertainty '{}' with linked convergence '{}->{}'", uncert.getName(), cag.getName(), RAConstants.RATE_OF_CONVERGENCE);
            ConsumerAgentGroupAttribute cagConv = cag.getGroupAttribute(RAConstants.RATE_OF_CONVERGENCE);
            if(cagConv == null) {
                throw new NoSuchElementException("cag '" + cag.getName() + "' has no '" + RAConstants.RATE_OF_CONVERGENCE + "'");
            }

            LinkedUncertaintyGroupAttribute cagAttr = new LinkedUncertaintyGroupAttribute();
            cagAttr.setName(attrName);
            cagAttr.setUncertainty(uncert);
            cagAttr.setConvergence(cagConv);
            cagAttr.setAutoAdjustment(autoAdjust);
            return cagAttr;
        } else {
            LOGGER.debug(IRPSection.INITIALIZATION_PARAMETER, "create uncertainty '{}' with convergence '{}'", uncert.getName(), conv.getName());
            UncertaintyWithConvergenceGroupAttribute cagAttr = new UncertaintyWithConvergenceGroupAttribute();
            cagAttr.setName(attrName);
            cagAttr.setUncertainty(uncert);
            cagAttr.setConvergence(conv);
            cagAttr.setAutoAdjustment(autoAdjust);
            return cagAttr;
        }
    }

    @Override
    public int getChecksum() {
        return Objects.hash(
                getName(),
                attrName,
                ChecksumComparable.getNameChecksum(uncertDist),
                ChecksumComparable.getNameChecksum(convDist)
        );
    }
}
