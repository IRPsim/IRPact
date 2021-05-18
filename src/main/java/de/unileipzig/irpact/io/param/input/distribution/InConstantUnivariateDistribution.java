package de.unileipzig.irpact.io.param.input.distribution;

import de.unileipzig.irpact.commons.distribution.ConstantUnivariateDoubleDistribution;
import de.unileipzig.irpact.commons.exception.ParsingException;
import de.unileipzig.irpact.core.log.IRPLogging;
import de.unileipzig.irpact.develop.Todo;
import de.unileipzig.irpact.io.param.input.IRPactInputParser;
import de.unileipzig.irpact.io.param.input.InputParser;
import de.unileipzig.irptools.defstructure.annotation.Definition;
import de.unileipzig.irptools.defstructure.annotation.FieldDefinition;
import de.unileipzig.irptools.util.CopyCache;
import de.unileipzig.irptools.util.TreeAnnotationResource;
import de.unileipzig.irptools.util.log.IRPLogger;

import java.lang.invoke.MethodHandles;
import java.util.Objects;

import static de.unileipzig.irpact.io.param.IOConstants.DISTRIBUTIONS;
import static de.unileipzig.irpact.io.param.ParamUtil.addEntry;
import static de.unileipzig.irpact.io.param.ParamUtil.putClassPath;

/**
 * @author Daniel Abitz
 */
@Todo("umbennen in DiracUnivariateDistribution, das dann auch in die oberflaeche uebernehmen")
@Definition
public class InConstantUnivariateDistribution implements InUnivariateDoubleDistribution {

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
        addEntry(res, thisClass(), "constDistValue");
    }

    private static final IRPLogger LOGGER = IRPLogging.getLogger(InConstantUnivariateDistribution.class);

    public String _name;

    @FieldDefinition
    public double constDistValue;

    public InConstantUnivariateDistribution() {
    }

    public InConstantUnivariateDistribution(String name, double value) {
        this._name = name;
        this.constDistValue = value;
    }

    @Override
    public InConstantUnivariateDistribution copy(CopyCache cache) {
        return cache.copyIfAbsent(this, this::newCopy);
    }

    public InConstantUnivariateDistribution newCopy(CopyCache cache) {
        InConstantUnivariateDistribution copy = new InConstantUnivariateDistribution();
        copy._name = _name;
        copy.constDistValue = constDistValue;
        return copy;
    }

    @Override
    public String getName() {
        return _name;
    }

    public double getConstDistValue() {
        return constDistValue;
    }

    @Override
    public ConstantUnivariateDoubleDistribution parse(IRPactInputParser parser) throws ParsingException {
        ConstantUnivariateDoubleDistribution dist = new ConstantUnivariateDoubleDistribution();
        dist.setName(getName());
        dist.setValue(getConstDistValue());
        return dist;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof InConstantUnivariateDistribution)) return false;
        InConstantUnivariateDistribution that = (InConstantUnivariateDistribution) o;
        return Double.compare(that.constDistValue, constDistValue) == 0 && Objects.equals(_name, that._name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(_name, constDistValue);
    }

    @Override
    public String toString() {
        return "InConstantUnivariateDistribution{" +
                "_name='" + _name + '\'' +
                ", constDistValue=" + constDistValue +
                '}';
    }
}
