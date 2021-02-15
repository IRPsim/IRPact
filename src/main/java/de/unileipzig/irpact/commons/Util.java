package de.unileipzig.irpact.commons;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Random;

/**
 * @author Daniel Abitz
 */
public final class Util {

    public static final long USE_RANDOM_SEED = 0L;
    public static final Random RND = new Random();
    public static final ObjectMapper JSON_MAPPER = new ObjectMapper();

    private Util() {
    }

    //TODO
    public static String lineSeparator() {
        return "\n";
    }

    public static long getRandomSeedIf0(long seed) {
        return seed == USE_RANDOM_SEED ? RND.nextLong() : seed;
    }

    public static String printClass(Object obj) {
        return obj == null
                ? "null"
                : obj.getClass().toString();
    }

    public static int nextInt(Random rnd, int lower, int upper) {
        return rnd.nextInt(upper - lower) + lower;
    }

    public static double nextDouble(Random rnd, double lower, double upper) {
        return rnd.nextDouble() * (upper - lower) + lower;
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
