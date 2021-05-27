package de.unileipzig.irpact.start.utilities;

import com.fasterxml.jackson.databind.node.ObjectNode;
import de.unileipzig.irpact.commons.persistence.RestoreException;
import de.unileipzig.irpact.commons.util.IRPactJson;
import de.unileipzig.irpact.commons.util.csv.CsvPrinter;
import de.unileipzig.irpact.commons.util.table.SimpleTable;
import de.unileipzig.irpact.commons.util.table.Table;
import de.unileipzig.irpact.core.agent.consumer.ConsumerAgentGroup;
import de.unileipzig.irpact.core.logging.IRPLogging;
import de.unileipzig.irpact.core.logging.IRPSection;
import de.unileipzig.irpact.core.util.AnnualAdoptionData;
import de.unileipzig.irpact.develop.Todo;
import de.unileipzig.irpact.develop.TodoException;
import de.unileipzig.irpact.develop.XXXXXXXXX;
import de.unileipzig.irpact.io.param.output.xDEP.OutAnnualAdoptionData;
import de.unileipzig.irpact.io.param.output.OutRoot;
import de.unileipzig.irpact.io.param.output.xDEP.OutConsumerAgentGroup;
import de.unileipzig.irpact.jadex.agents.consumer.JadexConsumerAgentGroup;
import de.unileipzig.irpact.jadex.persistance.JadexPersistenceModul;
import de.unileipzig.irpact.start.MainCommandLineOptions;
import de.unileipzig.irpact.start.irpact.IRPact;
import de.unileipzig.irpact.util.R.ExistingRScript;
import de.unileipzig.irpact.util.R.R;
import de.unileipzig.irpact.util.R.RScriptException;
import de.unileipzig.irptools.io.base.AnnualEntry;
import de.unileipzig.irptools.util.Util;
import de.unileipzig.irptools.util.log.IRPLogger;
import picocli.CommandLine;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Daniel Abitz
 */
public class Utilities {

    private static final IRPLogger LOGGER = IRPLogging.getLogger(Utilities.class);

    protected MainCommandLineOptions mainOptions;
    protected UtilitiesCommandLineOptions utilOptions;

    public Utilities(
            MainCommandLineOptions mainOptions,
            UtilitiesCommandLineOptions utilOptions) {
        setMainOptions(mainOptions);
        setUtilitiesOptions(utilOptions);
    }

    public void setMainOptions(MainCommandLineOptions mainOptions) {
        this.mainOptions = mainOptions;
    }

    public void setUtilitiesOptions(UtilitiesCommandLineOptions utilOptions) {
        this.utilOptions = utilOptions;
    }

    public void run() throws Exception {
        if(utilOptions.isPrintCumulativeAdoptions()) {
            printCumulativeAdoptions();
        }
        if(utilOptions.hasDecodeBinaryPaths()) {
            decodeBinaryPersist();
        }
    }

    //=========================
    //input printing
    //=========================

    /*

        if(CL_OPTIONS.hasInputOutPath()) {
            printInput();
            if(CL_OPTIONS.isNoSimulationAndNoImage()) {
                LOGGER.info(IRPSection.GENERAL, "no simulation");
                return;
            }
        }

    private void printIn() throws IOException {
        AnnualData<InRoot> data = new AnnualData<>(inEntry);
        AnnualFile file = data.serialize(getInputConverter(CL_OPTIONS));
        file.store(Paths.get("temp.json"));
        if(true) throw new RuntimeException();
    }
     */

    //=========================
    //spec <> param
    //=========================

