package de.unileipzig.irpact.io.param.input.visualisation.result2;

import de.unileipzig.irpact.commons.exception.ParsingException;
import de.unileipzig.irpact.core.postprocessing.image.SupportedEngine;
import de.unileipzig.irpact.develop.Dev;
import de.unileipzig.irpact.io.param.LocalizedUiResource;
import de.unileipzig.irpact.io.param.input.color.InColorPalette;
import de.unileipzig.irpact.io.param.input.process2.modular.ca.modules.calc.logging.InConsumerAgentCalculationLoggingModule2;
import de.unileipzig.irpact.start.irpact.IRPact;
import de.unileipzig.irptools.defstructure.annotation.Definition;
import de.unileipzig.irptools.defstructure.annotation.DefinitionName;
import de.unileipzig.irptools.defstructure.annotation.FieldDefinition;
import de.unileipzig.irptools.util.CopyCache;
import de.unileipzig.irptools.util.TreeAnnotationResource;

import java.lang.invoke.MethodHandles;

import static de.unileipzig.irpact.io.param.ParamUtil.*;
import static de.unileipzig.irpact.io.param.input.TreeViewStructureEnum.SETT_VISURESULT2_CUSTOMAVGQUANTIL;

/**
 * @author Daniel Abitz
 */
@Definition
@LocalizedUiResource.PutClassPath(SETT_VISURESULT2_CUSTOMAVGQUANTIL)
public class InCustomAverageQuantilRangeImage implements InLoggingResultImage2 {

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
        res.setRules(thisClass(), ENGINES, InOutputImage2.createEngineBuilder(thisClass()));
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
    public boolean useGnuplot = true;

    @FieldDefinition
    @LocalizedUiResource.AddEntry
    @LocalizedUiResource.SimpleSet(
            boolDomain = true
    )
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
            boolDomain = true,
            boolDefault = true
    )
    public boolean printAverage = false;

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
            intDefault = 1280,
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
            customImageDomain = true,
            customImageDefault = true
    )
    public int customImageId = IRPact.INVALID_CUSTOM_IMAGE;

    @FieldDefinition
    @LocalizedUiResource.AddEntry
    public InColorPalette[] colorPalette = new InColorPalette[0];

    @FieldDefinition
    @LocalizedUiResource.AddEntry
    public InQuantileRange[] ranges = new InQuantileRange[0];

    @FieldDefinition
    @LocalizedUiResource.AddEntry
    public InConsumerAgentCalculationLoggingModule2[] loggingModules = new InConsumerAgentCalculationLoggingModule2[0];

    public InCustomAverageQuantilRangeImage() {
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
    public InCustomAverageQuantilRangeImage copy(CopyCache cache) {
        return cache.copyIfAbsent(this, this::newCopy);
    }

    public InCustomAverageQuantilRangeImage newCopy(CopyCache cache) {
        return Dev.throwException();
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String getName() {
        return name;
    }

    public void setUseGnuplot(boolean useGnuplot) {
        this.useGnuplot = useGnuplot;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
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

    public void setPrintAverage(boolean printAverage) {
        this.printAverage = printAverage;
    }

    public boolean isPrintAverage() {
        return printAverage;
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

    public void setCustomImageId(int customImageId) {
        this.customImageId = customImageId;
    }

    @Override
    public int getCustomImageId() {
        return customImageId;
    }

    public void setColorPalette(InColorPalette colorPalette) {
        this.colorPalette = new InColorPalette[]{ colorPalette };
    }

    public boolean hasColorPalette() {
        return len(colorPalette) > 0;
    }

    public InColorPalette getColorPalette() throws ParsingException {
        return getInstance(colorPalette, "colorPalette");
    }

    public InQuantileRange[] getQuantileRanges() throws ParsingException {
        return getNonNullArray(ranges, "ranges");
    }

    public void setQuantileRanges(InQuantileRange... ranges) {
        this.ranges = ranges;
    }

    public InConsumerAgentCalculationLoggingModule2 getLoggingModule() throws ParsingException {
        return getInstance(loggingModules, "loggingModules");
    }

    public void setLoggingModule(InConsumerAgentCalculationLoggingModule2 loggingModule) {
        this.loggingModules = new InConsumerAgentCalculationLoggingModule2[]{loggingModule};
    }
}
