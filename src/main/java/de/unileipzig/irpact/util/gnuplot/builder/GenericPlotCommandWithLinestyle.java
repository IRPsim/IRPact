package de.unileipzig.irpact.util.gnuplot.builder;

import java.io.IOException;
import java.text.MessageFormat;

/**
 * @author Daniel Abitz
 */
public class GenericPlotCommandWithLinestyle extends PlotCommand {

    protected static final String PATTERN = "{0} u 2:xtic(1) ti col ls 1, for [i=3:*] '''' u i ti col ls (((i-2)%{1})+1)";
    protected String data;
    protected int maxCycle;

    public GenericPlotCommandWithLinestyle(String data, int maxCycle) {
        super(null);
        setData(data);
        setMaxCycle(maxCycle);
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getData() {
        return data;
    }

    public void setMaxCycle(int maxCycle) {
        this.maxCycle = maxCycle;
    }

    public int getMaxCycle() {
        return maxCycle;
    }

    public String getText() {
        return MessageFormat.format(PATTERN, getData(), getMaxCycle());
    }

    @Override
    public void print(StringSettings settings, Appendable target) throws IOException {
        target.append("plot ");
        target.append(getText());
    }
}
