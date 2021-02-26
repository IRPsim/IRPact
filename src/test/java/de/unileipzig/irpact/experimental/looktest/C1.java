package de.unileipzig.irpact.experimental.looktest;

/**
 * @author Daniel Abitz
 */
public class C1 implements I {

    public static void doIt() {
        System.out.println(I.lookupClass().getName());
    }
}
