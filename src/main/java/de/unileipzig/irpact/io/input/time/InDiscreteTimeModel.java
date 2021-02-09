package de.unileipzig.irpact.io.input.time;

import de.unileipzig.irptools.defstructure.annotation.Definition;
import de.unileipzig.irptools.defstructure.annotation.Edn;
import de.unileipzig.irptools.defstructure.annotation.FieldDefinition;

import java.util.Objects;

/**
 * @author Daniel Abitz
 */
@Definition(
        edn = @Edn(
                label = {"Zeitliche Modell", "Diskret"},
                priorities = {"-2", "1"}
        )
)
public class InDiscreteTimeModel implements InTimeModel {

    public String _name;

    @FieldDefinition
    public long timePerTickInMs;

    public InDiscreteTimeModel() {
    }

    public InDiscreteTimeModel(String name, long timePerTickInMs) {
        this._name = name;
        this.timePerTickInMs = timePerTickInMs;
    }

    public String getName() {
        return _name;
    }

    public long getTimePerTickInMs() {
        return timePerTickInMs;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof InDiscreteTimeModel)) return false;
        InDiscreteTimeModel timeModel = (InDiscreteTimeModel) o;
        return timePerTickInMs == timeModel.timePerTickInMs && Objects.equals(_name, timeModel._name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(_name, timePerTickInMs);
    }

    @Override
    public String toString() {
        return "InDiscreteTimeModel{" +
                "_name='" + _name + '\'' +
                ", timePerTickInMs=" + timePerTickInMs +
                '}';
    }
}
