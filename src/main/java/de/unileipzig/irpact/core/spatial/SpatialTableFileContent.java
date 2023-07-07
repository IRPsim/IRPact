package de.unileipzig.irpact.core.spatial;

import de.unileipzig.irpact.commons.NameableBase;
import de.unileipzig.irpact.commons.spatial.attribute.SpatialAttribute;
import de.unileipzig.irpact.commons.util.io.FileContent;
import de.unileipzig.irpact.commons.util.table.Table;

import java.util.List;

/**
 * @author Daniel Abitz
 */
public class SpatialTableFileContent extends NameableBase implements FileContent {

    protected Table<SpatialAttribute> tableData;
    protected double coverage;

    public SpatialTableFileContent(String name, Table<SpatialAttribute> tableData, double coverage) {
        setName(name);
        this.tableData = tableData;
        this.coverage = coverage;
    }

    @Override
    public Table<SpatialAttribute> content() {
        return getTableData();
    }

    public Table<SpatialAttribute> getTableData() {
        return tableData;
    }

    public boolean hasCoverage() {
        return !Double.isNaN(coverage);
    }

    public double getCoverage() {
        return coverage;
    }

    public List<SpatialAttribute> get(int index) {
        return tableData.listRow(index);
    }

    public int size() {
        return tableData.rowCount();
    }
}
