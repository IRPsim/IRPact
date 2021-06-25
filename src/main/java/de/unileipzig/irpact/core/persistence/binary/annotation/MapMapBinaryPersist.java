package de.unileipzig.irpact.core.persistence.binary.annotation;

import de.unileipzig.irpact.commons.util.MapSupplier;
import de.unileipzig.irpact.core.persistence.binary.GenericPR;

import java.lang.annotation.*;

/**
 * @author Daniel Abitz
 */
@Inherited
@Documented
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Repeatable(MapMapBinaryPersisters.class)
public @interface MapMapBinaryPersist {

    String persisterName() default GenericPR.IGNORE;

    String restorerName() default GenericPR.IGNORE;

    String getter() default GenericPR.IGNORE;

    MappingMode firstMode() default MappingMode.UNDEFINED;

    MappingMode secondMode() default MappingMode.UNDEFINED;

    MappingMode thirdMode() default MappingMode.UNDEFINED;

    MapSupplier supplier() default MapSupplier.LINKED;
}
