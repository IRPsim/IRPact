package de.unileipzig.irpact.io.param.input.spatial;

import de.unileipzig.irpact.commons.exception.ParsingException;
import de.unileipzig.irpact.core.spatial.Metric;
import de.unileipzig.irpact.core.spatial.twodim.Metric2D;
import de.unileipzig.irpact.core.spatial.twodim.Space2D;
import de.unileipzig.irpact.io.param.input.InputParser;
import de.unileipzig.irptools.defstructure.annotation.Definition;
import de.unileipzig.irptools.defstructure.annotation.FieldDefinition;
import de.unileipzig.irptools.util.TreeAnnotationResource;

import java.lang.invoke.MethodHandles;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @author Daniel Abitz
 */
@Definition
public class InSpace2D implements InSpatialModel {

    //damit ich bei copy&paste nie mehr vergesse die Klasse anzupassen :)
    private static final MethodHandles.Lookup L = MethodHandles.lookup();
    public static Class<?> thisClass() {
        return L.lookupClass();
    }

    public static void initRes(TreeAnnotationResource res) {
    }

    public static void applyRes(TreeAnnotationResource res) {
        res.putPath(
                thisClass(),
                res.getCachedElement("Räumliche Modell"),
                res.getCachedElement("Space2D")
        );
    }

    private static final Metric2D[] METRICS = new Metric2D[] {
        Metric2D.MANHATTEN, Metric2D.EUCLIDEAN, Metric2D.EUCLIDEAN2, Metric2D.MAXIMUM
    };

    public String _name;

    @FieldDefinition
    public boolean useManhatten;

    @FieldDefinition
    public boolean useEuclid = true;

    @FieldDefinition
    public boolean useEuclid2;

    @FieldDefinition
    public boolean useMaximum;

    public InSpace2D() {
    }

    public InSpace2D(String name, Metric2D metric) {
        this._name = name;
        setMetric(metric);
    }

    @Override
    public String getName() {
        return _name;
    }

    private boolean[] buildFlagArray() {
        return new boolean[]{useManhatten, useEuclid, useEuclid2, useMaximum};
    }

    private List<Metric2D> getMetrics() throws ParsingException {
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
        useEuclid2 = false;
        useMaximum = false;

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

            default:
                throw new IllegalArgumentException("unsupported metric: " + metric);
        }
    }

    public Metric2D getMetric() throws ParsingException {
        List<Metric2D> metrics = getMetrics();
        switch (metrics.size()) {
            case 0:
                throw new ParsingException("Missing time unit");

            case 1:
                return metrics.get(0);

            default:
                throw new ParsingException("Multiple time units set: " + metrics);
        }
    }

    public boolean useEuclid() {
        return useEuclid;
    }

    @Override
    public Space2D parse(InputParser parser) throws ParsingException {
        Space2D space2D = new Space2D();
        space2D.setEnvironment(parser.getEnvironment());
        space2D.setName(getName());
        space2D.setMetric(getMetric());
        return space2D;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof InSpace2D)) return false;
        InSpace2D space2D = (InSpace2D) o;
        return useEuclid == space2D.useEuclid && Objects.equals(_name, space2D._name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(_name, useEuclid);
    }

    @Override
    public String toString() {
        return "InSpace2D{" +
                "_name='" + _name + '\'' +
                ", useEuclid=" + useEuclid +
                '}';
    }
}
