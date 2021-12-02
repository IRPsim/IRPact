package de.unileipzig.irpact.io.param.input.distribution;

import de.unileipzig.irpact.commons.distribution.TruncatedNormalDistribution;
import de.unileipzig.irpact.commons.exception.ParsingException;
import de.unileipzig.irpact.commons.util.Rnd;
import de.unileipzig.irpact.core.logging.IRPLogging;
import de.unileipzig.irpact.core.logging.IRPSection;
import de.unileipzig.irpact.core.start.IRPactInputParser;
import de.unileipzig.irpact.io.param.LocalizedUiResource;
import de.unileipzig.irptools.defstructure.annotation.Definition;
import de.unileipzig.irptools.defstructure.annotation.DefinitionName;
import de.unileipzig.irptools.defstructure.annotation.FieldDefinition;
import de.unileipzig.irptools.defstructure.annotation.GamsParameter;
import de.unileipzig.irptools.util.CopyCache;
import de.unileipzig.irptools.util.TreeAnnotationResource;
import de.unileipzig.irptools.util.log.IRPLogger;

import java.lang.invoke.MethodHandles;

import static de.unileipzig.irpact.io.param.ParamUtil.*;
import static de.unileipzig.irpact.io.param.input.TreeViewStructureEnum.DISTRIBUTIONS_TRUNCNORM;

/**
 * @author Daniel Abitz
 */
@Definition
@LocalizedUiResource.PutClassPath(DISTRIBUTIONS_TRUNCNORM)
public class InTruncatedNormalDistribution implements InUnivariateDoubleDistribution {

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

    private static final IRPLogger LOGGER = IRPLogging.getLogger(InTruncatedNormalDistribution.class);

    @DefinitionName
    public String name;

    @FieldDefinition(
            gams = @GamsParameter(
                    defaultValue = VALUE1
            )
    )
    @LocalizedUiResource.AddEntry
    public double standardDeviation;

    @FieldDefinition(
            gams = @GamsParameter(
                    defaultValue = VALUE0
            )
    )
    @LocalizedUiResource.AddEntry
    public double mean;

    @FieldDefinition(
            gams = @GamsParameter(
                    defaultValue = VALUENEG1
            )
    )
    @LocalizedUiResource.AddEntry
    public double lowerBound;

    @FieldDefinition(
            gams = @GamsParameter(
                    defaultValue = VALUE1
            )
    )
    @LocalizedUiResource.AddEntry
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
        copy.name = name;
        copy.standardDeviation = standardDeviation;
        copy.mean = mean;
        copy.lowerBound = lowerBound;
        copy.upperBound = upperBound;
        return copy;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String getName() {
        return name;
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
