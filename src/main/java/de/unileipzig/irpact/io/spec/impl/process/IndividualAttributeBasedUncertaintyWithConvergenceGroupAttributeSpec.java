package de.unileipzig.irpact.io.spec.impl.process;

import de.unileipzig.irpact.commons.exception.ParsingException;
import de.unileipzig.irpact.core.log.IRPLogging;
import de.unileipzig.irpact.io.param.input.agent.consumer.InConsumerAgentGroupAttribute;
import de.unileipzig.irpact.io.param.input.distribution.InUnivariateDoubleDistribution;
import de.unileipzig.irpact.io.param.input.process.ra.InIndividualAttributeBasedUncertaintyWithConvergenceGroupAttribute;
import de.unileipzig.irpact.io.spec.SpecificationHelper;
import de.unileipzig.irpact.io.spec.SpecificationJob;
import de.unileipzig.irpact.io.spec.impl.AbstractSubSpec;
import de.unileipzig.irptools.util.log.IRPLogger;

import java.util.Objects;

import static de.unileipzig.irpact.io.spec.SpecificationConstants.*;

/**
 * @author Daniel Abitz
 */
public class IndividualAttributeBasedUncertaintyWithConvergenceGroupAttributeSpec extends AbstractSubSpec<InIndividualAttributeBasedUncertaintyWithConvergenceGroupAttribute> {

    private static final IRPLogger LOGGER = IRPLogging.getLogger(IndividualAttributeBasedUncertaintyWithConvergenceGroupAttributeSpec.class);

    public static final IndividualAttributeBasedUncertaintyWithConvergenceGroupAttributeSpec INSTANCE = new IndividualAttributeBasedUncertaintyWithConvergenceGroupAttributeSpec();
    public static final String TYPE = "IndividualAttributeBasedUncertaintyWithConvergenceGroupAttribute";

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
        return input instanceof InIndividualAttributeBasedUncertaintyWithConvergenceGroupAttribute;
    }

    @Override
    protected InIndividualAttributeBasedUncertaintyWithConvergenceGroupAttribute[] newArray(int len) {
        return new InIndividualAttributeBasedUncertaintyWithConvergenceGroupAttribute[len];
    }

    @Override
    protected IRPLogger logger() {
        return LOGGER;
    }

    @Override
    public InIndividualAttributeBasedUncertaintyWithConvergenceGroupAttribute[] toParamArray(SpecificationJob job) throws ParsingException {
        return toParamArray(job.getData().getSpatialModel(), job);
    }

    @Override
    public InIndividualAttributeBasedUncertaintyWithConvergenceGroupAttribute toParam(SpecificationHelper rootSpec, SpecificationJob job) throws ParsingException {
        String name = rootSpec.getText(TAG_name);
        if(job.isCached(name)) {
            return job.getCached(name);
        }

        InIndividualAttributeBasedUncertaintyWithConvergenceGroupAttribute topo = new InIndividualAttributeBasedUncertaintyWithConvergenceGroupAttribute();
        topo.setName(name);

        InUnivariateDoubleDistribution uncertDist = job.parseInlinedDistribution(rootSpec.getNode(TAG_parameters, TAG_uncertainty));
        topo.setUncertaintyDistribution(uncertDist);

        InUnivariateDoubleDistribution convDist = job.parseInlinedDistribution(rootSpec.getNode(TAG_parameters, TAG_convergence));
        topo.setConvergenceDistribution(convDist);

        InConsumerAgentGroupAttribute[] attrs = job.parseNamedSplitConsumerAgentGroupAttributeArray(rootSpec.getNode(TAG_parameters, TAG_attributes));
        topo.setAttributes(attrs);

        job.cache(name, topo);
        return topo;
    }

    @Override
    public Class<InIndividualAttributeBasedUncertaintyWithConvergenceGroupAttribute> getParamType() {
        return InIndividualAttributeBasedUncertaintyWithConvergenceGroupAttribute.class;
    }

    @Override
    public void toSpec(InIndividualAttributeBasedUncertaintyWithConvergenceGroupAttribute input, SpecificationJob job) throws ParsingException {
        create(input, job.getData().getSpatialModel(), job);
    }

    @Override
    protected void create(InIndividualAttributeBasedUncertaintyWithConvergenceGroupAttribute input, SpecificationHelper rootSpec, SpecificationJob job) throws ParsingException {
        rootSpec.set(TAG_name, input.getName());
        rootSpec.set(TAG_type, TYPE);

        rootSpec.set(TAG_parameters, TAG_uncertainty, job.inlineEntity(input.getUncertaintyDistribution(), false));
        rootSpec.set(TAG_parameters, TAG_convergence, job.inlineEntity(input.getConvergenceDistribution(), false));
        rootSpec.set(TAG_parameters, TAG_attributes, job.namedNameSplitConsumerAgentGroupAttributes(input.getAttributes()));
    }
}
