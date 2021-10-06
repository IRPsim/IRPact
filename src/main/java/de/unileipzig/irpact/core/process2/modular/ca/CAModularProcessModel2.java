package de.unileipzig.irpact.core.process2.modular.ca;

import de.unileipzig.irpact.commons.time.Timestamp;
import de.unileipzig.irpact.core.process2.modular.ModularProcessModel2;
import de.unileipzig.irpact.core.util.AdoptionPhase;

/**
 * @author Daniel Abitz
 */
public interface CAModularProcessModel2 extends ModularProcessModel2 {

    AdoptionPhase determinePhase(Timestamp ts);
}
