package de.unileipzig.irpact.io.param.input;

import de.unileipzig.irpact.io.param.LocalizedUiResource;
import de.unileipzig.irptools.defstructure.annotation.Definition;
import de.unileipzig.irptools.defstructure.annotation.DefinitionName;
import de.unileipzig.irptools.defstructure.annotation.FieldDefinition;
import de.unileipzig.irptools.util.CopyCache;
import de.unileipzig.irptools.util.TreeAnnotationResource;

import java.lang.invoke.MethodHandles;

import static de.unileipzig.irpact.io.param.input.TreeViewStructureEnum.INFO_INFO;

/**
 * @author Daniel Abitz
 */
@Definition
@LocalizedUiResource.PutClassPath(INFO_INFO)
public class InInformation implements InIRPactEntity {

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
            decDefault = 0
    )
    public double placeholder = 0;

    public InInformation() {
    }

    public InInformation(String text) {
        setName(text);
    }

    @Override
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public InInformation copy(CopyCache cache) {
        return cache.copyIfAbsent(this, this::newCopy);
    }

    public InInformation newCopy(CopyCache cache) {
        InInformation copy = new InInformation();
        copy.name = name;
        copy.placeholder = placeholder;
        return copy;
    }
}
