package de.unileipzig.irpact.core.spatial;

import de.unileipzig.irpact.commons.Check;

/**
 * @author Daniel Abitz
 */
public abstract class SpatialModelBase implements SpatialModel {

    protected String name;

    public SpatialModelBase(String name) {
        this.name = Check.requireNonNull(name, "name");
    }

    @Override
    public String getName() {
        return name;
    }
}
