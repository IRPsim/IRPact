package de.unileipzig.irpact.commons.persistence.annotation;

import java.lang.annotation.Repeatable;

/**
 * Simple marker annotation to mark classes for persisting and restoring.
 *
 * @author Daniel Abitz
 */
@Repeatable(UsedPersisterRestorers.class)
public @interface UsedPersisterRestorer {

    Class<?> value();
}
