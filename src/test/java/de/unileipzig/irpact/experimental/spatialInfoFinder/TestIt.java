package de.unileipzig.irpact.experimental.spatialInfoFinder;

import de.unileipzig.irpact.commons.attribute.Attribute;
import de.unileipzig.irpact.commons.exception.ParsingException;
import de.unileipzig.irpact.commons.util.CollectionUtil;
import de.unileipzig.irpact.commons.util.Rnd;
import de.unileipzig.irpact.commons.util.csv.CsvParser;
import de.unileipzig.irpact.commons.util.table.SimpleTable;
import de.unileipzig.irpact.commons.util.table.Table;
import de.unileipzig.irpact.commons.util.xlsx.XlsxToCsvConverterOLD;
import de.unileipzig.irpact.core.log.IRPLogging;
import de.unileipzig.irpact.core.process.ra.RAConstants;
import de.unileipzig.irpact.core.spatial.SpatialInformation;
import de.unileipzig.irpact.core.spatial.SpatialTableFileLoader;
import de.unileipzig.irpact.core.spatial.SpatialUtil;
import de.unileipzig.irpact.commons.spatial.attribute.SpatialAttribute;
import de.unileipzig.irpact.core.spatial.filter.MaxDistanceSpatialInformationFilter;
import de.unileipzig.irpact.core.spatial.filter.RangeSpatialInformationFilter;
import de.unileipzig.irpact.core.spatial.twodim.BasicPoint2D;
import de.unileipzig.irpact.core.spatial.twodim.Metric2D;
import de.unileipzig.irpact.io.param.input.agent.population.InRelativeExternConsumerAgentPopulationSize;
import de.unileipzig.irpact.util.PVactUtil;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * @author Daniel Abitz
 */
@Disabled
public class TestIt {

    private static List<SpatialInformation> findK(
            List<SpatialInformation> infoList,
            Predicate<SpatialInformation> filter,
            int k) {
        List<SpatialInformation> out = new ArrayList<>();
        for(SpatialInformation info: infoList) {
            if(out.size() == k) {
                break;
            }
            if(filter.test(info)) {
                out.add(info);
            }
        }
        return out;
    }

    @Test
    void testMileuStuff() throws IOException {
        Path dir = Paths.get("D:\\Prog\\JetBrains\\SUSICProjects\\IRPact\\testfiles\\0data");
        Path csv = dir.resolve("Datensatz_210322.csv");
        CsvParser<SpatialAttribute> parser = new CsvParser<>();
        parser.setConverter(PVactUtil.CSV_CONVERTER);
        parser.setNumberOfInfoRows(1);
        parser.parse(csv);
        List<List<SpatialAttribute>> attrs = parser.getRows();

        Set<String> milies = SpatialUtil.collectDistinct(attrs, RAConstants.DOM_MILIEU);
        System.out.println(milies);

        Map<String, Integer> count = SpatialUtil.filterAndCountAll(attrs, RAConstants.DOM_MILIEU, null);
        Map<String, Integer> countSort = new TreeMap<>(count);
        double total = countSort.values().stream().mapToInt(i -> i).sum();
        System.out.println(total);
        countSort.forEach((s, n) -> {
            System.out.println(s + " " + n + " " + (n.doubleValue() / total));
        });
    }

    /*
        BUM 9090 0.18893415364150315
        EPE 3112 0.06468240771533089
        G   381  0.007919022281343531
        HED 4591 0.09542317924842035
        KET 3747 0.07788077818423678
        LIB 7350 0.15276854007316262
        PER 3055 0.06349767209843699
        PRA 2999 0.06233372131692717
        PRE 5567 0.11570917858330562
        SOK 4824 0.10026604589291653
        TRA 3396 0.07058530096441636
     */

    @Test
    void testMileuSelect() throws IOException, ParsingException {
        Path dir = Paths.get("D:\\Prog\\JetBrains\\SUSICProjects\\IRPact\\testfiles\\0data");
        Path csv = dir.resolve("Datensatz_210322.csv");
        CsvParser<SpatialAttribute> parser = new CsvParser<>();
        parser.setConverter(PVactUtil.CSV_CONVERTER);
        parser.setNumberOfInfoRows(1);
        parser.parse(csv);
        List<List<SpatialAttribute>> attrs = parser.getRows();

        //"PRA", "PER", "SOK", "BUM", "PRE", "EPE", "TRA", "KET", "LIB", "HED", "G"
        List<String> milieus = CollectionUtil.arrayListOf("BUM", "EPE", "G");

        int maximumSize = 100000;
        boolean allowSmallerSize = true;
        boolean useMaximumPossibleSize = false;

        Map<String, Integer> share = InRelativeExternConsumerAgentPopulationSize.calculateShares(
                milieus,
                RAConstants.DOM_MILIEU,
                attrs,
                maximumSize,
                allowSmallerSize,
                useMaximumPossibleSize,
                s -> s,
                true
        );
        Map<String, Integer> countShare = new TreeMap<>(share);

        double sum = countShare.values().stream().mapToInt(i -> i).sum();
        System.out.println(sum);
        countShare.forEach((s, i) -> System.out.println(s + " " + i + " " + (i/sum)));
    }

