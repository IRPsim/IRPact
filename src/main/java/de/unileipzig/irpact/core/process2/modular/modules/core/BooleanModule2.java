package de.unileipzig.irpact.core.process2.modular.modules.core;

import de.unileipzig.irpact.core.process2.PostAction2;

import java.util.List;

/**
 * @author Daniel Abitz
 */
public interface BooleanModule2<I> extends Module2<I, Boolean> {

    @Override
    default Boolean apply(I input, List<PostAction2> actions) throws Throwable {
        return test(input, actions);
    }

    boolean test(I input, List<PostAction2> actions) throws Throwable;
}
