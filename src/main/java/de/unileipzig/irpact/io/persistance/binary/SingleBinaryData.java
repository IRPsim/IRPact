package de.unileipzig.irpact.io.persistance.binary;

import de.unileipzig.irpact.commons.util.IRPactBase32;
import de.unileipzig.irptools.defstructure.annotation.Definition;
import de.unileipzig.irptools.defstructure.annotation.FieldDefinition;

/**
 * @author Daniel Abitz
 */
@Definition
public class SingleBinaryData {

    public String _name;

    @FieldDefinition
    public long uid;

    public SingleBinaryData() {
    }

    public void setUID(long uid) {
        this.uid = uid;
    }

    public long getUID() {
        return uid;
    }

    public void setBytes(byte[] data) {
        _name = IRPactBase32.encodeToString(data);
    }

    public byte[] getBytes() {
        return _name == null
                ? null
                : IRPactBase32.decodeString(_name);
    }
}
