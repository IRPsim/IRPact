package de.unileipzig.irpact.commons.util;

import de.unileipzig.irpact.commons.util.Rnd;

import java.util.function.BinaryOperator;

/**
 * @author Daniel Abitz
 */
public class RndElementReducer<T> implements BinaryOperator<T> {

    protected int currentIndex = 0;
    protected int returnIndex;
    protected boolean finished = false;

    public RndElementReducer(Rnd rnd, int size) {
        this(rnd.nextInt(size));
    }

    public RndElementReducer(int returnIndex) {
        this.returnIndex = returnIndex;
    }

    @Override
    public T apply(T t, T t2) {
        if(finished) {
            return t;
        } else {
            if(returnIndex == 0) {
                finished = true;
                return t;
            }
            if(currentIndex == returnIndex - 1) {
                finished = true;
                return t2;
            }
            currentIndex++;
            return null;
        }
    }
}
