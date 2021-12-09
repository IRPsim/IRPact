package de.unileipzig.irpact.io.param.input.visualisation.result;

import de.unileipzig.irpact.commons.exception.ParsingException;
import de.unileipzig.irpact.commons.util.StringUtil;
import de.unileipzig.irpact.core.postprocessing.image.d2v.DataToVisualize;
import de.unileipzig.irpact.core.postprocessing.image.SupportedEngine;
import de.unileipzig.irpact.develop.ToRemove;
import de.unileipzig.irpact.io.param.input.InIRPactEntity;
import de.unileipzig.irpact.io.param.input.file.InRealAdoptionDataFile;
import de.unileipzig.irptools.Constants;
import de.unileipzig.irptools.defstructure.annotation.Definition;
import de.unileipzig.irptools.util.TreeAnnotationResource;
import de.unileipzig.irptools.util.XorWithoutUnselectRuleBuilder;

import static de.unileipzig.irpact.io.param.ParamUtil.len;

/**
 * @author Daniel Abitz
 */
@Definition
@ToRemove
public interface InOutputImage extends InIRPactEntity {

    String[] dataToVisualize = {
            "annualZip",
            "annualZipWithReal",
            "annualZipWithRealTotal",
            "cumulativeAnnualPhase",
            "cumulativeAnnualPhase2",
            "annualInterest2D",
            "annualPhaseOverview"
    };
    String[] dataToVisualizeWithoutDefault = StringUtil.without(dataToVisualize, "annualZip");
    XorWithoutUnselectRuleBuilder dataToVisualizeBuilder = new XorWithoutUnselectRuleBuilder()
            .withTrueValue(Constants.TRUE1)
            .withFalseValue(Constants.FALSE0)
            .withKeys(dataToVisualize);

    static void initRes(TreeAnnotationResource res) {
    }
    static void applyRes(TreeAnnotationResource res) {
    }

    default String getBaseFileName() {
        return getName();
    }

    SupportedEngine getEngine() throws ParsingException;

    DataToVisualize getMode() throws ParsingException;

    default void enableAll() {
        setEnableAll(true);
    }

    default void disableAll() {
        setEnableAll(false);
    }

    void setEnableAll(boolean enableAll);

    boolean isStoreImage();

    boolean isStoreData();

    boolean isStoreScript();

    double getLinewidth();

    default int getLinewidthInt() {
        return (int) getLinewidth();
    }

    int getImageWidth();

    int getImageHeight();

    boolean hasRealAdoptionDataFile();

    InRealAdoptionDataFile getRealAdoptionDataFile() throws ParsingException;

    default boolean isEnabled() {
        return isStoreData() || isStoreScript() || isStoreImage();
    }

    default boolean isDisabled() {
        return !isEnabled();
    }
}
