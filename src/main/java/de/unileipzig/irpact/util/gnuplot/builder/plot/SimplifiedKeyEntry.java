package de.unileipzig.irpact.util.gnuplot.builder.plot;

import de.unileipzig.irpact.util.gnuplot.builder.StringSettings;

import java.io.IOException;

/**
 * @author Daniel Abitz
 */
public class SimplifiedKeyEntry implements SubCommand {

    protected Object ti;

    public SimplifiedKeyEntry() {
    }

    public SimplifiedKeyEntry setTi(Object ti) {
        this.ti = ti;
        return this;
    }

    @Override
    public void print(StringSettings settings, Appendable target) throws IOException {
        target.append("keyentry w p ps 0 ti ");
        target.append(ti.toString());
    }
}
