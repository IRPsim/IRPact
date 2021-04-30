package de.unileipzig.irpact.commons.persistence;

import java.lang.annotation.Repeatable;

/**
 * @author Daniel Abitz
 */
@Repeatable(UsedRestorers.class)
public @interface UsedRestorer {

    Class<?> value();
}
