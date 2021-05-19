package de.unileipzig.irpact.commons.persistence.annotation;

import java.lang.annotation.Repeatable;

/**
 * @author Daniel Abitz
 */
@Repeatable(UsedRestorers.class)
public @interface UsedRestorer {

    Class<?> value();
}
