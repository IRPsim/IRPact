package de.unileipzig.irpact.experimental.tests.manyThreads;

import jadex.bridge.service.annotation.Reference;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * @author Daniel Abitz
 */
@Reference
public class RetData implements Iterable<String> {

    private final List<String> names = new ArrayList<>();

    public void add(String name) {
        names.add(name);
    }

    public List<String> getNames() {
        return names;
    }

    @Override
    public Iterator<String> iterator() {
        return names.iterator();
    }
}
