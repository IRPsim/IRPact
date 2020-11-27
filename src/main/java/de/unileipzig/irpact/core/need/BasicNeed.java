package de.unileipzig.irpact.core.need;

import de.unileipzig.irpact.commons.NameableBase;

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
