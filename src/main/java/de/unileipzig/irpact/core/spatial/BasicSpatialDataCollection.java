package de.unileipzig.irpact.core.spatial;

import de.unileipzig.irpact.commons.NameableBase;
import de.unileipzig.irpact.commons.util.data.DataCollection;
import de.unileipzig.irpact.commons.util.data.LinkedDataCollection;
import de.unileipzig.irpact.core.spatial.distribution2.SpatialDataFilter;

import java.util.Objects;

/**
 * @author Daniel Abitz
 */
public class BasicSpatialDataCollection extends NameableBase implements SpatialDataCollection {

    protected LinkedDataCollection<SpatialInformation> data;

    public BasicSpatialDataCollection() {
    }

    public void setData(LinkedDataCollection<SpatialInformation> data) {
        this.data = data;
    }

    @Override
    public DataCollection<SpatialInformation> getData() {
        return data;
    }

    @Override
    public boolean removeId(int id) {
        return data.removeFirst(info -> info.hasId() && info.getId() == id) != null;
    }

    @Override
    public boolean hasFilter(SpatialDataFilter filter) {
        for(DataCollection.Filter<? super SpatialInformation> f: data.getFilters()) {
            SpatialDataFilter sdf = (SpatialDataFilter) f;
            if(sdf == filter || Objects.equals(sdf.getName(), filter.getName())) {
                return true;
            }
        }
        return false;
    }

    @Override
    public DataCollection.View<SpatialInformation> getView(SpatialDataFilter filter) {
        return data.getView(filter);
    }

    @Override
    public DataCollection.View<SpatialInformation> addFilter(SpatialDataFilter filter) {
        return data.createView(filter);
    }
}
