package de.unileipzig.irpact.experimental.dataDist;

import de.unileipzig.irpact.commons.util.CollectionUtil;
import de.unileipzig.irpact.commons.util.Rnd;
import de.unileipzig.irpact.core.spatial.SpatialInformation;
import de.unileipzig.irpact.core.spatial.SpatialTableFileLoader;
import de.unileipzig.irpact.core.spatial.SpatialUtil;
import de.unileipzig.irpact.core.spatial.attribute.SpatialAttribute;
import de.unileipzig.irpact.core.spatial.filter.MaxDistanceSpatialInformationFilter;
import de.unileipzig.irpact.core.spatial.filter.RangeSpatialInformationFilter;
import de.unileipzig.irpact.core.spatial.twodim.BasicPoint2D;
import de.unileipzig.irpact.core.spatial.twodim.Metric2D;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.function.Predicate;

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
    void testIt1() throws IOException {
        Path path = Paths.get("D:\\Prog\\JetBrains\\SUSICProjects\\IRPact\\src\\main\\resources\\irpacttempdata", "Datensatz_210225.xlsx");
        String x = "X_Zentroid";
        String y = "Y_Zentroid";
        List<List<SpatialAttribute<?>>> attrList = SpatialTableFileLoader.parseXlsx(path);
        List<SpatialInformation> infoList = SpatialUtil.mapToPoint2D(attrList, x, y);
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
        List<List<SpatialAttribute<?>>> attrList = SpatialTableFileLoader.parseXlsx(path);
        List<SpatialInformation> infoList = SpatialUtil.mapToPoint2D(attrList, x, y);

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
        List<List<SpatialAttribute<?>>> attrList = SpatialTableFileLoader.parseXlsx(path);
        List<SpatialInformation> infoList = SpatialUtil.mapToPoint2D(attrList, x, y);

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
