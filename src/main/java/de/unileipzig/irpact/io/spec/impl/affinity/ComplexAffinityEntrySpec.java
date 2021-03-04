package de.unileipzig.irpact.io.spec.impl.affinity;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import de.unileipzig.irpact.commons.exception.ParsingException;
import de.unileipzig.irpact.io.param.input.affinity.InComplexAffinityEntry;
import de.unileipzig.irpact.io.param.input.agent.consumer.InConsumerAgentGroup;
import de.unileipzig.irpact.io.spec.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author Daniel Abitz
 */
public class ComplexAffinityEntrySpec
        implements ToSpecConverter<InComplexAffinityEntry>, ToParamConverter<InComplexAffinityEntry> {

    public static final ComplexAffinityEntrySpec INSTANCE = new ComplexAffinityEntrySpec();

    @Override
    public Class<InComplexAffinityEntry> getParamType() {
        return InComplexAffinityEntry.class;
    }

    @Override
    public void toSpec(InComplexAffinityEntry input, SpecificationManager manager, SpecificationConverter converter, boolean inline) throws ParsingException {
        create(input, manager.getAffinities().get(), manager, converter, inline);
    }

    @Override
    public void create(InComplexAffinityEntry input, ObjectNode root, SpecificationManager manager, SpecificationConverter converter, boolean inline) throws ParsingException {
        SpecificationHelper rootSpec = new SpecificationHelper(root);
        SpecificationHelper spec = rootSpec.getObjectSpec(input.getSrcCag().getName());
        spec.set(input.getTarCag().getName(), input.getAffinityValue());
    }

    @Override
    public InComplexAffinityEntry[] toParam(SpecificationManager manager, SpecificationConverter converter, SpecificationCache cache) throws ParsingException {
        List<InComplexAffinityEntry> entries = new ArrayList<>();
        SpecificationHelper srcSpec = new SpecificationHelper(manager.getAffinities().get());
        for(Map.Entry<String, JsonNode> srcEntry: srcSpec.iterateFields()) {
            String srcCagName = srcEntry.getKey();
            InConsumerAgentGroup srcCag = converter.getConsumerAgentGroup(srcCagName, manager, cache);
            SpecificationHelper tarSpec = new SpecificationHelper(srcEntry.getValue());
            for(Map.Entry<String, JsonNode> tarEntry: tarSpec.iterateFields()) {
                String tarCagName = tarEntry.getKey();
                InConsumerAgentGroup tarCag = converter.getConsumerAgentGroup(tarCagName, manager, cache);
                double value = tarEntry.getValue().doubleValue();
                entries.add(new InComplexAffinityEntry(srcCagName + "_" + tarCagName, srcCag, tarCag, value));
            }
        }
        return entries.toArray(new InComplexAffinityEntry[0]);
    }
}
