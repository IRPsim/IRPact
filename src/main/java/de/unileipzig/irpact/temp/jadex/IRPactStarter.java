package de.unileipzig.irpact.temp.jadex;

import de.unileipzig.irpact.commons.concurrent.ConcurrentUtil;
import de.unileipzig.irpact.experimental.todev.time.ContinuousConverter;
import de.unileipzig.irpact.experimental.todev.time.TickConverter;
import de.unileipzig.irpact.jadex.util.JadexUtil;
import de.unileipzig.irpact.start.irpact.input.IRPactInputData;
import de.unileipzig.irpact.start.irpact.input.agent.AgentGroup;
import de.unileipzig.irpact.start.irpact.input.need.Need;
import de.unileipzig.irpact.start.irpact.input.product.FixedProduct;
import de.unileipzig.irpact.start.irpact.input.product.ProductGroup;
import de.unileipzig.irpact.start.irpact.input.simulation.ContinousTimeModel;
import de.unileipzig.irpact.start.irpact.input.simulation.DiscretTimeModel;
import de.unileipzig.irpact.start.irpact.input.simulation.TimeModel;
import de.unileipzig.irpact.start.irpact.output.AdaptionRate;
import de.unileipzig.irpact.start.irpact.output.IRPactOutputData;
import de.unileipzig.irpact.temp.SatisfiedNeed;
import de.unileipzig.irpact.temp.TConsumerAgentData;
import de.unileipzig.irpact.temp.TTestAgentData;
import de.unileipzig.irpact.temp.TTimerAgentData;
import de.unileipzig.irptools.defstructure.AnnotationParser;
import de.unileipzig.irptools.defstructure.Converter;
import de.unileipzig.irptools.defstructure.DefinitionCollection;
import de.unileipzig.irptools.defstructure.DefinitionMapper;
import de.unileipzig.irptools.io.input.InputData;
import de.unileipzig.irptools.io.input.InputFile;
import de.unileipzig.irptools.io.scenario.ScenarioData;
import de.unileipzig.irptools.io.scenario.ScenarioFile;
import jadex.base.IPlatformConfiguration;
import jadex.base.PlatformConfigurationHandler;
import jadex.base.Starter;
import jadex.bridge.IExternalAccess;
import jadex.bridge.service.types.clock.IClock;
import jadex.bridge.service.types.clock.IClockService;
import jadex.bridge.service.types.cms.CreationInfo;
import jadex.bridge.service.types.simulation.ISimulationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Path;
import java.util.*;

/**
 * @author Daniel Abitz
 */
public class IRPactStarter {

    private static final Logger logger = LoggerFactory.getLogger(IRPactStarter.class);

    private Path inputPath;
    private Path outputPath;

    private int year;
    private IRPactInputData data;

    private IExternalAccess platform;
    private IClockService platformClock;
    private ISimulationService platformSimu;
    private List<IExternalAccess> agents = new ArrayList<>();
    private IExternalAccess testAgent;
    private IExternalAccess timerAgent;
    private ContinuousConverter continuousConverter;
    private TickConverter tickConverter;

    public IRPactStarter(Path inputPath, Path outputPath) {
        this.inputPath = inputPath;
        this.outputPath = outputPath;
    }

    private void parseInputData() throws IOException {
        logger.debug("parse InputData");
        ScenarioFile scenarioFile = ScenarioFile.parse(inputPath);

        DefinitionCollection dcoll = AnnotationParser.parse(IRPactInputData.LIST);
        DefinitionMapper dmap = new DefinitionMapper(dcoll);
        Converter converter = new Converter(dmap);

        ScenarioData<IRPactInputData> scenarioData = scenarioFile.deserialize(converter);

        data = scenarioData.get(0).getData();
        year = scenarioData.get(0).getYear();
    }

    private void startPlatform() {
        logger.debug("start Platform");
        IPlatformConfiguration config = PlatformConfigurationHandler.getMinimal();
        config.setValue("kernel_component", true);
        config.setValue("kernel_bdi", true);
        config.setDefaultTimeout(-1L);
        platform = Starter.createPlatform(config)
                .get();
    }

    private void setupPlatform() {
        logger.debug("setup Platform");
        setupTimeModel();
    }

