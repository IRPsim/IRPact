package de.unileipzig.irpact.io.param.input.distribution;

import de.unileipzig.irpact.commons.distribution.FiniteMassPointsDiscreteDistribution;
import de.unileipzig.irpact.commons.exception.ParsingException;
import de.unileipzig.irpact.commons.util.Rnd;
import de.unileipzig.irpact.commons.util.data.weighted.WeightedDouble;
import de.unileipzig.irpact.core.logging.IRPLogging;
import de.unileipzig.irpact.core.logging.IRPSection;
import de.unileipzig.irpact.core.start.IRPactInputParser;
import de.unileipzig.irpact.io.param.LocalizedUiResource;
import de.unileipzig.irpact.io.param.ParamUtil;
import de.unileipzig.irptools.defstructure.annotation.Definition;
import de.unileipzig.irptools.defstructure.annotation.DefinitionName;
import de.unileipzig.irptools.defstructure.annotation.FieldDefinition;
import de.unileipzig.irptools.util.CopyCache;
import de.unileipzig.irptools.util.TreeAnnotationResource;
import de.unileipzig.irptools.util.log.IRPLogger;

import java.lang.invoke.MethodHandles;
import java.util.Collection;

import static de.unileipzig.irpact.io.param.input.TreeViewStructureEnum.DISTRIBUTIONS_MP;

/**
 * @author Daniel Abitz
 */
@Definition
@LocalizedUiResource.PutClassPath(DISTRIBUTIONS_MP)
public class InFiniteMassPointsDiscreteDistribution implements InUnivariateDoubleDistribution {

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

    private static final IRPLogger LOGGER = IRPLogging.getLogger(thisClass());

    @DefinitionName
    public String name;

    @FieldDefinition
    @LocalizedUiResource.AddEntry
    public InMassPoint[] massPoints;

    public InFiniteMassPointsDiscreteDistribution() {
    }

    public InFiniteMassPointsDiscreteDistribution(String name, Collection<? extends InMassPoint> massPoints) {
        setName(name);
        setMassPoints(massPoints);
    }

    @Override
    public InFiniteMassPointsDiscreteDistribution copy(CopyCache cache) {
        return cache.copyIfAbsent(this, this::newCopy);
    }

    public InFiniteMassPointsDiscreteDistribution newCopy(CopyCache cache) {
        InFiniteMassPointsDiscreteDistribution copy = new InFiniteMassPointsDiscreteDistribution();
        copy.name = name;
        copy.massPoints = cache.copyArray(massPoints);
        return copy;
    }

    @Override
    public String getName() {
        return name;
    }

    public void setName(String _name) {
        this.name = _name;
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
    public FiniteMassPointsDiscreteDistribution parse(IRPactInputParser parser) throws ParsingException {
        FiniteMassPointsDiscreteDistribution dist = new FiniteMassPointsDiscreteDistribution();
        dist.setName(getName());

        for(InMassPoint inMp: massPoints) {
            WeightedDouble mp = inMp.toWeightedDouble();
            dist.add(mp);
        }

        Rnd rnd = parser.deriveRnd();
        dist.setRandom(rnd);
        LOGGER.trace(IRPSection.INITIALIZATION_PARAMETER, "FiniteMassPointsDiscreteDistribution '{}' uses seed: {}", getName(), rnd.getInitialSeed());

        dist.init();
        return dist;
    }
}
