package de.unileipzig.irpact.jadex.persistance.binary.annotation;

import de.unileipzig.irpact.jadex.persistance.binary.GenericPR;

import java.lang.annotation.*;

/**
 * @author Daniel Abitz
 */
@Inherited
@Documented
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Repeatable(UseBinaryPersisters.class)
public @interface UseBinaryPersist {

    String persisterName() default GenericPR.IGNORE;

    String restorerName() default GenericPR.IGNORE;
}
