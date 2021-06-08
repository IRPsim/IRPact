package de.unileipzig.irpact.core.spatial.distribution2;

import de.unileipzig.irpact.commons.NameableBase;
import de.unileipzig.irpact.core.spatial.data.SpatialDataFilter;
import de.unileipzig.irpact.core.spatial.SpatialInformation;

/**
 * @author Daniel Abitz
 */
public class Unfiltered extends NameableBase implements SpatialDataFilter {

    public static final Unfiltered DEFAULT_INSTANCE = new Unfiltered("Unfiltered_DEFAULT");

    public Unfiltered(String name) {
        setName(name);
    }

    @Override
    public boolean test(SpatialInformation info) {
        return true;
    }
}
