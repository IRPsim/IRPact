package de.unileipzig.irpact.io.param.output.outtest;

import de.unileipzig.irptools.defstructure.annotation.Definition;
import de.unileipzig.irptools.defstructure.annotation.FieldDefinition;
import de.unileipzig.irptools.util.TreeAnnotationResource;

import java.lang.invoke.MethodHandles;

/**
 * @author Daniel Abitz
 */
@Definition
public class OutZip {

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
    }

    public String _name;

//    @FieldDefinition
//    public double placeholder;

    public OutZip() {
    }

    public OutZip(String name) {
        setName(name);
    }

    public void setName(String name) {
        this._name = "z" + name;
    }

    public String getName() {
        return _name;
    }
}
