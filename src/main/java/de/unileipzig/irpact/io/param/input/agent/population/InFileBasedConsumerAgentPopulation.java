package de.unileipzig.irpact.io.param.input.agent.population;

import de.unileipzig.irpact.commons.Nameable;
import de.unileipzig.irpact.commons.exception.ParsingException;
import de.unileipzig.irpact.core.agent.consumer.ConsumerAgentGroup;
import de.unileipzig.irpact.core.agent.population.AgentPopulation;
import de.unileipzig.irpact.core.logging.IRPLogging;
import de.unileipzig.irpact.core.logging.IRPSection;
import de.unileipzig.irpact.core.spatial.SpatialTableFileContent;
import de.unileipzig.irpact.core.spatial.SpatialUtil;
import de.unileipzig.irpact.io.param.ParamUtil;
import de.unileipzig.irpact.io.param.input.IRPactInputParser;
import de.unileipzig.irpact.io.param.input.names.InAttributeName;
import de.unileipzig.irpact.io.param.input.InputParser;
import de.unileipzig.irpact.io.param.input.agent.consumer.InConsumerAgentGroup;
import de.unileipzig.irpact.io.param.input.file.InSpatialTableFile;
import de.unileipzig.irptools.defstructure.annotation.Definition;
import de.unileipzig.irptools.defstructure.annotation.FieldDefinition;
import de.unileipzig.irptools.util.CopyCache;
import de.unileipzig.irptools.util.TreeAnnotationResource;
import de.unileipzig.irptools.util.log.IRPLogger;

import java.lang.invoke.MethodHandles;
import java.util.*;
import java.util.stream.Collectors;

import static de.unileipzig.irpact.io.param.IOConstants.AGENTS;
import static de.unileipzig.irpact.io.param.IOConstants.POPULATION;
import static de.unileipzig.irpact.io.param.ParamUtil.addEntry;
import static de.unileipzig.irpact.io.param.ParamUtil.putClassPath;

/**
 * @author Daniel Abitz
 */
@Definition
public class InFileBasedConsumerAgentPopulation implements InAgentPopulation {

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
        putClassPath(res, thisClass(), AGENTS, POPULATION, thisName());
        addEntry(res, thisClass(), "desiredSize");
        addEntry(res, thisClass(), "useAll");
        addEntry(res, thisClass(), "requiresDesiredTotalSize");
        addEntry(res, thisClass(), "cags");
        addEntry(res, thisClass(), "file");
        addEntry(res, thisClass(), "selectKey");
    }

    private static final IRPLogger LOGGER = IRPLogging.getLogger(InFileBasedConsumerAgentPopulation.class);

    public String _name;

    @FieldDefinition
    public int desiredSize;

    @FieldDefinition
    public boolean useAll;

    @FieldDefinition
    public boolean requiresDesiredTotalSize;

    @FieldDefinition
    public InConsumerAgentGroup[] cags;

    @FieldDefinition
    public InSpatialTableFile[] file;

    @FieldDefinition
    public InAttributeName[] selectKey;

    public InFileBasedConsumerAgentPopulation() {
    }

    @Override
    public InFileBasedConsumerAgentPopulation copy(CopyCache cache) {
        return cache.copyIfAbsent(this, this::newCopy);
    }

    public InFileBasedConsumerAgentPopulation newCopy(CopyCache cache) {
        InFileBasedConsumerAgentPopulation copy = new InFileBasedConsumerAgentPopulation();
        copy._name = _name;
        copy.desiredSize = desiredSize;
        copy.requiresDesiredTotalSize = requiresDesiredTotalSize;
        copy.cags = cache.copyArray(cags);
        copy.file = cache.copyArray(file);
        copy.selectKey = cache.copyArray(selectKey);
        return copy;
    }

    @Override
    public String getName() {
        return _name;
    }

    public void setName(String name) {
        this._name = name;
    }

    public void setDesiredSize(int desiredSize) {
        this.desiredSize = desiredSize;
    }

    public int getDesiredSize() {
        return desiredSize;
    }

    public void setRequiresDesiredTotalSize(boolean requiresDesiredTotalSize) {
        this.requiresDesiredTotalSize = requiresDesiredTotalSize;
    }

    public boolean isRequiresDesiredTotalSize() {
        return requiresDesiredTotalSize;
    }

    public void setUseAll(boolean useAll) {
        this.useAll = useAll;
    }

    public boolean isUseAll() {
        return useAll;
    }

    public InConsumerAgentGroup[] getConsumerAgentGroups() throws ParsingException {
        return ParamUtil.getNonEmptyArray(cags, "consumerAgentGroups");
    }

    public void setConsumerAgentGroups(InConsumerAgentGroup[] cags) {
        this.cags = cags;
    }

    public void setConsumerAgentGroups(Collection<? extends InConsumerAgentGroup> cags) {
        this.cags = cags.toArray(new InConsumerAgentGroup[0]);
    }

    public void setFile(InSpatialTableFile attrFile) {
        this.file = new InSpatialTableFile[]{attrFile};
    }

    public InSpatialTableFile getFile() throws ParsingException {
        return ParamUtil.getInstance(file, "file");
    }

    public void setSelectKey(InAttributeName selectKey) {
        this.selectKey = new InAttributeName[]{selectKey};
    }

    public InAttributeName getSelectKey() throws ParsingException {
        return ParamUtil.getInstance(selectKey, "selectKey");
    }

    @Override
    public void setup(IRPactInputParser parser, Object input) throws ParsingException {
        List<ConsumerAgentGroup> cags = parseCags(parser);
        List<String> cagNames = cags.stream().map(Nameable::getName).collect(Collectors.toList());
        String selectKey = getSelectKey().getName();
        SpatialTableFileContent fileContent = parser.parseEntityTo(getFile());

        Map<String, Integer> sizes = SpatialUtil.calculateSizes(
                fileContent.content(),
                selectKey,
                cagNames,
                isUseAll() ? -1 : getDesiredSize(),
                isRequiresDesiredTotalSize()
        );

        AgentPopulation population = parser.getEnvironment().getAgents().getInitialAgentPopulation();
        for(Map.Entry<String, Integer> entry: sizes.entrySet()) {
            ConsumerAgentGroup cag = getCag(entry.getKey(), cags);
            if(population.has(cag)) {
                throw new ParsingException("cag '{}' already has size {} (try to set {})", cag.getName(), population.get(cag), entry.getValue());
            }
            population.set(cag, entry.getValue());
            LOGGER.trace(IRPSection.INITIALIZATION_PARAMETER, "set initial size for cag '{}': {}", cag.getName(), entry.getValue());
        }

    }

    protected List<ConsumerAgentGroup> parseCags(InputParser parser) throws ParsingException {
        List<ConsumerAgentGroup> cags = new ArrayList<>();
        for(InConsumerAgentGroup inCag: getConsumerAgentGroups()) {
            ConsumerAgentGroup cag = parser.parseEntityTo(inCag);
            cags.add(cag);
        }
        return cags;
    }

    protected static ConsumerAgentGroup getCag(String name, Collection<? extends ConsumerAgentGroup> cags) {
        for(ConsumerAgentGroup cag: cags) {
            if(name.equals(cag.getName())) {
                return cag;
            }
        }
        throw new NoSuchElementException("cag: " + name);
    }
}
