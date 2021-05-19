package de.unileipzig.irpact.commons.persistence.annotation;

/**
 * Simple marker annotation to mark values for persisting.
 *
 * @author Daniel Abitz
 */
public @interface PersistentValue {

    String value() default "";
}
