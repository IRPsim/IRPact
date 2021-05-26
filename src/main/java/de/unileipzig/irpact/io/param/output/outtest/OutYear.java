package de.unileipzig.irpact.io.param.output.outtest;

import de.unileipzig.irptools.defstructure.annotation.Definition;
import de.unileipzig.irptools.defstructure.annotation.FieldDefinition;
import de.unileipzig.irptools.util.TreeAnnotationResource;

import java.lang.invoke.MethodHandles;

/**
 * @author Daniel Abitz
 */
@Definition
public class OutYear {

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

    @FieldDefinition
    public int year;

    public OutYear() {
    }

    public OutYear(int year) {
        setName(year);
        this.year = year;
    }

    public void setName(String name) {
        this._name = "y" + name;
    }

    public void setName(int year) {
        setName(Integer.toString(year));
    }

    public String getName() {
        return _name;
    }
}
