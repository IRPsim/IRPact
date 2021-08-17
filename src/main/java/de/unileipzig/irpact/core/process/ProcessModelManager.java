package de.unileipzig.irpact.core.process;

import de.unileipzig.irpact.core.misc.InitalizablePart;

import java.util.Collection;

/**
 * @author Daniel Abitz
 */
public interface ProcessModelManager extends InitalizablePart {

    Collection<ProcessModel> getProcessModels();

    boolean hasProcessModel(String name);

    ProcessModel getProcessModel(String name);
}
