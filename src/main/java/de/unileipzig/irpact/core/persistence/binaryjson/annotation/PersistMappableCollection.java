package de.unileipzig.irpact.core.persistence.binaryjson.annotation;

import de.unileipzig.irpact.commons.util.CollectionSupplier;
import de.unileipzig.irpact.core.persistence.binaryjson.GenericPR;

import java.lang.annotation.*;

/**
 * @author Daniel Abitz
 */
@Inherited
@Documented
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Repeatable(PersistMappableCollections.class)
public @interface PersistMappableCollection {

    String persisterName() default GenericPR.IGNORE;

    String restorerName() default GenericPR.IGNORE;

    String getter() default GenericPR.IGNORE;

    String setter() default GenericPR.IGNORE;

    Class<?> type() default IGNORE.class;

    MappingMode mode();

    CollectionSupplier supplier() default CollectionSupplier.ARRAY;
}
