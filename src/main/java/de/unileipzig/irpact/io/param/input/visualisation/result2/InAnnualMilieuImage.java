package de.unileipzig.irpact.io.param.input.visualisation.result2;

import de.unileipzig.irpact.commons.exception.ParsingException;
import de.unileipzig.irpact.core.postprocessing.image.SupportedEngine;
import de.unileipzig.irpact.develop.Dev;
import de.unileipzig.irpact.io.param.LocalizedUiResource;
import de.unileipzig.irpact.io.param.input.color.InColorPalette;
import de.unileipzig.irpact.start.irpact.IRPact;
import de.unileipzig.irptools.defstructure.annotation.Definition;
import de.unileipzig.irptools.defstructure.annotation.DefinitionName;
import de.unileipzig.irptools.defstructure.annotation.FieldDefinition;
import de.unileipzig.irptools.util.CopyCache;
import de.unileipzig.irptools.util.TreeAnnotationResource;

import java.lang.invoke.MethodHandles;

import static de.unileipzig.irpact.io.param.ParamUtil.*;
import static de.unileipzig.irpact.io.param.input.TreeViewStructureEnum.SETT_VISURESULT2_ANNUALMILIEU;

/**
 * @author Daniel Abitz
 */
@Definition
@LocalizedUiResource.PutClassPath(SETT_VISURESULT2_ANNUALMILIEU)
@LocalizedUiResource.XorWithoutUnselectRule
public class InAnnualMilieuImage implements InLoggingResultImage2 {

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

    public static InAnnualMilieuImage createDefault() {
        return new InAnnualMilieuImage(IRPact.IMAGE_ANNUAL_ADOPTION_MILIEU);
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
            domain = DOMAIN_G0,
            intDefault = 1280,
            pixelUnit = true
    )
    public int imageWidth = 1280;

    @FieldDefinition
    @LocalizedUiResource.AddEntry
    @LocalizedUiResource.SimpleSet(
            domain = DOMAIN_G0,
            intDefault = 1280,
            pixelUnit = true
    )
    public int imageHeight = 720;

    @FieldDefinition
    @LocalizedUiResource.AddEntry
    @LocalizedUiResource.SimpleSet(
            domain = DOMAIN_G0,
            decDefault = 1
    )
    public double boxWidth = 1;

    @FieldDefinition
    @LocalizedUiResource.AddEntry
    @LocalizedUiResource.SimpleSet(
            boolDomain = true,
            boolDefault = true
    )
    public boolean showInitial = true;

    @FieldDefinition
    @LocalizedUiResource.AddEntry
    @LocalizedUiResource.SimpleSet(
            boolDomain = true
    )
    public boolean useCustomYRange = false;

    @FieldDefinition
    @LocalizedUiResource.AddEntry
    @LocalizedUiResource.SimpleSet(
            decDefault = 0
    )
    public double minY = 0.0;

    @FieldDefinition
    @LocalizedUiResource.AddEntry
    @LocalizedUiResource.SimpleSet(
            decDefault = 0
    )
    public double maxY = 0.0;

    @FieldDefinition
    @LocalizedUiResource.AddEntry
    @LocalizedUiResource.SimpleSet(
            intDefault = IRPact.INVALID_CUSTOM_IMAGE
    )
    public int customImageId = IRPact.INVALID_CUSTOM_IMAGE;

    @FieldDefinition
    @LocalizedUiResource.AddEntry
    public InColorPalette[] colorPalette = new InColorPalette[0];

    public InAnnualMilieuImage() {
        setEngine(SupportedEngine.GNUPLOT);
        setStoreImage(true);
        setStoreScript(true);
        setStoreData(true);
    }

    public InAnnualMilieuImage(String name) {
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
    public InAnnualMilieuImage copy(CopyCache cache) {
        return cache.copyIfAbsent(this, this::newCopy);
    }

    public InAnnualMilieuImage newCopy(CopyCache cache) {
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

    public void setBoxWidth(double boxWidth) {
        this.boxWidth = boxWidth;
    }

    public double getBoxWidth() {
        return boxWidth;
    }

    public void setUseCustomYRange(boolean useCustomYRange) {
        this.useCustomYRange = useCustomYRange;
    }

    public boolean isUseCustomYRange() {
        return useCustomYRange;
    }

    public void setMinY(double minY) {
        this.minY = minY;
    }

    public double getMinY() {
        return minY;
    }

    public void setMaxY(double maxY) {
        this.maxY = maxY;
    }

    public double getMaxY() {
        return maxY;
    }

    public void setCustomImageId(int customImageId) {
        this.customImageId = customImageId;
    }

    public void setShowInitial(boolean showInitial) {
        this.showInitial = showInitial;
    }

    public boolean isShowInitial() {
        return showInitial;
    }

    public void setColorPalette(InColorPalette colorPalette) {
        this.colorPalette = new InColorPalette[]{ colorPalette };
    }

    @Override
    public boolean hasColorPalette() {
        return len(colorPalette) > 0;
    }

    @Override
    public InColorPalette getColorPalette() throws ParsingException {
        return getInstance(colorPalette, "colorPalette");
    }

    @Override
    public int getCustomImageId() {
        return customImageId;
    }
}
