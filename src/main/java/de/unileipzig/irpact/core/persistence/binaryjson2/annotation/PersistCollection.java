package de.unileipzig.irpact.core.persistence.binaryjson2.annotation;

import de.unileipzig.irpact.core.persistence.binaryjson2.func.CollectionSupplier;
import de.unileipzig.irpact.core.persistence.binaryjson2.func.MapJsonNodeFunction;

import java.lang.annotation.*;

/**
 * @author Daniel Abitz
 */
@Inherited
@Documented
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface PersistCollection {

    int id() default 0;

    Class<? extends CollectionSupplier> collSupplier();

    Class<? extends MapJsonNodeFunction<?>> elementMapper();
}
