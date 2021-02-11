package de.unileipzig.irpact.core.log;

/**
 * @author Daniel Abitz
 */
public final class LoggingPart {

    private static int id = 0;
    private static int nextId() {
        return id++;
    }

    public static final int PARAMETER = nextId();
    public static final int PLATFORM = nextId();
    public static final int AGENT = nextId();
    public static final int NETWORK = nextId();

    private LoggingPart() {
    }
}
