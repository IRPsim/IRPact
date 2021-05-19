package de.unileipzig.irpact.io.param.input.agent.consumer;

import de.unileipzig.irpact.commons.distribution.UnivariateDoubleDistribution;
import de.unileipzig.irpact.commons.exception.ParsingException;
import de.unileipzig.irpact.core.agent.consumer.ConsumerAgentGroup;
import de.unileipzig.irpact.core.agent.consumer.attribute.BasicConsumerAgentDoubleGroupAttribute;
import de.unileipzig.irpact.core.log.IRPLogging;
import de.unileipzig.irpact.core.log.IRPSection;
import de.unileipzig.irpact.io.param.input.IRPactInputParser;
import de.unileipzig.irpact.io.param.input.names.InAttributeName;
import de.unileipzig.irpact.io.param.ParamUtil;
import de.unileipzig.irpact.io.param.input.InRoot;
import de.unileipzig.irpact.io.param.input.InputParser;
import de.unileipzig.irpact.io.param.input.distribution.InUnivariateDoubleDistribution;
import de.unileipzig.irptools.defstructure.annotation.Definition;
import de.unileipzig.irptools.defstructure.annotation.FieldDefinition;
import de.unileipzig.irptools.util.CopyCache;
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
public class InNameSplitConsumerAgentGroupAttribute implements InIndependentConsumerAgentGroupAttribute {

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
        addEntry(res, thisClass(), "dist");
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
        this._name = ParamUtil.concName(cag, attributeName);
        setDistribution(distribution);
    }

    @Override
    public InNameSplitConsumerAgentGroupAttribute copy(CopyCache cache) {
        return cache.copyIfAbsent(this, this::newCopy);
    }

    public InNameSplitConsumerAgentGroupAttribute newCopy(CopyCache cache) {
        InNameSplitConsumerAgentGroupAttribute copy = new InNameSplitConsumerAgentGroupAttribute();
        copy._name = _name;
        copy.dist = cache.copyArray(dist);
        return copy;
    }

    @Override
    public String getName() {
        return _name;
    }

    public void setName(String fullName) {
        this._name = fullName;
    }

    public void setName(String cagName, String attrName) {
        this._name = ParamUtil.concName(cagName, attrName);
    }

    @Override
    public InConsumerAgentGroup getConsumerAgentGroup(InputParser p) throws ParsingException {
        IRPactInputParser parser = (IRPactInputParser) p;
        String name = getConsumerAgentGroupName();
        InRoot root = parser.getRoot();
        return root.findConsumerAgentGroup(name);
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

    @Override
    public InUnivariateDoubleDistribution getDistribution() throws ParsingException {
        return ParamUtil.getInstance(dist, "UnivariateDoubleDistribution");
    }

    @Override
    public void setup(IRPactInputParser parser, Object input) throws ParsingException {
        final ConsumerAgentGroup cag;
        if(input instanceof ConsumerAgentGroup) {
            cag = (ConsumerAgentGroup) input;
        } else {
            InConsumerAgentGroup inCag = getConsumerAgentGroup(parser);
            cag = parser.parseEntityTo(inCag);
        }

        if(parser.isRestored() && cag.hasGroupAttribute(getAttributeName())) {
            LOGGER.trace(IRPSection.INITIALIZATION_PARAMETER, "cag '{}' already has '{}' -> skip", cag.getName(), getAttributeName());
            return;
        }

        BasicConsumerAgentDoubleGroupAttribute cagAttr = new BasicConsumerAgentDoubleGroupAttribute();
        cagAttr.setName(getAttributeName());
        cagAttr.setArtificial(false);

        if(cag.hasGroupAttribute(cagAttr)) {
            throw new ParsingException("ConsumerAgentGroupAttribute '" + cagAttr.getName() + "' already exists in '" + cag.getName() + "'");
        }

        UnivariateDoubleDistribution dist = parser.parseEntityTo(getDistribution());
        cagAttr.setDistribution(dist);

        cag.addGroupAttribute(cagAttr);
        LOGGER.trace(IRPSection.INITIALIZATION_PARAMETER, "added ConsumerAgentGroupAttribute '{}' ('{}') to group '{}'", cagAttr.getName(), getName(), cag.getName());
    }
}
