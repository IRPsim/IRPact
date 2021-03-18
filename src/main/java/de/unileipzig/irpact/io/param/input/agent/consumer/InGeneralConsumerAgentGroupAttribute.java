package de.unileipzig.irpact.io.param.input.agent.consumer;

import de.unileipzig.irpact.commons.distribution.UnivariateDoubleDistribution;
import de.unileipzig.irpact.commons.exception.ParsingException;
import de.unileipzig.irpact.core.agent.consumer.BasicConsumerAgentGroupAttribute;
import de.unileipzig.irpact.core.log.IRPLogging;
import de.unileipzig.irpact.core.log.IRPSection;
import de.unileipzig.irpact.io.param.input.InAttributeName;
import de.unileipzig.irpact.io.param.ParamUtil;
import de.unileipzig.irpact.io.param.input.InputParser;
import de.unileipzig.irpact.io.param.input.distribution.InUnivariateDoubleDistribution;
import de.unileipzig.irpact.util.AddToRoot;
import de.unileipzig.irptools.defstructure.annotation.Definition;
import de.unileipzig.irptools.defstructure.annotation.FieldDefinition;
import de.unileipzig.irptools.util.TreeAnnotationResource;
import de.unileipzig.irptools.util.log.IRPLogger;

import java.lang.invoke.MethodHandles;

/**
 * @author Daniel Abitz
 */
@AddToRoot
@Definition
public class InGeneralConsumerAgentGroupAttribute implements InConsumerAgentGroupAttribute {

    //damit ich bei copy&paste nie mehr vergesse die Klasse anzupassen :)
    private static final MethodHandles.Lookup L = MethodHandles.lookup();
    public static Class<?> thisClass() {
        return L.lookupClass();
    }

    public static void initRes(TreeAnnotationResource res) {
    }
    public static void applyRes(TreeAnnotationResource res) {
        res.putPath(
                thisClass(),
                res.getCachedElement("Agenten"),
                res.getCachedElement("Konsumer"),
                res.getCachedElement("Attribute")
        );

        res.putPath(
                thisClass(), "cagAttrName",
                res.getCachedElement("Agenten"),
                res.getCachedElement("Konsumer"),
                res.getCachedElement("Attribute"),
                res.getCachedElement("Attribute-Name-Mapping")
        );

        res.putPath(
                thisClass(), "cagAttrDistribution",
                res.getCachedElement("Agenten"),
                res.getCachedElement("Konsumer"),
                res.getCachedElement("Attribute"),
                res.getCachedElement("Attribute-Verteilung-Mapping")
        );

        res.newEntryBuilder()
                .setGamsIdentifier("Name des KG-Attributes")
                .setGamsDescription("Attributname")
                .store(thisClass(), "cagAttrName");

        res.newEntryBuilder()
                .setGamsIdentifier("Verteilungsfunktion des KG-Attributes")
                .setGamsDescription("genutzte Funktion")
                .store(thisClass(), "cagAttrDistribution");
    }

    private static final IRPLogger LOGGER = IRPLogging.getLogger(InGeneralConsumerAgentGroupAttribute.class);

    public String _name;

    @FieldDefinition
    public InConsumerAgentGroup[] cag;

    @FieldDefinition
    public InAttributeName[] attrName;

    @FieldDefinition
    public InUnivariateDoubleDistribution[] dist;

    public InGeneralConsumerAgentGroupAttribute() {
    }

    public InGeneralConsumerAgentGroupAttribute(
            String name,
            InConsumerAgentGroup cag,
            InAttributeName attributeName,
            InUnivariateDoubleDistribution distribution) {
        this._name = name;
        setConsumerAgentGroup(cag);
        setAttributeNameInstance(attributeName);
        setDistribution(distribution);
    }

    @Override
    public String getName() {
        return _name;
    }

    public void setAttributeNameInstance(InAttributeName attrName) {
        this.attrName = new InAttributeName[]{attrName};
    }

    public InAttributeName getAttributeNameInstance() throws ParsingException {
        return ParamUtil.getInstance(attrName, "AttributeName");
    }

    @Override
    public String getAttributeName() throws ParsingException {
        return getAttributeNameInstance().getName();
    }

    public void setConsumerAgentGroup(InConsumerAgentGroup cag) {
        this.cag = new InConsumerAgentGroup[]{cag};
    }

    @Override
    public InConsumerAgentGroup getConsumerAgentGroup(InputParser parser) throws ParsingException {
        return getConsumerAgentGroup();
    }

    public InConsumerAgentGroup getConsumerAgentGroup() throws ParsingException {
        return ParamUtil.getInstance(cag, "ConsumerAgentGroup");
    }

    @Override
    public String getConsumerAgentGroupName() throws ParsingException {
        return getConsumerAgentGroup().getName();
    }

    public void setDistribution(InUnivariateDoubleDistribution dist) {
        this.dist = new InUnivariateDoubleDistribution[]{dist};
    }

    @Override
    public InUnivariateDoubleDistribution getDistribution() throws ParsingException {
        return ParamUtil.getInstance(dist, "UnivariateDoubleDistribution");
    }

    @Override
    public BasicConsumerAgentGroupAttribute parse(InputParser parser) throws ParsingException {
        BasicConsumerAgentGroupAttribute cagAttr = new BasicConsumerAgentGroupAttribute();
        cagAttr.setName(getAttributeName());

        UnivariateDoubleDistribution dist = parser.parseEntityTo(getDistribution());
        cagAttr.setDistribution(dist);

        LOGGER.debug(IRPSection.INITIALIZATION_PARAMETER, "created ConsumerAgentGroupAttribute '{}' ('{}')", cagAttr.getName(), getName());
        return cagAttr;
    }
}
