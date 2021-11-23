package de.unileipzig.irpact.util.gnuplot.builder;

import java.io.IOException;

/**
 * @author Daniel Abitz
 */
public class StyleLineCommand implements Command {

    protected int index;
    protected Integer lt;
    protected Object lc;
    protected Number lw;

    public StyleLineCommand() {
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public void setType(Integer lt) {
        this.lt = lt;
    }

    public void setRGB(String color) {
        lc = "rgb " + color;
    }

    public void setWidth(Number lw) {
        this.lw = lw;
    }

    @Override
    public void print(StringSettings settings, Appendable target) throws IOException {
        StringBuilder sb = new StringBuilder();
        sb.append("set style line ");
        sb.append(index);
        if(lt != null) {
            sb.append(" lt ");
            sb.append(lt);
        }
        if(lc != null) {
            sb.append(" lc ");
            sb.append(lc);
        }
        if(lw != null) {
            sb.append(" lw ");
            sb.append(lw);
        }

        target.append(sb);
    }
}
