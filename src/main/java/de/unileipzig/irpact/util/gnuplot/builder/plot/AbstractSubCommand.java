package de.unileipzig.irpact.util.gnuplot.builder.plot;

import java.io.IOException;

/**
 * @author Daniel Abitz
 */
public abstract class AbstractSubCommand implements SubCommand {

    protected static void tryAppendColumn(Appendable target, Object column) throws IOException {
        if(column != null) {
            target.append(":").append(column.toString());
        }
    }

    protected static void tryAppend(Appendable target, Object value) throws IOException {
        tryAppend(target, null, value);
    }

    protected static void tryAppend(Appendable target, Object prePart, Object value) throws IOException {
        tryAppend(target, prePart, value, null);
    }

    protected static void trySet(Appendable target, Object value, boolean test) throws IOException {
        if(test) {
            tryAppend(target, value);
        }
    }

    protected static void tryAppend(Appendable target, Object prePart, Object value, Object postPart) throws IOException {
        if(value != null) {
            if(prePart != null) target.append(prePart.toString());
            target.append(value.toString());
            if(postPart != null) target.append(postPart.toString());
        }
    }
}
