package de.unileipzig.irpact.start.irpact.output;

import de.unileipzig.irpact.start.irpact.input.agent.AgentGroup;
import de.unileipzig.irpact.start.irpact.input.need.Need;
import de.unileipzig.irptools.defstructure.annotation.Definition;
import de.unileipzig.irptools.defstructure.annotation.FieldDefinition;

import java.util.Objects;

/**
 * @author Daniel Abitz
 */
@Definition
public class AdaptionRate {

    public String _name;

    @FieldDefinition
    public AgentGroup group;

    @FieldDefinition
    public Need need;

    @FieldDefinition
    public double rate;

    public AdaptionRate() {
    }

    public AdaptionRate(String name, AgentGroup group, Need need, double rate) {
        this._name = name;
        this.group = group;
        this.need = need;
        this.rate = rate;
    }

    //=========================
    //default implementaion
    //=========================

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AdaptionRate that = (AdaptionRate) o;
        return Double.compare(that.rate, rate) == 0 &&
                Objects.equals(_name, that._name) &&
                Objects.equals(group, that.group) &&
                Objects.equals(need, that.need);
    }

    @Override
    public int hashCode() {
        return Objects.hash(_name, group, need, rate);
    }

    @Override
    public String toString() {
        return "AdaptionRate{" +
                "_name='" + _name + '\'' +
                ", group=" + group +
                ", need=" + need +
                ", rate=" + rate +
                '}';
    }
}