    /*
        1000.0 false false
        BUM 723 0.723
        EPE 247 0.247
        G 30 0.03

        12583.0 false true
        BUM 9090 0.7224032424699992
        EPE 3112 0.24731780974330445
        G 381 0.

        100000 false true -> ERROR, da zu groß
        BUM 9090 0.7224032424699992
        EPE 3112 0.24731780974330445
        G 381 0.030278947786696337

        12583.0 true true ->
        BUM 9090 0.7224032424699992
        EPE 3112 0.24731780974330445
        G 381 0.030278947786696337

     */

    @Test
    void testMileuSelect2() throws IOException, ParsingException {
        IRPLogging.initConsole();
        Path dir = Paths.get("D:\\Prog\\JetBrains\\SUSICProjects\\IRPact\\testfiles\\0data");
        Path csv = dir.resolve("Datensatz_210322.csv");
        CsvParser<SpatialAttribute> parser = new CsvParser<>();
        parser.setConverter(PVactUtil.CSV_CONVERTER);
        parser.setNumberOfInfoRows(1);
        parser.parse(csv);
        List<List<SpatialAttribute>> attrs = parser.getRows();

        //"PRA", "PER", "SOK", "BUM", "PRE", "EPE", "TRA", "KET", "LIB", "HED", "G"
        List<String> milieus = CollectionUtil.arrayListOf("PRA", "PER", "G");

        Map<String, Integer> share = InRelativeExternConsumerAgentPopulationSize.calculateShares(
                milieus,
                RAConstants.DOM_MILIEU,
                attrs,
                100,
                false,
                false,
                s -> s,
                true
        );
        Map<String, Integer> countShare = new TreeMap<>(share);

        double sum = countShare.values().stream().mapToInt(i -> i).sum();
        System.out.println(sum);
        countShare.forEach((s, i) -> System.out.println(s + " " + i + " " + (i/sum)));
    }

    @Test
    void testMileuSelect3() throws IOException, ParsingException {
        IRPLogging.initConsole();
        Path dir = Paths.get("D:\\Prog\\JetBrains\\SUSICProjects\\IRPact\\testfiles\\0data");
        Path csv = dir.resolve("Datensatz_210322.csv");
        CsvParser<SpatialAttribute> parser = new CsvParser<>();
        parser.setConverter(PVactUtil.CSV_CONVERTER);
        parser.setNumberOfInfoRows(1);
        parser.parse(csv);
        SimpleTable<SpatialAttribute> table = new SimpleTable<>();
        table.set(parser.getHeader(), parser.getRows());

        List<Object> objs = table.streamColumn(RAConstants.DOM_MILIEU)
                .map(Attribute::getValue)
                .distinct()
                .collect(Collectors.toList());

        System.out.println(objs);
    }

    @Test
    void parseCsv() throws IOException {
        Path dir = Paths.get("D:\\Prog\\JetBrains\\SUSICProjects\\IRPact\\testfiles\\0data");
        Path csv = dir.resolve("Datensatz_210322.csv");

        CsvParser<SpatialAttribute> parser = new CsvParser<>();
        parser.setConverter(PVactUtil.CSV_CONVERTER);
        parser.setNumberOfInfoRows(1);
        parser.parse(csv);

        List<List<SpatialAttribute>> infos = parser.getRows();
        System.out.println(infos.size());
        System.out.println(infos.get(0));
    }

    @Test
    void testConvertStuff() throws IOException {
        Path dir = Paths.get("D:\\Prog\\JetBrains\\SUSICProjects\\IRPact\\testfiles\\0data");
        Path xlsx = dir.resolve("Datensatz_210322.xlsx");
        Path csv = dir.resolve("Datensatz_210322.csv");

        XlsxToCsvConverterOLD converter = new XlsxToCsvConverterOLD();
        converter.setNumberOfInfoRows(1);
        converter.convert(xlsx, csv);
    }

    @Test
    void milieuGetter() throws IOException {
        Path path = Paths.get("D:\\Prog\\JetBrains\\SUSICProjects\\IRPact\\src\\main\\resources\\irpacttempdata", "Datensatz_210322.xlsx");
        String x = "X_Zentroid";
        String y = "Y_Zentroid";
        Table<SpatialAttribute> attrList = SpatialTableFileLoader.parseXlsx(path);
        List<SpatialInformation> infoList = SpatialUtil.mapToPoint2D(attrList.listTable(), x, y);
        System.out.println(infoList.size());

        Set<String> milieus = infoList.stream()
                .map(info -> info.getAttribute(RAConstants.DOM_MILIEU).getStringValue())
                .map(str -> "\"" + str + "\"")
                .collect(Collectors.toSet());

        System.out.println(milieus);

    }

