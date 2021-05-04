package de.unileipzig.irpact.io.param.input.agent.consumer;

import de.unileipzig.irpact.commons.exception.ParsingException;
import de.unileipzig.irpact.commons.util.ExceptionUtil;
import de.unileipzig.irpact.core.agent.consumer.attribute.BasicConsumerAgentAnnualGroupAttribute;
import de.unileipzig.irpact.core.agent.consumer.attribute.BasicConsumerAgentDoubleGroupAttribute;
import de.unileipzig.irpact.core.log.IRPLogging;
import de.unileipzig.irpact.io.param.ParamUtil;
import de.unileipzig.irpact.io.param.input.InAttributeName;
import de.unileipzig.irpact.io.param.input.InputParser;
import de.unileipzig.irpact.io.param.input.distribution.InUnivariateDoubleDistribution;
import de.unileipzig.irpact.jadex.agents.consumer.JadexConsumerAgentGroup;
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
public class InDummyConsumerAgentAnnualGroupAttribute {

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
        putClassPath(res, thisClass(), AGENTS, CONSUMER, CONSUMER_GROUP, thisName());
        addEntry(res, thisClass(), "cagAttributes");
        addEntry(res, thisClass(), "cagInterest");
        addEntry(res, thisClass(), "productFindingSchemes");
        addEntry(res, thisClass(), "spatialDistribution");
    }

    private static final IRPLogger LOGGER = IRPLogging.getLogger(thisClass());

    public String _name;

    public InAttributeName[] attributeName;

    public int year;

    public InUnivariateDoubleDistribution[] distribution;

    public InDummyConsumerAgentAnnualGroupAttribute() {
    }

    public InDummyConsumerAgentAnnualGroupAttribute(
            String name,
            String attributeName,
            int year,
            InUnivariateDoubleDistribution distribution) {
        this(name, new InAttributeName(attributeName), year, distribution);
    }

    public InDummyConsumerAgentAnnualGroupAttribute(
            String name,
            InAttributeName attributeName,
            int year,
            InUnivariateDoubleDistribution distribution) {
        setName(name);
        setAttributeName(attributeName);
        setYear(year);
        setDistribution(distribution);
    }

    public String getName() {
        return _name;
    }

    public void setName(String name) {
        this._name = name;
    }

    public InAttributeName getAttributeName() throws ParsingException {
        return ParamUtil.getInstance(attributeName, "attributeName");
    }

    public String getAttributeNameString() throws ParsingException {
        return getAttributeName().getName();
    }

    public void setAttributeName(InAttributeName attributeName) {
        this.attributeName = new InAttributeName[]{attributeName};
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public InUnivariateDoubleDistribution getDistribution() throws ParsingException {
        return ParamUtil.getInstance(distribution, "distribution");
    }

    public void setDistribution(InUnivariateDoubleDistribution distribution) {
        this.distribution = new InUnivariateDoubleDistribution[]{distribution};
    }

    public void setup(InputParser parser, Object input) throws ParsingException {
        JadexConsumerAgentGroup jcag = (JadexConsumerAgentGroup) input;
        String attributeName = getAttributeNameString();
        if(!jcag.hasGroupAttribute(attributeName)) {
            BasicConsumerAgentAnnualGroupAttribute aGrpAttr = new BasicConsumerAgentAnnualGroupAttribute();
            aGrpAttr.setName(attributeName);
            aGrpAttr.setArtificial(false);
            jcag.addGroupAttribute(aGrpAttr);
        }

        BasicConsumerAgentAnnualGroupAttribute aGrpAttr = (BasicConsumerAgentAnnualGroupAttribute) jcag.getGroupAttribute(attributeName);
        if(aGrpAttr.hasYear(getYear())) {
            throw ExceptionUtil.create(ParsingException::new, "cag '{}' attribute '{}' already has year '{}'", jcag.getName(), aGrpAttr.getName(), getYear());
        }

        BasicConsumerAgentDoubleGroupAttribute yearAttr = new BasicConsumerAgentDoubleGroupAttribute();
        yearAttr.setName(getName() + "_" + getYear());
        yearAttr.setArtificial(true);
        yearAttr.setDistribution(parser.parseEntityTo(getDistribution()));

        LOGGER.trace("add '{}' to annual attribute '{}' in cag '{}'", yearAttr.getName(), aGrpAttr.getName(), jcag.getName());
        aGrpAttr.put(year, yearAttr);
    }
}
