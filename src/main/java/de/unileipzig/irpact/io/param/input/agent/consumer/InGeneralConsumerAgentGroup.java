package de.unileipzig.irpact.io.param.input.agent.consumer;

import de.unileipzig.irpact.commons.exception.ParsingException;
import de.unileipzig.irpact.core.agent.AgentManager;
import de.unileipzig.irpact.core.agent.consumer.attribute.ConsumerAgentGroupAttribute;
import de.unileipzig.irpact.core.logging.IRPLogging;
import de.unileipzig.irpact.core.logging.IRPSection;
import de.unileipzig.irpact.core.product.ProductFindingScheme;
import de.unileipzig.irpact.core.product.interest.ProductInterestSupplyScheme;
import de.unileipzig.irpact.io.param.ParamUtil;
import de.unileipzig.irpact.io.param.input.IRPactInputParser;
import de.unileipzig.irpact.io.param.input.interest.InProductInterestSupplyScheme;
import de.unileipzig.irpact.io.param.input.product.InProductFindingScheme;
import de.unileipzig.irpact.io.param.input.spatial.dist.InSpatialDistribution;
import de.unileipzig.irpact.jadex.agents.consumer.JadexConsumerAgentGroup;
import de.unileipzig.irptools.defstructure.annotation.Definition;
import de.unileipzig.irptools.defstructure.annotation.FieldDefinition;
import de.unileipzig.irptools.util.CopyCache;
import de.unileipzig.irptools.util.TreeAnnotationResource;
import de.unileipzig.irptools.util.log.IRPLogger;

import java.lang.invoke.MethodHandles;
import java.util.Collection;

import static de.unileipzig.irpact.io.param.IOConstants.*;
import static de.unileipzig.irpact.io.param.ParamUtil.addEntry;
import static de.unileipzig.irpact.io.param.ParamUtil.putClassPath;

/**
 * @author Daniel Abitz
 */
@Definition
public class InGeneralConsumerAgentGroup implements InConsumerAgentGroup {

    private static final MethodHandles.Lookup L = MethodHandles.lookup();
    public static Class<?> thisClass() {
        return L.lookupClass();
    }
    public static String thisName() {
        return thisClass().getSimpleName();
    }

    public static void initRes(TreeAnnotationResource res) {
    }
    public static void applyRes(TreeAnnotationResource res) {
        putClassPath(res, thisClass(), AGENTS, CONSUMER, CONSUMER_GROUP, thisName());
        addEntry(res, thisClass(), "cagAttributes");
        addEntry(res, thisClass(), "cagInterest");
        addEntry(res, thisClass(), "productFindingSchemes");
        addEntry(res, thisClass(), "spatialDistribution");
    }

    private static final IRPLogger LOGGER = IRPLogging.getLogger(thisClass());

    public String _name;

    @FieldDefinition
    public InDependentConsumerAgentGroupAttribute[] cagAttributes;

    @FieldDefinition
    public InProductInterestSupplyScheme[] cagInterest;

    @FieldDefinition
    public InProductFindingScheme[] productFindingSchemes;

    @FieldDefinition
    public InSpatialDistribution[] spatialDistribution;

    public InGeneralConsumerAgentGroup() {
    }

    @Override
    public InGeneralConsumerAgentGroup copy(CopyCache cache) {
        return cache.copyIfAbsent(this, this::newCopy);
    }

    public InGeneralConsumerAgentGroup newCopy(CopyCache cache) {
        InGeneralConsumerAgentGroup copy = new InGeneralConsumerAgentGroup();
        copy._name = _name;
        copy.cagAttributes = cache.copyArray(cagAttributes);
        copy.cagInterest = cache.copyArray(cagInterest);
        copy.productFindingSchemes = cache.copyArray(productFindingSchemes);
        copy.spatialDistribution = cache.copyArray(spatialDistribution);
        return copy;
    }

    @Override
    public String getName() {
        return _name;
    }

    public void setName(String _name) {
        this._name = _name;
    }

    public InDependentConsumerAgentGroupAttribute[] getAttributes() throws ParsingException {
        return ParamUtil.getNonNullArray(cagAttributes, "cagAttributes");
    }

    public void setAttributes(Collection<? extends InDependentConsumerAgentGroupAttribute> attrs) {
        this.cagAttributes = attrs.toArray(new InDependentConsumerAgentGroupAttribute[0]);
    }

    public boolean hasAttribute(InDependentConsumerAgentGroupAttribute attr) throws ParsingException {
        for(InDependentConsumerAgentGroupAttribute a: getAttributes()) {
            if(a == attr) {
                return true;
            }
        }
        return false;
    }

    public InProductInterestSupplyScheme getInterest() throws ParsingException {
        return ParamUtil.getInstance(cagInterest, "cagInterest");
    }

