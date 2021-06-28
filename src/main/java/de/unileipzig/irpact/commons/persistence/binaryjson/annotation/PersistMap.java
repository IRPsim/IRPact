package de.unileipzig.irpact.commons.persistence.binaryjson.annotation;

import de.unileipzig.irpact.core.persistence.binaryjson.GenericPR;

import java.lang.annotation.*;

/**
 * @author Daniel Abitz
 */
@Inherited
@Documented
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Repeatable(PersistMaps.class)
public @interface PersistMap {

    String persisterName() default GenericPR.IGNORE;

    String restorerName() default GenericPR.IGNORE;

    String getter() default GenericPR.IGNORE;

    MappingMode keyMode();

    MappingMode valueMode();
}
