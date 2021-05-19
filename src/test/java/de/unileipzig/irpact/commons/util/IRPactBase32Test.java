package de.unileipzig.irpact.commons.util;

import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author Daniel Abitz
 */
class IRPactBase32Test {

    @Test
    void testEncodeDecodeWithPrefix() {
        String text = "äöü€@µ";
        String irpb32 = IRPactBase32.encodeToString(IRPactBase32.IRP32_PREFIX, text, StandardCharsets.UTF_8);
        assertEquals("xOEIC7DM3NJH85B20OAQGZZZZ", irpb32);
        String dec = IRPactBase32.decodeToString(IRPactBase32.IRP32_PREFIX, irpb32, StandardCharsets.UTF_8);
        assertEquals(text, dec);
    }

    @Test
    void testEncodeDecodeWithoutPrefix() {
        String text = "äöü€@µ";
        String irpb32 = IRPactBase32.encodeToString(text, StandardCharsets.UTF_8);
        assertEquals("OEIC7DM3NJH85B20OAQGZZZZ", irpb32);
        String dec = IRPactBase32.decodeToString(irpb32, StandardCharsets.UTF_8);
        assertEquals(text, dec);
    }

    @Test
    void testEncodeDecode() {
        byte[] arr = new byte[100];
        Random rnd = new Random(42);
        rnd.nextBytes(arr);
        byte[] copy = arr.clone();

        byte[] encoded = IRPactBase32.encode(arr);
        assertArrayEquals(copy, arr);
        byte[] decoded = IRPactBase32.decode(encoded);
        assertArrayEquals(arr, decoded);
    }
}