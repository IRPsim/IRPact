package de.unileipzig.irpact.core.spatial;

import de.unileipzig.irpact.commons.NameableBase;
import de.unileipzig.irpact.commons.spatial.attribute.SpatialAttribute;
import de.unileipzig.irpact.commons.util.io.FileContent;
import de.unileipzig.irpact.commons.util.io3.TableData3;

import java.util.List;

/**
 * @author Daniel Abitz
 */
public class SpatialTableFileContent2 extends NameableBase implements FileContent {

    protected TableData3<SpatialAttribute> tableData;
    protected double coverage;

    public SpatialTableFileContent2(String name, TableData3<SpatialAttribute> tableData, double coverage) {
        setName(name);
        this.tableData = tableData;
        this.coverage = coverage;
    }

    @Override
    public TableData3<SpatialAttribute> content() {
        return getTableData();
    }

    public TableData3<SpatialAttribute> getTableData() {
        return tableData;
    }

    public boolean hasCoverage() {
        return !Double.isNaN(coverage);
    }

    public double getCoverage() {
        return coverage;
    }

    public List<SpatialAttribute> get(int index) {
        return tableData.getRow(index);
    }

    public int size() {
        return tableData.getNumberOfRows();
    }
}
