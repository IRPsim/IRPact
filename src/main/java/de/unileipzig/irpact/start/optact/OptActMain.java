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

    private Path inputPath;
    private Path outputPath;

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
        if(inputPath == null) {
            throw new NullPointerException("input path");
        }
        if(outputPath == null) {
            throw new NullPointerException("output path");
        }
        if(Files.notExists(inputPath)) {
            throw new FileNotFoundException(inputPath.toString());
        }

        PerennialData<InRoot> inputData = loadInput(createInputConverter());
        AnnualEntry<InRoot> inputEntry = inputData.get(0);
        OutRoot outRoot = createOutputRoot(inputEntry.getData());
        PerennialData<OutRoot> outData = createOutputData(inputEntry, outRoot);
        PerennialFile outFile = outData.serialize(createOutputConverter());
        outFile.store(outputPath);


//        PerennialFile inFile = PerennialFile.parse(inputPath);
//        Year in2015 = inFile.getYears().get(0);
//        List<String> groups = listAgentGroups(in2015);
//
//        PerennialFile outFile = new PerennialFile();
//        Year out2015 = outFile.getYears().newYear();
//        out2015.getConfig().copyFrom(inFile.getYears().get(0).getConfig());
//
//
//        out2015.getSets().put("set_ii");
//        for(String grp: groups) {
//            out2015.getSets().put("set_side", grp);
//
//            double sunSum = calc_par_out_IuOSonnentankNetzversorgung_Summe(in2015, grp);
//            out2015.getSets().put("set_side_cust", grp, "par_out_IuOSonnentankNetzversorgung_Summe", sunSum);
//            int newSize = calc_par_out_S_DS(in2015, grp);
//            out2015.getSets().put("set_side_cust", grp, "par_out_S_DS", newSize);
//        }
    }

    @Override
    public Integer call() {
        if (inputFile == null) {
            throw new NullPointerException("input file missing");
        }
        if (outputFile == null) {
            throw new NullPointerException("output file missing");
        }
        inputPath = Paths.get(inputFile);
        outputPath = Paths.get(outputFile);
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
