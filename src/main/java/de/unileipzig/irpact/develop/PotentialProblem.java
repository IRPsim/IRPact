package de.unileipzig.irpact.develop;

import java.lang.annotation.Repeatable;

/**
 * @author Daniel Abitz
 */
@Repeatable(PotentialProblems.class)
public @interface PotentialProblem {

    String value() default "";
}