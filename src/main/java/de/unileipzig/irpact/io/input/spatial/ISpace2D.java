package de.unileipzig.irpact.io.input.spatial;

import de.unileipzig.irpact.util.Todo;
import de.unileipzig.irptools.defstructure.annotation.Definition;
import de.unileipzig.irptools.defstructure.annotation.Edn;
import de.unileipzig.irptools.defstructure.annotation.FieldDefinition;

/**
 * @author Daniel Abitz
 */
@Definition(
        edn = @Edn(
                label = {"SpatialModel/Space2D"}
        )
)
@Todo("verallgemeinern")
public class ISpace2D {

    public String _name;

    @FieldDefinition
    public int metricID;

    public ISpace2D() {
    }

    public ISpace2D(String name, int id) {
        this._name = name;
        this.metricID = id;
    }

    public String getName() {
        return _name;
    }

    public int getMetricID() {
        return metricID;
    }
}
