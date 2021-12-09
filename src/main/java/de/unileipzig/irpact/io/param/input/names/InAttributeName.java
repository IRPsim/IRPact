package de.unileipzig.irpact.io.param.input.names;

import de.unileipzig.irpact.io.param.LocalizedUiResource;
import de.unileipzig.irptools.defstructure.annotation.Definition;
import de.unileipzig.irptools.defstructure.annotation.DefinitionName;
import de.unileipzig.irptools.defstructure.annotation.FieldDefinition;
import de.unileipzig.irptools.util.CopyCache;
import de.unileipzig.irptools.util.TreeAnnotationResource;

import java.lang.invoke.MethodHandles;

import static de.unileipzig.irpact.io.param.input.TreeViewStructureEnum.ATTRNAMES;

/**
 * @author Daniel Abitz
 */
@Definition
@LocalizedUiResource.PutClassPath(ATTRNAMES)
public class InAttributeName implements InName {

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
    public int placeholder;

    public InAttributeName() {
    }

    public InAttributeName(String name) {
        this.name = name;
    }

    @Override
    public InAttributeName copy(CopyCache cache) {
        return cache.copyIfAbsent(this, this::newCopy);
    }

    public InAttributeName newCopy(CopyCache cache) {
        InAttributeName copy = new InAttributeName();
        copy.name = name;
        return copy;
    }

    public String getName() {
        return name;
    }
}
