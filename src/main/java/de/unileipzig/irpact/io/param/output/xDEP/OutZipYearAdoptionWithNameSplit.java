package de.unileipzig.irpact.io.param.output.xDEP;

import de.unileipzig.irpact.commons.util.data.VarCollection;
import de.unileipzig.irptools.defstructure.annotation.Definition;
import de.unileipzig.irptools.defstructure.annotation.FieldDefinition;
import de.unileipzig.irptools.util.TreeAnnotationResource;

import java.lang.invoke.MethodHandles;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Daniel Abitz
 */
@Deprecated
@Definition
public class OutZipYearAdoptionWithNameSplit {

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

    public OutZipYearAdoptionWithNameSplit() {
    }

    public void setName(String name) {
        this._name = name;
    }

    public String getName() {
        return _name;
    }

    public void buildName(String zip, int year) {
        setName("z" + zip + "_y" + year);;
    }

    public void setAdoptions(int adoptions) {
        this.adoptions = adoptions;
    }

    public int getAdoptions() {
        return adoptions;
    }

    public static List<OutZipYearAdoptionWithNameSplit> apply(VarCollection data) {
        List<OutZipYearAdoptionWithNameSplit> result = new ArrayList<>();
        for(Object[] entry: data.iterable()) {
            Integer year = (Integer) entry[0];
            String zip = (String) entry[1];
            Integer adopts = (Integer) entry[2];

            OutZipYearAdoptionWithNameSplit adop = new OutZipYearAdoptionWithNameSplit();
            adop.buildName(zip, year);
            adop.setAdoptions(adopts);
            result.add(adop);
        }
        return result;
    }
}
