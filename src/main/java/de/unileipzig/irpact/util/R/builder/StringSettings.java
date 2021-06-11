package de.unileipzig.irpact.util.R.builder;

import de.unileipzig.irpact.commons.util.StringUtil;

/**
 * @author Daniel Abitz
 */
public class StringSettings {

    protected String newLine = "\n";
    protected String delimiter = " ";
    protected String quote = "\"";
    protected String elementDelimiter = ", ";
    protected String equalSign = " = ";
    protected String inequalSign = " != ";
    protected String tab = "    ";
    protected String comment = "# ";
    protected String assignmentSign = " <- ";
    protected String elementLinker = " + ";
    protected String waveSign = " ~ ";

    protected boolean forceQuote = false;

    public StringSettings() {
    }

    public String tryQuote(String str) {
        if(forceQuote || str.contains(delimiter)) {
            return quote(str);
        } else {
            return str;
        }
    }

    public String quote(String str) {
        return quote + str + quote;
    }

    public String getNewLine() {
        return newLine;
    }

    public void setNewLine(String newLine) {
        this.newLine = newLine;
    }

    public String getQuote() {
        return quote;
    }

    public void setQuote(String quote) {
        this.quote = quote;
    }

    public String getDelimiter() {
        return delimiter;
    }

    public void setDelimiter(String delimiter) {
        this.delimiter = delimiter;
    }

    public void setElementDelimiter(String elementDelimiter) {
        this.elementDelimiter = elementDelimiter;
    }

    public String getElementDelimiter() {
        return elementDelimiter;
    }

    public void setEqualSign(String equalSign) {
        this.equalSign = equalSign;
    }

    public String getEqualSign() {
        return equalSign;
    }

    public void setInequalSign(String inequalSign) {
        this.inequalSign = inequalSign;
    }

    public String getInequalSign() {
        return inequalSign;
    }

    public void setTab(String tab) {
        this.tab = tab;
    }

    public String getTab() {
        return tab;
    }

    public String getTab(int count) {
        return StringUtil.repeat(tab, count);
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getComment() {
        return comment;
    }

    public void setAssignmentSign(String assignmentSign) {
        this.assignmentSign = assignmentSign;
    }

    public String getAssignmentSign() {
        return assignmentSign;
    }

    public void setElementLinker(String elementLinker) {
        this.elementLinker = elementLinker;
    }

    public String getElementLinker() {
        return elementLinker;
    }

    public void setWaveSign(String waveSign) {
        this.waveSign = waveSign;
    }

    public String getWaveSign() {
        return waveSign;
    }

    public void setForceQuote(boolean forceQuote) {
        this.forceQuote = forceQuote;
    }

    public boolean isForceQuote() {
        return forceQuote;
    }
}