    private void setupTimeModel() {
        logger.debug("setup TimeModel");
        platformClock = JadexUtil.getClockService(platform);
        platformSimu = JadexUtil.getSimulationService(platform);

        platformSimu.pause().get();

        TimeModel tm = data.timeModels[0];
        if(tm instanceof ContinousTimeModel) {
            ContinousTimeModel ctm = (ContinousTimeModel) tm;
            platformSimu.setClockType(IClock.TYPE_CONTINUOUS).get();
            platformClock.setDilation(ctm.acceleration);
            continuousConverter = new ContinuousConverter(year);
        } else {
            DiscretTimeModel dtm = (DiscretTimeModel) tm;
            platformSimu.setClockType(IClock.TYPE_EVENT_DRIVEN).get();
            platformClock.setDelta(1);
            tickConverter = new TickConverter(year, dtm.msPerTick);
        }

        platformSimu.start().get();
        logger.debug("new Clock: {}", platformClock.getClockType());
        logger.debug("acceleration: {}", platformClock.getDilation());
        logger.debug("tickDelta: {}", platformClock.getDelta());
        platformSimu.pause().get();
        platformClock.stop();
    }

    private CreationInfo newInfo(TConsumerAgentData data) {
        CreationInfo info = new CreationInfo();
        info.setName(data.getName());
        info.setFilename("de.unileipzig.irpact.temp.jadex.TConsumerAgentBDI.class");
        info.addArgument("data", data);
        return info;
    }

    private void createAgents() {
        logger.debug("create Agents");
        Set<FixedProduct> productSet = data.getProducts();
        for(AgentGroup group: data.agentGroups) {
            logger.debug("start {} agents in group '{}'", group.numberOfAgents, group._name);
            Random groupRandom = new Random(group.seed);
            for(int i = 0; i < group.numberOfAgents; i++) {
                Random agentRandom = new Random(groupRandom.nextLong());
                TConsumerAgentData agentData = new TConsumerAgentData(
                        group,
                        "Agent-" + group._name + "-" + i,
                        group.getNeeds(),
                        agentRandom,
                        continuousConverter,
                        tickConverter,
                        data.getDelay(),
                        productSet
                );
                CreationInfo info = newInfo(agentData);
                IExternalAccess agent = platform.createComponent(info).get();
                agents.add(agent);
            }
        }
    }

    private void startTimerAgent() {
        logger.debug("start TimerAgent");
        TTimerAgentData agentData = new TTimerAgentData("TimerAgent", data.agentGroups, continuousConverter, tickConverter);
        CreationInfo info = new CreationInfo();
        info.setName(agentData.getName());
        info.setFilename("de.unileipzig.irpact.temp.jadex.TTimerAgentBDI.class");
        info.addArgument("data", agentData);
        timerAgent = platform.createComponent(info).get();
    }

    private void startTestAgent() {
        logger.debug("start TestAgent");
        TTestAgentData agentData = new TTestAgentData(data, "TestAgent", continuousConverter, tickConverter);
        CreationInfo info = new CreationInfo();
        info.setName(agentData.getName());
        info.setFilename("de.unileipzig.irpact.temp.jadex.TTestAgentBDI.class");
        info.addArgument("data", agentData);
        testAgent = platform.createComponent(info).get();
    }

    private void startSimulation() {
        ConcurrentUtil.sleepSilently(2000);
        logger.debug("start Simulation");
        if(continuousConverter != null) {
            continuousConverter.setStart(platformClock.getStarttime());
        }
        platformSimu.start().get();
        platformClock.start();
    }

    private void stopSimulation() {
        ConcurrentUtil.sleepSilently(10000);
        logger.debug("stop Simulation");
        platform.killComponent().get();
    }

    private void waitForPlatform() {
        logger.debug("wait for Termination");
        platform.waitForTermination().get();
    }

    @SuppressWarnings("unchecked")
    private void collectResults() throws IOException {
        logger.debug("collect Results");
        List<SatisfiedNeed> resultList = new ArrayList<>();
        for(IExternalAccess agent: agents) {
            Set<SatisfiedNeed> satisfiedNeedSet = (Set<SatisfiedNeed>) agent.getResultsAsync().get().get("satisfiedNeedSet");
            resultList.addAll(satisfiedNeedSet);
        }
        IRPactOutputData outputData = mapToOutput(resultList);

        DefinitionCollection dcoll = AnnotationParser.parse(IRPactOutputData.LIST);
        DefinitionMapper dmap = new DefinitionMapper(dcoll);
        Converter converter = new Converter(dmap);

        InputData<IRPactOutputData> outData = new InputData<>(outputData);
        InputFile outFile = outData.serialize(converter);
        outFile.store(outputPath);
    }