    /*
    private void convertParamToSpec(ObjectNode root) throws Exception {
        LOGGER.trace("convert parameter to specification");
        AnnualEntry<InRoot> entry = IRPact.convert(clOptions, root);
        InRoot inRoot = entry.getData();
        inRoot.general.firstSimulationYear = entry.getConfig().getYear(); //!
        SpecificationConverter converter = new SpecificationConverter();
        SpecificationData data = converter.toSpec(inRoot);
        data.store(clOptions.getSpecOutputDirPath());
    }

    private void convertSpecToParam() throws Exception {
        LOGGER.trace("convert specification to parameter");
        SpecificationConverter converter = new SpecificationConverter();
        InRoot root =  converter.toParam(clOptions.getSpecInputDirPath());
        PerennialData<InRoot> pData = new PerennialData<>();
        pData.add(root.general.firstSimulationYear, root);
        PerennialFile pFile = pData.serialize(IRPact.getInputConverter(clOptions));
        pFile.store(clOptions.getOutputPath(), StandardCharsets.UTF_8);
        LOGGER.trace(IRPSection.SPECIFICATION_CONVERTER, "param file stored: '{}'", clOptions.getOutputPath());
    }
     */

    //=========================
    //print cumulative adoptions
    //=========================

    private void printCumulativeAdoptions() throws IOException, RScriptException, InterruptedException {
        LOGGER.trace(IRPSection.UTILITIES, "task: print cumulative adoptions with R");

        Path tempFile = getTempCsvFile(utilOptions.getROutput());
        try {
            printTempCsvFile(tempFile);
            runRScriptForCumulativeAdoptions(tempFile);
        } catch (RScriptException e) {
            String content = Util.readString(tempFile, StandardCharsets.UTF_8);
            LOGGER.error(content);
            throw e;
        } finally {
            if(Files.exists(tempFile)) {
                LOGGER.trace(IRPSection.UTILITIES, "delete temp csv file '{}'", tempFile);
                Files.delete(tempFile);
            }
        }

        LOGGER.trace(IRPSection.UTILITIES, "task 'print cumulative adoptions with R' finished");
    }

    @XXXXXXXXX
    @Todo
    private void printTempCsvFile(Path tempFile) throws IOException {
        LOGGER.trace(IRPSection.UTILITIES, "convert data to csv");
        if(true) throw new TodoException();
//        ObjectNode rootNode = parseRInputAsOutRoot();
//        AnnualEntry<OutRoot> outRootEntry = IRPact.convertOutput(mainOptions, rootNode);
//        OutRoot outRoot = outRootEntry.getData();
//        AnnualAdoptionData data = parseWithDummy(outRoot.getAnnualAdoptionData());
//        Table<String> table = toStringTable(data);
//        LOGGER.trace(IRPSection.UTILITIES, "save temp csv file '{}'", tempFile);
//        printCsv(tempFile, table);
    }

    private void runRScriptForCumulativeAdoptions(Path tempFile) throws IOException, RScriptException, InterruptedException {
        LOGGER.trace(IRPSection.UTILITIES, "run R");
        runRScriptWithInputAndOutput(
                utilOptions.getRExe(),
                utilOptions.getRScript(),
                tempFile,
                utilOptions.getROutput()
        );
    }

    private ObjectNode parseRInputAsOutRoot() throws IOException {
        LOGGER.trace(IRPSection.UTILITIES, "parse r input '{}' as OutRoot", utilOptions.getRInput());
        return IRPactJson.readJson(utilOptions.getRInput());
    }

    private static Path getTempCsvFile(Path input) {
        String fileName = input.getFileName().toString();
        Path tempFile = input.resolveSibling(fileName + ".temp.csv");
        LOGGER.trace(IRPSection.UTILITIES, "create temp file '{}'", tempFile);
        return tempFile;
    }

    private static AnnualAdoptionData parseWithDummy(OutAnnualAdoptionData... datas) {
        Map<String, ConsumerAgentGroup> cache = new HashMap<>();
        AnnualAdoptionData outData = new AnnualAdoptionData();
        for(OutAnnualAdoptionData data: datas) {
            for(OutConsumerAgentGroup inCag: data.getConsumerAgentGroups()) {
                JadexConsumerAgentGroup cag = (JadexConsumerAgentGroup) cache.computeIfAbsent(
                        inCag.getName(),
                        _name -> {
                            JadexConsumerAgentGroup _cag = new JadexConsumerAgentGroup();
                            _cag.setName(_name);
                            return _cag;
                        }
                );

                outData.set(
                        data.getYear(),
                        cag,
                        data.getAdoptionsThisYear(inCag),
                        data.getAdoptionsCumulativ(inCag),
                        data.getAdoptionShareThisYear(inCag),
                        data.getAdoptionShareCumulativ(inCag)
                );
            }
        }
        return outData;
    }

