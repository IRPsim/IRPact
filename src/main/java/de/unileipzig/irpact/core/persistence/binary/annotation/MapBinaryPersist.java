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
@Repeatable(MapBinaryPersisters.class)
public @interface MapBinaryPersist {

    String persisterName() default GenericPR.IGNORE;

    String restorerName() default GenericPR.IGNORE;

    String getter() default GenericPR.IGNORE;

    MappingMode keyMode() default MappingMode.UNDEFINED;

    MappingMode valueMode() default MappingMode.UNDEFINED;
}
