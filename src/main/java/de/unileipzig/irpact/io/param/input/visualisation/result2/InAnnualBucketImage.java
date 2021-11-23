package de.unileipzig.irpact.io.param.input.visualisation.result2;

import de.unileipzig.irpact.commons.exception.ParsingException;
import de.unileipzig.irpact.core.postprocessing.image.SupportedEngine;
import de.unileipzig.irpact.develop.Dev;
import de.unileipzig.irpact.io.param.input.InRootUI;
import de.unileipzig.irpact.io.param.input.process2.modular.ca.modules.calc.logging.InConsumerAgentCalculationLoggingModule2;
import de.unileipzig.irpact.io.param.input.color.InColorPalette;
import de.unileipzig.irpact.start.irpact.IRPact;
import de.unileipzig.irptools.defstructure.annotation.Definition;
import de.unileipzig.irptools.defstructure.annotation.FieldDefinition;
import de.unileipzig.irptools.util.CopyCache;
import de.unileipzig.irptools.util.TreeAnnotationResource;

import java.lang.invoke.MethodHandles;

import static de.unileipzig.irpact.io.param.ParamUtil.*;

/**
 * @author Daniel Abitz
 */
@Definition
public class InAnnualBucketImage implements InLoggingResultImage2 {

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
        putClassPath(res, thisClass(), InRootUI.SETT_VISURESULT2_ANNUALBUCKET);
        addEntryWithDefaultAndDomain(res, thisClass(), "enabled", VALUE_TRUE, DOMAIN_BOOLEAN);
        addEntryWithDefaultAndDomain(res, thisClass(), "useGnuplot", VALUE_TRUE, DOMAIN_BOOLEAN);
        addEntryWithDefaultAndDomain(res, thisClass(), "useR", VALUE_FALSE, DOMAIN_BOOLEAN);
        addEntryWithDefaultAndDomain(res, thisClass(), "storeScript", VALUE_FALSE, DOMAIN_BOOLEAN);
        addEntryWithDefaultAndDomain(res, thisClass(), "storeData", VALUE_FALSE, DOMAIN_BOOLEAN);
        addEntryWithDefaultAndDomain(res, thisClass(), "storeImage", VALUE_TRUE, DOMAIN_BOOLEAN);
        addEntryWithDefaultAndDomain(res, thisClass(), "imageWidth", VALUE_1280, DOMAIN_G0);
        addEntryWithDefaultAndDomain(res, thisClass(), "imageHeight", VALUE_720, DOMAIN_G0);
        addEntryWithDefaultAndDomain(res, thisClass(), "boxWidth", VALUE_1, DOMAIN_G0);
        addEntryWithDefaultAndDomain(res, thisClass(), "useCustomYRange", VALUE_TRUE, DOMAIN_BOOLEAN);
        addEntryWithDefault(res, thisClass(), "minY", VALUE_0);
        addEntryWithDefault(res, thisClass(), "maxY", VALUE_0);
        addEntryWithDefaultAndDomain(res, thisClass(), "bucketSize", asValue(0.1), DOMAIN_G0);
        addEntryWithDefaultAndDomain(res, thisClass(), "fractionDigits", VALUE_1, DOMAIN_GEQ0);
        addEntryWithDefaultAndDomain(res, thisClass(), "customImageId", VALUE_0, customImageDomain());
        addEntry(res, thisClass(), "colorPalette");
        addEntry(res, thisClass(), "loggingModule");

        setRules(res, thisClass(), ENGINES, InOutputImage2.createEngineBuilder(thisClass()));

        setUnit(res, thisClass(), "imageWidth", UNIT_PIXEL);
        setUnit(res, thisClass(), "imageHeight", UNIT_PIXEL);
    }

    public static InAnnualBucketImage NPV = new InAnnualBucketImage(IRPact.IMAGE_BUCKET_NPV);
    public static InAnnualBucketImage ENV = new InAnnualBucketImage(IRPact.IMAGE_BUCKET_ENV);
    public static InAnnualBucketImage NOV = new InAnnualBucketImage(IRPact.IMAGE_BUCKET_NOV);
    public static InAnnualBucketImage SOCIAL = new InAnnualBucketImage(IRPact.IMAGE_BUCKET_SOCIAL);
    public static InAnnualBucketImage LOCAL = new InAnnualBucketImage(IRPact.IMAGE_BUCKET_LOCAL);
    public static InAnnualBucketImage UTILITY = new InAnnualBucketImage(IRPact.IMAGE_BUCKET_UTILITY);

    public String _name;

    @FieldDefinition
    public boolean enabled = true;

    @FieldDefinition
    public boolean useGnuplot = true;

    @FieldDefinition
    public boolean useR = false;

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
    public double boxWidth = 1;

    @FieldDefinition
    public boolean useCustomYRange = false;

    @FieldDefinition
    public double minY = 0.0;

    @FieldDefinition
    public double maxY = 0.0;

    @FieldDefinition
    public double bucketSize = 0.1;

    @FieldDefinition
    public int fractionDigits = 1;

    @FieldDefinition
    public int customImageId = IRPact.INVALID_CUSTOM_IMAGE;

    @FieldDefinition
    public InColorPalette[] colorPalette = new InColorPalette[0];

    @FieldDefinition
    public InConsumerAgentCalculationLoggingModule2[] loggingModule = new InConsumerAgentCalculationLoggingModule2[0];

    public InAnnualBucketImage() {
        setEngine(SupportedEngine.GNUPLOT);
        setStoreImage(true);
        setStoreScript(true);
        setStoreData(true);
    }

    public InAnnualBucketImage(String name) {
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
    public InAnnualBucketImage copy(CopyCache cache) {
        return cache.copyIfAbsent(this, this::newCopy);
    }

    public InAnnualBucketImage newCopy(CopyCache cache) {
        return Dev.throwException();
    }

    public void setName(String name) {
        this._name = name;
    }

    @Override
    public String getName() {
        return _name;
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

    public void setBucketSize(double bucketSize) {
        this.bucketSize = bucketSize;
    }

    public double getBucketSize() {
        return bucketSize;
    }

    public void setFractionDigits(int fractionDigits) {
        this.fractionDigits = fractionDigits;
    }

    public int getFractionDigits() {
        return fractionDigits;
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

    @Override
    public int getCustomImageId() {
        return customImageId;
    }

    public InConsumerAgentCalculationLoggingModule2 getLoggingModule() throws ParsingException {
        return getInstance(loggingModule, "loggingModules");
    }

    public void setLoggingModule(InConsumerAgentCalculationLoggingModule2 loggingModule) {
        this.loggingModule = new InConsumerAgentCalculationLoggingModule2[]{loggingModule};
    }
}