    @Test
    void testIt1() throws IOException {
        Path path = Paths.get("D:\\Prog\\JetBrains\\SUSICProjects\\IRPact\\src\\main\\resources\\irpacttempdata", "Datensatz_210225.xlsx");
        String x = "X_Zentroid";
        String y = "Y_Zentroid";
        Table<SpatialAttribute> attrList = SpatialTableFileLoader.parseXlsx(path);
        List<SpatialInformation> infoList = SpatialUtil.mapToPoint2D(attrList.listTable(), x, y);
        System.out.println(infoList.size());

        BasicPoint2D p = new BasicPoint2D(12.505449, 51.346420780942);
        RangeSpatialInformationFilter filter = new RangeSpatialInformationFilter();
        filter.setReference(p);
        filter.setMetric(Metric2D.HAVERSINE_KM);
        filter.setMin(1);
        filter.setMax(1.1);

        List<SpatialInformation> filtered = CollectionUtil.filterToList(infoList, filter);
        System.out.println(filtered.size());
    }

    @Test
    void testIt2() throws IOException {
        Path path = Paths.get("D:\\Prog\\JetBrains\\SUSICProjects\\IRPact\\src\\main\\resources\\irpacttempdata", "Datensatz_210225.xlsx");
        String x = "X_Zentroid";
        String y = "Y_Zentroid";
        Table<SpatialAttribute> attrList = SpatialTableFileLoader.parseXlsx(path);
        List<SpatialInformation> infoList = SpatialUtil.mapToPoint2D(attrList.listTable(), x, y);

        Rnd rnd = new Rnd(123);
        int k = 5;
        MaxDistanceSpatialInformationFilter max02 = new MaxDistanceSpatialInformationFilter();
        max02.setMetric(Metric2D.HAVERSINE_KM);
        max02.setInclusive(true);
        max02.setMax(0.2);

        List<SpatialInformation> l1;
        List<SpatialInformation> l2;

        int run = 0;
        while(true) {
            System.out.println("run: " + (run++));

            SpatialInformation si1 = CollectionUtil.getRandom(infoList, rnd);
            max02.setReference(si1);
            l1 = findK(infoList, max02, k);

            if(l1.size() != k) {
                continue;
            }

            SpatialInformation si2 = CollectionUtil.getRandom(infoList, rnd);
            if(Metric2D.HAVERSINE_KM.distance(si1, si2) < 1.0) {
                continue;
            }
            max02.setReference(si2);
            l2 = findK(infoList, max02, k);
            if(l2.size() == k) {
                break;
            }
        }

        System.out.println(l1);
        System.out.println(l2);

        for(SpatialInformation sp1: l1) {
            for(SpatialInformation sp2: l2) {
                double dist = Metric2D.HAVERSINE_KM.distance(sp1, sp2);
                System.out.println(sp1 + " <-> " + sp2 + " : " + dist);
            }
        }
    }

    @Test
    void testIt3() throws IOException {
        Path path = Paths.get("D:\\Prog\\JetBrains\\SUSICProjects\\IRPact\\src\\main\\resources\\irpacttempdata", "Datensatz_210225.xlsx");
        String x = "X_Zentroid";
        String y = "Y_Zentroid";
        Table<SpatialAttribute> attrList = SpatialTableFileLoader.parseXlsx(path);
        List<SpatialInformation> infoList = SpatialUtil.mapToPoint2D(attrList.listTable(), x, y);

        Rnd rnd = new Rnd(123);
        int k = 5;
        MaxDistanceSpatialInformationFilter max02 = new MaxDistanceSpatialInformationFilter();
        max02.setMetric(Metric2D.HAVERSINE_KM);
        max02.setInclusive(true);
        max02.setMax(0.2);

        List<SpatialInformation> l1;
        List<SpatialInformation> l2;
        List<SpatialInformation> l3;

        int run = 0;
        while(true) {
            System.out.println("run: " + (run++));

            SpatialInformation si1 = CollectionUtil.getRandom(infoList, rnd);
            max02.setReference(si1);
            l1 = findK(infoList, max02, k);

            if(l1.size() != k) {
                continue;
            }

            SpatialInformation si2 = CollectionUtil.getRandom(infoList, rnd);
            if(Metric2D.HAVERSINE_KM.distance(si1, si2) < 1.0) {
                continue;
            }
            max02.setReference(si2);
            l2 = findK(infoList, max02, k);
            if(l2.size() != k) {
                continue;
            }

            SpatialInformation si3 = CollectionUtil.getRandom(infoList, rnd);
            if(Metric2D.HAVERSINE_KM.distance(si1, si3) < 1.0) {
                continue;
            }
            if(Metric2D.HAVERSINE_KM.distance(si2, si3) < 1.0) {
                continue;
            }
            max02.setReference(si3);
            l3 = findK(infoList, max02, k);
            if(l3.size() == k) {
                break;
            }
        }

        System.out.println(l1);
        System.out.println(l2);
        System.out.println(l3);

//        for(SpatialInformation sp1: l1) {
//            for(SpatialInformation sp2: l2) {
//                double dist = Metric2D.HAVERSINE_KM.distance(sp1, sp2);
//                System.out.println(sp1 + " <-> " + sp2 + " : " + dist);
//            }
//        }
    }
}
