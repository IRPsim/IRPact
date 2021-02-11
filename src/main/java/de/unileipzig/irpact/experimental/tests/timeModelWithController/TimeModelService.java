package de.unileipzig.irpact.experimental.tests.timeModelWithController;

import de.unileipzig.irpact.jadex.simulation.JadexLifeCycleControl;
import de.unileipzig.irpact.jadex.time.JadexTimeModel;

/**
 * @author Daniel Abitz
 */
public interface TimeModelService {

    JadexTimeModel getTimeModel();

    JadexLifeCycleControl getControl();
}
