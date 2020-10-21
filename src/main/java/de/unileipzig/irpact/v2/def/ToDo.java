package de.unileipzig.irpact.v2.def;

import java.lang.annotation.Repeatable;

/**
 * @author Daniel Abitz
 */
@Repeatable(ToDos.class)
public @interface ToDo {

    String value() default "";
}