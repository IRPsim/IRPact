package de.unileipzig.irpact.core.spatial;

import de.unileipzig.irpact.commons.Nameable;
import de.unileipzig.irpact.commons.distribution.UnivariateDoubleDistribution;
import de.unileipzig.irpact.commons.spatial.attribute.SpatialAttribute;
import de.unileipzig.irpact.commons.spatial.attribute.SpatialDoubleAttribute;
import de.unileipzig.irpact.commons.spatial.attribute.SpatialStringAttribute;
import de.unileipzig.irpact.commons.util.data.DataType;
import de.unileipzig.irpact.commons.util.data.LinkedDataCollection;
import de.unileipzig.irpact.commons.util.table.Table;
import de.unileipzig.irpact.core.spatial.data.BasicSpatialDataCollection;
import de.unileipzig.irpact.core.spatial.data.SpatialDataCollection;
import de.unileipzig.irpact.core.spatial.data.SpatialDataFilter;
import de.unileipzig.irpact.core.spatial.distribution.SelectAndGroupFilter;
import de.unileipzig.irpact.core.spatial.distribution.SelectFilter;
import de.unileipzig.irpact.core.spatial.twodim.BasicPoint2D;

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

    public static SpatialDoubleAttribute secureGet(List<SpatialAttribute> row, String key) {
        if(key == null) {
            throw new NullPointerException("key is null");
        }

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

    public static SpatialStringAttribute secureGetString(List<SpatialAttribute> row, String key) {
        if(key == null) {
            throw new NullPointerException("key is null");
        }

        for(SpatialAttribute attr: row) {
            if(Objects.equals(attr.getName(), key)) {
                if(!attr.isValueAttributeWithDataType(DataType.STRING)) {
                    throw new IllegalArgumentException("attribute '" + key + "' is no double");
                }
                return (SpatialStringAttribute) attr;
            }
        }

        throw new NoSuchElementException("attribute '" + key + "' not found (" + collectKeys(row) + ")");
    }

    public static SpatialDoubleAttribute getOr(List<SpatialAttribute> row, String key, SpatialDoubleAttribute ifMissing) {
        if(key == null) {
            return ifMissing;
        }
        for(SpatialAttribute attr: row) {
            if(Objects.equals(attr.getName(), key)) {
                if(attr.isValueAttributeWithDataType(DataType.DOUBLE)) {
                    return (SpatialDoubleAttribute) attr;
                } else {
                    return ifMissing;
                }
            }
        }
        return ifMissing;
    }

    public static SpatialStringAttribute getStringOr(List<SpatialAttribute> row, String key, SpatialStringAttribute ifMissing) {
        if(key == null) {
            return ifMissing;
        }
        for(SpatialAttribute attr: row) {
            if(Objects.equals(attr.getName(), key)) {
                if(attr.isValueAttributeWithDataType(DataType.STRING)) {
                    return (SpatialStringAttribute) attr;
                } else {
                    return ifMissing;
                }
            }
        }
        return ifMissing;
    }

    //=========================
    //REMOVE
    //=========================

    @Deprecated
    public static List<SpatialInformation> mapToPoint2D(List<List<SpatialAttribute>> input, String xKey, String yKey, String idKey) {
        return input.stream()
                .map(row -> {
                    double x = secureGet(row, xKey).getDoubleValue();
                    double y = secureGet(row, yKey).getDoubleValue();
                    BasicPoint2D p = new BasicPoint2D(x, y);
                    p.addAllAttributes(row);
                    SpatialAttribute idAttr = getOr(row, idKey, null);
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
                    SpatialAttribute idAttr = getOr(row, idKey, null);
                    if(idAttr != null) {
                        p.setId(idAttr.asValueAttribute().getIntValue());
                    }
                    return p;
                })
                .collect(Collectors.toList());
    }

    @Deprecated
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

    @Deprecated
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

    @Deprecated
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

    @Deprecated
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

    @Deprecated
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

    @Deprecated
    public static long tryGetId(SpatialInformation information) {
        return information == null
                ? -1L
                : information.getId();
    }

    //=========================
    //v2
    //=========================

    public static Set<String> collectAll(Table<SpatialAttribute> input, String key) {
        return input.listTable()
                .stream()
                .map(row -> {
                    SpatialStringAttribute attribute = getStringOr(row, key, null);
                    return attribute == null ? null : attribute.getStringValue();
                })
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());
    }

    public static SpatialDataCollection mapToPoint2DIfAbsent_2(
            String name,
            SpatialModel model,
            Table<SpatialAttribute> input,
            String xKey,
            String yKey,
            String idKey) {
        if(model.hasData(name)) {
            return model.getData(name);
        } else {
            SpatialDataCollection data = mapToPoint2D_2(name, input, xKey, yKey, idKey);
            model.storeData(data);
            return data;
        }
    }

    public static SpatialDataCollection mapToPoint2D_2(
            String name,
            Table<SpatialAttribute> input,
            String xKey,
            String yKey,
            String idKey) {
        LinkedDataCollection<SpatialInformation> dataColl = new LinkedDataCollection<>(ArrayList::new);

        input.listTable()
                .stream()
                .map(row -> {
                    double x = secureGet(row, xKey).getDoubleValue();
                    double y = secureGet(row, yKey).getDoubleValue();
                    BasicPoint2D p = new BasicPoint2D(x, y);
                    SpatialDoubleAttribute idAttr = getOr(row, idKey, null);
                    if(idAttr != null) {
                        long id = idAttr.getLongValue();
                        p.setId(id);
                    }
                    p.addAllAttributes(row);
                    return p;
                })
                .forEach(dataColl::add);

        BasicSpatialDataCollection spatialData = new BasicSpatialDataCollection();
        spatialData.setName(name);
        spatialData.setData(dataColl);

        return spatialData;
    }

    public static List<SpatialDataFilter> createFilters_2(
            SpatialDataCollection data,
            String selectKey,
            String selectValue,
            String groupingKey) {
        Set<String> groupingValues = new LinkedHashSet<>();

        data.getData()
                .stream()
                .map(info -> {
                    SpatialAttribute attr = info.getAttribute(groupingKey);
                    if(attr == null) {
                        throw new NoSuchElementException("missing '" + groupingKey + "'");
                    }
                    return attr.asValueAttribute().getValueAsString();
                })
                .forEach(groupingValues::add);

        return createFilters_2(
                selectKey, Collections.singleton(selectValue),
                groupingKey, groupingValues
        );
    }

    public static List<SpatialDataFilter> createFilters_2(
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

        return createFilters_2(selectKey, selectValues, groupingKey, groupingValues);
    }

    public static List<SpatialDataFilter> createFilters_2(
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

        return new ArrayList<>(filters.values());
    }

    public static List<SpatialDataFilter> createFilters_2(
            String selectKey,
            String selectValue) {
        return createFilters_2(selectKey, Collections.singleton(selectValue));
    }

    public static List<SpatialDataFilter> createFilters_2(
            String selectKey,
            Collection<String> selectValues) {
        Map<String, SpatialDataFilter> filters = new HashMap<>();

        for(String selectValue: selectValues) {
            SelectFilter filter = new SelectFilter(selectKey, selectValue);
            filter.buildName();
            if(filters.containsKey(filter.getName())) {
                throw new IllegalArgumentException("filter '" + filter.getName() + "' already exists");
            }
            filters.put(filter.getName(), filter);
        }

        return new ArrayList<>(filters.values());
    }

    public static int sum(Map<?, Integer> map) {
        return map.values().stream().mapToInt(i -> i).sum();
    }

    public static String findLargest(Map<String, Integer> map) {
        if(map.isEmpty()) {
            throw new IllegalArgumentException("empty map");
        }

        String largest = null;
        int largestSize = Integer.MIN_VALUE;
        for(Map.Entry<String, Integer> entry: map.entrySet()) {
            if(largestSize < entry.getValue()) {
                largest = entry.getKey();
                largestSize = entry.getValue();
            }
        }
        return largest;
    }

    public static Map<String, Integer> calculateSizes(
            Table<SpatialAttribute> data,
            String selectKey,
            Collection<? extends String> validKeys,
            int desiredTotalSize,
            boolean requiresDesiredTotalSize) {

        Map<String, Integer> initialSizes = new HashMap<>();
        data.listTable().forEach(row -> {
                    String selectValue = secureGetString(row, selectKey).getStringValue();
                    Integer current = initialSizes.computeIfAbsent(selectValue, _secectValue -> 0);
                    initialSizes.put(selectValue, current + 1);
                });

        Map<String, Integer> validSizes = new HashMap<>(initialSizes);
        if(validKeys != null) {
            validSizes.keySet().retainAll(validKeys);
        }
        final int sumValid = sum(validSizes);

        if(desiredTotalSize > sumValid && requiresDesiredTotalSize) {
            throw new IllegalArgumentException("not enoug data: " + sumValid + " < " + desiredTotalSize);
        }

        if(desiredTotalSize < 0 || desiredTotalSize >= sumValid) {
            return validSizes;
        }

        validSizes.replaceAll((_key, _size) -> {
            double share = (double) _size / (double) sumValid;
            return (int) (share * desiredTotalSize);
        });

        if(sum(validSizes) < desiredTotalSize) {
            int delta = desiredTotalSize - sum(validSizes);
            String largestKey = findLargest(validSizes);
            validSizes.put(largestKey, validSizes.get(largestKey) + delta);
        }

        return validSizes;
    }
}
