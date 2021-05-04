package de.unileipzig.irpact.commons.attribute.v2;

/**
 * @author Daniel Abitz
 */
public abstract class AbstractValueAttribute extends AbstractAttribute implements ValueAttribute {

    protected static <R> R castTo(
            Class<R> type,
            Object input) {
        if(input == null) {
            return null;
        }
        if(type.isInstance(input)) {
            return type.cast(input);
        }
        throw new IllegalArgumentException("wrong input type");
    }

    @Override
    public String toString() {
        return "{" + getName() + "=" + getValueAsString() + "}";
    }
}
