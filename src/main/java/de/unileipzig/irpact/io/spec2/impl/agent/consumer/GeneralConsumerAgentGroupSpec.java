package de.unileipzig.irpact.io.spec2.impl.agent.consumer;

import de.unileipzig.irpact.commons.exception.ParsingException;
import de.unileipzig.irpact.core.log.IRPLogging;
import de.unileipzig.irpact.io.param.input.agent.consumer.InConsumerAgentGroupAttribute;
import de.unileipzig.irpact.io.param.input.agent.consumer.InGeneralConsumerAgentGroup;
import de.unileipzig.irpact.io.param.input.agent.consumer.InNameSplitConsumerAgentGroupAttribute;
import de.unileipzig.irpact.io.param.input.distribution.InUnivariateDoubleDistribution;
import de.unileipzig.irpact.io.spec2.SpecificationHelper2;
import de.unileipzig.irpact.io.spec2.SpecificationJob2;
import de.unileipzig.irpact.io.spec2.impl.AbstractSubSpec;
import de.unileipzig.irptools.util.log.IRPLogger;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static de.unileipzig.irpact.io.spec.SpecificationConstants.*;

/**
 * @author Daniel Abitz
 */
public class GeneralConsumerAgentGroupSpec extends AbstractSubSpec<InGeneralConsumerAgentGroup> {

    private static final IRPLogger LOGGER = IRPLogging.getLogger(GeneralConsumerAgentGroupSpec.class);

    public static final GeneralConsumerAgentGroupSpec INSTANCE = new GeneralConsumerAgentGroupSpec();
    public static final String TYPE = "GeneralConsumerAgentGroup";

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
        return input instanceof InGeneralConsumerAgentGroup;
    }

    @Override
    protected InGeneralConsumerAgentGroup[] newArray(int len) {
        return new InGeneralConsumerAgentGroup[len];
    }

    @Override
    protected IRPLogger logger() {
        return LOGGER;
    }

    @Override
    public InGeneralConsumerAgentGroup[] toParamArray(SpecificationJob2 job) throws ParsingException {
        return toParamArray(job.getData().getSpatialModel(), job);
    }

    @Override
    public InGeneralConsumerAgentGroup toParam(SpecificationHelper2 rootSpec, SpecificationJob2 job) throws ParsingException {
        String name = rootSpec.getText(TAG_name);
        if(job.isCached(name)) {
            return job.getCached(name);
        }

        InGeneralConsumerAgentGroup grp = new InGeneralConsumerAgentGroup();
        grp.setName(rootSpec.getText(TAG_name));

        grp.setNumberOfAgents(rootSpec.getInt(TAG_parameters, TAG_numberOfAgents));
        grp.setInformationAuthority(rootSpec.getDouble(TAG_parameters, TAG_informationAuthority));

        grp.setInterest(job.parseInlinedProductInterestSupplyScheme(rootSpec.getNode(TAG_interest)));
        grp.setSpatialDistribution(job.parseInlinedSpatialDistribution(rootSpec.getNode(TAG_spatialDistribution)));
        grp.setProductFindingScheme(job.parseInlinedProductFindingScheme(rootSpec.getNode(TAG_productFindingScheme)));

        List<InConsumerAgentGroupAttribute> attrList = new ArrayList<>();
        SpecificationHelper2 attrsSpec = rootSpec.getArray(TAG_attributes);
        for(SpecificationHelper2 attrSpec: attrsSpec.iterateElements()) {
            String attrName = attrSpec.getText(TAG_name);
            InUnivariateDoubleDistribution attrDist = job.parseInlinedDistribution(attrSpec.getNode(TAG_distribution));
            InNameSplitConsumerAgentGroupAttribute grpAttr = new InNameSplitConsumerAgentGroupAttribute();
            grpAttr.setName(grp.getName(), attrName);
            grpAttr.setDistribution(attrDist);
            attrList.add(grpAttr);
        }
        grp.setAttributes(attrList);

        job.cache(name, grp);
        return grp;
    }

    @Override
    public Class<InGeneralConsumerAgentGroup> getParamType() {
        return InGeneralConsumerAgentGroup.class;
    }

    @Override
    public void toSpec(InGeneralConsumerAgentGroup input, SpecificationJob2 job) throws ParsingException {
        create(input, job.getData().getSpatialModel(), job);
    }

    @Override
    protected void create(InGeneralConsumerAgentGroup input, SpecificationHelper2 rootSpec, SpecificationJob2 job) throws ParsingException {
        rootSpec.set(TAG_name, input.getName());
        rootSpec.set(TAG_type, TYPE);

        rootSpec.set(TAG_parameters, TAG_numberOfAgents, input.getNumberOfAgents());
        rootSpec.set(TAG_parameters, TAG_informationAuthority, input.getInformationAuthority());

        rootSpec.set(TAG_interest, job.inlineEntity(input.getInterest(), false));
        rootSpec.set(TAG_spatialDistribution, job.inlineEntity(input.getSpatialDistribution(), false));
        rootSpec.set(TAG_productFindingScheme, job.inlineEntity(input.getProductFindingScheme(), false));

        SpecificationHelper2 attrsSpec = rootSpec.getOrCreateArray(TAG_attributes);
        for(InConsumerAgentGroupAttribute grpAttr: input.getAttributes()) {
            SpecificationHelper2 attrSpec = attrsSpec.addObject();
            attrSpec.set(TAG_name, grpAttr.getAttributeName());
            attrSpec.set(TAG_distribution, job.inlineEntity(grpAttr.getDistribution(), false));
        }
    }
}
