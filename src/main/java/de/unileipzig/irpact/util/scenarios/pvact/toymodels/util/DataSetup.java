package de.unileipzig.irpact.util.scenarios.pvact.toymodels.util;

import de.unileipzig.irpact.commons.spatial.attribute.BasicSpatialDoubleAttribute;
import de.unileipzig.irpact.commons.spatial.attribute.SpatialAttribute;
import de.unileipzig.irpact.core.process.ra.RAConstants;
import de.unileipzig.irpact.core.spatial.SpatialUtil;
import de.unileipzig.irpact.util.scenarios.pvact.toymodels.AbstractToyModel;

import java.util.*;

/**
 * @author Daniel Abitz
 */
public class DataSetup {

    protected final Set<String> cagGroups = new LinkedHashSet<>();
    protected final Map<String, Integer> cagSizes = new LinkedHashMap<>();
    protected final Map<String, DataModifier> cagModifiers = new LinkedHashMap<>();
    protected DataModifier globalModifier;
    protected boolean original = false;

    public DataSetup() {
    }

    public boolean hasGlobalModifier() {
        return globalModifier != null;
    }

    public boolean isOriginal() {
        return original;
    }

    public void setOriginal(boolean original) {
        this.original = original;
    }

    public void setGlobalModifier(DataModifier globalModifier) {
        this.globalModifier = globalModifier;
    }

    public DataModifier getGlobalModifier() {
        return globalModifier;
    }

    public void addCagGroups(String... cagGroups) {
        Collections.addAll(this.cagGroups, cagGroups);
    }

    public void setSize(String cagGroup, int size) {
        cagGroups.add(cagGroup);
        cagSizes.put(cagGroup, size);
    }

    public void setModifier(String cagGroup, DataModifier modifier) {
        cagGroups.add(cagGroup);
        cagModifiers.put(cagGroup, modifier);
    }

    public void setSizeAndModifier(String cagGroup, int size, DataModifier modifier) {
        setSize(cagGroup, size);
        setModifier(cagGroup, modifier);
    }

    public Set<String> getCagGroups() {
        return cagGroups;
    }

    public int getTotalSize() {
        return cagSizes.values()
                .stream()
                .mapToInt(i -> i)
                .sum();
    }

    public int getSize(String cagGroup) {
        return cagSizes.getOrDefault(cagGroup, 0);
    }

    public boolean hasModifier(String cagGroup) {
        return cagModifiers.containsKey(cagGroup);
    }

    public DataModifier getModifier(String cagGroup) {
        return cagModifiers.get(cagGroup);
    }

    public static void addOriginalIdAttribute(List<SpatialAttribute> row) {
        int originalId = -1;
        for(SpatialAttribute spatialAttribute : row) {
            if(RAConstants.ID.equals(spatialAttribute.getName())) {
                originalId = spatialAttribute.asValueAttribute().getIntValue();
                break;
            }
        }

        BasicSpatialDoubleAttribute originalIdAttr = new BasicSpatialDoubleAttribute();
        originalIdAttr.setName(AbstractToyModel.ORIGINAL_ID);
        originalIdAttr.setIntValue(originalId);

        row.add(0, originalIdAttr);
    }

    public static void setCagGroup(List<SpatialAttribute> row, String cagGroupName) {
        SpatialUtil.replaceString(row, RAConstants.DOM_MILIEU, cagGroupName);
    }

    public static void setId(List<SpatialAttribute> row, int id) {
        SpatialUtil.replaceInt(row, RAConstants.ID, id);
    }

    public static void setPPEuro(List<SpatialAttribute> row, double value) {
        SpatialUtil.replaceDouble(row, RAConstants.PURCHASE_POWER_EUR, value);
    }
    public static void setA1(List<SpatialAttribute> row, double value) {
        setPPEuro(row, value);
    }

    public static void setXY(List<SpatialAttribute> row, double x, double y) {
        setX(row, x);
        setY(row, y);
    }

    public static void setX(List<SpatialAttribute> row, double x) {
        SpatialUtil.replaceDouble(row, RAConstants.X_CENT, x);
    }

    public static void setY(List<SpatialAttribute> row, double y) {
        SpatialUtil.replaceDouble(row, RAConstants.Y_CENT, y);
    }

    public static void setHouseOwner(List<SpatialAttribute> row) {
        setHouseOwner(row, 1);
    }
    public static void setHouseOwner(List<SpatialAttribute> row, double value) {
        SpatialUtil.replaceDouble(row, RAConstants.HOUSE_OWNER, value);
    }
    public static void setA6(List<SpatialAttribute> row, double value) {
        setHouseOwner(row, value);
    }

    public static void setNotHouseOwner(List<SpatialAttribute> row) {
        setHouseOwner(row, 0);
    }

    public static void setShare1Or2House(List<SpatialAttribute> row) {
        setShare1Or2House(row, 1);
    }
    public static void setShare1Or2House(List<SpatialAttribute> row, double value) {
        SpatialUtil.replaceDouble(row, RAConstants.SHARE_1_2_HOUSE, value);
    }
    public static void setA5(List<SpatialAttribute> row, double value) {
        setShare1Or2House(row, value);
    }

    public static void setNotShare1Or2House(List<SpatialAttribute> row) {
        setShare1Or2House(row, 0);
    }

    public static void setSlope(List<SpatialAttribute> row, double value) {
        SpatialUtil.replaceDouble(row, RAConstants.SLOPE, value);
    }

    public static void setOrientation(List<SpatialAttribute> row, double value) {
        SpatialUtil.replaceDouble(row, RAConstants.ORIENTATION, value);
    }
}
