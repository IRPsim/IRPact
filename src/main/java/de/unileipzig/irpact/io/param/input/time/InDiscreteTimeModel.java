package de.unileipzig.irpact.io.param.input.time;

import de.unileipzig.irpact.commons.exception.ParsingException;
import de.unileipzig.irpact.io.param.input.InputParser;
import de.unileipzig.irpact.jadex.simulation.JadexSimulationEnvironment;
import de.unileipzig.irpact.jadex.time.DiscreteTimeModel;
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
    }
    public static void applyRes(TreeAnnotationResource res) {
        res.putPath(
                InDiscreteTimeModel.class,
                res.getCachedElement("Zeitliche Modell"),
                res.getCachedElement("Diskret")
        );

        res.newEntryBuilder()
                .setGamsIdentifier("Zeit pro Schritt")
                .setGamsDescription("Zeit pro Schritt")
                .setGamsUnit("[ms]")
                .store(InDiscreteTimeModel.class, "timePerTickInMs");
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

    @Override
    public String getName() {
        return _name;
    }

    public long getTimePerTickInMs() {
        return timePerTickInMs;
    }

    @Override
    public DiscreteTimeModel parse(InputParser parser) throws ParsingException {
        DiscreteTimeModel timeModel = new DiscreteTimeModel();
        timeModel.setName(getName());
        timeModel.setEnvironment((JadexSimulationEnvironment) parser.getEnvironment());
        timeModel.setStoredDelta(1L); //fix
        timeModel.setStoredTimePerTickInMs(getTimePerTickInMs());
        return timeModel;
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
