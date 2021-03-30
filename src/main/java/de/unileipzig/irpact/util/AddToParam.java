package de.unileipzig.irpact.util;

/**
 * Reminder to add the something to param.
 *
 * @author Daniel Abitz
 */
public @interface AddToParam {

    String value() default "";
}