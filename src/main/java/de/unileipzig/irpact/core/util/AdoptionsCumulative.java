package de.unileipzig.irpact.core.util;

import de.unileipzig.irpact.commons.util.StringUtil;
import de.unileipzig.irpact.commons.util.data.VarCollection;

import java.util.function.BinaryOperator;
import java.util.function.Supplier;

/**
 * @author Daniel Abitz
 */
public abstract class AdoptionsCumulative {

    protected static final Supplier<Integer> ZERO = () -> 0;
    protected static final BinaryOperator<Integer> ADD = Integer::sum;

    public abstract VarCollection getMapping();

    protected abstract void initHeader(StringBuilder sb);

    public String toCsv() {
        StringBuilder sb = new StringBuilder();
        initHeader(sb);
        for(Object[] entry: getMapping().iterable()) {
            sb.append(StringUtil.concat(",", entry));
            sb.append("\n");
        }
        return sb.toString();
    }
}
