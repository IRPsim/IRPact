package de.unileipzig.irpact.io.param.inout.persistance;

import de.unileipzig.irpact.commons.util.IRPactBase32;
import de.unileipzig.irptools.defstructure.annotation.Definition;
import de.unileipzig.irptools.defstructure.annotation.FieldDefinition;

import java.util.Objects;

/**
 * @author Daniel Abitz
 */
@Definition
public class UIDName implements UIDElement {

    public String _name;

    @FieldDefinition
    public long uid;

    @FieldDefinition
    public boolean encoded;

    private String decodedName;

    public UIDName() {
    }

    @Override
    public long getUID() {
        return uid;
    }

    public String getName() {
        return _name;
    }

    public String getDecodedName() {
        if(encoded) {
            if(decodedName == null) {
                decodedName = IRPactBase32.base32ToUtf8(_name);
            }
            return decodedName;
        } else {
            return _name;
        }
    }

    public void setAll(long id, String name, String decodedName) {
        this.uid = id;
        this._name = name;
        this.decodedName = decodedName;
        this.encoded = !Objects.equals(_name, decodedName);
    }
}
