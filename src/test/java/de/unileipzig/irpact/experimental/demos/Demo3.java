package de.unileipzig.irpact.experimental.demos;

import de.unileipzig.irpact.commons.exception.ParsingException;
import de.unileipzig.irpact.core.log.IRPLevel;
import de.unileipzig.irpact.core.log.IRPLogging;
import de.unileipzig.irpact.core.process.ra.RAConstants;
import de.unileipzig.irpact.core.spatial.twodim.Metric2D;
import de.unileipzig.irpact.experimental.TestFiles;
import de.unileipzig.irpact.io.param.input.InAttributeName;
import de.unileipzig.irpact.io.param.input.InRoot;
import de.unileipzig.irpact.io.param.input.InVersion;
import de.unileipzig.irpact.io.param.input.affinity.InComplexAffinityEntry;
import de.unileipzig.irpact.io.param.input.agent.consumer.InConsumerAgentGroup;
import de.unileipzig.irpact.io.param.input.agent.consumer.InConsumerAgentGroupAttribute;
import de.unileipzig.irpact.io.param.input.binary.VisibleBinaryData;
import de.unileipzig.irpact.io.param.input.distribution.InConstantUnivariateDistribution;
import de.unileipzig.irpact.io.param.input.distribution.InUnivariateDoubleDistribution;
import de.unileipzig.irpact.io.param.input.file.InPVFile;
import de.unileipzig.irpact.io.param.input.file.InSpatialTableFile;
import de.unileipzig.irpact.io.param.input.interest.InProductThresholdInterestSupplyScheme;
import de.unileipzig.irpact.io.param.input.network.InGraphTopologyScheme;
import de.unileipzig.irpact.io.param.input.network.InUnlinkedGraphTopology;
import de.unileipzig.irpact.io.param.input.process.*;
import de.unileipzig.irpact.io.param.input.product.*;
import de.unileipzig.irpact.io.param.input.spatial.InSpace2D;
import de.unileipzig.irpact.io.param.input.spatial.InSpatialModel;
import de.unileipzig.irpact.io.param.input.spatial.dist.InCustomSpatialDistribution2D;
import de.unileipzig.irpact.io.param.input.spatial.dist.InSpatialDistribution;
import de.unileipzig.irpact.io.param.input.time.InDiscreteTimeModel;
import de.unileipzig.irpact.io.param.input.time.InTimeModel;
import de.unileipzig.irpact.io.spec.SpecificationConverter;
import de.unileipzig.irpact.io.spec.SpecificationManager;
import de.unileipzig.irpact.start.Start;
import de.unileipzig.irptools.defstructure.DefaultScenarioFactory;
import de.unileipzig.irptools.util.Util;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author Daniel Abitz
 */
@Disabled
public class Demo3 implements DefaultScenarioFactory {


    private static InConsumerAgentGroupAttribute build(
            String prefix,
            InAttributeName name,
            InUnivariateDoubleDistribution dist,
            List<InConsumerAgentGroupAttribute> out) {
        InConsumerAgentGroupAttribute attr = new InConsumerAgentGroupAttribute(prefix + "_" + name.getName(), name, dist);
        out.add(attr);
        return attr;
    }

