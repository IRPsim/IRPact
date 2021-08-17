package de.unileipzig.irpact.io.spec.impl.affinity;

import de.unileipzig.irpact.commons.exception.ParsingException;
import de.unileipzig.irpact.core.logging.IRPLogging;
import de.unileipzig.irpact.io.param.input.affinity.InAffinityEntry;
import de.unileipzig.irpact.io.param.input.affinity.InComplexAffinityEntry;
import de.unileipzig.irpact.io.param.input.agent.consumer.InConsumerAgentGroup;
import de.unileipzig.irpact.io.spec.SpecificationHelper;
import de.unileipzig.irpact.io.spec.SpecificationJob;
import de.unileipzig.irpact.io.spec.impl.AbstractSpec;
import de.unileipzig.irptools.util.log.IRPLogger;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author Daniel Abitz
 */
public class AffinityEntrySpec extends AbstractSpec<InAffinityEntry> {

    private static final IRPLogger LOGGER = IRPLogging.getLogger(AffinityEntrySpec.class);

    public static final AffinityEntrySpec INSTANCE = new AffinityEntrySpec();

    @Override
    protected InAffinityEntry[] newArray(int len) {
        return new InAffinityEntry[len];
    }

    @Override
    protected IRPLogger logger() {
        return LOGGER;
    }

    @Override
    public InAffinityEntry[] toParamArray(SpecificationJob job) throws ParsingException {
        List<InAffinityEntry> entries = new ArrayList<>();
        SpecificationHelper srcSpec = SpecificationHelper.of(job.getData().getAffinities().get());
        for(Map.Entry<String, SpecificationHelper> srcEntry: srcSpec.iterateFields()) {
            String srcCagName = srcEntry.getKey();
            InConsumerAgentGroup srcCag = job.findConsumerAgentGroup(srcCagName);
            for(Map.Entry<String, SpecificationHelper> tarEntry: srcEntry.getValue().iterateFields()) {
                String tarCagName = tarEntry.getKey();
                InConsumerAgentGroup tarCag = job.findConsumerAgentGroup(tarCagName);
                double value = tarEntry.getValue().getDouble();

                InComplexAffinityEntry entry = new InComplexAffinityEntry(
                        job.concNames(srcCagName, tarCagName),
                        srcCag, tarCag, value
                );
                entries.add(entry);
            }
        }
        return entries.toArray(new InAffinityEntry[0]);
    }

    @Override
    public InAffinityEntry toParam(SpecificationHelper rootSpec, SpecificationJob job) throws ParsingException {
        throw new UnsupportedOperationException("toParamArray only");
    }

    @Override
    public Class<InAffinityEntry> getParamType() {
        return InAffinityEntry.class;
    }

    @Override
    public void toSpec(InAffinityEntry input, SpecificationJob job) throws ParsingException {
        create(input, job.getData().getAffinities().get(), job);
    }

    @Override
    protected void create(InAffinityEntry input, SpecificationHelper rootSpec, SpecificationJob job) throws ParsingException {
        SpecificationHelper spec = rootSpec.getOrCreateObject(input.getSrcCagName());
        spec.set(input.getTarCagName(), input.getAffinityValue());
    }
}
