package de.unileipzig.irpact.io.spec.impl.affinity;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import de.unileipzig.irpact.commons.exception.ParsingException;
import de.unileipzig.irpact.io.param.input.affinity.InComplexAffinityEntry;
import de.unileipzig.irpact.io.param.input.agent.consumer.InConsumerAgentGroup;
import de.unileipzig.irpact.io.spec.SpecificationConverter;
import de.unileipzig.irpact.io.spec.SpecificationManager;
import de.unileipzig.irpact.io.spec.impl.SpecBase;
import de.unileipzig.irptools.util.Util;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author Daniel Abitz
 */
public class ComplexAffinityEntrySpec extends SpecBase<InComplexAffinityEntry, InComplexAffinityEntry[]> {

    public static final ComplexAffinityEntrySpec INSTANCE = new ComplexAffinityEntrySpec();

    public ComplexAffinityEntrySpec() {
    }

    @Override
    public InComplexAffinityEntry[] toParam(
            SpecificationManager manager,
            Map<String, Object> cache) {
        List<InComplexAffinityEntry> entryList = new ArrayList<>();
        ObjectNode affinityRoot = manager.getAffinities();
        for(Map.Entry<String, JsonNode> srcEntry: Util.iterateFields(affinityRoot)) {
            InConsumerAgentGroup srcCag = find(cache, srcEntry.getKey());
            for(Map.Entry<String, JsonNode> tarEntry: Util.iterateFields(srcEntry.getValue())) {
                InConsumerAgentGroup tarCag = find(cache, tarEntry.getKey());
                double value = tarEntry.getValue().doubleValue();
                InComplexAffinityEntry entry = new InComplexAffinityEntry(srcCag.getName() + "_" + tarCag.getName(), srcCag, tarCag, value);
                entryList.add(entry);
            }
        }
        return entryList.toArray(new InComplexAffinityEntry[0]);
    }

    @Override
    public Class<InComplexAffinityEntry> getParamType() {
        return InComplexAffinityEntry.class;
    }

    @Override
    public void toSpec(
            InComplexAffinityEntry instance,
            SpecificationManager manager,
            SpecificationConverter converter) throws ParsingException {
        ObjectNode root = manager.getAffinities();
        ObjectNode srcNode = Util.getOrCreateObject(root, instance.getSrcCag().getName());
        srcNode.put(instance.getTarCag().getName(), instance.getAffinityValue());
    }
}
