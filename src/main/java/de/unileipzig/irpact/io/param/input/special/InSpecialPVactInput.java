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
    @FieldDefinition
    @LocalizedUiResource.AddEntry(SPECIALINPUT_PVACT_CONSTRATE)
    @LocalizedUiResource.SimpleSet(
            decDefault = 0
    )
    public double const2021 = 0.0;
    @FieldDefinition
    @LocalizedUiResource.AddEntry(SPECIALINPUT_PVACT_CONSTRATE)
    @LocalizedUiResource.SimpleSet(
            decDefault = 0
    )
    public double const2022 = 0.0;
    @FieldDefinition
    @LocalizedUiResource.AddEntry(SPECIALINPUT_PVACT_CONSTRATE)
    @LocalizedUiResource.SimpleSet(
            decDefault = 0
    )
    public double const2023 = 0.0;
    @FieldDefinition
    @LocalizedUiResource.AddEntry(SPECIALINPUT_PVACT_CONSTRATE)
    @LocalizedUiResource.SimpleSet(
            decDefault = 0
    )
    public double const2024 = 0.0;
    @FieldDefinition
    @LocalizedUiResource.AddEntry(SPECIALINPUT_PVACT_CONSTRATE)
    @LocalizedUiResource.SimpleSet(
            decDefault = 0
    )
    public double const2025 = 0.0;
    @FieldDefinition
    @LocalizedUiResource.AddEntry(SPECIALINPUT_PVACT_CONSTRATE)
    @LocalizedUiResource.SimpleSet(
            decDefault = 0
    )
    public double const2026 = 0.0;
    @FieldDefinition
    @LocalizedUiResource.AddEntry(SPECIALINPUT_PVACT_CONSTRATE)
    @LocalizedUiResource.SimpleSet(
            decDefault = 0
    )
    public double const2027 = 0.0;
    @FieldDefinition
    @LocalizedUiResource.AddEntry(SPECIALINPUT_PVACT_CONSTRATE)
    @LocalizedUiResource.SimpleSet(
            decDefault = 0
    )
    public double const2028 = 0.0;
    @FieldDefinition
    @LocalizedUiResource.AddEntry(SPECIALINPUT_PVACT_CONSTRATE)
    @LocalizedUiResource.SimpleSet(
            decDefault = 0
    )
    public double const2029 = 0.0;
    @FieldDefinition
    @LocalizedUiResource.AddEntry(SPECIALINPUT_PVACT_CONSTRATE)
    @LocalizedUiResource.SimpleSet(
            decDefault = 0
    )
    public double const2030 = 0.0;
    @FieldDefinition
    @LocalizedUiResource.AddEntry(SPECIALINPUT_PVACT_CONSTRATE)
    @LocalizedUiResource.SimpleSet(
            decDefault = 0
    )
    public double const2031 = 0.0;
    @FieldDefinition
    @LocalizedUiResource.AddEntry(SPECIALINPUT_PVACT_CONSTRATE)
    @LocalizedUiResource.SimpleSet(
            decDefault = 0
    )
    public double const2032 = 0.0;
    @FieldDefinition
    @LocalizedUiResource.AddEntry(SPECIALINPUT_PVACT_CONSTRATE)
    @LocalizedUiResource.SimpleSet(
            decDefault = 0
    )
    public double const2033 = 0.0;
    @FieldDefinition
    @LocalizedUiResource.AddEntry(SPECIALINPUT_PVACT_CONSTRATE)
    @LocalizedUiResource.SimpleSet(
            decDefault = 0
    )
    public double const2034 = 0.0;
    @FieldDefinition
    @LocalizedUiResource.AddEntry(SPECIALINPUT_PVACT_CONSTRATE)
    @LocalizedUiResource.SimpleSet(
            decDefault = 0
    )
    public double const2035 = 0.0;
    @FieldDefinition
    @LocalizedUiResource.AddEntry(SPECIALINPUT_PVACT_CONSTRATE)
    @LocalizedUiResource.SimpleSet(
            decDefault = 0
    )
    public double const2036 = 0.0;
    @FieldDefinition
    @LocalizedUiResource.AddEntry(SPECIALINPUT_PVACT_CONSTRATE)
    @LocalizedUiResource.SimpleSet(
            decDefault = 0
    )
    public double const2037 = 0.0;
    @FieldDefinition
    @LocalizedUiResource.AddEntry(SPECIALINPUT_PVACT_CONSTRATE)
    @LocalizedUiResource.SimpleSet(
            decDefault = 0
    )
    public double const2038 = 0.0;
    @FieldDefinition
    @LocalizedUiResource.AddEntry(SPECIALINPUT_PVACT_CONSTRATE)
    @LocalizedUiResource.SimpleSet(
            decDefault = 0
    )
    public double const2039 = 0.0;
    @FieldDefinition
    @LocalizedUiResource.AddEntry(SPECIALINPUT_PVACT_CONSTRATE)
    @LocalizedUiResource.SimpleSet(
            decDefault = 0
    )
    public double const2040 = 0.0;

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
        const2021 = getValue(2021, data);
        const2022 = getValue(2022, data);
        const2023 = getValue(2023, data);
        const2024 = getValue(2024, data);
        const2025 = getValue(2025, data);
        const2026 = getValue(2026, data);
        const2027 = getValue(2027, data);
        const2028 = getValue(2028, data);
        const2029 = getValue(2029, data);
        const2030 = getValue(2030, data);
        const2031 = getValue(2031, data);
        const2032 = getValue(2032, data);
        const2033 = getValue(2033, data);
        const2034 = getValue(2034, data);
        const2035 = getValue(2035, data);
        const2036 = getValue(2036, data);
        const2037 = getValue(2037, data);
        const2038 = getValue(2038, data);
        const2039 = getValue(2039, data);
        const2040 = getValue(2040, data);
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
        cagGrpAttr.put(2021, createGroupAttribute(attrName, 2021, const2021));
        cagGrpAttr.put(2022, createGroupAttribute(attrName, 2022, const2022));
        cagGrpAttr.put(2023, createGroupAttribute(attrName, 2023, const2023));
        cagGrpAttr.put(2024, createGroupAttribute(attrName, 2024, const2024));
        cagGrpAttr.put(2025, createGroupAttribute(attrName, 2025, const2025));
        cagGrpAttr.put(2026, createGroupAttribute(attrName, 2026, const2026));
        cagGrpAttr.put(2027, createGroupAttribute(attrName, 2027, const2027));
        cagGrpAttr.put(2028, createGroupAttribute(attrName, 2028, const2028));
        cagGrpAttr.put(2029, createGroupAttribute(attrName, 2029, const2029));
        cagGrpAttr.put(2030, createGroupAttribute(attrName, 2030, const2030));
        cagGrpAttr.put(2031, createGroupAttribute(attrName, 2031, const2031));
        cagGrpAttr.put(2032, createGroupAttribute(attrName, 2032, const2032));
        cagGrpAttr.put(2033, createGroupAttribute(attrName, 2033, const2033));
        cagGrpAttr.put(2034, createGroupAttribute(attrName, 2034, const2034));
        cagGrpAttr.put(2035, createGroupAttribute(attrName, 2035, const2035));
        cagGrpAttr.put(2036, createGroupAttribute(attrName, 2036, const2036));
        cagGrpAttr.put(2037, createGroupAttribute(attrName, 2037, const2037));
        cagGrpAttr.put(2038, createGroupAttribute(attrName, 2038, const2038));
        cagGrpAttr.put(2039, createGroupAttribute(attrName, 2039, const2039));
        cagGrpAttr.put(2040, createGroupAttribute(attrName, 2040, const2040));

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
    @FieldDefinition
    @LocalizedUiResource.AddEntry(SPECIALINPUT_PVACT_RENORATE)
    @LocalizedUiResource.SimpleSet(
            decDefault = 0
    )
    public double reno2021 = 0.0;
    @FieldDefinition
    @LocalizedUiResource.AddEntry(SPECIALINPUT_PVACT_RENORATE)
    @LocalizedUiResource.SimpleSet(
            decDefault = 0
    )
    public double reno2022 = 0.0;
    @FieldDefinition
    @LocalizedUiResource.AddEntry(SPECIALINPUT_PVACT_RENORATE)
    @LocalizedUiResource.SimpleSet(
            decDefault = 0
    )
    public double reno2023 = 0.0;
    @FieldDefinition
    @LocalizedUiResource.AddEntry(SPECIALINPUT_PVACT_RENORATE)
    @LocalizedUiResource.SimpleSet(
            decDefault = 0
    )
    public double reno2024 = 0.0;
    @FieldDefinition
    @LocalizedUiResource.AddEntry(SPECIALINPUT_PVACT_RENORATE)
    @LocalizedUiResource.SimpleSet(
            decDefault = 0
    )
    public double reno2025 = 0.0;
    @FieldDefinition
    @LocalizedUiResource.AddEntry(SPECIALINPUT_PVACT_RENORATE)
    @LocalizedUiResource.SimpleSet(
            decDefault = 0
    )
    public double reno2026 = 0.0;
    @FieldDefinition
    @LocalizedUiResource.AddEntry(SPECIALINPUT_PVACT_RENORATE)
    @LocalizedUiResource.SimpleSet(
            decDefault = 0
    )
    public double reno2027 = 0.0;
    @FieldDefinition
    @LocalizedUiResource.AddEntry(SPECIALINPUT_PVACT_RENORATE)
    @LocalizedUiResource.SimpleSet(
            decDefault = 0
    )
    public double reno2028 = 0.0;
    @FieldDefinition
    @LocalizedUiResource.AddEntry(SPECIALINPUT_PVACT_RENORATE)
    @LocalizedUiResource.SimpleSet(
            decDefault = 0
    )
    public double reno2029 = 0.0;
    @FieldDefinition
    @LocalizedUiResource.AddEntry(SPECIALINPUT_PVACT_RENORATE)
    @LocalizedUiResource.SimpleSet(
            decDefault = 0
    )
    public double reno2030 = 0.0;
    @FieldDefinition
    @LocalizedUiResource.AddEntry(SPECIALINPUT_PVACT_RENORATE)
    @LocalizedUiResource.SimpleSet(
            decDefault = 0
    )
    public double reno2031 = 0.0;
    @FieldDefinition
    @LocalizedUiResource.AddEntry(SPECIALINPUT_PVACT_RENORATE)
    @LocalizedUiResource.SimpleSet(
            decDefault = 0
    )
    public double reno2032 = 0.0;
    @FieldDefinition
    @LocalizedUiResource.AddEntry(SPECIALINPUT_PVACT_RENORATE)
    @LocalizedUiResource.SimpleSet(
            decDefault = 0
    )
    public double reno2033 = 0.0;
    @FieldDefinition
    @LocalizedUiResource.AddEntry(SPECIALINPUT_PVACT_RENORATE)
    @LocalizedUiResource.SimpleSet(
            decDefault = 0
    )
    public double reno2034 = 0.0;
    @FieldDefinition
    @LocalizedUiResource.AddEntry(SPECIALINPUT_PVACT_RENORATE)
    @LocalizedUiResource.SimpleSet(
            decDefault = 0
    )
    public double reno2035 = 0.0;
    @FieldDefinition
    @LocalizedUiResource.AddEntry(SPECIALINPUT_PVACT_RENORATE)
    @LocalizedUiResource.SimpleSet(
            decDefault = 0
    )
    public double reno2036 = 0.0;
    @FieldDefinition
    @LocalizedUiResource.AddEntry(SPECIALINPUT_PVACT_RENORATE)
    @LocalizedUiResource.SimpleSet(
            decDefault = 0
    )
    public double reno2037 = 0.0;
    @FieldDefinition
    @LocalizedUiResource.AddEntry(SPECIALINPUT_PVACT_RENORATE)
    @LocalizedUiResource.SimpleSet(
            decDefault = 0
    )
    public double reno2038 = 0.0;
    @FieldDefinition
    @LocalizedUiResource.AddEntry(SPECIALINPUT_PVACT_RENORATE)
    @LocalizedUiResource.SimpleSet(
            decDefault = 0
    )
    public double reno2039 = 0.0;
    @FieldDefinition
    @LocalizedUiResource.AddEntry(SPECIALINPUT_PVACT_RENORATE)
    @LocalizedUiResource.SimpleSet(
            decDefault = 0
    )
    public double reno2040 = 0.0;

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
        reno2021 = getValue(2021, data);
        reno2022 = getValue(2022, data);
        reno2023 = getValue(2023, data);
        reno2024 = getValue(2024, data);
        reno2025 = getValue(2025, data);
        reno2026 = getValue(2026, data);
        reno2027 = getValue(2027, data);
        reno2028 = getValue(2028, data);
        reno2029 = getValue(2029, data);
        reno2030 = getValue(2030, data);
        reno2031 = getValue(2031, data);
        reno2032 = getValue(2032, data);
        reno2033 = getValue(2033, data);
        reno2034 = getValue(2034, data);
        reno2035 = getValue(2035, data);
        reno2036 = getValue(2036, data);
        reno2037 = getValue(2037, data);
        reno2038 = getValue(2038, data);
        reno2039 = getValue(2039, data);
        reno2040 = getValue(2040, data);
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
        cagGrpAttr.put(2021, createGroupAttribute(attrName, 2021, reno2021));
        cagGrpAttr.put(2022, createGroupAttribute(attrName, 2022, reno2022));
        cagGrpAttr.put(2023, createGroupAttribute(attrName, 2023, reno2023));
        cagGrpAttr.put(2024, createGroupAttribute(attrName, 2024, reno2024));
        cagGrpAttr.put(2025, createGroupAttribute(attrName, 2025, reno2025));
        cagGrpAttr.put(2026, createGroupAttribute(attrName, 2026, reno2026));
        cagGrpAttr.put(2027, createGroupAttribute(attrName, 2027, reno2027));
        cagGrpAttr.put(2028, createGroupAttribute(attrName, 2028, reno2028));
        cagGrpAttr.put(2029, createGroupAttribute(attrName, 2029, reno2029));
        cagGrpAttr.put(2030, createGroupAttribute(attrName, 2030, reno2030));
        cagGrpAttr.put(2031, createGroupAttribute(attrName, 2031, reno2031));
        cagGrpAttr.put(2032, createGroupAttribute(attrName, 2032, reno2032));
        cagGrpAttr.put(2033, createGroupAttribute(attrName, 2033, reno2033));
        cagGrpAttr.put(2034, createGroupAttribute(attrName, 2034, reno2034));
        cagGrpAttr.put(2035, createGroupAttribute(attrName, 2035, reno2035));
        cagGrpAttr.put(2036, createGroupAttribute(attrName, 2036, reno2036));
        cagGrpAttr.put(2037, createGroupAttribute(attrName, 2037, reno2037));
        cagGrpAttr.put(2038, createGroupAttribute(attrName, 2038, reno2038));
        cagGrpAttr.put(2039, createGroupAttribute(attrName, 2039, reno2039));
        cagGrpAttr.put(2040, createGroupAttribute(attrName, 2040, reno2040));

        return cagGrpAttr;
    }

    private void parseRenovationRateAttribute(SimulationEnvironment environment) {
        if(isUseRenovationRates()) {
            String attrName = RAConstants.RENOVATION_RATE;
            ConsumerAgentAnnualGroupAttribute renoRates = createRenovationRateAttribute(attrName);
            for(ConsumerAgentGroup cag: environment.getAgents().getConsumerAgentGroups()) {
                if(cag.hasGroupAttribute(attrName)) {
                    LOGGER.debug("remove old '{}' from '{}'", attrName, cag.getName());
                    cag.removeGroupAttribute(attrName);
                }
                cag.addGroupAttribute(renoRates);
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
