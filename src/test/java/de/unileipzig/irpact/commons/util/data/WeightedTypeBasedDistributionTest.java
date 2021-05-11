package de.unileipzig.irpact.commons.util.data;

import de.unileipzig.irpact.commons.util.CollectionUtil;
import de.unileipzig.irpact.commons.util.Rnd;
import de.unileipzig.irpact.commons.util.data.weighted.WeightedTypeBasedDistribution;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Daniel Abitz
 */
class WeightedTypeBasedDistributionTest {

    @Test
    void testWithMultiData() {
        List<String> dataList = CollectionUtil.arrayListOf("a0", "a1", "a2", "b0", "b1", "b2");
        DataCollection<String> data = new LinkedDataCollection<>();
        data.addAll(dataList);

        WeightedTypeBasedDistribution<String, String> wbrd = new WeightedTypeBasedDistribution<>();
        wbrd.setRemoveOnDraw(true);
        wbrd.setData(data);
        wbrd.setRandom(new Rnd(123));
        wbrd.set("a", s -> s.startsWith("a"));
        wbrd.set("b", s -> s.startsWith("b"));
        wbrd.set("0", s -> s.endsWith("0"));

        List<String> draws = new ArrayList<>();
        for(int i = 0; i < dataList.size(); i++) {
            draws.add(wbrd.drawValue());
        }
        assertTrue(dataList.containsAll(draws));
        assertTrue(wbrd.isEmpty());
        assertThrows(IllegalStateException.class, wbrd::drawValue);
    }

    @Disabled("performance test")
    @Test
    void testWithLargeData() {
        Set<String> dataList = new LinkedHashSet<>();
        for(int i = 0; i < 50000; i++) {
            dataList.add("a" + i);
            dataList.add("b" + i);
        }

        DataCollection<String> data = new LinkedDataCollection<>(LinkedHashSet::new);
        data.addAll(dataList);

        WeightedTypeBasedDistribution<String, String> wbrd = new WeightedTypeBasedDistribution<>();
        wbrd.setRemoveOnDraw(true);
        wbrd.setData(data);
        wbrd.setRandom(new Rnd(123));
        wbrd.set("a", s -> s.startsWith("a"));
        wbrd.set("b", s -> s.startsWith("b"));

        Set<String> draws = new HashSet<>();
        for(int i = 0; i < dataList.size(); i++) {
            draws.add(wbrd.drawValue());
        }

        assertEquals(dataList, draws);
        assertTrue(wbrd.isEmpty());
        assertThrows(IllegalStateException.class, wbrd::drawValue);
    }

    @Test
    void testDeterministic() {
        List<String> dataList = CollectionUtil.arrayListOf("a0", "a1", "a2", "b0", "b1", "b2");
        List<String> dataList1 = new ArrayList<>(dataList);
        List<String> dataList2 = new ArrayList<>(dataList);
        DataCollection<String> data1 = new LinkedDataCollection<>();
        data1.addAll(dataList1);
        DataCollection<String> data2 = new LinkedDataCollection<>();
        data2.addAll(dataList2);

        WeightedTypeBasedDistribution<String, String> rd1 = new WeightedTypeBasedDistribution<>();
        rd1.setRandom(new Rnd(123));
        rd1.setRemoveOnDraw(true);
        rd1.setData(data1);
        rd1.set("a", s -> s.startsWith("a"));
        rd1.set("b", s -> s.startsWith("b"));
        WeightedTypeBasedDistribution<String, String> rd2 = new WeightedTypeBasedDistribution<>();
        rd2.setRandom(new Rnd(123));
        rd2.setRemoveOnDraw(true);
        rd2.setData(data2);
        rd2.set("a", s -> s.startsWith("a"));
        rd2.set("b", s -> s.startsWith("b"));

        List<String> out1 = new ArrayList<>();
        List<String> out2 = new ArrayList<>();
        for(int i = 0; i < dataList.size(); i++) {
            out1.add(rd1.drawValue());
            out2.add(rd2.drawValue());
        }

        assertTrue(rd1.isEmpty());
        assertTrue(rd2.isEmpty());

        assertTrue(dataList.containsAll(out1));
        assertTrue(out1.containsAll(dataList));
        assertEquals(dataList.size(), out1.size());
        assertEquals(out1, out2);
    }

    @Test
    void tesUsed() {
        List<String> dataList = CollectionUtil.arrayListOf("a0", "a1", "a2", "b0", "b1", "b2");
        List<String> dataList1 = new ArrayList<>(dataList);
        List<String> dataList2 = new ArrayList<>(dataList);
        DataCollection<String> data1 = new LinkedDataCollection<>();
        data1.addAll(dataList1);
        DataCollection<String> data2 = new LinkedDataCollection<>();
        data2.addAll(dataList2);

        List<String> out1 = new ArrayList<>();
        List<String> out2 = new ArrayList<>();

        WeightedTypeBasedDistribution<String, String> rd1 = new WeightedTypeBasedDistribution<>();
        rd1.setRandom(new Rnd(123));
        rd1.setRemoveOnDraw(true);
        rd1.setData(data1);
        rd1.set("a", s -> s.startsWith("a"));
        rd1.set("b", s -> s.startsWith("b"));

        for(int i = 0; i < 3; i++) {
            out1.add(rd1.drawValue());
        }

        long newSeed = rd1.getRandom().reseed();

        WeightedTypeBasedDistribution<String, String> rd2 = new WeightedTypeBasedDistribution<>();
        rd2.setRandom(new Rnd(newSeed));
        rd2.setRemoveOnDraw(true);
        rd2.setData(data2);
        rd2.set("a", s -> s.startsWith("a"));
        rd2.set("b", s -> s.startsWith("b"));
        rd2.usedAll(out1);

        List<String> all = new ArrayList<>(out1);
        out1.clear();

        for(int i = 0; i < 3; i++) {
            out1.add(rd1.drawValue());
            out2.add(rd2.drawValue());
        }

        assertTrue(rd1.isEmpty());
        assertTrue(rd2.isEmpty());

        assertEquals(out1, out2);

        all.addAll(out1);
        assertTrue(dataList.containsAll(all));
        assertTrue(all.containsAll(dataList));
    }
}