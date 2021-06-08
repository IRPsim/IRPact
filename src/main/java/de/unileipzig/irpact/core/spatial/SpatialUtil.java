package de.unileipzig.irpact.core.spatial;

import de.unileipzig.irpact.commons.Nameable;
import de.unileipzig.irpact.commons.distribution.UnivariateDoubleDistribution;
import de.unileipzig.irpact.commons.spatial.attribute.SpatialAttribute;
import de.unileipzig.irpact.commons.spatial.attribute.SpatialDoubleAttribute;
import de.unileipzig.irpact.commons.util.ShareCalculator;
import de.unileipzig.irpact.commons.util.data.DataType;
import de.unileipzig.irpact.commons.util.data.LinkedDataCollection;
import de.unileipzig.irpact.commons.util.table.Table;
import de.unileipzig.irpact.core.logging.IRPLogging;
import de.unileipzig.irpact.core.spatial.data.BasicSpatialDataCollection;
import de.unileipzig.irpact.core.spatial.data.SpatialDataCollection;
import de.unileipzig.irpact.core.spatial.data.SpatialDataFilter;
import de.unileipzig.irpact.core.spatial.distribution2.SelectAndGroupFilter;
import de.unileipzig.irpact.core.spatial.distribution2.SelectFilter;
import de.unileipzig.irpact.core.spatial.twodim.BasicPoint2D;
import de.unileipzig.irptools.util.log.IRPLogger;

