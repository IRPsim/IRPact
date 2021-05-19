package de.unileipzig.irpact.io.param.input.distribution;

import de.unileipzig.irpact.commons.util.Rnd;
import de.unileipzig.irpact.commons.distribution.BooleanDistribution;
import de.unileipzig.irpact.commons.exception.ParsingException;
import de.unileipzig.irpact.core.log.IRPLogging;
import de.unileipzig.irpact.core.log.IRPSection;
import de.unileipzig.irpact.io.param.input.IRPactInputParser;
import de.unileipzig.irptools.defstructure.annotation.Definition;
import de.unileipzig.irptools.defstructure.annotation.FieldDefinition;
import de.unileipzig.irptools.util.CopyCache;
import de.unileipzig.irptools.util.TreeAnnotationResource;
import de.unileipzig.irptools.util.log.IRPLogger;

import java.lang.invoke.MethodHandles;

import static de.unileipzig.irpact.io.param.IOConstants.*;
import static de.unileipzig.irpact.io.param.ParamUtil.*;

/**
 * @author Daniel Abitz
 */
@Definition
public class InBooleanDistribution implements InUnivariateDoubleDistribution {

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
        addEntry(res, thisClass(), "trueValue");
        addEntry(res, thisClass(), "falseValue");
    }

    private static final IRPLogger LOGGER = IRPLogging.getLogger(InBooleanDistribution.class);

    public String _name;

    @FieldDefinition
    public double trueValue;

    @FieldDefinition
    public double falseValue;

    public InBooleanDistribution() {
    }

    public InBooleanDistribution(String name) {
        this._name = name;
    }

    @Override
    public InBooleanDistribution copy(CopyCache cache) {
        return cache.copyIfAbsent(this, this::newCopy);
    }

    public InBooleanDistribution newCopy(CopyCache cache) {
        InBooleanDistribution copy = new InBooleanDistribution();
        copy._name = _name;
        copy.trueValue = trueValue;
        copy.falseValue = falseValue;
        return copy;
    }

    public void setName(String name) {
        this._name = name;
    }

    @Override
    public String getName() {
        return _name;
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
