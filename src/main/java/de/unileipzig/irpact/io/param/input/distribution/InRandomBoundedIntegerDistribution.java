package de.unileipzig.irpact.io.param.input.distribution;

import de.unileipzig.irpact.commons.Rnd;
import de.unileipzig.irpact.commons.distribution.RandomBoundedIntegerDistribution;
import de.unileipzig.irpact.commons.exception.ParsingException;
import de.unileipzig.irpact.core.log.IRPLogging;
import de.unileipzig.irpact.core.log.IRPSection;
import de.unileipzig.irpact.io.param.input.InputParser;
import de.unileipzig.irptools.defstructure.annotation.Definition;
import de.unileipzig.irptools.defstructure.annotation.FieldDefinition;
import de.unileipzig.irptools.util.TreeAnnotationResource;
import de.unileipzig.irptools.util.log.IRPLogger;

import java.lang.invoke.MethodHandles;
import java.util.Objects;

import static de.unileipzig.irpact.io.param.IOConstants.DISTRIBUTIONS;
import static de.unileipzig.irpact.io.param.ParamUtil.addEntry;
import static de.unileipzig.irpact.io.param.ParamUtil.putClassPath;

/**
 * @author Daniel Abitz
 */
@Definition
public class InRandomBoundedIntegerDistribution implements InUnivariateDoubleDistribution {

    private static final MethodHandles.Lookup L = MethodHandles.lookup();
    public static Class<?> thisClass() {
        return L.lookupClass();
    }
    public static String thisName() {
        return thisClass().getSimpleName();
    }

    public static void initRes(TreeAnnotationResource res) {
    }
    public static void applyRes(TreeAnnotationResource res) {
        putClassPath(res, thisClass(), DISTRIBUTIONS, thisName());
        addEntry(res, thisClass(), "lowerBoundInt");
        addEntry(res, thisClass(), "upperBoundInt");
    }

    private static final IRPLogger LOGGER = IRPLogging.getLogger(InRandomBoundedIntegerDistribution.class);

    public String _name;

    @FieldDefinition
    public int lowerBoundInt;

    @FieldDefinition
    public int upperBoundInt;

    public InRandomBoundedIntegerDistribution() {
    }

    public InRandomBoundedIntegerDistribution(String name, int lowerBoundInt, int upperBoundInt) {
        this._name = name;
        this.lowerBoundInt = lowerBoundInt;
        this.upperBoundInt = upperBoundInt;
    }

    @Override
    public String getName() {
        return _name;
    }

    public int getLowerBound() {
        return lowerBoundInt;
    }

    public int getUpperBound() {
        return upperBoundInt;
    }

    @Override
    public RandomBoundedIntegerDistribution parse(InputParser parser) throws ParsingException {
        RandomBoundedIntegerDistribution dist = new RandomBoundedIntegerDistribution();
        dist.setName(getName());
        dist.setLowerBound(getLowerBound());
        dist.setUpperBound(getUpperBound());
        Rnd rnd = parser.deriveRnd();
        dist.setRandom(rnd);
        LOGGER.debug(IRPSection.INITIALIZATION_PARAMETER, "RandomBoundedIntegerDistribution '{}' uses seed: {}", getName(), rnd.getInitialSeed());
        return dist;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof InRandomBoundedIntegerDistribution)) return false;
        InRandomBoundedIntegerDistribution that = (InRandomBoundedIntegerDistribution) o;
        return Double.compare(that.lowerBoundInt, lowerBoundInt) == 0
                && Double.compare(that.upperBoundInt, upperBoundInt) == 0
                && Objects.equals(_name, that._name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(_name, lowerBoundInt, upperBoundInt);
    }

    @Override
    public String toString() {
        return "InConstantUnivariateDistribution{" +
                "_name='" + _name + '\'' +
                ", lowerBoundInt=" + lowerBoundInt +
                ", upperBoundInt=" + upperBoundInt +
                '}';
    }
}
