package de.unileipzig.irpact.util.gnuplot.builder;

import java.io.IOException;
import java.text.MessageFormat;

/**
 * @author Daniel Abitz
 */
public class MultiDataPlotCommandWithLinewidth extends PlotCommand {

    protected static final String PATTERN =
            "{0} u 2:xtic(1) ti col linecolor 2 linewidth {1}, for [i=3:*] '''' u i ti col linecolor i linewidth {1}";

    protected String data;              //0
    protected int linewidth;            //1

    public MultiDataPlotCommandWithLinewidth() {
        super(null);
    }

    public void setData(int arg) {
        setData(GnuPlotBuilder.arg(arg));
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

    @Override
    public String getText() {
        return MessageFormat.format(PATTERN,
                getData(),
                getLinewidth());
    }

    @Override
    public void print(StringSettings settings, Appendable target) throws IOException {
        target.append("plot ");
        target.append(getText());
    }
}
