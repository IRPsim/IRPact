package de.unileipzig.irpact.core.spatial;

import de.unileipzig.irpact.commons.distribution.UnivariateDoubleDistribution;
import de.unileipzig.irpact.commons.spatial.attribute.SpatialAttribute;
import de.unileipzig.irpact.commons.spatial.attribute.SpatialDoubleAttribute;
import de.unileipzig.irpact.commons.util.ShareCalculator;
import de.unileipzig.irpact.commons.util.data.DataType;
import de.unileipzig.irpact.commons.util.data.LinkedDataCollection;
import de.unileipzig.irpact.commons.util.table.Table;
import de.unileipzig.irpact.core.log.IRPLogging;
import de.unileipzig.irpact.core.spatial.twodim.BasicPoint2D;
import de.unileipzig.irptools.util.log.IRPLogger;

import java.util.*;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * @author Daniel Abitz
 */
public final class SpatialUtil {

    private static final IRPLogger LOGGER = IRPLogging.getLogger(SpatialUtil.class);

    public static Predicate<List<SpatialAttribute>> filterAttribute(String attrName, String value) {
        return row -> {
            for(SpatialAttribute attr: row) {
                if(attr.isValueAttribute() && Objects.equals(attr.getName(), attrName)) {
                    return Objects.equals(attr.asValueAttribute().getValue(), value);
                }
            }
            return false;
        };
    }

    public static Function<? super List<SpatialAttribute>, ? extends String> selectAttribute(String attrName) {
        return row -> {
            for(SpatialAttribute attr: row) {
                if(attr.isValueAttribute() && Objects.equals(attr.getName(), attrName)) {
                    return attr.asValueAttribute().getStringValue();
                }
            }
            return null;
        };
    }

    public static Set<String> collectDistinct(List<List<SpatialAttribute>> input, String attrName) {
        return input.stream()
                .map(selectAttribute(attrName))
                .collect(Collectors.toSet());
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
                if(!attr.isValueAttributeWithDataType(DataType.DOUBLE)) {
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

    public static SpatialDataCollection mapToPoint2D(
            String name,
            Table<SpatialAttribute> input,
            UnivariateDoubleDistribution xSupplier,
            UnivariateDoubleDistribution ySupplier) {
        List<SpatialInformation> infos = input.listTable()
                .stream()
                .map(row -> {
                    double x = xSupplier.drawDoubleValue();
                    double y = ySupplier.drawDoubleValue();
                    BasicPoint2D p = new BasicPoint2D(x, y);
                    p.addAllAttributes(row);
                    return p;
                })
                .collect(Collectors.toList());
        return mapToPoint2D(name, infos);
    }

    public static SpatialDataCollection mapToPoint2D(
            String name,
            Table<SpatialAttribute> input,
            String xKey,
            String yKey) {
        List<SpatialInformation> infos = input.listTable()
                .stream()
                .map(row -> {
                    double x = secureGet(row, xKey).getDoubleValue();
                    double y = secureGet(row, yKey).getDoubleValue();
                    BasicPoint2D p = new BasicPoint2D(x, y);
                    p.addAllAttributes(row);
                    return p;
                })
                .collect(Collectors.toList());
        return mapToPoint2D(name, infos);
    }

    protected static SpatialDataCollection mapToPoint2D(
            String name,
            List<SpatialInformation> infos) {
        LinkedDataCollection<SpatialInformation> dataColl = new LinkedDataCollection<>(ArrayList::new);
        dataColl.addAll(infos);
        BasicSpatialDataCollection spatialData = new BasicSpatialDataCollection();
        spatialData.setName(name);
        spatialData.setData(dataColl);
        return spatialData;
    }

    public static Map<String, List<SpatialInformation>> groupingBy(Collection<SpatialInformation> input, String attrName) {
        return input.stream()
                .collect(Collectors.groupingBy(info -> {
                    SpatialAttribute attr = info.getAttribute(attrName);
                    if(attr == null) {
                        throw new IllegalArgumentException("missing '" + attrName + "'");
                    }
                    return attr.asValueAttribute().getValueAsString();
                }));
    }

    public static SpatialAttribute get(Collection<? extends SpatialAttribute> coll, String name) {
        for(SpatialAttribute attr: coll) {
            if(Objects.equals(attr.getName(), name)) {
                return attr;
            }
        }
        return null;
    }

    public static <T> void filterAndCount(
            List<List<SpatialAttribute>> input,
            String attrName,
            ShareCalculator<T> share,
            Function<? super String, ? extends T> mapper) {
        input.forEach(list -> {
            SpatialAttribute attr = get(list, attrName);
            if(attr == null) throw new NullPointerException(attrName);
            String value = attr.asValueAttribute().getValueAsString();
            T t = mapper.apply(value);
            share.updateSize(t, 1);
        });
    }

    public static Map<String, Integer> filterAndCountAll(List<List<SpatialAttribute>> input, String attrName, Collection<String> keys) {
        Map<String, Integer> map = new HashMap<>();
        input.stream()
                .map(selectAttribute(attrName))
                .forEach(k -> {
                    if(keys == null || keys.contains(k)) {
                        int current = map.computeIfAbsent(k, _k -> 0);
                        map.put(k, current + 1);
                    }
                });
        return map;
    }

    public static void xxx(
            Table<SpatialAttribute> spatialData) {

    }
}
