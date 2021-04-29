package de.unileipzig.irpact.util.gis;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * @author Daniel Abitz
 */
public class OgrHelper {

    protected ShapeFile shapeFile;
    protected String bldgPart;
    protected List<String> elements;

    public OgrHelper(ShapeFile shapeFile, String bldgPart, Collection<String> elements) {
        this.shapeFile = shapeFile;
        this.bldgPart = bldgPart;
        this.elements = new ArrayList<>(elements);
    }

    @Override
    public String toString() {
        return "OgrHelper{" +
                "shapeFile=" + shapeFile.getFileWithoutExtension() +
                ", bldgPart='" + bldgPart + '\'' +
                ", elements=" + elements +
                '}';
    }

    public static String buildSpecialQueryFor(Collection<? extends OgrHelper> coll) {
        StringBuilder sb = new StringBuilder();
        sb.append("\"");
        sb.append("(Element = 'Roof')");
        for(OgrHelper ogrHelper: coll) {
            sb.append(" OR (");
            sb.append("(Element = '").append(ogrHelper.elements.get(0)).append("')");
            sb.append(" AND ");
            sb.append("(BldgPart = '").append(ogrHelper.bldgPart).append("')");
            sb.append(")");
        }
        sb.append("\"");
        return sb.toString();
    }
}
