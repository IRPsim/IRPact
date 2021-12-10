package de.unileipzig.irpact.commons.util.irpact32;

import org.apache.commons.codec.binary.Base32;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.function.Consumer;
import java.util.function.LongSupplier;

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

    public static final int BUFFER_SIZE = 8192;
    public static final int CUNK_SIZE = 76;

    public static final String PREFIX = "x";
    public static final String DELIMITER = "y";

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

    public static long encode(Path in, Path out, int bufferSize) throws IOException {
        try(InputStream is = Files.newInputStream(in);
            OutputStream os = Files.newOutputStream(out)) {
            return encode(is, os, bufferSize);
        }
    }

    public static long encode(InputStream in, OutputStream out, int bufferSize) throws IOException {
        try(IRPactBase32OutputStream b32out = new IRPactBase32OutputStream(out)) {
            byte[] buf = new byte[bufferSize];
            int l;
            long c = 0;
            while((l = in.read(buf)) != -1) {
                if(l > 0) {
                    b32out.write(buf, 0, l);
                    c += l;
                }
            }
            return c;
        }
    }

    public static long decode(Path in, Path out, int bufferSize) throws IOException {
        try(InputStream is = Files.newInputStream(in);
            OutputStream os = Files.newOutputStream(out)) {
            return decode(is, os, bufferSize);
        }
    }

    public static long decode(InputStream in, OutputStream out, int bufferSize) throws IOException {
        try(BinaryPersistDecodingInputStream b32in = new BinaryPersistDecodingInputStream(in)) {
            byte[] buf = new byte[bufferSize];
            int l;
            long c = 0;
            while((l = b32in.read(buf)) != -1) {
                if(l > 0) {
                    out.write(buf, 0, l);
                    c += l;
                }
            }
            return c;
        }
    }

    private static BufferedReader buffer(Reader reader) {
        return reader instanceof BufferedReader
                ? (BufferedReader) reader
                : new BufferedReader(reader);
    }

    public static String[] split(String chunk32) {
        return chunk32.split(DELIMITER);
    }

    public static String getLastSplitPart(String chunk32) {
        int lastIndex = chunk32.lastIndexOf(DELIMITER);
        return lastIndex == -1
                ? chunk32
                : chunk32.substring(lastIndex + 1);
    }

    public static int parseAndSplitIRPactBase32Data(
            Reader reader,
            LongSupplier idSupplier,
            int chunkSize,
            String prefix,
            String delimiter,
            Consumer<? super String> consumer) throws IOException {
        try(BufferedReader breader = buffer(reader)) {
            boolean eof = false;
            int chunkCount = 0;
            StringBuilder sb = new StringBuilder(chunkSize);
            while(!eof) {
                sb.setLength(0);
                while(sb.length() < chunkSize) {
                    int c = breader.read();
                    if(c == -1) {
                        eof = true;
                        break;
                    } else {
                        sb.append((char) c);
                    }
                }
                if(sb.length() > 0) {
                    long id = idSupplier.getAsLong();
                    String idHex = Long.toHexString(id);
                    sb.insert(0, prefix + idHex + delimiter);
                    String chunk = sb.toString();
                    consumer.accept(chunk);
                    chunkCount++;
                }
            }
            return chunkCount;
        }
    }
}
