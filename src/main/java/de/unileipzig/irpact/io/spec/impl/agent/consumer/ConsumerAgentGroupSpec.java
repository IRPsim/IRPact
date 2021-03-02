package de.unileipzig.irpact.io.spec.impl.agent.consumer;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import de.unileipzig.irpact.io.param.input.InAttributeName;
import de.unileipzig.irpact.io.param.input.agent.consumer.InConsumerAgentGroup;
import de.unileipzig.irpact.io.param.input.agent.consumer.InConsumerAgentGroupAttribute;
import de.unileipzig.irpact.io.param.input.distribution.InUnivariateDoubleDistribution;
import de.unileipzig.irpact.io.param.input.interest.InProductInterestSupplyScheme;
import de.unileipzig.irpact.io.param.input.product.InProductFindingScheme;
import de.unileipzig.irpact.io.param.input.spatial.dist.InSpatialDistribution;
import de.unileipzig.irpact.io.spec.SpecificationConverter;
import de.unileipzig.irpact.io.spec.SpecificationHelper;
import de.unileipzig.irpact.io.spec.SpecificationManager;
import de.unileipzig.irpact.io.spec.impl.SpecBase;
import de.unileipzig.irptools.util.Util;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static de.unileipzig.irpact.io.spec.SpecificationConstants.*;

/**
 * @author Daniel Abitz
 */
public class ConsumerAgentGroupSpec extends SpecBase<InConsumerAgentGroup, InConsumerAgentGroup[]> {

    public static final ConsumerAgentGroupSpec INSTANCE = new ConsumerAgentGroupSpec();

    public ConsumerAgentGroupSpec() {
    }

    @Override
    public InConsumerAgentGroup[] toParam(
            SpecificationManager manager,
            Map<String, Object> cache) {
        List<InConsumerAgentGroup> cagList = new ArrayList<>();
        for(Map.Entry<String, ObjectNode> cagEntry: manager.getConsumerAgentGroups().entrySet()) {
            SpecificationHelper spec = new SpecificationHelper(cagEntry.getValue());
            String cagName = spec.getName();
            int numberOfAgents = spec.getInt(TAG_numberOfAgents);
            InProductInterestSupplyScheme interest = find(cache, spec.getText(TAG_interest));

            List<InConsumerAgentGroupAttribute> attrList = new ArrayList<>();
            for(JsonNode attrNode: Util.iterateElements(spec.getAttributes())) {
                SpecificationHelper attrSpec = new SpecificationHelper(attrNode);
                String attrName = cagName + "_" + attrSpec.getName();
                InAttributeName inName = putIfMissing(cache, attrSpec.getName(), new InAttributeName(attrSpec.getName()));
                InUnivariateDoubleDistribution attrDist = find(cache, attrSpec.getText(TAG_distribution));
                InConsumerAgentGroupAttribute cagAttr = new InConsumerAgentGroupAttribute(
                        attrName,
                        inName,
                        attrDist
                );
                attrList.add(cagAttr);
            }

            InConsumerAgentGroup cag = new InConsumerAgentGroup(
                    cagName,
                    1.0, //!!!
                    numberOfAgents,
                    attrList,
                    interest
            );

            InSpatialDistribution spatialDist = find(cache, spec.getText(TAG_spatialDistribution));
            cag.setSpatialDistribution(spatialDist);


            InProductFindingScheme findingScheme = find(cache, spec.getText(TAG_productFindingScheme));
            cag.setProductFindingScheme(findingScheme);

            cagList.add(cag);
        }

        return cagList.toArray(new InConsumerAgentGroup[0]);
    }

    @Override
    public Class<InConsumerAgentGroup> getParamType() {
        return InConsumerAgentGroup.class;
    }

    @Override
    public void toSpec(
            InConsumerAgentGroup instance,
            SpecificationManager manager,
            SpecificationConverter converter) {
        SpecificationHelper spec = new SpecificationHelper(manager.getConsumerAgentGroup(instance.getName()));
        spec.setName(instance.getName());
        spec.setNumberOfAgents(instance.getNumberOfAgents());
        spec.set(TAG_interest, instance.getAwareness().getName());
        spec.set(TAG_spatialDistribution, instance.getSpatialDistribution().getName());
        spec.set(TAG_productFindingScheme, instance.getProductFindingScheme().getName());
        spec.set(TAG_informationAuthority, instance.getInformationAuthority());

        ArrayNode arr = spec.getAttributes();
        for(InConsumerAgentGroupAttribute inAttr: instance.getAttributes()) {
            SpecificationHelper specAttr = new SpecificationHelper(arr.addObject());
            specAttr.setName(inAttr.getCagAttrName().getName());
            specAttr.setDistribution(inAttr.getCagAttrDistribution().getName());

            converter.apply(manager, inAttr.getCagAttrDistribution());
        }

        converter.apply(manager, instance.getAwareness());
    }
}
