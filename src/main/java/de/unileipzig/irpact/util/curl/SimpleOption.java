package de.unileipzig.irpact.util.curl;

/**
 * @author Daniel Abitz
 */
public class SimpleOption implements Option {

    private final String o;
    private final String v;

    public SimpleOption(String o, String v) {
        this.o = o;
        this.v = v;
    }

    public static SimpleOption of(String o, String v) {
        return new SimpleOption(o, v);
    }

    public static SimpleOption ofOption(String o) {
        return new SimpleOption(o, null);
    }

    public static SimpleOption ofValue(String v) {
        return new SimpleOption(null, v);
    }

    @Override
    public String getOption() {
        return o;
    }

    @Override
    public String getValue() {
        return v;
    }

    @Override
    public String toString() {
        return "SimpleOption{" +
                "o='" + o + '\'' +
                ", v='" + v + '\'' +
                '}';
    }
}
