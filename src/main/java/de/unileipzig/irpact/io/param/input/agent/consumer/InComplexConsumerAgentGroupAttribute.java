package de.unileipzig.irpact.io.param.input.agent.consumer;

import de.unileipzig.irpact.commons.distribution.UnivariateDoubleDistribution;
import de.unileipzig.irpact.commons.exception.ParsingException;
import de.unileipzig.irpact.core.agent.consumer.BasicConsumerAgentGroupAttribute;
import de.unileipzig.irpact.core.agent.consumer.ConsumerAgentGroup;
import de.unileipzig.irpact.core.log.IRPLogging;
import de.unileipzig.irpact.core.log.IRPSection;
import de.unileipzig.irpact.io.param.input.InAttributeName;
import de.unileipzig.irpact.io.param.input.InUtil;
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
public class InComplexConsumerAgentGroupAttribute implements I_InConsumerAgentGroupAttribute {

    //damit ich bei copy&paste nie mehr vergesse die Klasse anzupassen :)
    private static final MethodHandles.Lookup L = MethodHandles.lookup();
    public static Class<?> thisClass() {
        return L.lookupClass();
    }

    public static void initRes(TreeAnnotationResource res) {
    }
    public static void applyRes(TreeAnnotationResource res) {
    }

    private static final IRPLogger LOGGER = IRPLogging.getLogger(InComplexConsumerAgentGroupAttribute.class);

    public String _name;

    @FieldDefinition
    public InConsumerAgentGroup[] cag;

    @FieldDefinition
    public InAttributeName[] attrName;

    @FieldDefinition
    public InUnivariateDoubleDistribution[] dist;

    public InComplexConsumerAgentGroupAttribute() {
    }

    public InComplexConsumerAgentGroupAttribute(
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
        return InUtil.getInstance(attrName, "AttributeName");
    }

    @Override
    public String getAttributeName() throws ParsingException {
        return getAttributeNameInstance().getName();
    }

    public void setConsumerAgentGroup(InConsumerAgentGroup cag) {
        this.cag = new InConsumerAgentGroup[]{cag};
    }

    public InConsumerAgentGroup getConsumerAgentGroup() throws ParsingException {
        return InUtil.getInstance(cag, "ConsumerAgentGroup");
    }

    @Override
    public String getConsumerAgentGroupName() throws ParsingException {
        return getConsumerAgentGroup().getName();
    }

    public void setDistribution(InUnivariateDoubleDistribution dist) {
        this.dist = new InUnivariateDoubleDistribution[]{dist};
    }

    public InUnivariateDoubleDistribution getDistribution() throws ParsingException {
        return InUtil.getInstance(dist, "UnivariateDoubleDistribution");
    }

    @Override
    public BasicConsumerAgentGroupAttribute parse(InputParser parser) throws ParsingException {
        InConsumerAgentGroup inCag = getConsumerAgentGroup();
        ConsumerAgentGroup cag = parser.parseEntityTo(inCag);

        BasicConsumerAgentGroupAttribute cagAttr = new BasicConsumerAgentGroupAttribute();
        cagAttr.setName(getAttributeName());

        if(cag.hasGroupAttribute(cagAttr)) {
            throw new ParsingException("ConsumerAgentGroupAttribute '" + cagAttr.getName() + "' already exists in '" + cag.getName() + "'");
        }

        UnivariateDoubleDistribution dist = parser.parseEntityTo(getDistribution());
        cagAttr.setDistribution(dist);

        cag.addGroupAttribute(cagAttr);
        LOGGER.debug(IRPSection.INITIALIZATION_PARAMETER, "added ConsumerAgentGroupAttribute '{}' ('{}') to group '{}'", cagAttr.getName(), getName(), cag.getName());

        return cagAttr;
    }
}
