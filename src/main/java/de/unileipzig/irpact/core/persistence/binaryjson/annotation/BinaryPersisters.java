package de.unileipzig.irpact.core.persistence.binaryjson.annotation;

import java.lang.annotation.*;

/**
 * @author Daniel Abitz
 */
@Inherited
@Documented
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface BinaryPersisters {

    BinaryPersist[] value();
}
