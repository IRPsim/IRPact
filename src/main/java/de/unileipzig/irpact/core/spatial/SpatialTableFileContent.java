package de.unileipzig.irpact.core.spatial;

import de.unileipzig.irpact.commons.spatial.attribute.SpatialAttribute;
import de.unileipzig.irpact.commons.util.data.FileContent;
import de.unileipzig.irpact.commons.util.table.Table;

import java.util.List;

/**
 * @author Daniel Abitz
 */
public class SpatialTableFileContent implements FileContent {

    protected Table<SpatialAttribute> data;

    public SpatialTableFileContent(Table<SpatialAttribute> data) {
        this.data = data;
    }

    @Override
    public Table<SpatialAttribute> content() {
        return data;
    }

    public List<SpatialAttribute> get(int index) {
        return data.listRow(index);
    }

    public int size() {
        return data.rowCount();
    }
}
