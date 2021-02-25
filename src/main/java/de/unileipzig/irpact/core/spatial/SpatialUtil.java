package de.unileipzig.irpact.core.spatial;

import de.unileipzig.irpact.commons.distribution.UnivariateDoubleDistribution;
import de.unileipzig.irpact.commons.util.DataType;
import de.unileipzig.irpact.core.spatial.attribute.SpatialAttribute;
import de.unileipzig.irpact.core.spatial.attribute.SpatialStringAttribute;
import de.unileipzig.irpact.core.spatial.twodim.BasicPoint2D;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author Daniel Abitz
 */
public final class SpatialUtil {

    public static List<List<SpatialAttribute<?>>> filter(List<List<SpatialAttribute<?>>> input, String attrName, String value) {
        return input.stream()
                .filter(row -> {
                    for(SpatialAttribute<?> attr: row) {
                        if(Objects.equals(attr.getName(), attrName)) {
                            return Objects.equals(attr.getValue(), value);
                        }
                    }
                    return false;
                })
                .collect(Collectors.toList());
    }

    public static List<SpatialInformation> mapToPoint2D(List<List<SpatialAttribute<?>>> input, UnivariateDoubleDistribution xSupplier, UnivariateDoubleDistribution ySupplier) {
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
                    SpatialAttribute<?> attr = info.getAttribute(attrName);
                    if(attr == null) {
                        throw new IllegalArgumentException("missing '" + attrName + "'");
                    }
                    return attr.getValueAsString();
                }));
    }
}
