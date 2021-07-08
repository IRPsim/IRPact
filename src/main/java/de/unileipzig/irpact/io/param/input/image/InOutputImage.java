package de.unileipzig.irpact.io.param.input.image;

import de.unileipzig.irpact.commons.exception.ParsingException;
import de.unileipzig.irpact.core.util.img.DataToVisualize;
import de.unileipzig.irpact.core.util.img.SupportedEngine;
import de.unileipzig.irpact.io.param.input.InIRPactEntity;
import de.unileipzig.irptools.Constants;
import de.unileipzig.irptools.defstructure.annotation.Definition;
import de.unileipzig.irptools.util.TreeAnnotationResource;
import de.unileipzig.irptools.util.XorWithoutUnselectRuleBuilder;

/**
 * @author Daniel Abitz
 */
@Definition
public interface InOutputImage extends InIRPactEntity {

    String[] dataToVisualize = {"annualZip", "annualZipWithReal", "cumulativeAnnualPhase"};
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

    default boolean isEnabled() {
        return isStoreData() || isStoreScript() || isStoreImage();
    }

    default boolean isDisabled() {
        return !isEnabled();
    }
}
