package de.unileipzig.irpact.commons.logging;

/**
 * @author Daniel Abitz
 */
public interface ProgressNotifier {

    boolean isFinished();

    default boolean isNotFinished() {
        return !isFinished();
    }

    default void update() {
        update(1);
    }

    void update(long count);
}
