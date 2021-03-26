package de.unileipzig.irpact.io.param.input;

import de.unileipzig.irptools.defstructure.annotation.Definition;
import de.unileipzig.irptools.defstructure.annotation.FieldDefinition;
import de.unileipzig.irptools.util.TreeAnnotationResource;

import java.lang.invoke.MethodHandles;
import java.util.Objects;

import static de.unileipzig.irpact.io.param.ParamUtil.addEntry;
import static de.unileipzig.irpact.io.param.ParamUtil.putClassPath;

/**
 * @author Daniel Abitz
 */
@Definition
public class InAttributeName implements InEntity {

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
        putClassPath(res, thisClass(), thisName());
        addEntry(res, thisClass(), "placeholder");
    }

    public String _name;

    @FieldDefinition
    public int placeholder;

    public InAttributeName() {
    }

    public InAttributeName(String name) {
        this._name = name;
    }

    public String getName() {
        return _name;
    }

    @Override
    public Object parse(InputParser parser) {
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof InAttributeName)) return false;
        InAttributeName that = (InAttributeName) o;
        return placeholder == that.placeholder && Objects.equals(_name, that._name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(_name, placeholder);
    }

    @Override
    public String toString() {
        return "InAttributeName{" +
                "_name='" + _name + '\'' +
                ", placeholder=" + placeholder +
                '}';
    }
}
