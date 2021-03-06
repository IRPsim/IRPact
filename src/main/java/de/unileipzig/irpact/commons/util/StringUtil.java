package de.unileipzig.irpact.commons.util;

import org.apache.commons.codec.binary.Base64;
import org.slf4j.helpers.MessageFormatter;

import java.io.*;
import java.nio.charset.Charset;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author Daniel Abitz
 */
public final class StringUtil {

    private static final Base64 BASE64 = new Base64();
    public static final String LINE_SEPARATOR = "\n";
    public static final DecimalFormat DF2_POINT = createFormat("#.##", Locale.ENGLISH);
    public static final DecimalFormat DF2_COMMA = createFormat("#.##", Locale.GERMAN);

    private StringUtil() {
    }

    private static DecimalFormat createFormat(String pattern, Locale locale) {
        NumberFormat nf = NumberFormat.getNumberInstance(locale);
        DecimalFormat df = (DecimalFormat) nf;
        df.applyPattern(pattern);
        return df;
    }

    public static String firstLetterToUpperCase(String input) {
        if(input == null || input.isEmpty()) return input;
        return input.substring(0, 1).toUpperCase() + input.substring(1);
    }

    public static String lineSeparator() {
        return LINE_SEPARATOR;
    }

    public static boolean isBlank(String str) {
        char c;
        for(int i = 0; i < str.length(); i++) {
            c = str.charAt(i);
            if(c != ' ' && c != '\t' && !Character.isWhitespace(c)) {
                return false;
            }
        }
        return true;
    }

    public static String concat(String delimiter, Object... parts) {
        return concat(delimiter, Arrays.asList(parts));
    }

    public static String concat(String delimiter, Collection<?> parts) {
        StringBuilder sb = new StringBuilder();
        for(Object part: parts) {
            if(sb.length() > 0) {
                sb.append(delimiter);
            }
            sb.append(part);
        }
        return sb.toString();
    }

    public static String[] without(String[] input, String toRemove) {
        int count = 0;
        for(String str: input) {
            if(Objects.equals(str, toRemove)) {
                count++;
            }
        }
        String[] out = new String[input.length - count];
        int i = 0;
        for(String str: input) {
            if(!Objects.equals(str, toRemove)) {
                out[i++] = str;
            }
        }
        return out;
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

    public static boolean splitLen(String text, int lineLen, Collection<? super String> out) {
        if(text.length() < lineLen) {
            return out.add(text);
        }
        boolean changed = false;
        for(int start = 0; start < text.length(); start += lineLen) {
            String part = text.substring(start, Math.min(start + lineLen, text.length()));
            changed |= out.add(part);
        }
        return changed;
    }

    public static String toBase64(String text, Charset charset) {
        byte[] data = text.getBytes(charset);
        return toBase64(data);
    }

    public static String toBase64(byte[] data) {
        return BASE64.encodeToString(data);
    }

    public static String fromBase64(String b64, Charset outCharset) {
        byte[] data = fromBase64(b64);
        return new String(data, outCharset);
    }

    public static byte[] fromBase64(String b64) {
        return BASE64.decode(b64);
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

    public static int parseInt(String input) {
        if(input == null) throw new NullPointerException();
        if(input.contains(".")) {
            return (int) Double.parseDouble(input);
        } else {
            return Integer.parseInt(input);
        }
    }

    public static long parseLong(String input) {
        if(input == null) throw new NullPointerException();
        if(input.contains(".")) {
            return (long) Double.parseDouble(input);
        } else {
            return Long.parseLong(input);
        }
    }

    public static String printDoubleWithComma(double value) {
        String str = Double.toString(value);
        return str.replace('.', ',');
    }

    public static String escapeQuote(String input, String escape) {
        String escaped = escape + "\"";
        return input.replace("\"", escaped);
    }

    public static String restoreQuote(String input, String escape) {
        String escaped = escape + "\"";
        return input.replace("\"", escaped);
    }

    public static String escapeNewLine(String input, String escape) {
        String escaped = escape + "n";
        return input.replace("\n", escaped);
    }

    public static String restoreNewLine(String input, String escape) {
        String escaped = escape + "n";
        return input.replace(escaped, "\n");
    }

    public static String replaceSpace(String input, String replacement) {
        return input == null ? "null" : input.replace(" ", replacement);
    }

    public static String printStackTrace(Throwable t) throws UncheckedIOException {
        try(StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw)) {
            t.printStackTrace(pw);
            pw.flush();
            return sw.toString();
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    public static String printStackTraceWithTitle(String title, Throwable t) throws UncheckedIOException {
        String stackTrace = printStackTrace(t);
        return title + lineSeparator() + stackTrace;
    }

    public static List<String> getLines(CharSequence text) {
        try(StringReader sr = new StringReader(text.toString());
            BufferedReader br = new BufferedReader(sr)) {
            return br.lines().collect(Collectors.toList());
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    public static String toString(Collection<?> coll) {
        StringBuilder sb = new StringBuilder();
        boolean first = true;
        for(Object obj: coll) {
            if(first) {
                first = false;
            } else {
                sb.append(", ");
            }
            sb.append(obj);
        }
        return sb.toString();
    }
}
