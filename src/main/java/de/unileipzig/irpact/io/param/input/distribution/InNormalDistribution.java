package de.unileipzig.irpact.io.param.input.distribution;

import de.unileipzig.irpact.commons.distribution.NormalDistribution;
import de.unileipzig.irpact.commons.exception.ParsingException;
import de.unileipzig.irpact.commons.util.Rnd;
import de.unileipzig.irpact.core.logging.IRPLogging;
import de.unileipzig.irpact.core.logging.IRPSection;
import de.unileipzig.irpact.core.start.IRPactInputParser;
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
public class InNormalDistribution implements InUnivariateDoubleDistribution {

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

        setDefault(res, thisClass(), "standardDeviation", new Object[] {"1"});
        setDefault(res, thisClass(), "mean", new Object[] {"0"});
    }

    private static final IRPLogger LOGGER = IRPLogging.getLogger(InNormalDistribution.class);

    public String _name;

    @FieldDefinition
    public double standardDeviation;

    @FieldDefinition
    public double mean;

    public InNormalDistribution() {
    }

    public InNormalDistribution(String name, double standardDeviation, double mean) {
        setName(name);
        setStandardDeviation(standardDeviation);
        setMean(mean);
    }

    @Override
    public InNormalDistribution copy(CopyCache cache) {
        return cache.copyIfAbsent(this, this::newCopy);
    }

    public InNormalDistribution newCopy(CopyCache cache) {
        InNormalDistribution copy = new InNormalDistribution();
        copy._name = _name;
        copy.standardDeviation = standardDeviation;
        copy.mean = mean;
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

    @Override
    public Object parse(IRPactInputParser parser) throws ParsingException {
        NormalDistribution dist = new NormalDistribution();
        dist.setName(getName());
        Rnd rnd = parser.deriveRnd();
        dist.setRandom(rnd);
        dist.setStandardDeviation(getStandardDeviation());
        dist.setMean(getMean());
        LOGGER.trace(IRPSection.INITIALIZATION_PARAMETER, "NormalDistribution '{}' uses seed: {}", getName(), rnd.getInitialSeed());
        return dist;
    }
}
