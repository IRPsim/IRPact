package de.unileipzig.irpact.io.param.input.time;

import de.unileipzig.irpact.commons.exception.ParsingException;
import de.unileipzig.irpact.core.log.IRPLogging;
import de.unileipzig.irpact.io.param.input.InputParser;
import de.unileipzig.irpact.jadex.simulation.JadexSimulationEnvironment;
import de.unileipzig.irpact.jadex.time.UnitStepDiscreteTimeModel;
import de.unileipzig.irptools.defstructure.annotation.Definition;
import de.unileipzig.irptools.defstructure.annotation.FieldDefinition;
import de.unileipzig.irptools.util.TreeAnnotationResource;
import de.unileipzig.irptools.util.log.IRPLogger;

import java.lang.invoke.MethodHandles;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Daniel Abitz
 */
@Definition
public class InUnitStepDiscreteTimeModel implements InTimeModel {

    //damit ich bei copy&paste nie mehr vergesse die Klasse anzupassen :)
    private static final MethodHandles.Lookup L = MethodHandles.lookup();
    public static Class<?> thisClass() {
        return L.lookupClass();
    }

    public static void initRes(TreeAnnotationResource res) {
    }
    public static void applyRes(TreeAnnotationResource res) {
    }

    private static final IRPLogger LOGGER = IRPLogging.getLogger(thisClass());

    private static final ChronoUnit[] UNITS = {
            ChronoUnit.MILLIS,
            ChronoUnit.SECONDS,
            ChronoUnit.MINUTES,
            ChronoUnit.HOURS,
            ChronoUnit.DAYS,
            ChronoUnit.WEEKS,
            ChronoUnit.MONTHS
    };

    public String _name;

    @FieldDefinition
    public long amountOfTime;

    @FieldDefinition
    public boolean useMs = true;

    @FieldDefinition
    public boolean useSec;

    @FieldDefinition
    public boolean useMin;

    @FieldDefinition
    public boolean useH;

    @FieldDefinition
    public boolean useD;

    @FieldDefinition
    public boolean useW;

    @FieldDefinition
    public boolean useM;

    public InUnitStepDiscreteTimeModel() {
    }

    public InUnitStepDiscreteTimeModel(String name, long amountOfTime, ChronoUnit unit) {
        this._name = name;
        setAmountOfTime(amountOfTime);
        setUnit(unit);
    }

    @Override
    public String getName() {
        return _name;
    }

    public void setAmountOfTime(long amountOfTime) {
        this.amountOfTime = amountOfTime;
    }

    public long getAmountOfTime() throws ParsingException {
        if(amountOfTime < 1L) {
            throw new ParsingException("amount of time < 1: " + amountOfTime);
        }
        return amountOfTime;
    }

    private boolean[] buildFlagArray() {
        return new boolean[]{useMs, useSec, useMin, useH, useD, useW, useM};
    }

    private List<ChronoUnit> getUnits() {
        List<ChronoUnit> list = new ArrayList<>();
        boolean[] flagArr = buildFlagArray();
        for(int i = 0; i < flagArr.length; i++) {
            if(flagArr[i]) {
                list.add(UNITS[i]);
            }
        }
        return list;
    }

    public ChronoUnit getUnit() throws ParsingException {
        List<ChronoUnit> units = getUnits();
        switch (units.size()) {
            case 0:
                throw new ParsingException("Missing time unit");

            case 1:
                return units.get(0);

            default:
                throw new ParsingException("Multiple time units set: " + units);
        }
    }

    public void setUnit(ChronoUnit unit) {
        useMs = false;
        useSec = false;
        useMin = false;
        useH = false;
        useD = false;
        useW = false;
        useM = false;

        switch (unit) {
            case MILLIS:
                useMs = true;
                break;

            case SECONDS:
                useSec = true;
                break;

            case MINUTES:
                useMin = true;
                break;

            case HOURS:
                useH = true;
                break;

            case DAYS:
                useD = true;
                break;

            case WEEKS:
                useW = true;
                break;

            case MONTHS:
                useM = true;
                break;

            default:
                throw new IllegalArgumentException("unsupported unit: " + unit);
        }
    }

    @Override
    public UnitStepDiscreteTimeModel parse(InputParser parser) throws ParsingException {
        UnitStepDiscreteTimeModel timeModel = new UnitStepDiscreteTimeModel();
        timeModel.setName(getName());
        timeModel.setEnvironment((JadexSimulationEnvironment) parser.getEnvironment());
        UnitStepDiscreteTimeModel.SimpleTimeAdvanceFunction func = new UnitStepDiscreteTimeModel.SimpleTimeAdvanceFunction(
                getAmountOfTime(),
                getUnit()
        );
        timeModel.setTimeAdvanceFunction(func);
        return timeModel;
    }
}
