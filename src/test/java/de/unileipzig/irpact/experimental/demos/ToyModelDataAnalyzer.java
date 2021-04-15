package de.unileipzig.irpact.experimental.demos;

import de.unileipzig.irpact.commons.util.data.MutableDouble;
import de.unileipzig.irpact.core.process.ra.RAConstants;
import de.unileipzig.irpact.core.spatial.SpatialInformation;
import de.unileipzig.irpact.core.spatial.SpatialUtil;
import de.unileipzig.irpact.core.spatial.twodim.Metric2D;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * @author Daniel Abitz
 */
@Disabled
public class ToyModelDataAnalyzer {

    @SuppressWarnings("SameParameterValue")
    private static void calcDistances(Collection<SpatialInformation> coll, Metric2D metric) {
        for(SpatialInformation from: coll) {
            String fromId = from.getAttribute(RAConstants.ID).getValueAsString();
            for(SpatialInformation to: coll) {
                String toId = to.getAttribute(RAConstants.ID).getValueAsString();
                double dist = metric.distance(from, to);
                System.out.println(fromId + " -> " + toId + " = " + dist);
            }
        }
    }

    @SuppressWarnings("SameParameterValue")
    private static void calcDistances(SpatialInformation from, Collection<SpatialInformation> coll, Metric2D metric) {
        String fromId = from.getAttribute(RAConstants.ID).getValueAsString();
        for(SpatialInformation to: coll) {
            String toId = to.getAttribute(RAConstants.ID).getValueAsString();
            double dist = metric.distance(from, to);
            System.out.println(fromId + " -> " + toId + " = " + dist);
        }
    }

    @SuppressWarnings("SameParameterValue")
    private static void calcMinMaxDistances(
            MutableDouble min, MutableDouble max,
            SpatialInformation from,
            Collection<SpatialInformation> coll,
            Metric2D metric) {
        for(SpatialInformation to: coll) {
            double dist = metric.distance(from, to);
            min.setMin(dist);
            max.setMax(dist);
        }
    }

    private static void calcMinMaxDistances(
            MutableDouble min, MutableDouble max,
            Collection<SpatialInformation> coll,
            boolean skipSelfCheck,
            Metric2D metric) {
        for(SpatialInformation from: coll) {
            for(SpatialInformation to: coll) {
                if(skipSelfCheck && from == to) {
                    continue;
                }

                double dist = metric.distance(from, to);
                min.setMin(dist);
                max.setMax(dist);
            }
        }
    }

    //=========================
    //Testdata
    //=========================

    @Test
    void testData5_1() throws IOException {
        List<SpatialInformation> infos = ToyModelUtil.getInformations("testdaten_Test_5_1.xlsx");
        Map<String, List<SpatialInformation>> milieus = SpatialUtil.groupingBy(infos, RAConstants.DOM_MILIEU);
        milieus.forEach((m, list) -> System.out.println(m + " " + list));

        MutableDouble s_a_min = MutableDouble.maxValue();
        MutableDouble s_a_max = MutableDouble.minValue();
        for(SpatialInformation S: milieus.get("S")) {
            calcMinMaxDistances(s_a_min, s_a_max, S, milieus.get("A"), Metric2D.HAVERSINE_KM);
        }
        System.out.println(s_a_min);
        System.out.println(s_a_max);
        System.out.println();

        MutableDouble s_k_min = MutableDouble.maxValue();
        MutableDouble s_k_max = MutableDouble.minValue();
        for(SpatialInformation S: milieus.get("S")) {
            calcMinMaxDistances(s_k_min, s_k_max, S, milieus.get("K"), Metric2D.HAVERSINE_KM);
        }
        System.out.println(s_k_min);
        System.out.println(s_k_max);
        System.out.println();

        MutableDouble a_k_min = MutableDouble.maxValue();
        MutableDouble a_k_max = MutableDouble.minValue();
        for(SpatialInformation A: milieus.get("A")) {
            calcMinMaxDistances(a_k_min, a_k_max, A, milieus.get("K"), Metric2D.HAVERSINE_KM);
        }
        System.out.println(a_k_min);
        System.out.println(a_k_max);

        /*
            S-A
            0.02734270844979025
            1.13713663610765

            -> max distance 2km

            S-K
            15.517940691862615
            16.141826227151622

            A-K
            15.652090033151183
            17.115125738873576
         */
    }

    @Test
    void testData5_2() throws IOException {
        List<SpatialInformation> infos = ToyModelUtil.getInformations("testdaten_Test_5_2.xlsx");
        Map<String, List<SpatialInformation>> milieus = SpatialUtil.groupingBy(infos, RAConstants.DOM_MILIEU);
        milieus.forEach((m, list) -> System.out.println(m + " " + list));

        MutableDouble min = MutableDouble.maxValue();
        MutableDouble max = MutableDouble.minValue();
        calcMinMaxDistances(min, max, infos, true, Metric2D.HAVERSINE_KM);
        System.out.println(min);
        System.out.println(max);

        /*
            min: 69.45274097361487
            max: 970.9013071917849

            -> max distance 50km
         */
    }

    @Test
    void testData5_3() throws IOException {
        List<SpatialInformation> infos = ToyModelUtil.getInformations("testdaten_Test_5_3.xlsx");
        Map<String, List<SpatialInformation>> milieus = SpatialUtil.groupingBy(infos, RAConstants.DOM_MILIEU);
        milieus.forEach((m, list) -> System.out.println(m + " " + list));

        MutableDouble min = MutableDouble.maxValue();
        MutableDouble max = MutableDouble.minValue();
        calcMinMaxDistances(min, max, infos, true, Metric2D.HAVERSINE_KM);
        System.out.println(min);
        System.out.println(max);

        /*
           min: 9.185156356622917E-4
           max: 1.13713663610765

            -> max distance 2km -> whole-network
         */
    }

    @Test
    void testData5_4() throws IOException {
        List<SpatialInformation> infos = ToyModelUtil.getInformations("testdaten_Test_5_4.xlsx");
        Map<String, List<SpatialInformation>> milieus = SpatialUtil.groupingBy(infos, RAConstants.DOM_MILIEU);
        milieus.forEach((m, list) -> System.out.println(m + " " + list));

        MutableDouble s_a_min = MutableDouble.maxValue();
        MutableDouble s_a_max = MutableDouble.minValue();
        for(SpatialInformation S: milieus.get("S")) {
            calcMinMaxDistances(s_a_min, s_a_max, S, milieus.get("A"), Metric2D.HAVERSINE_KM);
        }
        System.out.println(s_a_min);
        System.out.println(s_a_max);
        System.out.println();

        MutableDouble s_k_min = MutableDouble.maxValue();
        MutableDouble s_k_max = MutableDouble.minValue();
        for(SpatialInformation S: milieus.get("S")) {
            calcMinMaxDistances(s_k_min, s_k_max, S, milieus.get("K"), Metric2D.HAVERSINE_KM);
        }
        System.out.println(s_k_min);
        System.out.println(s_k_max);
        System.out.println();

        MutableDouble a_k_min = MutableDouble.maxValue();
        MutableDouble a_k_max = MutableDouble.minValue();
        for(SpatialInformation A: milieus.get("A")) {
            calcMinMaxDistances(a_k_min, a_k_max, A, milieus.get("K"), Metric2D.HAVERSINE_KM);
        }
        System.out.println(a_k_min);
        System.out.println(a_k_max);

        /*
            S-A
            0.02734270844979025
            17.05347591569

            -> max distance 2km

            S-K
            0.2373636655214312
            16.156376972576577

            A-K
            16.057073868041126
            17.08491902638462
         */
    }
}
