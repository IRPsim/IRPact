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

    private static final Base32 BASE32 = new Base32(true);
    private static final String PREFIX = "X";
    private static final byte FILL_OLD = (byte) '=';
    private static final byte FILL_NEW = (byte) 'Z';

    private IRPactBase32() {
    }

    public static String utf8ToBase32(String text) {
        return toBase32(text, StandardCharsets.UTF_8);
    }

    public static String toBase32(String text, Charset charset) {
        byte[] textBytes = text.getBytes(charset);
        byte[] b32Bytes = encode(textBytes);
        return new String(b32Bytes, StandardCharsets.US_ASCII);
    }

    public static byte[] encode(byte[] input) {
        byte[] b32 = BASE32.encode(input);
        exchange(b32);
        return b32;
    }

    public static String encodeToString(byte[] input) {
        byte[] b32 = encode(input);
        return PREFIX + new String(b32, StandardCharsets.US_ASCII);
    }

    public static String encodeUTF8ToString(String input) {
        return encodeTextToString(input, StandardCharsets.UTF_8);
    }

    public static String encodeTextToString(String input, Charset charset) {
        byte[] b = input.getBytes(charset);
        return encodeToString(b);
    }

    public static byte[] decode(byte[] b32) {
        restore(b32);
        byte[] output = BASE32.decode(b32);
        exchange(b32);
        return output;
    }

    public static byte[] decodeString(String irpb32) {
        String subb32 = irpb32.substring(PREFIX.length());
        byte[] b32Bytes = subb32.getBytes(StandardCharsets.US_ASCII);
        return decode(b32Bytes);
    }

    public static String decodeToUtf8(String irpb32) {
        return decodeToText(irpb32, StandardCharsets.UTF_8);
    }

    public static String decodeToText(String irpb32, Charset charset) {
        byte[] textBytes = decodeString(irpb32);
        return new String(textBytes, charset);
    }

    public static void exchange(byte[] b32) {
        for(int i = b32.length - 1; i >= 0; i--) {
            if(b32[i] != FILL_OLD) {
                break;
            } else {
                b32[i] = FILL_NEW;
            }
        }
    }

    public static void restore(byte[] b32) {
        for(int i = b32.length - 1; i >= 0; i--) {
            if(b32[i] != FILL_NEW) {
                break;
            } else {
                b32[i] = FILL_OLD;
            }
        }
    }
}
