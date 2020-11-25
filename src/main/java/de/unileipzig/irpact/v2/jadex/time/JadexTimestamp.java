package de.unileipzig.irpact.v2.jadex.time;

import de.unileipzig.irpact.v2.commons.time.Timestamp;
import jadex.bridge.service.annotation.Reference;

/**
 * @author Daniel Abitz
 */
@Reference(remote = true, local = true)
public interface JadexTimestamp extends Timestamp {
}
