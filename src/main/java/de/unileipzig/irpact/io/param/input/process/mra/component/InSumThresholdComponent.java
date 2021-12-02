package de.unileipzig.irpact.io.param.input.process.mra.component;

import de.unileipzig.irpact.commons.exception.ParsingException;
import de.unileipzig.irpact.core.logging.IRPLogging;
import de.unileipzig.irpact.core.process.mra.component.base.ValueComponent;
import de.unileipzig.irpact.core.process.mra.component.general.SumThresholdComponent;
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
public class InSumThresholdComponent implements InEvaluableComponent {

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
        putClassPath(res, thisClass(), TreeViewStructure.PROCESS_MRA_COMPONENTS_SUMTHRESH);

        addEntry(res, thisClass(), "weight");
        addEntry(res, thisClass(), "threshold");
        addEntry(res, thisClass(), "components");

        setDefault(res, thisClass(), "weight", VALUE_1);
        setDefault(res, thisClass(), "threshold", VALUE_0);
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
    public double weight;
    public double getWeight() {
        return weight;
    }
    public void setWeight(double weight) {
        this.weight = weight;
    }

    @FieldDefinition
    public double threshold;
    public double getThreshold() {
        return threshold;
    }
    public void setThreshold(double threshold) {
        this.threshold = threshold;
    }

    @FieldDefinition
    public InValueComponent[] components;
    public void setComponents(InValueComponent[] components) {
        this.components = components;
    }
    public InValueComponent[] getComponents() throws ParsingException {
        return getNonEmptyArray(components, "components");
    }

    public InSumThresholdComponent() {
    }

    @Override
    public InSumThresholdComponent copy(CopyCache cache) {
        return cache.copyIfAbsent(this, this::newCopy);
    }

    public InSumThresholdComponent newCopy(CopyCache cache) {
        InSumThresholdComponent copy = new InSumThresholdComponent();
        return Dev.throwException();
    }

    @Override
    public SumThresholdComponent parse(IRPactInputParser parser) throws ParsingException {

        SumThresholdComponent component = new SumThresholdComponent();
        component.setName(getName());
        component.setWeight(getWeight());
        component.setThreshold(getThreshold());
        for(InValueComponent comp: getComponents()) {
            ValueComponent vComp = parser.parseEntityTo(comp);
            component.add(vComp);
        }

        return component;
    }
}
