package de.unileipzig.irpact.io.param.input.visualisation.result;

import de.unileipzig.irpact.commons.exception.ParsingException;
import de.unileipzig.irpact.core.postprocessing.image.d2v.DataToVisualize;
import de.unileipzig.irpact.core.postprocessing.image.SupportedEngine;
import de.unileipzig.irpact.develop.ToRemove;
import de.unileipzig.irpact.io.param.LocalizedUiResource;
import de.unileipzig.irpact.io.param.input.TreeViewStructureEnum;
import de.unileipzig.irpact.io.param.input.file.InRealAdoptionDataFile;
import de.unileipzig.irptools.Constants;
import de.unileipzig.irptools.defstructure.annotation.Definition;
import de.unileipzig.irptools.defstructure.annotation.DefinitionName;
import de.unileipzig.irptools.defstructure.annotation.FieldDefinition;
import de.unileipzig.irptools.util.CopyCache;
import de.unileipzig.irptools.util.TreeAnnotationResource;
import de.unileipzig.irptools.util.XorWithoutUnselectRuleBuilder;

import java.lang.invoke.MethodHandles;
import java.util.ArrayList;
import java.util.List;

import static de.unileipzig.irpact.io.param.ParamUtil.*;

/**
 * @author Daniel Abitz
 */
@Definition
@ToRemove
public class InGenericOutputImage implements InOutputImage {

    private static final MethodHandles.Lookup L = MethodHandles.lookup();
    public static Class<?> thisClass() {
        return L.lookupClass();
    }
    public static String thisName() {
        return thisClass().getSimpleName();
    }

    protected static final String[] engineFieldNames = {"useGnuplot", "useR"};
    protected static final XorWithoutUnselectRuleBuilder engineBuilder = new XorWithoutUnselectRuleBuilder()
            .withKeyModifier(buildDefaultParameterNameOperator(thisClass()))
            .withTrueValue(Constants.TRUE1)
            .withFalseValue(Constants.FALSE0)
            .withKeys(engineFieldNames);

    @TreeAnnotationResource.Init
    public static void initRes(LocalizedUiResource res) {
    }
    @TreeAnnotationResource.Apply
    public static void applyRes(LocalizedUiResource res) {
        res.putClassPath(thisClass(), TreeViewStructureEnum.SETT_VISURESULT_GENERIC.getPath());
        res.addEntryWithDefaultAndDomain(thisClass(), "useGnuplot", VALUE_TRUE, DOMAIN_BOOLEAN);
        res.addEntryWithDefaultAndDomain(thisClass(), "useR", VALUE_FALSE, DOMAIN_BOOLEAN);
        res.addEntryWithDefaultAndDomain(thisClass(), "annualZip", VALUE_TRUE, DOMAIN_BOOLEAN);
        res.addEntriesWithDefaultAndDomain(thisClass(), dataToVisualizeWithoutDefault, VALUE_FALSE, DOMAIN_BOOLEAN);
        res.addEntryWithDefaultAndDomain(thisClass(), "storeScript", VALUE_FALSE, DOMAIN_BOOLEAN);
        res.addEntryWithDefaultAndDomain(thisClass(), "storeData", VALUE_FALSE, DOMAIN_BOOLEAN);
        res.addEntryWithDefaultAndDomain(thisClass(), "storeImage", VALUE_TRUE, DOMAIN_BOOLEAN);
        res.addEntryWithDefaultAndDomain(thisClass(), "imageWidth", VALUE_1280, DOMAIN_G0);
        res.addEntryWithDefaultAndDomain(thisClass(), "imageHeight", VALUE_720, DOMAIN_G0);
        res.addEntryWithDefaultAndDomain(thisClass(), "linewidth", VALUE_1, DOMAIN_G0);
        res.addEntry(thisClass(), "realAdoptionDataFile");

//        setDefault(thisClass(), varargs(
//                IRPact.IMAGE_ANNUAL_ADOPTIONS,
//                IRPact.IMAGE_COMPARED_ANNUAL_ADOPTIONS_ZIP,
//                IRPact.IMAGE_COMPARED_ANNUAL_ADOPTIONS,
//                IRPact.IMAGE_ANNUAL_CUMULATIVE_ADOPTIONS,
//                IRPact.IMAGE_ANNUAL_INTEREST,
//                IRPact.IMAGE_PHASE_OVERVIEW
//        ));

        res.setRules(thisClass(), engineFieldNames, engineBuilder);
        res.setRules(thisClass(), dataToVisualize, dataToVisualizeBuilder.withKeyModifier(buildDefaultParameterNameOperator(thisClass())));

        res.setUnit(thisClass(), "imageWidth", UNIT_PIXEL);
        res.setUnit(thisClass(), "imageHeight", UNIT_PIXEL);
    }

    //=========================
    //defaults
    //=========================

    public static InGenericOutputImage[] createDefaultImages() {
        return new InGenericOutputImage[0];
//        return new InGenericOutputImage[] {
//                new InGenericOutputImage(IRPact.IMAGE_ANNUAL_ADOPTIONS, DataToVisualize.ANNUAL_ZIP, null),
//                new InGenericOutputImage(IRPact.IMAGE_COMPARED_ANNUAL_ADOPTIONS_ZIP, DataToVisualize.COMPARED_ANNUAL_ZIP, null),
//                new InGenericOutputImage(IRPact.IMAGE_COMPARED_ANNUAL_ADOPTIONS, DataToVisualize.COMPARED_ANNUAL, null),
//                new InGenericOutputImage(IRPact.IMAGE_ANNUAL_CUMULATIVE_ADOPTIONS, DataToVisualize.CUMULATIVE_ANNUAL_PHASE_WITH_INITIAL, null),
//                new InGenericOutputImage(IRPact.IMAGE_ANNUAL_INTEREST, DataToVisualize.ANNUAL_INTEREST_2D, null),
//                new InGenericOutputImage(IRPact.IMAGE_PHASE_OVERVIEW, DataToVisualize.ANNUAL_PHASE_OVERVIEW, null)
//        };
    }

