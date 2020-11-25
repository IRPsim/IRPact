package de.unileipzig.irpact.v2.io.input.time;

import de.unileipzig.irpact.v2.jadex.time.JadexTimeModel;
import de.unileipzig.irptools.defstructure.annotation.Definition;

/**
 * @author Daniel Abitz
 */
@Definition
public interface ITimeModel {

    JadexTimeModel createInstance();
}
