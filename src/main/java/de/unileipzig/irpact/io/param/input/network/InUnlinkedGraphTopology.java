package de.unileipzig.irpact.io.param.input.network;

import de.unileipzig.irpact.commons.exception.ParsingException;
import de.unileipzig.irpact.core.network.SocialGraph;
import de.unileipzig.irpact.core.network.topology.UnlinkedGraphTopology;
import de.unileipzig.irpact.io.param.input.InputParser;
import de.unileipzig.irptools.defstructure.annotation.Definition;
import de.unileipzig.irptools.defstructure.annotation.FieldDefinition;
import de.unileipzig.irptools.util.TreeAnnotationResource;

import java.lang.invoke.MethodHandles;
import java.util.Objects;

/**
 * @author Daniel Abitz
 */
@Definition
public class InUnlinkedGraphTopology implements InGraphTopologyScheme {

    //damit ich bei copy&paste nie mehr vergesse die Klasse anzupassen :)
    private static final MethodHandles.Lookup L = MethodHandles.lookup();
    public static Class<?> thisClass() {
        return L.lookupClass();
    }

    public static void initRes(TreeAnnotationResource res) {
    }
    public static void applyRes(TreeAnnotationResource res) {
        res.putPath(
                InUnlinkedGraphTopology.class,
                res.getCachedElement("Netzwerk"),
                res.getCachedElement("Topologie"),
                res.getCachedElement("Leere Topologie")
        );

        res.newEntryBuilder()
                .setGamsIdentifier("-")
                .setGamsDescription("Platzhalter")
                .store(InUnlinkedGraphTopology.class, "placeholderUnlinked");
    }

    public String _name;

    @FieldDefinition
    public double placeholderUnlinked;

    public InUnlinkedGraphTopology() {
    }

    public InUnlinkedGraphTopology(String name) {
        this._name = name;
    }

    @Override
    public String getName() {
        return _name;
    }

    public void setName(String name) {
        this._name = name;
    }

    @Override
    public UnlinkedGraphTopology parse(InputParser parser) throws ParsingException {
        return new UnlinkedGraphTopology(SocialGraph.Type.COMMUNICATION, getName());
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
