package de.unileipzig.irpact.commons.exception;

/**
 * @author Daniel Abitz
 */
public class UncheckedInterruptedException extends UncheckedException {

    public UncheckedInterruptedException(InterruptedException e) {
        super(e);
    }

    public UncheckedInterruptedException(String msg, InterruptedException e) {
        super(msg, e);
    }
}
