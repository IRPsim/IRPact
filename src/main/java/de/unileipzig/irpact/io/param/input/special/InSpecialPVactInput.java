package de.unileipzig.irpact.io.param.input.special;

import de.unileipzig.irpact.commons.distribution.DiracUnivariateDoubleDistribution;
import de.unileipzig.irpact.core.agent.consumer.ConsumerAgentGroup;
import de.unileipzig.irpact.core.agent.consumer.attribute.BasicConsumerAgentAnnualGroupAttribute;
import de.unileipzig.irpact.core.agent.consumer.attribute.BasicConsumerAgentDoubleGroupAttribute;
import de.unileipzig.irpact.core.agent.consumer.attribute.ConsumerAgentAnnualGroupAttribute;
import de.unileipzig.irpact.core.agent.consumer.attribute.ConsumerAgentDoubleGroupAttribute;
import de.unileipzig.irpact.core.logging.IRPLogging;
import de.unileipzig.irpact.core.logging.IRPSection;
import de.unileipzig.irpact.core.process.ra.RAConstants;
import de.unileipzig.irpact.core.simulation.SimulationEnvironment;
import de.unileipzig.irpact.core.start.IRPactInputParser;
import de.unileipzig.irpact.develop.Dev;
import de.unileipzig.irpact.io.param.LocalizedUiResource;
import de.unileipzig.irptools.defstructure.annotation.Definition;
import de.unileipzig.irptools.defstructure.annotation.FieldDefinition;
import de.unileipzig.irptools.util.CopyCache;
import de.unileipzig.irptools.util.Copyable;
import de.unileipzig.irptools.util.TreeAnnotationResource;
import de.unileipzig.irptools.util.log.IRPLogger;

import java.lang.invoke.MethodHandles;
import java.util.Map;
import java.util.NoSuchElementException;

import static de.unileipzig.irpact.io.param.input.TreeViewStructureEnum.SPECIALINPUT_PVACT_CONSTRATE;
import static de.unileipzig.irpact.io.param.input.TreeViewStructureEnum.SPECIALINPUT_PVACT_RENORATE;

/**
 * @author Daniel Abitz
 */
@Definition(global = true)
public class InSpecialPVactInput implements Copyable {

    private static final MethodHandles.Lookup L = MethodHandles.lookup();
    public static Class<?> thisClass() {
        return L.lookupClass();
    }
    public static String thisName() {
        return thisClass().getSimpleName();
    }

    private static final IRPLogger LOGGER = IRPLogging.getLogger(thisClass());

    @TreeAnnotationResource.Init
    public static void initRes(TreeAnnotationResource res) {
    }
    @TreeAnnotationResource.Apply
    public static void applyRes(TreeAnnotationResource res) {
    }

    //=========================
    //construction rates
    //=========================

    @FieldDefinition
    @LocalizedUiResource.AddEntry(SPECIALINPUT_PVACT_CONSTRATE)
    @LocalizedUiResource.SimpleSet(
            boolDomain = true
    )
    public boolean useConstRates = false;
    public void setUseConstructionRates(boolean useConstRates) {
        this.useConstRates = useConstRates;
    }
    public boolean isUseConstructionRates() {
        return useConstRates;
    }

