package de.unileipzig.irpact.core.spatial.distribution2;

import de.unileipzig.irpact.commons.NameableBase;
import de.unileipzig.irpact.commons.spatial.attribute.SpatialAttribute;
import de.unileipzig.irpact.commons.util.StringUtil;
import de.unileipzig.irpact.core.spatial.data.SpatialDataFilter;
import de.unileipzig.irpact.core.spatial.SpatialInformation;

import java.util.Objects;

/**
 * @author Daniel Abitz
 */
public class SelectFilter extends NameableBase implements SpatialDataFilter {

    protected String selectKey;
    protected String selectValue;

    public SelectFilter(String selectKey, String selectValue) {
        this.selectKey = selectKey;
        this.selectValue = selectValue;
        setName(StringUtil.concat("_", selectKey, selectValue));
    }

    public String getSelectKey() {
        return selectKey;
    }

    public String getSelectValue() {
        return selectValue;
    }

    @Override
    public boolean test(SpatialInformation info) {
        SpatialAttribute selectAttr = info.getAttribute(selectKey);
        return selectAttr != null
                && selectAttr.isValueAttribute()
                && Objects.equals(selectValue, selectAttr.asValueAttribute().getValueAsString());
    }
}
