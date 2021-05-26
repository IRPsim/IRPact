package de.unileipzig.irpact.core.spatial.distribution2;

import de.unileipzig.irpact.commons.checksum.Checksums;
import de.unileipzig.irpact.commons.util.data.DataCollection;
import de.unileipzig.irpact.commons.util.data.weighted.WeightedDataSupplier;
import de.unileipzig.irpact.core.spatial.SpatialDataCollection;
import de.unileipzig.irpact.core.spatial.SpatialDataFilter;
import de.unileipzig.irpact.core.spatial.SpatialInformation;

import java.util.Collection;

/**
 * @author Daniel Abitz
 */
public class WeightedDiscreteSpatialDistribution
        extends WeightedDataSupplier<String, SpatialInformation>
        implements SpatialDistribution {

    protected SpatialDataCollection spatialDataCollection;

    public WeightedDiscreteSpatialDistribution() {
        super();
        setRemoveOnDraw(true);
    }

    public void setSpatialData(SpatialDataCollection spatialDataCollection) {
        this.spatialDataCollection = spatialDataCollection;
        this.data = spatialDataCollection.getData();
    }

    public SpatialDataCollection getSpatialData() {
        return spatialDataCollection;
    }

    @Override
    public void rebuildWeightedMapping(boolean skipEmptyViews) {
        super.rebuildWeightedMapping(skipEmptyViews);
    }

    @Override
    public void setData(DataCollection<SpatialInformation> data) {
        throw new UnsupportedOperationException("use setSpatialDataCollection");
    }

    @Override
    public void set(String key, DataCollection.Filter<? super SpatialInformation> filter) {
        throw new UnsupportedOperationException("use addFilter");
    }

    @Override
    public boolean setUsed(SpatialInformation information) {
        return spatialDataCollection.remove(information);
    }

    public void useAll() {
        SpatialDataFilter filter = Unfiltered.DEFAULT_INSTANCE;
        if(hasView(filter.getName())) {
            throw new IllegalArgumentException("view '" + filter.getName() + "' already exists");
        }
        DataCollection.View<SpatialInformation> view = spatialDataCollection.addIfAbsent(filter);
        putView(filter.getName(), view);
    }

    public void addFilter(SpatialDataFilter filter) {
        if(hasView(filter.getName())) {
            throw new IllegalArgumentException("view '" + filter.getName() + "' already exists");
        }
        DataCollection.View<SpatialInformation> view = spatialDataCollection.addIfAbsent(filter);
        putView(filter.getName(), view);
    }

    public void addFilters(Collection<? extends SpatialDataFilter> filters) {
        for(SpatialDataFilter filter: filters) {
            addFilter(filter);
        }
    }

    @Override
    public int getChecksum() throws UnsupportedOperationException {
        return Checksums.SMART.getChecksum(
                spatialDataCollection,
                rnd,
                removeOnDraw
        );
    }
}
