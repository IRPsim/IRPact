package de.unileipzig.irpact.core.persistence.binary.annotation;

import de.unileipzig.irpact.core.persistence.binary.GenericPR;

import java.lang.annotation.*;

/**
 * @author Daniel Abitz
 */
@Inherited
@Documented
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Repeatable(UseBinaryPersisters.class)
public @interface UseBinaryPersist {

    String persisterName() default GenericPR.IGNORE;

    String restorerName() default GenericPR.IGNORE;
}
