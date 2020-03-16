package de.unileipzig.irpact.core.need;

import de.unileipzig.irpact.commons.Check;
import de.unileipzig.irpact.commons.annotation.Idea;

import java.util.Objects;

/**
 * @author Daniel Abitz
 */
@Idea("wenn preference eingefuhrt wird uaf value basis, dann auch value need einfuehren")
public final class BasicNeed implements Need {

    private String name;

    public BasicNeed(String name) {
        this.name = Check.requireNonNull(name, "name");
    }

    public String getName() {
        return name;
    }

    @Override
    public String print() {
        return name;
    }

    @Override
    public String toString() {
        return name;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(name);
    }

    @Override
    public boolean equals(Object obj) {
        if(obj == null) return false;
        if(obj == this) return true;
        if(obj instanceof BasicNeed) {
            BasicNeed other = (BasicNeed) obj;
            return Objects.equals(other.name, name);
        }
        return false;
    }
}
