package de.unileipzig.irpact.io.param.input.binary;

import de.unileipzig.irpact.commons.util.IRPactBase32;
import de.unileipzig.irpact.commons.BinaryData;
import de.unileipzig.irpact.io.param.input.InEntity;
import de.unileipzig.irptools.defstructure.annotation.Definition;
import de.unileipzig.irptools.defstructure.annotation.FieldDefinition;
import de.unileipzig.irptools.util.TreeAnnotationResource;

import java.lang.invoke.MethodHandles;

/**
 * @author Daniel Abitz
 */
@Definition
public class VisibleBinaryData implements InEntity {

    //damit ich bei copy&paste nie mehr vergesse die Klasse anzupassen :)
    private static final MethodHandles.Lookup L = MethodHandles.lookup();
    public static Class<?> thisClass() {
        return L.lookupClass();
    }

    public static void initRes(TreeAnnotationResource res) {
    }
    public static void applyRes(TreeAnnotationResource res) {
        res.putPath(
                VisibleBinaryData.class,
                res.getCachedElement("Allgemeine Einstellungen"),
                res.getCachedElement("Spezielle Einstellungen"),
                res.getCachedElement("VisibleBinaryData")
        );

        res.newEntryBuilder()
                .setGamsHidden(false)
                .setGamsIdentifier("Binäre Daten")
                .setGamsDescription("Binäre Daten für diverse Funktionalitäten")
                .store(VisibleBinaryData.class);

        res.newEntryBuilder()
                .setGamsHidden(false)
                .setGamsIdentifier("ID")
                .setGamsDescription("Spezielle ID der Daten, Verwendungszweck und Funktionsweise ist von den Daten selber abhängig.")
                .store(VisibleBinaryData.class, "idVisible");
    }

    public String _name;

    @FieldDefinition
    public long idVisible;

    public VisibleBinaryData() {
    }

    @Override
    public String getName() {
        return _name;
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

        @Override
        public String print() {
            return VisibleBinaryData.this.getName();
        }
    }
}
