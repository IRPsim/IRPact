package de.unileipzig.irpact.io.param.input.process.mra.component;

import de.unileipzig.irpact.commons.exception.ParsingException;
import de.unileipzig.irpact.core.logging.IRPLogging;
import de.unileipzig.irpact.core.process.mra.component.general.SumAttributeComponent;
import de.unileipzig.irpact.core.start.IRPactInputParser;
import de.unileipzig.irpact.develop.Dev;
import de.unileipzig.irpact.develop.ToRemove;
import de.unileipzig.irpact.io.param.input.TreeViewStructure;
import de.unileipzig.irpact.io.param.input.names.InAttributeName;
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
public class InSumAttributeComponent implements InValueComponent {

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
        putClassPath(res, thisClass(), TreeViewStructure.PROCESS_MRA_COMPONENTS_SUMATTR);

        addEntry(res, thisClass(), "weight");
        addEntry(res, thisClass(), "attributeNames");

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
    public InAttributeName[] attributeNames;
    public void setAttributeNames(InAttributeName[] attributeNames) {
        this.attributeNames = attributeNames;
    }
    public InAttributeName[] getAttributeNames() throws ParsingException {
        return getNonEmptyArray(attributeNames, "attributeNames");
    }

    public InSumAttributeComponent() {
    }

    @Override
    public InSumAttributeComponent copy(CopyCache cache) {
        return cache.copyIfAbsent(this, this::newCopy);
    }

    public InSumAttributeComponent newCopy(CopyCache cache) {
        InSumAttributeComponent copy = new InSumAttributeComponent();
        return Dev.throwException();
    }

    @Override
    public SumAttributeComponent parse(IRPactInputParser parser) throws ParsingException {
        SumAttributeComponent component = new SumAttributeComponent();

        component.setName(getName());
        component.setWeight(getWeight());
        for(InAttributeName attributeName: getAttributeNames()) {
            component.addAttributeName(attributeName.getName());
        }

        return component;
    }
}
