package de.unileipzig.irpact.commons.checksum;

import java.lang.annotation.*;

/**
 * Simple marker annotation to mark values for checksum calculation.
 *
 * @author Daniel Abitz
 */
@Inherited
@Documented
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ChecksumValue {

    boolean declaredOnly() default true;
}
