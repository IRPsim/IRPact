package de.unileipzig.irpact.core.persistence.binaryjson2.annotation;

import de.unileipzig.irpact.core.persistence.binaryjson2.func.CustomPersistFunction;

import java.lang.annotation.*;

/**
 * @author Daniel Abitz
 */
@Inherited
@Documented
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface PersistCustom {

    int id() default 0;

    Class<? extends CustomPersistFunction<?>> function();
}