    public void setInterest(InProductInterestSupplyScheme awareness) {
        this.cagInterest = new InProductInterestSupplyScheme[]{awareness};
    }

    public InProductFindingScheme getProductFindingScheme() throws ParsingException {
        return ParamUtil.getInstance(productFindingSchemes, "productFindingSchemes");
    }

    public void setProductFindingScheme(InProductFindingScheme scheme) {
        productFindingSchemes = new InProductFindingScheme[]{scheme};
    }

    @Override
    public InSpatialDistribution getSpatialDistribution() throws ParsingException {
        return ParamUtil.getInstance(spatialDistribution, "spatialDistribution");
    }

    @Override
    public void setSpatialDistribution(InSpatialDistribution dist) {
        this.spatialDistribution = new InSpatialDistribution[]{dist};
    }

    @Override
    public JadexConsumerAgentGroup parse(IRPactInputParser parser) throws ParsingException {
        AgentManager agentManager = parser.getEnvironment().getAgents();

        if(parser.getEnvironment().isRestored() && agentManager.hasConsumerAgentGroup(getName())) {
            JadexConsumerAgentGroup jcag = (JadexConsumerAgentGroup) agentManager.getConsumerAgentGroup(getName());
            return update(parser, jcag);
        }

        LOGGER.trace(IRPSection.INITIALIZATION_PARAMETER, "parse InGeneralConsumerAgentGroup '{}'", getName());

        JadexConsumerAgentGroup jCag = new JadexConsumerAgentGroup();
        jCag.setEnvironment(parser.getEnvironment());
        jCag.setName(getName());
        jCag.setInformationAuthority(1.0);
        jCag.setMaxNumberOfActions(1);

        if(agentManager.hasConsumerAgentGroup(getName())) {
            throw new ParsingException("ConsumerAgentGroup '" + getName() + "' already exists");
        }

        if(parser.getRoot().addConsumerAgentGroup(this)) {
            LOGGER.trace(IRPSection.INITIALIZATION_PARAMETER, "added InGeneralConsumerAgentGroup '{}' to InRoot", getName());
            agentManager.addConsumerAgentGroup(jCag);
            LOGGER.trace(IRPSection.INITIALIZATION_PARAMETER, "added JadexConsumerAgentGroup '{}'", jCag.getName());
        }

        ProductInterestSupplyScheme awarenessSupplyScheme = parser.parseEntityTo(getInterest());
        jCag.setInterestSupplyScheme(awarenessSupplyScheme);

        ProductFindingScheme productFindingScheme = parser.parseEntityTo(getProductFindingScheme());
        jCag.setProductFindingScheme(productFindingScheme);

        getSpatialDistribution().setup(parser, jCag);

        for(InConsumerAgentGroupAttribute inCagAttr: getAttributes()) {
            if(inCagAttr.requiresSetup()) {
                inCagAttr.setup(parser, jCag);
            } else {
                ConsumerAgentGroupAttribute cagAttr = parser.parseEntityTo(inCagAttr);
                if(jCag.hasGroupAttribute(cagAttr.getName())) {
                    throw new ParsingException("ConsumerAgentGroupAttribute '" + cagAttr.getName() + "' already exists in " + jCag.getName());
                }
                LOGGER.trace(IRPSection.INITIALIZATION_PARAMETER, "add ConsumerAgentGroupAttribute '{}' ('{}') to group '{}'", cagAttr.getName(), inCagAttr.getName(), jCag.getName());
                jCag.addGroupAttribute(cagAttr);
            }
        }

        return jCag;
    }

    public JadexConsumerAgentGroup update(IRPactInputParser parser, JadexConsumerAgentGroup restored) throws ParsingException {
        LOGGER.trace(IRPSection.INITIALIZATION_PARAMETER, "update '{}'", restored.getName());
        for(InConsumerAgentGroupAttribute inCagAttr: getAttributes()) {
            if(restored.hasGroupAttribute(inCagAttr.getAttributeName())) {
                LOGGER.trace(IRPSection.INITIALIZATION_PARAMETER, "cag '{}' already has '{}' -> skip", restored.getName(), inCagAttr.getAttributeName());
            } else {
                if(inCagAttr.requiresSetup()) {
                    inCagAttr.setup(parser, restored);
                } else {
                    ConsumerAgentGroupAttribute cagAttr = parser.parseEntityTo(inCagAttr);
                    LOGGER.trace(IRPSection.INITIALIZATION_PARAMETER, "add ConsumerAgentGroupAttribute '{}' ('{}') to group '{}'", cagAttr.getName(), inCagAttr.getName(), restored.getName());
                    restored.addGroupAttribute(cagAttr);
                }
            }
        }
        return restored;
    }
}