    private static InRoot getRoot() {
        //attr
        InAttributeName A2 = new InAttributeName(RAConstants.NOVELTY_SEEKING);
        InAttributeName A3 = new InAttributeName(RAConstants.DEPENDENT_JUDGMENT_MAKING);
        InAttributeName A4 = new InAttributeName(RAConstants.ENVIRONMENTAL_CONCERN);
        InAttributeName A7 = new InAttributeName(RAConstants.CONSTRUCTION_RATE);
        InAttributeName A8 = new InAttributeName(RAConstants.RENOVATION_RATE);

        InAttributeName B6 = new InAttributeName(RAConstants.REWIRING_RATE);

        InAttributeName C1 = new InAttributeName(RAConstants.COMMUNICATION_FREQUENCY_SN);

        InAttributeName D1 = new InAttributeName(RAConstants.INITIAL_PRODUCT_INTEREST);
        InAttributeName D3 = new InAttributeName(RAConstants.FINANCIAL_THRESHOLD);
        InAttributeName D4 = new InAttributeName(RAConstants.ADOPTION_THRESHOLD);
        InAttributeName D5 = new InAttributeName(RAConstants.INITIAL_ADOPTER);

        InAttributeName E1 = new InAttributeName(RAConstants.INVESTMENT_COST);

        InAttributeName A1 = new InAttributeName(RAConstants.PURCHASE_POWER);
        InAttributeName A5 = new InAttributeName(RAConstants.SHARE_1_2_HOUSE);
        InAttributeName A6 = new InAttributeName(RAConstants.HOUSE_OWNER);

        //files
        InPVFile pvFile = new InPVFile("BarwertrechnerMini_ES");
        InSpatialTableFile tableFile = new InSpatialTableFile("210225_Datensatz");

        //dist
        InUnivariateDoubleDistribution diraq0 = new InConstantUnivariateDistribution("diraq0", 0);
        InUnivariateDoubleDistribution diraq03 = new InConstantUnivariateDistribution("diraq03", 0.3);
        InUnivariateDoubleDistribution diraq07 = new InConstantUnivariateDistribution("diraq07", 0.7);
        InUnivariateDoubleDistribution diraq1 = new InConstantUnivariateDistribution("diraq1", 1);

        //product
        InProductGroupAttribute pv_e1 = new InProductGroupAttribute(
                "PV_E1",
                E1,
                diraq1
        );
        InProductGroup pv = new InProductGroup("PV", new InProductGroupAttribute[]{pv_e1});
        InFixProductAttribute fix_pv_e1 = new InFixProductAttribute("PV_E1_fix", pv_e1, 1.0);
        InFixProduct fix_pv = new InFixProduct("PV_fix", pv, new InFixProductAttribute[]{fix_pv_e1});
        InFixProductFindingScheme fixScheme = new InFixProductFindingScheme("PV_fix_scheme", fix_pv);

        //spatial
        InCustomSpatialDistribution2D spaDist = new InCustomSpatialDistribution2D(
                "RandomDraw00",
                diraq0,
                diraq0,
                tableFile
        );

        //cags
        //A
        String name = "A";
        List<InConsumerAgentGroupAttribute> list = new ArrayList<>();
        build(name, A1, diraq1, list);      //ueberschreiben der spatial-datei
        build(name, A2, diraq1, list);
        build(name, A3, diraq1, list);
        build(name, A4, diraq1, list);
        build(name, A5, diraq1, list);      //ueberschreiben der spatial-datei
        build(name, A6, diraq0, list);      //ueberschreiben der spatial-datei
        build(name, A7, diraq0, list);
        build(name, A8, diraq0, list);

        build(name, B6, diraq0, list);

        build(name, C1, diraq0, list);

        build(name, D1, diraq1, list);
        build(name, D3, diraq03, list);
        build(name, D4, diraq07, list);
        build(name, D5, diraq0, list);

        InProductThresholdInterestSupplyScheme A_awa = new InProductThresholdInterestSupplyScheme(name + "_awa", diraq0);

        InConsumerAgentGroup A = new InConsumerAgentGroup(name, 1.0, 10, list, A_awa);
        A.productFindingSchemes = new InProductFindingScheme[]{fixScheme};
        A.spatialDistribution = new InSpatialDistribution[]{spaDist};

        //B
        name = "K";
        list.clear();
        build(name, A1, diraq1, list);      //ueberschreiben der spatial-datei
        build(name, A2, diraq1, list);
        build(name, A3, diraq1, list);
        build(name, A4, diraq1, list);
        build(name, A5, diraq1, list);      //ueberschreiben der spatial-datei
        build(name, A6, diraq0, list);      //ueberschreiben der spatial-datei
        build(name, A7, diraq0, list);
        build(name, A8, diraq0, list);

        build(name, B6, diraq0, list);

        build(name, C1, diraq0, list);

        build(name, D1, diraq0, list);      //!
        build(name, D3, diraq03, list);
        build(name, D4, diraq07, list);
        build(name, D5, diraq0, list);

        InProductThresholdInterestSupplyScheme B_awa = new InProductThresholdInterestSupplyScheme(name + "_awa", diraq0);

        InConsumerAgentGroup K = new InConsumerAgentGroup(name, 1.0, 10, list, B_awa);
        K.productFindingSchemes = new InProductFindingScheme[]{fixScheme};
        K.spatialDistribution = new InSpatialDistribution[]{spaDist};

        //affinity
        InComplexAffinityEntry A_A = new InComplexAffinityEntry(A.getName() + "_" + A.getName(), A, A, 0.0);
        InComplexAffinityEntry A_B = new InComplexAffinityEntry(A.getName() + "_" + K.getName(), A, K, 0.0);
        InComplexAffinityEntry B_B = new InComplexAffinityEntry(K.getName() + "_" + K.getName(), K, K, 0.0);
        InComplexAffinityEntry B_A = new InComplexAffinityEntry(K.getName() + "_" + A.getName(), K, A, 0.0);

        //process
        InCustomUncertaintyGroupAttribute uncert = new InCustomUncertaintyGroupAttribute();
        uncert.setName("RA_uncer");
        uncert.cags = new InConsumerAgentGroup[]{A, K};
        uncert.names = new InAttributeName[]{A2, A3, A4};
        uncert.setUncertaintyDistribution(diraq0);
        uncert.setConvergenceDistribution(diraq0);

        InRAProcessModel processModel = new InRAProcessModel(
                "RA",
                0.25, 0.25, 0.25, 0.25,
                3, 2, 1, 0,
                pvFile,
                new InSlopeSupplier[0],
                new InOrientationSupplier[0],
                new InUncertaintyGroupAttribute[]{uncert}
        );

        //=========================

        InRoot root = new InRoot();
        //general
        root.general.seed = 3;
        root.general.startYear = 2015;
        root.general.endYear = 2015;
        root.version = InVersion.currentVersionAsArray();
        root.general.setTimeout(5, TimeUnit.MINUTES);
        root.general.setLogLevel(IRPLevel.ALL);
        root.general.logAll = true;
        //affinity
        root.affinityEntries = new InComplexAffinityEntry[]{A_A, A_B, B_A, B_B};
        //agent
        root.consumerAgentGroups = new InConsumerAgentGroup[] {A, K};
        //network
        root.graphTopologySchemes = new InGraphTopologyScheme[]{new InUnlinkedGraphTopology("Unlinked")};
        //process
        root.processModel = new InProcessModel[] {processModel};
        //product
        root.productGroups = new InProductGroup[] {pv};
        root.fixProducts = new InFixProduct[] {fix_pv};
        //spatial
        root.spatialModel = new InSpatialModel[] {new InSpace2D("2D", Metric2D.EUCLIDEAN)};
        //time
        root.timeModel = new InTimeModel[] {new InDiscreteTimeModel("Diskret", TimeUnit.DAYS.toMillis(1))};
        //binary
        root.visibleBinaryData = new VisibleBinaryData[0];

        return root;
    }

