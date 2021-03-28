package de.unileipzig.irpact.core.spatial;

import de.unileipzig.irpact.commons.distribution.UnivariateDoubleDistribution;
import de.unileipzig.irpact.commons.util.data.DataType;
import de.unileipzig.irpact.commons.spatial.attribute.SpatialAttribute;
import de.unileipzig.irpact.commons.spatial.attribute.SpatialDoubleAttribute;
import de.unileipzig.irpact.core.spatial.twodim.BasicPoint2D;

import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * @author Daniel Abitz
 */
public final class SpatialUtil {

    protected static Predicate<List<SpatialAttribute>> filterAttribute(String attrName, String value) {
        return row -> {
            for(SpatialAttribute attr: row) {
                if(Objects.equals(attr.getName(), attrName)) {
                    return Objects.equals(attr.getValue(), value);
                }
            }
            return false;
        };
    }

    public static int filterAndCount(List<List<SpatialAttribute>> input, String attrName, String value) {
        return (int) input.stream()
                .filter(filterAttribute(attrName, value))
                .count();
    }

    public static List<List<SpatialAttribute>> filter(List<List<SpatialAttribute>> input, String attrName, String value) {
        return input.stream()
                .filter(filterAttribute(attrName, value))
                .collect(Collectors.toList());
    }

    private static SpatialDoubleAttribute secureGet(List<SpatialAttribute> row, String key) {
        for(SpatialAttribute attr: row) {
            if(Objects.equals(attr.getName(), key)) {
                if(attr.getType() != DataType.DOUBLE) {
                    throw new IllegalArgumentException("attribute '" + key + "' is no double");
                }
                return (SpatialDoubleAttribute) attr;
            }
        }
        throw new NoSuchElementException("attribute '" + key + "' not found");
    }

    public static List<SpatialInformation> mapToPoint2D(List<List<SpatialAttribute>> input, String xKey, String yKey) {
        return input.stream()
                .map(row -> {
                    double x = secureGet(row, xKey).getDoubleValue();
                    double y = secureGet(row, yKey).getDoubleValue();
                    BasicPoint2D p = new BasicPoint2D(x, y);
                    p.addAllAttributes(row);
                    return p;
                })
                .collect(Collectors.toList());
    }

    public static List<SpatialInformation> mapToPoint2D(List<List<SpatialAttribute>> input, UnivariateDoubleDistribution xSupplier, UnivariateDoubleDistribution ySupplier) {
        return input.stream()
                .map(row -> {
                    double x = xSupplier.drawDoubleValue();
                    double y = ySupplier.drawDoubleValue();
                    BasicPoint2D p = new BasicPoint2D(x, y);
                    p.addAllAttributes(row);
                    return p;
                })
                .collect(Collectors.toList());
    }

    public static Map<String, List<SpatialInformation>> groupingBy(Collection<SpatialInformation> input, String attrName) {
        return input.stream()
                .collect(Collectors.groupingBy(info -> {
                    SpatialAttribute attr = info.getAttribute(attrName);
                    if(attr == null) {
                        throw new IllegalArgumentException("missing '" + attrName + "'");
                    }
                    return attr.getValueAsString();
                }));
    }
}
