package de.unileipzig.irpact.commons.persistence.annotation;

/**
 * @author Daniel Abitz
 */
public @interface UsedPersisterRestorers {

    UsedPersisterRestorer[] value();
}
