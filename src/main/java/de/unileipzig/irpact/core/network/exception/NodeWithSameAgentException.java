package de.unileipzig.irpact.core.network.exception;

/**
 * @author Daniel Abitz
 */
public class NodeWithSameAgentException extends RuntimeException {

    public NodeWithSameAgentException() {
        super();
    }

    public NodeWithSameAgentException(String msg) {
        super(msg);
    }
}