    @FieldDefinition
    @LocalizedUiResource.AddEntry(SPECIALINPUT_PVACT_CONSTRATE)
    @LocalizedUiResource.SimpleSet(
            decDefault = 0
    )
    public double const2008 = 0.0;
    @FieldDefinition
    @LocalizedUiResource.AddEntry(SPECIALINPUT_PVACT_CONSTRATE)
    @LocalizedUiResource.SimpleSet(
            decDefault = 0
    )
    public double const2009 = 0.0;
    @FieldDefinition
    @LocalizedUiResource.AddEntry(SPECIALINPUT_PVACT_CONSTRATE)
    @LocalizedUiResource.SimpleSet(
            decDefault = 0
    )
    public double const2010 = 0.0;
    @FieldDefinition
    @LocalizedUiResource.AddEntry(SPECIALINPUT_PVACT_CONSTRATE)
    @LocalizedUiResource.SimpleSet(
            decDefault = 0
    )
    public double const2011 = 0.0;
    @FieldDefinition
    @LocalizedUiResource.AddEntry(SPECIALINPUT_PVACT_CONSTRATE)
    @LocalizedUiResource.SimpleSet(
            decDefault = 0
    )
    public double const2012 = 0.0;
    @FieldDefinition
    @LocalizedUiResource.AddEntry(SPECIALINPUT_PVACT_CONSTRATE)
    @LocalizedUiResource.SimpleSet(
            decDefault = 0
    )
    public double const2013 = 0.0;
    @FieldDefinition
    @LocalizedUiResource.AddEntry(SPECIALINPUT_PVACT_CONSTRATE)
    @LocalizedUiResource.SimpleSet(
            decDefault = 0
    )
    public double const2014 = 0.0;
    @FieldDefinition
    @LocalizedUiResource.AddEntry(SPECIALINPUT_PVACT_CONSTRATE)
    @LocalizedUiResource.SimpleSet(
            decDefault = 0
    )
    public double const2015 = 0.0;
    @FieldDefinition
    @LocalizedUiResource.AddEntry(SPECIALINPUT_PVACT_CONSTRATE)
    @LocalizedUiResource.SimpleSet(
            decDefault = 0
    )
    public double const2016 = 0.0;
    @FieldDefinition
    @LocalizedUiResource.AddEntry(SPECIALINPUT_PVACT_CONSTRATE)
    @LocalizedUiResource.SimpleSet(
            decDefault = 0
    )
    public double const2017 = 0.0;
    @FieldDefinition
    @LocalizedUiResource.AddEntry(SPECIALINPUT_PVACT_CONSTRATE)
    @LocalizedUiResource.SimpleSet(
            decDefault = 0
    )
    public double const2018 = 0.0;
    @FieldDefinition
    @LocalizedUiResource.AddEntry(SPECIALINPUT_PVACT_CONSTRATE)
    @LocalizedUiResource.SimpleSet(
            decDefault = 0
    )
    public double const2019 = 0.0;
    @FieldDefinition
    @LocalizedUiResource.AddEntry(SPECIALINPUT_PVACT_CONSTRATE)
    @LocalizedUiResource.SimpleSet(
            decDefault = 0
    )
    public double const2020 = 0.0;

    public void setConstructionRates(Map<? extends Number, ? extends Number> data) {
        const2008 = getValue(2008, data);
        const2009 = getValue(2009, data);
        const2010 = getValue(2010, data);
        const2011 = getValue(2011, data);
        const2012 = getValue(2012, data);
        const2013 = getValue(2013, data);
        const2014 = getValue(2014, data);
        const2015 = getValue(2015, data);
        const2016 = getValue(2016, data);
        const2017 = getValue(2017, data);
        const2018 = getValue(2018, data);
        const2019 = getValue(2019, data);
        const2020 = getValue(2020, data);
    }

    public ConsumerAgentAnnualGroupAttribute createConstructionRateAttribute(String attrName) {
        BasicConsumerAgentAnnualGroupAttribute cagGrpAttr = new BasicConsumerAgentAnnualGroupAttribute();
        cagGrpAttr.setArtificial(true);
        cagGrpAttr.setName(attrName);

        cagGrpAttr.put(2008, createGroupAttribute(attrName, 2008, const2008));
        cagGrpAttr.put(2009, createGroupAttribute(attrName, 2009, const2009));
        cagGrpAttr.put(2010, createGroupAttribute(attrName, 2010, const2010));
        cagGrpAttr.put(2011, createGroupAttribute(attrName, 2011, const2011));
        cagGrpAttr.put(2012, createGroupAttribute(attrName, 2012, const2012));
        cagGrpAttr.put(2013, createGroupAttribute(attrName, 2013, const2013));
        cagGrpAttr.put(2014, createGroupAttribute(attrName, 2014, const2014));
        cagGrpAttr.put(2015, createGroupAttribute(attrName, 2015, const2015));
        cagGrpAttr.put(2016, createGroupAttribute(attrName, 2016, const2016));
        cagGrpAttr.put(2017, createGroupAttribute(attrName, 2017, const2017));
        cagGrpAttr.put(2018, createGroupAttribute(attrName, 2018, const2018));
        cagGrpAttr.put(2019, createGroupAttribute(attrName, 2019, const2019));
        cagGrpAttr.put(2020, createGroupAttribute(attrName, 2020, const2020));

        return cagGrpAttr;
    }

