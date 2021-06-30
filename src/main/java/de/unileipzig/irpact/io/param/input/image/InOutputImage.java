package de.unileipzig.irpact.io.param.input.image;

import de.unileipzig.irpact.commons.exception.ParsingException;
import de.unileipzig.irpact.core.util.img.DataToVisualize;
import de.unileipzig.irpact.core.util.img.SupportedEngine;
import de.unileipzig.irpact.io.param.input.InIRPactEntity;
import de.unileipzig.irpact.start.irpact.IRPact;
import de.unileipzig.irptools.Constants;
import de.unileipzig.irptools.defstructure.annotation.Definition;
import de.unileipzig.irptools.util.TreeAnnotationResource;
import de.unileipzig.irptools.util.XorWithoutUnselectRuleBuilder;

/**
 * @author Daniel Abitz
 */
@Definition
public interface InOutputImage extends InIRPactEntity {

//    int MODE_NOTHING = 0;
//    int MODE_ADOPTION_LINECHART = 1;
//    int MODE_ADOPTION_INTERACTION_LINECHART = 2;
//    int MODE_ADOPTION_PHASE_BARCHART = 3;
//
//    int ENGINE_GNUPLOT = 1;
//    int ENGINE_R = 2;
//    Object[] DEFAULT_ENGINE = {ENGINE_GNUPLOT};

//    static String printEngineDomain() {
//        return "[" + ENGINE_GNUPLOT + ", " + ENGINE_R + "]";
//    }
//
//    static String printModeDomain() {
//        return "[" + MODE_NOTHING + ", " + MODE_ADOPTION_PHASE_BARCHART + "]";
//    }


    String[] dataToVisualize = {"annualZip", "annualZipWithReal", "cumulativeAnnualPhase"};
    static final XorWithoutUnselectRuleBuilder dataToVisualizeBuilder = new XorWithoutUnselectRuleBuilder()
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

    default boolean isEnabled() {
        return isStoreData() || isStoreData() || isStoreScript();
    }

    default boolean isDisabled() {
        return !isEnabled();
    }
}
