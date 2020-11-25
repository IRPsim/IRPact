package de.unileipzig.irpact.start;

import de.unileipzig.irptools.util.IRPLogger;

import java.util.function.BooleanSupplier;

/**
 * @author Daniel Abitz
 */
public class IRPact {

    //=========================
    //logging and co
    //=========================

    public static BooleanSupplier UTIL_LOGGING = IRPact::doUtilLogging;
    private static boolean utilLogging = true;

    public static boolean doUtilLogging() {
        return utilLogging;
    }

    public static void setUtilLogging(boolean enabled) {
        utilLogging = enabled;
    }

    public static void disableUtilLogging() {
        setUtilLogging(false);
    }

    public static void enableUtilLogging() {
        setUtilLogging(true);
    }

    public static IRPLogger getUtilLogger(Class<?> c) {
        return IRPLogger.getLogger(UTIL_LOGGING, c);
    }

    //=========================
    //main
    //=========================

    public static void main(String[] args) {
    }
}
