package de.unileipzig.irpact.io.input.network;

import de.unileipzig.irpact.commons.eval.Inverse;
import de.unileipzig.irpact.commons.spatial.BasicDistanceEvaluator;
import de.unileipzig.irpact.commons.spatial.DistanceEvaluator;
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
        res.newElementBuilder()
                .setEdnLabel("Invers")
                .setEdnPriority(1)
                .putCache("Invers");
    }
    public static void applyRes(TreeAnnotationResource res) {
        res.putPath(
                InInverse.class,
                res.getCachedElement("Netzwerk"),
                res.getCachedElement("Abstandsfunktion"),
                res.getCachedElement("Invers")
        );
    }

    public String _name;

    @FieldDefinition
    public int placeholderInverse;

    private BasicDistanceEvaluator instance = new BasicDistanceEvaluator(new Inverse());

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
    public DistanceEvaluator getInstance() {
        return instance;
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
