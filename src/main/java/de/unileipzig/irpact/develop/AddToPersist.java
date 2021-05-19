package de.unileipzig.irpact.develop;

/**
 * Reminder to add persist support for the class.
 *
 * @author Daniel Abitz
 */
public @interface AddToPersist {

    String value() default "";
}