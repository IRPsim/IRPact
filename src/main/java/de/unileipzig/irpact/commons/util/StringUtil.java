package de.unileipzig.irpact.commons.util;

/**
 * @author Daniel Abitz
 */
public final class StringUtil {

    public static final String LINE_SEPARATOR = "\n";

    private StringUtil() {
    }

    public static String lineSeparator() {
        return LINE_SEPARATOR;
    }

    public static String splitLen(String text, int lineLen) {
        if(text.length() < lineLen) {
            return text;
        }
        StringBuilder sb = new StringBuilder();
        for(int start = 0; start < text.length(); start += lineLen) {
            if(start > 0) {
                sb.append(lineSeparator());
            }
            sb.append(text, start, Math.min(start + lineLen, text.length()));
        }
        return sb.toString();
    }
}
