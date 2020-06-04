package de.unileipzig.irpact.start.hardcodeddemo;

import com.fasterxml.jackson.databind.node.ObjectNode;
import de.unileipzig.irpact.commons.JsonUtil;
import de.unileipzig.irpact.start.hardcodeddemo.def.in.AgentGroup;
import de.unileipzig.irpact.start.hardcodeddemo.def.in.InputRoot;
import de.unileipzig.irpact.start.hardcodeddemo.def.in.Product;
import de.unileipzig.irpact.start.hardcodeddemo.def.out.OutputRoot;
import de.unileipzig.irpact.start.hardcodeddemo.def.out.OutputScalars;
import de.unileipzig.irptools.defstructure.AnnotationParser;
import de.unileipzig.irptools.defstructure.Converter;
import de.unileipzig.irptools.defstructure.DefinitionCollection;
import de.unileipzig.irptools.defstructure.DefinitionMapper;
import de.unileipzig.irptools.gamsjson.*;
import de.unileipzig.irptools.util.Pair;
import de.unileipzig.irptools.util.Table;
import de.unileipzig.irptools.util.Util;
import jadex.base.IPlatformConfiguration;
import jadex.base.PlatformConfigurationHandler;
import jadex.base.Starter;
import jadex.bridge.IExternalAccess;
import jadex.bridge.service.types.cms.CreationInfo;
import picocli.CommandLine;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.concurrent.Callable;

/**
 * @author Daniel Abitz
 */
@CommandLine.Command(
        name = "HardCodedAgentDemo",
        description = "Starts HardCodedAgentDemo."
)
public class HardCodedAgentDemo implements Callable<Integer> {


    @CommandLine.Option(
            names = { "-i", "--input" },
            description = "path to input file"
    )
    private String inputFile;

    @CommandLine.Option(
            names = { "-o", "--output" },
            description = "path to output file"
    )
    private String outputFile;

    private IExternalAccess platform;
    private MappedGamsJson<InputRoot> input;

    public HardCodedAgentDemo() {
    }

    public void run() throws IOException {
        parseInput();
        startPlatform();
        startMasterAgent();

        Random rng = new Random(getPrimaryData().getScalars().getSeed());
        List<Pair<AdaptionAgentGroup, IExternalAccess>> startedAgents = startAgents(rng);

        platform.waitForTermination()
                .get();

        Map<AgentGroup, List<AdaptedProducts>> results = processResults(startedAgents);
        createOutput(results);
    }

    private void parseInput() throws IOException {
        Path inputPath = Paths.get(inputFile);
        ObjectNode root = Util.readJson(inputPath);

        DefinitionCollection dcoll = AnnotationParser.parse(InputRoot.CLASSES);
        DefinitionMapper dmap = new DefinitionMapper(dcoll);
        Converter converter = new Converter(dmap);

        GamsJson.Type jsonType = GamsJson.Type.detectType(root);
        if (jsonType != GamsJson.Type.INPUT) {
            throw new IllegalArgumentException("unknown json file: " + inputPath.toString());
        }

        input = converter.fromGamsJson(GamsJson.Type.INPUT, root);
    }

    private InputRoot getPrimaryData() {
        return input.getEntries()
                .get(0)
                .getData();
    }

    private int getTotalNumberOfAgents() {
        return Arrays.stream(getPrimaryData().getAgentGroups())
                .mapToInt(AgentGroup::getNumberOfAgents)
                .sum();
    }

    private int getYear() {
        return input.getEntries()
                .get(0)
                .getYearEntry()
                .getYear();
    }

    private void startPlatform() {
        IPlatformConfiguration config = PlatformConfigurationHandler.getMinimal();
        config.setValue("kernel_component", true);
        config.setValue("kernel_bdi", true);
        config.setDefaultTimeout(-1L);
        platform = Starter.createPlatform(config)
                .get();
    }

    private void startMasterAgent() {
        CreationInfo masterInfo = new CreationInfo();
        masterInfo.setName("MASTER");
        masterInfo.setFilename("de.unileipzig.irpact.start.hardcodeddemo.MasterAgentBDI.class");
        masterInfo.addArgument("totalNumberOfAgents", getTotalNumberOfAgents());
        platform.createComponent(masterInfo)
                .get();
    }

    private List<Pair<AdaptionAgentGroup, IExternalAccess>> startAgents(Random rng) {
        InputRoot root0 = getPrimaryData();
        List<Pair<AdaptionAgentGroup, IExternalAccess>> startedAgents = new ArrayList<>();
        for(AgentGroup group: root0.getAgentGroups()) {
            AdaptionAgentGroup adaptionGroup = new AdaptionAgentGroup(
                    group.getName(),
                    getYear(),
                    new Random(rng.nextLong()),
                    group
            );
            startAgentsInGroup(adaptionGroup, root0.getProducts(), startedAgents);
        }
        return startedAgents;
    }

