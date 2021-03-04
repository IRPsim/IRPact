package de.unileipzig.irpact.io.spec.impl.distribution;

import com.fasterxml.jackson.databind.node.ObjectNode;
import de.unileipzig.irpact.io.param.input.distribution.InUnivariateDoubleDistribution;
import de.unileipzig.irpact.io.spec.*;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;

/**
 * @author Daniel Abitz
 */
public class UnivariateDoubleDistributionSpec implements ToParamConverter<InUnivariateDoubleDistribution> {

    public static final UnivariateDoubleDistributionSpec INSTANCE = new UnivariateDoubleDistributionSpec();

    //TODO kombinieren
    @Override
    public InUnivariateDoubleDistribution[] toParam(SpecificationManager manager, SpecificationConverter converter, SpecificationCache cache) {
        List<InUnivariateDoubleDistribution> distList = new ArrayList<>();

        for(ObjectNode root: manager.getDistributions().getAll().values()) {
            SpecificationHelper spec = new SpecificationHelper(root);
            String type = spec.getType();
            if(BooleanDistributionSpec.TYPE.equals(type)) {
                distList.add(BooleanDistributionSpec.INSTANCE.toParam(root, manager, converter, cache));
            }
            else if(ConstantUnivariateDistributionSpec.TYPE.equals(type)) {
                distList.add(ConstantUnivariateDistributionSpec.INSTANCE.toParam(root, manager, converter, cache));
            }
            else if(FiniteMassPointsDiscreteDistributionSpec.TYPE.equals(type)) {
                distList.add(FiniteMassPointsDiscreteDistributionSpec.INSTANCE.toParam(root, manager, converter, cache));
            }
            else if(RandomBoundedIntegerDistributionSpec.TYPE.equals(type)) {
                distList.add(RandomBoundedIntegerDistributionSpec.INSTANCE.toParam(root, manager, converter, cache));
            }
            else {
                throw new IllegalArgumentException("unsupported type: " + type);
            }
        }

        return distList.toArray(new InUnivariateDoubleDistribution[0]);
    }

    @Override
    public InUnivariateDoubleDistribution toParam(ObjectNode root, SpecificationManager manager, SpecificationConverter converter, SpecificationCache cache) {
        SpecificationHelper spec = new SpecificationHelper(root);
        String type = spec.getType();
        if(BooleanDistributionSpec.TYPE.equals(type)) {
            return BooleanDistributionSpec.INSTANCE.toParam(root, manager, converter, cache);
        }
        else if(ConstantUnivariateDistributionSpec.TYPE.equals(type)) {
            return ConstantUnivariateDistributionSpec.INSTANCE.toParam(root, manager, converter, cache);
        }
        else if(FiniteMassPointsDiscreteDistributionSpec.TYPE.equals(type)) {
            return FiniteMassPointsDiscreteDistributionSpec.INSTANCE.toParam(root, manager, converter, cache);
        }
        else if(RandomBoundedIntegerDistributionSpec.TYPE.equals(type)) {
            return RandomBoundedIntegerDistributionSpec.INSTANCE.toParam(root, manager, converter, cache);
        }
        else {
            throw new IllegalArgumentException("unsupported type: " + type);
        }
    }

    public InUnivariateDoubleDistribution toParamByName(String name, SpecificationManager manager, SpecificationConverter converter, SpecificationCache cache) {
        for(InUnivariateDoubleDistribution dist: toParam(manager, converter, cache)) {
            if(Objects.equals(dist.getName(), name)) {
                return dist;
            }
        }
        throw new NoSuchElementException(name);
    }
}
