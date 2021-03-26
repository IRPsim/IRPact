package de.unileipzig.irpact.experimental.deprecated.input.spatial;

import de.unileipzig.irpact.util.TodoOLD;
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
@TodoOLD("verallgemeinern")
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