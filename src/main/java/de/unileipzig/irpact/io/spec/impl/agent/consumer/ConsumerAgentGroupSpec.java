package de.unileipzig.irpact.io.spec.impl.agent.consumer;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import de.unileipzig.irpact.commons.exception.ParsingException;
import de.unileipzig.irpact.io.param.input.agent.consumer.InConsumerAgentGroup;
import de.unileipzig.irpact.io.param.input.agent.consumer.InConsumerAgentGroupAttribute;
import de.unileipzig.irpact.io.param.input.distribution.InUnivariateDoubleDistribution;
import de.unileipzig.irpact.io.param.input.interest.InProductInterestSupplyScheme;
import de.unileipzig.irpact.io.param.input.product.InProductFindingScheme;
import de.unileipzig.irpact.io.param.input.spatial.dist.InSpatialDistribution;
import de.unileipzig.irpact.io.spec.*;
import de.unileipzig.irpact.io.spec.impl.SpecUtil;
import de.unileipzig.irpact.io.spec.impl.interest.ProductInterestSupplySchemeSpec;
import de.unileipzig.irpact.io.spec.impl.product.ProductFindingSchemeSpec;
import de.unileipzig.irpact.io.spec.impl.spatial.dist.SpatialDistributionSpec;

import java.util.ArrayList;
import java.util.List;

import static de.unileipzig.irpact.io.spec.SpecificationConstants.*;

/**
 * @author Daniel Abitz
 */
public class ConsumerAgentGroupSpec
        implements ToSpecConverter<InConsumerAgentGroup>, ToParamConverter<InConsumerAgentGroup> {

    public static final ConsumerAgentGroupSpec INSTANCE = new ConsumerAgentGroupSpec();

    @Override
    public Class<InConsumerAgentGroup> getParamType() {
        return InConsumerAgentGroup.class;
    }

    @Override
    public void toSpec(InConsumerAgentGroup input, SpecificationManager manager, SpecificationConverter converter, boolean inline) throws ParsingException {
        create(input, manager.getConsumerAgentGroups().get(input.getName()), manager, converter, inline);
    }

    @Override
    public void create(InConsumerAgentGroup input, ObjectNode root, SpecificationManager manager, SpecificationConverter converter, boolean inline) throws ParsingException {
        SpecificationHelper spec = new SpecificationHelper(root);
        spec.setName(input.getName());
        spec.setNumberOfAgents(input.getNumberOfAgents());
        spec.set(TAG_informationAuthority, input.getInformationAuthority());

        SpecUtil.inline(
                input.getInterest(), input.getInterest().getName(),
                TAG_interest,
                manager,
                spec,
                converter,
                true
        );
        SpecUtil.inline(
                input.getSpatialDistribution(), input.getSpatialDistribution().getName(),
                TAG_spatialDistribution,
                manager,
                spec,
                converter,
                true
        );
        SpecUtil.inline(
                input.getProductFindingScheme(), input.getProductFindingScheme().getName(),
                TAG_productFindingScheme,
                manager,
                spec,
                converter,
                true
        );

        SpecificationHelper attrsSpec = spec.getAttributesSpec();
        for(InConsumerAgentGroupAttribute inAttr: input.getAttributes()) {
            SpecificationHelper attrSpec = attrsSpec.addObjectSpec();
            attrSpec.setName(inAttr.getCagAttrName().getName());

            SpecUtil.inlineDistribution(
                    inAttr.getCagAttrDistribution(),
                    attrSpec,
                    manager,
                    converter,
                    inline
            );
        }
    }

    @Override
    public InConsumerAgentGroup[] toParam(SpecificationManager manager, SpecificationConverter converter, SpecificationCache cache) {
        List<InConsumerAgentGroup> groups = new ArrayList<>();
        for(ObjectNode root: manager.getConsumerAgentGroups().getAll().values()) {
            InConsumerAgentGroup cag = toParam(root, manager, converter, cache);
            groups.add(cag);
        }
        return groups.toArray(new InConsumerAgentGroup[0]);
    }

    @Override
    public InConsumerAgentGroup toParam(ObjectNode root, SpecificationManager manager, SpecificationConverter converter, SpecificationCache cache) {
        SpecificationHelper spec = new SpecificationHelper(root);
        String name = spec.getName();

        if(cache.has(name)) {
            return cache.getAs(name);
        }

        InProductInterestSupplyScheme interestScheme = SpecUtil.parseInlined(
                TAG_interest,
                spec,
                _name -> {
                    try {
                        return ProductInterestSupplySchemeSpec.INSTANCE.toParamByName(_name, manager, converter, cache);
                    } catch (ParsingException e) {
                        throw e.unchecked();
                    }
                },
                _spec -> ProductInterestSupplySchemeSpec.INSTANCE.toParam(_spec.rootAsObject(), manager, converter, cache)
        );

        InSpatialDistribution spatialDistribution = SpecUtil.parseInlined(
                TAG_spatialDistribution,
                spec,
                _name -> {
                    try {
                        return SpatialDistributionSpec.INSTANCE.toParamByName(_name, manager, converter, cache);
                    } catch (ParsingException e) {
                        throw e.unchecked();
                    }
                },
                _spec -> SpatialDistributionSpec.INSTANCE.toParam(_spec.rootAsObject(), manager, converter, cache)
        );

        InProductFindingScheme findingScheme = SpecUtil.parseInlined(
                TAG_productFindingScheme,
                spec,
                _name -> {
                    try {
                        return ProductFindingSchemeSpec.INSTANCE.toParamByName(_name, manager, converter, cache);
                    } catch (ParsingException e) {
                        throw e.unchecked();
                    }
                },
                _spec -> ProductFindingSchemeSpec.INSTANCE.toParam(_spec.rootAsObject(), manager, converter, cache)
        );

        InConsumerAgentGroup cag = new InConsumerAgentGroup();
        cag._name = name;
        cag.numberOfAgentsX = spec.getNumberOfAgents();
        cag.informationAuthority = spec.getDouble(TAG_informationAuthority);
        cag.setAwareness(interestScheme);
        cag.setSpatialDistribution(spatialDistribution);
        cag.setProductFindingScheme(findingScheme);

        List<InConsumerAgentGroupAttribute> attrList = new ArrayList<>();
        SpecificationHelper attrsSpec = spec.getAttributesSpec();
        for(JsonNode attrNode: attrsSpec.iterateElements()) {
            SpecificationHelper attrSpec = new SpecificationHelper(attrNode);
            String attrName = attrSpec.getName();
            InUnivariateDoubleDistribution attrDist = SpecUtil.parseInlinedDistribution(
                    attrSpec,
                    manager,
                    converter,
                    cache
            );
            InConsumerAgentGroupAttribute attr = new InConsumerAgentGroupAttribute(name + "_" + attrName , cache.getAttrName(attrName), attrDist);
            attrList.add(attr);
        }
        cag.cagAttributes = attrList.toArray(new InConsumerAgentGroupAttribute[0]);

        cache.securePut(name, cag);
        return cag;
    }
}
