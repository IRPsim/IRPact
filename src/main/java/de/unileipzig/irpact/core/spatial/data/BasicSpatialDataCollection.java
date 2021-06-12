package de.unileipzig.irpact.core.spatial.data;

import de.unileipzig.irpact.commons.NameableBase;
import de.unileipzig.irpact.commons.util.data.DataCollection;
import de.unileipzig.irpact.commons.util.data.LinkedDataCollection;
import de.unileipzig.irpact.core.spatial.SpatialInformation;

import java.util.Objects;

/**
 * @author Daniel Abitz
 */
public class BasicSpatialDataCollection extends NameableBase implements SpatialDataCollection {

    protected LinkedDataCollection<SpatialInformation> data;

    public BasicSpatialDataCollection() {
    }

    public BasicSpatialDataCollection(LinkedDataCollection<SpatialInformation> data) {
        setData(data);
    }

    public void setData(LinkedDataCollection<SpatialInformation> data) {
        this.data = data;
    }

    @Override
    public DataCollection<SpatialInformation> getData() {
        return data;
    }

    @Override
    public boolean remove(SpatialInformation information) {
        final SpatialInformation removed = information.hasId()
                ? removeId0(information)
                : remove0(information);
        //sanity equals check?
        return removed != null;
    }

    private SpatialInformation removeId0(SpatialInformation info) {
        return data.removeFirst(info::hasSameId);
    }

    private SpatialInformation remove0(SpatialInformation info) {
        return data.removeFirst(info::isEquals);
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
    public DataCollection.View<SpatialInformation> getUnfilteredView() {
        return data.asView();
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
