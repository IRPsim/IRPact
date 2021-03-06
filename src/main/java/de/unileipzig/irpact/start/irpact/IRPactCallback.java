package de.unileipzig.irpact.start.irpact;

import de.unileipzig.irpact.commons.Nameable;

/**
 * @author Daniel Abitz
 */
public interface IRPactCallback extends Nameable {

    void onFinished(IRPActAccess access) throws Exception;
}
