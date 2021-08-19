package de.unileipzig.irpact.commons.util;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Daniel Abitz
 */
class CollectionUtilTest {

    @Test
    void testDrawRandom() {
        Rnd rnd = new Rnd(42);
        List<Integer> list = Arrays.asList(0, 1, 2, 3, 4, 5, 6);
        List<Integer> rndDraw = CollectionUtil.drawRandom(list, 6, rnd);
        assertEquals(6, rndDraw.size());
        List<Integer> listCopy = new ArrayList<>(list);
        listCopy.removeAll(rndDraw);
        assertEquals(Collections.singletonList(3), listCopy);
    }
}