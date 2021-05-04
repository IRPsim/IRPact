package de.unileipzig.irpact.commons.persistence.annotation;

import java.lang.annotation.Repeatable;

/**
 * @author Daniel Abitz
 */
@Repeatable(UsedPersisterRestorers.class)
public @interface UsedPersisterRestorer {

    Class<?> value();
}
