package de.unileipzig.irpact.commons.util.data.grouping;

import de.unileipzig.irpact.commons.util.CollectionUtil;
import de.unileipzig.irpact.commons.util.StringUtil;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

/**
 * @author Daniel Abitz
 */
public final class GroupingUtil {

    private GroupingUtil() {
    }

    //=========================
    //util
    //=========================

    public static String[][] transpose(String[][] input) {
        return CollectionUtil.transpose(input, (i, j) -> new String[i.intValue()][j.intValue()]);
    }

    //=========================
    //grouping2
    //=========================

    protected static <A, B, X> List<X> get(
            Map<A, Map<B, List<X>>> grouping,
            A a,
            B b) {
        Map<B, List<X>> bMap = grouping.get(a);
        if(bMap == null) return null;
        return bMap.get(b);
    }

    public static <A, B, X> String[][] toRawTable(
            Map<A, Map<B, List<X>>> grouping,
            List<A> allAKeys,
            List<B> allBKeys,
            Function<? super List<X>, ? extends String> reduceFunction) {
        String[][] out = new String[allAKeys.size()][allBKeys.size()];
        for(int ia = 0; ia < allAKeys.size(); ia++) {
            A a = allAKeys.get(ia);
            for(int jb = 0; jb < allBKeys.size(); jb++) {
                String entry;
                if(grouping == null) {
                    entry = reduceFunction.apply(Collections.emptyList());
                } else {
                    B b = allBKeys.get(jb);
                    List<X> list = get(grouping, a, b);
                    entry = reduceFunction.apply(list);
                }
                out[ia][jb] = entry;
            }
        }
        return out;
    }

    public static void appendTo(
            Appendable appendable,
            List<?> allAKeys,
            List<?> allBKeys,
            String[][] rawData,
            String delimiter) throws IOException {
        appendable.append("");
        for(Object b: allBKeys) {
            appendable.append(delimiter);
            appendable.append(b.toString());
        }

        for(int ia = 0; ia < allAKeys.size(); ia++) {
            Object a = allAKeys.get(ia);
            appendable.append(StringUtil.lineSeparator());
            appendable.append(a.toString());
            for(int jb = 0; jb < allBKeys.size(); jb++) {
                String entry = rawData[ia][jb];
                appendable.append(delimiter);
                appendable.append(entry);
            }
        }
    }

    public static String print(
            List<?> allAKeys,
            List<?> allBKeys,
            String[][] rawData,
            String delimiter) {
        try {
            StringBuilder sb = new StringBuilder();
            appendTo(sb, allAKeys, allBKeys, rawData, delimiter);
            return sb.toString();
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    //=========================
    //grouping1
    //=========================

    public static <A, X> String[][] toRawTable(
            Map<A, List<X>> grouping,
            List<A> allAKeys,
            Function<? super List<X>, ? extends String> reduceFunction) {
        String[][] out = new String[allAKeys.size()][1];
        for(int ia = 0; ia < allAKeys.size(); ia++) {
            A a = allAKeys.get(ia);
            List<X> list = grouping.get(a);
            String entry = reduceFunction.apply(list);
            out[ia][0] = entry;
        }
        return out;
    }

    public static void appendTo(
            Appendable appendable,
            List<?> allAKeys,
            String[][] rawData,
            String delimiter) throws IOException {

        if(rawData.length == 1) {
            //transposed
            for(int ia = 0; ia < allAKeys.size(); ia++) {
                if(ia > 0) {
                    appendable.append(delimiter);
                }
                Object a = allAKeys.get(ia);
                appendable.append(a.toString());
            }

            appendable.append(StringUtil.lineSeparator());
            for(int ia = 0; ia < allAKeys.size(); ia++) {
                if(ia > 0) {
                    appendable.append(delimiter);
                }
                String value = rawData[0][ia];
                appendable.append(value);
            }
        } else {
            //not transposed
            for(int ia = 0; ia < allAKeys.size(); ia++) {
                if(ia > 0) {
                    appendable.append(StringUtil.lineSeparator());
                }
                Object a = allAKeys.get(ia);
                appendable.append(a.toString());
                String entry = rawData[ia][0];
                appendable.append(delimiter);
                appendable.append(entry);
            }
        }
    }

    public static String print(
            List<?> allAKeys,
            String[][] rawData,
            String delimiter) {
        try {
            StringBuilder sb = new StringBuilder();
            appendTo(sb, allAKeys, rawData, delimiter);
            return sb.toString();
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }
}
