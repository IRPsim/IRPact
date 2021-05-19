package de.unileipzig.irpact.io.spec.impl.network;

import de.unileipzig.irpact.commons.exception.ParsingException;
import de.unileipzig.irpact.core.log.IRPLogging;
import de.unileipzig.irpact.io.param.input.network.InCompleteGraphTopology;
import de.unileipzig.irpact.io.spec.SpecificationHelper;
import de.unileipzig.irpact.io.spec.SpecificationJob;
import de.unileipzig.irpact.io.spec.impl.AbstractSubSpec;
import de.unileipzig.irptools.util.log.IRPLogger;

import java.util.Objects;

import static de.unileipzig.irpact.io.spec.SpecificationConstants.TAG_name;
import static de.unileipzig.irpact.io.spec.SpecificationConstants.TAG_type;

/**
 * @author Daniel Abitz
 */
public class CompleteGraphTopologySpec extends AbstractSubSpec<InCompleteGraphTopology> {

    private static final IRPLogger LOGGER = IRPLogging.getLogger(CompleteGraphTopologySpec.class);

    public static final CompleteGraphTopologySpec INSTANCE = new CompleteGraphTopologySpec();
    public static final String TYPE = "CompleteGraphTopology";

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
        return input instanceof InCompleteGraphTopology;
    }

    @Override
    protected InCompleteGraphTopology[] newArray(int len) {
        return new InCompleteGraphTopology[len];
    }

    @Override
    protected IRPLogger logger() {
        return LOGGER;
    }

    @Override
    public InCompleteGraphTopology[] toParamArray(SpecificationJob job) throws ParsingException {
        return toParamArray(job.getData().getSpatialModel(), job);
    }

    @Override
    public InCompleteGraphTopology toParam(SpecificationHelper rootSpec, SpecificationJob job) throws ParsingException {
        String name = rootSpec.getText(TAG_name);
        if(job.isCached(name)) {
            return job.getCached(name);
        }

        InCompleteGraphTopology topo = new InCompleteGraphTopology();
        topo.setName(name);

        job.cache(name, topo);
        return topo;
    }

    @Override
    public Class<InCompleteGraphTopology> getParamType() {
        return InCompleteGraphTopology.class;
    }

    @Override
    public void toSpec(InCompleteGraphTopology input, SpecificationJob job) throws ParsingException {
        create(input, job.getData().getSpatialModel(), job);
    }

    @Override
    protected void create(InCompleteGraphTopology input, SpecificationHelper rootSpec, SpecificationJob job) throws ParsingException {
        rootSpec.set(TAG_name, input.getName());
        rootSpec.set(TAG_type, TYPE);
    }
}
