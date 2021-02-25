package de.unileipzig.irpact.core.process;

import de.unileipzig.irpact.core.misc.Initialization;

import java.util.Collection;

/**
 * @author Daniel Abitz
 */
public interface ProcessModelManager extends Initialization {

    Collection<ProcessModel> getProcessModels();

    boolean hasProcessModel(String name);

    ProcessModel getProcessModel(String name);
}
