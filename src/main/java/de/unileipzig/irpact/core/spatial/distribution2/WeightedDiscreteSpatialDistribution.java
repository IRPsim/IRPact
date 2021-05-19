package de.unileipzig.irpact.core.spatial.distribution2;

import de.unileipzig.irpact.commons.util.data.DataCollection;
import de.unileipzig.irpact.commons.util.data.weighted.WeightedTypeBasedDistribution;
import de.unileipzig.irpact.core.spatial.SpatialDataCollection;
import de.unileipzig.irpact.core.spatial.SpatialInformation;

import java.util.Map;

/**
 * @author Daniel Abitz
 */
public class WeightedDiscreteSpatialDistribution
        extends WeightedTypeBasedDistribution<String, SpatialInformation>
        implements SpatialDistribution{

    public WeightedDiscreteSpatialDistribution() {
    }

    public void useAll(SpatialDataCollection dataColl) {
        SpatialDataFilter filter = Unfiltered.DEFAULT_INSTANCE;
        DataCollection.View<SpatialInformation> view = dataColl.hasFilter(filter)
                ? dataColl.getView(filter)
                : dataColl.addFilter(filter);
        set(filter.getName(), view);
    }

    public void addFilters(
            SpatialDataCollection dataColl,
            Map<? extends String, ? extends SpatialDataFilter> filters) {
        for(Map.Entry<? extends String, ? extends SpatialDataFilter> entry: filters.entrySet()) {
            SpatialDataFilter filter = entry.getValue();
            DataCollection.View<SpatialInformation> view = dataColl.hasFilter(filter)
                    ? dataColl.getView(filter)
                    : dataColl.addFilter(filter);
            set(entry.getKey(), view);
        }
    }
}
