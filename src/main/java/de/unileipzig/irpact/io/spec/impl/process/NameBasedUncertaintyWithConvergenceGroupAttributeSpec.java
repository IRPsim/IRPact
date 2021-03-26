package de.unileipzig.irpact.io.spec.impl.process;

import de.unileipzig.irpact.commons.exception.ParsingException;
import de.unileipzig.irpact.core.log.IRPLogging;
import de.unileipzig.irpact.io.param.input.InAttributeName;
import de.unileipzig.irpact.io.param.input.agent.consumer.InConsumerAgentGroup;
import de.unileipzig.irpact.io.param.input.distribution.InUnivariateDoubleDistribution;
import de.unileipzig.irpact.io.param.input.process.ra.InNameBasedUncertaintyWithConvergenceGroupAttribute;
import de.unileipzig.irpact.io.spec.SpecificationHelper;
import de.unileipzig.irpact.io.spec.SpecificationJob;
import de.unileipzig.irpact.io.spec.impl.AbstractSubSpec;
import de.unileipzig.irptools.util.log.IRPLogger;

import java.util.Objects;

import static de.unileipzig.irpact.io.spec.SpecificationConstants.*;

/**
 * @author Daniel Abitz
 */
public class NameBasedUncertaintyWithConvergenceGroupAttributeSpec extends AbstractSubSpec<InNameBasedUncertaintyWithConvergenceGroupAttribute> {

    private static final IRPLogger LOGGER = IRPLogging.getLogger(NameBasedUncertaintyWithConvergenceGroupAttributeSpec.class);

    public static final NameBasedUncertaintyWithConvergenceGroupAttributeSpec INSTANCE = new NameBasedUncertaintyWithConvergenceGroupAttributeSpec();
    public static final String TYPE = "NameBasedUncertaintyWithConvergenceGroupAttribute";

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
        return input instanceof InNameBasedUncertaintyWithConvergenceGroupAttribute;
    }

    @Override
    protected InNameBasedUncertaintyWithConvergenceGroupAttribute[] newArray(int len) {
        return new InNameBasedUncertaintyWithConvergenceGroupAttribute[len];
    }

    @Override
    protected IRPLogger logger() {
        return LOGGER;
    }

    @Override
    public InNameBasedUncertaintyWithConvergenceGroupAttribute[] toParamArray(SpecificationJob job) throws ParsingException {
        return toParamArray(job.getData().getSpatialModel(), job);
    }

    @Override
    public InNameBasedUncertaintyWithConvergenceGroupAttribute toParam(SpecificationHelper rootSpec, SpecificationJob job) throws ParsingException {
        String name = rootSpec.getText(TAG_name);
        if(job.isCached(name)) {
            return job.getCached(name);
        }

        InNameBasedUncertaintyWithConvergenceGroupAttribute topo = new InNameBasedUncertaintyWithConvergenceGroupAttribute();
        topo.setName(name);

        InUnivariateDoubleDistribution uncertDist = job.parseInlinedDistribution(rootSpec.getNode(TAG_parameters, TAG_uncertainty));
        topo.setUncertaintyDistribution(uncertDist);

        InUnivariateDoubleDistribution convDist = job.parseInlinedDistribution(rootSpec.getNode(TAG_parameters, TAG_convergence));
        topo.setConvergenceDistribution(convDist);

        InConsumerAgentGroup[] groups = job.parseNamedConsumerAgentGroupArray(rootSpec.getNode(TAG_parameters, TAG_consumerAgentGroups));
        topo.setGroups(groups);

        InAttributeName[] names = job.parseAttributeNameArray(rootSpec.getNode(TAG_parameters, TAG_attributes));
        topo.setNames(names);

        job.cache(name, topo);
        return topo;
    }

    @Override
    public Class<InNameBasedUncertaintyWithConvergenceGroupAttribute> getParamType() {
        return InNameBasedUncertaintyWithConvergenceGroupAttribute.class;
    }

    @Override
    public void toSpec(InNameBasedUncertaintyWithConvergenceGroupAttribute input, SpecificationJob job) throws ParsingException {
        create(input, job.getData().getSpatialModel(), job);
    }

    @Override
    protected void create(InNameBasedUncertaintyWithConvergenceGroupAttribute input, SpecificationHelper rootSpec, SpecificationJob job) throws ParsingException {
        rootSpec.set(TAG_name, input.getName());
        rootSpec.set(TAG_type, TYPE);

        rootSpec.set(TAG_parameters, TAG_uncertainty, job.inlineEntity(input.getUncertaintyDistribution(), false));
        rootSpec.set(TAG_parameters, TAG_convergence, job.inlineEntity(input.getConvergenceDistribution(), false));
        rootSpec.set(TAG_parameters, TAG_consumerAgentGroups, job.namedEntityArray(input.getGroups()));
        rootSpec.set(TAG_parameters, TAG_attributes, job.namedEntityArray(input.getNames()));
    }
}