    public static InGenericOutputImage[] createDefaultImages(InRealAdoptionDataFile realAdoptionDataFile) {
        return new InGenericOutputImage[0];
//        return new InGenericOutputImage[] {
//                new InGenericOutputImage(IRPact.IMAGE_ANNUAL_ADOPTIONS, DataToVisualize.ANNUAL_ZIP, null),
//                new InGenericOutputImage(IRPact.IMAGE_COMPARED_ANNUAL_ADOPTIONS_ZIP, DataToVisualize.COMPARED_ANNUAL_ZIP, realAdoptionDataFile),
//                new InGenericOutputImage(IRPact.IMAGE_COMPARED_ANNUAL_ADOPTIONS, DataToVisualize.COMPARED_ANNUAL, realAdoptionDataFile),
//                new InGenericOutputImage(IRPact.IMAGE_ANNUAL_CUMULATIVE_ADOPTIONS, DataToVisualize.CUMULATIVE_ANNUAL_PHASE_WITH_INITIAL, null),
//                new InGenericOutputImage(IRPact.IMAGE_ANNUAL_INTEREST, DataToVisualize.ANNUAL_INTEREST_2D, null),
//                new InGenericOutputImage(IRPact.IMAGE_PHASE_OVERVIEW, DataToVisualize.ANNUAL_PHASE_OVERVIEW, null)
//        };
    }

    public static void setEnableAll(boolean enableAll, InGenericOutputImage... images) {
        for(InGenericOutputImage image: images) {
            image.setEnableAll(enableAll);
        }
    }

    public static void setEngine(SupportedEngine engine, InGenericOutputImage... images) {
        for(InGenericOutputImage image: images) {
            image.setEngine(engine);
        }
    }

    //=========================
    //...
    //=========================

    @DefinitionName
    public String name;

    @FieldDefinition
    public boolean useGnuplot = true;

    @FieldDefinition
    public boolean useR = false;

    @FieldDefinition
    public boolean annualZip = true;

    @FieldDefinition
    public boolean annualZipWithReal = false;

    @FieldDefinition
    public boolean annualZipWithRealTotal = false;

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

    public InGenericOutputImage() {
    }

    public InGenericOutputImage(String name, DataToVisualize mode, InRealAdoptionDataFile realAdoptionDataFile) {
        setName(name);
        setEngine(SupportedEngine.GNUPLOT);
        setMode(mode);
        setStoreImage(true);
        setStoreData(false);
        setStoreScript(false);
        setLinewidth(1);
        setRealAdoptionDataFile(realAdoptionDataFile);
    }

    @Override
    public InGenericOutputImage copy(CopyCache cache) {
        return cache.copyIfAbsent(this, this::newCopy);
    }

    public InGenericOutputImage newCopy(CopyCache cache) {
        InGenericOutputImage copy = new InGenericOutputImage();
        copy.name = name;
        copy.useGnuplot = useGnuplot;
        copy.useR = useR;
        copy.annualZip = annualZip;
        copy.annualZipWithReal = annualZipWithReal;
        copy.annualZipWithRealTotal = annualZipWithRealTotal;
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
        copy.realAdoptionDataFile = cache.copyArray(realAdoptionDataFile);
        return copy;
    }

    @Override
    public void setEnableAll(boolean enableAll) {
        storeImage = enableAll;
        storeScript = enableAll;
        storeData = enableAll;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String getName() {
        return name;
    }

    public void setEngine(SupportedEngine engine) {
        useGnuplot = false;
        useR = false;

        switch(engine) {
            case GNUPLOT:
                useGnuplot = true;
                break;

            case R:
                useR = true;
                break;

            default:
                throw new IllegalArgumentException("unsupported engine: " + engine);
        }
    }

    @Override
    public SupportedEngine getEngine() throws ParsingException {
        List<SupportedEngine> engines = new ArrayList<>();
        if(useGnuplot) engines.add(SupportedEngine.GNUPLOT);
        if(useR) engines.add(SupportedEngine.R);

        switch(engines.size()) {
            case 0:
                throw new ParsingException("Missing engine");

            case 1:
                return engines.get(0);

            default:
                throw new ParsingException("Multiple engines: " + engines);
        }
    }

    public void setMode(DataToVisualize mode) {
        annualZip = false;
        annualZipWithReal = false;
        annualZipWithRealTotal = false;
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

            case COMPARED_ANNUAL:
                annualZipWithRealTotal = true;
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
        if(annualZipWithRealTotal) modes.add(DataToVisualize.COMPARED_ANNUAL);
        if(cumulativeAnnualPhase) modes.add(DataToVisualize.CUMULATIVE_ANNUAL_PHASE);
        if(cumulativeAnnualPhase2) modes.add(DataToVisualize.CUMULATIVE_ANNUAL_PHASE_WITH_INITIAL);
        if(annualInterest2D) modes.add(DataToVisualize.ANNUAL_INTEREST_2D);
        if(annualPhaseOverview) modes.add(DataToVisualize.ANNUAL_PHASE_OVERVIEW);

        switch(modes.size()) {
            case 0:
                throw new ParsingException("Missing mode");

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

    @Override
    public boolean hasRealAdoptionDataFile() {
        return len(realAdoptionDataFile) == 1;
    }

    @Override
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
