package de.unileipzig.irpact.core.spatial;

import de.unileipzig.irpact.commons.NameableBase;
import de.unileipzig.irpact.commons.util.data.DataCollection;

/**
 * @author Daniel Abitz
 */
public class BasicSpatialDataCollection extends NameableBase implements SpatialDataCollection {

    protected DataCollection<SpatialInformation> data;

    public BasicSpatialDataCollection() {
    }

    public void setData(DataCollection<SpatialInformation> data) {
        this.data = data;
    }

    @Override
    public DataCollection<SpatialInformation> getData() {
        return data;
    }
}
