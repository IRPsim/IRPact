package de.unileipzig.irpact.io.spec.impl.network;

import com.fasterxml.jackson.databind.node.ObjectNode;
import de.unileipzig.irpact.commons.exception.ParsingException;
import de.unileipzig.irpact.io.param.input.network.InFreeNetworkTopology;
import de.unileipzig.irpact.io.param.input.network.InGraphTopologyScheme;
import de.unileipzig.irpact.io.param.input.network.InUnlinkedGraphTopology;
import de.unileipzig.irpact.io.spec.*;

import static de.unileipzig.irpact.io.spec.SpecificationConstants.TAG_topology;

/**
 * @author Daniel Abitz
 */
public class GraphTopologySpec implements ToSpecConverter<InGraphTopologyScheme>, ToParamConverter<InGraphTopologyScheme> {

    public static final GraphTopologySpec INSTANCE = new GraphTopologySpec();

    @Override
    public Class<InGraphTopologyScheme> getParamType() {
        return InGraphTopologyScheme.class;
    }

    @Override
    public void toSpec(InGraphTopologyScheme input, SpecificationManager manager, SpecificationConverter converter, boolean inline) throws ParsingException {
        if(input instanceof InUnlinkedGraphTopology) {
            UnlinkedGraphTopologySpec.INSTANCE.toSpec((InUnlinkedGraphTopology) input, manager, converter, inline);
        }
        else if(input instanceof InFreeNetworkTopology) {
            FreeNetworkTopologySpec.INSTANCE.toSpec((InFreeNetworkTopology) input, manager, converter, inline);
        } else {
            throw new ParsingException("unknown class: " + input.getClass());
        }
    }

    @Override
    public void create(InGraphTopologyScheme input, ObjectNode root, SpecificationManager manager, SpecificationConverter converter, boolean inline) throws ParsingException {
        if(input instanceof InUnlinkedGraphTopology) {
            UnlinkedGraphTopologySpec.INSTANCE.create((InUnlinkedGraphTopology) input, root, manager, converter, inline);
        }
        else if(input instanceof InFreeNetworkTopology) {
            FreeNetworkTopologySpec.INSTANCE.create((InFreeNetworkTopology) input, root, manager, converter, inline);
        }
        else {
            throw new ParsingException("unknown class: " + input.getClass());
        }
    }

    @Override
    public InGraphTopologyScheme[] toParam(SpecificationManager manager, SpecificationConverter converter, SpecificationCache cache) throws ParsingException {
        SpecificationHelper spec = new SpecificationHelper(manager.getSocialNetwork().get());
        SpecificationHelper topo = spec.getObjectSpec(TAG_topology);
        String type = topo.getType();

        if(UnlinkedGraphTopologySpec.TYPE.equals(type)) {
            return UnlinkedGraphTopologySpec.INSTANCE.toParam(manager, converter, cache);
        }
        else if(FreeNetworkTopologySpec.TYPE.equals(type)) {
            return FreeNetworkTopologySpec.INSTANCE.toParam(manager, converter, cache);
        }
        else {
            throw new IllegalArgumentException("unknown type: " + type);
        }
    }
}
