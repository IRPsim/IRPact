package de.unileipzig.irpact.commons.util;

import org.apache.commons.codec.binary.Base32;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

/**
 * Modification of Base32 for IRPact.
 * The implementation uses RFC 4648 Base32hex and exchanges = with Z.
 *
 * @author Daniel Abitz
 */
public final class IRPactBase32 {

    private static final Base32 BASE32 = new Base32(true);
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

    public static String base32ToUtf8(String b32) {
        return toText(b32, StandardCharsets.UTF_8);
    }

    public static String toText(String b32, Charset charset) {
        byte[] b32Bytes = b32.getBytes(StandardCharsets.US_ASCII);
        byte[] textBytes = decode(b32Bytes);
        return new String(textBytes, charset);
    }

    public static byte[] encode(byte[] input) {
        byte[] b32 = BASE32.encode(input);
        exchange(b32);
        return b32;
    }

    public static String encodeToString(byte[] input) {
        byte[] b32 = encode(input);
        return new String(b32, StandardCharsets.US_ASCII);
    }

    public static String encodeUTF8ToString(String input) {
        byte[] b = input.getBytes(StandardCharsets.UTF_8);
        return encodeToString(b);
    }

    public static byte[] decode(byte[] b32) {
        restore(b32);
        byte[] output = BASE32.decode(b32);
        exchange(b32);
        return output;
    }

    public static byte[] decodeString(String b32) {
        byte[] b32Bytes = b32.getBytes(StandardCharsets.US_ASCII);
        return decode(b32Bytes);
    }

    public static String decodeStringToUTF8(String b32) {
        byte[] b = decodeString(b32);
        return new String(b, StandardCharsets.UTF_8);
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
