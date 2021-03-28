package de.unileipzig.irpact.io.param.input.agent.population;

import de.unileipzig.irpact.commons.util.ShareCalculator;
import de.unileipzig.irpact.commons.exception.ParsingException;
import de.unileipzig.irpact.core.agent.consumer.ConsumerAgentGroup;
import de.unileipzig.irpact.core.log.IRPLogging;
import de.unileipzig.irpact.core.log.IRPSection;
import de.unileipzig.irpact.core.simulation.InitializationData;
import de.unileipzig.irpact.core.spatial.SpatialUtil;
import de.unileipzig.irpact.core.spatial.attribute.SpatialAttribute;
import de.unileipzig.irpact.io.param.ParamUtil;
import de.unileipzig.irpact.io.param.input.InAttributeName;
import de.unileipzig.irpact.io.param.input.InputParser;
import de.unileipzig.irpact.io.param.input.agent.consumer.InConsumerAgentGroup;
import de.unileipzig.irpact.io.param.input.file.InSpatialTableFile;
import de.unileipzig.irptools.defstructure.annotation.Definition;
import de.unileipzig.irptools.defstructure.annotation.FieldDefinition;
import de.unileipzig.irptools.util.TreeAnnotationResource;
import de.unileipzig.irptools.util.log.IRPLogger;

import java.lang.invoke.MethodHandles;
import java.util.*;

import static de.unileipzig.irpact.io.param.IOConstants.AGENTS;
import static de.unileipzig.irpact.io.param.IOConstants.POPULATION;
import static de.unileipzig.irpact.io.param.ParamUtil.addEntry;
import static de.unileipzig.irpact.io.param.ParamUtil.putClassPath;

/**
 * @author Daniel Abitz
 */
