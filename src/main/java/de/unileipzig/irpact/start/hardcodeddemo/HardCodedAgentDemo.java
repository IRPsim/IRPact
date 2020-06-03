package de.unileipzig.irpact.start.hardcodeddemo;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import de.unileipzig.irpact.start.hardcodeddemo.def.in.AgentGroup;
import de.unileipzig.irpact.start.hardcodeddemo.def.in.GlobalRoot;
import de.unileipzig.irpact.start.hardcodeddemo.def.in.Product;
import de.unileipzig.irptools.defstructure.AnnotationParser;
import de.unileipzig.irptools.defstructure.Converter;
import de.unileipzig.irptools.defstructure.DefinitionCollection;
import de.unileipzig.irptools.defstructure.DefinitionMapper;
import de.unileipzig.irptools.gamsjson.Delta;
import de.unileipzig.irptools.gamsjson.MappedGamsJson;
import de.unileipzig.irptools.util.Table;
import de.unileipzig.irptools.util.Util;
import jadex.base.IPlatformConfiguration;
import jadex.base.PlatformConfigurationHandler;
import jadex.base.Starter;
import jadex.bridge.IExternalAccess;
import jadex.bridge.service.types.cms.CreationInfo;
import jadex.commons.future.DefaultResultListener;
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

    public static boolean debug = false;
    private static final ObjectMapper mapper = new ObjectMapper();
    private static final Object lock = new Object();

    @CommandLine.Option(
            names = {"-i", "--input"},
            description = "path to input file"
    )
    private String inputFile;

    @CommandLine.Option(
            names = {"-o", "--output"},
            description = "path to output file"
    )
    private String outputFile;

    public HardCodedAgentDemo() {
    }

    @Override
    public Integer call() {
        if(inputFile == null) {
            throw new NullPointerException("input file missing");
        }
        if(outputFile == null) {
            throw new NullPointerException("output file missing");
        }
        return CommandLine.ExitCode.OK;
    }

    private static Table<AgentGroup, Product, Double> mapToAdapt(Map<AgentGroup, List<AdaptedProducts>> input, int year) {
        Table<AgentGroup, Product, Double> out = Table.newLinked();
        for(Map.Entry<AgentGroup, List<AdaptedProducts>> entry: input.entrySet()) {
            AgentGroup group = entry.getKey();
            List<AdaptedProducts> adaptedProductsList = entry.getValue();
            for(AdaptedProducts adaptedProducts: adaptedProductsList) {
                Set<Product> set = adaptedProducts.getMap().get(year);
                if(set == null) continue;
                for(Product product: set) {
                    double current = out.contains(group, product)
                            ? out.get(group, product)
                            : 0.0;
                    out.put(group, product, current + 1.0);
                }
            }
        }
        return out;
    }

    private static synchronized void putResult(
            Map<AgentGroup, List<AdaptedProducts>> out,
            AgentGroup name,
            AdaptedProducts adaptedProducts) {
        List<AdaptedProducts> list = out.computeIfAbsent(name, _name -> new ArrayList<>());
        list.add(adaptedProducts);
    }

    private void run() throws IOException {
        Path inputPath = Paths.get(inputFile);
        ObjectNode inputNode = Util.readJson(inputPath);
        //System.out.println(JsonUtil.writeJson(inputNode, JsonUtil.defaultPrinter));

        DefinitionCollection dcoll = AnnotationParser.parse(GlobalRoot.CLASSES);
        DefinitionMapper dmap = new DefinitionMapper(dcoll);
        Converter converter = new Converter(dmap);

        MappedGamsJson<GlobalRoot> mappedRoot = converter.fromGamsJson(inputNode);
        //System.out.println(mappedRoot);

        //=====

        IPlatformConfiguration config = PlatformConfigurationHandler.getMinimal();
        config.setValue("kernel_component", true);
        config.setValue("kernel_bdi", true);
        config.setDefaultTimeout(-1L);

        IExternalAccess platform = Starter.createPlatform(config)
                .get();

        CreationInfo masterInfo = new CreationInfo();
        masterInfo.setName("MASTER");
        masterInfo.setFilename("de.unileipzig.irpact.start.hardcodeddemo.MasterAgentBDI.class");
        IExternalAccess masterAgent = platform.createComponent(masterInfo)
                .get();

        GlobalRoot root0 = mappedRoot.getMappedEntries()
                .get(0)
                .getData();

        long seed = root0.getScalars().getSeed();
        Random random = new Random(seed);

        Product[] products = root0.getProducts();

        Map<AgentGroup, List<AdaptedProducts>> out = new LinkedHashMap<>();
        for(int i = 0; i < root0.getAgentGroups().length; i++) {
            AgentGroup group = root0.getAgentGroups()[i];
            final int _i = i;
            Map<Integer, Double> adaptionRateMap = Delta.collectYearEntryData(
                    mappedRoot,
                    _gr -> _gr.getAgentGroups()[_i].adaptionRate
            );
            int[] years = adaptionRateMap.keySet()
                    .stream()
                    .mapToInt(_x -> _x)
                    .toArray();
            double[] adaotionRates = adaptionRateMap.values()
                    .stream()
                    .mapToDouble(_x -> _x)
                    .toArray();

            AdaptionAgentGroup agentGroup = new AdaptionAgentGroup(
                    group.getName(),
                    years,
                    adaotionRates,
                    new Random(random.nextLong())
            );
            for(int j = 0; j < group.getNumberOfAgents(); j++) { //j wird nicht genutzt
                AdaptionAgentData data = agentGroup.deriveData();
                data.setProducts(products);
                //AGENT ERSTELLEN
                CreationInfo cInfo = new CreationInfo();
                cInfo.setName(data.getName());
                cInfo.setFilename("de.unileipzig.irpact.start.hardcodeddemo.AdaptionAgentBDI.class");
                cInfo.addArgument("data", data);

                IExternalAccess agent = platform.createComponent(cInfo)
                        .get();
                /*
                agent.getResultsAsync().addResultListener(new DefaultResultListener<Map<String, Object>>() {
                    @Override
                    public void resultAvailable(Map<String, Object> result) {
                        AdaptedProducts adaptedProducts = (AdaptedProducts) result.get("adapted");
                        if(adaptedProducts == null) {
                            System.out.println("NULL @ " + data.getName() + " -> " + result);
                        } else {
                            putResult(out, group.getName(), data.getName(), adaptedProducts);
                            agent.killComponent();
                        }
                    }
                });
                */
                agent.waitForTermination().addResultListener(new DefaultResultListener<Map<String, Object>>() {
                    @Override
                    public void resultAvailable(Map<String, Object> result) {
                        AdaptedProducts adaptedProducts = (AdaptedProducts) result.get("adapted");
                        //putResult(out, group.getName(), data.getName(), adaptedProducts);
                        putResult(out, group, adaptedProducts);
                    }
                });
            }
        }

        //MasterAgentBdi !
        platform.waitForTermination()
                .get();

        MappedGamsJson<de.unileipzig.irpact.start.hardcodeddemo.def.out.GlobalRoot> mappedOut = new MappedGamsJson<>();

        int[] years = mappedRoot.getMappedEntries()
                .stream()
                .mapToInt(_entry -> _entry.getYearEntry().getYear())
                .toArray();

        for(int year: years) {
            Table<AgentGroup, Product, Double> table = mapToAdapt(out, year);

            de.unileipzig.irpact.start.hardcodeddemo.def.out.GlobalRoot outRoot = new de.unileipzig.irpact.start.hardcodeddemo.def.out.GlobalRoot();
            de.unileipzig.irpact.start.hardcodeddemo.def.out.GlobalScalars outScalar = new de.unileipzig.irpact.start.hardcodeddemo.def.out.GlobalScalars();
            outRoot.scalars = outScalar;
            outRoot.agentGroups = mappedRoot.findYear(year).getData().agentGroups;
            outRoot.products = mappedRoot.findYear(year).getData().products;
            outScalar.adaptions = table;

            mappedOut.add(year, outRoot);
        }

        DefinitionCollection out_dcoll = AnnotationParser.parse(de.unileipzig.irpact.start.hardcodeddemo.def.out.GlobalRoot.CLASSES);
        DefinitionMapper out_dmap = new DefinitionMapper(out_dcoll);
        Converter out_converter = new Converter(out_dmap);

        out_converter.toGamsJson(mappedOut);

        Path outputPath = Paths.get(outputFile);
        Util.writeJson(mappedOut.getGamsJson().getRoot(), outputPath, Util.defaultPrinter);
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