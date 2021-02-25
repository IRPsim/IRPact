package de.unileipzig.irpact.io.param.input.network;

import de.unileipzig.irpact.commons.eval.Inverse;
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
public class InInverse implements InDistanceEvaluator {

    public static void initRes(TreeAnnotationResource res) {
    }
    public static void applyRes(TreeAnnotationResource res) {
        res.putPath(
                InInverse.class,
                res.getCachedElement("Netzwerk"),
                res.getCachedElement("Abstandsfunktion"),
                res.getCachedElement("Invers")
        );

        res.newEntryBuilder()
                .setGamsIdentifier("-")
                .setGamsDescription("Platzhalter")
                .store(InUnlinkedGraphTopology.class, "placeholderInverse");
    }

    public String _name;

    @FieldDefinition
    public int placeholderInverse;

    public InInverse() {
    }

    public InInverse(String name) {
        this._name = name;
    }

    @Override
    public String getName() {
        return _name;
    }

    @Override
    public BasicDistanceEvaluator parse(InputParser parser) throws ParsingException {
        return new BasicDistanceEvaluator(new Inverse());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof InInverse)) return false;
        InInverse inInverse = (InInverse) o;
        return placeholderInverse == inInverse.placeholderInverse && Objects.equals(_name, inInverse._name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(_name, placeholderInverse);
    }

    @Override
    public String toString() {
        return "InInverse{" +
                "_name='" + _name + '\'' +
                ", placeholderInverse=" + placeholderInverse +
                '}';
    }
}
