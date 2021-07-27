package de.unileipzig.irpact.core.process.modular.ca.util;

import de.unileipzig.irpact.commons.Nameable;
import de.unileipzig.irpact.commons.time.Timestamp;
import de.unileipzig.irpact.core.util.AdoptionPhase;

/**
 * @author Daniel Abitz
 */
public interface AdoptionPhaseDeterminer extends Nameable {

    AdoptionPhase determine(Timestamp ts);
}
