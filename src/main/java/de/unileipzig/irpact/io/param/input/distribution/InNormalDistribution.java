package de.unileipzig.irpact.io.param.input.distribution;

import de.unileipzig.irpact.commons.distribution.NormalDistribution;
import de.unileipzig.irpact.commons.distribution.RoundingMode;
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
import static de.unileipzig.irpact.io.param.input.TreeViewStructureEnum.DISTRIBUTIONS_NORM;

/**
 * @author Daniel Abitz
 */
@Definition
@LocalizedUiResource.PutClassPath(DISTRIBUTIONS_NORM)
@LocalizedUiResource.XorWithoutUnselectRule
public class InNormalDistribution implements InUnivariateDoubleDistribution {

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

    private static final IRPLogger LOGGER = IRPLogging.getLogger(InNormalDistribution.class);

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

    @FieldDefinition
    @LocalizedUiResource.AddEntry
    @LocalizedUiResource.SimpleSet(
            boolDomain = true,
            boolDefault = true
    )
    @LocalizedUiResource.XorWithoutUnselectRuleEntry
    public boolean modeNoRounding = true;

    @FieldDefinition
    @LocalizedUiResource.AddEntry
    @LocalizedUiResource.SimpleSet(
            boolDomain = true
    )
    @LocalizedUiResource.XorWithoutUnselectRuleEntry
    public boolean modeFloor = false;

    @FieldDefinition
    @LocalizedUiResource.AddEntry
    @LocalizedUiResource.SimpleSet(
            boolDomain = true
    )
    @LocalizedUiResource.XorWithoutUnselectRuleEntry
    public boolean modeCeil = false;

    @FieldDefinition
    @LocalizedUiResource.AddEntry
    @LocalizedUiResource.SimpleSet(
            boolDomain = true
    )
    @LocalizedUiResource.XorWithoutUnselectRuleEntry
    public boolean modeRound = false;

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
        copy.name = name;
        copy.standardDeviation = standardDeviation;
        copy.mean = mean;
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

    public RoundingMode getRoundingMode() {
        return RoundingMode.get(
                modeNoRounding,
                modeFloor,
                modeCeil,
                modeRound
        );
    }

    public void setRoundingMode(RoundingMode mode) {
        modeNoRounding = false;
        modeFloor = false;
        modeCeil = false;
        modeRound = false;

        switch (mode) {
            case NONE:
                modeNoRounding = true;
                break;

            case FLOOR:
                modeFloor = true;
                break;

            case CEIL:
                modeCeil = true;
                break;

            case ROUND:
                modeRound = true;
                break;
        }
    }

    @Override
    public Object parse(IRPactInputParser parser) throws ParsingException {
        NormalDistribution dist = new NormalDistribution();
        dist.setName(getName());
        Rnd rnd = parser.deriveRnd();
        dist.setRandom(rnd);
        dist.setStandardDeviation(getStandardDeviation());
        dist.setMean(getMean());
        dist.setRoundingMode(getRoundingMode());
        LOGGER.trace(IRPSection.INITIALIZATION_PARAMETER, "NormalDistribution '{}' uses seed: {}", getName(), rnd.getInitialSeed());
        return dist;
    }
}
