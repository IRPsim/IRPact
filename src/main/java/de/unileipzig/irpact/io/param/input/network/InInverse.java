package de.unileipzig.irpact.io.param.input.network;

import de.unileipzig.irpact.commons.eval.Inverse;
import de.unileipzig.irpact.commons.exception.ParsingException;
import de.unileipzig.irpact.commons.spatial.BasicDistanceEvaluator;
import de.unileipzig.irpact.core.start.IRPactInputParser;
import de.unileipzig.irptools.defstructure.annotation.Definition;
import de.unileipzig.irptools.defstructure.annotation.FieldDefinition;
import de.unileipzig.irptools.util.CopyCache;
import de.unileipzig.irptools.util.TreeAnnotationResource;

import java.lang.invoke.MethodHandles;
import java.util.Objects;

import static de.unileipzig.irpact.io.param.IOConstants.DIST_FUNC;
import static de.unileipzig.irpact.io.param.IOConstants.NETWORK;
import static de.unileipzig.irpact.io.param.ParamUtil.addEntry;
import static de.unileipzig.irpact.io.param.ParamUtil.putClassPath;

/**
 * @author Daniel Abitz
 */
@Definition
public class InInverse implements InDistanceEvaluator {

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
        addEntry(res, thisClass(), "placeholderInverse");
    }

    public String _name;

    @FieldDefinition
    public int placeholderInverse;

    public InInverse() {
    }

    public InInverse(String name) {
        this._name = name;
    }

    @Override
    public InInverse copy(CopyCache cache) {
        return cache.copyIfAbsent(this, this::newCopy);
    }

    public InInverse newCopy(CopyCache cache) {
        InInverse copy = new InInverse();
        copy._name = _name;
        return copy;
    }

    @Override
    public String getName() {
        return _name;
    }

    public void setName(String name) {
        this._name = name;
    }

    @Override
    public BasicDistanceEvaluator parse(IRPactInputParser parser) throws ParsingException {
        return new BasicDistanceEvaluator(new Inverse());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof InInverse)) return false;
        InInverse inInverse = (InInverse) o;
        return placeholderInverse == inInverse.placeholderInverse && Objects.equals(_name, inInverse._name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(_name, placeholderInverse);
    }

    @Override
    public String toString() {
        return "InInverse{" +
                "_name='" + _name + '\'' +
                ", placeholderInverse=" + placeholderInverse +
                '}';
    }
}
