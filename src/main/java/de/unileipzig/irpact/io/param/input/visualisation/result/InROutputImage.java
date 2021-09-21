package de.unileipzig.irpact.io.param.input.visualisation.result;

import de.unileipzig.irpact.commons.exception.ParsingException;
import de.unileipzig.irpact.core.postprocessing.image.d2v.DataToVisualize;
import de.unileipzig.irpact.core.postprocessing.image.SupportedEngine;
import de.unileipzig.irpact.io.param.input.InRootUI;
import de.unileipzig.irpact.io.param.input.file.InRealAdoptionDataFile;
import de.unileipzig.irptools.defstructure.annotation.Definition;
import de.unileipzig.irptools.defstructure.annotation.FieldDefinition;
import de.unileipzig.irptools.util.CopyCache;
import de.unileipzig.irptools.util.TreeAnnotationResource;

import java.lang.invoke.MethodHandles;
import java.util.ArrayList;
import java.util.List;

import static de.unileipzig.irpact.io.param.ParamUtil.*;

/**
 * @author Daniel Abitz
 */
@Definition
public class InROutputImage implements InOutputImage {

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
        putClassPath(res, thisClass(), InRootUI.SETT_VISURESULT_R);
        addEntryWithDefaultAndDomain(res, thisClass(), "annualZip", VALUE_TRUE, DOMAIN_BOOLEAN);
        addEntriesWithDefaultAndDomain(res, thisClass(), dataToVisualizeWithoutDefault, VALUE_FALSE, DOMAIN_BOOLEAN);
        addEntryWithDefaultAndDomain(res, thisClass(), "storeScript", VALUE_FALSE, DOMAIN_BOOLEAN);
        addEntryWithDefaultAndDomain(res, thisClass(), "storeData", VALUE_FALSE, DOMAIN_BOOLEAN);
        addEntryWithDefaultAndDomain(res, thisClass(), "storeImage", VALUE_TRUE, DOMAIN_BOOLEAN);
        addEntryWithDefaultAndDomain(res, thisClass(), "imageWidth", VALUE_1280, DOMAIN_G0);
        addEntryWithDefaultAndDomain(res, thisClass(), "imageHeight", VALUE_720, DOMAIN_G0);
        addEntryWithDefaultAndDomain(res, thisClass(), "linewidth", VALUE_1, DOMAIN_G0);
        addEntry(res, thisClass(), "realAdoptionDataFile");

        setRules(res, thisClass(), dataToVisualize, dataToVisualizeBuilder.withKeyModifier(buildDefaultParameterNameOperator(thisClass())));

