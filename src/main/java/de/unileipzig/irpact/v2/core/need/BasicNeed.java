package de.unileipzig.irpact.v2.core.need;

import de.unileipzig.irpact.v2.commons.NameableBase;

/**
 * @author Daniel Abitz
 */
public class BasicNeed extends NameableBase implements Need {

    public BasicNeed() {
    }

    public BasicNeed(String name) {
        setName(name);
    }
}
