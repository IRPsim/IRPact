package de.unileipzig.irpact.commons.util;

import org.apache.commons.codec.binary.Base64;
import org.slf4j.helpers.MessageFormatter;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.Collection;

/**
 * @author Daniel Abitz
 */
public final class StringUtil {

    private static final Base64 BASE64 = new Base64();
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

    public static String printDoubleWithComma(double value) {
        String str = Double.toString(value);
        return str.replace('.', ',');
    }
}
