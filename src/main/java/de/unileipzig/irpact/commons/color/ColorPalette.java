package de.unileipzig.irpact.commons.color;

import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Stream;

/**
 * @author Daniel Abitz
 */
public class ColorPalette implements Iterable<Color> {

    protected List<Color> colors;

    public ColorPalette() {
        this(new ArrayList<>());
    }

    public ColorPalette(Color... colors) {
        this();
        addAll(colors);
    }

    public ColorPalette(List<Color> colors) {
        this.colors = colors;
    }

    public void add(Color color) {
        colors.add(color);
    }

    public void addAll(Color... colors) {
        Collections.addAll(this.colors, colors);
    }

    public Color get(int i) {
        return colors.get(i);
    }

    public String printRGBHex(int i) {
        return printRGBHex(get(i));
    }

    public String printARGBHex(int i) {
        return printARGBHex(get(i));
    }

    public static String printRGBHex(Color color) {
        int rgb = color.getRGB() & 0xffffff;
        StringBuilder sb = new StringBuilder(Integer.toHexString(rgb));
        while(sb.length() < 6) {
            sb.insert(0, "0");
        }
        return sb.toString();
    }

    public static String printARGBHex(Color color) {
        int argb = color.getRGB();
        StringBuilder sb = new StringBuilder(Integer.toHexString(argb));
        while(sb.length() < 8) {
            sb.insert(0, "0");
        }
        return sb.toString();
    }

    public int size() {
        return colors.size();
    }

    public Stream<Color> stream() {
        return colors.stream();
    }

    @Override
    public Iterator<Color> iterator() {
        return colors.iterator();
    }
}
