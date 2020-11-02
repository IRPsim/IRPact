package de.unileipzig.irpact.v2.io.input.spatial;

import de.unileipzig.irpact.v2.develop.ToDo;
import de.unileipzig.irptools.defstructure.annotation.Definition;
import de.unileipzig.irptools.defstructure.annotation.Edn;
import de.unileipzig.irptools.defstructure.annotation.FieldDefinition;

/**
 * @author Daniel Abitz
 */
@Definition(
        edn = @Edn(
                path = {"SpatialModel/Space2D"}
        )
)
@ToDo("verallgemeinern")
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
