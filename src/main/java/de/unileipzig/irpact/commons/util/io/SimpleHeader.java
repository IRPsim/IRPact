package de.unileipzig.irpact.commons.util.io;

import de.unileipzig.irpact.commons.util.CollectionUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Daniel Abitz
 */
public class SimpleHeader implements Header {

    protected List<String> header;

    public SimpleHeader() {
        this(new ArrayList<>());
    }

    public SimpleHeader(List<String> header) {
        this.header = header;
    }

    public SimpleHeader(String[] header) {
        this.header = CollectionUtil.arrayListOf(header);
    }

    public void add(String label) {
        header.add(label);
    }

    public List<String> get() {
        return header;
    }

    public void reset() {
        header.clear();
    }

    @Override
    public String getLabel(int index) {
        return header.get(index);
    }

    @Override
    public int length() {
        return header.size();
    }

    @Override
    public String[] toArray() {
        return header.toArray(new String[0]);
    }

    @Override
    public List<String> toList() {
        return new ArrayList<>(header);
    }

    @Override
    public String toString() {
        return header.toString();
    }
}
