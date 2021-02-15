package de.unileipzig.irpact.io.input.binary;

import de.unileipzig.irpact.commons.util.IRPactBase32;
import de.unileipzig.irpact.commons.BinaryData;
import de.unileipzig.irptools.defstructure.annotation.Definition;
import de.unileipzig.irptools.defstructure.annotation.FieldDefinition;
import de.unileipzig.irptools.util.TreeAnnotationResource;

/**
 * @author Daniel Abitz
 */
@Definition
public class VisibleBinaryData {

    public static void initRes(TreeAnnotationResource res) {
        res.newElementBuilder()
                .setEdnLabel("Binäre Daten")
                .setEdnPriority(0)
                .putCache("VisibleBinaryData");
    }
    public static void applyRes(TreeAnnotationResource res) {
        res.putPath(
                VisibleBinaryData.class,
                res.getCachedElement("Allgemeine Einstellungen"),
                res.getCachedElement("Diverses"),
                res.getCachedElement("VisibleBinaryData")
        );

        res.newEntryBuilder()
                .setGamsHidden(false)
                .setGamsIdentifier("VisibleBinaryData")
                .setGamsDescription("Binäre Daten für diverse Funktionalitäten")
                .store(VisibleBinaryData.class);
        res.newEntryBuilder()
                .setGamsHidden(false)
                .setGamsIdentifier("VisibleBinaryDataID")
                .setGamsDescription("Spezielle ID der Daten, Verwendungszweck und Funktionsweise ist von den Daten selber abhängig.")
                .store(VisibleBinaryData.class, "idVisible");
    }

    public String _name;

    @FieldDefinition
    public long idVisible;

    public VisibleBinaryData() {
    }

    public void setID(long id) {
        this.idVisible = id;
    }

    public long getID() {
        return idVisible;
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
            VisibleBinaryData.this.setID(id);
        }

        @Override
        public long getID() {
            return VisibleBinaryData.this.getID();
        }

        @Override
        public void setBytes(byte[] data) {
            VisibleBinaryData.this.setBytes(data);
        }

        @Override
        public byte[] getBytes() {
            return VisibleBinaryData.this.getBytes();
        }
    }
}
