package de.unileipzig.irpact.develop;

/**
 * Reminder to add the class to the persist.
 *
 * @author Daniel Abitz
 */
public @interface AddToPersist {

    String value() default "";
}