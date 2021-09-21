package de.unileipzig.irpact.util.gnuplot.builder;

import java.io.IOException;

/**
 * @author Daniel Abitz
 */
public class SplotBoxesReverseCommand implements Command {

    protected int from;
    protected int to;
    protected String source;

    public void setFrom(int from) {
        this.from = from;
    }

    public int getFrom() {
        return from;
    }

    public void setTo(int to) {
        this.to = to;
    }

    public int getTo() {
        return to;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getSource() {
        return source;
    }

    @Override
    public void print(StringSettings settings, Appendable target) throws IOException {
        target.append("splot for [i=");
        target.append(Integer.toString(getFrom()));
        target.append(":");
        target.append(Integer.toString(getTo()));
        target.append(":-1] ");
        target.append(getSource());
        target.append(" using 1:(i):i w boxes ti col");
    }
}
