package de.unileipzig.irpact.commons.attribute.v2;

/**
 * @author Daniel Abitz
 */
public class GroupEntityStringAttribute<G> extends BasicStringAttribute implements GroupEntityValueAttribute<G> {

    protected G group;

    public GroupEntityStringAttribute() {
        super();
    }

    public GroupEntityStringAttribute(String name, G group, String value) {
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
