package de.unileipzig.irpact.v2.io.input.awareness;

import de.unileipzig.irpact.v2.commons.awareness.Awareness;
import de.unileipzig.irptools.defstructure.annotation.Definition;

/**
 * @author Daniel Abitz
 */
@Definition
public interface IAwareness {

    <T> Awareness<T> createInstance();
}
