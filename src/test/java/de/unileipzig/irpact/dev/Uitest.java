package de.unileipzig.irpact.dev;

import de.unileipzig.irpact.start.Start;
import de.unileipzig.irpact.uitest.out.OutputRoot;
import de.unileipzig.irpact.uitest.v1.InputRoot;
import de.unileipzig.irptools.defstructure.*;
import de.unileipzig.irptools.io.scenario.ScenarioData;
import de.unileipzig.irptools.io.scenario.ScenarioFile;
import de.unileipzig.irptools.uiedn.Sections;
import de.unileipzig.irptools.uiedn.io.EdnPrinter;
import de.unileipzig.irptools.uiedn.io.PrinterFormat;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

/**
 * @author Daniel Abitz
 */
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
    private static final Path defaultscenarioPathOut = dir.resolve("scenarios/default.out.json");

    private static void doOut(List<ParserInput> list) throws IOException {
        DefinitionCollection dcoll = AnnotationParser.parse(list);
        DefinitionMapper dmap = new DefinitionMapper(dcoll);
        GamsCollection gcoll = dmap.getGamsCollection();

        GamsPrinter.write(GamsType.INPUT_OUTPUT, gcoll, outputgmsPath, c);

        Sections uiinput = dmap.toEdn(GamsType.INPUT_OUTPUT);
        EdnPrinter.writeTo(uiinput, PrinterFormat.MY_DEFAULT, uioutputPath, c);
    }

    @Test
    void out() throws IOException {
        doOut(OutputRoot.CLASSES);
    }

    @Test
    void runIt() throws IOException {
        Start.main(new String[]{
                "-i", "xxx",
                "-o", defaultscenarioPathOut.toString()
        });
    }

    private static void doIn(List<ParserInput> list, Object defaultData) throws IOException {
        DefinitionCollection dcoll = AnnotationParser.parse(list);
        DefinitionMapper dmap = new DefinitionMapper(dcoll);
        GamsCollection gcoll = dmap.getGamsCollection();
        Converter converter = new Converter(dmap);

        GamsPrinter.write(GamsType.INPUT, gcoll, inputgmsPath, c);

        Sections uiinput = dmap.toEdn(GamsType.INPUT);
        EdnPrinter.writeTo(uiinput, PrinterFormat.MY_DEFAULT, uiinputPath, c);

        Sections uiinputdelta = dmap.toDeltaEdn(GamsType.INPUT);
        EdnPrinter.writeTo(uiinputdelta, PrinterFormat.MY_DEFAULT, uiinputdeltaPath, c);

        if(defaultData != null) {
            ScenarioData<Object> scenarioData = new ScenarioData<>();
            scenarioData.add(2015, defaultData);
            scenarioData.getList().get(0).getConfig().init(2015);
            ScenarioFile scenarioFile = scenarioData.serialize(converter);
            scenarioFile.store(defaultscenarioPath);
        }
    }

    @Test
    void v1() throws IOException {
        doIn(InputRoot.CLASSES, InputRoot.x());
    }
}
