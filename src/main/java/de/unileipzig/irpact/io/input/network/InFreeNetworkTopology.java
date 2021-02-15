package de.unileipzig.irpact.io.input.network;

import de.unileipzig.irpact.io.input.distribution.InConstantUnivariateDistribution;
import de.unileipzig.irptools.defstructure.annotation.Definition;
import de.unileipzig.irptools.defstructure.annotation.FieldDefinition;
import de.unileipzig.irptools.util.TreeAnnotationResource;

import java.util.Arrays;
import java.util.Objects;

/**
 * @author Daniel Abitz
 */
@Definition
public class InFreeNetworkTopology implements InGraphTopologyScheme {

    public static void initRes(TreeAnnotationResource res) {
        res.newElementBuilder()
                .setEdnLabel("Freie Topologie")
                .setEdnPriority(0)
                .setEdnDescription("Freie Topologie")
                .putCache("Freie Topologie");
    }
    public static void applyRes(TreeAnnotationResource res) {
        res.putPath(
                InFreeNetworkTopology.class,
                res.getCachedElement("Netzwerk"),
                res.getCachedElement("Topologie"),
                res.getCachedElement("Freie Topologie")
        );

        res.newEntryBuilder()
                .setGamsIdentifier("Legt den Evaluator für die Abstände zwischen den Agenten fest.")
                .setGamsDescription("Evaluator für Abstände")
                .store(InFreeNetworkTopology.class, "distanceEvaluator");

        res.newEntryBuilder()
                .setGamsIdentifier("Knotenanzahl je KG")
                .setGamsDescription("Knotenanzahl")
                .store(InFreeNetworkTopology.class, "numberOfTies");
    }

    public String _name;

    @FieldDefinition
    public InDistanceEvaluator[] distanceEvaluator;

    @FieldDefinition
    public InNumberOfTies[] numberOfTies;

    @FieldDefinition
    public double initialWeight;

    public InFreeNetworkTopology() {
    }

    public InFreeNetworkTopology(String name, InDistanceEvaluator evaluator, InNumberOfTies[] numberOfTies, double initialWeight) {
        this._name = name;
        this.distanceEvaluator = new InDistanceEvaluator[]{evaluator};
        this.numberOfTies = numberOfTies;
        this.initialWeight = initialWeight;
    }

    public String getName() {
        return _name;
    }

    public InDistanceEvaluator getDistanceEvaluator() {
        return distanceEvaluator[0];
    }

    public InNumberOfTies[] getNumberOfTies() {
        return numberOfTies;
    }

    public double getInitialWeight() {
        return initialWeight;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof InFreeNetworkTopology)) return false;
        InFreeNetworkTopology topology = (InFreeNetworkTopology) o;
        return Double.compare(topology.initialWeight, initialWeight) == 0 && Objects.equals(_name, topology._name) && Arrays.equals(distanceEvaluator, topology.distanceEvaluator) && Arrays.equals(numberOfTies, topology.numberOfTies);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(_name, initialWeight);
        result = 31 * result + Arrays.hashCode(distanceEvaluator);
        result = 31 * result + Arrays.hashCode(numberOfTies);
        return result;
    }

    @Override
    public String toString() {
        return "InFreeNetworkTopology{" +
                "_name='" + _name + '\'' +
                ", distanceEvaluator=" + Arrays.toString(distanceEvaluator) +
                ", numberOfTies=" + Arrays.toString(numberOfTies) +
                ", initialWeight=" + initialWeight +
                '}';
    }
}
