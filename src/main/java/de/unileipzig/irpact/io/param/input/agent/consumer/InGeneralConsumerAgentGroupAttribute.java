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
import de.unileipzig.irptools.defstructure.annotation.Definition;
import de.unileipzig.irptools.defstructure.annotation.FieldDefinition;
import de.unileipzig.irptools.util.TreeAnnotationResource;
import de.unileipzig.irptools.util.log.IRPLogger;

import java.lang.invoke.MethodHandles;

import static de.unileipzig.irpact.io.param.IOConstants.*;
import static de.unileipzig.irpact.io.param.ParamUtil.addEntry;
import static de.unileipzig.irpact.io.param.ParamUtil.putClassPath;

/**
 * @author Daniel Abitz
 */
@Definition
public class InGeneralConsumerAgentGroupAttribute implements InDependentConsumerAgentGroupAttribute {

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
        putClassPath(res, thisClass(), AGENTS, CONSUMER, CONSUMER_ATTR, thisName());
        addEntry(res, thisClass(), "attrName");
        addEntry(res, thisClass(), "dist");
    }

    private static final IRPLogger LOGGER = IRPLogging.getLogger(InGeneralConsumerAgentGroupAttribute.class);

    public String _name;

    @FieldDefinition
    public InAttributeName[] attrName;

    @FieldDefinition
    public InUnivariateDoubleDistribution[] dist;

    public InGeneralConsumerAgentGroupAttribute() {
    }

    public InGeneralConsumerAgentGroupAttribute(
            String name,
            InAttributeName attributeName,
            InUnivariateDoubleDistribution distribution) {
        this._name = name;
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

    public void setDistribution(InUnivariateDoubleDistribution dist) {
        this.dist = new InUnivariateDoubleDistribution[]{dist};
    }

    public InUnivariateDoubleDistribution getDistribution() throws ParsingException {
        return ParamUtil.getInstance(dist, "UnivariateDoubleDistribution");
    }

    @Override
    public BasicConsumerAgentGroupAttribute parse(InputParser parser) throws ParsingException {
        BasicConsumerAgentGroupAttribute cagAttr = new BasicConsumerAgentGroupAttribute();
        cagAttr.setName(getAttributeName());

        UnivariateDoubleDistribution dist = parser.parseEntityTo(getDistribution());
        cagAttr.setUnivariateDoubleDistributionValue(dist);

        LOGGER.trace(IRPSection.INITIALIZATION_PARAMETER, "created ConsumerAgentGroupAttribute '{}' ('{}')", cagAttr.getName(), getName());
        return cagAttr;
    }
}
