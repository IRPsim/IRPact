package de.unileipzig.irpact.util.gnuplot.builder;

import java.io.IOException;
import java.text.MessageFormat;

/**
 * @author Daniel Abitz
 */
public class SpecialLinePlotCommand extends PlotCommand {

    protected static final String PATTERN = "{0} u 2:xtic(1) ti col linewidth {1}, for [i=3:*] '''' u i ti col linewidth {1}";
    protected String data;
    protected int linewidth;

    public SpecialLinePlotCommand(String data) {
        this(data, 1);
    }

    public SpecialLinePlotCommand(String data, int linewidth) {
        super(null);
        setData(data);
        setLinewidth(linewidth);
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getData() {
        return data;
    }

    public int getLinewidth() {
        return linewidth;
    }

    public void setLinewidth(int linewidth) {
        this.linewidth = linewidth;
    }

    public String getText() {
        return MessageFormat.format(PATTERN, getData(), getLinewidth());
    }

    @Override
    public void print(StringSettings settings, Appendable target) throws IOException {
        target.append("plot ");
        target.append(getText());
    }
}
