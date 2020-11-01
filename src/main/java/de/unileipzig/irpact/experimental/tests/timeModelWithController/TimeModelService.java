package de.unileipzig.irpact.experimental.tests.timeModelWithController;

import de.unileipzig.irpact.v2.jadex.simulation.JadexSimulationControl;
import de.unileipzig.irpact.v2.jadex.time.JadexTimeModel;

/**
 * @author Daniel Abitz
 */
public interface TimeModelService {

    JadexTimeModel getTimeModel();

    JadexSimulationControl getControl();
}