    private void parseConstructionRateAttribute(SimulationEnvironment environment) {
        if(isUseConstructionRates()) {
            String attrName = RAConstants.CONSTRUCTION_RATE;
            ConsumerAgentAnnualGroupAttribute constRates = createConstructionRateAttribute(attrName);
            for(ConsumerAgentGroup cag: environment.getAgents().getConsumerAgentGroups()) {
                if(cag.hasGroupAttribute(attrName)) {
                    LOGGER.debug("remove old '{}' from '{}'", attrName, cag.getName());
                    cag.removeGroupAttribute(attrName);
                }
                cag.addGroupAttribute(constRates);
            }
        }
    }

    //=========================
    //reno
    //=========================

    @FieldDefinition
    @LocalizedUiResource.AddEntry(SPECIALINPUT_PVACT_RENORATE)
    @LocalizedUiResource.SimpleSet(
            boolDomain = true
    )
    public boolean useRenoRates = false;
    public void setUseRenovationRates(boolean useRenoRates) {
        this.useRenoRates = useRenoRates;
    }
    public boolean isUseRenovationRates() {
        return useRenoRates;
    }

    @FieldDefinition
    @LocalizedUiResource.AddEntry(SPECIALINPUT_PVACT_RENORATE)
    @LocalizedUiResource.SimpleSet(
            decDefault = 0
    )
    public double reno2008 = 0.0;
    @FieldDefinition
    @LocalizedUiResource.AddEntry(SPECIALINPUT_PVACT_RENORATE)
    @LocalizedUiResource.SimpleSet(
            decDefault = 0
    )
    public double reno2009 = 0.0;
    @FieldDefinition
    @LocalizedUiResource.AddEntry(SPECIALINPUT_PVACT_RENORATE)
    @LocalizedUiResource.SimpleSet(
            decDefault = 0
    )
    public double reno2010 = 0.0;
    @FieldDefinition
    @LocalizedUiResource.AddEntry(SPECIALINPUT_PVACT_RENORATE)
    @LocalizedUiResource.SimpleSet(
            decDefault = 0
    )
    public double reno2011 = 0.0;
    @FieldDefinition
    @LocalizedUiResource.AddEntry(SPECIALINPUT_PVACT_RENORATE)
    @LocalizedUiResource.SimpleSet(
            decDefault = 0
    )
    public double reno2012 = 0.0;
    @FieldDefinition
    @LocalizedUiResource.AddEntry(SPECIALINPUT_PVACT_RENORATE)
    @LocalizedUiResource.SimpleSet(
            decDefault = 0
    )
    public double reno2013 = 0.0;
    @FieldDefinition
    @LocalizedUiResource.AddEntry(SPECIALINPUT_PVACT_RENORATE)
    @LocalizedUiResource.SimpleSet(
            decDefault = 0
    )
    public double reno2014 = 0.0;
    @FieldDefinition
    @LocalizedUiResource.AddEntry(SPECIALINPUT_PVACT_RENORATE)
    @LocalizedUiResource.SimpleSet(
            decDefault = 0
    )
    public double reno2015 = 0.0;
    @FieldDefinition
    @LocalizedUiResource.AddEntry(SPECIALINPUT_PVACT_RENORATE)
    @LocalizedUiResource.SimpleSet(
            decDefault = 0
    )
    public double reno2016 = 0.0;
    @FieldDefinition
    @LocalizedUiResource.AddEntry(SPECIALINPUT_PVACT_RENORATE)
    @LocalizedUiResource.SimpleSet(
            decDefault = 0
    )
    public double reno2017 = 0.0;
    @FieldDefinition
    @LocalizedUiResource.AddEntry(SPECIALINPUT_PVACT_RENORATE)
    @LocalizedUiResource.SimpleSet(
            decDefault = 0
    )
    public double reno2018 = 0.0;
    @FieldDefinition
    @LocalizedUiResource.AddEntry(SPECIALINPUT_PVACT_RENORATE)
    @LocalizedUiResource.SimpleSet(
            decDefault = 0
    )
    public double reno2019 = 0.0;
    @FieldDefinition
    @LocalizedUiResource.AddEntry(SPECIALINPUT_PVACT_RENORATE)
    @LocalizedUiResource.SimpleSet(
            decDefault = 0
    )
    public double reno2020 = 0.0;

