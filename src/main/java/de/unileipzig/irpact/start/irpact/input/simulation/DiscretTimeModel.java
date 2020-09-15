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
                path = "Simulation/DiscretTimeModel"
        )
)
public class DiscretTimeModel implements TimeModel {

    public String _name;

    @FieldDefinition
    public int msPerTick;

    @FieldDefinition
    public long delay;

    public DiscretTimeModel() {
    }

    public DiscretTimeModel(String _name, int msPerTick, long delay) {
        this._name = _name;
        this.msPerTick = msPerTick;
        this.delay = delay;
    }

    //=========================
    //helper
    //=========================

    @Override
    public String toString() {
        return "DiscretTimeModel{" +
                "_name='" + _name + '\'' +
                ", tickDelta=" + msPerTick +
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
        DiscretTimeModel that = (DiscretTimeModel) o;
        return msPerTick == that.msPerTick &&
                delay == that.delay &&
                Objects.equals(_name, that._name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(_name, msPerTick, delay);
    }
}
