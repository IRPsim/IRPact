package de.unileipzig.irpact.core.util.v2;

import de.unileipzig.irpact.commons.util.data.VarCollection;

import java.util.function.BinaryOperator;
import java.util.function.Supplier;

/**
 * @author Daniel Abitz
 */
public abstract class AbstractAdoptionAnalyser implements AdoptionAnalyser {

    protected static final Supplier<Integer> ZERO = () -> 0;
    protected static final Supplier<Integer> ONE = () -> 1;
    protected static final BinaryOperator<Integer> ADD = Integer::sum;

    protected final VarCollection data;

    public AbstractAdoptionAnalyser(VarCollection data) {
        this.data = data;
    }

    @Override
    public VarCollection getData() {
        return data;
    }

    protected String findString(AdoptionInfo info, String key) {
        return info.getAgent()
                .findAttribute(key)
                .asValueAttribute()
                .getStringValue();
    }

    protected double findDouble(AdoptionInfo info, String key) {
        return info.getAgent()
                .findAttribute(key)
                .asValueAttribute()
                .getDoubleValue();
    }
}
