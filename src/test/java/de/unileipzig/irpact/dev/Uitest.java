package de.unileipzig.irpact.dev;

import com.fasterxml.jackson.databind.node.ObjectNode;
import de.unileipzig.irpact.commons.JsonUtil;
import de.unileipzig.irpact.start.Start;
import de.unileipzig.irpact.uitest.out.OutputRoot;
import de.unileipzig.irpact.uitest.in1.InputRoot;
import de.unileipzig.irptools.defstructure.*;
import de.unileipzig.irptools.io.perennial.PerennialData;
import de.unileipzig.irptools.io.perennial.PerennialFile;
import de.unileipzig.irptools.uiedn.Sections;
import de.unileipzig.irptools.uiedn.io.EdnPrinter;
import de.unileipzig.irptools.uiedn.io.PrinterFormat;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

/**
 * @author Daniel Abitz
 */
//test
@Disabled
class Uitest {

    private static final Charset c = Charset.forName("windows-1252");

    private static final Path dir = Paths.get("").toAbsolutePath().resolve("src").resolve("main").resolve("resources");
    private static final Path inputgmsPath = dir.resolve("input/input.gms");
    private static final Path uiinputPath = dir.resolve("input/ui-input.edn");
    private static final Path uiinputdeltaPath = dir.resolve("input/ui-input-delta.edn");
    private static final Path outputgmsPath = dir.resolve("output/output.gms");
    private static final Path uioutputPath = dir.resolve("output/ui-output.edn");
    private static final Path defaultscenarioPath = dir.resolve("scenarios/default.json");
    private static final Path defaultscenario2Path = dir.resolve("scenarios/default2.json");
    private static final Path defaultscenarioPathOut = dir.resolve("scenarios/default.out2.json");

    private static void doOut(List<ParserInput> list) throws IOException {
        DefinitionCollection dcoll = AnnotationParser.parse(list);
        DefinitionMapper dmap = new DefinitionMapper(dcoll);
        GamsCollection gcoll = dmap.getGamsCollection();

        GamsPrinter.write(Type.OUTPUT, gcoll, outputgmsPath, c);

        Sections uiinput = dmap.toEdn(Type.OUTPUT);
        EdnPrinter.writeTo(uiinput, PrinterFormat.MY_DEFAULT, uioutputPath, c);
    }

    @Test
    void out1() throws IOException {
        doOut(OutputRoot.CLASSES);
    }

    @Test
    void out2() throws IOException {
        doOut(de.unileipzig.irpact.uitest.out2.OutputRoot.CLASSES);
    }

    @Test
    void runIt() throws IOException {
        Start.main(new String[]{
                "-i", "xxx",
                "-o", defaultscenarioPathOut.toString()
        });
    }

    @Test
    void runIt2() throws IOException {
        Start.main(new String[]{
                "-i", defaultscenarioPath.toString(),
                "-o", defaultscenarioPathOut.toString()
        });
    }

    @Test
    void runIt3() throws IOException {
        Start.main(new String[]{
                "-i", defaultscenario2Path.toString(),
                "-o", defaultscenarioPathOut.toString()
        });
    }

    private static void doIn(List<ParserInput> list, Object defaultData) throws IOException {
        DefinitionCollection dcoll = AnnotationParser.parse(list);
        DefinitionMapper dmap = new DefinitionMapper(dcoll);
        GamsCollection gcoll = dmap.getGamsCollection();
        Converter converter = new Converter(dmap);

        GamsPrinter.write(Type.INPUT, gcoll, inputgmsPath, c);

        Sections uiinput = dmap.toEdn(Type.INPUT);
        EdnPrinter.writeTo(uiinput, PrinterFormat.MY_DEFAULT, uiinputPath, c);

        Sections uiinputdelta = dmap.toDeltaEdn(Type.INPUT);
        EdnPrinter.writeTo(uiinputdelta, PrinterFormat.MY_DEFAULT, uiinputdeltaPath, c);

        if(defaultData != null) {
            PerennialData<Object> scenarioData = new PerennialData<>();
            scenarioData.add(2015, defaultData);
            scenarioData.getList().get(0).getConfig().init();
            scenarioData.getList().get(0).getConfig().setYear(2015);
            PerennialFile scenarioFile = scenarioData.serialize(converter);
            scenarioFile.store(defaultscenarioPath);
        }
    }

    @Test
    void v1() throws IOException {
        doIn(InputRoot.CLASSES, InputRoot.x());
    }

    @Test
    void rewriteJson() throws IOException {
        Path in = Paths.get("E:\\Temp", "zzz.json");
        Path out = in.resolveSibling("zzz.pretty.json");

        ObjectNode root = JsonUtil.readJson(in);
        JsonUtil.writeJson(root, out, JsonUtil.defaultPrinter);
    }
}
