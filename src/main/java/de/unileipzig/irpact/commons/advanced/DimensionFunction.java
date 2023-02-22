package de.unileipzig.irpact.commons.advanced;

public interface DimensionFunction<T> {

  double get(T value, int dimension);
}
