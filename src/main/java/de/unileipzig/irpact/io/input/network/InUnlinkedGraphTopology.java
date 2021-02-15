package de.unileipzig.irpact.io.input.network;

import de.unileipzig.irptools.defstructure.annotation.Definition;
import de.unileipzig.irptools.defstructure.annotation.FieldDefinition;
import de.unileipzig.irptools.util.TreeAnnotationResource;

import java.util.Objects;

/**
 * @author Daniel Abitz
 */
@Definition
public class InUnlinkedGraphTopology implements InGraphTopologyScheme {

    public static void initRes(TreeAnnotationResource res) {
        res.newElementBuilder()
                .setEdnLabel("Leere Topologie")
                .setEdnPriority(0)
                .setEdnDescription("Leere Topologie, welche keine Kanten besitzt.")
                .putCache("Leere Topologie");
    }
    public static void applyRes(TreeAnnotationResource res) {
        res.putPath(
                InUnlinkedGraphTopology.class,
                res.getCachedElement("Netzwerk"),
                res.getCachedElement("Topologie"),
                res.getCachedElement("Leere Topologie")
        );
    }

    public String _name;

    @FieldDefinition
    public double placeholderUnlinked;

    public InUnlinkedGraphTopology() {
    }

    public InUnlinkedGraphTopology(String name) {
        this._name = name;
    }

    public String getName() {
        return _name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof InUnlinkedGraphTopology)) return false;
        InUnlinkedGraphTopology topology = (InUnlinkedGraphTopology) o;
        return Objects.equals(_name, topology._name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(_name);
    }

    @Override
    public String toString() {
        return "InFreeNetworkTopology{" +
                "_name='" + _name + '\'' +
                '}';
    }
}
