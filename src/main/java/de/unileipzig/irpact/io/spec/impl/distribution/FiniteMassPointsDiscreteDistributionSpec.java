package de.unileipzig.irpact.io.spec.impl.distribution;

import de.unileipzig.irpact.commons.exception.ParsingException;
import de.unileipzig.irpact.core.logging.IRPLogging;
import de.unileipzig.irpact.io.param.input.distribution.InFiniteMassPointsDiscreteDistribution;
import de.unileipzig.irpact.io.param.input.distribution.InMassPoint;
import de.unileipzig.irpact.io.spec.SpecificationHelper;
import de.unileipzig.irpact.io.spec.SpecificationJob;
import de.unileipzig.irpact.io.spec.impl.AbstractSubSpec;
import de.unileipzig.irptools.util.log.IRPLogger;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static de.unileipzig.irpact.io.spec.SpecificationConstants.*;

/**
 * @author Daniel Abitz
 */
public class FiniteMassPointsDiscreteDistributionSpec extends AbstractSubSpec<InFiniteMassPointsDiscreteDistribution> {

    private static final IRPLogger LOGGER = IRPLogging.getLogger(FiniteMassPointsDiscreteDistributionSpec.class);

    public static final FiniteMassPointsDiscreteDistributionSpec INSTANCE = new FiniteMassPointsDiscreteDistributionSpec();
    public static final String TYPE = "FiniteMassPointsDiscreteDistribution";

    private static final String MASSPOINT_COUNTER_ID = "masspoint";
    private static final String MASSPOINT_PREFIX = "mp";

    @Override
    public boolean isType(String type) {
        return Objects.equals(type, TYPE);
    }

    @Override
    public String getType() {
        return TYPE;
    }

    @Override
    public boolean isInstance(Object input) {
        return input instanceof InFiniteMassPointsDiscreteDistribution;
    }

    @Override
    protected InFiniteMassPointsDiscreteDistribution[] newArray(int len) {
        return new InFiniteMassPointsDiscreteDistribution[len];
    }

    @Override
    protected IRPLogger logger() {
        return LOGGER;
    }

    @Override
    public InFiniteMassPointsDiscreteDistribution[] toParamArray(SpecificationJob job) throws ParsingException {
        throw new UnsupportedOperationException();
    }

    @Override
    public InFiniteMassPointsDiscreteDistribution toParam(SpecificationHelper rootSpec, SpecificationJob job) throws ParsingException {
        String name = rootSpec.getText(TAG_name);
        if(job.isCached(name)) {
            return job.getCached(name);
        }

        SpecificationHelper mpSpec = rootSpec.getObject(TAG_parameters);
        List<InMassPoint> mpList = new ArrayList<>();
        for(Map.Entry<String, SpecificationHelper> mpEntry: mpSpec.iterateFields()) {
            double value = Double.parseDouble(mpEntry.getKey());
            double weight = mpEntry.getValue().getDouble();
            String mpName = job.concData(MASSPOINT_PREFIX, job.getAndInc(MASSPOINT_COUNTER_ID));
            InMassPoint mp = new InMassPoint(mpName, value, weight);
            mpList.add(mp);
        }

        InFiniteMassPointsDiscreteDistribution distribution = new InFiniteMassPointsDiscreteDistribution(
                name,
                mpList
        );
        job.cache(name, distribution);
        return distribution;
    }

    @Override
    public Class<InFiniteMassPointsDiscreteDistribution> getParamType() {
        return InFiniteMassPointsDiscreteDistribution.class;
    }

    @Override
    public void toSpec(InFiniteMassPointsDiscreteDistribution input, SpecificationJob job) throws ParsingException {
        toSpecIfNotExists(input.getName(), job.getData().getDistributions(), input, job);
    }

    @Override
    protected void create(InFiniteMassPointsDiscreteDistribution input, SpecificationHelper rootSpec, SpecificationJob job) throws ParsingException {
        rootSpec.set(TAG_name, input.getName());
        rootSpec.set(TAG_type, TYPE);

        SpecificationHelper mpSpec = rootSpec.getOrCreateObject(TAG_parameters);
        for(InMassPoint mp: input.getMassPoints()) {
            mpSpec.set(mp.getValue(), mp.getWeight());
        }
    }
}
