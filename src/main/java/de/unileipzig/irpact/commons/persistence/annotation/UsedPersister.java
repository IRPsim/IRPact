package de.unileipzig.irpact.commons.persistence.annotation;

import java.lang.annotation.Repeatable;

/**
 * @author Daniel Abitz
 */
@Repeatable(UsedPersisters.class)
public @interface UsedPersister {

    Class<?> value();
}
