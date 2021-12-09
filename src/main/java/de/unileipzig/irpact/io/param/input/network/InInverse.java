package de.unileipzig.irpact.io.param.input.network;

import de.unileipzig.irpact.commons.eval.Inverse;
import de.unileipzig.irpact.commons.exception.ParsingException;
import de.unileipzig.irpact.commons.spatial.BasicDistanceEvaluator;
import de.unileipzig.irpact.core.start.IRPactInputParser;
import de.unileipzig.irpact.io.param.LocalizedUiResource;
import de.unileipzig.irptools.defstructure.annotation.Definition;
import de.unileipzig.irptools.defstructure.annotation.DefinitionName;
import de.unileipzig.irptools.defstructure.annotation.FieldDefinition;
import de.unileipzig.irptools.util.CopyCache;
import de.unileipzig.irptools.util.TreeAnnotationResource;

import java.lang.invoke.MethodHandles;
import java.util.Objects;

import static de.unileipzig.irpact.io.param.input.TreeViewStructureEnum.NETWORK_DISTFUNC_INVERSE;

/**
 * @author Daniel Abitz
 */
@Definition
@LocalizedUiResource.PutClassPath(NETWORK_DISTFUNC_INVERSE)
public class InInverse implements InDistanceEvaluator {

    private static final MethodHandles.Lookup L = MethodHandles.lookup();
    public static Class<?> thisClass() {
        return L.lookupClass();
    }
    public static String thisName() {
        return thisClass().getSimpleName();
    }

    @TreeAnnotationResource.Init
    public static void initRes(LocalizedUiResource res) {
    }
    @TreeAnnotationResource.Apply
    public static void applyRes(LocalizedUiResource res) {
    }

    @DefinitionName
    public String name;

    @FieldDefinition
    @LocalizedUiResource.AddEntry
    public int placeholderInverse;

    public InInverse() {
    }

    public InInverse(String name) {
        this.name = name;
    }

    @Override
    public InInverse copy(CopyCache cache) {
        return cache.copyIfAbsent(this, this::newCopy);
    }

    public InInverse newCopy(CopyCache cache) {
        InInverse copy = new InInverse();
        copy.name = name;
        return copy;
    }

    @Override
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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
        return placeholderInverse == inInverse.placeholderInverse && Objects.equals(name, inInverse.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, placeholderInverse);
    }

    @Override
    public String toString() {
        return "InInverse{" +
                "_name='" + name + '\'' +
                ", placeholderInverse=" + placeholderInverse +
                '}';
    }
}
