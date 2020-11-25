package de.unileipzig.irpact.start.optact;

import de.unileipzig.irpact.start.optact.in.InRoot;
import de.unileipzig.irpact.start.optact.in.SideCustom;
import de.unileipzig.irpact.start.optact.out.OutCustom;
import de.unileipzig.irpact.start.optact.out.OutRoot;
import de.unileipzig.irptools.defstructure.AnnotationParser;
import de.unileipzig.irptools.defstructure.Converter;
import de.unileipzig.irptools.defstructure.DefinitionCollection;
import de.unileipzig.irptools.defstructure.DefinitionMapper;
import de.unileipzig.irptools.io.base.AnnualEntry;
import de.unileipzig.irptools.io.perennial.PerennialData;
import de.unileipzig.irptools.io.perennial.PerennialFile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import picocli.CommandLine;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.concurrent.Callable;

/**
 * @author Daniel Abitz
 */
public class OptActMain implements Callable<Integer> {

    private static final Logger logger = LoggerFactory.getLogger(OptActMain.class);

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

    @CommandLine.Option(
            names = { "--image" },
            description = "path to image file"
    )
    private String imageFile;

    @CommandLine.Option(
            names = { "--noSimulation" },
            description = "disable simulation"
    )
    private boolean noSimulation;

    private Path inputPath;
    private Path outputPath;
    private Path imagePath;

    public OptActMain() {
    }

//    public void init(Path inputPath, Path outputPath) {
//        this.inputPath = inputPath;
//        this.outputPath = outputPath;
//    }

//    private static double calc_par_out_IuOSonnentankNetzversorgung_Summe(Year year, String agentGroup) {
//        double mwst = year.getScalars().getNode("sca_Tax_PS_vat").doubleValue();
//        JsonNode node = year.getSets().getNode("set_side_cust").get(agentGroup).get("par_IuO_ESector_CustSide");
//        if(node == null || node.isMissingNode() || node.isNull()) {
//            throw new NoSuchElementException("par_IuO_ESector_CustSide @ " + agentGroup);
//        }
//        if(node.isTextual()) {
//            String id = node.textValue();
//            int intId = Integer.parseInt(id);
//            return intId * mwst;
//        } else {
//            double[] arr = Util.toDoubleArray((ArrayNode) node);
//            double sum = Arrays.stream(arr).sum();
//            return sum * mwst;
//        }
//    }

    private static double calc_par_out_IuOSonnentankNetzversorgung_Summe(SideCustom agentGrp, double mwst) {
        double sumOrId = agentGrp.timeStuff.hasData()
                ? Arrays.stream(agentGrp.timeStuff.toArray()).sum()
                : Integer.parseInt(agentGrp.timeStuff.getId());
        return sumOrId * mwst;
    }

    private static int calc_par_out_S_DS(SideCustom agentGrp) {
        return agentGrp.number + agentGrp.delta;
    }

//    private static List<String> listAgentGroups(Year year) {
//        JsonNode node = year.getSets().getNode("set_side_cust");
//        List<String> groups = new ArrayList<>();
//        for(Map.Entry<String, JsonNode> entry: Util.iterate(node)) {
//            groups.add(entry.getKey());
//        }
//        return groups;
//    }
//
//    private static int calc_par_out_S_DS(Year year, String agentGroup) {
//        int size = year.getSets().getNode("set_side_cust").get(agentGroup).get("par_S_DS").intValue();
//        int delta = year.getSets().getNode("set_side_cust").get(agentGroup).get("par_kg_modifier").intValue();
//        return size + delta;
//    }

//    private static void check(Path path) throws FileNotFoundException {
//        if(path == null) {
//            throw new NullPointerException();
//        }
//        if(Files.notExists(path)) {
//            throw new FileNotFoundException(path.toString());
//        }
//    }

    private Converter createInputConverter() {
        DefinitionCollection dcoll = AnnotationParser.parse(InRoot.CLASSES);
        DefinitionMapper dmap = new DefinitionMapper(dcoll);
        return new Converter(dmap);
    }

    private Converter createOutputConverter() {
        DefinitionCollection dcoll = AnnotationParser.parse(OutRoot.CLASSES);
        DefinitionMapper dmap = new DefinitionMapper(dcoll);
        return new Converter(dmap);
    }

    private PerennialData<InRoot> loadInput(Converter converter) throws IOException {
        PerennialFile inFile = PerennialFile.parse(inputPath);
        return inFile.deserialize(converter);
    }

    private OutRoot createOutputRoot(InRoot inputData) {
        List<OutCustom> outGrps = new ArrayList<>();
        for(SideCustom agentGrp: inputData.customs) {
            OutCustom outGrp = new OutCustom(agentGrp._name);
            outGrp.summe = calc_par_out_IuOSonnentankNetzversorgung_Summe(agentGrp, inputData.global.mwst);
            outGrp.numberOut = calc_par_out_S_DS(agentGrp);
            outGrps.add(outGrp);
        }

        OutRoot outRoot = new OutRoot();
        outRoot.outGrps = outGrps.toArray(new OutCustom[0]);
        return outRoot;
    }

    private PerennialData<OutRoot> createOutputData(AnnualEntry<InRoot> inputEntry, OutRoot outRoot) {
        PerennialData<OutRoot> outData = new PerennialData<>();
        outData.add(inputEntry.getConfig().getYear(), outRoot);
        AnnualEntry<OutRoot> outEntry = outData.get(0);
        outEntry.getConfig().copyFrom(inputEntry.getConfig());
        return outData;
    }

    public void run() throws IOException {
        if(imagePath != null) {
            runImageDemo();
        } else {
            runOptActDemo();
        }
    }

    private void runImageDemo() {
        logger.trace("run image demo");

    }

    private void runOptActDemo() throws IOException {
        logger.trace("run optact demo");
        if(noSimulation) {
            logger.warn("no simulation");
            return;
        }

        PerennialData<InRoot> inputData = loadInput(createInputConverter());
        AnnualEntry<InRoot> inputEntry = inputData.get(0);
        OutRoot outRoot = createOutputRoot(inputEntry.getData());
        PerennialData<OutRoot> outData = createOutputData(inputEntry, outRoot);
        PerennialFile outFile = outData.serialize(createOutputConverter());
        outFile.store(outputPath);
    }

    @Override
    public Integer call() {
        if(inputFile == null) {
            logger.error("input file missing");
            System.exit(CommandLine.ExitCode.USAGE);
        }
        inputPath = Paths.get(inputFile);
        logger.debug("input file: {}", inputFile);
        if(Files.notExists(inputPath)) {
            logger.error("input file not found: {}", inputPath);
            System.exit(CommandLine.ExitCode.SOFTWARE);
        }

        if(outputFile == null) {
            if(!noSimulation) {
                logger.error("output file missing");
                System.exit(CommandLine.ExitCode.USAGE);
            }
        } else {
            outputPath = Paths.get(outputFile);
            logger.debug("output file: {}", outputPath);
        }

        if(imageFile != null) {
            imagePath = Paths.get(imageFile);
            logger.debug("image file: {}", imagePath);
        }

        if(noSimulation) {
            logger.debug("simulation disabled");
        } else {
            logger.debug("simulation enabled");
        }

        return CommandLine.ExitCode.OK;
    }

    public static void main(String[] args) throws IOException {
        OptActMain optact = new OptActMain();
        CommandLine cmdLine = new CommandLine(optact);
        int exitCode = cmdLine.execute(args);
        if(exitCode == CommandLine.ExitCode.OK) {
            optact.run();
        } else {
            System.exit(exitCode);
        }
    }
}
