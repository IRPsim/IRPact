package de.unileipzig.irpact.io.param.input.distribution;

import de.unileipzig.irpact.commons.distribution.SlowTruncatedNormalDistribution;
import de.unileipzig.irpact.commons.exception.ParsingException;
import de.unileipzig.irpact.commons.util.Rnd;
import de.unileipzig.irpact.core.logging.IRPLogging;
import de.unileipzig.irpact.core.logging.IRPSection;
import de.unileipzig.irpact.io.param.input.IRPactInputParser;
import de.unileipzig.irptools.defstructure.annotation.Definition;
import de.unileipzig.irptools.defstructure.annotation.FieldDefinition;
import de.unileipzig.irptools.util.CopyCache;
import de.unileipzig.irptools.util.TreeAnnotationResource;
import de.unileipzig.irptools.util.log.IRPLogger;

import java.lang.invoke.MethodHandles;

import static de.unileipzig.irpact.io.param.IOConstants.DISTRIBUTIONS;
import static de.unileipzig.irpact.io.param.ParamUtil.*;

/**
 * @author Daniel Abitz
 */
@Definition(ignore = true)
public class InSlowTruncatedNormalDistribution implements InUnivariateDoubleDistribution {

    private static final MethodHandles.Lookup L = MethodHandles.lookup();
    public static Class<?> thisClass() {
        return L.lookupClass();
    }
    public static String thisName() {
        return thisClass().getSimpleName();
    }
    public static boolean isIgnored() {
        if(thisClass().isAnnotationPresent(Definition.class)) {
            Definition def = thisClass().getDeclaredAnnotation(Definition.class);
            return def.ignore();
        } else {
            throw new IllegalArgumentException("missing definition annotation");
        }
    }

    public static void initRes(TreeAnnotationResource res) {
    }
    public static void applyRes(TreeAnnotationResource res) {
        if(isIgnored()) return;

        putClassPath(res, thisClass(), DISTRIBUTIONS, thisName());
        addEntry(res, thisClass(), "standardDeviation");
        addEntry(res, thisClass(), "mean");
        addEntry(res, thisClass(), "lowerBound");
        addEntry(res, thisClass(), "lowerBoundInclusive");
        addEntry(res, thisClass(), "upperBound");
        addEntry(res, thisClass(), "upperBoundInclusive");

        setDefault(res, thisClass(), "standardDeviation", varargs("1"));
        setDefault(res, thisClass(), "mean", varargs("0"));
        setDefault(res, thisClass(), "lowerBound", varargs("-1"));
        setDefault(res, thisClass(), "lowerBoundInclusive", VALUE_TRUE);
        setDefault(res, thisClass(), "upperBound", varargs("1"));
        setDefault(res, thisClass(), "upperBoundInclusive", VALUE_TRUE);
    }

    private static final IRPLogger LOGGER = IRPLogging.getLogger(InSlowTruncatedNormalDistribution.class);

    public String _name;

    @FieldDefinition
    public double standardDeviation;

    @FieldDefinition
    public double mean;

    @FieldDefinition
    public double lowerBound;

    @FieldDefinition
    public boolean lowerBoundInclusive;

    @FieldDefinition
    public double upperBound;

    @FieldDefinition
    public boolean upperBoundInclusive;

    public InSlowTruncatedNormalDistribution() {
    }

    public InSlowTruncatedNormalDistribution(
            String name,
            double standardDeviation,
            double mean,
            double lowerBound, boolean lowerBoundInclusive,
            double upperBound, boolean upperBoundInclusive) {
        setName(name);
        setStandardDeviation(standardDeviation);
        setMean(mean);
        setLowerBound(lowerBound);
        setUpperBound(upperBound);
        setLowerBoundInclusive(lowerBoundInclusive);
        setUpperBoundInclusive(upperBoundInclusive);
    }

    @Override
    public InSlowTruncatedNormalDistribution copy(CopyCache cache) {
        return cache.copyIfAbsent(this, this::newCopy);
    }

    public InSlowTruncatedNormalDistribution newCopy(CopyCache cache) {
        InSlowTruncatedNormalDistribution copy = new InSlowTruncatedNormalDistribution();
        copy._name = _name;
        copy.standardDeviation = standardDeviation;
        copy.mean = mean;
        copy.lowerBound = lowerBound;
        copy.lowerBoundInclusive = lowerBoundInclusive;
        copy.upperBound = upperBound;
        copy.upperBoundInclusive = upperBoundInclusive;
        return copy;
    }

    public void setName(String name) {
        this._name = name;
    }

    @Override
    public String getName() {
        return _name;
    }

    public double getStandardDeviation() {
        return standardDeviation;
    }

    public void setStandardDeviation(double standardDeviation) {
        this.standardDeviation = standardDeviation;
    }

    public double getMean() {
        return mean;
    }

    public void setMean(double mean) {
        this.mean = mean;
    }

    public double getLowerBound() {
        return lowerBound;
    }

    public void setLowerBound(double lowerBound) {
        this.lowerBound = lowerBound;
    }

    public double getUpperBound() {
        return upperBound;
    }

    public void setUpperBound(double upperBound) {
        this.upperBound = upperBound;
    }

    public void setLowerBoundInclusive(boolean lowerBoundInclusive) {
        this.lowerBoundInclusive = lowerBoundInclusive;
    }

    public boolean isLowerBoundInclusive() {
        return lowerBoundInclusive;
    }

    public void setUpperBoundInclusive(boolean upperBoundInclusive) {
        this.upperBoundInclusive = upperBoundInclusive;
    }

    public boolean isUpperBoundInclusive() {
        return upperBoundInclusive;
    }

    @Override
    public SlowTruncatedNormalDistribution parse(IRPactInputParser parser) throws ParsingException {
        SlowTruncatedNormalDistribution dist = new SlowTruncatedNormalDistribution();
        dist.setName(getName());
        Rnd rnd = parser.deriveRnd();
        dist.setRandom(rnd);
        dist.setStandardDeviation(getStandardDeviation());
        dist.setMean(getMean());
        dist.setUpperBound(getUpperBound());
        dist.setLowerBound(getLowerBound());
        dist.setLowerBoundInclusive(isLowerBoundInclusive());
        dist.setUpperBoundInclusive(isUpperBoundInclusive());
        LOGGER.trace(IRPSection.INITIALIZATION_PARAMETER, "SlowTruncatedNormalDistribution '{}' uses seed: {}", getName(), rnd.getInitialSeed());
        return dist;
    }
}
