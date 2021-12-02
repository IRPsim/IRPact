package de.unileipzig.irpact.io.param.input.distribution;

import de.unileipzig.irpact.commons.util.Rnd;
import de.unileipzig.irpact.commons.distribution.BoundedUniformIntegerDistribution;
import de.unileipzig.irpact.commons.exception.ParsingException;
import de.unileipzig.irpact.core.logging.IRPLogging;
import de.unileipzig.irpact.core.logging.IRPSection;
import de.unileipzig.irpact.core.start.IRPactInputParser;
import de.unileipzig.irpact.io.param.LocalizedUiResource;
import de.unileipzig.irptools.defstructure.annotation.Definition;
import de.unileipzig.irptools.defstructure.annotation.DefinitionName;
import de.unileipzig.irptools.defstructure.annotation.FieldDefinition;
import de.unileipzig.irptools.util.CopyCache;
import de.unileipzig.irptools.util.TreeAnnotationResource;
import de.unileipzig.irptools.util.log.IRPLogger;

import java.lang.invoke.MethodHandles;

import static de.unileipzig.irpact.io.param.IOConstants.DISTRIBUTIONS;
import static de.unileipzig.irpact.io.param.ParamUtil.*;
import static de.unileipzig.irpact.io.param.input.TreeViewStructureEnum.DISTRIBUTIONS_BOUNDUNIINT;

/**
 * @author Daniel Abitz
 */
@Definition
@LocalizedUiResource.PutClassPath(DISTRIBUTIONS_BOUNDUNIINT)
public class InBoundedUniformIntegerDistribution implements InUnivariateDoubleDistribution {

    private static final MethodHandles.Lookup L = MethodHandles.lookup();
    public static Class<?> thisClass() {
        return L.lookupClass();
    }
    public static String thisName() {
        return thisClass().getSimpleName();
    }

    @TreeAnnotationResource.Init
    public static void initRes(TreeAnnotationResource res) {
    }
    @TreeAnnotationResource.Apply
    public static void applyRes(TreeAnnotationResource res) {
        putClassPath(res, thisClass(), DISTRIBUTIONS, thisName());
    }

    private static final IRPLogger LOGGER = IRPLogging.getLogger(InBoundedUniformIntegerDistribution.class);

    @DefinitionName
    public String name;

    @FieldDefinition
    @LocalizedUiResource.AddEntry
    public int lowerBound;

    @FieldDefinition
    @LocalizedUiResource.AddEntry
    public int upperBound;

    public InBoundedUniformIntegerDistribution() {
    }

    public InBoundedUniformIntegerDistribution(String name, int lowerBound, int upperBound) {
        this.name = name;
        this.lowerBound = lowerBound;
        this.upperBound = upperBound;
    }

    @Override
    public InBoundedUniformIntegerDistribution copy(CopyCache cache) {
        return cache.copyIfAbsent(this, this::newCopy);
    }

    public InBoundedUniformIntegerDistribution newCopy(CopyCache cache) {
        InBoundedUniformIntegerDistribution copy = new InBoundedUniformIntegerDistribution();
        copy.name = name;
        copy.lowerBound = lowerBound;
        copy.upperBound = upperBound;
        return copy;
    }

    @Override
    public String getName() {
        return name;
    }

    public int getLowerBound() {
        return lowerBound;
    }

    public int getUpperBound() {
        return upperBound;
    }

    @Override
    public BoundedUniformIntegerDistribution parse(IRPactInputParser parser) throws ParsingException {
        BoundedUniformIntegerDistribution dist = new BoundedUniformIntegerDistribution();
        dist.setName(getName());
        dist.setLowerBound(getLowerBound());
        dist.setUpperBound(getUpperBound());
        Rnd rnd = parser.deriveRnd();
        dist.setRandom(rnd);
        LOGGER.trace(IRPSection.INITIALIZATION_PARAMETER, "RandomBoundedIntegerDistribution '{}' uses seed: {}", getName(), rnd.getInitialSeed());
        return dist;
    }
}
