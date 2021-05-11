package de.unileipzig.irpact.core.spatial;

import de.unileipzig.irpact.commons.Nameable;
import de.unileipzig.irpact.commons.util.data.DataCollection;
import de.unileipzig.irpact.core.spatial.distribution2.SpatialDataFilter;

/**
 * @author Daniel Abitz
 */
public interface SpatialDataCollection extends Nameable {

    DataCollection<SpatialInformation> getData();

    boolean hasFilter(SpatialDataFilter filter);

    DataCollection.View<SpatialInformation> getView(SpatialDataFilter filter);

    DataCollection.View<SpatialInformation> addFilter(SpatialDataFilter filter);
}
