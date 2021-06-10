package de.unileipzig.irpact.io.param.input.distribution;

import de.unileipzig.irpact.commons.distribution.DiracUnivariateDoubleDistribution;
import de.unileipzig.irpact.commons.exception.ParsingException;
import de.unileipzig.irpact.core.logging.IRPLogging;
import de.unileipzig.irpact.io.param.input.IRPactInputParser;
import de.unileipzig.irptools.defstructure.annotation.Definition;
import de.unileipzig.irptools.defstructure.annotation.FieldDefinition;
import de.unileipzig.irptools.util.CopyCache;
import de.unileipzig.irptools.util.TreeAnnotationResource;
import de.unileipzig.irptools.util.log.IRPLogger;

import java.lang.invoke.MethodHandles;
import java.util.Objects;

import static de.unileipzig.irpact.io.param.IOConstants.DISTRIBUTIONS;
import static de.unileipzig.irpact.io.param.ParamUtil.*;

/**
 * @author Daniel Abitz
 */
@Definition
public class InDiracUnivariateDistribution implements InUnivariateDoubleDistribution {

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
        addEntry(res, thisClass(), "value");
    }

    private static final IRPLogger LOGGER = IRPLogging.getLogger(InDiracUnivariateDistribution.class);

    public String _name;

    @FieldDefinition
    public double value;

    public InDiracUnivariateDistribution() {
    }

    public InDiracUnivariateDistribution(String name, double value) {
        this._name = name;
        this.value = value;
    }

    @Override
    public InDiracUnivariateDistribution copy(CopyCache cache) {
        return cache.copyIfAbsent(this, this::newCopy);
    }

    public InDiracUnivariateDistribution newCopy(CopyCache cache) {
        InDiracUnivariateDistribution copy = new InDiracUnivariateDistribution();
        copy._name = _name;
        copy.value = value;
        return copy;
    }

    @Override
    public String getName() {
        return _name;
    }

    public double getValue() {
        return value;
    }

    @Override
    public DiracUnivariateDoubleDistribution parse(IRPactInputParser parser) throws ParsingException {
        DiracUnivariateDoubleDistribution dist = new DiracUnivariateDoubleDistribution();
        dist.setName(getName());
        dist.setValue(getValue());
        return dist;
    }
}
