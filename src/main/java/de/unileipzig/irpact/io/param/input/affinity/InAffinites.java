package de.unileipzig.irpact.io.param.input.affinity;

import de.unileipzig.irpact.commons.exception.ParsingException;
import de.unileipzig.irpact.core.agent.consumer.BasicConsumerAgentGroupAffinityMapping;
import de.unileipzig.irpact.core.agent.consumer.ConsumerAgentGroup;
import de.unileipzig.irpact.core.log.IRPLogging;
import de.unileipzig.irpact.io.param.ParamUtil;
import de.unileipzig.irpact.io.param.input.IRPactInputParser;
import de.unileipzig.irpact.io.param.input.InIRPactEntity;
import de.unileipzig.irpact.io.param.input.agent.consumer.InConsumerAgentGroup;
import de.unileipzig.irptools.defstructure.annotation.Definition;
import de.unileipzig.irptools.defstructure.annotation.FieldDefinition;
import de.unileipzig.irptools.util.CopyCache;
import de.unileipzig.irptools.util.TreeAnnotationResource;
import de.unileipzig.irptools.util.log.IRPLogger;

import java.lang.invoke.MethodHandles;
import java.util.Collection;
import java.util.NoSuchElementException;
import java.util.Objects;

import static de.unileipzig.irpact.io.param.IOConstants.*;
import static de.unileipzig.irpact.io.param.ParamUtil.addEntry;
import static de.unileipzig.irpact.io.param.ParamUtil.putClassPath;

/**
 * @author Daniel Abitz
 */
@Definition
public class InAffinites implements InIRPactEntity {

    private static final MethodHandles.Lookup L = MethodHandles.lookup();
    public static Class<?> thisClass() {
        return L.lookupClass();
    }
    public static String thisName() {
        return thisClass().getSimpleName();
    }

    public static void initRes(TreeAnnotationResource res) {
    }
    public static void applyRes(TreeAnnotationResource res) {
        putClassPath(res, thisClass(), AGENTS, CONSUMER, CONSUMER_AFFINITY, thisName());
        addEntry(res, thisClass(), "entries");
    }

    private static final IRPLogger LOGGER = IRPLogging.getLogger(thisClass());

    public String _name;

    @FieldDefinition
    public InAffinityEntry[] entries;

    public InAffinites() {
    }

    @Override
    public InAffinites copy(CopyCache cache) {
        return cache.copyIfAbsent(this, this::newCopy);
    }

    public InAffinites newCopy(CopyCache cache) {
        InAffinites copy = new InAffinites();
        copy._name = _name;
        copy.entries = cache.copyArray(entries);
        return copy;
    }

    @Override
    public String getName() {
        return _name;
    }

    public void setName(String name) {
        this._name = name;
    }

    public InAffinityEntry findEntry(String srcCag, String tarCag) throws NoSuchElementException, ParsingException {
        if(entries == null) {
            throw new NoSuchElementException();
        }
        for(InAffinityEntry entry: entries) {
            if(Objects.equals(srcCag, entry.getSrcCagName()) && Objects.equals(tarCag, entry.getTarCagName())) {
                return entry;
            }
        }
        throw new NoSuchElementException();
    }

    public void setEntries(Collection<? extends InAffinityEntry> entries) {
        this.entries = entries.toArray(new InAffinityEntry[0]);
    }

    public void setEntries(InAffinityEntry[] entries) {
        this.entries = entries;
    }

    public InAffinityEntry[] getEntries() throws ParsingException {
        return ParamUtil.getNonNullArray(entries, "entries");
    }

    @Override
    public BasicConsumerAgentGroupAffinityMapping parse(IRPactInputParser parser) throws ParsingException {

        if(parser.isRestored() && parser.getEnvironment().getAgents().hasConsumerAgentGroupAffinityMapping()) {
            BasicConsumerAgentGroupAffinityMapping mapping = (BasicConsumerAgentGroupAffinityMapping) parser.getEnvironment().getAgents().getConsumerAgentGroupAffinityMapping();
            return updateRestored(parser, mapping);
        }

        BasicConsumerAgentGroupAffinityMapping affinities = new BasicConsumerAgentGroupAffinityMapping();

        for(InAffinityEntry entry: getEntries()) {
            InConsumerAgentGroup srcInCag = entry.getSrcCag(parser);
            ConsumerAgentGroup srcCag = parser.parseEntityTo(srcInCag);
            InConsumerAgentGroup tarInCag = entry.getTarCag(parser);
            ConsumerAgentGroup tarCag = parser.parseEntityTo(tarInCag);

            double value = entry.getAffinityValue();
            if(value == 0.0) {
                LOGGER.debug("skip affinity '{}' -> '{}' with value '{}'", srcCag.getName(), tarCag.getName(), value);
            } else {
                LOGGER.trace("add affinity '{}' -> '{}': {}", srcCag.getName(), tarCag.getName(), entry.getAffinityValue());
                affinities.put(srcCag, tarCag, entry.getAffinityValue());
            }
        }

        return affinities;
    }

    public BasicConsumerAgentGroupAffinityMapping updateRestored(IRPactInputParser parser, BasicConsumerAgentGroupAffinityMapping restored) {
        LOGGER.trace("affinities already exists");
        return restored;
    }
}
