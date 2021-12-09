package de.unileipzig.irpact.io.param.input;

import de.unileipzig.irpact.io.param.LocalizedUiResource;
import de.unileipzig.irptools.defstructure.annotation.Definition;
import de.unileipzig.irptools.defstructure.annotation.DefinitionName;
import de.unileipzig.irptools.defstructure.annotation.FieldDefinition;
import de.unileipzig.irptools.util.CopyCache;
import de.unileipzig.irptools.util.TreeAnnotationResource;

import java.lang.invoke.MethodHandles;

import static de.unileipzig.irpact.io.param.input.TreeViewStructureEnum.INFO_ABOUTIRPACT;

/**
 * @author Daniel Abitz
 */
@Definition
@LocalizedUiResource.PutClassPath(INFO_ABOUTIRPACT)
public class InIRPactVersion implements InIRPactEntity {

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
    @LocalizedUiResource.SimpleSet(
            intDefault = 0
    )
    public int placeholder = 0;

    public InIRPactVersion() {
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public InIRPactVersion copy(CopyCache cache) {
        return cache.copyIfAbsent(this, this::newCopy);
    }

    public InIRPactVersion newCopy(CopyCache cache) {
        InIRPactVersion copy = new InIRPactVersion();
        copy.name = name;
        return copy;
    }
}
