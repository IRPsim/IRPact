package de.unileipzig.irpact.commons.persistence.binaryjson.annotation;

import java.lang.annotation.*;

/**
 * @author Daniel Abitz
 */
@Inherited
@Documented
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface PersistNonPrimitives {

    PersistNonPrimitive[] value();
}
