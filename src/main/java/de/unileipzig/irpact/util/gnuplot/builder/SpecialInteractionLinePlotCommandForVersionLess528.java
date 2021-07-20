package de.unileipzig.irpact.util.gnuplot.builder;

import java.io.IOException;
import java.text.MessageFormat;

/**
 * @author Daniel Abitz
 */
public class SpecialInteractionLinePlotCommandForVersionLess528 extends PlotCommand {

    protected static final String SEP = ", ";
    protected static final String KEY_HEADER = "keyentry w p ps 0 ti \"{0}\"";
    protected static final String REAL_DATA_ENTRY = "NaN w lines dashtype {0} linecolor rgb \"black\" ti \"{1}\"";
    protected static final String PLOT_X_FIRST = "{0} u 1:2:xtic(1) ti col linewidth {1} linecolor {2} pointtype {3} dashtype {4}";
    protected static final String PLOT_X_OTHER = "'''' u 1:{0} ti col linewidth {1} linecolor {2} pointtype {3} dashtype {4}";
    protected static final String PLOT_Y = "'''' u 1:{0} notitle linewidth {1} linecolor {2} pointtype {3} dashtype {4}";


    protected String keyTitle0;
    protected int dashtype0 = 1;
    protected int pointtype0 = 1;
    protected String dashtype0Label;
    protected int dashtype1 = 2;
    protected int pointtype1 = 2;
    protected String dashtype1Label;
    protected String keyTitle1;
    protected String data;
    protected int linewidth;
    protected int numberOfEntries = 1;

    public SpecialInteractionLinePlotCommandForVersionLess528() {
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

    public void setPointtype0(int pointtype0) {
        this.pointtype0 = pointtype0;
    }
    public int getPointtype0() {
        return pointtype0;
    }

    public void setPointtype1(int pointtype1) {
        this.pointtype1 = pointtype1;
    }
    public int getPointtype1() {
        return pointtype1;
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

    public void setNumberOfEntries(int numberOfEntries) {
        this.numberOfEntries = numberOfEntries;
    }
    public int getNumberOfEntries() {
        return numberOfEntries;
    }

    @Override
    public String getText() {
        StringBuilder sb = new StringBuilder();
        sb.append(MessageFormat.format(KEY_HEADER, keyTitle0));
        sb.append(SEP).append(MessageFormat.format(REAL_DATA_ENTRY, dashtype0, dashtype0Label));
        sb.append(SEP).append(MessageFormat.format(REAL_DATA_ENTRY, dashtype1, dashtype1Label));
        sb.append(SEP).append(MessageFormat.format(KEY_HEADER, keyTitle1));
        for(int i = 0; i < numberOfEntries; i++) {
            if(i == 0) {
                sb.append(SEP).append(MessageFormat.format(PLOT_X_FIRST, data, linewidth, 1, 1, 1));
            } else {
                sb.append(SEP).append(MessageFormat.format(PLOT_X_OTHER, (i + 1) * 2, linewidth, i + 1, 1, 1));
            }
            sb.append(SEP).append(MessageFormat.format(PLOT_Y, (i + 1) * 2 + 1, linewidth, i + 1, 2, 2));
        }

        return sb.toString();
    }

    @Override
    public void print(StringSettings settings, Appendable target) throws IOException {
        target.append("plot ");
        target.append(getText());
    }
}
