package de.unileipzig.irpact.experimental.deprecated.input.distribution;

import de.unileipzig.irpact.commons.util.data.Pair;
import de.unileipzig.irpact.commons.distribution.FiniteMassPointsDiscreteDistributionOLD;
import de.unileipzig.irptools.defstructure.annotation.Definition;
import de.unileipzig.irptools.defstructure.annotation.Edn;
import de.unileipzig.irptools.defstructure.annotation.FieldDefinition;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Daniel Abitz
 */
@Definition(
        edn = @Edn(
                label = {"Distribution/FiniteMassPointsDiscreteDistribution"}
        )
)
public class IFiniteMassPointsDiscreteDistribution implements IUnivariateDoubleDistribution {

    public String _name;

    @FieldDefinition
    public long fmpSeed;

    @FieldDefinition
    public IMassPoint[] massPoints;

    public IFiniteMassPointsDiscreteDistribution() {
    }

    public IFiniteMassPointsDiscreteDistribution(String name, long fmpSeed, IMassPoint massPoint) {
        this(name, fmpSeed, new IMassPoint[]{massPoint});
    }

    public IFiniteMassPointsDiscreteDistribution(String name, long fmpSeed, IMassPoint[] massPoints) {
        this._name = name;
        this.fmpSeed = fmpSeed;
        this.massPoints = massPoints;
    }

    public String getName() {
        return _name;
    }

    public long getFmpSeed() {
        return fmpSeed;
    }

    public IMassPoint[] getMassPoints() {
        return massPoints;
    }

    @Override
    public FiniteMassPointsDiscreteDistributionOLD createInstance() {
        FiniteMassPointsDiscreteDistributionOLD dist = new FiniteMassPointsDiscreteDistributionOLD();
        dist.setName(getName());
        dist.init(getFmpSeed());
        List<Pair<Double, Double>> list = Arrays.stream(getMassPoints())
                .map(p -> new Pair<>(p.getMpValue(), p.getMpWeight()))
                .collect(Collectors.toList());
        dist.init(list);
        return dist;
    }
}