    private static Table<String> toStringTable(AnnualAdoptionData data) {
        SimpleTable<String> table = new SimpleTable<>();
        table.addColumns("year", "milieu", "adoptions", "adoptionsCumulative", "adoptionsShare", "adoptionsShareCumulative");
        for(int year: data.getYears()) {
            for(ConsumerAgentGroup cag: data.getConsumerAgentGroups()) {
                int adoptions = data.getAdoptionsMap().get(year, cag);
                int adoptionsCumulative = data.getAdoptionsCumulativMap().get(year, cag);
                double adoptionsShare = data.getAdoptionsShareMap().get(year, cag);
                double adoptionsShareCumulative = data.getAdoptionsShareCumulativeMap().get(year, cag);

                table.addRow(
                        Integer.toString(year),
                        cag.getName(),
                        Integer.toString(adoptions),
                        Integer.toString(adoptionsCumulative),
                        Double.toString(adoptionsShare),
                        Double.toString(adoptionsShareCumulative)
                );
            }
        }

        return table;
    }

    private static void printCsv(Path target, Table<String> table) throws IOException {
        CsvPrinter<String> printer = new CsvPrinter<>(str -> str);
        printer.setDelimiter(",");
        printer.setPrintFinalEmptyLine(true);
        printer.write(target, StandardCharsets.UTF_8, table.getHeader(), table.listTable());
    }

    private static void runRScriptWithInputAndOutput(
            Path rExe,
            Path rScript,
            Path input,
            Path output) throws IOException, RScriptException, InterruptedException {
        runRScriptWithArguments(rExe, rScript, input.toString(), output.toString());
    }

    private static void runRScriptWithArguments(
            Path rExe,
            Path rScript,
            String... args) throws IOException, RScriptException, InterruptedException {
        R engine = new R(rExe);
        ExistingRScript script = new ExistingRScript(rScript);
        script.addArgs(args);
        LOGGER.trace(IRPSection.UTILITIES, "run command: {}", script.peekCommand(engine));
        script.execute(engine);
        if(script.hasWarning()) {
            LOGGER.warn("R warning:\n{}", script.printWarning());
        }
    }

    //=========================
    //decode binary persist
    //=========================

    public void decodeBinaryPersist() throws IOException, RestoreException {
        Path input = utilOptions.getDecodeBinaryPaths()[0];
        Path output = utilOptions.getDecodeBinaryPaths()[1];

        LOGGER.trace("decode binary persist '{}' -> '{}'", input, output);

        ObjectNode root = IRPactJson.readJson(input);
        AnnualEntry<OutRoot> entry = IRPact.convertOutput(mainOptions, root);

        JadexPersistenceModul modul = new JadexPersistenceModul();
        ObjectNode decoded = modul.decode(entry.getData().getBinaryPersistData());
        IRPactJson.writeJson(decoded, output, IRPactJson.DEFAULT);
    }

    //=========================
    //start
    //=========================

    public static void run(MainCommandLineOptions mainOptions, String[] args) throws Exception {
        UtilitiesCommandLineOptions utilOptions = new UtilitiesCommandLineOptions(args);
        int exitCode = utilOptions.parse();

        if(exitCode != CommandLine.ExitCode.OK) {
            if(utilOptions.hasExecuteResultMessage()) {
                utilOptions.getExecuteResultMessage().error(LOGGER);
            }
            return;
        }

        if(utilOptions.hasExecuteResultMessage()) {
            utilOptions.getExecuteResultMessage().trace(LOGGER);
        }

        if(utilOptions.isPrintHelpOrVersion()) {
            if(utilOptions.isPrintVersion()) {
                utilOptions.getCommandLine().printVersionHelp(System.out);
            }
            if(utilOptions.isPrintHelp()) {
                utilOptions.getCommandLine().usage(System.out);
            }
            return;
        }

        Utilities utilities = new Utilities(mainOptions, utilOptions);
        utilities.run();
    }
}
