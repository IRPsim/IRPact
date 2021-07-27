package de.unileipzig.irpact.start;

/**
 * @author Daniel Abitz
 */
public final class GlobalSettings {

    private static boolean copyLogToOutput = true;
    private static boolean printStacktraceImage = true;

    protected GlobalSettings() {
    }

    public static boolean shouldCopyLogToOutput() {
        return copyLogToOutput;
    }

    public static void setCopyLogToOutput(boolean value) {
        copyLogToOutput = value;
    }

    public static boolean shouldPrintStacktraceImage() {
        return printStacktraceImage;
    }

    public static void setPrintStacktraceImage(boolean value) {
        printStacktraceImage = value;
    }
}
