package de.unileipzig.irpact.disabled;

import com.fasterxml.jackson.core.JsonEncoding;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.util.DefaultIndenter;
import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import de.unileipzig.irpact.input.def.ConsumerAgentGroup;
import de.unileipzig.irpact.input.def.ConsumerAgentGroupAttribute;
import de.unileipzig.irpact.input.def.Root;
import de.unileipzig.irpact.io.*;
import de.unileipzig.irpact.start.Demo;
import de.unileipzig.irpact.start.Start;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;

/**
 * @author Daniel Abitz
 */
@Disabled
class TreeSetTest {

    private static final Path dir = Paths.get("D:\\Prog\\JetBrains\\SUSICProjects\\IRPact\\src\\main\\java\\de\\unileipzig\\irpact\\input\\def");

    /*
    @Test
    void testIt() throws IOException {
        Parser p = new Parser();
        p.parse(dir);
    }
    */

    @Test
    void asd() {
        String x = "par_x_v_d";
        System.out.println(Arrays.toString(x.split("_")));
    }

    @Test
    void testParseInterface() throws IOException {
        Path testInterface = Paths.get("D:\\Prog\\JetBrains\\SUSICProjects\\IRPact\\src\\main\\java\\de\\unileipzig\\irpact\\input\\def", "ConsumerAgentGroup.java");
        /*
        Input i1;
        try(BufferedReader reader = Files.newBufferedReader(testInterface, StandardCharsets.UTF_8)) {
            i1 = Input.parse(reader);
            System.out.println(i1.getType());
            System.out.println(i1.getName());
            System.out.println(i1.getParamMap());
            System.out.println(i1.getInterfaceList());
            System.out.println(i1.getFields());
        }
        */
        Input i2;
        try(BufferedReader reader = Files.newBufferedReader(testInterface, StandardCharsets.UTF_8)) {
            i2 = InputParser.parse(reader);
            System.out.println(i2.getType());
            System.out.println(i2.getName());
            System.out.println(i2.getParamMap());
            System.out.println(i2.getInterfaceList());
            System.out.println(i2.getFields());
        }
        //System.out.println(i1.equals(i2));
        //System.out.println(i2.equals(i1));
    }

    @Test
    void testParseAll() throws IOException {
        InputMapping map = InputMapping.parseDir(dir);
        map.getInputMap().values()
                .forEach(input -> {
                    System.out.println(input.getParamMap());
                    System.out.println(input.getType());
                    System.out.println(input.getName());
                    System.out.println(input.getInterfaceList());
                    input.getFields().forEach(
                            field -> {
                                System.out.println("  " + field.getParamMap());
                                System.out.println("  " + field.getType());
                                System.out.println("  " + field.getName());
                                System.out.println("  " + field.isArray());
                                System.out.println("---");
                            }
                    );
                    System.out.println("===");
                });
        InputMapping.Result mi = map.getResult();
        mi.forEach(input -> {
            System.out.println(input.getParam());
            System.out.println(input.getName());
            System.out.println(input.getDeps());
            System.out.println("===");
        });
        mi.forEach(input -> {
            System.out.println(input.printGAMS());
        });
    }

    private static ObjectMapper mapper = new ObjectMapper();

    private static ObjectNode parse(Path p) throws IOException {
        try(InputStream in = Files.newInputStream(p)) {
            return (ObjectNode) mapper.readTree(in);
        }
    }

    private static String print(JsonNode node) throws IOException {
        StringWriter sw = new StringWriter();
        try(JsonGenerator gen = mapper.getFactory().createGenerator(sw)) {
            gen.setPrettyPrinter(new DefaultPrettyPrinter().withArrayIndenter(DefaultIndenter.SYSTEM_LINEFEED_INSTANCE));
            gen.writeTree(node);
        }
        return sw.toString();
    }

