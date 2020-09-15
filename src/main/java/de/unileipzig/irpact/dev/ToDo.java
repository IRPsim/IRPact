package de.unileipzig.irpact.dev;

import java.lang.annotation.Repeatable;

/**
 * Name says it all...
 *
 * @author Daniel Abitz
 */
@Repeatable(ToDos.class)
public @interface ToDo {

    String value() default "";
}