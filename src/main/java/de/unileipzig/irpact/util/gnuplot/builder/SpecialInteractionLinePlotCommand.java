package de.unileipzig.irpact.util.gnuplot.builder;

import java.io.IOException;
import java.text.MessageFormat;

/**
 * @author Daniel Abitz
 */
public class SpecialInteractionLinePlotCommand extends PlotCommand {

    protected static final String PATTERN =
            "keyentry w p ps 0 ti \"{0}\", " +
            "NaN w lines dashtype {1} linecolor rgb \"black\" ti \"{2}\", " +
            "NaN w lines dashtype {3} linecolor rgb \"black\" ti \"{4}\", " +
            "keyentry w p ps 0 ti \"{5}\", " +
            "{6} u 1:2:xtic(1) ti col linewidth {7} linecolor 1 dashtype {1}, " +
            "'''' u 1:3 notitle linewidth {7} linecolor 1 dashtype {3}, " +
            "for [i=2:*] '''' u 1:i*2 ti col linewidth {7} linecolor i dashtype {1}, " +
            "for [i=2:*] '''' u 1:i*2+1 notitle linewidth {7} linecolor i dashtype {3}";

    protected String keyTitle0;         //0
    protected int dashtype0 = 1;        //1
    protected String dashtype0Label;    //2
    protected int dashtype1 = 2;        //3
    protected String dashtype1Label;    //4
    protected String keyTitle1;         //5
    protected String data;              //6
    protected int linewidth;            //7

    public SpecialInteractionLinePlotCommand() {
        super(null);
    }

    public void setKeyTitle0(String keyTitle0) {
        this.keyTitle0 = keyTitle0;
    }
    public String getKeyTitle0() {
        return keyTitle0;
    }

    public void setKeyTitle1(String keyTitle1) {
        this.keyTitle1 = keyTitle1;
    }
    public String getKeyTitle1() {
        return keyTitle1;
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

    public void setDashtype0Label(String dashtype0Label) {
        this.dashtype0Label = dashtype0Label;
    }
    public String getDashtype0Label() {
        return dashtype0Label;
    }

    public void setDashtype1Label(String dashtype1Label) {
        this.dashtype1Label = dashtype1Label;
    }
    public String getDashtype1Label() {
        return dashtype1Label;
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
                getKeyTitle0(),
                getDashtype0(),
                getDashtype0Label(),
                getDashtype1(),
                getDashtype1Label(),
                getKeyTitle1(),
                getData(),
                getLinewidth());
    }

    @Override
    public void print(StringSettings settings, Appendable target) throws IOException {
        target.append("plot ");
        target.append(getText());
    }
}
