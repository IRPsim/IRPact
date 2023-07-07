package de.unileipzig.irpact.core.persistence.binaryjson2.annotation;

import de.unileipzig.irpact.core.persistence.binaryjson2.StandardSettings;
import de.unileipzig.irpact.core.persistence.binaryjson2.functions.CollectionSupplier;
import de.unileipzig.irpact.core.persistence.binaryjson2.functions.MapJsonNodeFunction;

import java.lang.annotation.*;

/**
 * @author Daniel Abitz
 */
@Inherited
@Documented
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Repeatable(PersistCollections.class)
public @interface PersistCollection {

    String persister() default StandardSettings.DEFAULT_NAME;

    int order() default StandardSettings.DEFAULT_ORDER;

    Class<? extends CollectionSupplier> collSupplier();

    Class<? extends MapJsonNodeFunction<?>> elementMapper();
}
