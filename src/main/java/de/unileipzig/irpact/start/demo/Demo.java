package de.unileipzig.irpact.start.demo;

import com.fasterxml.jackson.core.JsonEncoding;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.util.DefaultIndenter;
import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import de.unileipzig.irpact.OLD.io.def.ConsumerAgentGroup;
import de.unileipzig.irpact.OLD.io.def.ConsumerAgentGroupAttribute;
import de.unileipzig.irpact.OLD.io.def.Root;
import de.unileipzig.irpact.OLD.io.Constants;
import de.unileipzig.irpact.OLD.io.SetToTree;
import joptsimple.OptionParser;
import joptsimple.OptionSet;
import joptsimple.OptionSpec;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;

/**
 * @author Daniel Abitz
 */
public class Demo {

    private static final ObjectMapper mapper = new ObjectMapper();

    private static final OptionParser parser = new OptionParser();
    private static final OptionSpec<Void> helpSpec = parser.acceptsAll(Arrays.asList("?", "h", "help"), "print help");
    private static final OptionSpec<String> inputSpec = parser.acceptsAll(Arrays.asList("i", "in", "input"), "input file")
            .withRequiredArg()
            .ofType(String.class);
    private static final OptionSpec<String> outputSpec = parser.acceptsAll(Arrays.asList("o", "out", "output"), "output file")
            .withRequiredArg()
            .ofType(String.class);

    static {
        parser.allowsUnrecognizedOptions();
    }

    private static double calcMagicOutout(ConsumerAgentGroup group) {
        double temp = 0.0;
        for(ConsumerAgentGroupAttribute attr: group.consumerAgentGroupAttributes) {
            temp += (attr.value0 * attr.value1);
        }
        temp *= group.informationAuthority;
        return temp;
    }

    private static double calcMagicOutput(Root r) {
        double sum = 0.0;
        for(ConsumerAgentGroup group: r.consumerAgentGroups) {
            sum += calcMagicOutout(group);
        }
        if(r.global != null) {
            sum *= r.global.multiplier;
        }
        return sum;
    }

    public static double calcMagicOutput(Path inputPath) throws IOException {
        ObjectNode inputSetNode;
        try(InputStream in = Files.newInputStream(inputPath)) {
            inputSetNode = (ObjectNode) mapper.readTree(in);
        }
        ObjectNode inputTreeNode = mapper.createObjectNode();
        SetToTree.handle(mapper.getNodeFactory(), inputSetNode, inputTreeNode);

        Root r = new Root();
        r.readFrom(inputTreeNode);
        return calcMagicOutput(r);
    }

    public static void handle(String[] args) throws IOException {
        OptionSet set = parser.parse(args);
        if(set.has(helpSpec)) {
            parser.printHelpOn(System.out);
            return;
        }
        Path inputPath;
        if(set.has(inputSpec)) {
            String input = set.valueOf(inputSpec);
            inputPath = Paths.get(input);
        } else {
            throw new IllegalArgumentException("Missing input file.");
        }
        Path outputPath;
        if(set.has(outputSpec)) {
            String output = set.valueOf(outputSpec);
            outputPath = Paths.get(output);
        } else {
            throw new IllegalArgumentException("Missing output file.");
        }

        double magicOutput = calcMagicOutput(inputPath);

        ObjectNode outNode = mapper.createObjectNode();
        ObjectNode scaNode = outNode.putObject(Constants.SCALARS);
        scaNode.put(Constants.SCA + "magic_output", magicOutput);
        try(OutputStream out = Files.newOutputStream(outputPath);
            JsonGenerator gen = mapper.getFactory().createGenerator(out, JsonEncoding.UTF8)) {
            gen.setPrettyPrinter(new DefaultPrettyPrinter().withArrayIndenter(DefaultIndenter.SYSTEM_LINEFEED_INSTANCE));
            gen.writeTree(outNode);
        }
    }
}
