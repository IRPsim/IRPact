package de.unileipzig.irpact.io.param.input.time;

import de.unileipzig.irpact.commons.exception.ParsingException;
import de.unileipzig.irpact.core.start.IRPactInputParser;
import de.unileipzig.irpact.io.param.LocalizedUiResource;
import de.unileipzig.irpact.jadex.simulation.JadexSimulationEnvironment;
import de.unileipzig.irpact.jadex.time.DiscreteTimeModel;
import de.unileipzig.irptools.defstructure.annotation.Definition;
import de.unileipzig.irptools.defstructure.annotation.DefinitionName;
import de.unileipzig.irptools.defstructure.annotation.FieldDefinition;
import de.unileipzig.irptools.util.CopyCache;
import de.unileipzig.irptools.util.TreeAnnotationResource;

import java.lang.invoke.MethodHandles;
import java.util.Objects;

import static de.unileipzig.irpact.io.param.input.TreeViewStructureEnum.TIME_DISCRET;

/**
 * @author Daniel Abitz
 */
@Definition(ignore = true)
@LocalizedUiResource.Ignore
@LocalizedUiResource.PutClassPath(TIME_DISCRET)
public class InDiscreteTimeModel implements InTimeModel {

    private static final MethodHandles.Lookup L = MethodHandles.lookup();
    public static Class<?> thisClass() {
        return L.lookupClass();
    }
    public static String thisName() {
        return thisClass().getSimpleName();
    }

    @TreeAnnotationResource.Init
    public static void initRes(LocalizedUiResource res) {
    }
    @TreeAnnotationResource.Apply
    public static void applyRes(LocalizedUiResource res) {
    }

    @DefinitionName
    public String name;

    @FieldDefinition
    @LocalizedUiResource.AddEntry
    @LocalizedUiResource.SimpleSet(
            intDefault = 1000
    )
    public long timePerTickInMs = 1000;

    public InDiscreteTimeModel() {
    }

    public InDiscreteTimeModel(String name, long timePerTickInMs) {
        this.name = name;
        this.timePerTickInMs = timePerTickInMs;
    }

    @Override
    public InDiscreteTimeModel copy(CopyCache cache) {
        throw new UnsupportedOperationException();
    }

    @Override
    public String getName() {
        return name;
    }

    public long getTimePerTickInMs() {
        return timePerTickInMs;
    }

    @Override
    public DiscreteTimeModel parse(IRPactInputParser parser) throws ParsingException {
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
        return timePerTickInMs == timeModel.timePerTickInMs && Objects.equals(name, timeModel.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, timePerTickInMs);
    }

    @Override
    public String toString() {
        return "InDiscreteTimeModel{" +
                "_name='" + name + '\'' +
                ", timePerTickInMs=" + timePerTickInMs +
                '}';
    }
}
