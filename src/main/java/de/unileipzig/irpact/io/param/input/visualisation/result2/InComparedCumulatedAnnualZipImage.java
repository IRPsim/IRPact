package de.unileipzig.irpact.io.param.input.visualisation.result2;

import de.unileipzig.irpact.commons.exception.ParsingException;
import de.unileipzig.irpact.core.postprocessing.image.SupportedEngine;
import de.unileipzig.irpact.develop.Dev;
import de.unileipzig.irpact.io.param.LocalizedUiResource;
import de.unileipzig.irpact.io.param.input.color.InColorPalette;
import de.unileipzig.irpact.io.param.input.file.InRealAdoptionDataFile;
import de.unileipzig.irpact.start.irpact.IRPact;
import de.unileipzig.irptools.defstructure.annotation.Definition;
import de.unileipzig.irptools.defstructure.annotation.DefinitionName;
import de.unileipzig.irptools.defstructure.annotation.FieldDefinition;
import de.unileipzig.irptools.util.CopyCache;
import de.unileipzig.irptools.util.TreeAnnotationResource;

import java.lang.invoke.MethodHandles;

import static de.unileipzig.irpact.io.param.ParamUtil.*;
import static de.unileipzig.irpact.io.param.input.TreeViewStructureEnum.SETT_VISURESULT2_COMPAREDCUMULATEDZIP;

/**
 * @author Daniel Abitz
 */
@Definition
@LocalizedUiResource.PutClassPath(SETT_VISURESULT2_COMPAREDCUMULATEDZIP)
@LocalizedUiResource.XorWithoutUnselectRule
public class InComparedCumulatedAnnualZipImage implements InLoggingResultImage2 {

    private static final MethodHandles.Lookup L = MethodHandles.lookup();
    public static Class<?> thisClass() {
        return L.lookupClass();
    }
    public static String thisName() {
        return thisClass().getSimpleName();
    }

    @TreeAnnotationResource.Init
    public static void initRes(LocalizedUiResource res) {
    }
    @TreeAnnotationResource.Apply
    public static void applyRes(LocalizedUiResource res) {
    }

    public static InComparedCumulatedAnnualZipImage createDefault() {
        return new InComparedCumulatedAnnualZipImage(IRPact.IMAGE_COMPARED_CUMULATED_ANNUAL_ADOPTIONS_ZIP);
    }

    @DefinitionName
    public String name;

    @FieldDefinition
    @LocalizedUiResource.AddEntry
    @LocalizedUiResource.SimpleSet(
            boolDomain = true,
            boolDefault = true
    )
    public boolean enabled = true;

    @FieldDefinition
    @LocalizedUiResource.AddEntry
    @LocalizedUiResource.SimpleSet(
            boolDomain = true,
            boolDefault = true
    )
    @LocalizedUiResource.XorWithoutUnselectRuleEntry
    public boolean useGnuplot = true;

    @FieldDefinition
    @LocalizedUiResource.AddEntry
    @LocalizedUiResource.SimpleSet(
            boolDomain = true
    )
    @LocalizedUiResource.XorWithoutUnselectRuleEntry
    public boolean useR = false;

    @FieldDefinition
    @LocalizedUiResource.AddEntry
    @LocalizedUiResource.SimpleSet(
            boolDomain = true
    )
    public boolean storeScript = false;

    @FieldDefinition
    @LocalizedUiResource.AddEntry
    @LocalizedUiResource.SimpleSet(
            boolDomain = true
    )
    public boolean storeData = false;

    @FieldDefinition
    @LocalizedUiResource.AddEntry
    @LocalizedUiResource.SimpleSet(
            boolDomain = true,
            boolDefault = true
    )
    public boolean storeImage = true;

    @FieldDefinition
    @LocalizedUiResource.AddEntry
    @LocalizedUiResource.SimpleSet(
            g0Domain = true,
            intDefault = 1280,
            pixelUnit = true
    )
    public int imageWidth = 1280;

    @FieldDefinition
    @LocalizedUiResource.AddEntry
    @LocalizedUiResource.SimpleSet(
            g0Domain = true,
            intDefault = 720,
            pixelUnit = true
    )
    public int imageHeight = 720;

