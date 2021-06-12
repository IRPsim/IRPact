package de.unileipzig.irpact.util.R.builder;

import java.io.IOException;
import java.io.UncheckedIOException;

/**
 * @author Daniel Abitz
 */
public interface Element {

    //=========================
    //Constants
    //=========================

    String TITLE = "title";
    String X = "x";
    String Y = "y";
    String GROUP = "group";
    String COLOUR = "colour";
    String FILL = "fill";
    String LINETYPE = "linetype";
    String DATA = "data";
    String HEIGHT = "height";
    String WIDTH = "width";
    String UNITS = "units";
    String DPI = "dpi";
    String HEADER = "header";
    String SEP = "sep";
    String COL_CLASSES = "colClasses";
    String POSITION = "position";
    String STAT = "stat";
    String VALUES = "values";
    String SIZE = "size";
    String ENCODING = "encoding";
    String BREAKS = "breaks";
    String PLOT_TITLE = "plot.title";

    String NUMERIC = "numeric";
    String CHARACTER = "character";
    String STACK = "stack";
    String IDENTITY = "identity";

    String UTF8 = "UTF-8";

    String TWODASH = "twodash";
    String SOLID = "solid";
    String LONGDASH = "longdash";
    String DOTTED = "dotted";
    String DOTDASH = "dotdash";
    String DASHED = "dashed";
    String BLANK = "blank";

    String INCH = "in";

    //=========================
    //helper
    //=========================

    static String iconv(String text, String encoding) {
        return "iconv(\"" + text + "\", \"" + encoding + "\")";
    }

    //=========================
    //Element
    //=========================

    boolean isPrintable();

    boolean print(StringSettings settings, Appendable target) throws IOException;

    default String print(StringSettings settings) {
        try {
            StringBuilder sb = new StringBuilder();
            print(settings, sb);
            return sb.toString();
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }
}
