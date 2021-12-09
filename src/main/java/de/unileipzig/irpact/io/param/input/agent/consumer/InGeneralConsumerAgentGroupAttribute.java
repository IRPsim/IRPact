package de.unileipzig.irpact.io.param.input.agent.consumer;

import de.unileipzig.irpact.commons.distribution.UnivariateDoubleDistribution;
import de.unileipzig.irpact.commons.exception.ParsingException;
import de.unileipzig.irpact.core.agent.consumer.attribute.BasicConsumerAgentDoubleGroupAttribute;
import de.unileipzig.irpact.core.logging.IRPLogging;
import de.unileipzig.irpact.core.logging.IRPSection;
import de.unileipzig.irpact.core.start.IRPactInputParser;
import de.unileipzig.irpact.io.param.LocalizedUiResource;
import de.unileipzig.irpact.io.param.input.names.InAttributeName;
import de.unileipzig.irpact.io.param.ParamUtil;
import de.unileipzig.irpact.io.param.input.distribution.InUnivariateDoubleDistribution;
import de.unileipzig.irptools.defstructure.annotation.Definition;
import de.unileipzig.irptools.defstructure.annotation.DefinitionName;
import de.unileipzig.irptools.defstructure.annotation.FieldDefinition;
import de.unileipzig.irptools.util.CopyCache;
import de.unileipzig.irptools.util.TreeAnnotationResource;
import de.unileipzig.irptools.util.log.IRPLogger;

import java.lang.invoke.MethodHandles;

import static de.unileipzig.irpact.io.param.input.TreeViewStructureEnum.AGENTS_CONSUMER_ATTR_GENERALGRP;

/**
 * @author Daniel Abitz
 */
@Definition
@LocalizedUiResource.PutClassPath(AGENTS_CONSUMER_ATTR_GENERALGRP)
public class InGeneralConsumerAgentGroupAttribute implements InDependentConsumerAgentGroupAttribute {

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

    private static final IRPLogger LOGGER = IRPLogging.getLogger(InGeneralConsumerAgentGroupAttribute.class);

    @DefinitionName
    public String name;

    @FieldDefinition
    @LocalizedUiResource.AddEntry
    public InAttributeName[] attrName;

    @FieldDefinition
    @LocalizedUiResource.AddEntry
    public InUnivariateDoubleDistribution[] dist;

    public InGeneralConsumerAgentGroupAttribute() {
    }

    public InGeneralConsumerAgentGroupAttribute(
            String name,
            InAttributeName attributeName,
            InUnivariateDoubleDistribution distribution) {
        this.name = name;
        setAttributeNameInstance(attributeName);
        setDistribution(distribution);
    }

    @Override
    public InGeneralConsumerAgentGroupAttribute copy(CopyCache cache) {
        return cache.copyIfAbsent(this, this::newCopy);
    }

    public InGeneralConsumerAgentGroupAttribute newCopy(CopyCache cache) {
        InGeneralConsumerAgentGroupAttribute copy = new InGeneralConsumerAgentGroupAttribute();
        copy.name = name;
        copy.attrName = cache.copyArray(attrName);
        copy.dist = cache.copyArray(dist);
        return copy;
    }

    @Override
    public String getName() {
        return name;
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
    public BasicConsumerAgentDoubleGroupAttribute parse(IRPactInputParser parser) throws ParsingException {
        BasicConsumerAgentDoubleGroupAttribute cagAttr = new BasicConsumerAgentDoubleGroupAttribute();
        cagAttr.setName(getAttributeName());
        cagAttr.setArtificial(false);

        UnivariateDoubleDistribution dist = parser.parseEntityTo(getDistribution());
        cagAttr.setDistribution(dist);

        LOGGER.trace(IRPSection.INITIALIZATION_PARAMETER, "created ConsumerAgentGroupAttribute '{}' ('{}')", cagAttr.getName(), getName());
        return cagAttr;
    }
}
