package de.unileipzig.irpact.io.param.input.process.mra.component;

import de.unileipzig.irpact.commons.exception.ParsingException;
import de.unileipzig.irpact.core.logging.IRPLogging;
import de.unileipzig.irpact.core.process.mra.component.base.ValueComponent;
import de.unileipzig.irpact.core.process.mra.component.general.SumIntermediateComponent;
import de.unileipzig.irpact.core.start.IRPactInputParser;
import de.unileipzig.irpact.develop.Dev;
import de.unileipzig.irpact.io.param.input.InRootUI;
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
public class InSumIntermediateComponent implements InValueComponent {

    private static final MethodHandles.Lookup L = MethodHandles.lookup();
    public static Class<?> thisClass() {
        return L.lookupClass();
    }
    public static String thisName() {
        return thisClass().getSimpleName();
    }

    public static void initRes(TreeAnnotationResource res) {
    }
    public static void applyRes(TreeAnnotationResource res) {
        putClassPath(res, thisClass(), InRootUI.PROCESS_MRA_COMPONENTS_SUMINTER);

        addEntry(res, thisClass(), "weight");
        addEntry(res, thisClass(), "components");

        setDefault(res, thisClass(), "weight", VALUE_1);
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
    public InValueComponent[] components;
    public void setComponents(InValueComponent[] components) {
        this.components = components;
    }
    public InValueComponent[] getComponents() throws ParsingException {
        return getNonEmptyArray(components, "components");
    }

    public InSumIntermediateComponent() {
    }

    @Override
    public InSumIntermediateComponent copy(CopyCache cache) {
        return cache.copyIfAbsent(this, this::newCopy);
    }

    public InSumIntermediateComponent newCopy(CopyCache cache) {
        InSumIntermediateComponent copy = new InSumIntermediateComponent();
        return Dev.throwException();
    }

    @Override
    public SumIntermediateComponent parse(IRPactInputParser parser) throws ParsingException {

        SumIntermediateComponent component = new SumIntermediateComponent();
        component.setName(getName());
        component.setWeight(getWeight());
        for(InValueComponent comp: getComponents()) {
            ValueComponent vComp = parser.parseEntityTo(comp);
            component.add(vComp);
        }

        return component;
    }
}
