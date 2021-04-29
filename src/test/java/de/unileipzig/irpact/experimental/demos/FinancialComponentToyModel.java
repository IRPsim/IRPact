package de.unileipzig.irpact.experimental.demos;

import de.unileipzig.irpact.commons.util.csv.CsvValuePrinter;
import de.unileipzig.irpact.commons.util.xlsx.XlsxSheetParser;
import de.unileipzig.irpact.commons.util.xlsx.XlsxTable;
import de.unileipzig.irpact.commons.util.csv.CsvPrinter;
import de.unileipzig.irpact.commons.util.xlsx.XlsxValue;
import de.unileipzig.irpact.core.log.IRPLevel;
import de.unileipzig.irpact.core.process.ra.RAConstants;
import de.unileipzig.irpact.core.simulation.tasks.PredefinedPrePlatformCreationTask;
import de.unileipzig.irpact.core.spatial.twodim.Metric2D;
import de.unileipzig.irpact.io.param.input.InAttributeName;
import de.unileipzig.irpact.io.param.input.InGeneral;
import de.unileipzig.irpact.io.param.input.InRoot;
import de.unileipzig.irpact.io.param.input.InVersion;
import de.unileipzig.irpact.io.param.input.affinity.InComplexAffinityEntry;
import de.unileipzig.irpact.io.param.input.agent.consumer.InPVactConsumerAgentGroup;
import de.unileipzig.irpact.io.param.input.agent.population.InRelativeExternConsumerAgentPopulationSize;
import de.unileipzig.irpact.io.param.input.binary.VisibleBinaryData;
import de.unileipzig.irpact.io.param.input.distribution.InConstantUnivariateDistribution;
import de.unileipzig.irpact.io.param.input.file.InPVFile;
import de.unileipzig.irpact.io.param.input.file.InSpatialTableFile;
import de.unileipzig.irpact.io.param.input.network.InGraphTopologyScheme;
import de.unileipzig.irpact.io.param.input.network.InUnlinkedGraphTopology;
import de.unileipzig.irpact.io.param.input.process.InProcessModel;
import de.unileipzig.irpact.io.param.input.process.ra.InPVactUncertaintyGroupAttribute;
import de.unileipzig.irpact.io.param.input.process.ra.InRAProcessModel;
import de.unileipzig.irpact.io.param.input.spatial.InSpace2D;
import de.unileipzig.irpact.io.param.input.spatial.InSpatialModel;
import de.unileipzig.irpact.io.param.input.spatial.dist.InFileSelectedSpatialDistribution2D;
import de.unileipzig.irpact.io.param.input.time.InTimeModel;
import de.unileipzig.irpact.io.param.input.time.InUnitStepDiscreteTimeModel;
import de.unileipzig.irpact.misc.logfile.SimpleLogFileLines;
import de.unileipzig.irpact.misc.logfile.filters.ContainsLineFilter;
import de.unileipzig.irpact.misc.logfile.modify.RemoveUntilTextModifier;
import de.unileipzig.irpact.misc.logfile.sort.NumericLogFileLineSorter;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;

/**
 * @author Daniel Abitz
 */
@Disabled
public class FinancialComponentToyModel {

    @Test
    void xx() throws IOException {
        Path in = Paths.get("D:\\Prog\\JetBrains\\SUSICProjects\\IRPact\\testfiles\\uitests\\x9\\scenariosX", "fc_test-log.log");
        Path out = Paths.get("D:\\Prog\\JetBrains\\SUSICProjects\\IRPact\\testfiles\\uitests\\x9\\scenariosX", "fc_test-log-out.log");
        SimpleLogFileLines log = SimpleLogFileLines.parse(in, StandardCharsets.UTF_8, new ContainsLineFilter("[FCX]"));
        System.out.println(log.totalLines());
        log.modifyThis(new RemoveUntilTextModifier("[FCX]", true));
        log.sortThis(new NumericLogFileLineSorter(",", 0));
        log.addLine(0, "ID,Milieu,FC");
        log.writeTo(out, StandardCharsets.UTF_8);
    }

    @Test
    void testParseAndCopyKK() throws IOException, InvalidFormatException {
        Path in = Paths.get("D:\\Prog\\JetBrains\\SUSICProjects\\IRPact\\testfiles\\0data", "Datensatz_210322.xlsx");
        Path out = Paths.get("D:\\Prog\\JetBrains\\SUSICProjects\\IRPact\\testfiles\\0data", "Datensatz_210322_KK.csv");

        XlsxSheetParser<XlsxValue> parser = XlsxValue.newParser();
        parser.setNumberOfInfoRows(1);

        XlsxTable<XlsxValue> table = new XlsxTable<>();
        table.load(parser, in);

        XlsxTable<XlsxValue> mini = table.copyToNewTable("ID", "Milieu", "KK_Index");
        CsvPrinter<XlsxValue> printer = new CsvPrinter<>((columnIndex, header, value) -> {
            if("ID".equals(header[columnIndex])) {
                return Integer.toString((int) value.doubleValue());
            } else {
                return value.printValue();
            }
        });
        printer.setDelimiter(",");
        printer.write(out, StandardCharsets.UTF_8, mini.getHeader(), mini.listTable());
    }

