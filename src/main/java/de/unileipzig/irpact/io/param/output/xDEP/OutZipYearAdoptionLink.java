package de.unileipzig.irpact.io.param.output.xDEP;

import de.unileipzig.irpact.commons.util.data.VarCollection;
import de.unileipzig.irptools.defstructure.annotation.Definition;
import de.unileipzig.irptools.defstructure.annotation.FieldDefinition;
import de.unileipzig.irptools.util.TreeAnnotationResource;

import java.lang.invoke.MethodHandles;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author Daniel Abitz
 */
@Deprecated
@Definition
public class OutZipYearAdoptionLink {

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
    public OutYear[] years;

    @FieldDefinition
    public OutZip[] zips;

    @FieldDefinition
    public OutZipYearAdoption[] adoptions;

    public OutZipYearAdoptionLink() {
    }

    public OutZipYearAdoptionLink(String name) {
        setName(name);
    }

    public void setName(String name) {
        this._name = name;
    }

    public String getName() {
        return _name;
    }

    public void link(OutYear year, OutZip zip, OutZipYearAdoption... data) {
        this.years = new OutYear[]{year};
        this.zips = new OutZip[]{zip};
        this.adoptions = data;
    }

    public void link(OutYear[] years, OutZip[] zips, OutZipYearAdoption... data) {
        this.years = years;
        this.zips = zips;
        this.adoptions = data;
    }

    public static List<OutZipYearAdoptionLink> createLinks(Map<Object, Object> cache, VarCollection data) {
        List<OutZipYearAdoptionLink> result = new ArrayList<>();
        for(Object[] entry: data.iterable()) {
            Integer year = (Integer) entry[0];
            String zip = (String) entry[1];
            Integer adopts = (Integer) entry[2];

            OutYear y = (OutYear) cache.computeIfAbsent(year, _year -> new OutYear((Integer) _year));
            OutZip z = (OutZip) cache.computeIfAbsent(zip, _zip -> new OutZip((String) _zip));
            OutZipYearAdoption zya = (OutZipYearAdoption) cache.computeIfAbsent(adopts, _adopts -> new OutZipYearAdoption((Integer) _adopts));
            OutZipYearAdoptionLink link = new OutZipYearAdoptionLink("l_y" + year + "_z" + zip);
            link.link(y, z, zya);
            result.add(link);
        }
        return result;
    }
}
