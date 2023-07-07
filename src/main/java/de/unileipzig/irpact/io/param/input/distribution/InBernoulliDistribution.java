package de.unileipzig.irpact.io.param.input.distribution;

import de.unileipzig.irpact.commons.distribution.BernoulliDistribution;
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

import static de.unileipzig.irpact.io.param.input.TreeViewStructureEnum.DISTRIBUTIONS_BERNOULLI;
import static de.unileipzig.irptools.Constants.*;

/**
 * @author Daniel Abitz
 */
@Definition
@LocalizedUiResource.PutClassPath(DISTRIBUTIONS_BERNOULLI)
public class InBernoulliDistribution implements InUnivariateDoubleDistribution {

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

    private static final IRPLogger LOGGER = IRPLogging.getLogger(InBernoulliDistribution.class);

    @DefinitionName
    public String name;

    @FieldDefinition
    @LocalizedUiResource.AddEntry
    public double p;

    @FieldDefinition(
            gams = @GamsParameter(
                    defaultValue = TRUE1
            )
    )
    @LocalizedUiResource.AddEntry
    public double trueValue;

    @FieldDefinition(
            gams = @GamsParameter(
                    defaultValue = FALSE0
            )
    )
    @LocalizedUiResource.AddEntry
    public double falseValue;

    public InBernoulliDistribution() {
    }

    public InBernoulliDistribution(String name, double p) {
        this(name, p, 1, 0);
    }

    public InBernoulliDistribution(String name, double p, double trueValue, double falseValue) {
        setName(name);
        setP(p);
        setTrueValue(trueValue);
        setFalseValue(falseValue);
    }

    @Override
    public InBernoulliDistribution copy(CopyCache cache) {
        return cache.copyIfAbsent(this, this::newCopy);
    }

    public InBernoulliDistribution newCopy(CopyCache cache) {
        InBernoulliDistribution copy = new InBernoulliDistribution();
        copy.name = name;
        copy.p = p;
        copy.trueValue = trueValue;
        copy.falseValue = falseValue;
        return copy;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String getName() {
        return name;
    }

    public double getP() {
        return p;
    }

    public void setP(double p) {
        this.p = p;
    }

    public void setTrueValue(double trueValue) {
        this.trueValue = trueValue;
    }

    public double getTrueValue() {
        return trueValue;
    }

    public void setFalseValue(double falseValue) {
        this.falseValue = falseValue;
    }

    public double getFalseValue() {
        return falseValue;
    }

    @Override
    public Object parse(IRPactInputParser parser) throws ParsingException {
        BernoulliDistribution dist = new BernoulliDistribution();
        dist.setName(getName());
        Rnd rnd = parser.deriveRnd();
        dist.setRandom(rnd);
        dist.setP(getP());
        dist.setTrueValue(getTrueValue());
        dist.setFalseValue(getFalseValue());
        LOGGER.trace(IRPSection.INITIALIZATION_PARAMETER, "BernoulliDistribution '{}' uses seed: {}", getName(), rnd.getInitialSeed());
        return dist;
    }
}
