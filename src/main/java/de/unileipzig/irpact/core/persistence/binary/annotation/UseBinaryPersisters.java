package de.unileipzig.irpact.core.persistence.binary.annotation;

import java.lang.annotation.*;

/**
 * @author Daniel Abitz
 */
@Inherited
@Documented
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface UseBinaryPersisters {

    UseBinaryPersist[] value();
}
