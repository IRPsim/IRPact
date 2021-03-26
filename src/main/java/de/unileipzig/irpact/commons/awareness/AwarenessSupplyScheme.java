package de.unileipzig.irpact.commons.awareness;

import de.unileipzig.irpact.core.misc.Scheme;

/**
 * @author Daniel Abitz
 */
public interface AwarenessSupplyScheme<T> extends Scheme {

    Awareness<T> derive();
}
