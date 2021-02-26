package de.unileipzig.irpact.io.param.input.network;

import de.unileipzig.irpact.commons.exception.ParsingException;
import de.unileipzig.irpact.core.network.SocialGraph;
import de.unileipzig.irpact.core.network.topology.CompleteGraphTopology;
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
public class InCompleteGraphTopology implements InGraphTopologyScheme {

    //damit ich bei copy&paste nie mehr vergesse die Klasse anzupassen :)
    private static final MethodHandles.Lookup L = MethodHandles.lookup();
    public static Class<?> thisClass() {
        return L.lookupClass();
    }

    public static void initRes(TreeAnnotationResource res) {
    }
    public static void applyRes(TreeAnnotationResource res) {
        res.putPath(
                InCompleteGraphTopology.class,
                res.getCachedElement("Netzwerk"),
                res.getCachedElement("Topologie"),
                res.getCachedElement("Complete Topologie")
        );

        res.newEntryBuilder()
                .setGamsIdentifier("Initiale Kantengewicht")
                .setGamsDescription("Initiale Gewicht der Kanten")
                .store(InCompleteGraphTopology.class, "initialWeight");
    }

    public String _name;

    @FieldDefinition
    public double initialWeight;

    public InCompleteGraphTopology() {
    }

    public InCompleteGraphTopology(String name, double initialWeight) {
        this._name = name;
        this.initialWeight = initialWeight;
    }

    @Override
    public String getName() {
        return _name;
    }

    public double getInitialWeight() {
        return initialWeight;
    }

    @Override
    public CompleteGraphTopology parse(InputParser parser) throws ParsingException {
        return new CompleteGraphTopology(SocialGraph.Type.COMMUNICATION, getName(), getInitialWeight());
    }

    @Override
    public String toString() {
        return "InCompleteGraphTopology{" +
                "_name='" + _name + '\'' +
                ", initialWeight=" + initialWeight +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof InCompleteGraphTopology)) return false;
        InCompleteGraphTopology that = (InCompleteGraphTopology) o;
        return Double.compare(that.initialWeight, initialWeight) == 0 && Objects.equals(_name, that._name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(_name, initialWeight);
    }
}
