package de.unileipzig.irpact.util.gnuplot.builder;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Daniel Abitz
 */
public class SetTicsCommand implements Command {

    protected String type;
    protected Map<Integer, String> tics = new HashMap<>();

    public SetTicsCommand() {
    }

    public void setXTics() {
        type = "xtics";
    }

    public void setYTics() {
        type = "ytics";
    }

    public String getType() {
        return type;
    }

    public void setTics(Map<Integer, String> tics) {
        this.tics = tics;
    }

    @Override
    public void print(StringSettings settings, Appendable target) throws IOException {
        target.append("set ");
        target.append(type);
        target.append("(");
        boolean first = true;
        for(Map.Entry<Integer, String> entry: tics.entrySet()) {
            if(first) {
                first = false;
            } else {
                target.append(", ");
            }
            target.append("\"");
            target.append(entry.getValue());
            target.append("\" ");
            target.append(Integer.toString(entry.getKey()));
        }
        target.append(")");
    }
}
