package de.unileipzig.irpact.io.input.spatial;

import de.unileipzig.irptools.defstructure.annotation.Definition;
import de.unileipzig.irptools.defstructure.annotation.FieldDefinition;
import de.unileipzig.irptools.util.TreeAnnotationResource;

import java.util.Objects;

/**
 * @author Daniel Abitz
 */
@Definition
public class InSpace2D implements InSpatialModel {

    public static void initRes(TreeAnnotationResource res) {
        res.newElementBuilder()
                .setEdnLabel("Space2D")
                .setEdnPriority(0)
                .putCache("Space2D");
    }

    public static void applyRes(TreeAnnotationResource res) {
        res.putPath(
                InSpace2D.class,
                res.getCachedElement("Räumliche Modell"),
                res.getCachedElement("Space2D")
        );
    }

    public String _name;

    @FieldDefinition
    public boolean useEuclid;

    public InSpace2D() {
    }

    public InSpace2D(String name, boolean useEuclid) {
        this._name = name;
        this.useEuclid = useEuclid;
    }

    public String getName() {
        return _name;
    }

    public boolean useEuclid() {
        return useEuclid;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof InSpace2D)) return false;
        InSpace2D space2D = (InSpace2D) o;
        return useEuclid == space2D.useEuclid && Objects.equals(_name, space2D._name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(_name, useEuclid);
    }

    @Override
    public String toString() {
        return "InSpace2D{" +
                "_name='" + _name + '\'' +
                ", useEuclid=" + useEuclid +
                '}';
    }
}
