package de.unileipzig.irpact.util.gnuplot.builder;

/**
 * @author Daniel Abitz
 */
public class StringSettings {

    protected String newLine = "\n";
    protected String comment = "#";
    protected String quote = "\"";

    protected boolean forceQuote = false;

    public StringSettings() {
    }

    public void setQuote(String quote) {
        this.quote = quote;
    }

    public String getQuote() {
        return quote;
    }

    public String quote(String input) {
        return quote + input + quote;
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
