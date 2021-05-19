package de.unileipzig.irpact.commons.checksum;

/**
 * @author Daniel Abitz
 */
public final class ChecksumInfo {

    private String text;
    private Object value;

    private ChecksumInfo(String text, Object value) {
        this.text = text;
        this.value = value;
    }

    public static ChecksumInfo get(Object value) {
        return get(null, value);
    }

    public static ChecksumInfo get(String text, Object value) {
        return new ChecksumInfo(text, value);
    }

    String getText() {
        return text;
    }

    Object getValue() {
        return value;
    }

    public void dispose() {
        text = null;
        value = null;
    }
}
