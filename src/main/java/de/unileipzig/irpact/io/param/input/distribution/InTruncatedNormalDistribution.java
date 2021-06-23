package de.unileipzig.irpact.io.param.input.distribution;

import de.unileipzig.irpact.commons.distribution.TruncatedNormalDistribution;
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
@Definition
public class InTruncatedNormalDistribution implements InUnivariateDoubleDistribution {

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
        addEntry(res, thisClass(), "standardDeviation");
        addEntry(res, thisClass(), "mean");
        addEntry(res, thisClass(), "lowerBound");
        addEntry(res, thisClass(), "upperBound");

        setDefault(res, thisClass(), "standardDeviation", varargs("1"));
        setDefault(res, thisClass(), "mean", varargs("0"));
        setDefault(res, thisClass(), "lowerBound", varargs("-1"));
        setDefault(res, thisClass(), "upperBound", varargs("1"));
    }

    private static final IRPLogger LOGGER = IRPLogging.getLogger(InTruncatedNormalDistribution.class);

    public String _name;

    @FieldDefinition
    public double standardDeviation;

    @FieldDefinition
    public double mean;

    @FieldDefinition
    public double lowerBound;

    @FieldDefinition
    public double upperBound;

    public InTruncatedNormalDistribution() {
    }

    public InTruncatedNormalDistribution(
            String name,
            double standardDeviation,
            double mean,
            double lowerBound,
            double upperBound) {
        setName(name);
        setStandardDeviation(standardDeviation);
        setMean(mean);
        setLowerBound(lowerBound);
        setUpperBound(upperBound);
    }

    @Override
    public InTruncatedNormalDistribution copy(CopyCache cache) {
        return cache.copyIfAbsent(this, this::newCopy);
    }

    public InTruncatedNormalDistribution newCopy(CopyCache cache) {
        InTruncatedNormalDistribution copy = new InTruncatedNormalDistribution();
        copy._name = _name;
        copy.standardDeviation = standardDeviation;
        copy.mean = mean;
        copy.lowerBound = lowerBound;
        copy.upperBound = upperBound;
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

    @Override
    public TruncatedNormalDistribution parse(IRPactInputParser parser) throws ParsingException {
        TruncatedNormalDistribution dist = new TruncatedNormalDistribution();
        dist.setName(getName());
        Rnd rnd = parser.deriveRnd();
        dist.setRandom(rnd);
        dist.setSigma(getStandardDeviation());
        dist.setMu(getMean());
        dist.setSupportUpperBound(getUpperBound());
        dist.setSupportLowerBound(getLowerBound());
        dist.initalize();
        LOGGER.trace(IRPSection.INITIALIZATION_PARAMETER, "TruncatedNormalDistribution '{}' uses seed: {}", getName(), rnd.getInitialSeed());
        return dist;
    }
}
