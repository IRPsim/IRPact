package de.unileipzig.irpact.io.param.input.spatial;

import de.unileipzig.irpact.commons.exception.ParsingException;
import de.unileipzig.irpact.core.spatial.twodim.Metric2D;
import de.unileipzig.irpact.core.spatial.twodim.Space2D;
import de.unileipzig.irpact.core.start.IRPactInputParser;
import de.unileipzig.irptools.Constants;
import de.unileipzig.irptools.defstructure.annotation.Definition;
import de.unileipzig.irptools.defstructure.annotation.FieldDefinition;
import de.unileipzig.irptools.util.CopyCache;
import de.unileipzig.irptools.util.TreeAnnotationResource;
import de.unileipzig.irptools.util.XorWithoutUnselectRuleBuilder;

import java.lang.invoke.MethodHandles;
import java.util.ArrayList;
import java.util.List;

import static de.unileipzig.irpact.io.param.IOConstants.*;
import static de.unileipzig.irpact.io.param.ParamUtil.*;

/**
 * @author Daniel Abitz
 */
@Definition
public class InSpace2D implements InSpatialModel {

    private static final MethodHandles.Lookup L = MethodHandles.lookup();
    public static Class<?> thisClass() {
        return L.lookupClass();
    }
    public static String thisName() {
        return thisClass().getSimpleName();
    }


    protected static final String[] metricFieldNames = {"useManhatten", "useEuclid", "useEuclid2", "useMaximum", "useHaversineM", "useHaversineKM"};
    protected static final XorWithoutUnselectRuleBuilder metricBuilder = new XorWithoutUnselectRuleBuilder()
            .withKeyModifier(buildDefaultParameterNameOperator(thisClass()))
            .withTrueValue(Constants.TRUE1)
            .withFalseValue(Constants.FALSE0)
            .withKeys(metricFieldNames);

    public static void initRes(TreeAnnotationResource res) {
    }
    public static void applyRes(TreeAnnotationResource res) {
        putClassPath(res, thisClass(), SPATIAL, SPATIAL_MODEL, thisName());
        addEntry(res, thisClass(), "useManhatten");
        addEntry(res, thisClass(), "useEuclid");
        addEntry(res, thisClass(), "useEuclid2");
        addEntry(res, thisClass(), "useMaximum");
        addEntry(res, thisClass(), "useHaversineM");
        addEntry(res, thisClass(), "useHaversineKM");

        setDefault(res, thisClass(), new String[]{"useManhatten", "useEuclid", "useEuclid2", "useMaximum", "useHaversineM"}, VALUE_FALSE);
        setDefault(res, thisClass(), "useHaversineKM", VALUE_TRUE);

        setRules(res, thisClass(), metricFieldNames, metricBuilder);
    }

    public String _name;

    @FieldDefinition
    public boolean useManhatten = false;

    @FieldDefinition
    public boolean useEuclid = false;

    @FieldDefinition
    public boolean useEuclid2 = false;

    @FieldDefinition
    public boolean useMaximum = false;

    @FieldDefinition
    public boolean useHaversineM = false;

    @FieldDefinition
    public boolean useHaversineKM = true;

    public InSpace2D() {
    }

    public InSpace2D(String name, Metric2D metric) {
        this._name = name;
        setMetric(metric);
    }

    @Override
    public InSpace2D copy(CopyCache cache) {
        return cache.copyIfAbsent(this, this::newCopy);
    }

    public InSpace2D newCopy(CopyCache cache) {
        InSpace2D copy = new InSpace2D();
        copy._name = _name;
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
        return _name;
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
