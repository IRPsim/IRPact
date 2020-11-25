package de.unileipzig.irpact.start.irpact2;

import ch.qos.logback.classic.Level;
import de.unileipzig.irpact.v2.commons.log.Logback;
import de.unileipzig.irpact.v2.core.misc.DebugLevel;
import de.unileipzig.irpact.v2.io.JadexInputConverter;
import de.unileipzig.irpact.v2.io.input.IRoot;
import de.unileipzig.irpact.v2.io.output.ORoot;
import de.unileipzig.irpact.v2.jadex.simulation.JadexSimulationEnvironment;
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

import java.io.IOException;
import java.nio.file.Path;

/**
 * @author Daniel Abitz
 */
@SuppressWarnings("FieldCanBeLocal")
public class IRPact {

    private static final Logger logger = LoggerFactory.getLogger(IRPact.class);

    private DefinitionCollection inputColl;
    private DefinitionMapper inputMap;
    private Converter inputConverter;
    private DefinitionCollection outputColl;
    private DefinitionMapper outputMap;
    private Converter outputConverter;

    private IRPactCommandLine clParam;
    private AnnualEntry<IRoot> inputEntry;
    private JadexSimulationEnvironment environment;
    private Platform platform;

    public IRPact() {
    }

    public int init(String[] args) {
        clParam = new IRPactCommandLine(args);
        return clParam.getExitCode();
    }

    private void setup() {
        Logback.setupSystemOutAndErr();
        Logback.setLevel(Level.TRACE);

        inputColl = AnnotationParser.parse(IRoot.CLASSES);
        inputMap = new DefinitionMapper(inputColl);
        inputConverter = new Converter(inputMap);

        outputColl = AnnotationParser.parse(ORoot.CLASSES);
        outputMap = new DefinitionMapper(outputColl);
        outputConverter = new Converter(outputMap);
    }

    private void setupLogging(DebugLevel debugLevel) {
        switch (debugLevel) {
            case DEFAULT:
                Logback.setLevel(Level.INFO);
                break;
            case DEBUG:
                Logback.setLevel(Level.DEBUG);
                break;
            case TRACE:
                Logback.setLevel(Level.TRACE);
                break;
        }
        logger.info("Debuglevel: {}", debugLevel);
    }

    private void createPlatform() throws InterruptedException {
        platform = new Platform();
        platform.init(inputEntry, environment);
        platform.run();
    }

    public void run() throws IOException, InterruptedException {
        setup();

        Path input = clParam.getInputPath();
        PerennialFile inputFile = PerennialFile.parse(input);
        PerennialData<IRoot> inputData = inputFile.deserialize(inputConverter);
        inputEntry = inputData.get(0);
        environment = JadexInputConverter.INSTANCE.build(inputEntry.getData());
        setupLogging(environment.getDebugLevel());

        logger.info("Input: {}", input);
        logger.info("Output: {}", clParam.getOutputPath());

        createPlatform();
    }

    public static void main(String[] args) throws IOException, InterruptedException {
        IRPact app = new IRPact();
        int exitCode = app.init(args);
        if(exitCode != CommandLine.ExitCode.OK) {
            System.exit(exitCode);
        } else {
            app.run();
        }
    }
}
