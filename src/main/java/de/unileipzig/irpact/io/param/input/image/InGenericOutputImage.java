package de.unileipzig.irpact.io.param.input.image;

import de.unileipzig.irpact.start.irpact.IRPact;
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
public class InGenericOutputImage implements InOutputImage {

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
        addEntry(res, thisClass(), "engine");
        addEntry(res, thisClass(), "mode");
        addEntry(res, thisClass(), "storeScript");
        addEntry(res, thisClass(), "storeData");
        addEntry(res, thisClass(), "storeImage");

        setDefault(res, thisClass(), DEFAULT_MODES);
        setDefault(res, thisClass(), "engine", DEFAULT_ENGINE);
        setDomain(res, thisClass(), "engine", InOutputImage.printEngineDomain());
        setDomain(res, thisClass(), "mode", InOutputImage.printModeDomain());
    }

    public static final InGenericOutputImage ANNUAL_ADOPTIONS = new InGenericOutputImage(IRPact.IMAGE_ANNUAL_ADOPTIONS, ENGINE_GNUPLOT, MODE_ADOPTION_LINECHART);
    public static final InGenericOutputImage COMPARED_ANNUAL_ADOPTIONS = new InGenericOutputImage(IRPact.IMAGE_COMPARED_ANNUAL_ADOPTIONS, ENGINE_GNUPLOT, MODE_ADOPTION_INTERACTION_LINECHART);
    public static final InGenericOutputImage ANNUAL_CUMULATIVE_ADOPTIONS = new InGenericOutputImage(IRPact.IMAGE_ANNUAL_CUMULATIVE_ADOPTIONS, ENGINE_GNUPLOT, MODE_ADOPTION_PHASE_BARCHART);
    public static final InGenericOutputImage[] DEFAULTS = { ANNUAL_ADOPTIONS, COMPARED_ANNUAL_ADOPTIONS, ANNUAL_CUMULATIVE_ADOPTIONS };

    public String _name;

    @FieldDefinition
    public int engine = ENGINE_GNUPLOT;

    @FieldDefinition
    public int mode = MODE_NOTHING;

    @FieldDefinition
    public boolean storeScript = false;

    @FieldDefinition
    public boolean storeData = false;

    @FieldDefinition
    public boolean storeImage = false;

    public InGenericOutputImage() {
    }

    protected InGenericOutputImage(String name, int engine, int mode) {
        setName(name);
        setEngine(engine);
        setMode(mode);
        setStoreImage(true);
        setStoreData(false);
        setStoreScript(false);
    }

    @Override
    public InGenericOutputImage copy(CopyCache cache) {
        return cache.copyIfAbsent(this, this::newCopy);
    }

    public InGenericOutputImage newCopy(CopyCache cache) {
        InGenericOutputImage copy = new InGenericOutputImage();
        copy._name = _name;
        copy.engine = engine;
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

    public void setEngine(int engine) {
        this.engine = engine;
    }

    @Override
    public int getEngine() {
        return engine;
    }

    public void setMode(int mode) {
        this.mode = mode;
    }

    public int getMode() {
        return mode;
    }

    public void setStoreImage(boolean storeImage) {
        this.storeImage = storeImage;
    }

    public boolean isStoreImage() {
        return storeImage;
    }

    public void setStoreData(boolean storeData) {
        this.storeData = storeData;
    }

    public boolean isStoreData() {
        return storeData;
    }

    public void setStoreScript(boolean storeScript) {
        this.storeScript = storeScript;
    }

    public boolean isStoreScript() {
        return storeScript;
    }
}
