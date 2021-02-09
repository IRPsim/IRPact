package de.unileipzig.irpact.experimental.deprecated.input.time;

import de.unileipzig.irpact.jadex.time.JadexTimeModel;
import de.unileipzig.irptools.defstructure.annotation.Definition;

/**
 * @author Daniel Abitz
 */
@Definition
public interface ITimeModel {

    JadexTimeModel createInstance();
}
