package de.unileipzig.irpact.core.postprocessing.data4;

import de.unileipzig.irpact.commons.NameableBase;
import de.unileipzig.irpact.core.process2.modular.SharedModuleData;
import de.unileipzig.irpact.core.process2.modular.ca.ra.RAHelperAPI2;

/**
 * @author Daniel Abitz
 */
public final class RAHelperAPIInstance extends NameableBase implements RAHelperAPI2 {

    public RAHelperAPIInstance(String name) {
        setName(name);
    }

    @Override
    public SharedModuleData getSharedData() {
        throw new UnsupportedOperationException();
    }
}
