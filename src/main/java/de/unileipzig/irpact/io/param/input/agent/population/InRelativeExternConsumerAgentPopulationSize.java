package de.unileipzig.irpact.io.param.input.agent.population;

import de.unileipzig.irpact.commons.spatial.attribute.SpatialAttribute;
import de.unileipzig.irpact.commons.util.ExceptionUtil;
import de.unileipzig.irpact.commons.util.ShareCalculator;
import de.unileipzig.irpact.commons.exception.ParsingException;
import de.unileipzig.irpact.core.agent.consumer.ConsumerAgentGroup;
import de.unileipzig.irpact.core.log.IRPLogging;
import de.unileipzig.irpact.core.log.IRPSection;
import de.unileipzig.irpact.core.simulation.Settings;
import de.unileipzig.irpact.core.spatial.SpatialTableFileContent;
import de.unileipzig.irpact.core.spatial.SpatialUtil;
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
import java.util.function.Function;
import java.util.function.ToIntFunction;

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
        addEntry(res, thisClass(), "maximumSize");
        addEntry(res, thisClass(), "useMaximumPossibleSize");
        addEntry(res, thisClass(), "allowSmallerSize");
        addEntry(res, thisClass(), "cags");
        addEntry(res, thisClass(), "file");
        addEntry(res, thisClass(), "selectKey");
    }

    private static final IRPLogger LOGGER = IRPLogging.getLogger(thisClass());

    public String _name;

    @FieldDefinition
    public int maximumSize;

    @FieldDefinition
    public boolean useMaximumPossibleSize;

    @FieldDefinition
    public boolean allowSmallerSize;

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

    public int getMaximumSize() {
        return maximumSize;
    }

    public void setMaximumSize(int maximumSize) {
        this.maximumSize = maximumSize;
    }

    public void setUseMaximumPossibleSize(boolean useMaximumPossibleSize) {
        this.useMaximumPossibleSize = useMaximumPossibleSize;
    }

    public boolean isUseMaximumPossibleSize() {
        return useMaximumPossibleSize;
    }

    public void setAllowSmallerSize(boolean allowSmallerSize) {
        this.allowSmallerSize = allowSmallerSize;
    }

    public boolean isAllowSmallerSize() {
        return allowSmallerSize;
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
        Settings initData = ParamUtil.castTo(input, Settings.class);
        final List<ConsumerAgentGroup> cags = parseCags(parser);
        checkConsumerAgentGroupExistence(cags, initData);

        final SpatialTableFileContent attrList = parser.parseEntityTo(getFile());
        final String selector = getSelectKey().getName();

        Map<ConsumerAgentGroup, Integer> proportionalSizes = calculateShares(
                cags,
                selector,
                attrList.data(),
                getMaximumSize(),
                isAllowSmallerSize(),
                isUseMaximumPossibleSize(),
                ConsumerAgentGroup::getName,
                true
        );

        for(Map.Entry<ConsumerAgentGroup, Integer> propotionalEntry: proportionalSizes.entrySet()) {
            ConsumerAgentGroup cag = propotionalEntry.getKey();
            int propotionalSize = propotionalEntry.getValue();
            initData.setInitialNumberOfConsumerAgents(cag, propotionalSize);
            LOGGER.trace(IRPSection.INITIALIZATION_PARAMETER, "set initial number of agents for cag '{}': {}", cag.getName(), propotionalSize);
        }
    }

    protected void checkConsumerAgentGroupExistence(List<ConsumerAgentGroup> cags, Settings initData) throws ParsingException {
        for(ConsumerAgentGroup cag: cags) {
            if(initData.hasInitialNumberOfConsumerAgents(cag)) {
                throw new ParsingException("cag '" + cag.getName() + "' already has a population size: " + initData.getInitialNumberOfConsumerAgents(cag) + " (try to set: " + maximumSize + ")");
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

    public static <T> Map<T, Integer> calculateShares(
            List<T> cags,
            String selector,
            List<List<SpatialAttribute>> attrList,
            int maximumSize,
            boolean allowSmallerSize,
            boolean useMaximumPossibleSize,
            Function<T, String> toString,
            boolean doLogging) throws ParsingException {

        ShareCalculator<T> shareCalculator = new ShareCalculator<>();

        parseIndividualSizes(
                cags,
                shareCalculator,
                cag -> SpatialUtil.filterAndCount(attrList, selector, toString.apply(cag)),
                toString,
                doLogging
        );

        calculateShares(
                cags,
                shareCalculator,
                toString,
                doLogging
        );

        int parsedTotalSize = shareCalculator.sumSizes();
        if(useMaximumPossibleSize) {
            if(doLogging) LOGGER.warn("use maximum possible size {}", parsedTotalSize);
            maximumSize = parsedTotalSize;
        } else {
            if(parsedTotalSize < maximumSize) {
                if(allowSmallerSize) {
                    if(doLogging) LOGGER.warn("parsed total size ({}) < maximum size ({}), set maximum size to {}", parsedTotalSize, maximumSize, parsedTotalSize);
                    maximumSize = parsedTotalSize;
                } else {
                    throw ExceptionUtil.create(ParsingException::new, "requested maximum size is to large: maximum size ({}) > possible size ({})", maximumSize, parsedTotalSize);
                }
            }
        }

        return calculateProportionalSizes(
                cags,
                maximumSize,
                shareCalculator,
                doLogging
        );
    }

    protected static <T> void parseIndividualSizes(
            List<T> cags,
            ShareCalculator<T> shareCalculator,
            ToIntFunction<T> sizeOfCag,
            Function<T, String> toString,
            boolean doLogging) {
        for(T cag: cags) {
            int cagSize = sizeOfCag.applyAsInt(cag);
            shareCalculator.setSize(cag, cagSize);
            if(doLogging) LOGGER.trace(IRPSection.INITIALIZATION_PARAMETER, "parsed size for cag '{}': {}", toString.apply(cag), cagSize);
        }
    }

    protected static <T> void calculateShares(
            List<T> cags,
            ShareCalculator<T> shareCalculator,
            Function<? super T, ? extends String> toString,
            boolean doLogging) {
        shareCalculator.calculateShares();
        for(T cag: cags) {
            double share = shareCalculator.getShare(cag);
            if(doLogging) LOGGER.trace(IRPSection.INITIALIZATION_PARAMETER, "size share for cag '{}': {}", toString.apply(cag), share);
        }
    }

    protected static <T> Map<T, Integer> calculateProportionalSizes(
            List<T> cags,
            int totalSize,
            ShareCalculator<T> shareCalculator,
            boolean doLogging) throws ParsingException {
        Map<T, Integer> cagSizes = new HashMap<>();
        for(T cag: cags) {
            int cagSize = shareCalculator.getProportionalSize(cag, totalSize);
            cagSizes.put(cag, cagSize);
        }
        repairRoundingErrors(cagSizes, totalSize, shareCalculator, doLogging);
        return cagSizes;
    }

    protected static <T> void repairRoundingErrors(
            Map<T, Integer> cagSizes,
            int totalSize,
            ShareCalculator<T> shareCalculator,
            boolean doLogging) throws ParsingException {
        int totalCagSize = cagSizes.values().stream()
                .mapToInt(v -> v)
                .sum();
        int difference = totalSize - totalCagSize;
        if(doLogging) LOGGER.trace(IRPSection.INITIALIZATION_PARAMETER, "difference between totalSize and totalCagSize: {}", difference);
        if(difference < 0) {
            //Das (sollte) ist unmoeglich sein!
            throw new ParsingException("something went terribly wrong");
        }
        if(difference > 0) {
            if(doLogging) LOGGER.trace(IRPSection.INITIALIZATION_PARAMETER, "repair rounding errors");
            T largestGroup = shareCalculator.getKeyWithLargestShare();
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
