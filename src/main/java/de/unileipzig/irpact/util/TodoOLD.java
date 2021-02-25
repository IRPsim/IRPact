package de.unileipzig.irpact.util;

import java.lang.annotation.Repeatable;

/**
 * @author Daniel Abitz
 */
@Repeatable(TodosOLD.class)
public @interface TodoOLD {

    String value() default "";
}