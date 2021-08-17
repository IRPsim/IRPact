package de.unileipzig.irpact.core.postprocessing.data;

/**
 * @author Daniel Abitz
 */
public enum DataToAnalyse {
    ZIP("zip"),
    PHASE("phase"),
    ALL_AGENTS("allAgents");

    private final String TEXT;

    DataToAnalyse(String text) {
        this.TEXT = text;
    }

    public String getText() {
        return TEXT;
    }
}
