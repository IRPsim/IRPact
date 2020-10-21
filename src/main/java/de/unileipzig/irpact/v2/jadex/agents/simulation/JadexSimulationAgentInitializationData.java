package de.unileipzig.irpact.v2.jadex.agents.simulation;

import de.unileipzig.irpact.v2.core.misc.InitializationData;
import de.unileipzig.irpact.v2.def.ToDo;
import jadex.bridge.service.annotation.Reference;

/**
 * @author Daniel Abitz
 */
@Reference(local = true, remote = true)
public class JadexSimulationAgentInitializationData implements InitializationData {

    @ToDo("abstract stuff einbauen")
    @Override
    public String getName() {
        return null;
    }
}
