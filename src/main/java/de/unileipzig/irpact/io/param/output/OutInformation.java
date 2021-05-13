package de.unileipzig.irpact.io.param.output;

import de.unileipzig.irptools.defstructure.annotation.FieldDefinition;
import de.unileipzig.irptools.util.CopyCache;
import de.unileipzig.irptools.util.Copyable;
import de.unileipzig.irptools.util.TreeAnnotationResource;

import java.lang.invoke.MethodHandles;

import static de.unileipzig.irpact.io.param.ParamUtil.addEntry;

/**
 * @author Daniel Abitz
 */
public class OutInformation implements OutEntity {

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
        addEntry(res, thisClass());
        addEntry(res, thisClass(), "placeholder");
    }

    public String _name;

    @FieldDefinition
    public double placeholder;

    public OutInformation() {
    }

    public OutInformation(String text) {
        setText(text);
    }

    @Override
    public OutInformation copy(CopyCache cache) {
        return cache.copyIfAbsent(this, this::newCopy);
    }

    public OutInformation newCopy(CopyCache cache) {
        OutInformation copy = new OutInformation();
        copy._name = _name;
        return copy;
    }

    @Override
    public String getName() {
        return getText();
    }

    public void setText(String text) {
        this._name = text;
    }

    public String getText() {
        return _name;
    }
}
