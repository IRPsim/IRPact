package de.unileipzig.irpact.util.gnuplot.builder;

import java.io.IOException;
import java.text.MessageFormat;

/**
 * @author Daniel Abitz
 */
public class PlotCommandForTwoValuesAndLineWidthAndDashtype extends PlotCommand {

    protected static final String PATTERN =
            "{0} u 2:xtic(1) ti col dashtype {1} linewidth {3}, '''' u 3 ti col dashtype {2} linewidth {3}";

    protected String data;              //0
    protected int dashtype0 = 1;        //1
    protected int dashtype1 = 2;        //2
    protected int linewidth;            //3

    public PlotCommandForTwoValuesAndLineWidthAndDashtype() {
        super(null);
    }

    public void setDashtype0(int dashtype0) {
        this.dashtype0 = dashtype0;
    }
    public int getDashtype0() {
        return dashtype0;
    }

    public void setDashtype1(int dashtype1) {
        this.dashtype1 = dashtype1;
    }
    public int getDashtype1() {
        return dashtype1;
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
                getDashtype0(),
                getDashtype1(),
                getLinewidth());
    }

    @Override
    public void print(StringSettings settings, Appendable target) throws IOException {
        target.append("plot ");
        target.append(getText());
    }
}
