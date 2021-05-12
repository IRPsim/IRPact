package de.unileipzig.irpact.io.param.input.binary;

import de.unileipzig.irpact.commons.util.IRPactBase32;
import de.unileipzig.irpact.commons.util.data.BinaryData;
import de.unileipzig.irpact.io.param.input.InEntity;
import de.unileipzig.irpact.io.param.input.file.InSpatialTableFile;
import de.unileipzig.irptools.defstructure.annotation.Definition;
import de.unileipzig.irptools.defstructure.annotation.FieldDefinition;
import de.unileipzig.irptools.util.CopyCache;
import de.unileipzig.irptools.util.TreeAnnotationResource;

import java.lang.invoke.MethodHandles;

import static de.unileipzig.irpact.io.param.IOConstants.*;
import static de.unileipzig.irpact.io.param.ParamUtil.addEntry;
import static de.unileipzig.irpact.io.param.ParamUtil.putClassPath;

/**
 * @author Daniel Abitz
 */
@Definition
public class VisibleBinaryData implements InEntity {

    private static final MethodHandles.Lookup L = MethodHandles.lookup();
    public static Class<?> thisClass() {
        return L.lookupClass();
    }
    public static String thisName() {
        return thisClass().getSimpleName();
    }

    public static void initRes(TreeAnnotationResource res) {
    }
    public static void applyRes(TreeAnnotationResource res) {
        putClassPath(res, thisClass(), GENERAL_SETTINGS, SPECIAL_SETTINGS, thisName());
        addEntry(res, thisClass());
        addEntry(res, thisClass(), "idVisible");
    }

    public String _name;

    @FieldDefinition
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
        copy._name = _name;
        copy.idVisible = idVisible;
        return copy;
    }

    @Override
    public String getName() {
        return _name;
    }

    public void setName(String name) {
        this._name = name;
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
