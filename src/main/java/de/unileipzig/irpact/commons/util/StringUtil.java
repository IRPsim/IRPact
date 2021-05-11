package de.unileipzig.irpact.commons.util;

import de.unileipzig.irptools.util.Util;
import org.slf4j.helpers.MessageFormatter;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.Arrays;
import java.util.Collection;

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

    public static String concat(String delimiter, String... parts) {
        return concat(delimiter, Arrays.asList(parts));
    }

    public static String concat(String delimiter, Collection<? extends String> parts) {
        StringBuilder sb = new StringBuilder();
        for(String part: parts) {
            if(sb.length() > 0) {
                sb.append(delimiter);
            }
            sb.append(part);
        }
        return sb.toString();
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

    public static String format(String pattern, Object arg) {
        return MessageFormatter.format(pattern, arg).getMessage();
    }

    public static String format(String pattern, Object arg1, Object arg2) {
        return MessageFormatter.format(pattern, arg1, arg2).getMessage();
    }

    public static String format(String pattern, Object... args) {
        return MessageFormatter.arrayFormat(pattern, args).getMessage();
    }

    public static String repeat(String input, int count) {
        if(count == 0) {
            return "";
        }

        try {
            StringBuilder sb = new StringBuilder();
            appendRepeat(sb, input, count);
            return sb.toString();
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    public static void appendRepeat(Appendable appendable, String input, int count) throws IOException {
        for(int i = 0; i < count; i++) {
            appendable.append(input);
        }
    }

    public static double parseDoubleWithComma(String value) {
        String valueWithDot = value.replace(',', '.');
        return Double.parseDouble(valueWithDot);
    }

    public static String printDoubleWithComma(double value) {
        String str = Double.toString(value);
        return str.replace('.', ',');
    }
}
