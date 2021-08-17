package de.unileipzig.irpact.io.param.output;

import de.unileipzig.irpact.io.param.input.InIRPactEntity;
import de.unileipzig.irptools.defstructure.DefinitionType;
import de.unileipzig.irptools.defstructure.annotation.Definition;
import de.unileipzig.irptools.defstructure.annotation.FieldDefinition;
import de.unileipzig.irptools.util.CopyCache;
import de.unileipzig.irptools.util.TreeAnnotationResource;

import java.lang.invoke.MethodHandles;

import static de.unileipzig.irpact.io.param.IOConstants.INFORMATIONS_OUT;
import static de.unileipzig.irpact.io.param.ParamUtil.addEntry;
import static de.unileipzig.irpact.io.param.ParamUtil.putClassPath;

/**
 * @author Daniel Abitz
 */
@Definition(template = DefinitionType.OUTPUT)
public class OutInformation implements InIRPactEntity {

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
        putClassPath(res, thisClass(), INFORMATIONS_OUT, thisName());
        addEntry(res, thisClass(), "placeholder");
    }

    public String _name;

    @FieldDefinition
    public double placeholder;

    public OutInformation() {
    }

    public OutInformation(String name) {
        setName(name);
    }

    @Override
    public String getName() {
        return _name;
    }

    public void setName(String name) {
        this._name = name;
    }

    @Override
    public OutInformation copy(CopyCache cache) {
        return cache.copyIfAbsent(this, this::newCopy);
    }

    public OutInformation newCopy(CopyCache cache) {
        OutInformation copy = new OutInformation();
        copy._name = _name;
        copy.placeholder = placeholder;
        return copy;
    }
}
