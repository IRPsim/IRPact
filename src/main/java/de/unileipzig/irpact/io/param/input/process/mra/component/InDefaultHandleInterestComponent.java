package de.unileipzig.irpact.io.param.input.process.mra.component;

import de.unileipzig.irpact.commons.exception.ParsingException;
import de.unileipzig.irpact.core.logging.IRPLogging;
import de.unileipzig.irpact.core.process.mra.ModularRAProcessModel;
import de.unileipzig.irpact.core.process.mra.component.special.DefaultHandleInterestComponent;
import de.unileipzig.irpact.core.process.ra.RAModelData;
import de.unileipzig.irpact.core.start.IRPactInputParser;
import de.unileipzig.irpact.develop.Dev;
import de.unileipzig.irpact.develop.ToRemove;
import de.unileipzig.irpact.io.param.input.TreeViewStructure;
import de.unileipzig.irptools.defstructure.annotation.Definition;
import de.unileipzig.irptools.defstructure.annotation.FieldDefinition;
import de.unileipzig.irptools.util.CopyCache;
import de.unileipzig.irptools.util.TreeAnnotationResource;
import de.unileipzig.irptools.util.log.IRPLogger;

import java.lang.invoke.MethodHandles;

import static de.unileipzig.irpact.io.param.ParamUtil.*;

/**
 * @author Daniel Abitz
 */
@Definition
@ToRemove
public class InDefaultHandleInterestComponent implements InEvaluableComponent {

    private static final MethodHandles.Lookup L = MethodHandles.lookup();
    public static Class<?> thisClass() {
        return L.lookupClass();
    }
    public static String thisName() {
        return thisClass().getSimpleName();
    }

    @TreeAnnotationResource.Init
    public static void initRes(TreeAnnotationResource res) {
    }
    @TreeAnnotationResource.Apply
    public static void applyRes(TreeAnnotationResource res) {
        putClassPath(res, thisClass(), TreeViewStructure.PROCESS_MRA_COMPONENTS_DEFINTEREST);

        addEntry(res, thisClass(), "adopterPoints");
        addEntry(res, thisClass(), "interestedPoints");
        addEntry(res, thisClass(), "awarePoints");
        addEntry(res, thisClass(), "unknownPoints");


        setDefault(res, thisClass(), "adopterPoints", varargs(RAModelData.DEFAULT_ADOPTER_POINTS));
        setDefault(res, thisClass(), "interestedPoints", varargs(RAModelData.DEFAULT_INTERESTED_POINTS));
        setDefault(res, thisClass(), "awarePoints", varargs(RAModelData.DEFAULT_AWARE_POINTS));
        setDefault(res, thisClass(), "unknownPoints", varargs(RAModelData.DEFAULT_UNKNOWN_POINTS));
    }

    private static final IRPLogger LOGGER = IRPLogging.getLogger(thisClass());

    public String _name;

    @Override
    public String getName() {
        return _name;
    }

    public void setName(String name) {
        this._name = name;
    }

    @FieldDefinition
    public int adopterPoints;
    public int getAdopterPoints() {
        return adopterPoints;
    }
    public void setAdopterPoints(int adopterPoints) {
        this.adopterPoints = adopterPoints;
    }

    @FieldDefinition
    public int interestedPoints;
    public int getInterestedPoints() {
        return interestedPoints;
    }
    public void setInterestedPoints(int interestedPoints) {
        this.interestedPoints = interestedPoints;
    }

    @FieldDefinition
    public int awarePoints;
    public int getAwarePoints() {
        return awarePoints;
    }
    public void setAwarePoints(int awarePoints) {
        this.awarePoints = awarePoints;
    }

    @FieldDefinition
    public int unknownPoints;
    public int getUnknownPoints() {
        return unknownPoints;
    }
    public void setUnknownPoints(int unknownPoints) {
        this.unknownPoints = unknownPoints;
    }

    public InDefaultHandleInterestComponent() {
    }

    @Override
    public InDefaultHandleInterestComponent copy(CopyCache cache) {
        return cache.copyIfAbsent(this, this::newCopy);
    }

    public InDefaultHandleInterestComponent newCopy(CopyCache cache) {
        InDefaultHandleInterestComponent copy = new InDefaultHandleInterestComponent();
        return Dev.throwException();
    }

    public void setDefaultValues() {
        setAdopterPoints(RAModelData.DEFAULT_ADOPTER_POINTS);
        setInterestedPoints(RAModelData.DEFAULT_INTERESTED_POINTS);
        setAwarePoints(RAModelData.DEFAULT_AWARE_POINTS);
        setUnknownPoints(RAModelData.DEFAULT_UNKNOWN_POINTS);
    }

    @Override
    public DefaultHandleInterestComponent parse(IRPactInputParser parser, Object input) throws ParsingException {
        ModularRAProcessModel model = getAs(input);

        DefaultHandleInterestComponent component = new DefaultHandleInterestComponent();

        component.setName(getName());
        component.setEnvironment(parser.getEnvironment());
        component.setModel(model);
        component.setAdopterPoints(getAdopterPoints());
        component.setInterestedPoints(getInterestedPoints());
        component.setAwarePoints(getAwarePoints());
        component.setUnknownPoints(getUnknownPoints());

        return component;
    }
}
