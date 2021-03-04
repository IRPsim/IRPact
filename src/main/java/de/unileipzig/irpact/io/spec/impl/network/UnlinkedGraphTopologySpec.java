package de.unileipzig.irpact.io.spec.impl.network;

import com.fasterxml.jackson.databind.node.ObjectNode;
import de.unileipzig.irpact.io.param.input.network.InUnlinkedGraphTopology;
import de.unileipzig.irpact.io.spec.*;

import static de.unileipzig.irpact.io.spec.SpecificationConstants.TAG_topology;

/**
 * @author Daniel Abitz
 */
public class UnlinkedGraphTopologySpec
        implements ToSpecConverter<InUnlinkedGraphTopology>, ToParamConverter<InUnlinkedGraphTopology> {

    public static final UnlinkedGraphTopologySpec INSTANCE = new UnlinkedGraphTopologySpec();
    public static final String TYPE = "UnlinkedGraphTopology";

    @Override
    public Class<InUnlinkedGraphTopology> getParamType() {
        return InUnlinkedGraphTopology.class;
    }

    @Override
    public void toSpec(InUnlinkedGraphTopology input, SpecificationManager manager, SpecificationConverter converter, boolean inline) {
        SpecificationHelper spec = new SpecificationHelper(manager.getSocialNetwork().get());
        create(input, spec.getObject(TAG_topology), manager, converter, inline);
    }

    @Override
    public void create(InUnlinkedGraphTopology input, ObjectNode root, SpecificationManager manager, SpecificationConverter converter, boolean inline) {
        SpecificationHelper spec = new SpecificationHelper(root);
        spec.setName(input.getName());
        spec.setType(TYPE);
    }

    @Override
    public InUnlinkedGraphTopology[] toParam(SpecificationManager manager, SpecificationConverter converter, SpecificationCache cache) {
        SpecificationHelper spec = new SpecificationHelper(manager.getSocialNetwork().get());
        SpecificationHelper topoSpec = spec.getObjectSpec(TAG_topology);
        InUnlinkedGraphTopology topo = new InUnlinkedGraphTopology(topoSpec.getName());
        return new InUnlinkedGraphTopology[]{topo};
    }
}
