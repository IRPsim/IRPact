package de.unileipzig.irpact.io.param.input.distribution;

import de.unileipzig.irpact.commons.distribution.ConstantUnivariateDoubleDistribution;
import de.unileipzig.irpact.commons.exception.ParsingException;
import de.unileipzig.irpact.core.log.IRPLogging;
import de.unileipzig.irpact.io.param.input.InputParser;
import de.unileipzig.irptools.defstructure.annotation.Definition;
import de.unileipzig.irptools.defstructure.annotation.FieldDefinition;
import de.unileipzig.irptools.util.TreeAnnotationResource;
import de.unileipzig.irptools.util.log.IRPLogger;

import java.lang.invoke.MethodHandles;
import java.util.Objects;

/**
 * @author Daniel Abitz
 */
@Definition
public class InConstantUnivariateDistribution implements InUnivariateDoubleDistribution {

    //damit ich bei copy&paste nie mehr vergesse die Klasse anzupassen :)
    private static final MethodHandles.Lookup L = MethodHandles.lookup();
    public static Class<?> thisClass() {
        return L.lookupClass();
    }

    public static void initRes(TreeAnnotationResource res) {
    }
    public static void applyRes(TreeAnnotationResource res) {
        res.putPath(
                InConstantUnivariateDistribution.class,
                res.getCachedElement("Verteilungsfunktionen"),
                res.getCachedElement("Dirac")
        );

        res.newEntryBuilder()
                .setGamsIdentifier("Wert")
                .setGamsDescription("Konstante Rückgabewert")
                .store(InConstantUnivariateDistribution.class, "constDistValue");
    }

    private static final IRPLogger LOGGER = IRPLogging.getLogger(InConstantUnivariateDistribution.class);

    public String _name;

    @FieldDefinition
    public double constDistValue;

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

    public double getConstDistValue() {
        return constDistValue;
    }

    @Override
    public ConstantUnivariateDoubleDistribution parse(InputParser parser) throws ParsingException {
        ConstantUnivariateDoubleDistribution dist = new ConstantUnivariateDoubleDistribution();
        dist.setName(getName());
        dist.setValue(getConstDistValue());
        return dist;
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
