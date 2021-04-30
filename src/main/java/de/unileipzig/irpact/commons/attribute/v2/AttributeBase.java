package de.unileipzig.irpact.commons.attribute.v2;

import de.unileipzig.irpact.commons.Nameable;

/**
 * @author Daniel Abitz
 */
public interface AttributeBase extends Nameable {

    default AttributeBase copy() {
        throw new UnsupportedOperationException();
    }

    boolean isArtificial();
}
