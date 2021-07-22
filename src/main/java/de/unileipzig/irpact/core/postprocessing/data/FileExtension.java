package de.unileipzig.irpact.core.postprocessing.data;

/**
 * @author Daniel Abitz
 */
public enum FileExtension {
    CSV("csv"),
    XLSX("xlsx");

    private final String TEXT;

    FileExtension(String text) {
        this.TEXT = text;
    }

    public String getText() {
        return TEXT;
    }
}
