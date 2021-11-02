package de.unileipzig.irpact.io.param.input.visualisation.result2;

import de.unileipzig.irpact.commons.exception.ParsingException;
import de.unileipzig.irpact.core.postprocessing.image.SupportedEngine;
import de.unileipzig.irpact.develop.Dev;
import de.unileipzig.irpact.io.param.input.InRootUI;
import de.unileipzig.irpact.io.param.input.process2.modular.ca.modules.calc.logging.InConsumerAgentCalculationLoggingModule2;
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
public class InSpecialAverageQuantilRangeImage implements InLoggingResultImage2 {

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
        putClassPath(res, thisClass(), InRootUI.SETT_VISURESULT2_SPECIALAVGQUANTIL);
        addEntryWithDefaultAndDomain(res, thisClass(), "enabled", VALUE_TRUE, DOMAIN_BOOLEAN);
        addEntryWithDefaultAndDomain(res, thisClass(), "useGnuplot", VALUE_TRUE, DOMAIN_BOOLEAN);
        addEntryWithDefaultAndDomain(res, thisClass(), "useR", VALUE_FALSE, DOMAIN_BOOLEAN);
        addEntryWithDefaultAndDomain(res, thisClass(), "storeScript", VALUE_FALSE, DOMAIN_BOOLEAN);
        addEntryWithDefaultAndDomain(res, thisClass(), "storeData", VALUE_FALSE, DOMAIN_BOOLEAN);
        addEntryWithDefaultAndDomain(res, thisClass(), "storeImage", VALUE_TRUE, DOMAIN_BOOLEAN);
        addEntryWithDefaultAndDomain(res, thisClass(), "printAverage", VALUE_TRUE, DOMAIN_BOOLEAN);
        addEntryWithDefaultAndDomain(res, thisClass(), "imageWidth", VALUE_1280, DOMAIN_G0);
        addEntryWithDefaultAndDomain(res, thisClass(), "imageHeight", VALUE_720, DOMAIN_G0);
        addEntryWithDefaultAndDomain(res, thisClass(), "linewidth", VALUE_1, DOMAIN_G0);
        addEntryWithDefaultAndDomain(res, thisClass(), "customImageId", VALUE_0, customImageDomain());
        addEntry(res, thisClass(), "loggingModules");

        setRules(res, thisClass(), ENGINES, InOutputImage2.createEngineBuilder(thisClass()));

        setUnit(res, thisClass(), "imageWidth", UNIT_PIXEL);
        setUnit(res, thisClass(), "imageHeight", UNIT_PIXEL);
    }

    public static InSpecialAverageQuantilRangeImage NPV = new InSpecialAverageQuantilRangeImage(IRPact.IMAGE_QUANTILE_NPV);
    public static InSpecialAverageQuantilRangeImage ENV = new InSpecialAverageQuantilRangeImage(IRPact.IMAGE_QUANTILE_ENV);
    public static InSpecialAverageQuantilRangeImage NOV = new InSpecialAverageQuantilRangeImage(IRPact.IMAGE_QUANTILE_NOV);
    public static InSpecialAverageQuantilRangeImage SOCIAL = new InSpecialAverageQuantilRangeImage(IRPact.IMAGE_QUANTILE_SOCIAL);
    public static InSpecialAverageQuantilRangeImage LOCAL = new InSpecialAverageQuantilRangeImage(IRPact.IMAGE_QUANTILE_LOCAL);

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
    public boolean printAverage = true;

    @FieldDefinition
    public int imageWidth = 1280;

    @FieldDefinition
    public int imageHeight = 720;

    @FieldDefinition
    public int linewidth = 1;

    @FieldDefinition
    public int customImageId = IRPact.INVALID_CUSTOM_IMAGE;

    @FieldDefinition
    public InConsumerAgentCalculationLoggingModule2[] loggingModules = new InConsumerAgentCalculationLoggingModule2[0];

    public InSpecialAverageQuantilRangeImage() {
        setEngine(SupportedEngine.GNUPLOT);
        setStoreImage(true);
        setStoreScript(true);
        setStoreData(true);
        setPrintAverage(true);
        disableCustomImageId();
    }

    public InSpecialAverageQuantilRangeImage(String name) {
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
    public InSpecialAverageQuantilRangeImage copy(CopyCache cache) {
        return cache.copyIfAbsent(this, this::newCopy);
    }

    public InSpecialAverageQuantilRangeImage newCopy(CopyCache cache) {
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

    public void disableCustomImageId() {
        setCustomImageId(IRPact.INVALID_CUSTOM_IMAGE);
    }

    public void setCustomImageId(int customImageId) {
        this.customImageId = customImageId;
    }

    @Override
    public int getCustomImageId() {
        return customImageId;
    }

    public InConsumerAgentCalculationLoggingModule2 getLoggingModule() throws ParsingException {
        return getInstance(loggingModules, "loggingModules");
    }

    public void setLoggingModule(InConsumerAgentCalculationLoggingModule2 loggingModule) {
        this.loggingModules = new InConsumerAgentCalculationLoggingModule2[]{loggingModule};
    }
}
