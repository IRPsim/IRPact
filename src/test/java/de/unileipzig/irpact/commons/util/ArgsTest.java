package de.unileipzig.irpact.commons.util;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Daniel Abitz
 */
class ArgsTest {

    @Test
    void testSetAll() {
        String[] argArr = {"--a", "x", "-hallo", "\"welt !\"", "--b", "--c", "abc", "xyz"};
        Args args = new Args();
        args.parse(argArr);
        assertEquals("x", args.get("--a"));
        assertEquals("welt !", args.get("-hallo"));
        assertNull(args.get("--b"));
        assertEquals(CollectionUtil.arrayListOf("abc", "xyz"), args.getAll("--c"));
        assertArrayEquals(argArr, args.toArray());
    }

    @Test
    void testSet() {
        Args args = new Args()
                .set("--a", "x")
                .set("-hallo", "welt !")
                .set("--b")
                .setAll("--c", "abc", "xyz");

        String[] argArr = {"--a", "x", "-hallo", "\"welt !\"", "--b", "--c", "abc", "xyz"};
        assertArrayEquals(argArr, args.toArray());
    }
}