package de.unileipzig.irpact.experimental;

import de.unileipzig.irpact.commons.util.CollectionUtil;
import de.unileipzig.irpact.commons.util.StringUtil;
import de.unileipzig.irpact.commons.util.data.Grouping1;
import de.unileipzig.irpact.commons.util.data.Grouping2;
import de.unileipzig.irpact.commons.util.data.GroupingUtil;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.function.Function;

/**
 * @author Daniel Abitz
 */
@Disabled
class GroupingFun {

    @SuppressWarnings("FieldMayBeFinal")
    private static final class TestData {

        private String x;
        private String y;
        private String z;
        private double a;
        private double b;

        private TestData(String x, String y, String z, double a, double b) {
            this.x = x;
            this.y = y;
            this.z = z;
            this.a = a;
            this.b = b;
        }

        public String getX() {
            return x;
        }

        public String getY() {
            return y;
        }

        public String getZ() {
            return z;
        }

        public double getA() {
            return a;
        }

        public double getB() {
            return b;
        }
    }

    private static final Function<? super TestData, ? extends String> xSelector = TestData::getX;
    private static final Function<? super TestData, ? extends String> ySelector = TestData::getY;
    private static final Function<? super TestData, ? extends String> zSelector = TestData::getZ;

    private static final Function<? super List<TestData>, ? extends String> sumA = list -> {
        double sum = list.stream()
                .mapToDouble(TestData::getA)
                .sum();
        return StringUtil.printDoubleWithComma(sum);
    };

    private static final Function<? super List<TestData>, ? extends String> minusSumB = list -> {
        double sum = list.stream()
                .mapToDouble(TestData::getB)
                .sum();
        return StringUtil.printDoubleWithComma(sum * -1);
    };

    @Test
    void funWithGrouping2() {
        List<String> xKeys = CollectionUtil.arrayListOf("a", "b", "c");
        List<String> yKeys = CollectionUtil.arrayListOf("x", "y");

        Grouping2<String, String, TestData> g = new Grouping2<>(xSelector, ySelector);
        g.add(new TestData("a", "x", "1", 1.1, 1));
        g.add(new TestData("a", "x", "2", 2.1, 3));
        g.add(new TestData("a", "y", "3", 10.1, 5));
        g.add(new TestData("b", "y", "4", 20.1, 7));
        g.add(new TestData("b", "x", "5", 3.1, 9));
        g.add(new TestData("b", "x", "6", 4.1, 11));
        g.add(new TestData("c", "y", "7", 30.1, 13));
        g.add(new TestData("c", "y", "8", 40.1, 15));
        g.add(new TestData("c", "x", "9", 5.1, 17));

        String[][] rawData = GroupingUtil.toRawTable(
                g.getGrouping(),
                xKeys,
                yKeys,
                sumA
        );
        String text = GroupingUtil.print(
                xKeys,
                yKeys,
                rawData,
                ";"
        );
        System.out.println(text);

        System.out.println();

        String[][] rawDataT = GroupingUtil.transpose(rawData);
        String textT = GroupingUtil.print(
                yKeys,
                xKeys,
                rawDataT,
                ";"
        );
        System.out.println(textT);
    }

    @Test
    void funWithGrouping1() {
        List<String> zKeys = CollectionUtil.arrayListOf("1", "2", "3");

        Grouping1<String, TestData> g = new Grouping1<>(zSelector);
        g.add(new TestData("a", "x", "1", 1.1, 1));
        g.add(new TestData("a", "x", "2", 2.1, 3));
        g.add(new TestData("a", "y", "1", 10.1, 5));
        g.add(new TestData("b", "y", "2", 20.1, 7));
        g.add(new TestData("b", "x", "1", 3.1, 9));
        g.add(new TestData("b", "x", "2", 4.1, 11));
        g.add(new TestData("c", "y", "1", 30.1, 13));
        g.add(new TestData("c", "y", "2", 40.1, 15));
        g.add(new TestData("c", "x", "3", 5.1, 17));

        String[][] rawData = GroupingUtil.toRawTable(
                g.getGrouping(),
                zKeys,
                minusSumB
        );
        String text = GroupingUtil.print(
                zKeys,
                rawData,
                ";"
        );
        System.out.println(text);

        System.out.println();

        String[][] rawDataT = GroupingUtil.transpose(rawData);
        String textT = GroupingUtil.print(
                zKeys,
                rawDataT,
                ";"
        );
        System.out.println(textT);
    }
}