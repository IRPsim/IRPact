package de.unileipzig.irpact.io.param.input.names;

import de.unileipzig.irpact.io.param.input.InRootUI;
import de.unileipzig.irptools.defstructure.annotation.Definition;
import de.unileipzig.irptools.defstructure.annotation.FieldDefinition;
import de.unileipzig.irptools.util.CopyCache;
import de.unileipzig.irptools.util.TreeAnnotationResource;

import java.lang.invoke.MethodHandles;

import static de.unileipzig.irpact.io.param.ParamUtil.addEntry;
import static de.unileipzig.irpact.io.param.ParamUtil.putClassPath;

/**
 * @author Daniel Abitz
 */
@Definition
public class InAttributeName implements InName {

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
        putClassPath(res, thisClass(), InRootUI.ATTRNAMES);
        addEntry(res, thisClass(), "placeholder");
    }

    public String _name;

    @FieldDefinition
    public int placeholder;

    public InAttributeName() {
    }

    public InAttributeName(String name) {
        this._name = name;
    }

    @Override
    public InAttributeName copy(CopyCache cache) {
        return cache.copyIfAbsent(this, this::newCopy);
    }

    public InAttributeName newCopy(CopyCache cache) {
        InAttributeName copy = new InAttributeName();
        copy._name = _name;
        return copy;
    }

    public String getName() {
        return _name;
    }
}