import java.util.*;
import java.util.function.Function;
import java.util.function.LongSupplier;
import java.util.function.Predicate;
import java.util.function.Supplier;
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

    private static List<String> collectKeys(List<SpatialAttribute> row) {
        return row.stream()
                .map(Nameable::getName)
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
        throw new NoSuchElementException("attribute '" + key + "' not found (" + collectKeys(row) + ")");
    }

    private static SpatialDoubleAttribute tryGet(List<SpatialAttribute> row, String key) {
        if(key == null) {
            return null;
        }
        for(SpatialAttribute attr: row) {
            if(Objects.equals(attr.getName(), key)) {
                if(attr.isValueAttributeWithDataType(DataType.DOUBLE)) {
                    return (SpatialDoubleAttribute) attr;
                } else {
                    return null;
                }
            }
        }
        return null;
    }

    @Deprecated
    public static List<SpatialInformation> mapToPoint2D(List<List<SpatialAttribute>> input, String xKey, String yKey, String idKey) {
        return input.stream()
                .map(row -> {
                    double x = secureGet(row, xKey).getDoubleValue();
                    double y = secureGet(row, yKey).getDoubleValue();
                    BasicPoint2D p = new BasicPoint2D(x, y);
                    p.addAllAttributes(row);
                    SpatialAttribute idAttr = tryGet(row, idKey);
                    if(idAttr != null) {
                        p.setId(idAttr.asValueAttribute().getIntValue());
                    }
                    return p;
                })
                .collect(Collectors.toList());
    }

    @Deprecated
    public static List<SpatialInformation> mapToPoint2D(List<List<SpatialAttribute>> input, UnivariateDoubleDistribution xSupplier, UnivariateDoubleDistribution ySupplier, String idKey) {
        return input.stream()
                .map(row -> {
                    double x = xSupplier.drawDoubleValue();
                    double y = ySupplier.drawDoubleValue();
                    BasicPoint2D p = new BasicPoint2D(x, y);
                    p.addAllAttributes(row);
                    SpatialAttribute idAttr = tryGet(row, idKey);
                    if(idAttr != null) {
                        p.setId(idAttr.asValueAttribute().getIntValue());
                    }
                    return p;
                })
                .collect(Collectors.toList());
    }

    public static SpatialDataCollection mapToPoint2D(
            String name,
            Table<SpatialAttribute> input,
            UnivariateDoubleDistribution xSupplier,
            UnivariateDoubleDistribution ySupplier,
            LongSupplier idSupplier,
            Supplier<? extends Collection<SpatialInformation>> supplier) {
        List<SpatialInformation> infos = input.listTable()
                .stream()
                .map(row -> {
                    double x = xSupplier.drawDoubleValue();
                    double y = ySupplier.drawDoubleValue();
                    long id = idSupplier.getAsLong();
                    BasicPoint2D p = new BasicPoint2D(id, x, y);
                    p.addAllAttributes(row);
                    return p;
                })
                .collect(Collectors.toList());
        return mapToPoint2D(name, infos, supplier);
    }

    public static SpatialDataCollection mapToPoint2D(
            String name,
            Table<SpatialAttribute> input,
            String xKey,
            String yKey,
            LongSupplier idSupplier,
            Supplier<? extends Collection<SpatialInformation>> supplier) {
        List<SpatialInformation> infos = input.listTable()
                .stream()
                .map(row -> {
                    double x = secureGet(row, xKey).getDoubleValue();
                    double y = secureGet(row, yKey).getDoubleValue();
                    long id = idSupplier.getAsLong();
                    BasicPoint2D p = new BasicPoint2D(id, x, y);
                    p.addAllAttributes(row);
                    return p;
                })
                .collect(Collectors.toList());
        return mapToPoint2D(name, infos, supplier);
    }

    protected static SpatialDataCollection mapToPoint2D(
            String name,
            List<SpatialInformation> infos,
            Supplier<? extends Collection<SpatialInformation>> supplier) {
        LinkedDataCollection<SpatialInformation> dataColl = new LinkedDataCollection<>(supplier);
        dataColl.addAll(infos);
        BasicSpatialDataCollection spatialData = new BasicSpatialDataCollection();
        spatialData.setName(name);
        spatialData.setData(dataColl);
        return spatialData;
    }

    public static Map<String, SpatialDataFilter> createFilters(
            SpatialDataCollection data,
            String selectKey) {
        List<String> distinctValues = data.getData()
                .stream()
                .map(info -> {
                    SpatialAttribute attr = info.getAttribute(selectKey);
                    if(attr == null) {
                        throw new NoSuchElementException("missing '" + selectKey + "'");
                    }
                    return attr.asValueAttribute().getValueAsString();
                })
                .distinct()
                .collect(Collectors.toList());
        Map<String, SpatialDataFilter> filters = new HashMap<>();
        for(String selectValue: distinctValues) {
            SelectFilter filter = new SelectFilter(selectKey, selectValue);
            filters.put(selectValue, filter);
        }
        return filters;
    }

    public static Map<String, SpatialDataFilter> createFilters(
            SpatialDataCollection data,
            String selectKey,
            String groupingKey) {
        Set<String> selectValues = new LinkedHashSet<>();
        Set<String> groupingValues = new LinkedHashSet<>();

        for(SpatialInformation info: data.getData()) {
            SpatialAttribute selAttr = info.getAttribute(selectKey);
            if(selAttr == null) {
                throw new NoSuchElementException("missing '" + selectKey + "'");
            }

            SpatialAttribute grpAttr = info.getAttribute(groupingKey);
            if(grpAttr == null) {
                throw new NoSuchElementException("missing '" + groupingKey + "'");
            }

            selectValues.add(selAttr.asValueAttribute().getValueAsString());
            groupingValues.add(grpAttr.asValueAttribute().getValueAsString());
        }

        Map<String, SpatialDataFilter> filters = new HashMap<>();
        for(String selectValue: selectValues) {
            for(String groupingValue: groupingValues) {
                SelectAndGroupFilter filter = new SelectAndGroupFilter(selectKey, selectValue, groupingKey, groupingValue);
                filter.buildName();
                filters.put(filter.getName(), filter);
            }
        }
        return filters;
    }

    public static Map<String, SpatialDataFilter> createFilters(
            SpatialDataCollection data,
            String selectKey,
            String selectValue,
            String groupingKey) {
        List<String> distinctValues = data.getData()
                .stream()
                .map(info -> {
                    SpatialAttribute attr = info.getAttribute(groupingKey);
                    if(attr == null) {
                        throw new NoSuchElementException("missing '" + groupingKey + "'");
                    }
                    return attr.asValueAttribute().getValueAsString();
                })
                .distinct()
                .collect(Collectors.toList());
        Map<String, SpatialDataFilter> filters = new HashMap<>();
        for(String groupingValue: distinctValues) {
            SelectAndGroupFilter filter = new SelectAndGroupFilter(selectKey, selectValue, groupingKey, groupingValue);
            filter.buildName();
            filters.put(groupingValue, filter);
        }
        return filters;
    }

    public static Map<String, SpatialDataFilter> createFilters(
            String selectKey, Collection<String> selectValues,
            String groupingKey, Collection<String> groupingValues) {
        Map<String, SpatialDataFilter> filters = new HashMap<>();
        for(String selectValue: selectValues) {
            for(String groupingValue: groupingValues) {
                SelectAndGroupFilter filter = new SelectAndGroupFilter(selectKey, selectValue, groupingKey, groupingValue);
                filter.buildName();
                if(filters.containsKey(filter.getName())) {
                    throw new IllegalArgumentException("filter '" + filter.getName() + "' already exists");
                }
                filters.put(filter.getName(), filter);
            }
        }
        return filters;
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

    public static int tryGet(SpatialInformation information, String attrName, int defaultValue) {
        if(information == null) {
            return defaultValue;
        }
        SpatialAttribute attr = information.getAttribute(attrName);
        if(attr == null || attr.isNoValueAttribute()) {
            return defaultValue;
        } else {
            if(attr.asValueAttribute().isDataType(DataType.DOUBLE)) {
                return attr.asValueAttribute().getIntValue();
            } else {
                return defaultValue;
            }
        }
    }

    public static long tryGetId(SpatialInformation information) {
        return information == null
                ? -1L
                : information.getId();
    }
}
