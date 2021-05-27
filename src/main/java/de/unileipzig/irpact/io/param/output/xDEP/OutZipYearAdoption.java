package de.unileipzig.irpact.io.param.output.xDEP;

import de.unileipzig.irptools.defstructure.annotation.Definition;
import de.unileipzig.irptools.defstructure.annotation.FieldDefinition;
import de.unileipzig.irptools.util.TreeAnnotationResource;

import java.lang.invoke.MethodHandles;

/**
 * @author Daniel Abitz
 */
@Deprecated
@Definition
public class OutZipYearAdoption {

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
    public int adoptions;

    public OutZipYearAdoption() {
    }

    public OutZipYearAdoption(int adoptions) {
        setName("a" + adoptions);
        setAdoptions(adoptions);
    }

    public void setName(String name) {
        this._name = name;
    }

    public String getName() {
        return _name;
    }

    public void setAdoptions(int adoptions) {
        this.adoptions = adoptions;
    }

    public int getAdoptions() {
        return adoptions;
    }
}
