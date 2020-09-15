package de.unileipzig.irpact.start.irpact.input.simulation;

import de.unileipzig.irptools.defstructure.annotation.Definition;
import de.unileipzig.irptools.defstructure.annotation.Edn;
import de.unileipzig.irptools.defstructure.annotation.FieldDefinition;

import java.util.Objects;

/**
 * @author Daniel Abitz
 */
@Definition(
        edn = @Edn(
                label = "Simulation/ContinousTimeModel"
        )
)
public class ContinousTimeModel implements TimeModel {

    public String _name;

    @FieldDefinition
    public int acceleration;

    @FieldDefinition
    public long delay;

    public ContinousTimeModel() {
    }

    public ContinousTimeModel(String _name, int acceleration, long delay) {
        this._name = _name;
        this.acceleration = acceleration;
        this.delay = delay;
    }

    //=========================
    //helper
    //=========================

    @Override
    public String toString() {
        return "DiscretTimeModel{" +
                "_name='" + _name + '\'' +
                ", acceleration=" + acceleration +
                ", delay=" + delay +
                '}';
    }

    //=========================
    //default implementaion
    //=========================

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ContinousTimeModel that = (ContinousTimeModel) o;
        return acceleration == that.acceleration &&
                delay == that.delay &&
                Objects.equals(_name, that._name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(_name, acceleration, delay);
    }
}
