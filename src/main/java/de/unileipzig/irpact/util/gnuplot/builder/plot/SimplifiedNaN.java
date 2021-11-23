package de.unileipzig.irpact.util.gnuplot.builder.plot;

import de.unileipzig.irpact.util.gnuplot.builder.StringSettings;

import java.io.IOException;

/**
 * @author Daniel Abitz
 */
public class SimplifiedNaN extends AbstractSubCommand {

    protected Object type;
    protected Object ti;
    protected Object lc;
    protected Object ls;
    protected Object lw;
    protected Object dt;

    public SimplifiedNaN() {
    }

    public SimplifiedNaN setLines() {
        return setType("lines");
    }

    public SimplifiedNaN setBoxes() {
        return setType("boxes");
    }

    public SimplifiedNaN setType(Object type) {
        this.type = type;
        return this;
    }

    public SimplifiedNaN setTi(String ti) {
        this.ti = ti;
        return this;
    }

    public SimplifiedNaN setLc(Object lc) {
        this.lc = lc;
        return this;
    }

    public SimplifiedNaN setLs(Object ls) {
        this.ls = ls;
        return this;
    }

    public SimplifiedNaN setLw(Object lw) {
        this.lw = lw;
        return this;
    }

    public SimplifiedNaN setDt(Object dt) {
        this.dt = dt;
        return this;
    }

    @Override
    public void print(StringSettings settings, Appendable target) throws IOException {
        target.append("NaN w ").append(type.toString());
        tryAppend(target, " ti ", ti);
        tryAppend(target, " dt ", dt);
        tryAppend(target, " lc ", lc);
        tryAppend(target, " ls ", ls);
        tryAppend(target, " lw ", lw);
    }
}