    @Override
    public InRoot createDefaultScenario() {
        return getRoot();
    }

    private static final String demo = "Demo3_Entscheidungsprozess_Adoptionsentscheidung";

    @Test
    void store() throws IOException, ParsingException {
        IRPLogging.initConsole();
        Path out = TestFiles.testfiles.resolve("demos").resolve(demo);
        InRoot inRoot = createDefaultScenario();
        SpecificationConverter converter = new SpecificationConverter();
        SpecificationManager manager = converter.toSpec(inRoot);
        manager.store(out);
    }

    @Test
    void runAll() throws IOException, ParsingException {
        store();
        runToParam();
        runToSpec();
    }

    @Test
    void runAllWithTest() throws IOException, ParsingException {
        runAll();
        testContent();
    }

    @Test
    void runToParam() {
        String[] args = {
                "--specToParam", TestFiles.testfiles.resolve("demos").resolve(demo).toString(),
                "-o", TestFiles.testfiles.resolve("demos").resolve("params").resolve(demo + "_param.json").toString()
        };
        Start.main(args);
    }
    @Test
    void runToSpec() {
        String[] args = {
                "-i", TestFiles.testfiles.resolve("demos").resolve("params").resolve(demo + "_param.json").toString(),
                "--paramToSpec", TestFiles.testfiles.resolve("demos").resolve("test_" + demo).toString()
        };
        Start.main(args);
    }

    @Test
    void testContent() throws IOException {
        Path dir1 = TestFiles.testfiles.resolve("demos").resolve(demo);
        Path dir2 = TestFiles.testfiles.resolve("demos").resolve("test_" + demo);

        List<Path> paths1;
        try(Stream<Path> stream = Files.walk(dir1, Integer.MAX_VALUE)) {
            paths1 = stream.filter(Files::isRegularFile)
                    .collect(Collectors.toList());
        }

        List<Path> paths2;
        try(Stream<Path> stream = Files.walk(dir2, Integer.MAX_VALUE)) {
            paths2 = stream.filter(Files::isRegularFile)
                    .collect(Collectors.toList());
        }

        assertEquals(paths1.size(), paths2.size());
        for(int i = 0; i < paths1.size(); i++) {
            Path p1 = paths1.get(i);
            Path p2 = paths2.get(i);
            assertEquals(p1.getFileName(), p2.getFileName());

            String content1 = Util.readString(p1, StandardCharsets.UTF_8);
            String content2 = Util.readString(p2, StandardCharsets.UTF_8);

            System.out.println("check: " + p1 + " | " + p2);
            assertEquals(content1, content2);
        }
    }
}
