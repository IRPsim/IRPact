package de.unileipzig.irpact.io.param.input.binary;

import de.unileipzig.irpact.commons.util.irpact32.IRPactBase32;
import de.unileipzig.irpact.commons.util.data.BinaryData;
import de.unileipzig.irpact.io.param.LocalizedUiResource;
import de.unileipzig.irpact.io.param.input.InIRPactEntity;
import de.unileipzig.irptools.defstructure.annotation.Definition;
import de.unileipzig.irptools.defstructure.annotation.DefinitionName;
import de.unileipzig.irptools.defstructure.annotation.FieldDefinition;
import de.unileipzig.irptools.util.CopyCache;
import de.unileipzig.irptools.util.TreeAnnotationResource;

import java.lang.invoke.MethodHandles;

import static de.unileipzig.irpact.io.param.input.TreeViewStructureEnum.SETT_SPECIAL_BIN;

/**
 * @author Daniel Abitz
 */
@Definition
@LocalizedUiResource.PutClassPath(SETT_SPECIAL_BIN)
public class VisibleBinaryData implements InIRPactEntity {

    private static final MethodHandles.Lookup L = MethodHandles.lookup();
    public static Class<?> thisClass() {
        return L.lookupClass();
    }
    public static String thisName() {
        return thisClass().getSimpleName();
    }

    @TreeAnnotationResource.Init
    public static void initRes(TreeAnnotationResource res) {
    }
    @TreeAnnotationResource.Apply
    public static void applyRes(TreeAnnotationResource res) {
    }

    @DefinitionName
    public String name;

    @FieldDefinition
    @LocalizedUiResource.AddEntry
    public long idVisible;

    public VisibleBinaryData() {
    }

    public VisibleBinaryData(String name, long id) {
        setName(name);
        setID(id);
    }

    @Override
    public VisibleBinaryData copy(CopyCache cache) {
        return cache.copyIfAbsent(this, this::newCopy);
    }

    public VisibleBinaryData newCopy(CopyCache cache) {
        VisibleBinaryData copy = new VisibleBinaryData();
        copy.name = name;
        copy.idVisible = idVisible;
        return copy;
    }

    @Override
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setID(long id) {
        this.idVisible = id;
    }

    public long getID() {
        return idVisible;
    }

    public void setBytes(byte[] data) {
        name = IRPactBase32.encodeToString(data);
    }

    public byte[] getBytes() {
        return name == null
                ? null
                : IRPactBase32.decode(name);
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
