package de.unileipzig.irpact.experimental.deprecated.input.awareness;

import de.unileipzig.irpact.commons.awareness.Awareness;
import de.unileipzig.irptools.defstructure.annotation.Definition;

/**
 * @author Daniel Abitz
 */
@Definition
public interface IAwareness {

    <T> Awareness<T> createInstance();
}
