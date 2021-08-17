package de.unileipzig.irpact.util.gnuplot.builder;

/**
 * @author Daniel Abitz
 */
public class StringSettings {

    protected String newLine = "\n";
    protected String comment = "#";

    protected boolean forceQuote = false;

    public StringSettings() {
    }

    public String getNewLine() {
        return newLine;
    }

    public void setNewLine(String newLine) {
        this.newLine = newLine;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