    @Test
    void as4d() throws IOException {
        Path p = TestPaths.resInput.resolve("input-tree.json");
        ObjectNode root = parse(p);
        ObjectNode setRoot = mapper.createObjectNode();
        TreeToSet.handle(root, setRoot);
        System.out.println(print(setRoot));
    }

    @Test
    void as4d22() throws IOException {
        Path p = TestPaths.resInput.resolve("input2-tree.json");
        ObjectNode root = parse(p);
        ObjectNode setRoot = mapper.createObjectNode();
        TreeToSet.handle(root, setRoot);
        System.out.println(print(setRoot));
    }

    @Test
    void as4d33() throws IOException {
        Path p = TestPaths.resInput.resolve("input3-tree.json");
        ObjectNode root = parse(p);
        ObjectNode setRoot = mapper.createObjectNode();
        TreeToSet.handle(root, setRoot);
        System.out.println(print(setRoot));
    }

    @Test
    void as4d2() throws IOException {
        Path p = Paths.get("D:\\Prog\\JetBrains\\SUSICProjects\\IRPact\\src\\main\\java\\de\\unileipzig\\irpact\\input\\examples\\maptests", "input-tree2.json");
        ObjectNode root = parse(p);
        ObjectNode setRoot = mapper.createObjectNode();
        TreeToSet.handle(root, setRoot);
        System.out.println(print(setRoot));
    }

    @Test
    void testSetToTree() throws IOException {
        Path p = Paths.get("D:\\Prog\\JetBrains\\SUSICProjects\\IRPact\\src\\main\\resources\\irpact\\examples\\input", "input.json");
        ObjectNode setRoot = parse(p);
        ObjectNode treeRoot = mapper.createObjectNode();
        SetToTree.handle(mapper.getNodeFactory(), setRoot, treeRoot);
        System.out.println(print(treeRoot));
        //===
        Path p2 = Paths.get("D:\\Prog\\JetBrains\\SUSICProjects\\IRPact\\src\\main\\resources\\irpact\\examples\\input", "input-tree.json");
        ObjectNode tree2 = parse(p2);
        System.out.println(tree2.equals(treeRoot));
    }

    @Test
    void testSetToTreeScalar() throws IOException {
        Path p = Paths.get("D:\\Prog\\JetBrains\\SUSICProjects\\IRPact\\src\\main\\resources\\irpact\\examples\\input", "input3.json");
        ObjectNode setRoot = parse(p);
        ObjectNode treeRoot = mapper.createObjectNode();
        SetToTree.handle(mapper.getNodeFactory(), setRoot, treeRoot);
        System.out.println(print(treeRoot));
        //===
        Path p2 = Paths.get("D:\\Prog\\JetBrains\\SUSICProjects\\IRPact\\src\\main\\resources\\irpact\\examples\\input", "input3-tree.json");
        ObjectNode tree2 = parse(p2);
        System.out.println(tree2.equals(treeRoot));
    }

    private static final Path testFilesDir = Paths.get("D:\\Prog\\JetBrains\\SUSICProjects\\IRPact\\testfiles");

