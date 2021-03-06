package de.unileipzig.irpact.core.spatial.data;

import de.unileipzig.irpact.commons.Nameable;
import de.unileipzig.irpact.commons.util.data.DataCollection;
import de.unileipzig.irpact.core.spatial.SpatialInformation;

/**
 * @author Daniel Abitz
 */
public interface SpatialDataCollection extends Nameable {

    boolean remove(SpatialInformation information);

    DataCollection<SpatialInformation> getData();

    boolean hasFilter(SpatialDataFilter filter);

    DataCollection.View<SpatialInformation> getUnfilteredView();

    DataCollection.View<SpatialInformation> getView(SpatialDataFilter filter);

    DataCollection.View<SpatialInformation> addFilter(SpatialDataFilter filter);

    default DataCollection.View<SpatialInformation> addIfAbsent(SpatialDataFilter filter) {
        return hasFilter(filter)
                ? getView(filter)
                : addFilter(filter);
    }
}
