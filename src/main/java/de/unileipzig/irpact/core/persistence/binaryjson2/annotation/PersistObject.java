package de.unileipzig.irpact.core.persistence.binaryjson2.annotation;

import de.unileipzig.irpact.core.persistence.binaryjson2.StandardSettings;

import java.lang.annotation.*;

/**
 * @author Daniel Abitz
 */
@Inherited
@Documented
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Repeatable(PersistObjects.class)
public @interface PersistObject {

    String persister() default StandardSettings.DEFAULT_NAME;

    int order() default StandardSettings.DEFAULT_ORDER;
}
