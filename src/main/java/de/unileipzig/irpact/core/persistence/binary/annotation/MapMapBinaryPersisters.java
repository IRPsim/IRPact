package de.unileipzig.irpact.core.persistence.binary.annotation;

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
