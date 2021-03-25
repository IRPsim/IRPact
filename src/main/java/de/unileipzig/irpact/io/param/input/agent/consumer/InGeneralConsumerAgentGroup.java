package de.unileipzig.irpact.io.param.input.agent.consumer;

import de.unileipzig.irpact.commons.exception.ParsingException;
import de.unileipzig.irpact.core.agent.AgentManager;
import de.unileipzig.irpact.core.agent.consumer.ConsumerAgentGroupAttribute;
import de.unileipzig.irpact.core.log.IRPLogging;
import de.unileipzig.irpact.core.log.IRPSection;
import de.unileipzig.irpact.core.product.ProductFindingScheme;
import de.unileipzig.irpact.core.product.interest.ProductInterestSupplyScheme;
import de.unileipzig.irpact.io.param.ParamUtil;
import de.unileipzig.irpact.io.param.input.InputParser;
import de.unileipzig.irpact.io.param.input.interest.InProductInterestSupplyScheme;
import de.unileipzig.irpact.io.param.input.product.InProductFindingScheme;
import de.unileipzig.irpact.io.param.input.spatial.dist.InSpatialDistribution;
import de.unileipzig.irpact.jadex.agents.consumer.JadexConsumerAgentGroup;
import de.unileipzig.irptools.defstructure.annotation.Definition;
import de.unileipzig.irptools.defstructure.annotation.FieldDefinition;
import de.unileipzig.irptools.util.TreeAnnotationResource;
import de.unileipzig.irptools.util.TreeResourceApplier;
import de.unileipzig.irptools.util.log.IRPLogger;

import java.lang.invoke.MethodHandles;
import java.util.Collection;

/**
 * @author Daniel Abitz
 */
@Definition
public class InGeneralConsumerAgentGroup implements InConsumerAgentGroup {

    private static final MethodHandles.Lookup L = MethodHandles.lookup();
    public static Class<?> thisClass() {
        return L.lookupClass();
    }

    public static void initRes(TreeAnnotationResource res) {
        TreeResourceApplier.callAllSubInitResSilently(thisClass(), res);
    }
    public static void applyRes(TreeAnnotationResource res) {
        res.putPath(
                thisClass(),
                res.getCachedElement("Agenten"),
                res.getCachedElement("Konsumer"),
                res.getCachedElement("Gruppen")
        );
        TreeResourceApplier.callAllSubApplyResSilently(thisClass(), res);
    }

    private static final IRPLogger LOGGER = IRPLogging.getLogger(thisClass());

    public String _name;

    public static void initRes0(TreeAnnotationResource res) {
        res.putPath(
                thisClass(), "cagAttributes",
                res.getCachedElement("Agenten"),
                res.getCachedElement("Konsumer"),
                res.getCachedElement("Gruppen"),
                res.getCachedElement("Gruppe-Attribut-Mapping")
        );
    }
    public static void applyRes0(TreeAnnotationResource res) {
        res.newEntryBuilder()
                .setGamsIdentifier("Attribute der KG")
                .setGamsDescription("Attribute")
                .store(thisClass(), "cagAttributes");
    }
    @FieldDefinition
    public InConsumerAgentGroupAttribute[] cagAttributes;

    public static void initRes1(TreeAnnotationResource res) {
        res.putPath(
                thisClass(), "cagInterest",
                res.getCachedElement("Agenten"),
                res.getCachedElement("Konsumer"),
                res.getCachedElement("Gruppen"),
                res.getCachedElement("Gruppe-Awareness-Mapping")
        );
    }
    public static void applyRes1(TreeAnnotationResource res) {
        res.newEntryBuilder()
                .setGamsIdentifier("Interest der KG")
                .setGamsDescription("genutzter Interest")
                .store(thisClass(), "cagInterest");
    }
    @FieldDefinition
    public InProductInterestSupplyScheme[] cagInterest;

