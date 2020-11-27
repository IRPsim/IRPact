package de.unileipzig.irpact.util;

import java.lang.annotation.Repeatable;

/**
 * @author Daniel Abitz
 */
@Repeatable(Todos.class)
public @interface Todo {

    String value() default "";
}