package de.unileipzig.irpact.start.irptools;

import de.unileipzig.irpact.start.CommandLineInterpreter;
import de.unileipzig.irpact.start.irpact.DefaultScenarioFactory;
import de.unileipzig.irpact.start.irpact.input.IRPactInputData;
import de.unileipzig.irpact.start.irpact.output.IRPactOutputData;
import de.unileipzig.irptools.defstructure.*;
import de.unileipzig.irptools.io.scenario.ScenarioData;
import de.unileipzig.irptools.io.scenario.ScenarioFile;
import de.unileipzig.irptools.uiedn.Sections;
import de.unileipzig.irptools.uiedn.io.EdnPrinter;
import de.unileipzig.irptools.uiedn.io.PrinterFormat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Starts IRPtools.
 *
 * @author Daniel Abitz
 */
@SuppressWarnings("FieldMayBeFinal")
public class IRPtoolsStarter {

    private static final Logger logger = LoggerFactory.getLogger(IRPtoolsStarter.class);

    private CommandLineInterpreter cli;

    public IRPtoolsStarter(CommandLineInterpreter cli) {
        this.cli = cli;
    }

    public void start() throws Exception {
        if(cli.createAllDefaults()) {
            doCreateAllDefaults();
        } else {
            logger.warn("TODO");
        }
    }

//    private Path getPathOrThrowException(String path) throws NoSuchFileException, URISyntaxException {
//        URL url = getClass().getResource(path);
//        logger.trace("path: {}, url: {}", path, url);
//        if(url == null) {
//            throw new NoSuchFileException(path);
//        }
//        return Paths.get(url.toURI());
//    }

    private Path getPathOrThrowException(String path) {
        Path p = getResDir().resolve(path);
        logger.trace("path: {}, url: {}", path, p);
        return p;
    }

    //=========================
    //Defaults
    //=========================

    private Path getResDir() {
        Path root = Paths.get("").toAbsolutePath();
        return root.resolve("src").resolve("main").resolve("resources");
    }

    private void doCreateAllDefaults() throws IOException {
        logger.info("doCreateAllDefaults");

        Path inputgmsPath = getPathOrThrowException("input/input.gms");
        Path uiinputPath = getPathOrThrowException("input/ui-input.edn");
        Path uiinputdeltaPath = getPathOrThrowException("input/ui-input-delta.edn");
        Path outputgmsPath = getPathOrThrowException("output/output.gms");
        Path uioutputPath = getPathOrThrowException("output/ui-output.edn");
        Path defaultscenarioPath = getPathOrThrowException("scenarios/default.json");

        createInputFiles(inputgmsPath, uiinputPath, uiinputdeltaPath, defaultscenarioPath);
        createOutputFiles(outputgmsPath, uioutputPath);
    }

    private void createInputFiles(
            Path inputgmsPath,
            Path uiinputPath,
            Path uiinputdeltaPath,
            Path defaultscenarioPath) throws IOException {
        DefinitionCollection dcoll = AnnotationParser.parse(IRPactInputData.LIST);
        DefinitionMapper dmap = new DefinitionMapper(dcoll);
        GamsCollection gcoll = dmap.getGamsCollection();
        Converter converter = new Converter(dmap);

        GamsPrinter.write(GamsType.INPUT, gcoll, inputgmsPath, StandardCharsets.UTF_8);

        Sections uiinput = dmap.toEdn(GamsType.INPUT);
        EdnPrinter.writeTo(uiinput, PrinterFormat.MY_DEFAULT, uiinputPath, StandardCharsets.UTF_8);

        Sections uiinputdelta = dmap.toDeltaEdn(GamsType.INPUT);
        EdnPrinter.writeTo(uiinputdelta, PrinterFormat.MY_DEFAULT, uiinputdeltaPath, StandardCharsets.UTF_8);

        IRPactInputData data = DefaultScenarioFactory.createContinousInputData();
        ScenarioData<IRPactInputData> scenarioData = new ScenarioData<>();
        scenarioData.add(2015, data);
        ScenarioFile scenarioFile = scenarioData.serialize(converter);
        scenarioFile.store(defaultscenarioPath);
    }

    private void createOutputFiles(
            Path outputgmsPath,
            Path uioutputPath) throws IOException {
        DefinitionCollection dcoll = AnnotationParser.parse(IRPactOutputData.LIST);
        DefinitionMapper dmap = new DefinitionMapper(dcoll);
        GamsCollection gcoll = dmap.getGamsCollection();

        GamsPrinter.write(GamsType.INPUT_OUTPUT, gcoll, outputgmsPath, StandardCharsets.UTF_8);

        Sections uiinput = dmap.toEdn(GamsType.INPUT_OUTPUT);
        EdnPrinter.writeTo(uiinput, PrinterFormat.MY_DEFAULT, uioutputPath, StandardCharsets.UTF_8);
    }
}
