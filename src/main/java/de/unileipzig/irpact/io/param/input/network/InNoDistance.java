package de.unileipzig.irpact.io.param.input.network;

import de.unileipzig.irpact.commons.eval.NoDistance;
import de.unileipzig.irpact.commons.exception.ParsingException;
import de.unileipzig.irpact.commons.spatial.BasicDistanceEvaluator;
import de.unileipzig.irpact.io.param.input.InputParser;
import de.unileipzig.irptools.defstructure.annotation.Definition;
import de.unileipzig.irptools.defstructure.annotation.FieldDefinition;
import de.unileipzig.irptools.util.CopyCache;
import de.unileipzig.irptools.util.TreeAnnotationResource;

import java.lang.invoke.MethodHandles;
import java.util.Objects;

import static de.unileipzig.irpact.io.param.IOConstants.*;
import static de.unileipzig.irpact.io.param.ParamUtil.addEntry;
import static de.unileipzig.irpact.io.param.ParamUtil.putClassPath;

/**
 * @author Daniel Abitz
 */
@Definition
public class InNoDistance implements InDistanceEvaluator {

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
        putClassPath(res, thisClass(), NETWORK, DIST_FUNC, thisName());
        addEntry(res, thisClass(), "placeholderNoDistance");
    }

    public String _name;

    @FieldDefinition
    public int placeholderNoDistance;

    public InNoDistance() {
    }

    public InNoDistance(String name) {
        this._name = name;
    }

    @Override
    public InNoDistance copy(CopyCache cache) {
        return cache.copyIfAbsent(this, this::newCopy);
    }

    public InNoDistance newCopy(CopyCache cache) {
        InNoDistance copy = new InNoDistance();
        copy._name = _name;
        return copy;
    }

    @Override
    public String getName() {
        return _name;
    }

    public void setName(String _name) {
        this._name = _name;
    }

    @Override
    public BasicDistanceEvaluator parse(InputParser parser) throws ParsingException {
        return new BasicDistanceEvaluator(new NoDistance());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof InNoDistance)) return false;
        InNoDistance that = (InNoDistance) o;
        return placeholderNoDistance == that.placeholderNoDistance && Objects.equals(_name, that._name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(_name, placeholderNoDistance);
    }

    @Override
    public String toString() {
        return "InNoDistance{" +
                "_name='" + _name + '\'' +
                ", placeholderNoDistance=" + placeholderNoDistance +
                '}';
    }
}
