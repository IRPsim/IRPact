package de.unileipzig.irpact.io.input;

import de.unileipzig.irptools.defstructure.annotation.Definition;
import de.unileipzig.irptools.defstructure.annotation.FieldDefinition;
import de.unileipzig.irptools.util.TreeAnnotationResource;

import java.util.Objects;

/**
 * @author Daniel Abitz
 */
@Definition
public class InAttributeName {

    public static void initRes(TreeAnnotationResource res) {
    }
    public static void applyRes(TreeAnnotationResource res) {
        res.putPath(
                InAttributeName.class,
                res.getCachedElement("Namen")
        );
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
