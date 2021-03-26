package de.unileipzig.irpact.core.need;

import de.unileipzig.irpact.commons.NameableBase;
import de.unileipzig.irpact.util.Todo;

import java.util.Objects;

/**
 * @author Daniel Abitz
 */
@Todo("initial need !!!")
public class BasicNeed extends NameableBase implements Need {

    public BasicNeed() {
    }

    public BasicNeed(String name) {
        setName(name);
    }

    @Override
    public int getChecksum() {
        return Objects.hashCode(getName());
    }

    @Override
    public String toString() {
        return name;
    }
}
