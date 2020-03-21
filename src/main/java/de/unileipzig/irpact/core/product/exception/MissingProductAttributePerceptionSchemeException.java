package de.unileipzig.irpact.core.product.exception;

/**
 * @author Daniel Abitz
 */
@Deprecated
public class MissingProductAttributePerceptionSchemeException extends RuntimeException {

    public MissingProductAttributePerceptionSchemeException() {
        super();
    }

    public MissingProductAttributePerceptionSchemeException(String msg) {
        super(msg);
    }
}