    private void startAgentsInGroup(AdaptionAgentGroup group, Product[] products, List<Pair<AdaptionAgentGroup, IExternalAccess>> startedAgents) {
        for(int i = 0; i < group.getNumberOfAgents(); i++) {
            IExternalAccess agent = createAgent(group, products);
            startedAgents.add(new Pair<>(group, agent));
        }
    }

    private IExternalAccess createAgent(AdaptionAgentGroup group, Product[] products) {
        AdaptionAgentData data = group.deriveData();
        data.setProducts(products);

        CreationInfo cInfo = new CreationInfo();
        cInfo.setName(data.getName());
        cInfo.setFilename("de.unileipzig.irpact.start.hardcodeddemo.AdaptionAgentBDI.class");
        cInfo.addArgument("data", data);

        return platform.createComponent(cInfo)
                .get();
    }

    private Map<AgentGroup, List<AdaptedProducts>> processResults(List<Pair<AdaptionAgentGroup, IExternalAccess>> startedAgents) {
        Map<AgentGroup, List<AdaptedProducts>> results = new LinkedHashMap<>();
        for(Pair<AdaptionAgentGroup, IExternalAccess> startedAgent: startedAgents) {
            Map<String, Object> agentResults = startedAgent.getValue()
                    .getResultsAsync()
                    .get();
            AdaptedProducts adaptedProducts = (AdaptedProducts) agentResults.get("adapted");
            List<AdaptedProducts> adaptedProductsList = results.computeIfAbsent(
                    startedAgent.getKey().getGroup(),
                    _group -> new ArrayList<>()
            );
            adaptedProductsList.add(adaptedProducts);
        }
        return results;
    }

    private static Table<AgentGroup, Product, Double> createOutputTableForYear(Map<AgentGroup, List<AdaptedProducts>> results, int year) {
        Table<AgentGroup, Product, Double> out = Table.newLinked();
        for(Map.Entry<AgentGroup, List<AdaptedProducts>> entry : results.entrySet()) {
            AgentGroup group = entry.getKey();
            List<AdaptedProducts> adaptedProductsList = entry.getValue();
            for(AdaptedProducts adaptedProducts : adaptedProductsList) {
                Set<Product> set = adaptedProducts.getMap().get(year);
                if(set == null) {
                    continue;
                }
                for(Product product : set) {
                    double current = out.contains(group, product)
                            ? out.get(group, product)
                            : 0.0;
                    out.put(group, product, current + 1.0);
                }
            }
        }
        return out;
    }

    private void applyInputConfig(ObjectNode outNode) {
        ObjectNode inNode = input.getEntries()
                .get(0)
                .getYearEntry()
                .getRoot();
        if(inNode.has("config")) {
            outNode.set("config", inNode.get("config"));
        }
    }

    private void createOutput(Map<AgentGroup, List<AdaptedProducts>> results) throws IOException {
        Table<AgentGroup, Product, Double> adaptions = createOutputTableForYear(results, getYear());

        OutputRoot outRoot = new OutputRoot();
        OutputScalars outScalars = new OutputScalars();
        outRoot.scalars = outScalars;
        outRoot.agentGroups = getPrimaryData().getAgentGroups();
        outRoot.products = getPrimaryData().getProducts();
        outScalars.adaptions = adaptions;

        ObjectNode outNode = JsonUtil.mapper.createObjectNode();
        applyInputConfig(outNode);

        GamsJson gamsJson = new GamsJson(GamsJson.Type.INPUT, outNode);
        YearEntry yearEntry = new YearEntry(outNode);
        MappedYearEntry<OutputRoot> mappedYearEntry = new MappedYearEntry<>(outRoot, yearEntry);
        MappedGamsJson<OutputRoot> outJson = new MappedGamsJson<>(gamsJson, Collections.singletonList(mappedYearEntry));


        DefinitionCollection dcoll = AnnotationParser.parse(OutputRoot.CLASSES);
        DefinitionMapper dmap = new DefinitionMapper(dcoll);
        Converter converter = new Converter(dmap);

        converter.toGamsJson(outJson);

        Path outputPath = Paths.get(outputFile);
        Util.writeJson(outJson.getGamsJson().getRoot(), outputPath, Util.defaultPrinter);
    }

    @Override
    public Integer call() {
        if (inputFile == null) {
            throw new NullPointerException("input file missing");
        }
        if (outputFile == null) {
            throw new NullPointerException("output file missing");
        }
        return CommandLine.ExitCode.OK;
    }

    public static void main(String[] args) throws IOException {
        HardCodedAgentDemo demo = new HardCodedAgentDemo();
        CommandLine cmdLine = new CommandLine(demo);
        int exitCode = cmdLine.execute(args);
        if(exitCode == CommandLine.ExitCode.OK) {
            demo.run();
        } else {
            System.exit(exitCode);
        }
    }
}
