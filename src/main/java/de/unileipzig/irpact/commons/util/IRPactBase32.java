package de.unileipzig.irpact.commons.util;

import org.apache.commons.codec.binary.Base32;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

/**
 * Modification of Base32 for IRPact.
 * The implementation uses RFC 4648 Base32hex and exchanges = with Z. In addition all encoded strings start with X to
 * avoid leading numbers.
 *
 * @author Daniel Abitz
 */
public final class IRPactBase32 {

    private static final byte FILL_IRP32 = (byte) 'Z';
    public static final Base32 BASE32 = new Base32(true, FILL_IRP32);

    public static final String IRP32_PREFIX = "x";

    private IRPactBase32() {
    }

    public static String encodeToString(String input, Charset inCharset) {
        return encodeToString(null, input, inCharset);
    }

    public static String encodeToString(byte[] input) {
        return encodeToString(null, input);
    }

    public static String encodeToString(String prefix, String input, Charset inCharset) {
        byte[] data = input.getBytes(inCharset);
        return encodeToString(prefix, data);
    }

    public static String encodeToString(String prefix, byte[] input) {
        byte[] encoded = encode(input);
        String irp32 = new String(encoded, StandardCharsets.UTF_8);
        return prefix == null || prefix.isEmpty()
                ? irp32
                : prefix + irp32;
    }

    public static byte[] encode(byte[] input) {
        return BASE32.encode(input);
    }

    public static String decodeToString(String irp32, Charset outCharset) {
        byte[] data = irp32.getBytes(StandardCharsets.UTF_8);
        return decodeToString(data, outCharset);
    }

    public static String decodeToString(byte[] irp32, Charset outCharset) {
        byte[] decoded = decode(irp32);
        return new String(decoded, outCharset);
    }

    public static String decodeToString(String prefix, String irp32, Charset outCharset) {
        if(prefix == null || prefix.isEmpty()) {
            return decodeToString(irp32, outCharset);
        } else {
            String sub = irp32.substring(prefix.length());
            return decodeToString(sub, outCharset);
        }
    }

    public static byte[] decode(String irp32) {
        byte[] data = irp32.getBytes(StandardCharsets.UTF_8);
        return decode(data);
    }

    public static byte[] decode(byte[] irp32) {
        return BASE32.decode(irp32);
    }
}
