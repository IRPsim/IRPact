package de.unileipzig.irpact.io.input.distribution;

import de.unileipzig.irpact.commons.Rnd;
import de.unileipzig.irpact.commons.distribution.RandomBoundedIntegerDistribution;
import de.unileipzig.irpact.commons.distribution.UnivariateDoubleDistribution;
import de.unileipzig.irpact.core.log.IRPLogging;
import de.unileipzig.irpact.core.log.IRPSection;
import de.unileipzig.irpact.io.input.agent.consumer.InConsumerAgentGroup;
import de.unileipzig.irptools.defstructure.annotation.Definition;
import de.unileipzig.irptools.defstructure.annotation.FieldDefinition;
import de.unileipzig.irptools.util.TreeAnnotationResource;
import de.unileipzig.irptools.util.log.IRPLogger;

import java.util.Objects;

/**
 * @author Daniel Abitz
 */
@Definition
public class InRandomBoundedIntegerDistribution implements InUnivariateDoubleDistribution {

    public static void initRes(TreeAnnotationResource res) {
        res.newElementBuilder()
                .setEdnLabel("Gleichverteilte ganze Zahlen")
                .setEdnPriority(0)
                .setEdnDescription("Verteilungsfunktion, welche ganzzahlige Werte gleichverteilt aus einem Bereich zieht.")
                .putCache("RandomBoundedInteger");
    }
    public static void applyRes(TreeAnnotationResource res) {
        res.putPath(
                InRandomBoundedIntegerDistribution.class,
                res.getCachedElement("Verteilungsfunktionen"),
                res.getCachedElement("RandomBoundedInteger")
        );

        res.newEntryBuilder()
                .setGamsIdentifier("Untergrenze (inklusiv)")
                .setGamsDescription("Untergrenze")
                .store(InRandomBoundedIntegerDistribution.class, "lowerBoundInt");

        res.newEntryBuilder()
                .setGamsIdentifier("Obergrenze (exklusiv)")
                .setGamsDescription("Obergrenze")
                .store(InRandomBoundedIntegerDistribution.class, "upperBoundInt");
    }

    private static final IRPLogger LOGGER = IRPLogging.getLogger(InRandomBoundedIntegerDistribution.class);

    public String _name;

    @FieldDefinition
    public int lowerBoundInt;

    @FieldDefinition
    public int upperBoundInt;

    private RandomBoundedIntegerDistribution instance;

    public InRandomBoundedIntegerDistribution() {
    }

    public InRandomBoundedIntegerDistribution(String name, int lowerBoundInt, int upperBoundInt) {
        this._name = name;
        this.lowerBoundInt = lowerBoundInt;
        this.upperBoundInt = upperBoundInt;
    }

    @Override
    public String getName() {
        return _name;
    }

    @Override
    public UnivariateDoubleDistribution getInstance(Rnd rnd) {
        if(instance == null) {
            instance = new RandomBoundedIntegerDistribution();
            instance.setName(getName());
            instance.setLowerBound(getLowerBound());
            instance.setUpperBound(getUpperBound());
            Rnd thisRnd = rnd.createNewRandom();
            instance.setRandom(thisRnd);
            LOGGER.trace(IRPSection.INITIALIZATION_PARAMETER, "InRandomBoundedIntegerDistribution '{}' rnd-seed: {}", getName(), thisRnd.getInitialSeed());
        }
        return instance;
    }

    public int getLowerBound() {
        return lowerBoundInt;
    }

    public int getUpperBound() {
        return upperBoundInt;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof InRandomBoundedIntegerDistribution)) return false;
        InRandomBoundedIntegerDistribution that = (InRandomBoundedIntegerDistribution) o;
        return Double.compare(that.lowerBoundInt, lowerBoundInt) == 0
                && Double.compare(that.upperBoundInt, upperBoundInt) == 0
                && Objects.equals(_name, that._name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(_name, lowerBoundInt, upperBoundInt);
    }

    @Override
    public String toString() {
        return "InConstantUnivariateDistribution{" +
                "_name='" + _name + '\'' +
                ", lowerBoundInt=" + lowerBoundInt +
                ", upperBoundInt=" + upperBoundInt +
                '}';
    }
}
