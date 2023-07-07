package de.unileipzig.irpact.core.persistence.binaryjson2.annotation;

import java.lang.annotation.*;

/**
 * @author Daniel Abitz
 */
@Inherited
@Documented
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface PersistObjects {

    PersistObject[] value();
}