        setUnit(res, thisClass(), "imageWidth", UNIT_PIXEL);
        setUnit(res, thisClass(), "imageHeight", UNIT_PIXEL);
    }

    public String _name;

    @FieldDefinition
    public boolean annualZip = true;

    @FieldDefinition
    public boolean annualZipWithReal = false;

    @FieldDefinition
    public boolean cumulativeAnnualPhase = false;

    @FieldDefinition
    public boolean cumulativeAnnualPhase2 = false;

    @FieldDefinition
    public boolean annualInterest2D = false;

    @FieldDefinition
    public boolean annualPhaseOverview = false;

    @FieldDefinition
    public boolean storeScript = false;

    @FieldDefinition
    public boolean storeData = false;

    @FieldDefinition
    public boolean storeImage = true;

    @FieldDefinition
    public int imageWidth = 1280;

    @FieldDefinition
    public int imageHeight = 720;

    @FieldDefinition
    public double linewidth = 1;

    @FieldDefinition
    public InRealAdoptionDataFile[] realAdoptionDataFile = new InRealAdoptionDataFile[0];

    public InROutputImage() {
    }

    public InROutputImage(String name) {
        this(name, DataToVisualize.ANNUAL_ZIP);
    }

    public InROutputImage(String name, DataToVisualize mode) {
        setName(name);
        setMode(mode);
        setStoreImage(true);
        setStoreData(false);
        setStoreScript(false);
        setLinewidth(1);
    }

    @Override
    public InROutputImage copy(CopyCache cache) {
        return cache.copyIfAbsent(this, this::newCopy);
    }

    public InROutputImage newCopy(CopyCache cache) {
        InROutputImage copy = new InROutputImage();
        copy._name = _name;
        copy.annualZip = annualZip;
        copy.annualZipWithReal = annualZipWithReal;
        copy.cumulativeAnnualPhase = cumulativeAnnualPhase;
        copy.cumulativeAnnualPhase2 = cumulativeAnnualPhase2;
        copy.annualInterest2D = annualInterest2D;
        copy.annualPhaseOverview = annualPhaseOverview;
        copy.storeData = storeData;
        copy.storeScript = storeScript;
        copy.storeImage = storeImage;
        copy.imageWidth = imageWidth;
        copy.imageHeight = imageHeight;
        copy.linewidth = linewidth;
        return copy;
    }

    @Override
    public void setEnableAll(boolean enableAll) {
        storeImage = enableAll;
        storeScript = enableAll;
        storeData = enableAll;
    }

    public void setName(String name) {
        this._name = name;
    }

    @Override
    public String getName() {
        return _name;
    }

    @Override
    public SupportedEngine getEngine() {
        return SupportedEngine.R;
    }

    public void setMode(DataToVisualize mode) {
        annualZip = false;
        annualZipWithReal = false;
        cumulativeAnnualPhase = false;
        cumulativeAnnualPhase2 = false;
        annualInterest2D = false;
        annualPhaseOverview = false;

        switch(mode) {
            case ANNUAL_ZIP:
                annualZip = true;
                break;

            case COMPARED_ANNUAL_ZIP:
                annualZipWithReal = true;
                break;

            case CUMULATIVE_ANNUAL_PHASE:
                cumulativeAnnualPhase = true;
                break;

            case CUMULATIVE_ANNUAL_PHASE_WITH_INITIAL:
                cumulativeAnnualPhase2 = true;
                break;

            case ANNUAL_INTEREST_2D:
                annualInterest2D = true;
                break;

            case ANNUAL_PHASE_OVERVIEW:
                annualPhaseOverview = true;
                break;

            default:
                throw new IllegalArgumentException("unsupported mode: " + mode);
        }
    }

    @Override
    public DataToVisualize getMode() throws ParsingException {
        List<DataToVisualize> modes = new ArrayList<>();
        if(annualZip) modes.add(DataToVisualize.ANNUAL_ZIP);
        if(annualZipWithReal) modes.add(DataToVisualize.COMPARED_ANNUAL_ZIP);
        if(cumulativeAnnualPhase) modes.add(DataToVisualize.CUMULATIVE_ANNUAL_PHASE);
        if(cumulativeAnnualPhase2) modes.add(DataToVisualize.CUMULATIVE_ANNUAL_PHASE_WITH_INITIAL);
        if(annualInterest2D) modes.add(DataToVisualize.ANNUAL_INTEREST_2D);
        if(annualPhaseOverview) modes.add(DataToVisualize.ANNUAL_PHASE_OVERVIEW);

        switch(modes.size()) {
            case 0:
                throw new ParsingException("Missing time unit");

            case 1:
                return modes.get(0);

            default:
                throw new ParsingException("Multiple modes: " + modes);
        }
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

    public void setImageWidth(int imageWidth) {
        this.imageWidth = imageWidth;
    }

    @Override
    public int getImageWidth() {
        return imageWidth;
    }

    public void setImageHeight(int imageHeight) {
        this.imageHeight = imageHeight;
    }

    @Override
    public int getImageHeight() {
        return imageHeight;
    }

    public double getLinewidth() {
        return linewidth;
    }

    public void setLinewidth(double linewidth) {
        this.linewidth = linewidth;
    }

    public boolean hasRealAdoptionDataFile() {
        return len(realAdoptionDataFile) == 1;
    }

    public InRealAdoptionDataFile getRealAdoptionDataFile() throws ParsingException {
        return getInstance(realAdoptionDataFile, "realAdoptionDataFile");
    }

    public void setRealAdoptionDataFile(InRealAdoptionDataFile realAdoptionDataFile) {
        if(realAdoptionDataFile == null) {
            this.realAdoptionDataFile = new InRealAdoptionDataFile[0];
        } else {
            this.realAdoptionDataFile = new InRealAdoptionDataFile[] {realAdoptionDataFile};
        }
    }
}
