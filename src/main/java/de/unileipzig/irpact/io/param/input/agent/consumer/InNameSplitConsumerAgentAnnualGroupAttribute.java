package de.unileipzig.irpact.io.param.input.agent.consumer;

import de.unileipzig.irpact.commons.exception.ParsingException;
import de.unileipzig.irpact.core.agent.consumer.ConsumerAgentGroup;
import de.unileipzig.irpact.core.agent.consumer.attribute.BasicConsumerAgentAnnualGroupAttribute;
import de.unileipzig.irpact.core.agent.consumer.attribute.BasicConsumerAgentDoubleGroupAttribute;
import de.unileipzig.irpact.core.logging.IRPLogging;
import de.unileipzig.irpact.core.logging.IRPSection;
import de.unileipzig.irpact.io.param.ParamUtil;
import de.unileipzig.irpact.core.start.IRPactInputParser;
import de.unileipzig.irpact.io.param.input.names.InAttributeName;
import de.unileipzig.irpact.io.param.input.InRoot;
import de.unileipzig.irpact.core.start.InputParser;
import de.unileipzig.irpact.io.param.input.distribution.InUnivariateDoubleDistribution;
import de.unileipzig.irptools.defstructure.annotation.FieldDefinition;
import de.unileipzig.irptools.util.CopyCache;
import de.unileipzig.irptools.util.TreeAnnotationResource;
import de.unileipzig.irptools.util.log.IRPLogger;

import java.lang.invoke.MethodHandles;

import static de.unileipzig.irpact.io.param.IOConstants.*;
import static de.unileipzig.irpact.io.param.ParamUtil.*;

/**
 * @author Daniel Abitz
 */
public class InNameSplitConsumerAgentAnnualGroupAttribute implements InIndependentConsumerAgentGroupAttribute {

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
        addEntry(res, thisClass(), "distribution");

        setDelta(res, thisClass(), "distribution");
    }

    private static final IRPLogger LOGGER = IRPLogging.getLogger(thisClass());

    public String _name;

    @FieldDefinition
    public InUnivariateDoubleDistribution[] distribution;

    private int customYear;
    private boolean hasCustomYear = false;

    public InNameSplitConsumerAgentAnnualGroupAttribute() {
    }

    public InNameSplitConsumerAgentAnnualGroupAttribute(
            InConsumerAgentGroup cag,
            InAttributeName attributeName,
            InUnivariateDoubleDistribution distribution) {
        setName(ParamUtil.concName(cag, attributeName));
        setDistribution(distribution);
    }

    public InNameSplitConsumerAgentAnnualGroupAttribute(
            InConsumerAgentGroup cag,
            InAttributeName attributeName,
            InUnivariateDoubleDistribution distribution,
            int customYear) {
        this(cag, attributeName, distribution);
        setCustomYear(customYear);
    }

    @Override
    public InNameSplitConsumerAgentAnnualGroupAttribute copy(CopyCache cache) {
        return cache.copyIfAbsent(this, this::newCopy);
    }

    public InNameSplitConsumerAgentAnnualGroupAttribute newCopy(CopyCache cache) {
        InNameSplitConsumerAgentAnnualGroupAttribute copy = new InNameSplitConsumerAgentAnnualGroupAttribute();
        copy._name = _name;
        copy.distribution = cache.copyArray(distribution);
        copy.customYear = customYear;
        copy.hasCustomYear = hasCustomYear;
        return copy;
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

    @Override
    public String getName() {
        return _name;
    }

    public void setName(String name) {
        this._name = name;
    }

    @Override
    public InUnivariateDoubleDistribution getDistribution() throws ParsingException {
        return ParamUtil.getInstance(distribution, "distribution");
    }

    public void setDistribution(InUnivariateDoubleDistribution distribution) {
        this.distribution = new InUnivariateDoubleDistribution[]{distribution};
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

    @Override
    public boolean requiresSetup() {
        return true;
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

        String attributeName = getAttributeName();
        if(!cag.hasGroupAttribute(attributeName)) {
            LOGGER.trace("add annual attribute '{}' to cag '{}'", attributeName, cag.getName());
            BasicConsumerAgentAnnualGroupAttribute aGrpAttr = new BasicConsumerAgentAnnualGroupAttribute();
            aGrpAttr.setName(attributeName);
            aGrpAttr.setArtificial(false);
            cag.addGroupAttribute(aGrpAttr);
        }

        int year = hasCustomYear()
                ? getCustomYear()
                : parser.getSimulationYear();

        BasicConsumerAgentAnnualGroupAttribute aGrpAttr = (BasicConsumerAgentAnnualGroupAttribute) cag.getGroupAttribute(attributeName);

        if(aGrpAttr.hasYear(year)) {
            if(parser.isRestored()) {
                LOGGER.trace(IRPSection.INITIALIZATION_PARAMETER, "annual attribute '{}' already has year '{}' -> skip", aGrpAttr.getName(), year);
                return;
            } else {
                throw new ParsingException("cag '{}' attribute '{}' already has year '{}'", cag.getName(), aGrpAttr.getName(), year);
            }
        }

        BasicConsumerAgentDoubleGroupAttribute yearAttr = new BasicConsumerAgentDoubleGroupAttribute();
        yearAttr.setName(getName() + "_" + year);
        yearAttr.setArtificial(false);
        yearAttr.setDistribution(parser.parseEntityTo(getDistribution()));

        LOGGER.trace(IRPSection.INITIALIZATION_PARAMETER, "add '{}' to annual attribute '{}' in cag '{}' for year '{}'", yearAttr.getName(), aGrpAttr.getName(), cag.getName(), year);
        aGrpAttr.put(parser.getSimulationYear(), yearAttr);
    }
}
