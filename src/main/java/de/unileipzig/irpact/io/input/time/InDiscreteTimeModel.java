package de.unileipzig.irpact.io.input.time;

import de.unileipzig.irptools.defstructure.annotation.Definition;
import de.unileipzig.irptools.defstructure.annotation.FieldDefinition;
import de.unileipzig.irptools.util.TreeAnnotationResource;

import java.util.Objects;

/**
 * @author Daniel Abitz
 */
@Definition
public class InDiscreteTimeModel implements InTimeModel {

    public static void initRes(TreeAnnotationResource res) {
        res.newElementBuilder()
                .setEdnLabel("Diskret")
                .setEdnPriority(0)
                .putCache("Diskret");
    }
    public static void applyRes(TreeAnnotationResource res) {
        res.putPath(
                InDiscreteTimeModel.class,
                res.getCachedElement("Zeitliche Modell"),
                res.getCachedElement("Diskret")
        );
    }

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
