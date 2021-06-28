package de.unileipzig.irpact.io.param.input.agent.population;

import de.unileipzig.irpact.commons.exception.ParsingException;
import de.unileipzig.irpact.core.agent.consumer.ConsumerAgentGroup;
import de.unileipzig.irpact.core.agent.population.AgentPopulation;
import de.unileipzig.irpact.core.logging.IRPLogging;
import de.unileipzig.irpact.core.logging.IRPSection;
import de.unileipzig.irpact.io.param.ParamUtil;
import de.unileipzig.irpact.core.start.IRPactInputParser;
import de.unileipzig.irpact.io.param.input.agent.consumer.InConsumerAgentGroup;
import de.unileipzig.irptools.defstructure.annotation.Definition;
import de.unileipzig.irptools.defstructure.annotation.FieldDefinition;
import de.unileipzig.irptools.util.CopyCache;
import de.unileipzig.irptools.util.TreeAnnotationResource;
import de.unileipzig.irptools.util.log.IRPLogger;

import java.lang.invoke.MethodHandles;
import java.util.Collection;

import static de.unileipzig.irpact.io.param.IOConstants.*;
import static de.unileipzig.irpact.io.param.ParamUtil.addEntry;
import static de.unileipzig.irpact.io.param.ParamUtil.putClassPath;

/**
 * @author Daniel Abitz
 */
@Definition
public class InFixConsumerAgentPopulation implements InAgentPopulation {

    private static final MethodHandles.Lookup L = MethodHandles.lookup();
    public static Class<?> thisClass() {
        return L.lookupClass();
    }
    public static String thisName() {
        return thisClass().getSimpleName();
    }

    private static final IRPLogger LOGGER = IRPLogging.getLogger(thisClass());

    public static void initRes(TreeAnnotationResource res) {
    }
    public static void applyRes(TreeAnnotationResource res) {
        putClassPath(res, thisClass(), AGENTS, POPULATION, thisName());
        addEntry(res, thisClass(), "size");
        addEntry(res, thisClass(), "cags");
    }

    public String _name;

    @FieldDefinition
    public int size;

    @FieldDefinition
    public InConsumerAgentGroup[] cags;

    public InFixConsumerAgentPopulation() {
    }

    public InFixConsumerAgentPopulation(String name, InConsumerAgentGroup cag, int size) {
        setName(name);
        setConsumerAgentGroup(cag);
        setSize(size);
    }

    @Override
    public InFixConsumerAgentPopulation copy(CopyCache cache) {
        return cache.copyIfAbsent(this, this::newCopy);
    }

    public InFixConsumerAgentPopulation newCopy(CopyCache cache) {
        InFixConsumerAgentPopulation copy = new InFixConsumerAgentPopulation();
        copy._name = _name;
        copy.size = size;
        copy.cags = cache.copyArray(cags);
        return copy;
    }

    @Override
    public String getName() {
        return _name;
    }

    public void setName(String name) {
        this._name = name;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public InConsumerAgentGroup[] getConsumerAgentGroups() throws ParsingException {
        return ParamUtil.getNonEmptyArray(cags, "consumerAgentGroups");
    }

    public void setConsumerAgentGroup(InConsumerAgentGroup cag) {
        this.cags = new InConsumerAgentGroup[]{cag};
    }

    public void setConsumerAgentGroups(InConsumerAgentGroup[] cags) {
        this.cags = cags;
    }

    public void setConsumerAgentGroups(Collection<? extends InConsumerAgentGroup> cags) {
        this.cags = cags.toArray(new InConsumerAgentGroup[0]);
    }

    @Override
    public void setup(IRPactInputParser parser, Object input) throws ParsingException {
        AgentPopulation initData = parser.getEnvironment().getAgents().getInitialAgentPopulation();
        for(InConsumerAgentGroup inCag: getConsumerAgentGroups()) {
            ConsumerAgentGroup cag = parser.parseEntityTo(inCag);
            if(initData.has(cag)) {
                throw new ParsingException("cag '" + cag.getName() + "' already has a population size: " + initData.get(cag) + " (try to set: " + size + ")");
            }
            LOGGER.trace(IRPSection.INITIALIZATION_PARAMETER, "set initial number of consumer agents '{}': {}", cag.getName(), size);
            initData.set(cag, size);
        }
    }
}
