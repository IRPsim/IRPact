package de.unileipzig.irpact.io.param.inout.persist.binary;

import de.unileipzig.irpact.develop.Todo;
import de.unileipzig.irpact.io.param.ParamUtil;
import de.unileipzig.irptools.Constants;
import de.unileipzig.irptools.defstructure.annotation.*;
import de.unileipzig.irptools.util.CopyCache;
import de.unileipzig.irptools.util.Copyable;
import de.unileipzig.irptools.util.TreeAnnotationResource;

import java.lang.invoke.MethodHandles;
import java.util.Comparator;

/**
 * @author Daniel Abitz
 */
@Definition(
        transferClass = true,
        gams = @Gams(
                hidden = Constants.TRUE1
        )
)
@Todo("add to loc?")
public class BinaryPersistData implements Copyable {

    public static final Comparator<BinaryPersistData> ASCENDING = Comparator.comparingLong(BinaryPersistData::getID);

    private static final MethodHandles.Lookup L = MethodHandles.lookup();
    public static Class<?> thisClass() {
        return L.lookupClass();
    }

    @TreeAnnotationResource.Init
    public static void initRes(TreeAnnotationResource res) {
    }
    @TreeAnnotationResource.Apply
    public static void applyRes(TreeAnnotationResource res) {
    }

    public static String deriveSetName() {
        return Constants.SET + ParamUtil.getClassNameWithoutClassSuffix(thisClass());
    }

    @DefinitionName
    public String name;

    @FieldDefinition(
            gams = @GamsParameter(
                    hidden = Constants.TRUE1
            )
    )
    public long id;

    public BinaryPersistData() {
    }

    @Override
    public BinaryPersistData copy(CopyCache cache) {
        return cache.copyIfAbsent(this, this::newCopy);
    }

    public BinaryPersistData newCopy(CopyCache cache) {
        BinaryPersistData copy = new BinaryPersistData();
        copy.name = name;
        copy.id = id;
        return copy;
    }

    public String getName() {
        return name;
    }

    public void setID(long id) {
        this.id = id;
    }

    public long getID() {
        return id;
    }

    public void setIRPBase32String(String irp32) {
        name = irp32;
    }

    public String getIRPBase32String() {
        return name;
    }
}
