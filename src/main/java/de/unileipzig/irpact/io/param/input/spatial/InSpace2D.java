package de.unileipzig.irpact.io.param.input.spatial;

import de.unileipzig.irpact.commons.exception.ParsingException;
import de.unileipzig.irpact.core.spatial.twodim.Metric2D;
import de.unileipzig.irpact.core.spatial.twodim.Space2D;
import de.unileipzig.irpact.core.start.IRPactInputParser;
import de.unileipzig.irpact.io.param.LocalizedUiResource;
import de.unileipzig.irptools.Constants;
import de.unileipzig.irptools.defstructure.annotation.Definition;
import de.unileipzig.irptools.defstructure.annotation.DefinitionName;
import de.unileipzig.irptools.defstructure.annotation.FieldDefinition;
import de.unileipzig.irptools.util.CopyCache;
import de.unileipzig.irptools.util.TreeAnnotationResource;
import de.unileipzig.irptools.util.XorWithoutUnselectRuleBuilder;

import java.lang.invoke.MethodHandles;
import java.util.ArrayList;
import java.util.List;

import static de.unileipzig.irpact.io.param.ParamUtil.*;
import static de.unileipzig.irpact.io.param.input.TreeViewStructureEnum.SPATIAL_MODEL_2D;

/**
 * @author Daniel Abitz
 */
@Definition
@LocalizedUiResource.PutClassPath(SPATIAL_MODEL_2D)
@LocalizedUiResource.XorWithoutUnselectRule
public class InSpace2D implements InSpatialModel {

    private static final MethodHandles.Lookup L = MethodHandles.lookup();
    public static Class<?> thisClass() {
        return L.lookupClass();
    }
    public static String thisName() {
        return thisClass().getSimpleName();
    }

//    protected static final String[] metricFieldNames = {"useManhatten", "useEuclid", "useEuclid2", "useMaximum", "useHaversineM", "useHaversineKM"};
//    protected static final XorWithoutUnselectRuleBuilder metricBuilder = new XorWithoutUnselectRuleBuilder()
//            .withKeyModifier(buildDefaultParameterNameOperator(thisClass()))
//            .withTrueValue(Constants.TRUE1)
//            .withFalseValue(Constants.FALSE0)
//            .withKeys(metricFieldNames);

    @TreeAnnotationResource.Init
    public static void initRes(LocalizedUiResource res) {
    }
    @TreeAnnotationResource.Apply
    public static void applyRes(LocalizedUiResource res) {
//        res.setRules(thisClass(), metricFieldNames, metricBuilder);
    }

    @DefinitionName
    public String name;

    @FieldDefinition
    @LocalizedUiResource.AddEntry
    @LocalizedUiResource.SimpleSet(
            boolDomain = true
    )
    @LocalizedUiResource.XorWithoutUnselectRuleEntry
    public boolean useManhatten = false;

    @FieldDefinition
    @LocalizedUiResource.AddEntry
    @LocalizedUiResource.SimpleSet(
            boolDomain = true
    )
    @LocalizedUiResource.XorWithoutUnselectRuleEntry
    public boolean useEuclid = false;

    @FieldDefinition
    @LocalizedUiResource.AddEntry
    @LocalizedUiResource.SimpleSet(
            boolDomain = true
    )
    @LocalizedUiResource.XorWithoutUnselectRuleEntry
    public boolean useEuclid2 = false;

    @FieldDefinition
    @LocalizedUiResource.AddEntry
    @LocalizedUiResource.SimpleSet(
            boolDomain = true
    )
    @LocalizedUiResource.XorWithoutUnselectRuleEntry
    public boolean useMaximum = false;

    @FieldDefinition
    @LocalizedUiResource.AddEntry
    @LocalizedUiResource.SimpleSet(
            boolDomain = true
    )
    @LocalizedUiResource.XorWithoutUnselectRuleEntry
    public boolean useHaversineM = false;

    @FieldDefinition
    @LocalizedUiResource.AddEntry
    @LocalizedUiResource.SimpleSet(
            boolDomain = true,
            boolDefault = true
    )
    @LocalizedUiResource.XorWithoutUnselectRuleEntry
    public boolean useHaversineKM = true;

    public InSpace2D() {
    }

    public InSpace2D(String name, Metric2D metric) {
        this.name = name;
        setMetric(metric);
    }

    @Override
    public InSpace2D copy(CopyCache cache) {
        return cache.copyIfAbsent(this, this::newCopy);
    }

    public InSpace2D newCopy(CopyCache cache) {
        InSpace2D copy = new InSpace2D();
        copy.name = name;
        copy.useManhatten = useManhatten;
        copy.useEuclid = useEuclid;
        copy.useEuclid2 = useEuclid2;
        copy.useMaximum = useMaximum;
        copy.useHaversineM = useHaversineM;
        copy.useHaversineKM = useHaversineKM;
        return copy;
    }

    @Override
    public String getName() {
        return name;
    }

    public void setMetric(Metric2D metric) {
        useManhatten = false;
        useEuclid = false;
        useEuclid2 = false;
        useMaximum = false;
        useHaversineM = false;
        useHaversineKM = false;

        switch (metric) {
            case MANHATTEN:
                useManhatten = true;
                break;

            case EUCLIDEAN:
                useEuclid = true;
                break;

            case EUCLIDEAN2:
                useEuclid2 = true;
                break;

            case MAXIMUM:
                useMaximum = true;
                break;

            case HAVERSINE_M:
                useHaversineM = true;
                break;

            case HAVERSINE_KM:
                useHaversineKM = true;
                break;

            default:
                throw new IllegalArgumentException("unsupported metric: " + metric);
        }
    }

    public Metric2D getMetric() throws ParsingException {
        List<Metric2D> units = new ArrayList<>();
        if(useManhatten) units.add(Metric2D.MANHATTEN);
        if(useEuclid) units.add(Metric2D.EUCLIDEAN);
        if(useEuclid2) units.add(Metric2D.EUCLIDEAN2);
        if(useMaximum) units.add(Metric2D.MAXIMUM);
        if(useHaversineM) units.add(Metric2D.HAVERSINE_M);
        if(useHaversineKM) units.add(Metric2D.HAVERSINE_KM);

        switch(units.size()) {
            case 0:
                throw new ParsingException("Missing time unit");

            case 1:
                return units.get(0);

            default:
                throw new ParsingException("Multiple time units set: " + units);
        }
    }

    @Override
    public Space2D parse(IRPactInputParser parser) throws ParsingException {
        Space2D space2D = new Space2D();
        space2D.setEnvironment(parser.getEnvironment());
        space2D.setName(getName());
        space2D.setMetric(getMetric());
        return space2D;
    }
}
