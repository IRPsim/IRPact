package de.unileipzig.irpact.util.script;

/**
 * @author Daniel Abitz
 */
public interface Engine {

    String printCommand();

    String printVersion();

    default boolean isUsable() {
        return isUsable(false);
    }

    boolean isUsable(boolean tryAgain);
}
