package de.unileipzig.irpact.io.param.input.agent.consumer;

import de.unileipzig.irpact.commons.distribution.UnivariateDoubleDistribution;
import de.unileipzig.irpact.commons.exception.ParsingException;
import de.unileipzig.irpact.core.agent.consumer.ConsumerAgentGroup;
import de.unileipzig.irpact.core.agent.consumer.attribute.BasicConsumerAgentDoubleGroupAttribute;
import de.unileipzig.irpact.core.logging.IRPLogging;
import de.unileipzig.irpact.core.logging.IRPSection;
import de.unileipzig.irpact.core.start.IRPactInputParser;
import de.unileipzig.irpact.io.param.LocalizedUiResource;
import de.unileipzig.irpact.io.param.input.names.InAttributeName;
import de.unileipzig.irpact.io.param.ParamUtil;
import de.unileipzig.irpact.io.param.input.InRoot;
import de.unileipzig.irpact.core.start.InputParser;
import de.unileipzig.irpact.io.param.input.distribution.InUnivariateDoubleDistribution;
import de.unileipzig.irptools.defstructure.annotation.Definition;
import de.unileipzig.irptools.defstructure.annotation.DefinitionName;
import de.unileipzig.irptools.defstructure.annotation.FieldDefinition;
import de.unileipzig.irptools.util.CopyCache;
import de.unileipzig.irptools.util.TreeAnnotationResource;
import de.unileipzig.irptools.util.log.IRPLogger;

import java.lang.invoke.MethodHandles;

import static de.unileipzig.irpact.io.param.input.TreeViewStructureEnum.AGENTS_CONSUMER_ATTR_SPLITGRP;

/**
 * @author Daniel Abitz
 */
@Definition
@LocalizedUiResource.PutClassPath(AGENTS_CONSUMER_ATTR_SPLITGRP)
public class InNameSplitConsumerAgentGroupAttribute implements InIndependentConsumerAgentGroupAttribute {

    private static final MethodHandles.Lookup L = MethodHandles.lookup();
    public static Class<?> thisClass() {
        return L.lookupClass();
    }
    public static String thisName() {
        return thisClass().getSimpleName();
    }

    @TreeAnnotationResource.Init
    public static void initRes(LocalizedUiResource res) {
    }
    @TreeAnnotationResource.Apply
    public static void applyRes(LocalizedUiResource res) {
    }

    private static final IRPLogger LOGGER = IRPLogging.getLogger(InNameSplitConsumerAgentGroupAttribute.class);

    @DefinitionName
    public String name;

    @FieldDefinition
    @LocalizedUiResource.AddEntry
    public InUnivariateDoubleDistribution[] dist;

    public InNameSplitConsumerAgentGroupAttribute() {
    }

    public InNameSplitConsumerAgentGroupAttribute(
            InConsumerAgentGroup cag,
            InAttributeName attributeName,
            InUnivariateDoubleDistribution distribution) {
        this.name = ParamUtil.concName(cag, attributeName);
        setDistribution(distribution);
    }

    @Override
    public InNameSplitConsumerAgentGroupAttribute copy(CopyCache cache) {
        return cache.copyIfAbsent(this, this::newCopy);
    }

    public InNameSplitConsumerAgentGroupAttribute newCopy(CopyCache cache) {
        InNameSplitConsumerAgentGroupAttribute copy = new InNameSplitConsumerAgentGroupAttribute();
        copy.name = name;
        copy.dist = cache.copyArray(dist);
        return copy;
    }

    @Override
    public String getName() {
        return name;
    }

    public void setName(String fullName) {
        this.name = fullName;
    }

    public void setName(String cagName, String attrName) {
        this.name = ParamUtil.concName(cagName, attrName);
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