@Definition
public class InRelativeExternConsumerAgentPopulationSize implements InPopulationSize {

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
        addEntry(res, thisClass(), "totalSize");
        addEntry(res, thisClass(), "useMaximumSize");
        addEntry(res, thisClass(), "considerAllForShares");
        addEntry(res, thisClass(), "cags");
        addEntry(res, thisClass(), "file");
        addEntry(res, thisClass(), "selectKey");
    }

    private static final IRPLogger LOGGER = IRPLogging.getLogger(thisClass());

    public String _name;

    @FieldDefinition
    public int totalSize;

    @FieldDefinition
    public boolean useMaximumSize;

    @FieldDefinition
    public boolean considerAllForShares;

    @FieldDefinition
    public InConsumerAgentGroup[] cags;

    @FieldDefinition
    public InSpatialTableFile[] file;

    @FieldDefinition
    public InAttributeName[] selectKey;

    public InRelativeExternConsumerAgentPopulationSize() {
    }

    @Override
    public String getName() {
        return _name;
    }

    public void setName(String name) {
        this._name = name;
    }

    public int getTotalSize() {
        return totalSize;
    }

    public void setTotalSize(int totalSize) {
        this.totalSize = totalSize;
    }

    public void setUseMaximumSize(boolean useMaximumSize) {
        this.useMaximumSize = useMaximumSize;
    }

    public boolean isUseMaximumSize() {
        return useMaximumSize;
    }

    public void setConsiderAllForShares(boolean considerAllForShares) {
        this.considerAllForShares = considerAllForShares;
    }

    public boolean isConsiderAllForShares() {
        return considerAllForShares;
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
    public void setup(InputParser parser, Object input) throws ParsingException {
        InitializationData initData = ParamUtil.castTo(input, InitializationData.class);
        List<ConsumerAgentGroup> cags = parseCags(parser);
        checkConsumerAgentGroupExistence(cags, initData);

        List<List<SpatialAttribute<?>>> attrList = parser.parseEntityTo(getFile());
        String selector = getSelectKey().getName();
        ShareCalculator<ConsumerAgentGroup> shareCalculator = new ShareCalculator<>();

        parseIndividualSizes(cags, attrList, selector, shareCalculator);

        int totalSizeForShares = getTotalSizeForShareCalculation(attrList, cags, shareCalculator);
        calculateShares(cags, totalSizeForShares, shareCalculator);

        int totalPopulationSize = getTotalPopulationSize(cags, shareCalculator);
        Map<ConsumerAgentGroup, Integer> proportionalSizes = calcProportionalSizes(cags, totalPopulationSize, shareCalculator);
        for(Map.Entry<ConsumerAgentGroup, Integer> propotionalEntry: proportionalSizes.entrySet()) {
            ConsumerAgentGroup cag = propotionalEntry.getKey();
            int propotionalSize = propotionalEntry.getValue();
            initData.setInitialNumberOfConsumerAgents(cag, propotionalSize);
            LOGGER.debug(IRPSection.INITIALIZATION_PARAMETER, "set initial number of agents for cag '{}': {}", cag.getName(), propotionalSize);
        }
    }

    protected void checkConsumerAgentGroupExistence(List<ConsumerAgentGroup> cags, InitializationData initData) throws ParsingException {
        for(ConsumerAgentGroup cag: cags) {
            if(initData.hasInitialNumberOfConsumerAgents(cag)) {
                throw new ParsingException("cag '" + cag.getName() + "' already has a population size: " + initData.getInitialNumberOfConsumerAgents(cag) + " (try to set: " + totalSize + ")");
            }
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

    protected static void parseIndividualSizes(
            List<ConsumerAgentGroup> cags,
            List<List<SpatialAttribute<?>>> attrList,
            String selector,
            ShareCalculator<ConsumerAgentGroup> shareCalculator) {
        for(ConsumerAgentGroup cag: cags) {
            int cagSize = SpatialUtil.filterAndCount(attrList, selector, cag.getName());
            shareCalculator.setSize(cag, cagSize);
            LOGGER.trace(IRPSection.INITIALIZATION_PARAMETER, "parsed size for cag '{}': {}", cag.getName(), cagSize);
        }
    }

    protected int getTotalSizeForShareCalculation(
            List<List<SpatialAttribute<?>>> attrList,
            List<ConsumerAgentGroup> cags,
            ShareCalculator<ConsumerAgentGroup> shareCalculator) {
        int totalSize;
        if(considerAllForShares) {
            totalSize = attrList.size();
            LOGGER.trace(IRPSection.INITIALIZATION_PARAMETER, "total size for share calculation (consider all cags): '{}'", totalSize);
        } else {
            totalSize = shareCalculator.sumSizes(cags);
            LOGGER.trace(IRPSection.INITIALIZATION_PARAMETER, "total size for share calculation of all parsed cags: '{}'", totalSize);
        }
        return totalSize;
    }

    protected int getTotalPopulationSize(List<ConsumerAgentGroup> cags, ShareCalculator<ConsumerAgentGroup> shareCalculator) {
        int totalSize;
        if(useMaximumSize) {
            totalSize = shareCalculator.sumSizes(cags);
            LOGGER.trace(IRPSection.INITIALIZATION_PARAMETER, "total size for all parsed cags: '{}'", totalSize);
        } else {
            totalSize = getTotalSize();
            LOGGER.trace(IRPSection.INITIALIZATION_PARAMETER, "use total size set by user: '{}'", totalSize);
        }
        return totalSize;
    }

    protected static void calculateShares(
            List<ConsumerAgentGroup> cags,
            int totalSize,
            ShareCalculator<ConsumerAgentGroup> shareCalculator) {
        shareCalculator.calculateShares(totalSize);
        for(ConsumerAgentGroup cag: cags) {
            double share = shareCalculator.getShare(cag);
            LOGGER.trace(IRPSection.INITIALIZATION_PARAMETER, "size share for cag '{}': {}", cag.getName(), share);
        }
    }

    protected static Map<ConsumerAgentGroup, Integer> calcProportionalSizes(
            List<ConsumerAgentGroup> cags,
            int totalSize,
            ShareCalculator<ConsumerAgentGroup> shareCalculator) throws ParsingException {
        Map<ConsumerAgentGroup, Integer> cagSizes = new HashMap<>();
        for(ConsumerAgentGroup cag: cags) {
            int cagSize = shareCalculator.getProportionalSize(cag, totalSize);
            cagSizes.put(cag, cagSize);
        }
        repairRoundingErrors(cagSizes, totalSize, shareCalculator);
        return cagSizes;
    }

    protected static void repairRoundingErrors(
            Map<ConsumerAgentGroup, Integer> cagSizes,
            int totalSize,
            ShareCalculator<ConsumerAgentGroup> shareCalculator) throws ParsingException {
        int totalCagSize = cagSizes.values().stream()
                .mapToInt(v -> v)
                .sum();
        int difference = totalSize - totalCagSize;
        LOGGER.debug(IRPSection.INITIALIZATION_PARAMETER, "difference between totalSize and totalCagSize: {}", difference);
        if(difference < 0) {
            //Das (sollte) ist unmoeglich sein!
            throw new ParsingException("something went terribly wrong");
        }
        if(difference > 0) {
            LOGGER.debug(IRPSection.INITIALIZATION_PARAMETER, "repair rounding errors");
            ConsumerAgentGroup largestGroup = shareCalculator.getKeyWithLargestShare();
            int currentSize = cagSizes.get(largestGroup);
            cagSizes.put(largestGroup, currentSize + difference);

            int newTotalSize = cagSizes.values().stream()
                    .mapToInt(v -> v)
                    .sum();
            if(newTotalSize != totalSize) {
                //Das (sollte) ist unmoeglich sein!
                throw new ParsingException("something went terribly wrong");
            }
        }
    }
}
