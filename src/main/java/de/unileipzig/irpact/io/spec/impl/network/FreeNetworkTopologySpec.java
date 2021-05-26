package de.unileipzig.irpact.io.spec.impl.network;

import de.unileipzig.irpact.commons.exception.ParsingException;
import de.unileipzig.irpact.core.logging.IRPLogging;
import de.unileipzig.irpact.io.param.ParamUtil;
import de.unileipzig.irpact.io.param.input.agent.consumer.InConsumerAgentGroup;
import de.unileipzig.irpact.io.param.input.network.InFreeNetworkTopology;
import de.unileipzig.irpact.io.param.input.network.InNumberOfTies;
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
public class FreeNetworkTopologySpec extends AbstractSubSpec<InFreeNetworkTopology> {

    private static final IRPLogger LOGGER = IRPLogging.getLogger(FreeNetworkTopologySpec.class);

    public static final FreeNetworkTopologySpec INSTANCE = new FreeNetworkTopologySpec();
    public static final String TYPE = "UnlinkedGraphTopology";

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
        return input instanceof InFreeNetworkTopology;
    }

    @Override
    protected InFreeNetworkTopology[] newArray(int len) {
        return new InFreeNetworkTopology[len];
    }

    @Override
    protected IRPLogger logger() {
        return LOGGER;
    }

    @Override
    public InFreeNetworkTopology[] toParamArray(SpecificationJob job) throws ParsingException {
        return toParamArray(job.getData().getSpatialModel(), job);
    }

    @Override
    public InFreeNetworkTopology toParam(SpecificationHelper rootSpec, SpecificationJob job) throws ParsingException {
        String name = rootSpec.getText(TAG_name);
        if(job.isCached(name)) {
            return job.getCached(name);
        }

        InFreeNetworkTopology topo = new InFreeNetworkTopology();
        topo.setName(name);
        topo.setDistanceEvaluator(job.parseInlinedDistanceEvaluator(rootSpec.getNode(TAG_parameters, TAG_distanceEvaluator)));
        topo.setInitialWeight(rootSpec.getDouble(TAG_parameters, TAG_initialWeight));

        List<InNumberOfTies> ties = new ArrayList<>();
        SpecificationHelper tieSpec = rootSpec.getObject(TAG_parameters, TAG_numberOfTies);
        for(Map.Entry<String, SpecificationHelper> entry: tieSpec.iterateFields()) {
            String cagName = entry.getKey();
            InConsumerAgentGroup cag = job.findConsumerAgentGroup(cagName);
            int count = entry.getValue().getInt();
            InNumberOfTies tie = new InNumberOfTies();
            tie.setName(ParamUtil.concData(cag.getName(), "tie"));
            tie.setConsumerAgentGroup(cag);
            tie.setCount(count);
            ties.add(tie);
        }
        topo.setNumberOfTies(ties);

        job.cache(name, topo);
        return topo;
    }

    @Override
    public Class<InFreeNetworkTopology> getParamType() {
        return InFreeNetworkTopology.class;
    }

    @Override
    public void toSpec(InFreeNetworkTopology input, SpecificationJob job) throws ParsingException {
        create(input, job.getData().getSpatialModel(), job);
    }

    @Override
    protected void create(InFreeNetworkTopology input, SpecificationHelper rootSpec, SpecificationJob job) throws ParsingException {
        rootSpec.set(TAG_name, input.getName());
        rootSpec.set(TAG_type, TYPE);
        rootSpec.set(TAG_parameters, TAG_distanceEvaluator, job.inlineEntity(input.getDistanceEvaluator(), false));
        rootSpec.set(TAG_parameters, TAG_initialWeight, input.getInitialWeight());

        SpecificationHelper tiesSpec = rootSpec.getOrCreateObject(TAG_parameters, TAG_numberOfTies);
        for(InNumberOfTies tie: input.getNumberOfTies()) {
            for(InConsumerAgentGroup cag: tie.getConsumerAgentGroups()) {
                tiesSpec.set(cag.getName(), tie.getCount());
            }
        }
    }
}
