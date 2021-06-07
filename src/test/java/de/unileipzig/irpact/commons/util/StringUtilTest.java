package de.unileipzig.irpact.commons.util;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Daniel Abitz
 */
class StringUtilTest {

    @Test
    void testBlankEmpty() {
        assertTrue(StringUtil.isBlank(""));
    }

    @Test
    void testBlankWhitespace() {
        assertTrue(StringUtil.isBlank(" "));
    }

    @Test
    void testBlankMultiWhitespace() {
        assertTrue(StringUtil.isBlank("  "));
        assertTrue(StringUtil.isBlank("   "));
        assertTrue(StringUtil.isBlank("    "));
    }

    @Test
    void testBlankTab() {
        assertTrue(StringUtil.isBlank("\t"));
    }

    @Test
    void testBlankMultiTab() {
        assertTrue(StringUtil.isBlank("\t\t"));
        assertTrue(StringUtil.isBlank("\t\t\t"));
        assertTrue(StringUtil.isBlank("\t\t\t\t"));
    }

    @Test
    void testBlankWhitespaceTab() {
        assertTrue(StringUtil.isBlank(" \t\t "));
        assertTrue(StringUtil.isBlank("\t \t \t "));
        assertTrue(StringUtil.isBlank(" \t  \t \t \t "));
    }

    @Test
    void testNonBlankWhitespaceTab() {
        assertFalse(StringUtil.isBlank(" \t\t x"));
        assertFalse(StringUtil.isBlank("\t \ta \t "));
        assertFalse(StringUtil.isBlank("1 \t  \t \t \t "));
    }
}