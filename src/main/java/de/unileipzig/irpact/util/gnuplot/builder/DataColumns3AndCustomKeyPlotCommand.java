package de.unileipzig.irpact.util.gnuplot.builder;

import java.io.IOException;
import java.text.MessageFormat;

/**
 * @author Daniel Abitz
 */
public class DataColumns3AndCustomKeyPlotCommand extends PlotCommand {

    protected static final String PATTERN = "NaN w boxes title \"{0}\" linecolor 1 linewidth {4}, NaN w boxes title \"{1}\" linecolor 2 linewidth {4}, NaN w boxes title \"{2}\" linecolor 3 linewidth {4}, {3} u 2:xtic(1) notitle col linewidth 1, '''' u 3 notitle col linecolor 1 linewidth {4}, '''' u 4 notitle col linecolor 2 linewidth {4}, '''' u 5 notitle col linecolor 3 linewidth {4}";
    protected String phase0;    //0
    protected String phase1;    //1
    protected String phase2;    //2
    protected String data;      //3
    protected int linewidth;    //4

    public DataColumns3AndCustomKeyPlotCommand(String phase0, String phase1, String phase2, String data, int linewidth) {
        super(null);
        setPhase0(phase0);
        setPhase1(phase1);
        setPhase2(phase2);
        setData(data);
        setLinewidth(linewidth);
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getData() {
        return data;
    }

    public void setPhase0(String phase0) {
        this.phase0 = phase0;
    }

    public String getPhase0() {
        return phase0;
    }

    public void setPhase1(String phase1) {
        this.phase1 = phase1;
    }

    public String getPhase1() {
        return phase1;
    }

    public void setPhase2(String phase2) {
        this.phase2 = phase2;
    }

    public String getPhase2() {
        return phase2;
    }

    public int getLinewidth() {
        return linewidth;
    }

    public void setLinewidth(int linewidth) {
        this.linewidth = linewidth;
    }

    public String getText() {
        return MessageFormat.format(PATTERN, getPhase0(), getPhase1(), getPhase2(), getData(), getLinewidth());
    }

    @Override
    public void print(StringSettings settings, Appendable target) throws IOException {
        target.append("plot ");
        target.append(getText());
    }
}
