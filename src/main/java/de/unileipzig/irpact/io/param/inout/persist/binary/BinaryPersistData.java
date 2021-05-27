package de.unileipzig.irpact.io.param.inout.persist.binary;

import de.unileipzig.irptools.defstructure.annotation.Definition;
import de.unileipzig.irptools.defstructure.annotation.FieldDefinition;
import de.unileipzig.irptools.util.CopyCache;
import de.unileipzig.irptools.util.Copyable;
import de.unileipzig.irptools.util.TreeAnnotationResource;

import java.lang.invoke.MethodHandles;
import java.util.Comparator;

import static de.unileipzig.irpact.io.param.ParamUtil.*;

/**
 * @author Daniel Abitz
 */
@Definition(transferClass = true)
public class BinaryPersistData implements Copyable {

    public static final Comparator<BinaryPersistData> ASCENDING = Comparator.comparingLong(BinaryPersistData::getID);

    private static final MethodHandles.Lookup L = MethodHandles.lookup();
    public static Class<?> thisClass() {
        return L.lookupClass();
    }

    public static void initRes(TreeAnnotationResource res) {
    }
    public static void applyRes(TreeAnnotationResource res) {
        addEntry(res, thisClass());
        addEntry(res, thisClass(), "id");
        setHidden(res, thisClass());
        setHidden(res, thisClass(), "id");
    }

    public String _name;

    @FieldDefinition
    public long id;

    public BinaryPersistData() {
    }

    @Override
    public BinaryPersistData copy(CopyCache cache) {
        return cache.copyIfAbsent(this, this::newCopy);
    }

    public BinaryPersistData newCopy(CopyCache cache) {
        BinaryPersistData copy = new BinaryPersistData();
        copy._name = _name;
        copy.id = id;
        return copy;
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

    public void setIRPBase32String(String irp32) {
        _name = irp32;
    }

    public String getIRPBase32String() {
        return _name;
    }
}
