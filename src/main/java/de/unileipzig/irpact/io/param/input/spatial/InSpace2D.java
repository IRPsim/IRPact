package de.unileipzig.irpact.io.param.input.spatial;

import de.unileipzig.irpact.commons.exception.ParsingException;
import de.unileipzig.irpact.core.spatial.twodim.Metric2D;
import de.unileipzig.irpact.core.spatial.twodim.Space2D;
import de.unileipzig.irpact.io.param.input.InputParser;
import de.unileipzig.irptools.defstructure.annotation.Definition;
import de.unileipzig.irptools.defstructure.annotation.FieldDefinition;
import de.unileipzig.irptools.util.CopyCache;
import de.unileipzig.irptools.util.TreeAnnotationResource;

import java.lang.invoke.MethodHandles;
import java.util.ArrayList;
import java.util.List;

import static de.unileipzig.irpact.io.param.IOConstants.*;
import static de.unileipzig.irpact.io.param.ParamUtil.addEntry;
import static de.unileipzig.irpact.io.param.ParamUtil.putClassPath;

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

    public static void initRes(TreeAnnotationResource res) {
    }

    public static void applyRes(TreeAnnotationResource res) {
        putClassPath(res, thisClass(), SPATIAL, SPATIAL_MODEL, thisName());
        addEntry(res, thisClass(), "useManhatten");
        addEntry(res, thisClass(), "useEuclid");
        addEntry(res, thisClass(), "useMaximum");
        addEntry(res, thisClass(), "useHaversine");
    }

    private static final Metric2D[] METRICS = new Metric2D[] {
        Metric2D.MANHATTEN, Metric2D.EUCLIDEAN, Metric2D.MAXIMUM, Metric2D.HAVERSINE_KM
    };

    public String _name;

    @FieldDefinition
    public boolean useManhatten;

    @FieldDefinition
    public boolean useEuclid;

    @FieldDefinition
    public boolean useMaximum;

    @FieldDefinition
    public boolean useHaversine;

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
        copy.useMaximum = useMaximum;
        copy.useHaversine = useHaversine;
        return copy;
    }

    @Override
    public String getName() {
        return _name;
    }

    private boolean[] buildFlagArray() {
        return new boolean[]{useManhatten, useEuclid, useMaximum, useHaversine};
    }

    private List<Metric2D> getMetrics() {
        List<Metric2D> list = new ArrayList<>();
        boolean[] flagArr = buildFlagArray();
        for(int i = 0; i < flagArr.length; i++) {
            if(flagArr[i]) {
                list.add(METRICS[i]);
            }
        }
        return list;
    }

    public void setMetric(Metric2D metric) {
        useManhatten = false;
        useEuclid = false;
        useMaximum = false;
        useHaversine = false;

        switch (metric) {
            case MANHATTEN:
                useManhatten = true;
                break;

            case EUCLIDEAN:
                useEuclid = true;
                break;

            case MAXIMUM:
                useMaximum = true;
                break;

            case HAVERSINE_KM:
                useHaversine = true;
                break;

            default:
                throw new IllegalArgumentException("unsupported metric: " + metric);
        }
    }

    public Metric2D getMetric() throws ParsingException {
        List<Metric2D> metrics = getMetrics();
        switch (metrics.size()) {
            case 0:
                throw new ParsingException("Missing metric");

            case 1:
                return metrics.get(0);

            default:
                throw new ParsingException("Multiple metrics: " + metrics);
        }
    }

    @Override
    public Space2D parse(InputParser parser) throws ParsingException {
        Space2D space2D = new Space2D();
        space2D.setEnvironment(parser.getEnvironment());
        space2D.setName(getName());
        space2D.setMetric(getMetric());
        return space2D;
    }
}
