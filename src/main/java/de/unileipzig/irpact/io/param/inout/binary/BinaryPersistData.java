package de.unileipzig.irpact.io.param.inout.binary;

import de.unileipzig.irpact.commons.util.IRPactBase32;
import de.unileipzig.irptools.defstructure.annotation.Definition;
import de.unileipzig.irptools.defstructure.annotation.FieldDefinition;
import de.unileipzig.irptools.util.TreeAnnotationResource;

import java.lang.invoke.MethodHandles;

/**
 * @author Daniel Abitz
 */
@Definition(
        transferClass = true
)
public class BinaryPersistData {

    //damit ich bei copy&paste nie mehr vergesse die Klasse anzupassen :)
    private static final MethodHandles.Lookup L = MethodHandles.lookup();
    public static Class<?> thisClass() {
        return L.lookupClass();
    }

    public static void initRes(TreeAnnotationResource res) {
    }
    public static void applyRes(TreeAnnotationResource res) {
        res.newEntryBuilder()
                .setGamsHidden(true)
                .setGamsIdentifier("BinaryPersistData")
                .setGamsDescription("Binäre Daten für den Transfer zwischen zwei Simulationsschritten.")
                .store(thisClass());
        res.newEntryBuilder()
                .setGamsHidden(true)
                .setGamsIdentifier("BinaryPersistDataID")
                .setGamsDescription("Einzigartige ID der Daten.")
                .store(thisClass(), "idHidden");
    }

    public String _name;

    @FieldDefinition
    public long id;

    public BinaryPersistData() {
    }

    public String getName() {
        return _name;
    }

    public void setID(long id) {
        this.id = id;
    }

    public long getID() {
        return id;
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
