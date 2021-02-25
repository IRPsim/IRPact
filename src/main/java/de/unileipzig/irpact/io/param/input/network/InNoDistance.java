package de.unileipzig.irpact.io.param.input.network;

import de.unileipzig.irpact.commons.eval.NoDistance;
import de.unileipzig.irpact.commons.exception.ParsingException;
import de.unileipzig.irpact.commons.spatial.BasicDistanceEvaluator;
import de.unileipzig.irpact.io.param.input.InputParser;
import de.unileipzig.irptools.defstructure.annotation.Definition;
import de.unileipzig.irptools.defstructure.annotation.FieldDefinition;
import de.unileipzig.irptools.util.TreeAnnotationResource;

import java.util.Objects;

/**
 * @author Daniel Abitz
 */
@Definition
public class InNoDistance implements InDistanceEvaluator {

    public static void initRes(TreeAnnotationResource res) {
    }
    public static void applyRes(TreeAnnotationResource res) {
        res.putPath(
                InNoDistance.class,
                res.getCachedElement("Netzwerk"),
                res.getCachedElement("Abstandsfunktion"),
                res.getCachedElement("NoDistance")
        );

        res.newEntryBuilder()
                .setGamsIdentifier("-")
                .setGamsDescription("Platzhalter")
                .store(InUnlinkedGraphTopology.class, "placeholderNoDistance");
    }

    public String _name;

    @FieldDefinition
    public int placeholderNoDistance;

    public InNoDistance() {
    }

    public InNoDistance(String name) {
        this._name = name;
    }

    @Override
    public String getName() {
        return _name;
    }

    @Override
    public BasicDistanceEvaluator parse(InputParser parser) throws ParsingException {
        return new BasicDistanceEvaluator(new NoDistance());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof InNoDistance)) return false;
        InNoDistance that = (InNoDistance) o;
        return placeholderNoDistance == that.placeholderNoDistance && Objects.equals(_name, that._name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(_name, placeholderNoDistance);
    }

    @Override
    public String toString() {
        return "InNoDistance{" +
                "_name='" + _name + '\'' +
                ", placeholderNoDistance=" + placeholderNoDistance +
                '}';
    }
}
