package de.unileipzig.irpact.core.persistence.binary.annotation;

import de.unileipzig.irpact.core.persistence.binary.GenericPR;

import java.lang.annotation.*;

/**
 * @author Daniel Abitz
 */
@Inherited
@Documented
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Repeatable(NonPrimitiveBinaryPersisters.class)
public @interface NonPrimitiveBinaryPersist {

    String persisterName() default GenericPR.IGNORE;

    String restorerName() default GenericPR.IGNORE;

    String setter() default GenericPR.IGNORE;

    String getter() default GenericPR.IGNORE;
}
