package de.unileipzig.irpact.util.logfile.modify;

/**
 * @author Daniel Abitz
 */
public class RemoveUntilTextModifier implements LogFileLineModifier {

    protected String s;
    protected boolean trim;

    public RemoveUntilTextModifier(String s, boolean trim) {
        this.s = s;
        this.trim = trim;
    }

    public String getText() {
        return s;
    }

    public boolean isTrim() {
        return trim;
    }

    @Override
    public String modify(String input) {
        int index = input.indexOf(s);
        if(index == -1) {
            return input;
        }
        String subStr = input.substring(index + s.length());
        return trim ? subStr.trim() : subStr;
    }
}
