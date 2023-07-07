package de.unileipzig.irpact.util.gnuplot.builder;

import java.io.IOException;
import java.text.MessageFormat;

/**
 * @author Daniel Abitz
 */
public class PlotCommandForNValuesAndLineWidthAndDashtype extends PlotCommand {

    protected static final String FIRST_PART =
            "{0} u 2:xtic(1) ti col dashtype {1} linewidth {2}";
    protected static final String PART =
            ", '''' u {0} ti col dashtype {1} linewidth {2}";

    protected int n;
    protected String data;
    protected int dashtype0 = 1;
    protected int dashtype1 = 2;
    protected int linewidth;

    public PlotCommandForNValuesAndLineWidthAndDashtype() {
        super(null);
    }

    public void setN(int n) {
        this.n = n;
    }

    public int getN() {
        return n;
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
        StringBuilder sb = new StringBuilder();
        sb.append(MessageFormat.format(FIRST_PART, getData(), getDashtype0(), getLinewidth()));
        for(int i = 1; i < getN(); i++) {
            sb.append(MessageFormat.format(PART, i + 2, getDashtype1(), getLinewidth()));
        }
        return sb.toString();
    }

    @Override
    public void print(StringSettings settings, Appendable target) throws IOException {
        target.append("plot ");
        target.append(getText());
    }
}
