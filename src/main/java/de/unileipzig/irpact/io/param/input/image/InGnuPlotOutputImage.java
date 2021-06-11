package de.unileipzig.irpact.io.param.input.image;

import de.unileipzig.irptools.defstructure.annotation.Definition;
import de.unileipzig.irptools.defstructure.annotation.FieldDefinition;
import de.unileipzig.irptools.util.CopyCache;
import de.unileipzig.irptools.util.TreeAnnotationResource;

import java.lang.invoke.MethodHandles;

import static de.unileipzig.irpact.io.param.IOConstants.*;
import static de.unileipzig.irpact.io.param.ParamUtil.*;

/**
 * @author Daniel Abitz
 */
@Definition
public class InGnuPlotOutputImage implements InOutputImage {

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
        putClassPath(res, thisClass(), GENERAL_SETTINGS, IMAGE, thisName());
        addEntry(res, thisClass(), "mode");
        addEntry(res, thisClass(), "storeScript");
        addEntry(res, thisClass(), "storeData");
        addEntry(res, thisClass(), "storeImage");
    }

    public String _name;

    @FieldDefinition
    public int mode = MODE_NOTHING;

    @FieldDefinition
    public boolean storeScript = false;

    @FieldDefinition
    public boolean storeData = false;

    @FieldDefinition
    public boolean storeImage = false;

    public InGnuPlotOutputImage() {
    }

    @Override
    public InGnuPlotOutputImage copy(CopyCache cache) {
        return cache.copyIfAbsent(this, this::newCopy);
    }

    public InGnuPlotOutputImage newCopy(CopyCache cache) {
        InGnuPlotOutputImage copy = new InGnuPlotOutputImage();
        copy._name = _name;
        copy.mode = mode;
        copy.storeData = storeData;
        copy.storeScript = storeScript;
        copy.storeImage = storeImage;
        return copy;
    }

    public void enableAll() {
        storeImage = true;
        storeScript = true;
        storeData = true;
    }

    public void setName(String name) {
        this._name = name;
    }

    @Override
    public String getName() {
        return _name;
    }

    @Override
    public int getEngine() {
        return ENGINE_GNUPLOT;
    }

    public void setMode(int mode) {
        this.mode = mode;
    }

    @Override
    public int getMode() {
        return mode;
    }

    public void setStoreImage(boolean storeImage) {
        this.storeImage = storeImage;
    }

    @Override
    public boolean isStoreImage() {
        return storeImage;
    }

    public void setStoreData(boolean storeData) {
        this.storeData = storeData;
    }

    @Override
    public boolean isStoreData() {
        return storeData;
    }

    public void setStoreScript(boolean storeScript) {
        this.storeScript = storeScript;
    }

    @Override
    public boolean isStoreScript() {
        return storeScript;
    }
}
