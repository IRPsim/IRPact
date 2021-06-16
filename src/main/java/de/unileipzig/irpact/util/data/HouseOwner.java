package de.unileipzig.irpact.util.data;

import de.unileipzig.irpact.commons.util.Printable;

/**
 * @author Daniel Abitz
 */
public enum HouseOwner implements Printable {
    ANDERE("Andere"),
    OEFFENTLICH("Oeffentliche bzw. kommunale Unternehmen"),
    PRIVAT("Privat"),
    LEIPZIG("Stadt Leipzig");

    private final String TEXT;

    HouseOwner(String text) {
        TEXT = text;
    }

    @Override
    public String print() {
        return TEXT;
    }
}