    private static InRoot createRoot() {
        //files
        InPVFile pvFile = new InPVFile("Barwertrechner");
        InSpatialTableFile tableFile = new InSpatialTableFile("Datensatz_210322");

        //dist
        InConstantUnivariateDistribution diraq0 = new InConstantUnivariateDistribution("diraq0", 0);

        InAttributeName domMilieu = new InAttributeName(RAConstants.DOM_MILIEU);
        InFileSelectedSpatialDistribution2D spatialDist = new InFileSelectedSpatialDistribution2D(
                "SpatialDist",
                new InAttributeName(RAConstants.X_CENT),
                new InAttributeName(RAConstants.Y_CENT),
                tableFile,
                domMilieu
        );

        //cags
        InPVactConsumerAgentGroup[] cags = ToyModelUtil.createNullPVcags(Arrays.asList("PRA", "PER", "SOK", "BUM", "PRE", "EPE", "TRA", "KET", "LIB", "HED", "G"), diraq0);
        Arrays.stream(cags).forEach(cag -> cag.setSpatialDistribution(spatialDist));
        Arrays.stream(cags).forEach(cag -> cag.a1 = null);

        InComplexAffinityEntry[] affinities = InComplexAffinityEntry.buildAll(cags, 0);

        InUnlinkedGraphTopology topology = new InUnlinkedGraphTopology("Unlinked_Topology");

        //Population
        InRelativeExternConsumerAgentPopulationSize populationSize = new InRelativeExternConsumerAgentPopulationSize(
                "Pop",
                cags,
                tableFile,
                domMilieu
        );

        InUnitStepDiscreteTimeModel timeModel = new InUnitStepDiscreteTimeModel();
        timeModel.setName("Discrete_TimeModel");
        timeModel.setAmountOfTime(1);
        timeModel.setUnit(ChronoUnit.WEEKS);


        InPVactUncertaintyGroupAttribute uncertainty = new InPVactUncertaintyGroupAttribute();
        uncertainty.setName("uncert");
        uncertainty.setGroups(cags);
        uncertainty.setForAll(diraq0);

        InRAProcessModel processModel = new InRAProcessModel();
        processModel.setName("RA_ProcessModel");
        processModel.setABCD(0.25);
        processModel.setDefaultPoints();
        processModel.setLogisticFactor(1.0 / 8.0);
        processModel.setUncertaintyGroupAttribute(uncertainty);
        processModel.setPvFile(pvFile);


        InSpace2D space2D = new InSpace2D("Space2D", Metric2D.HAVERSINE_KM);

        PredefinedPrePlatformCreationTask fcTask = new PredefinedPrePlatformCreationTask();
        fcTask.setInfo("PRINT_FC");
        fcTask.setTask(PredefinedPrePlatformCreationTask.FINANCIAL_COMPONENT);
        VisibleBinaryData vbd = new VisibleBinaryData();
        vbd.setID(fcTask.getID());
        vbd.setBytes(fcTask.getBytes());

        InGeneral general = new InGeneral();
        general.seed = 42;
        general.timeout = TimeUnit.MINUTES.toMillis(1);
        general.runOptActDemo = false;
        general.runPVAct = true;
        general.logLevel = IRPLevel.ALL.getLevelId();
        general.logAllIRPact = true;
        general.enableAllDataLogging();
        general.enableAllResultLogging();
        general.firstSimulationYear = 2015;
        general.lastSimulationYear = 2015;

        //=====
        InRoot root = new InRoot();
        root.version = new InVersion[]{InVersion.currentVersion()};
        root.general = general;
        root.affinityEntries = affinities;
        root.consumerAgentGroups = cags;
        root.setAgentPopulationSize(populationSize);
        root.graphTopologySchemes = new InGraphTopologyScheme[]{topology};
        root.processModels = new InProcessModel[]{processModel};
        root.spatialModel = new InSpatialModel[]{space2D};
        root.timeModel = new InTimeModel[]{timeModel};
        root.visibleBinaryData = new VisibleBinaryData[]{vbd};

        return root;
    }

    @Test
    void runThisModel() {
        ToyModelUtil.runNoSimulation("fc_test", createRoot(), false);
    }
}
