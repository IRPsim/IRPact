package de.unileipzig.irpact.start.optact;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import de.unileipzig.irptools.io.base.Year;
import de.unileipzig.irptools.io.perennial.PerennialFile;
import de.unileipzig.irptools.util.Util;
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

    public void init(Path inputPath, Path outputPath) {
        this.inputPath = inputPath;
        this.outputPath = outputPath;
    }

    private static double calc_par_out_IuOSonnentankNetzversorgung_Summe(Year year, String agentGroup) {
        double mwst = year.getScalars().getNode("sca_Tax_PS_vat").doubleValue();
        JsonNode node = year.getSets().getNode("set_side_cust").get(agentGroup).get("par_IuO_ESector_CustSide");
        if(node == null || node.isMissingNode() || node.isNull()) {
            throw new NoSuchElementException("par_IuO_ESector_CustSide @ " + agentGroup);
        }
        if(node.isTextual()) {
            String id = node.textValue();
            int intId = Integer.parseInt(id);
            return intId * mwst;
        } else {
            double[] arr = Util.toDoubleArray((ArrayNode) node);
            double sum = Arrays.stream(arr).sum();
            return sum * mwst;
        }
    }

    private static List<String> listAgentGroups(Year year) {
        JsonNode node = year.getSets().getNode("set_side_cust");
        List<String> groups = new ArrayList<>();
        for(Map.Entry<String, JsonNode> entry: Util.iterate(node)) {
            groups.add(entry.getKey());
        }
        return groups;
    }

    private static int calc_par_out_S_DS(Year year, String agentGroup) {
        int size = year.getSets().getNode("set_side_cust").get(agentGroup).get("par_S_DS").intValue();
        int delta = year.getSets().getNode("set_side_cust").get(agentGroup).get("par_kg_modifier").intValue();
        return size + delta;
    }

    private static void check(Path path) throws FileNotFoundException {
        if(path == null) {
            throw new NullPointerException();
        }
        if(Files.notExists(path)) {
            throw new FileNotFoundException(path.toString());
        }
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

        PerennialFile inFile = PerennialFile.parse(inputPath);
        Year in2015 = inFile.getYears().get(0);
        List<String> groups = listAgentGroups(in2015);

        PerennialFile outFile = new PerennialFile();
        Year out2015 = outFile.getYears().newYear();
        out2015.getConfig().copyFrom(inFile.getYears().get(0).getConfig());

        out2015.getSets().put("set_ii");
        for(String grp: groups) {
            out2015.getSets().put("set_side", grp);

            double sunSum = calc_par_out_IuOSonnentankNetzversorgung_Summe(in2015, grp);
            out2015.getSets().put("set_side_cust", grp, "par_out_IuOSonnentankNetzversorgung_Summe", sunSum);
            int newSize = calc_par_out_S_DS(in2015, grp);
            out2015.getSets().put("set_side_cust", grp, "par_out_S_DS", newSize);
        }

        outFile.store(outputPath);
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
}
