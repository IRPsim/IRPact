package de.unileipzig.irpact.io.param.input.agent.consumer;

import de.unileipzig.irpact.commons.distribution.UnivariateDoubleDistribution;
import de.unileipzig.irpact.commons.exception.ParsingException;
import de.unileipzig.irpact.core.agent.consumer.BasicConsumerAgentGroupAttribute;
import de.unileipzig.irpact.core.agent.consumer.ConsumerAgentGroup;
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
public class InNameSplitConsumerAgentGroupAttribute implements I_InConsumerAgentGroupAttribute {

    //damit ich bei copy&paste nie mehr vergesse die Klasse anzupassen :)
    private static final MethodHandles.Lookup L = MethodHandles.lookup();
    public static Class<?> thisClass() {
        return L.lookupClass();
    }

    public static void initRes(TreeAnnotationResource res) {
    }
    public static void applyRes(TreeAnnotationResource res) {
    }

    private static final IRPLogger LOGGER = IRPLogging.getLogger(InNameSplitConsumerAgentGroupAttribute.class);

    public String _name;

    @FieldDefinition
    public InUnivariateDoubleDistribution[] dist;

    public InNameSplitConsumerAgentGroupAttribute() {
    }

    public InNameSplitConsumerAgentGroupAttribute(
            InConsumerAgentGroup cag,
            InAttributeName attributeName,
            InUnivariateDoubleDistribution distribution) {
        this._name = ParamUtil.conc(cag, attributeName);
        setDistribution(distribution);
    }

    @Override
    public String getName() {
        return _name;
    }

    @Override
    public String getConsumerAgentGroupName() throws ParsingException {
        return ParamUtil.firstPart(getName());
    }

    @Override
    public String getAttributeName() throws ParsingException {
        return ParamUtil.secondPart(getName());
    }

    public void setDistribution(InUnivariateDoubleDistribution dist) {
        this.dist = new InUnivariateDoubleDistribution[]{dist};
    }

    public InUnivariateDoubleDistribution getDistribution() throws ParsingException {
        return ParamUtil.getInstance(dist, "UnivariateDoubleDistribution");
    }

    @Override
    public BasicConsumerAgentGroupAttribute parse(InputParser parser) throws ParsingException {
        InConsumerAgentGroup inCag = parser.getRoot().findConsumerAgentGroup(getConsumerAgentGroupName());
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
