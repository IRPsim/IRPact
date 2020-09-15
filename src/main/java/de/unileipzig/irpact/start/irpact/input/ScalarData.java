package de.unileipzig.irpact.start.irpact.input;

import de.unileipzig.irptools.defstructure.annotation.Definition;
import de.unileipzig.irptools.defstructure.annotation.FieldDefinition;

import java.util.Objects;

/**
 * @author Daniel Abitz
 */
@Definition(
        global = true
)
public class ScalarData {

    @FieldDefinition
    public double placeholder;

    public ScalarData() {
    }

    public ScalarData(double placeholder) {
        this.placeholder = placeholder;
    }

    //=========================
    //default implementaion
    //=========================

    @Override
    public String toString() {
        return "ScalarData{" +
                "placeholder=" + placeholder +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ScalarData that = (ScalarData) o;
        return Double.compare(that.placeholder, placeholder) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(placeholder);
    }
}
