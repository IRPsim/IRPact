package de.unileipzig.irpact.core.process2.modular.modules.core;

import de.unileipzig.irpact.core.process2.PostAction2;

import java.util.List;

/**
 * @author Daniel Abitz
 */
public interface CalculationModule2<I> extends Module2<I, Number> {

    @Override
    default Double apply(I input, List<PostAction2> actions) throws Throwable {
        return calculate(input, actions);
    }

    double calculate(I input, List<PostAction2> actions) throws Throwable;
}
