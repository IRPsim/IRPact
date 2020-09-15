package de.unileipzig.irpact.start.irpact.input.need;

import de.unileipzig.irpact.core.need.BasicNeed;
import de.unileipzig.irptools.defstructure.annotation.Definition;
import de.unileipzig.irptools.defstructure.annotation.Edn;

import java.util.Objects;

/**
 * @author Daniel Abitz
 */
@Definition(
        edn = @Edn(
                label = "Need"
        )
)
public class Need {

    public String _name;

    public Need() {
    }

    public Need(String name) {
        this._name = name;
    }

    //=========================
    //helper
    //=========================

    public BasicNeed createInstance() {
        return new BasicNeed(_name);
    }

    //=========================
    //default implementaion
    //=========================

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Need need = (Need) o;
        return Objects.equals(_name, need._name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(_name);
    }

    @Override
    public String toString() {
        return "Need{" +
                "_name='" + _name + '\'' +
                '}';
    }
}
