package de.unileipzig.irpact.util.pvact;

import de.unileipzig.irpact.commons.util.Printable;

import java.util.Arrays;
import java.util.List;

/**
 * @author Daniel Abitz
 */
public enum Milieu implements Printable {
    PRA("PRA"),
    PER("PER"),
    SOK("SOK"),
    BUM("BUM"),
    PRE("PRE"),
    EPE("EPE"),
    TRA("TRA"),
    KET("KET"),
    LIB("LIB"),
    HED("HED"),
    G("G");

    public static final List<Milieu> ALL = Arrays.asList(values());

    private final String TEXT;

    Milieu(String text) {
        TEXT = text;
    }

    @Override
    public String print() {
        return TEXT;
    }
}
