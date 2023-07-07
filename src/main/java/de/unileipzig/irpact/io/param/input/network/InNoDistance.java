package de.unileipzig.irpact.io.param.input.network;

import de.unileipzig.irpact.commons.eval.NoDistance;
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

import static de.unileipzig.irpact.io.param.IOConstants.*;
import static de.unileipzig.irpact.io.param.ParamUtil.addEntry;
import static de.unileipzig.irpact.io.param.ParamUtil.putClassPath;
import static de.unileipzig.irpact.io.param.input.TreeViewStructureEnum.NETWORK_DISTFUNC_NO;

/**
 * @author Daniel Abitz
 */
@Definition
@LocalizedUiResource.PutClassPath(NETWORK_DISTFUNC_NO)
public class InNoDistance implements InDistanceEvaluator {

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
    public int placeholderNoDistance;

    public InNoDistance() {
    }

    public InNoDistance(String name) {
        this.name = name;
    }

    @Override
    public InNoDistance copy(CopyCache cache) {
        return cache.copyIfAbsent(this, this::newCopy);
    }

    public InNoDistance newCopy(CopyCache cache) {
        InNoDistance copy = new InNoDistance();
        copy.name = name;
        return copy;
    }

    @Override
    public String getName() {
        return name;
    }

    public void setName(String _name) {
        this.name = _name;
    }

    @Override
    public BasicDistanceEvaluator parse(IRPactInputParser parser) throws ParsingException {
        return new BasicDistanceEvaluator(new NoDistance());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof InNoDistance)) return false;
        InNoDistance that = (InNoDistance) o;
        return placeholderNoDistance == that.placeholderNoDistance && Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, placeholderNoDistance);
    }

    @Override
    public String toString() {
        return "InNoDistance{" +
                "_name='" + name + '\'' +
                ", placeholderNoDistance=" + placeholderNoDistance +
                '}';
    }
}
