package de.unileipzig.irpact.io.param.inout.persistance;

import de.unileipzig.irptools.defstructure.annotation.Definition;
import de.unileipzig.irptools.defstructure.annotation.FieldDefinition;

import java.util.Collection;

/**
 * @author Daniel Abitz
 */
@Definition
public class UIDList implements UIDElement {

    public String _name;

    @FieldDefinition
    public long uid;

    @FieldDefinition
    public UIDElement[] list;

    public UIDList() {
    }

    public UIDList(long uid, Collection<? extends UIDElement> elements) {
        setUID(uid);
        setList(elements);
    }

    public void setUID(long uid) {
        this._name = UIDElement.str(uid);
        this.uid = uid;
    }

    @Override
    public long getUID() {
        return uid;
    }

    public void setList(Collection<? extends UIDElement> elements) {
        list = elements.toArray(new UIDElement[0]);
    }
}
