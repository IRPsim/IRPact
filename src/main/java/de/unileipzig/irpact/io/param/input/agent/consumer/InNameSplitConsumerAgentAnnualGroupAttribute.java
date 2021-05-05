package de.unileipzig.irpact.io.param.input.agent.consumer;

import de.unileipzig.irpact.commons.exception.ParsingException;
import de.unileipzig.irpact.commons.util.ExceptionUtil;
import de.unileipzig.irpact.core.agent.consumer.ConsumerAgentGroup;
import de.unileipzig.irpact.core.agent.consumer.attribute.BasicConsumerAgentAnnualGroupAttribute;
import de.unileipzig.irpact.core.agent.consumer.attribute.BasicConsumerAgentDoubleGroupAttribute;
import de.unileipzig.irpact.core.log.IRPLogging;
import de.unileipzig.irpact.io.param.ParamUtil;
import de.unileipzig.irpact.io.param.input.InAttributeName;
import de.unileipzig.irpact.io.param.input.InRoot;
import de.unileipzig.irpact.io.param.input.InputParser;
import de.unileipzig.irpact.io.param.input.distribution.InUnivariateDoubleDistribution;
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
    public InConsumerAgentGroup getConsumerAgentGroup(InputParser parser) throws ParsingException {
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
    public void setup(InputParser parser, Object input) throws ParsingException {
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
            throw ExceptionUtil.create(ParsingException::new, "cag '{}' attribute '{}' already has year '{}'", cag.getName(), aGrpAttr.getName(), year);
        }

        BasicConsumerAgentDoubleGroupAttribute yearAttr = new BasicConsumerAgentDoubleGroupAttribute();
        yearAttr.setName(getName() + "_" + year);
        yearAttr.setArtificial(true);
        yearAttr.setDistribution(parser.parseEntityTo(getDistribution()));

        LOGGER.trace("add '{}' to annual attribute '{}' in cag '{}' for year '{}'", yearAttr.getName(), aGrpAttr.getName(), cag.getName(), year);
        aGrpAttr.put(parser.getSimulationYear(), yearAttr);
    }
}