package de.unileipzig.irpact.core.process2.modular.modules.core;

import de.unileipzig.irpact.core.process2.PostAction2;

import java.util.List;

/**
 * @author Daniel Abitz
 */
public interface VoidModule2<I> extends Module2<I, Void> {

    @Override
    default Void apply(I input, List<PostAction2> actions) throws Throwable {
        run(input, actions);
        return null;
    }

    void run(I input, List<PostAction2> actions) throws Throwable;
}
