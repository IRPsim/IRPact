package de.unileipzig.irpact.commons.attribute.v2;

/**
 * @author Daniel Abitz
 */
public class GroupEntityDoubleAttribute<G> extends BasicDoubleAttribute implements GroupEntityValueAttribute<G> {

    protected G group;

    public GroupEntityDoubleAttribute() {
        super();
    }

    public GroupEntityDoubleAttribute(String name, G group, double value) {
        super(name, value);
        setGroup(group);
    }

    public void setGroup(G group) {
        this.group = group;
    }

    @Override
    public G getGroup() {
        return group;
    }
}