    private Map<AgentGroup, List<SatisfiedNeed>> mapToGroups(List<SatisfiedNeed> input) {
        Map<AgentGroup, List<SatisfiedNeed>> map = new LinkedHashMap<>();
        for(AgentGroup group: data.agentGroups) {
            map.put(group, new ArrayList<>());
        }
        for(SatisfiedNeed satisfiedNeed: input) {
            List<SatisfiedNeed> list = map.get(satisfiedNeed.getGroup());
            list.add(satisfiedNeed);
        }
        return map;
    }

    private Map<AgentGroup, Map<Need, Integer>> mapAdoptionCount(List<SatisfiedNeed> input) {
        Map<AgentGroup, Map<Need, Integer>> map = new LinkedHashMap<>();
        for(SatisfiedNeed satisfiedNeed: input) {
            AgentGroup group = satisfiedNeed.getGroup();
            Need need = satisfiedNeed.getNeed();
            Map<Need, Integer> counter = map.computeIfAbsent(group, _group -> new LinkedHashMap<>());
            int current = counter.computeIfAbsent(need, _need -> 0);
            counter.put(need, current + 1);
        }
        return map;
    }

    private List<AdaptionRate> collectAdoptionRates(List<SatisfiedNeed> input) {
        List<AdaptionRate> adaptionRates = new ArrayList<>();
        for(AgentGroup group: data.agentGroups) {
            for(Need need: group.getNeeds()) {
                double rate = calcAdoptionRate(group, need, input);
                AdaptionRate adaptionRate = new AdaptionRate(
                        group._name + "-" + need._name,
                        group,
                        need,
                        rate
                );
                adaptionRates.add(adaptionRate);
            }
        }
        return adaptionRates;
    }

    private double calcAdoptionRate(AgentGroup group, Need need, List<SatisfiedNeed> input) {
        int count = 0;
        for(SatisfiedNeed satisfiedNeed: input) {
            if(satisfiedNeed.getGroup() == group && satisfiedNeed.getNeed() == need) {
                count++;
            }
        }
        return (double) count / (double) group.numberOfAgents;
    }

    private Set<Need> getNeeds(List<SatisfiedNeed> input) {
        Set<Need> needSet = new LinkedHashSet<>();
        for(SatisfiedNeed need: input) {
            needSet.add(need.getNeed());
        }
        return needSet;
    }

    private Set<AgentGroup> getAgentGroups(List<SatisfiedNeed> input) {
        Set<AgentGroup> needSet = new LinkedHashSet<>();
        for(SatisfiedNeed need: input) {
            needSet.add(need.getGroup());
        }
        return needSet;
    }

    private Set<ProductGroup> getProductGroups(List<SatisfiedNeed> input) {
        Set<ProductGroup> needSet = new LinkedHashSet<>();
        for(SatisfiedNeed need: input) {
            needSet.add(need.getProduct().group);
        }
        return needSet;
    }

    private Set<FixedProduct> getFixedProducts(List<SatisfiedNeed> input) {
        Set<FixedProduct> needSet = new LinkedHashSet<>();
        for(SatisfiedNeed need: input) {
            needSet.add(need.getProduct());
        }
        return needSet;
    }

    private IRPactOutputData mapToOutput(List<SatisfiedNeed> input) {
        Set<AgentGroup> agentGroups = getAgentGroups(input);
        Set<ProductGroup> productGroups = getProductGroups(input);
        Set<FixedProduct> fixedProducts = getFixedProducts(input);
        Set<Need> needs = getNeeds(input);
        List<AdaptionRate> adaptionRates = collectAdoptionRates(input);
        return new IRPactOutputData(
                agentGroups.toArray(new AgentGroup[0]),
                productGroups.toArray(new ProductGroup[0]),
                fixedProducts.toArray(new FixedProduct[0]),
                needs.toArray(new Need[0]),
                adaptionRates.toArray(new AdaptionRate[0])
        );
    }

    public void start() throws IOException {
        logger.debug("Start...");
        parseInputData();
        startPlatform();
        setupPlatform();
        startTimerAgent();
//        startTestAgent();
        createAgents();
        startSimulation();
//        stopSimulation();
        waitForPlatform();
        collectResults();
        logger.debug("... Finished");
    }
}