    public static void initRes2(TreeAnnotationResource res) {
        res.putPath(
                thisClass(), "productFindingSchemes",
                res.getCachedElement("Agenten"),
                res.getCachedElement("Konsumer"),
                res.getCachedElement("Gruppen"),
                res.getCachedElement("Gruppe-ProductFinding-Mapping")
        );
    }
    public static void applyRes2(TreeAnnotationResource res) {
        res.newEntryBuilder()
                .setGamsIdentifier("Schema für die Produktfindung")
                .setGamsDescription("Legt das Schema für das finden von passenden Produkten fest")
                .store(thisClass(), "productFindingSchemes");
    }
    @FieldDefinition
    public InProductFindingScheme[] productFindingSchemes;

    public static void initRes3(TreeAnnotationResource res) {
        res.putPath(
                thisClass(), "spatialDistribution",
                res.getCachedElement("Agenten"),
                res.getCachedElement("Konsumer"),
                res.getCachedElement("Gruppen"),
                res.getCachedElement("Gruppe-Spatial-Mapping")
        );
    }
    public static void applyRes3(TreeAnnotationResource res) {
        res.newEntryBuilder()
                .setGamsIdentifier("Räumliche Verteilungsfunktion")
                .setGamsDescription("Legt die Verteilungsfunktion für diese Gruppe fest")
                .store(thisClass(), "spatialDistribution");
    }
    @FieldDefinition
    public InSpatialDistribution[] spatialDistribution;

    public static void initRes4(TreeAnnotationResource res) {
    }
    public static void applyRes4(TreeAnnotationResource res) {
        res.newEntryBuilder()
                .setGamsIdentifier("[ungenutzt] informationAuthority")
                .setGamsDescription("informationAuthority")
                .store(thisClass(), "informationAuthority");
    }
    @FieldDefinition
    public double informationAuthority;

    public InGeneralConsumerAgentGroup() {
    }

    @Override
    public String getName() {
        return _name;
    }

    public void setName(String _name) {
        this._name = _name;
    }

    public double getInformationAuthority() {
        return informationAuthority;
    }

    public void setInformationAuthority(double informationAuthority) {
        this.informationAuthority = informationAuthority;
    }

    public InConsumerAgentGroupAttribute[] getAttributes() {
        return cagAttributes;
    }

    public void setAttributes(Collection<? extends InConsumerAgentGroupAttribute> attrs) {
        this.cagAttributes = attrs.toArray(new InConsumerAgentGroupAttribute[0]);
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

    public InSpatialDistribution getSpatialDistribution() throws ParsingException {
        return ParamUtil.getInstance(spatialDistribution, "spatialDistribution");
    }

    public void setSpatialDistribution(InSpatialDistribution dist) {
        this.spatialDistribution = new InSpatialDistribution[]{dist};
    }

    @Override
    public JadexConsumerAgentGroup parse(InputParser parser) throws ParsingException {
        AgentManager agentManager = parser.getEnvironment().getAgents();

        JadexConsumerAgentGroup jCag = new JadexConsumerAgentGroup();
        jCag.setEnvironment(parser.getEnvironment());
        jCag.setName(getName());
        jCag.setInformationAuthority(getInformationAuthority());

        if(agentManager.hasConsumerAgentGroup(getName())) {
            throw new ParsingException("ConsumerAgentGroup '" + getName() + "' already exists");
        }

        ProductInterestSupplyScheme awarenessSupplyScheme = parser.parseEntityTo(getInterest());
        jCag.setInterestSupplyScheme(awarenessSupplyScheme);

        ProductFindingScheme productFindingScheme = parser.parseEntityTo(getProductFindingScheme());
        jCag.setProductFindingScheme(productFindingScheme);

        getSpatialDistribution().setup(parser, jCag);

        for(InConsumerAgentGroupAttribute inCagAttr: getAttributes()) {
            ConsumerAgentGroupAttribute cagAttr = parser.parseEntityTo(inCagAttr);
            if(jCag.hasGroupAttribute(cagAttr.getName())) {
                throw new ParsingException("ConsumerAgentGroupAttribute '" + cagAttr.getName() + "' already exists in " + jCag.getName());
            }
            LOGGER.debug(IRPSection.INITIALIZATION_PARAMETER, "add ConsumerAgentGroupAttribute '{}' ('{}') to group '{}'", cagAttr.getName(), inCagAttr.getName(), jCag.getName());
            jCag.addGroupAttribute(cagAttr);
        }

        return jCag;
    }
}
