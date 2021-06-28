package de.unileipzig.irpact.io.param.input;

import de.unileipzig.irptools.defstructure.annotation.Definition;
import de.unileipzig.irptools.defstructure.annotation.FieldDefinition;
import de.unileipzig.irptools.util.CopyCache;
import de.unileipzig.irptools.util.TreeAnnotationResource;

import java.lang.invoke.MethodHandles;

import static de.unileipzig.irpact.io.param.IOConstants.*;
import static de.unileipzig.irpact.io.param.ParamUtil.*;

/**
 * @author Daniel Abitz
 */
@Definition
public class InIRPactVersionPlaceholder implements InIRPactEntity {

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
        putClassPath(res, thisClass(), INFORMATIONS, ABOUT_IRPACT);
        addEntry(res, thisClass(), "placeholder");
    }

    public String _name;

    @FieldDefinition
    public int placeholder;

    public InIRPactVersionPlaceholder() {
    }

    @Override
    public String getName() {
        return _name;
    }

    @Override
    public InIRPactVersionPlaceholder copy(CopyCache cache) {
        return cache.copyIfAbsent(this, this::newCopy);
    }

    public InIRPactVersionPlaceholder newCopy(CopyCache cache) {
        InIRPactVersionPlaceholder copy = new InIRPactVersionPlaceholder();
        copy._name = _name;
        return copy;
    }
}
