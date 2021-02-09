package de.unileipzig.irpact.io.input.product;

import de.unileipzig.irptools.defstructure.annotation.Definition;
import de.unileipzig.irptools.defstructure.annotation.Edn;
import de.unileipzig.irptools.defstructure.annotation.EdnParameter;
import de.unileipzig.irptools.defstructure.annotation.FieldDefinition;

import java.util.Arrays;
import java.util.Objects;

/**
 * @author Daniel Abitz
 */
@Definition(
        edn = @Edn(
                label = {"Produkte", "Gruppen"},
                priorities = {"-1", "0"}
        )
)
public class InProductGroup {

    public String _name;

    @FieldDefinition(
            edn = @EdnParameter(
                    label = {"Produkte", "Gruppen", "Attribut-Mapping"},
                    priorities = {"-1", "0", "0"}
            )
    )
    public InProductGroupAttribute[] pgAttributes;

    public InProductGroup() {
    }

    public InProductGroup(String name, InProductGroupAttribute[] attributes) {
        this._name = name;
        this.pgAttributes = attributes;
    }

    public String getName() {
        return _name;
    }

    public InProductGroupAttribute[] getAttributes() {
        return pgAttributes;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof InProductGroup)) return false;
        InProductGroup that = (InProductGroup) o;
        return Objects.equals(_name, that._name) && Arrays.equals(pgAttributes, that.pgAttributes);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(_name);
        result = 31 * result + Arrays.hashCode(pgAttributes);
        return result;
    }

    @Override
    public String toString() {
        return "InProductGroup{" +
                "_name='" + _name + '\'' +
                ", pgAttributes=" + Arrays.toString(pgAttributes) +
                '}';
    }
}
