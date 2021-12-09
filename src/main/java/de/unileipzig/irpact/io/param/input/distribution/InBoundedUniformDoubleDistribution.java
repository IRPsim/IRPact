package de.unileipzig.irpact.io.param.input.distribution;

import de.unileipzig.irpact.commons.distribution.BoundedUniformDoubleDistribution;
import de.unileipzig.irpact.commons.exception.ParsingException;
import de.unileipzig.irpact.commons.util.Rnd;
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

import static de.unileipzig.irpact.io.param.input.TreeViewStructureEnum.DISTRIBUTIONS_BOUNDUNIDOUBLE;

/**
 * @author Daniel Abitz
 */
@Definition
@LocalizedUiResource.PutClassPath(DISTRIBUTIONS_BOUNDUNIDOUBLE)
public class InBoundedUniformDoubleDistribution implements InUnivariateDoubleDistribution {

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
    }

    private static final IRPLogger LOGGER = IRPLogging.getLogger(InBoundedUniformDoubleDistribution.class);

    @DefinitionName
    public String name;

    @FieldDefinition
    @LocalizedUiResource.AddEntry
    public double lowerBound;

    @FieldDefinition
    @LocalizedUiResource.AddEntry
    public double upperBound;

    public InBoundedUniformDoubleDistribution() {
    }

    public InBoundedUniformDoubleDistribution(String name, int lowerBound, int upperBoundInt) {
        this.name = name;
        this.lowerBound = lowerBound;
        this.upperBound = upperBoundInt;
    }

    @Override
    public InBoundedUniformDoubleDistribution copy(CopyCache cache) {
        return cache.copyIfAbsent(this, this::newCopy);
    }

    public InBoundedUniformDoubleDistribution newCopy(CopyCache cache) {
        InBoundedUniformDoubleDistribution copy = new InBoundedUniformDoubleDistribution();
        copy.name = name;
        copy.lowerBound = lowerBound;
        copy.upperBound = upperBound;
        return copy;
    }

    @Override
    public String getName() {
        return name;
    }

    public double getLowerBound() {
        return lowerBound;
    }

    public double getUpperBound() {
        return upperBound;
    }

    @Override
    public BoundedUniformDoubleDistribution parse(IRPactInputParser parser) throws ParsingException {
        BoundedUniformDoubleDistribution dist = new BoundedUniformDoubleDistribution();
        dist.setName(getName());
        dist.setLowerBound(getLowerBound());
        dist.setUpperBound(getUpperBound());
        Rnd rnd = parser.deriveRnd();
        dist.setRandom(rnd);
        LOGGER.trace(IRPSection.INITIALIZATION_PARAMETER, "UniformUnivariateDoubleDistribution '{}' uses seed: {}", getName(), rnd.getInitialSeed());
        return dist;
    }
}
