package de.unileipzig.irpact.io.param.input.distribution;

import de.unileipzig.irpact.commons.util.Rnd;
import de.unileipzig.irpact.commons.distribution.BooleanDistribution;
import de.unileipzig.irpact.commons.exception.ParsingException;
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

import static de.unileipzig.irpact.io.param.input.TreeViewStructureEnum.DISTRIBUTIONS_BOOLEAN;
import static de.unileipzig.irptools.Constants.FALSE0;
import static de.unileipzig.irptools.Constants.TRUE1;

/**
 * @author Daniel Abitz
 */
@Definition
@LocalizedUiResource.PutClassPath(DISTRIBUTIONS_BOOLEAN)
public class InBooleanDistribution implements InUnivariateDoubleDistribution {

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

    private static final IRPLogger LOGGER = IRPLogging.getLogger(InBooleanDistribution.class);

    @DefinitionName
    public String name;

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

    public InBooleanDistribution() {
    }

    public InBooleanDistribution(String name) {
        this.name = name;
    }

    @Override
    public InBooleanDistribution copy(CopyCache cache) {
        return cache.copyIfAbsent(this, this::newCopy);
    }

    public InBooleanDistribution newCopy(CopyCache cache) {
        InBooleanDistribution copy = new InBooleanDistribution();
        copy.name = name;
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
        BooleanDistribution dist = new BooleanDistribution();
        dist.setName(getName());
        dist.setFalseValue(getFalseValue());
        dist.setTrueValue(getTrueValue());
        Rnd rnd = parser.deriveRnd();
        dist.setRandom(rnd);
        LOGGER.trace(IRPSection.INITIALIZATION_PARAMETER, "BooleanDistribution '{}' uses seed: {}", getName(), rnd.getInitialSeed());
        return dist;
    }
}
