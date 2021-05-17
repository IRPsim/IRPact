package de.unileipzig.irpact.io.param.output;

import de.unileipzig.irptools.defstructure.annotation.Definition;
import de.unileipzig.irptools.util.CopyCache;
import de.unileipzig.irptools.util.Copyable;
import de.unileipzig.irptools.util.TreeAnnotationResource;

import java.lang.invoke.MethodHandles;

/**
 * @author Daniel Abitz
 */
@Definition(global = true)
public class OutGeneral implements Copyable {

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
    }

    public OutGeneral() {
    }

    @Override
    public OutGeneral copy(CopyCache cache) {
        return cache.copyIfAbsent(this, this::newCopy);
    }

    public OutGeneral newCopy(CopyCache cache) {
        return new OutGeneral();
    }
}
