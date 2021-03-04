package de.unileipzig.irpact.io.spec.impl.process;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import de.unileipzig.irpact.commons.exception.ParsingException;
import de.unileipzig.irpact.io.param.input.InAttributeName;
import de.unileipzig.irpact.io.param.input.agent.consumer.InConsumerAgentGroup;
import de.unileipzig.irpact.io.param.input.distribution.InUnivariateDoubleDistribution;
import de.unileipzig.irpact.io.param.input.process.InCustomUncertaintyGroupAttribute;
import de.unileipzig.irpact.io.spec.*;
import de.unileipzig.irpact.io.spec.impl.SpecUtil;

import java.util.ArrayList;
import java.util.List;

import static de.unileipzig.irpact.io.spec.SpecificationConstants.*;

/**
 * @author Daniel Abitz
 */
public class CustomUncertaintyGroupAttributeSpec
        implements ToSpecConverter<InCustomUncertaintyGroupAttribute>, ToParamConverter<InCustomUncertaintyGroupAttribute> {

    public static final CustomUncertaintyGroupAttributeSpec INSTANCE = new CustomUncertaintyGroupAttributeSpec();
    public static final String TYPE = "CustomUncertaintyGroupAttribute";

    @Override
    public Class<InCustomUncertaintyGroupAttribute> getParamType() {
        return InCustomUncertaintyGroupAttribute.class;
    }

    @Override
    public void toSpec(InCustomUncertaintyGroupAttribute input, SpecificationManager manager, SpecificationConverter converter, boolean inline) throws ParsingException {
        throw new UnsupportedOperationException();
    }

    @Override
    public void create(InCustomUncertaintyGroupAttribute input, ObjectNode root, SpecificationManager manager, SpecificationConverter converter, boolean inline) throws ParsingException {
        SpecificationHelper spec = new SpecificationHelper(root);
        spec.setName(input.getName());
        spec.setType(TYPE);

        SpecificationHelper cags = spec.getArraySpec(TAG_consumerAgentGroups);
        for(InConsumerAgentGroup cag: input.getGroups()) {
            cags.add(cag.getName());
        }

        SpecificationHelper attrs = spec.getArraySpec(TAG_attributes);
        for(InAttributeName attr: input.getNames()) {
            attrs.add(attr.getName());
        }

        SpecUtil.inlineDistribution(
                TAG_uncertainty,
                input.getUncertaintyDistribution(),
                spec,
                manager,
                converter,
                inline
        );
        SpecUtil.inlineDistribution(
                TAG_convergence,
                input.getConvergenceDistribution(),
                spec,
                manager,
                converter,
                inline
        );
    }

    @Override
    public InCustomUncertaintyGroupAttribute toParam(ObjectNode root, SpecificationManager manager, SpecificationConverter converter, SpecificationCache cache) {
        try {
            SpecificationHelper spec = new SpecificationHelper(root);
            InCustomUncertaintyGroupAttribute attr = new InCustomUncertaintyGroupAttribute();
            attr.setName(spec.getName());

            List<InConsumerAgentGroup> cags = new ArrayList<>();
            SpecificationHelper cagsSpec = spec.getArraySpec(TAG_consumerAgentGroups);
            for(JsonNode childNode: cagsSpec.iterateElements()) {
                String cagName = childNode.textValue();
                InConsumerAgentGroup cag = converter.getConsumerAgentGroup(cagName, manager, cache);
                cags.add(cag);
            }
            attr.cags = cags.toArray(new InConsumerAgentGroup[0]);

            List<InAttributeName> attrNames = new ArrayList<>();
            SpecificationHelper attrsSpec = spec.getArraySpec(TAG_attributes);
            for(JsonNode childNode: attrsSpec.iterateElements()) {
                String attrStr = childNode.textValue();
                InAttributeName attrName = cache.getAttrName(attrStr);
                attrNames.add(attrName);
            }
            attr.names = attrNames.toArray(new InAttributeName[0]);

            InUnivariateDoubleDistribution uncertDist = SpecUtil.parseInlinedDistribution(
                    TAG_uncertainty,
                    spec,
                    manager,
                    converter,
                    cache
            );
            attr.setUncertaintyDistribution(uncertDist);

            InUnivariateDoubleDistribution convDist = SpecUtil.parseInlinedDistribution(
                    TAG_convergence,
                    spec,
                    manager,
                    converter,
                    cache
            );
            attr.setConvergenceDistribution(convDist);

            return attr;
        } catch (ParsingException e) {
            throw e.unchecked();
        }
    }
}