    @Test
    void testCreateAndWrite() throws IOException {
        Root r = new Root();
        r.consumerAgentGroups = new ConsumerAgentGroup[2];
        r.consumerAgentGroups[0] = new ConsumerAgentGroup();
        r.consumerAgentGroups[0]._name = "group0";
        r.consumerAgentGroups[0].informationAuthority = 1;
        r.consumerAgentGroups[0].consumerAgentGroupAttributes = new ConsumerAgentGroupAttribute[2];
        r.consumerAgentGroups[0].consumerAgentGroupAttributes[0] = new ConsumerAgentGroupAttribute();
        r.consumerAgentGroups[0].consumerAgentGroupAttributes[0]._name = "attr1";
        r.consumerAgentGroups[0].consumerAgentGroupAttributes[0].value0 = 1;
        r.consumerAgentGroups[0].consumerAgentGroupAttributes[0].value1 = 2;
        r.consumerAgentGroups[0].consumerAgentGroupAttributes[1] = new ConsumerAgentGroupAttribute();
        r.consumerAgentGroups[0].consumerAgentGroupAttributes[1]._name = "attr2";
        r.consumerAgentGroups[0].consumerAgentGroupAttributes[1].value0 = 3;
        r.consumerAgentGroups[0].consumerAgentGroupAttributes[1].value1 = 4;
        r.consumerAgentGroups[1] = new ConsumerAgentGroup();
        r.consumerAgentGroups[1]._name = "group1";
        r.consumerAgentGroups[1].informationAuthority = 2;
        r.consumerAgentGroups[1].consumerAgentGroupAttributes = new ConsumerAgentGroupAttribute[1];
        r.consumerAgentGroups[1].consumerAgentGroupAttributes[0] = new ConsumerAgentGroupAttribute();
        r.consumerAgentGroups[1].consumerAgentGroupAttributes[0]._name = "attr3";
        r.consumerAgentGroups[1].consumerAgentGroupAttributes[0].value0 = 5;
        r.consumerAgentGroups[1].consumerAgentGroupAttributes[0].value1 = 6;

        ObjectNode treeRoot = mapper.createObjectNode();
        r.writeTo(treeRoot);

        ObjectNode setRoot = mapper.createObjectNode();

        TreeToSet.handle(treeRoot, setRoot);

        /*
        System.out.println(print(treeRoot));
        System.out.println("===");
        System.out.println(print(setRoot));
        */

        Path inputPath = testFilesDir.resolve("test-input.json");
        try(OutputStream out = Files.newOutputStream(inputPath);
            JsonGenerator gen = mapper.getFactory().createGenerator(out, JsonEncoding.UTF8)) {
            gen.setPrettyPrinter(new DefaultPrettyPrinter().withArrayIndenter(DefaultIndenter.SYSTEM_LINEFEED_INSTANCE));
            gen.writeTree(setRoot);
        }

        Path outputPath = testFilesDir.resolve("test-output.json");
        String[] args = {
                "--i", inputPath.toString(),
                "--o", outputPath.toString()
        };
        Start.main(args);
    }

    @Test
    void testDemoHelp() throws IOException {
        Start.main(new String[]{"--?"});
    }

    @Test
    void testDemo1() throws IOException {
        String[] args = {
                "--i", Paths.get("D:\\Prog\\JetBrains\\SUSICProjects\\IRPact\\src\\main\\resources\\irpact\\examples\\input", "input.json").toString(),
                "--o", testFilesDir.resolve("output.json").toString()
        };
        Start.main(args);
    }

    @Test
    void testDemo2() throws IOException {
        String[] args = {
                "--i", Paths.get("D:\\Prog\\JetBrains\\SUSICProjects\\IRPact\\src\\main\\resources\\irpact\\examples\\input", "input2.json").toString(),
                "--o", testFilesDir.resolve("output2.json").toString()
        };
        Start.main(args);
    }

    @Test
    void testDemo1Real() throws IOException {
        Path p = Paths.get("D:\\Prog\\JetBrains\\SUSICProjects\\IRPact\\src\\main\\resources\\irpact\\examples\\input", "input.json");
        double m = Demo.calcMagicOutput(p);
        System.out.println(m);
    }

    @Test
    void testDemo2Real() throws IOException {
        Path p = Paths.get("D:\\Prog\\JetBrains\\SUSICProjects\\IRPact\\src\\main\\resources\\irpact\\examples\\input", "input2.json");
        double m = Demo.calcMagicOutput(p);
        System.out.println(m);
    }

    @Test
    void testDemo3Real() throws IOException {
        Path p = Paths.get("D:\\Prog\\JetBrains\\SUSICProjects\\IRPact\\src\\main\\resources\\irpact\\examples\\input", "input3.json");
        double m = Demo.calcMagicOutput(p);
        System.out.println(m);
    }
}
