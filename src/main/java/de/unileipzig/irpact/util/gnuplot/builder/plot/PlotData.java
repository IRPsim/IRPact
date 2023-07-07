package de.unileipzig.irpact.util.gnuplot.builder.plot;

import de.unileipzig.irpact.util.gnuplot.builder.StringSettings;

import java.io.IOException;

/**
 * @author Daniel Abitz
 */
//INPUT u x:y:z ti TI ls LS dt DT
public class PlotData extends AbstractSubCommand {

    protected Object forLoop;
    protected Object input;

    protected Object column1;
    protected Object column2;
    protected Object column3;
    protected Object column4;
    protected Object column5;

    protected Object ti;
    protected boolean notitle = false;
    protected Object ls;
    protected Object lw;
    protected Object lc;
    protected Object dt;

    public PlotData() {
    }

    public PlotData setArgInput(int index) {
        return setInput(PlotCommandBuilder.arg(index));
    }

    public PlotData setSameInput() {
        return setInput("''");
    }

    public PlotData setInput(Object input) {
        this.input = input;
        return this;
    }

    public PlotData setColumn1(Object column1) {
        this.column1 = column1;
        return this;
    }

    public PlotData setColumn2(Object column2) {
        this.column2 = column2;
        return this;
    }

    public PlotData setColumn3(Object column3) {
        this.column3 = column3;
        return this;
    }

    public PlotData setColumn4(Object column4) {
        this.column4 = column4;
        return this;
    }

    public PlotData setColumn5(Object column5) {
        this.column5 = column5;
        return this;
    }

    public PlotData setForLoop(Object var, Object from, Object to) {
        return setForLoop("for [" + var + "=" + from + ":" + to + "]");
    }

    public PlotData setForLoop(Object forLoop) {
        this.forLoop = forLoop;
        return this;
    }

    public PlotData setTi(Object ti) {
        this.ti = ti;
        return this;
    }

    public PlotData setNotitle() {
        return setNotitle(true);
    }

    public PlotData setNotitle(boolean value) {
        this.notitle = value;
        return this;
    }

    public PlotData setLs(Object ls) {
        this.ls = ls;
        return this;
    }

    public PlotData setLw(Object lw) {
        this.lw = lw;
        return this;
    }

    public PlotData setLc(Object lc) {
        this.lc = lc;
        return this;
    }

    public PlotData setDt(Object dt) {
        this.dt = dt;
        return this;
    }

    @Override
    public void print(StringSettings settings, Appendable target) throws IOException {
        tryAppend(target, null, forLoop, " ");
        target.append(input.toString());
        target.append(" u ");

        target.append(column1.toString());
        tryAppendColumn(target, column2);
        tryAppendColumn(target, column3);
        tryAppendColumn(target, column4);
        tryAppendColumn(target, column5);

        tryAppend(target, " ti ", ti);
        trySet(target, " notitle", notitle);
        tryAppend(target, " ls ", ls);
        tryAppend(target, " dt ", dt);
        tryAppend(target, " lc ", lc);
        tryAppend(target, " lw ", lw);
    }
}
