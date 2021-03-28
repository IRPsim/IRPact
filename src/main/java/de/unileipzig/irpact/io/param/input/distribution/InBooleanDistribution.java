package de.unileipzig.irpact.io.param.input.distribution;

import de.unileipzig.irpact.commons.util.Rnd;
import de.unileipzig.irpact.commons.distribution.BooleanDistribution;
import de.unileipzig.irpact.commons.exception.ParsingException;
import de.unileipzig.irpact.core.log.IRPLogging;
import de.unileipzig.irpact.core.log.IRPSection;
import de.unileipzig.irpact.io.param.input.InputParser;
import de.unileipzig.irptools.defstructure.annotation.Definition;
import de.unileipzig.irptools.defstructure.annotation.FieldDefinition;
import de.unileipzig.irptools.util.TreeAnnotationResource;
import de.unileipzig.irptools.util.log.IRPLogger;

import java.lang.invoke.MethodHandles;
import java.util.Objects;

import static de.unileipzig.irpact.io.param.IOConstants.*;
import static de.unileipzig.irpact.io.param.ParamUtil.addEntry;
import static de.unileipzig.irpact.io.param.ParamUtil.putClassPath;

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
        addEntry(res, thisClass(), "placeholderBooleanDist");
    }

    private static final IRPLogger LOGGER = IRPLogging.getLogger(InBooleanDistribution.class);

    public String _name;

    @FieldDefinition
    public int placeholderBooleanDist;

    public InBooleanDistribution() {
    }

    public InBooleanDistribution(String name) {
        this._name = name;
    }

    @Override
    public String getName() {
        return _name;
    }

    @Override
    public Object parse(InputParser parser) throws ParsingException {
        BooleanDistribution dist = new BooleanDistribution();
        dist.setName(getName());
        Rnd rnd = parser.deriveRnd();
        dist.setRandom(rnd);
        LOGGER.debug(IRPSection.INITIALIZATION_PARAMETER, "BooleanDistribution '{}' uses seed: {}", getName(), rnd.getInitialSeed());
        return dist;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof InBooleanDistribution)) return false;
        InBooleanDistribution that = (InBooleanDistribution) o;
        return Objects.equals(_name, that._name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(_name);
    }

    @Override
    public String toString() {
        return "InConstantUnivariateDistribution{" +
                "_name='" + _name + '\'' +
                '}';
    }
}
