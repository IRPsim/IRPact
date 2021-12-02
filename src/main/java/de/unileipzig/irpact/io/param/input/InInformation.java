package de.unileipzig.irpact.io.param.input;

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
public class InInformation implements InIRPactEntity {

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
        putClassPath(res, thisClass(), TreeViewStructure.INFO_INFO);
        addEntry(res, thisClass(), "placeholder");
    }

    public String _name;

    @FieldDefinition
    public double placeholder;

    public InInformation() {
    }

    public InInformation(String text) {
        setName(text);
    }

    @Override
    public String getName() {
        return _name;
    }

    public void setName(String name) {
        this._name = name;
    }

    @Override
    public InInformation copy(CopyCache cache) {
        return cache.copyIfAbsent(this, this::newCopy);
    }

    public InInformation newCopy(CopyCache cache) {
        InInformation copy = new InInformation();
        copy._name = _name;
        copy.placeholder = placeholder;
        return copy;
    }
}
