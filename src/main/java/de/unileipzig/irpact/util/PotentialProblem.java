package de.unileipzig.irpact.util;

import java.lang.annotation.Repeatable;

/**
 * @author Daniel Abitz
 */
@Repeatable(PotentialProblems.class)
public @interface PotentialProblem {

    String value() default "";
}