    public void setRenovationRates(Map<? extends Number, ? extends Number> data) {
        reno2008 = getValue(2008, data);
        reno2009 = getValue(2009, data);
        reno2010 = getValue(2010, data);
        reno2011 = getValue(2011, data);
        reno2012 = getValue(2012, data);
        reno2013 = getValue(2013, data);
        reno2014 = getValue(2014, data);
        reno2015 = getValue(2015, data);
        reno2016 = getValue(2016, data);
        reno2017 = getValue(2017, data);
        reno2018 = getValue(2018, data);
        reno2019 = getValue(2019, data);
        reno2020 = getValue(2020, data);
    }

    public ConsumerAgentAnnualGroupAttribute createRenovationRateAttribute(String attrName) {
        BasicConsumerAgentAnnualGroupAttribute cagGrpAttr = new BasicConsumerAgentAnnualGroupAttribute();
        cagGrpAttr.setArtificial(true);
        cagGrpAttr.setName(attrName);

        cagGrpAttr.put(2008, createGroupAttribute(attrName, 2008, reno2008));
        cagGrpAttr.put(2009, createGroupAttribute(attrName, 2009, reno2009));
        cagGrpAttr.put(2010, createGroupAttribute(attrName, 2010, reno2010));
        cagGrpAttr.put(2011, createGroupAttribute(attrName, 2011, reno2011));
        cagGrpAttr.put(2012, createGroupAttribute(attrName, 2012, reno2012));
        cagGrpAttr.put(2013, createGroupAttribute(attrName, 2013, reno2013));
        cagGrpAttr.put(2014, createGroupAttribute(attrName, 2014, reno2014));
        cagGrpAttr.put(2015, createGroupAttribute(attrName, 2015, reno2015));
        cagGrpAttr.put(2016, createGroupAttribute(attrName, 2016, reno2016));
        cagGrpAttr.put(2017, createGroupAttribute(attrName, 2017, reno2017));
        cagGrpAttr.put(2018, createGroupAttribute(attrName, 2018, reno2018));
        cagGrpAttr.put(2019, createGroupAttribute(attrName, 2019, reno2019));
        cagGrpAttr.put(2020, createGroupAttribute(attrName, 2020, reno2020));

        return cagGrpAttr;
    }

    private void parseRenovationRateAttribute(SimulationEnvironment environment) {
        if(isUseRenovationRates()) {
            String attrName = RAConstants.RENOVATION_RATE;
            ConsumerAgentAnnualGroupAttribute constRates = createRenovationRateAttribute(attrName);
            for(ConsumerAgentGroup cag: environment.getAgents().getConsumerAgentGroups()) {
                if(cag.hasGroupAttribute(attrName)) {
                    LOGGER.debug("remove old '{}' from '{}'", attrName, cag.getName());
                    cag.removeGroupAttribute(attrName);
                }
                cag.addGroupAttribute(constRates);
            }
        }
    }

    //=========================
    //general
    //=========================

    public InSpecialPVactInput() {
    }

    @Override
    public InSpecialPVactInput copy(CopyCache cache) {
        return cache.copyIfAbsent(this, this::newCopy);
    }

    public InSpecialPVactInput newCopy(CopyCache cache) {
        return Dev.throwException();
    }

    //=========================
    //util
    //=========================

    protected static double getValue(int year, Map<? extends Number, ? extends Number> data) {
        Number value = data.get(year);
        if(value == null) {
            throw new NoSuchElementException("missing value for year: " + year);
        }
        return value.doubleValue();
    }

    protected static ConsumerAgentDoubleGroupAttribute createGroupAttribute(String name, int year, double value) {
        LOGGER.trace(IRPSection.INITIALIZATION_PARAMETER, "create group attribute '{}' for year '{}' with value '{}'", name, year, value);

        DiracUnivariateDoubleDistribution dist = new DiracUnivariateDoubleDistribution();
        dist.setName(name + "_" + year + "_dist");
        dist.setValue(value);

        BasicConsumerAgentDoubleGroupAttribute yearGrpAttr = new BasicConsumerAgentDoubleGroupAttribute();
        yearGrpAttr.setName(name + "_" + year);
        yearGrpAttr.setArtificial(true);
        yearGrpAttr.setDistribution(dist);

        return yearGrpAttr;
    }

    //=========================
    //parse
    //=========================

    public void parse(IRPactInputParser parser) {
        parseConstructionRateAttribute(parser.getEnvironment());
        parseRenovationRateAttribute(parser.getEnvironment());
    }
}
