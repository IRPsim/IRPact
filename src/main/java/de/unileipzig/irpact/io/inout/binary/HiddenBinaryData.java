package de.unileipzig.irpact.io.inout.binary;

import de.unileipzig.irpact.commons.util.IRPactBase32;
import de.unileipzig.irpact.commons.BinaryData;
import de.unileipzig.irptools.defstructure.annotation.Definition;
import de.unileipzig.irptools.defstructure.annotation.FieldDefinition;
import de.unileipzig.irptools.util.TreeAnnotationResource;

/**
 * @author Daniel Abitz
 */
@Definition(
        transferClass = true
)
public class HiddenBinaryData {

    public static void initRes(TreeAnnotationResource res) {
    }
    public static void applyRes(TreeAnnotationResource res) {
        res.newEntryBuilder()
                .setGamsHidden(true)
                .setGamsIdentifier("HiddenBinaryData")
                .setGamsDescription("Binäre Daten für diverse Funktionalitäten. Identisch zu VisibleBinaryData, aber für den internen Austausch gedacht und damit versteckt, um nicht im UI zu erscheinen.")
                .store(HiddenBinaryData.class);
        res.newEntryBuilder()
                .setGamsHidden(true)
                .setGamsIdentifier("HiddenBinaryDataID")
                .setGamsDescription("Spezielle ID der Daten, Verwendungszweck und Funktionsweise ist von den Daten selber abhängig.")
                .store(HiddenBinaryData.class, "idHidden");
    }

    public String _name;

    @FieldDefinition
    public long idHidden;

    public HiddenBinaryData() {
    }

    public void setID(long id) {
        this.idHidden = id;
    }

    public long getID() {
        return idHidden;
    }

    public void setBytes(byte[] data) {
        _name = IRPactBase32.encodeToString(data);
    }

    public byte[] getBytes() {
        return _name == null
                ? null
                : IRPactBase32.decodeString(_name);
    }

    private final BinaryData ACCESS = new BinaryAccess();
    public BinaryData asBinary() {
        return ACCESS;
    }

    /**
     * @author Daniel Abitz
     */
    public class BinaryAccess implements BinaryData {

        @Override
        public void setID(long id) {
            HiddenBinaryData.this.setID(id);
        }

        @Override
        public long getID() {
            return HiddenBinaryData.this.getID();
        }

        @Override
        public void setBytes(byte[] data) {
            HiddenBinaryData.this.setBytes(data);
        }

        @Override
        public byte[] getBytes() {
            return HiddenBinaryData.this.getBytes();
        }
    }
}
