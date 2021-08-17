package de.unileipzig.irpact.commons.exception;

/**
 * @author Daniel Abitz
 */
public class ParsingException extends IRPactException {

    public ParsingException() {
        super();
    }

    public ParsingException(String msg) {
        super(msg);
    }

    public ParsingException(String pattern, Object arg) {
        super(pattern, arg);
    }

    public ParsingException(String pattern, Object arg1, Object arg2) {
        super(pattern, arg1, arg2);
    }

    public ParsingException(String pattern, Object... args) {
        super(pattern, args);
    }

    public ParsingException(Throwable cause) {
        super(cause);
    }

    public ParsingException(Throwable cause, String msg) {
        super(cause, msg);
    }

    public ParsingException(Throwable cause, String pattern, Object arg) {
        super(cause, pattern, arg);
    }

    public ParsingException(Throwable cause, String pattern, Object arg1, Object arg2) {
        super(cause, pattern, arg1, arg2);
    }

    public ParsingException(Throwable cause, String pattern, Object... args) {
        super(cause, pattern, args);
    }

    @Override
    public UncheckedParsingException unchecked() {
        return new UncheckedParsingException(this);
    }
}
