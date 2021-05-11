package de.unileipzig.irpact.core.spatial.distribution2;

import de.unileipzig.irpact.commons.NameableBase;
import de.unileipzig.irpact.commons.spatial.attribute.SpatialAttribute;
import de.unileipzig.irpact.commons.util.StringUtil;
import de.unileipzig.irpact.core.spatial.SpatialInformation;

import java.util.Objects;

/**
 * @author Daniel Abitz
 */
public class SelectAndGroupFilter extends NameableBase implements SpatialDataFilter {

    protected String selectKey;
    protected String selectValue;
    protected String groupingKey;
    protected String groupingValue;

    public SelectAndGroupFilter(
            String selectKey, String selectValue,
            String groupingKey, String groupingValue) {
        this.selectKey = selectKey;
        this.selectValue = selectValue;
        this.groupingKey = groupingKey;
        this.groupingValue = groupingValue;
        setName(StringUtil.concat("_", selectKey, selectValue, groupingKey, groupingValue));
    }

    public String getSelectKey() {
        return selectKey;
    }

    public String getSelectValue() {
        return selectValue;
    }

    public String getGroupingKey() {
        return groupingKey;
    }

    public String getGroupingValue() {
        return groupingValue;
    }

    @Override
    public boolean test(SpatialInformation info) {
        SpatialAttribute selectAttr = info.getAttribute(selectKey);
        if(selectAttr == null
                || selectAttr.isNoValueAttribute()
                || !Objects.equals(selectValue, selectAttr.asValueAttribute().getValueAsString())) {
            return false;
        }

        SpatialAttribute groupAttr = info.getAttribute(groupingKey);
        return groupAttr != null
                && groupAttr.isValueAttribute()
                && Objects.equals(groupingValue, groupAttr.asValueAttribute().getValueAsString());
    }
}
