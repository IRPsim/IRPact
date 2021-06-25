package de.unileipzig.irpact.core.persistence.binaryjson.annotation;

import java.lang.annotation.*;

/**
 * @author Daniel Abitz
 */
@Inherited
@Documented
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface MapMapBinaryPersisters {

    MapMapBinaryPersist[] value();
}
