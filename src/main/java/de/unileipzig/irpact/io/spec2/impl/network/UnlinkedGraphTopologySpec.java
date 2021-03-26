package de.unileipzig.irpact.io.spec2.impl.network;

import de.unileipzig.irpact.commons.exception.ParsingException;
import de.unileipzig.irpact.core.log.IRPLogging;
import de.unileipzig.irpact.io.param.input.network.InUnlinkedGraphTopology;
import de.unileipzig.irpact.io.spec2.SpecificationHelper2;
import de.unileipzig.irpact.io.spec2.SpecificationJob2;
import de.unileipzig.irpact.io.spec2.impl.AbstractSubSpec;
import de.unileipzig.irptools.util.log.IRPLogger;

import java.util.Objects;

import static de.unileipzig.irpact.io.spec2.SpecificationConstants.*;

/**
 * @author Daniel Abitz
 */
public class UnlinkedGraphTopologySpec extends AbstractSubSpec<InUnlinkedGraphTopology> {

    private static final IRPLogger LOGGER = IRPLogging.getLogger(UnlinkedGraphTopologySpec.class);

    public static final UnlinkedGraphTopologySpec INSTANCE = new UnlinkedGraphTopologySpec();
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
        return input instanceof InUnlinkedGraphTopology;
    }

    @Override
    protected InUnlinkedGraphTopology[] newArray(int len) {
        return new InUnlinkedGraphTopology[len];
    }

    @Override
    protected IRPLogger logger() {
        return LOGGER;
    }

    @Override
    public InUnlinkedGraphTopology[] toParamArray(SpecificationJob2 job) throws ParsingException {
        return toParamArray(job.getData().getSpatialModel(), job);
    }

    @Override
    public InUnlinkedGraphTopology toParam(SpecificationHelper2 rootSpec, SpecificationJob2 job) throws ParsingException {
        String name = rootSpec.getText(TAG_name);
        if(job.isCached(name)) {
            return job.getCached(name);
        }

        InUnlinkedGraphTopology topo = new InUnlinkedGraphTopology();
        topo.setName(name);

        job.cache(name, topo);
        return topo;
    }

    @Override
    public Class<InUnlinkedGraphTopology> getParamType() {
        return InUnlinkedGraphTopology.class;
    }

    @Override
    public void toSpec(InUnlinkedGraphTopology input, SpecificationJob2 job) throws ParsingException {
        create(input, job.getData().getSpatialModel(), job);
    }

    @Override
    protected void create(InUnlinkedGraphTopology input, SpecificationHelper2 rootSpec, SpecificationJob2 job) throws ParsingException {
        rootSpec.set(TAG_name, input.getName());
        rootSpec.set(TAG_type, TYPE);
    }
}