    @FieldDefinition
    @LocalizedUiResource.AddEntry
    @LocalizedUiResource.SimpleSet(
            g0Domain = true,
            intDefault = 1
    )
    public int linewidth = 1;

    @FieldDefinition
    @LocalizedUiResource.AddEntry
    @LocalizedUiResource.SimpleSet(
            boolDomain = true,
            boolDefault = true
    )
    public boolean skipInvalidZips = true;

    @FieldDefinition
    @LocalizedUiResource.AddEntry
    @LocalizedUiResource.SimpleSet(
            boolDomain = true,
            boolDefault = true
    )
    public boolean showPreYear = true;

//    @FieldDefinition
//    @LocalizedUiResource.AddEntry
//    @LocalizedUiResource.SimpleSet(
//            boolDomain = true
//    )
    public boolean showUnscaled = false;

    @FieldDefinition
    @LocalizedUiResource.AddEntry
    @LocalizedUiResource.SimpleSet(
            customImageDefault = true,
            customImageDomain = true
    )
    public int customImageId = IRPact.INVALID_CUSTOM_IMAGE;

    @FieldDefinition
    @LocalizedUiResource.AddEntry
    public InColorPalette[] colorPalette = new InColorPalette[0];

    @FieldDefinition
    @LocalizedUiResource.AddEntry
    public InRealAdoptionDataFile[] realData = new InRealAdoptionDataFile[0];

    public InComparedCumulatedAnnualZipImage() {
        setEngine(SupportedEngine.GNUPLOT);
        setStoreImage(true);
        setStoreScript(true);
        setStoreData(true);
        setShowUnscaled(false);
    }

    public InComparedCumulatedAnnualZipImage(String name) {
        this();
        setName(name);
    }

    public void setEngine(SupportedEngine engine) {
        setUseGnuplot(false);
        setUseR(false);

        switch(engine) {
            case GNUPLOT:
                setUseGnuplot(true);
                break;

            case R:
                setUseR(true);
                break;

            default:
                throw new IllegalArgumentException("unsupported engine: " + engine);
        }
    }

    @Override
    public InComparedCumulatedAnnualZipImage copy(CopyCache cache) {
        return cache.copyIfAbsent(this, this::newCopy);
    }

    public InComparedCumulatedAnnualZipImage newCopy(CopyCache cache) {
        return Dev.throwException();
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String getName() {
        return name;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }

    public void setUseGnuplot(boolean useGnuplot) {
        this.useGnuplot = useGnuplot;
    }

    @Override
    public boolean isUseGnuplot() {
        return useGnuplot;
    }

    public void setUseR(boolean useR) {
        this.useR = useR;
    }

    @Override
    public boolean isUseR() {
        return useR;
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

    public void setLinewidth(int linewidth) {
        this.linewidth = linewidth;
    }

    public int getLinewidth() {
        return linewidth;
    }

    public void setShowUnscaled(boolean showUnscaled) {
        this.showUnscaled = showUnscaled;
    }

    public boolean isShowUnscaled() {
        return showUnscaled;
    }

    public void setSkipInvalidZips(boolean skipInvalidZips) {
        this.skipInvalidZips = skipInvalidZips;
    }

    public boolean isSkipInvalidZips() {
        return skipInvalidZips;
    }

    public void setShowPreYear(boolean showPreYear) {
        this.showPreYear = showPreYear;
    }

    public boolean isShowPreYear() {
        return showPreYear;
    }

    public void setCustomImageId(int customImageId) {
        this.customImageId = customImageId;
    }

    @Override
    public int getCustomImageId() {
        return customImageId;
    }

    public void setColorPalette(InColorPalette colorPalette) {
        this.colorPalette = set(this.colorPalette, colorPalette);
    }

    @Override
    public boolean hasColorPalette() {
        return len(colorPalette) > 0;
    }

    @Override
    public InColorPalette getColorPalette() throws ParsingException {
        return getInstance(colorPalette, "colorPalette");
    }

    public InRealAdoptionDataFile getRealData() throws ParsingException {
        return getInstance(realData, "realData");
    }

    public void setRealData(InRealAdoptionDataFile realData) {
        this.realData = new InRealAdoptionDataFile[]{realData};
    }
}
