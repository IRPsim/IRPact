package de.unileipzig.irpact.util.logfile.sort;

/**
 * @author Daniel Abitz
 */
public class NumericLogFileLineSorter implements LogFileLineSorter {

    protected String splitter;
    protected int sortIndex;

    public NumericLogFileLineSorter(String splitter, int sortIndex) {
        this.splitter = splitter;
        this.sortIndex = sortIndex;
    }

    public String getSplitter() {
        return splitter;
    }

    public int getSortIndex() {
        return sortIndex;
    }

    @Override
    public int compare(String o1, String o2) {
        String[] p1 = o1.split(splitter);
        String[] p2 = o2.split(splitter);

        double d1 = Double.parseDouble(p1[sortIndex]);
        double d2 = Double.parseDouble(p2[sortIndex]);
        return Double.compare(d1, d2);
    }
}
