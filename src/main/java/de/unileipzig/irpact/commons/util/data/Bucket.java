package de.unileipzig.irpact.commons.util.data;

/**
 * @author Daniel Abitz
 */
public interface Bucket<T> extends Comparable<Bucket<T>> {

    T getFrom();

    T getTo();
}
