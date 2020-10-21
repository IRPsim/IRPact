package de.unileipzig.irpact.v2.io.input.spatial;

import de.unileipzig.irptools.defstructure.annotation.Definition;
import de.unileipzig.irptools.defstructure.annotation.Edn;
import de.unileipzig.irptools.defstructure.annotation.FieldDefinition;

/**
 * @author Daniel Abitz
 */
@Definition(
        edn = @Edn(
                path = "SpatialModel/Space2D"
        )
)
public class ISpace2D {

    public String _name;

    @FieldDefinition
    public int metricID;

    public ISpace2D() {
    }

    public String getName() {
        return _name;
    }

    public int getMetricID() {
        return metricID;
    }
}
