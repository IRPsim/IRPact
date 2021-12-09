package de.unileipzig.irpact.io.param.input.agent.consumer;

import de.unileipzig.irpact.commons.exception.ParsingException;
import de.unileipzig.irpact.commons.util.ExceptionUtil;
import de.unileipzig.irpact.core.agent.consumer.attribute.BasicConsumerAgentAnnualGroupAttribute;
import de.unileipzig.irpact.core.agent.consumer.attribute.BasicConsumerAgentDoubleGroupAttribute;
import de.unileipzig.irpact.core.logging.IRPLogging;
import de.unileipzig.irpact.io.param.LocalizedUiResource;
import de.unileipzig.irpact.io.param.ParamUtil;
import de.unileipzig.irpact.core.start.IRPactInputParser;
import de.unileipzig.irpact.io.param.input.names.InAttributeName;
import de.unileipzig.irpact.io.param.input.distribution.InUnivariateDoubleDistribution;
import de.unileipzig.irpact.jadex.agents.consumer.JadexConsumerAgentGroup;
import de.unileipzig.irptools.Constants;
import de.unileipzig.irptools.defstructure.annotation.*;
import de.unileipzig.irptools.util.CopyCache;
import de.unileipzig.irptools.util.TreeAnnotationResource;
import de.unileipzig.irptools.util.log.IRPLogger;

import java.lang.invoke.MethodHandles;

import static de.unileipzig.irpact.io.param.input.TreeViewStructureEnum.AGENTS_CONSUMER_ATTR_GENERALANNUAL;

/**
 * @author Daniel Abitz
 */
@Definition
@LocalizedUiResource.PutClassPath(AGENTS_CONSUMER_ATTR_GENERALANNUAL)
public class InGeneralConsumerAgentAnnualGroupAttribute implements InDependentConsumerAgentGroupAttribute {

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

    private static final IRPLogger LOGGER = IRPLogging.getLogger(thisClass());

    @DefinitionName
    public String name;

    @FieldDefinition
    @LocalizedUiResource.AddEntry
    public InAttributeName[] attributeName;

    @FieldDefinition(
            edn = @EdnParameter(
                    delta = Constants.TRUE1
            )
    )
    @LocalizedUiResource.AddEntry
    public InUnivariateDoubleDistribution[] distribution;

    private int customYear;
    private boolean hasCustomYear = false;

    public InGeneralConsumerAgentAnnualGroupAttribute() {
    }

    public InGeneralConsumerAgentAnnualGroupAttribute(
            String name,
            InAttributeName attributeName,
            InUnivariateDoubleDistribution distribution) {
        setName(name);
        setAttributeNameInstance(attributeName);
        setDistribution(distribution);
    }

    public InGeneralConsumerAgentAnnualGroupAttribute(
            String name,
            InAttributeName attributeName,
            InUnivariateDoubleDistribution distribution,
            int customYear) {
        this(name, attributeName, distribution);
        setCustomYear(customYear);
    }

    @Override
    public InGeneralConsumerAgentAnnualGroupAttribute copy(CopyCache cache) {
        return cache.copyIfAbsent(this, this::newCopy);
    }

    public InGeneralConsumerAgentAnnualGroupAttribute newCopy(CopyCache cache) {
        InGeneralConsumerAgentAnnualGroupAttribute copy = new InGeneralConsumerAgentAnnualGroupAttribute();
        copy.name = name;
        copy.attributeName = cache.copyArray(attributeName);
        copy.distribution = cache.copyArray(distribution);
        copy.customYear = customYear;
        copy.hasCustomYear = hasCustomYear;
        return copy;
    }

    @Override
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAttributeNameInstance(InAttributeName attrName) {
        this.attributeName = new InAttributeName[]{attrName};
    }

    public InAttributeName getAttributeNameInstance() throws ParsingException {
        return ParamUtil.getInstance(attributeName, "AttributeName");
    }

    @Override
    public String getAttributeName() throws ParsingException {
        return getAttributeNameInstance().getName();
    }

    public int getCustomYear() {
        return customYear;
    }

    public void setCustomYear(int customYear) {
        this.customYear = customYear;
        hasCustomYear = true;
    }

    public void deleteCustomYear() {
        customYear = 0;
        hasCustomYear = false;
    }

    public boolean hasCustomYear() {
        return hasCustomYear;
    }

    public InUnivariateDoubleDistribution getDistribution() throws ParsingException {
        return ParamUtil.getInstance(distribution, "distribution");
    }

    public void setDistribution(InUnivariateDoubleDistribution distribution) {
        this.distribution = new InUnivariateDoubleDistribution[]{distribution};
    }

    @Override
    public boolean requiresSetup() {
        return true;
    }

    @Override
    public void setup(IRPactInputParser parser, Object input) throws ParsingException {
        JadexConsumerAgentGroup jcag = (JadexConsumerAgentGroup) input;

        String attributeName = getAttributeName();
        if(!jcag.hasGroupAttribute(attributeName)) {
            LOGGER.trace("add annual attribute '{}' to cag '{}'", attributeName, jcag.getName());
            BasicConsumerAgentAnnualGroupAttribute aGrpAttr = new BasicConsumerAgentAnnualGroupAttribute();
            aGrpAttr.setName(attributeName);
            aGrpAttr.setArtificial(false);
            jcag.addGroupAttribute(aGrpAttr);
        }

        int year = hasCustomYear()
                ? getCustomYear()
                : parser.getSimulationYear();

        BasicConsumerAgentAnnualGroupAttribute aGrpAttr = (BasicConsumerAgentAnnualGroupAttribute) jcag.getGroupAttribute(attributeName);
        if(aGrpAttr.hasYear(year)) {
            throw ExceptionUtil.create(ParsingException::new, "cag '{}' attribute '{}' already has year '{}'", jcag.getName(), aGrpAttr.getName(), year);
        }

        BasicConsumerAgentDoubleGroupAttribute yearAttr = new BasicConsumerAgentDoubleGroupAttribute();
        yearAttr.setName(getName() + "_" + year);
        yearAttr.setArtificial(true);
        yearAttr.setDistribution(parser.parseEntityTo(getDistribution()));

        LOGGER.trace("add '{}' to annual attribute '{}' in cag '{}' for year '{}'", yearAttr.getName(), aGrpAttr.getName(), jcag.getName(), year);
        aGrpAttr.put(parser.getSimulationYear(), yearAttr);
    }
}
