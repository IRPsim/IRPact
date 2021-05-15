package de.unileipzig.irpact.jadex.persistance.binary.io;

import org.apache.commons.codec.binary.Base32;

/**
 * @author Daniel Abitz
 */
public final class BinaryPersistBase32 {

    public static final Base32 BASE32 = new Base32(true);
    public static final byte FILL_BASE32 = (byte) '=';
    public static final byte FILL_IRP32 = (byte) 'Z';
    public static final int FILL_BASE32_INT = '=';
    public static final int FILL_IRP32_INT = 'Z';

    private BinaryPersistBase32() {
    }

    public static String buildId(String x, long id) {
        String hex = Long.toHexString(id);
        return x + hex + x;
    }

    public static long getId(String x, String irp32) {
        int dataStart = findDataStart(x, irp32);
        if(dataStart == -1) {
            throw new IllegalArgumentException();
        }
        String hex = irp32.substring(x.length(), dataStart - x.length());
        return Long.parseLong(hex, 16);
    }

    public static int findDataStart(String x, String irp32) {
        int index = irp32.indexOf(x, 1);
        if(index == -1) {
            return -1;
        }
        return index + x.length();
    }

    public static byte[] encode(byte[] input) {
        byte[] b32 = BASE32.encode(input);
        exchange(b32);
        return b32;
    }

    public static byte[] decode(byte[] irp32) {
        restore(irp32);
        byte[] output = BASE32.decode(irp32);
        exchange(irp32);
        return output;
    }

    public static void exchange(byte[] b32) {
        for(int i = b32.length - 1; i >= 0; i--) {
            if(b32[i] == FILL_BASE32) {
                b32[i] = FILL_IRP32;
            } else {
                break;
            }
        }
    }

    public static void restore(byte[] b32) {
        for(int i = b32.length - 1; i >= 0; i--) {
            if(b32[i] == FILL_IRP32) {
                b32[i] = FILL_BASE32;
            } else {
                break;
            }
        }
    }
}
