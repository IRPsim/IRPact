package de.unileipzig.irpact.core.persistence.binaryjson2.annotation;

import de.unileipzig.irpact.core.persistence.binaryjson2.StandardSettings;
import de.unileipzig.irpact.core.persistence.binaryjson2.functions.MapJsonNodeFunction;
import de.unileipzig.irpact.core.persistence.binaryjson2.functions.MapStringFunction;
import de.unileipzig.irpact.core.persistence.binaryjson2.functions.MapSupplier;
import de.unileipzig.irpact.develop.Todos;

import java.lang.annotation.*;

/**
 * @author Daniel Abitz
 */
@Inherited
@Documented
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Repeatable(PersistMaps.class)
public @interface PersistMap {

    String persister() default StandardSettings.DEFAULT_NAME;

    int order() default StandardSettings.DEFAULT_ORDER;

    Class<? extends MapSupplier> mapSupplier();

    Class<? extends MapStringFunction<?>> keyMapper();

    Class<? extends MapJsonNodeFunction<?>> valueMapper();
}
