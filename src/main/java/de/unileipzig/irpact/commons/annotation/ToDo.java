package de.unileipzig.irpact.commons.annotation;

import java.lang.annotation.Repeatable;

/**
 * @author Daniel Abitz
 */
@Repeatable(ToDos.class)
public @interface ToDo {

    String value() default "";
}