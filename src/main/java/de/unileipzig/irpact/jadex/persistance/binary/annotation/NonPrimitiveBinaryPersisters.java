package de.unileipzig.irpact.jadex.persistance.binary.annotation;

import java.lang.annotation.*;

/**
 * @author Daniel Abitz
 */
@Inherited
@Documented
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface NonPrimitiveBinaryPersisters {

    NonPrimitiveBinaryPersist[] value();
}
