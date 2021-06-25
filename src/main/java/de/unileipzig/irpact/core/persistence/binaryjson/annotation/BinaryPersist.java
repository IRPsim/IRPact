package de.unileipzig.irpact.core.persistence.binaryjson.annotation;

import de.unileipzig.irpact.core.persistence.binaryjson.GenericPR;

import java.lang.annotation.*;

/**
 * @author Daniel Abitz
 */
@Inherited
@Documented
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Repeatable(BinaryPersisters.class)
public @interface BinaryPersist {

    String persisterName() default GenericPR.IGNORE;

    String restorerName() default GenericPR.IGNORE;

    boolean enabled() default true;
}
