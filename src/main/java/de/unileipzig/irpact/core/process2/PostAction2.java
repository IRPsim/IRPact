package de.unileipzig.irpact.core.process2;

import de.unileipzig.irpact.core.process.PostAction;

/**
 * @author Daniel Abitz
 */
public interface PostAction2 extends PostAction {

    //legacy
    @Override
    default String getInputName() {
        return getName();
    }

    //legacy
    @Override
    default void execute() throws Throwable {
        execute2();
    }

    String getName();

    void execute2() throws Throwable;
}