package de.unileipzig.irpact.io.param.input.distribution;

import de.unileipzig.irpact.commons.Rnd;
import de.unileipzig.irpact.commons.WeightedDouble;
import de.unileipzig.irpact.commons.distribution.FiniteMassPointsDiscreteDistribution;
import de.unileipzig.irpact.commons.exception.ParsingException;
import de.unileipzig.irpact.core.log.IRPLogging;
import de.unileipzig.irpact.core.log.IRPSection;
import de.unileipzig.irpact.io.param.ParamUtil;
import de.unileipzig.irpact.io.param.input.InputParser;
import de.unileipzig.irpact.util.Todo;
import de.unileipzig.irptools.defstructure.annotation.Definition;
import de.unileipzig.irptools.defstructure.annotation.FieldDefinition;
import de.unileipzig.irptools.util.TreeAnnotationResource;
import de.unileipzig.irptools.util.log.IRPLogger;

import java.lang.invoke.MethodHandles;
import java.util.Collection;

import static de.unileipzig.irpact.io.param.IOConstants.DISTRIBUTIONS;
import static de.unileipzig.irpact.io.param.ParamUtil.addEntry;
import static de.unileipzig.irpact.io.param.ParamUtil.putClassPath;

/**
 * @author Daniel Abitz
 */
@Definition
public class InFiniteMassPointsDiscreteDistribution implements InUnivariateDoubleDistribution {

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
        addEntry(res, thisClass(), "lowerBoundInt");
    }

    private static final IRPLogger LOGGER = IRPLogging.getLogger(InFiniteMassPointsDiscreteDistribution.class);

    public String _name;

    @FieldDefinition
    public InMassPoint[] massPoints;

    public InFiniteMassPointsDiscreteDistribution() {
    }

    public InFiniteMassPointsDiscreteDistribution(String name, Collection<? extends InMassPoint> massPoints) {
        setName(name);
        setMassPoints(massPoints);
    }

    @Override
    public String getName() {
        return _name;
    }

    public void setName(String _name) {
        this._name = _name;
    }

    public InMassPoint[] getMassPoints() throws ParsingException {
        return ParamUtil.getNonEmptyArray(massPoints, "massPoints");
    }

    public void setMassPoints(Collection<? extends InMassPoint> massPoints) {
        this.massPoints = massPoints.toArray(new InMassPoint[0]);
    }

    public void setMassPoints(InMassPoint[] massPoints) {
        this.massPoints = massPoints;
    }

    @Override
    public FiniteMassPointsDiscreteDistribution parse(InputParser parser) throws ParsingException {
        FiniteMassPointsDiscreteDistribution dist = new FiniteMassPointsDiscreteDistribution();
        dist.setName(getName());

        for(InMassPoint inMp: massPoints) {
            WeightedDouble mp = inMp.toWeightedDouble();
            dist.add(mp);
        }

        Rnd rnd = parser.deriveRnd();
        dist.setRandom(rnd);
        LOGGER.debug(IRPSection.INITIALIZATION_PARAMETER, "FiniteMassPointsDiscreteDistribution '{}' uses seed: {}", getName(), rnd.getInitialSeed());

        dist.init();
        return dist;
    }
}
