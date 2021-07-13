package de.unileipzig.irpact.io.param.input.visualisation.result;

import de.unileipzig.irpact.commons.exception.ParsingException;
import de.unileipzig.irpact.core.postprocessing.image.DataToVisualize;
import de.unileipzig.irpact.core.postprocessing.image.SupportedEngine;
import de.unileipzig.irpact.io.param.input.InRootUI;
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
        putClassPath(res, thisClass(), InRootUI.SETT_VISURESULT_GNU);
        addEntryWithDefault(res, thisClass(), "annualZip", VALUE_TRUE);
        addEntryWithDefault(res, thisClass(), "annualZipWithReal", VALUE_FALSE);
        addEntryWithDefault(res, thisClass(), "cumulativeAnnualPhase", VALUE_FALSE);
        addEntryWithDefault(res, thisClass(), "storeScript", VALUE_FALSE);
        addEntryWithDefault(res, thisClass(), "storeData", VALUE_FALSE);
        addEntryWithDefault(res, thisClass(), "storeImage", VALUE_TRUE);
        addEntryWithDefault(res, thisClass(), "linewidth", varargs(1));

        setDomain(res, thisClass(), "linewidth", "(0,)");

        setRules(res, thisClass(), dataToVisualize, dataToVisualizeBuilder.withKeyModifier(buildDefaultParameterNameOperator(thisClass())));
    }

    public String _name;

    @FieldDefinition
    public boolean annualZip = true;

    @FieldDefinition
    public boolean annualZipWithReal = false;

    @FieldDefinition
    public boolean cumulativeAnnualPhase = false;

    @FieldDefinition
    public boolean storeScript = false;

    @FieldDefinition
    public boolean storeData = false;

    @FieldDefinition
    public boolean storeImage = true;

    @FieldDefinition
    public double linewidth = 1;

    public InGnuPlotOutputImage() {
    }

    public InGnuPlotOutputImage(String name) {
        this(name, DataToVisualize.ANNUAL_ZIP);
    }

    public InGnuPlotOutputImage(String name, DataToVisualize mode) {
        this(name, mode, false);
        setStoreImage(true);
        setLinewidth(1);
    }

    public InGnuPlotOutputImage(String name, DataToVisualize mode, boolean enableAll) {
        setName(name);
        setMode(mode);
        setEnableAll(enableAll);
        setLinewidth(1);
    }

    @Override
    public InGnuPlotOutputImage copy(CopyCache cache) {
        return cache.copyIfAbsent(this, this::newCopy);
    }

    public InGnuPlotOutputImage newCopy(CopyCache cache) {
        InGnuPlotOutputImage copy = new InGnuPlotOutputImage();
        copy._name = _name;
        copy.annualZip = annualZip;
        copy.annualZipWithReal = annualZipWithReal;
        copy.cumulativeAnnualPhase = cumulativeAnnualPhase;
        copy.storeData = storeData;
        copy.storeScript = storeScript;
        copy.storeImage = storeImage;
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
        return SupportedEngine.GNUPLOT;
    }

    public void setMode(DataToVisualize mode) {
        annualZip = false;
        annualZipWithReal = false;
        cumulativeAnnualPhase = false;

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

            default:
                throw new IllegalArgumentException("unsupported mode: " + mode);
        }
    }

    @Override
    public DataToVisualize getMode() throws ParsingException {
        List<DataToVisualize> modes = new ArrayList<>();
        if(annualZip) modes.add(DataToVisualize.ANNUAL_ZIP);
        if(annualZipWithReal) modes.add(DataToVisualize.COMPARED_ANNUAL_ZIP);
        if(cumulativeAnnualPhase) modes.add(DataToVisualize.COMPARED_ANNUAL_ZIP);

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

    public double getLinewidth() {
        return linewidth;
    }

    public void setLinewidth(double linewidth) {
        this.linewidth = linewidth;
    }
}
