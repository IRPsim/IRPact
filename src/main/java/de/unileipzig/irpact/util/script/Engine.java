package de.unileipzig.irpact.util.script;

/**
 * @author Daniel Abitz
 */
public interface Engine {

    String printCommand();

    default boolean isUsable() {
        return isUsable(false);
    }

    boolean isUsable(boolean tryAgain);
}
