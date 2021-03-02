package de.unileipzig.irpact.io.param.input.distribution;

import de.unileipzig.irpact.commons.Rnd;
import de.unileipzig.irpact.commons.WeightedDouble;
import de.unileipzig.irpact.commons.distribution.FiniteMassPointsDiscreteDistribution;
import de.unileipzig.irpact.commons.exception.ParsingException;
import de.unileipzig.irpact.core.log.IRPLogging;
import de.unileipzig.irpact.core.log.IRPSection;
import de.unileipzig.irpact.io.param.input.InputParser;
import de.unileipzig.irpact.util.Todo;
import de.unileipzig.irptools.defstructure.annotation.Definition;
import de.unileipzig.irptools.defstructure.annotation.FieldDefinition;
import de.unileipzig.irptools.util.TreeAnnotationResource;
import de.unileipzig.irptools.util.log.IRPLogger;

import java.lang.invoke.MethodHandles;

/**
 * @author Daniel Abitz
 */
@Definition
@Todo("ref einfuegen")
public class InFiniteMassPointsDiscreteDistribution implements InUnivariateDoubleDistribution {

    //damit ich bei copy&paste nie mehr vergesse die Klasse anzupassen :)
    private static final MethodHandles.Lookup L = MethodHandles.lookup();
    public static Class<?> thisClass() {
        return L.lookupClass();
    }

    public static void initRes(TreeAnnotationResource res) {
    }
    public static void applyRes(TreeAnnotationResource res) {
        res.putPath(
                InFiniteMassPointsDiscreteDistribution.class,
                res.getCachedElement("Verteilungsfunktionen"),
                res.getCachedElement("FiniteMassPointsDiscreteDistribution")
        );

        res.newEntryBuilder()
                .setGamsIdentifier("Massepunkte")
                .setGamsDescription("Die zu nutzende Massepunkte")
                .store(InFiniteMassPointsDiscreteDistribution.class, "massPoints");
    }

    private static final IRPLogger LOGGER = IRPLogging.getLogger(InFiniteMassPointsDiscreteDistribution.class);

    public String _name;

    @FieldDefinition
    public InMassPoint[] massPoints;

    public InFiniteMassPointsDiscreteDistribution() {
    }

    @Override
    public String getName() {
        return _name;
    }

    public InMassPoint[] getMassPoints() {
        return massPoints;
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
