package de.unileipzig.irpact.util.pvact;

import de.unileipzig.irpact.commons.util.Printable;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

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

    public static final List<Milieu> ALL =  Arrays.stream(values())
            .sorted(Comparator.comparing(Milieu::print))
            .collect(Collectors.toList());

    public static final List<Milieu> WITHOUT_G = Arrays.stream(values())
            .filter(m -> m != G)
            .sorted(Comparator.comparing(Milieu::print))
            .collect(Collectors.toList());

    private final String TEXT;

    Milieu(String text) {
        TEXT = text;
    }

    @Override
    public String print() {
        return TEXT;
    }

    public static Milieu get(String name) {
        for(Milieu m: values()) {
            if(m.TEXT.equals(name)) {
                return m;
            }
        }
        return null;
    }
}
