package de.unileipzig.irpact.commons.util;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Daniel Abitz
 */
class IRPactBase32Test {

    @Test
    void testEncodeDecodeWithUtf8() {
        String text = "äöü€@µ";
        String irpb32 = IRPactBase32.encodeUTF8ToString(text);
        assertEquals("XOEIC7DM3NJH85B20OAQGZZZZ", irpb32);
        String dec = IRPactBase32.decodeToUtf8(irpb32);
        assertEquals(text, dec);
    }
}