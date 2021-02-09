package de.unileipzig.irpact.io.input.awareness;

import de.unileipzig.irpact.commons.awareness.Awareness;
import de.unileipzig.irptools.defstructure.annotation.Definition;

/**
 * @author Daniel Abitz
 */
//TODO
@Definition
public interface InAwareness {

    <T> Awareness<T> createInstance();
}