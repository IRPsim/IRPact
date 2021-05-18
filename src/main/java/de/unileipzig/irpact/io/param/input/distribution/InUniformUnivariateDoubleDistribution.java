package de.unileipzig.irpact.io.param.input.distribution;

import de.unileipzig.irpact.commons.distribution.UniformUnivariateDoubleDistribution;
import de.unileipzig.irpact.commons.exception.ParsingException;
import de.unileipzig.irpact.commons.util.Rnd;
import de.unileipzig.irpact.core.log.IRPLogging;
import de.unileipzig.irpact.core.log.IRPSection;
import de.unileipzig.irpact.develop.AddToRoot;
import de.unileipzig.irpact.io.param.input.IRPactInputParser;
import de.unileipzig.irptools.defstructure.annotation.Definition;
import de.unileipzig.irptools.defstructure.annotation.FieldDefinition;
import de.unileipzig.irptools.util.CopyCache;
import de.unileipzig.irptools.util.TreeAnnotationResource;
import de.unileipzig.irptools.util.log.IRPLogger;

import java.lang.invoke.MethodHandles;

import static de.unileipzig.irpact.io.param.IOConstants.DISTRIBUTIONS;
import static de.unileipzig.irpact.io.param.ParamUtil.addEntry;
import static de.unileipzig.irpact.io.param.ParamUtil.putClassPath;

/**
 * @author Daniel Abitz
 */
@AddToRoot
@Definition
public class InUniformUnivariateDoubleDistribution implements InUnivariateDoubleDistribution {

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
        addEntry(res, thisClass(), "lowerBound");
        addEntry(res, thisClass(), "upperBound");
    }

    private static final IRPLogger LOGGER = IRPLogging.getLogger(InUniformUnivariateDoubleDistribution.class);

    public String _name;

    @FieldDefinition
    public double lowerBound;

    @FieldDefinition
    public double upperBound;

    public InUniformUnivariateDoubleDistribution() {
    }

    public InUniformUnivariateDoubleDistribution(String name, int lowerBound, int upperBoundInt) {
        this._name = name;
        this.lowerBound = lowerBound;
        this.upperBound = upperBoundInt;
    }

    @Override
    public InUniformUnivariateDoubleDistribution copy(CopyCache cache) {
        return cache.copyIfAbsent(this, this::newCopy);
    }

    public InUniformUnivariateDoubleDistribution newCopy(CopyCache cache) {
        InUniformUnivariateDoubleDistribution copy = new InUniformUnivariateDoubleDistribution();
        copy._name = _name;
        copy.lowerBound = lowerBound;
        copy.upperBound = upperBound;
        return copy;
    }

    @Override
    public String getName() {
        return _name;
    }

    public double getLowerBound() {
        return lowerBound;
    }

    public double getUpperBound() {
        return upperBound;
    }

    @Override
    public UniformUnivariateDoubleDistribution parse(IRPactInputParser parser) throws ParsingException {
        UniformUnivariateDoubleDistribution dist = new UniformUnivariateDoubleDistribution();
        dist.setName(getName());
        dist.setLowerBound(getLowerBound());
        dist.setUpperBound(getUpperBound());
        Rnd rnd = parser.deriveRnd();
        dist.setRandom(rnd);
        LOGGER.trace(IRPSection.INITIALIZATION_PARAMETER, "UniformUnivariateDoubleDistribution '{}' uses seed: {}", getName(), rnd.getInitialSeed());
        return dist;
    }
}
