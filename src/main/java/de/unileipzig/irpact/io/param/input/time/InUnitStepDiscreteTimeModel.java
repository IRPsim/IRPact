package de.unileipzig.irpact.io.param.input.time;

import de.unileipzig.irpact.commons.exception.ParsingException;
import de.unileipzig.irpact.core.logging.IRPLogging;
import de.unileipzig.irpact.core.start.IRPactInputParser;
import de.unileipzig.irpact.io.param.LocalizedUiResource;
import de.unileipzig.irpact.jadex.simulation.JadexSimulationEnvironment;
import de.unileipzig.irpact.jadex.time.FixedUnitStepDiscreteTimeModel;
import de.unileipzig.irptools.Constants;
import de.unileipzig.irptools.defstructure.annotation.Definition;
import de.unileipzig.irptools.defstructure.annotation.DefinitionName;
import de.unileipzig.irptools.defstructure.annotation.FieldDefinition;
import de.unileipzig.irptools.util.CopyCache;
import de.unileipzig.irptools.util.TreeAnnotationResource;
import de.unileipzig.irptools.util.XorWithoutUnselectRuleBuilder;
import de.unileipzig.irptools.util.log.IRPLogger;

import java.lang.invoke.MethodHandles;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

import static de.unileipzig.irpact.io.param.IOConstants.TIME;
import static de.unileipzig.irpact.io.param.ParamUtil.*;
import static de.unileipzig.irpact.io.param.input.TreeViewStructureEnum.TIME_UNITDISCRET;

/**
 * @author Daniel Abitz
 */
@Definition
@LocalizedUiResource.PutClassPath(TIME_UNITDISCRET)
@LocalizedUiResource.TodoAddSimpleRuler
public class InUnitStepDiscreteTimeModel implements InTimeModel {

    private static final MethodHandles.Lookup L = MethodHandles.lookup();
    public static Class<?> thisClass() {
        return L.lookupClass();
    }
    public static String thisName() {
        return thisClass().getSimpleName();
    }

    protected static final String[] timeUnitFieldNames = {"useMs", "useSec", "useMin", "useH", "useD", "useW", "useM"};
    protected static final XorWithoutUnselectRuleBuilder timeUnitBuilder = new XorWithoutUnselectRuleBuilder()
            .withKeyModifier(buildDefaultParameterNameOperator(thisClass()))
            .withTrueValue(Constants.TRUE1)
            .withFalseValue(Constants.FALSE0)
            .withKeys(timeUnitFieldNames);

    @TreeAnnotationResource.Init
    public static void initRes(LocalizedUiResource res) {
    }
    @TreeAnnotationResource.Apply
    public static void applyRes(LocalizedUiResource res) {
        res.setRules(thisClass(), timeUnitFieldNames, timeUnitBuilder);
    }

    private static final IRPLogger LOGGER = IRPLogging.getLogger(thisClass());

    @DefinitionName
    public String name;

    @FieldDefinition
    @LocalizedUiResource.AddEntry
    @LocalizedUiResource.SimpleSet(
            g0Domain = true,
            intDefault = 1
    )
    public long amountOfTime;

    @FieldDefinition
    @LocalizedUiResource.AddEntry
    @LocalizedUiResource.SimpleSet(
            boolDomain = true
    )
    public boolean useMs;

    @FieldDefinition
    @LocalizedUiResource.AddEntry
    @LocalizedUiResource.SimpleSet(
            boolDomain = true
    )
    public boolean useSec;

    @FieldDefinition
    @LocalizedUiResource.AddEntry
    @LocalizedUiResource.SimpleSet(
            boolDomain = true
    )
    public boolean useMin;

    @FieldDefinition
    @LocalizedUiResource.AddEntry
    @LocalizedUiResource.SimpleSet(
            boolDomain = true
    )
    public boolean useH;

    @FieldDefinition
    @LocalizedUiResource.AddEntry
    @LocalizedUiResource.SimpleSet(
            boolDomain = true
    )
    public boolean useD;

    @FieldDefinition
    @LocalizedUiResource.AddEntry
    @LocalizedUiResource.SimpleSet(
            boolDomain = true,
            boolDefault = true
    )
    public boolean useW;

    @FieldDefinition
    @LocalizedUiResource.AddEntry
    @LocalizedUiResource.SimpleSet(
            boolDomain = true
    )
    public boolean useM;

    public InUnitStepDiscreteTimeModel() {
    }

    public InUnitStepDiscreteTimeModel(String name, long amountOfTime, ChronoUnit unit) {
        this.name = name;
        setAmountOfTime(amountOfTime);
        setUnit(unit);
    }

    @Override
    public InUnitStepDiscreteTimeModel copy(CopyCache cache) {
        return cache.copyIfAbsent(this, this::newCopy);
    }

    public InUnitStepDiscreteTimeModel newCopy(CopyCache cache) {
        InUnitStepDiscreteTimeModel copy = new InUnitStepDiscreteTimeModel();
        copy.name = name;
        copy.amountOfTime = amountOfTime;
        copy.useMs = useMs;
        copy.useSec = useSec;
        copy.useMin = useMin;
        copy.useH = useH;
        copy.useD = useD;
        copy.useW = useW;
        copy.useM = useM;
        return copy;
    }

    @Override
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public ChronoUnit getUnit() throws ParsingException {
        List<ChronoUnit> units = new ArrayList<>();
        if(useMs) units.add(ChronoUnit.MILLIS);
        if(useSec) units.add(ChronoUnit.SECONDS);
        if(useMin) units.add(ChronoUnit.MINUTES);
        if(useH) units.add(ChronoUnit.HOURS);
        if(useD) units.add(ChronoUnit.DAYS);
        if(useW) units.add(ChronoUnit.WEEKS);
        if(useM) units.add(ChronoUnit.MONTHS);

        switch(units.size()) {
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
    public FixedUnitStepDiscreteTimeModel parse(IRPactInputParser parser) throws ParsingException {
        FixedUnitStepDiscreteTimeModel timeModel = new FixedUnitStepDiscreteTimeModel();
        timeModel.setName(getName());
        timeModel.setEnvironment((JadexSimulationEnvironment) parser.getEnvironment());
        FixedUnitStepDiscreteTimeModel.CeilingTimeAdvanceFunction func = new FixedUnitStepDiscreteTimeModel.CeilingTimeAdvanceFunction(
                getAmountOfTime(),
                getUnit()
        );
        timeModel.setTimeAdvanceFunction(func);
        return timeModel;
    }
}
