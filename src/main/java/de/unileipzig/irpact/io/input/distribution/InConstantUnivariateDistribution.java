package de.unileipzig.irpact.io.input.distribution;

import de.unileipzig.irpact.commons.Rnd;
import de.unileipzig.irpact.commons.distribution.ConstantUnivariateDoubleDistribution;
import de.unileipzig.irpact.commons.distribution.UnivariateDoubleDistribution;
import de.unileipzig.irptools.defstructure.annotation.Definition;
import de.unileipzig.irptools.defstructure.annotation.FieldDefinition;
import de.unileipzig.irptools.util.TreeAnnotationResource;

import java.util.Objects;

/**
 * @author Daniel Abitz
 */
@Definition
public class InConstantUnivariateDistribution implements InUnivariateDoubleDistribution {

    public static void initRes(TreeAnnotationResource res) {
        res.newElementBuilder()
                .setEdnLabel("Dirac")
                .setEdnPriority(0)
                .setEdnDescription("Verteilungsfunktion, welche einen konstanten Wert zurück gibt.")
                .putCache("Dirac");
    }
    public static void applyRes(TreeAnnotationResource res) {
        res.putPath(
                InConstantUnivariateDistribution.class,
                res.getCachedElement("Verteilungsfunktionen"),
                res.getCachedElement("Dirac")
        );

        res.newEntryBuilder()
                .setGamsIdentifier("Wert")
                .setGamsDescription("Wert")
                .store(InConstantUnivariateDistribution.class, "constDistValue");
    }

    public String _name;

    @FieldDefinition
    public double constDistValue;

    private ConstantUnivariateDoubleDistribution instance;

    public InConstantUnivariateDistribution() {
    }

    public InConstantUnivariateDistribution(String name, double value) {
        this._name = name;
        this.constDistValue = value;
    }

    @Override
    public String getName() {
        return _name;
    }

    @Override
    public UnivariateDoubleDistribution getInstance(Rnd rnd) {
        if(instance == null) {
            instance = new ConstantUnivariateDoubleDistribution();
            instance.setName(getName());
            instance.setValue(getConstDistValue());
        }
        return instance;
    }

    public double getConstDistValue() {
        return constDistValue;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof InConstantUnivariateDistribution)) return false;
        InConstantUnivariateDistribution that = (InConstantUnivariateDistribution) o;
        return Double.compare(that.constDistValue, constDistValue) == 0 && Objects.equals(_name, that._name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(_name, constDistValue);
    }

    @Override
    public String toString() {
        return "InConstantUnivariateDistribution{" +
                "_name='" + _name + '\'' +
                ", constDistValue=" + constDistValue +
                '}';
    }
}
