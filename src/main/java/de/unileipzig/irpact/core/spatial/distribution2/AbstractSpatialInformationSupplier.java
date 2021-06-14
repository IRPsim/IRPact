package de.unileipzig.irpact.core.spatial.distribution2;

import de.unileipzig.irpact.commons.checksum.ChecksumValue;
import de.unileipzig.irpact.commons.checksum.Checksums;
import de.unileipzig.irpact.commons.util.IdManager;
import de.unileipzig.irpact.commons.util.data.DataCollection;
import de.unileipzig.irpact.commons.util.data.weighted.WeightedDataSupplier;
import de.unileipzig.irpact.core.spatial.SpatialInformation;
import de.unileipzig.irpact.core.spatial.data.SpatialDataCollection;
import de.unileipzig.irpact.core.spatial.data.SpatialDataFilter;
import de.unileipzig.irpact.core.spatial.distribution.SpatialDistribution;

import java.util.Collection;

/**
 * @author Daniel Abitz
 */
public abstract class AbstractSpatialInformationSupplier
        extends WeightedDataSupplier<String, SpatialInformation>
        implements SpatialDistribution {

    @ChecksumValue
    protected final IdManager idManager = new IdManager(0L);
    @ChecksumValue
    protected SpatialDataCollection spatialDataCollection;

    public AbstractSpatialInformationSupplier() {
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

    public IdManager getIdManager() {
        return idManager;
    }

    public void addUnfiltered() {
        if(hasView(Unfiltered.DEFAULT_INSTANCE.getName())) {
            throw new IllegalArgumentException("view '" + Unfiltered.DEFAULT_INSTANCE.getName() + "' already exists");
        }
        DataCollection.View<SpatialInformation> view = spatialDataCollection.getUnfilteredView();
        putView(Unfiltered.DEFAULT_INSTANCE.getName(), view);
    }

    public void addFilter(SpatialDataFilter filter) {
        if(filter == null) {
            addUnfiltered();
            return;
        }

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

    protected void updateInformation(SpatialInformation drawnValue) {
        if(!drawnValue.hasId()) {
            drawnValue.setId(idManager.nextId());
        }
    }

    @Override
    public SpatialInformation drawValue() {
        SpatialInformation drawnValue = super.drawValue();
        updateInformation(drawnValue);
        return drawnValue;
    }

    @Override
    public int getChecksum() {
        return Checksums.SMART.getChecksum(
                spatialDataCollection,
                rnd,
                removeOnDraw,
                idManager
        );
    }
}
