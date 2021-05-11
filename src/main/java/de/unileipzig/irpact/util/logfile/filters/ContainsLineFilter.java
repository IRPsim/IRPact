package de.unileipzig.irpact.util.logfile.filters;

/**
 * @author Daniel Abitz
 */
public class ContainsLineFilter implements LogFileLineFilter {

    protected CharSequence s;

    public ContainsLineFilter(CharSequence s) {
        this.s = s;
    }

    public CharSequence getText() {
        return s;
    }

    @Override
    public boolean accept(String line) {
        return line != null && line.contains(s);
    }
}
