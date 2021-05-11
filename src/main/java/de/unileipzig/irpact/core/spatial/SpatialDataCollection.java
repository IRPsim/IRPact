package de.unileipzig.irpact.core.spatial;

import de.unileipzig.irpact.commons.Nameable;
import de.unileipzig.irpact.commons.util.data.DataCollection;

/**
 * @author Daniel Abitz
 */
public interface SpatialDataCollection extends Nameable {

    DataCollection<SpatialInformation> getData();
}
