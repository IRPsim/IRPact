package de.unileipzig.irpact.core.persistence.binaryjson2.annotation;

import de.unileipzig.irpact.core.persistence.binaryjson2.StandardSettings;
import de.unileipzig.irpact.core.persistence.binaryjson2.functions.CustomPersistFunction;

import java.lang.annotation.*;

/**
 * @author Daniel Abitz
 */
@Inherited
@Documented
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Repeatable(PersistCustoms.class)
public @interface PersistCustom {

    String persister() default StandardSettings.DEFAULT_NAME;

    int order() default StandardSettings.DEFAULT_ORDER;

    Class<? extends CustomPersistFunction<?>> function();
}
