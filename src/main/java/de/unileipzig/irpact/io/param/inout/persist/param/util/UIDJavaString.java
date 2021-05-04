package de.unileipzig.irpact.io.param.inout.persist.param.util;

import de.unileipzig.irpact.commons.util.IRPactBase32;
import de.unileipzig.irptools.defstructure.annotation.Definition;
import de.unileipzig.irptools.defstructure.annotation.FieldDefinition;
import de.unileipzig.irptools.util.TreeAnnotationResource;

import java.util.Objects;

/**
 * @author Daniel Abitz
 */
@Definition
public class UIDJavaString {

    public static void initRes(TreeAnnotationResource res) {
    }
    public static void applyRes(TreeAnnotationResource res) {
    }

    public String _name;

    @FieldDefinition
    public boolean encoded;

    private String decodedName;

    public UIDJavaString() {
    }

    public String getName() {
        return _name;
    }

    public String getDecodedName() {
        if(encoded) {
            if(decodedName == null) {
                decodedName = IRPactBase32.decodeToUtf8(_name);
            }
            return decodedName;
        } else {
            return _name;
        }
    }

    public void initialize(String name, String decodedName) {
        this._name = name;
        this.decodedName = decodedName;
        this.encoded = !Objects.equals(_name, decodedName);
    }
}
