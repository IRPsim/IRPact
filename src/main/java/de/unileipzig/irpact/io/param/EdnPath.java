package de.unileipzig.irpact.io.param;

import java.util.Collection;
import java.util.stream.Stream;

/**
 * @author Daniel Abitz
 */
public interface EdnPath extends Iterable<String> {

    EdnPath copy();

    EdnPath resolve(String next);

    EdnPath resolveSibling(String next);

    EdnPath addTo(Collection<? super EdnPath> target);

    int length();

    String get(int index);

    String getSecondToLast();

    String getLast();

    String[] toArray();

    String[] toArrayWithoutRoot();

    Stream<String> stream();
}
