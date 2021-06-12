package de.unileipzig.irpact.core.spatial;

import de.unileipzig.irpact.commons.NameableBase;
import de.unileipzig.irpact.commons.spatial.attribute.SpatialAttribute;
import de.unileipzig.irpact.commons.util.data.FileContent;
import de.unileipzig.irpact.commons.util.table.Table;

import java.util.List;

/**
 * @author Daniel Abitz
 */
public class SpatialTableFileContent extends NameableBase implements FileContent {

    protected Table<SpatialAttribute> tableData;

    public SpatialTableFileContent(String name, Table<SpatialAttribute> tableData) {
        setName(name);
        this.tableData = tableData;
    }

    @Override
    public Table<SpatialAttribute> content() {
        return tableData;
    }

    public List<SpatialAttribute> get(int index) {
        return tableData.listRow(index);
    }

    public int size() {
        return tableData.rowCount();
    }
}
