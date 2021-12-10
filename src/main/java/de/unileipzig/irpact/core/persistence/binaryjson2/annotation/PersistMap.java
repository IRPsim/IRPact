package de.unileipzig.irpact.core.persistence.binaryjson2.annotation;

import de.unileipzig.irpact.core.persistence.binaryjson2.func.MapJsonNodeFunction;
import de.unileipzig.irpact.core.persistence.binaryjson2.func.MapStringFunction;
import de.unileipzig.irpact.core.persistence.binaryjson2.func.MapSupplier;

import java.lang.annotation.*;

/**
 * @author Daniel Abitz
 */
@Inherited
@Documented
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface PersistMap {

    int id() default 0;

    Class<? extends MapSupplier> mapSupplier();

    Class<? extends MapStringFunction<?>> keyMapper();

    Class<? extends MapJsonNodeFunction<?>> valueMapper();
}
