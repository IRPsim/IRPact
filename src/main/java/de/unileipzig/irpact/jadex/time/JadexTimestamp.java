package de.unileipzig.irpact.jadex.time;

import de.unileipzig.irpact.commons.time.Timestamp;
import jadex.bridge.service.annotation.Reference;

/**
 * @author Daniel Abitz
 */
@Reference(remote = true, local = true)
public interface JadexTimestamp extends Timestamp {
}
