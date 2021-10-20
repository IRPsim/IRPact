package de.unileipzig.irpact.util.gnuplot.builder;

import java.io.IOException;
import java.text.MessageFormat;

/**
 * @author Daniel Abitz
 */
public class SimpleBoxplotPlotCommand extends PlotCommand {

    protected static final String PATTERN =
            "{0} u (1):1:({1}):2 notitle";

    protected String data;              //0
    protected double width = 0;         //1

    public SimpleBoxplotPlotCommand() {
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

    public void setWidth(double width) {
        this.width = width;
    }

    public double getWidth() {
        return width;
    }

    @Override
    public String getText() {
        return MessageFormat.format(PATTERN,
                getData(),
                getWidth());
    }

    @Override
    public void print(StringSettings settings, Appendable target) throws IOException {
        target.append("plot ");
        target.append(getText());
    }
}
