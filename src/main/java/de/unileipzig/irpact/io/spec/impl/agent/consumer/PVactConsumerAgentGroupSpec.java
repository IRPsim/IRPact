package de.unileipzig.irpact.io.spec.impl.agent.consumer;

import de.unileipzig.irpact.commons.exception.ParsingException;
import de.unileipzig.irpact.core.logging.IRPLogging;
import de.unileipzig.irpact.io.param.input.agent.consumer.InPVactConsumerAgentGroup;
import de.unileipzig.irpact.io.spec.SpecificationHelper;
import de.unileipzig.irpact.io.spec.SpecificationJob;
import de.unileipzig.irpact.io.spec.impl.AbstractSubSpec;
import de.unileipzig.irptools.util.log.IRPLogger;

import java.util.Objects;

import static de.unileipzig.irpact.io.spec.SpecificationConstants.*;

/**
 * @author Daniel Abitz
 */
public class PVactConsumerAgentGroupSpec extends AbstractSubSpec<InPVactConsumerAgentGroup> {

    private static final IRPLogger LOGGER = IRPLogging.getLogger(PVactConsumerAgentGroupSpec.class);

    public static final PVactConsumerAgentGroupSpec INSTANCE = new PVactConsumerAgentGroupSpec();
    public static final String TYPE = "PVactConsumerAgentGroup";

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
        return input instanceof InPVactConsumerAgentGroup;
    }

    @Override
    protected InPVactConsumerAgentGroup[] newArray(int len) {
        return new InPVactConsumerAgentGroup[len];
    }

    @Override
    protected IRPLogger logger() {
        return LOGGER;
    }

    @Override
    public InPVactConsumerAgentGroup[] toParamArray(SpecificationJob job) throws ParsingException {
        return toParamArray(job.getData().getSpatialModel(), job);
    }

    @Override
    public InPVactConsumerAgentGroup toParam(SpecificationHelper rootSpec, SpecificationJob job) throws ParsingException {
        String name = rootSpec.getText(TAG_name);
        if(job.isCached(name)) {
            return job.getCached(name);
        }

        InPVactConsumerAgentGroup grp = new InPVactConsumerAgentGroup();

        grp.setNoveltySeeking(job.parseInlinedDistribution(rootSpec.getNode(TAG_parameters, TAG_NOVELTY_SEEKING)));
        grp.setDependentJudgmentMaking(job.parseInlinedDistribution(rootSpec.getNode(TAG_parameters, TAG_INDEPENDENT_JUDGMENT_MAKING)));
        grp.setEnvironmentalConcern(job.parseInlinedDistribution(rootSpec.getNode(TAG_parameters, TAG_ENVIRONMENTAL_CONCERN)));
        grp.setFinancialThreshold(job.parseInlinedDistribution(rootSpec.getNode(TAG_parameters, TAG_FINANCIAL_THRESHOLD)));
        grp.setAdoptionThreshold(job.parseInlinedDistribution(rootSpec.getNode(TAG_parameters, TAG_ADOPTION_THRESHOLD)));
        grp.setNoveltySeeking(job.parseInlinedDistribution(rootSpec.getNode(TAG_parameters, TAG_NOVELTY_SEEKING)));
        grp.setCommunication(job.parseInlinedDistribution(rootSpec.getNode(TAG_parameters, TAG_COMMUNICATION_FREQUENCY_SN)));
        grp.setRewire(job.parseInlinedDistribution(rootSpec.getNode(TAG_parameters, TAG_REWIRING_RATE)));
        grp.setInitialAdopter(job.parseInlinedDistribution(rootSpec.getNode(TAG_parameters, TAG_INITIAL_ADOPTER)));
        grp.setInterestThreshold(job.parseInlinedDistribution(rootSpec.getNode(TAG_parameters, TAG_INTEREST_THRESHOLD)));

        grp.setSpatialDistribution(job.parseInlinedSpatialDistribution(rootSpec.getNode(TAG_parameters, TAG_spatialDistribution)));

        job.cache(name, grp);
        return grp;
    }

    @Override
    public Class<InPVactConsumerAgentGroup> getParamType() {
        return InPVactConsumerAgentGroup.class;
    }

    @Override
    public void toSpec(InPVactConsumerAgentGroup input, SpecificationJob job) throws ParsingException {
        create(input, job.getData().getConsumerAgentGroups().get(input.getName()), job);
    }

    @Override
    protected void create(InPVactConsumerAgentGroup input, SpecificationHelper rootSpec, SpecificationJob job) throws ParsingException {
        rootSpec.set(TAG_name, input.getName());
        rootSpec.set(TAG_type, TYPE);

        rootSpec.set(TAG_parameters, TAG_NOVELTY_SEEKING, job.inlineEntity(input.getNoveltySeeking(), false));
        rootSpec.set(TAG_parameters, TAG_INDEPENDENT_JUDGMENT_MAKING, job.inlineEntity(input.getDependentJudgmentMaking(), false));
        rootSpec.set(TAG_parameters, TAG_ENVIRONMENTAL_CONCERN, job.inlineEntity(input.getEnvironmentalConcern(), false));
        rootSpec.set(TAG_parameters, TAG_FINANCIAL_THRESHOLD, job.inlineEntity(input.getFinancialThreshold(), false));
        rootSpec.set(TAG_parameters, TAG_ADOPTION_THRESHOLD, job.inlineEntity(input.getAdoptionThreshold(), false));
        rootSpec.set(TAG_parameters, TAG_COMMUNICATION_FREQUENCY_SN, job.inlineEntity(input.getCommunication(), false));
        rootSpec.set(TAG_parameters, TAG_REWIRING_RATE, job.inlineEntity(input.getRewire(), false));
        rootSpec.set(TAG_parameters, TAG_INITIAL_ADOPTER, job.inlineEntity(input.getInitialAdopter(), false));
        rootSpec.set(TAG_parameters, TAG_INTEREST_THRESHOLD, job.inlineEntity(input.getInterestThreshold(), false));

        rootSpec.set(TAG_parameters, TAG_spatialDistribution, job.inlineEntity(input.getSpatialDistribution(), false));
    }
}
