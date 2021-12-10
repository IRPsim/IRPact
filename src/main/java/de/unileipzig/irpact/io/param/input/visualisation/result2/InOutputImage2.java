package de.unileipzig.irpact.io.param.input.visualisation.result2;

import de.unileipzig.irpact.commons.exception.ParsingException;
import de.unileipzig.irpact.core.postprocessing.image.SupportedEngine;
import de.unileipzig.irpact.io.param.ParamUtil;
import de.unileipzig.irpact.io.param.input.InIRPactEntity;
import de.unileipzig.irpact.io.param.input.color.InColorPalette;
import de.unileipzig.irptools.Constants;
import de.unileipzig.irptools.defstructure.annotation.Definition;
import de.unileipzig.irptools.util.TreeAnnotationResource;
import de.unileipzig.irptools.util.XorWithoutUnselectRuleBuilder;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Daniel Abitz
 */
@Definition
public interface InOutputImage2 extends InIRPactEntity {

//    String[] ENGINES = {"useGnuplot", "useR"};
//
//    static XorWithoutUnselectRuleBuilder createEngineBuilder(Class<?> c) {
//        return new XorWithoutUnselectRuleBuilder()
//                .withKeyModifier(ParamUtil.buildDefaultParameterNameOperator(c))
//                .withTrueValue(Constants.TRUE1)
//                .withFalseValue(Constants.FALSE0)
//                .withKeys(ENGINES);
//    }

    @TreeAnnotationResource.Init
    static void initRes(TreeAnnotationResource res) {
    }
    @TreeAnnotationResource.Apply
    static void applyRes(TreeAnnotationResource res) {
    }

    default String getBaseFileName() {
        return getName();
    }

    default SupportedEngine getEngine() throws ParsingException {
        List<SupportedEngine> engines = new ArrayList<>();
        if(isUseGnuplot()) engines.add(SupportedEngine.GNUPLOT);
        if(isUseR()) engines.add(SupportedEngine.R);

        switch(engines.size()) {
            case 0:
                throw new ParsingException("Missing engine");

            case 1:
                return engines.get(0);

            default:
                throw new ParsingException("Multiple engines: " + engines);
        }
    }

    boolean isEnabled();

    default boolean isDisabled() {
        return !isEnabled();
    }

    boolean isUseGnuplot();

    boolean isUseR();

    boolean isStoreImage();

    boolean isStoreData();

    boolean isStoreScript();

    int getImageWidth();

    int getImageHeight();

    int getCustomImageId();

    default boolean hasSomethingToStore() {
        return isStoreData() || isStoreScript() || isStoreImage();
    }

    default boolean hasNothingToStore() {
        return !hasSomethingToStore();
    }

    boolean hasColorPalette();

    InColorPalette getColorPalette() throws ParsingException;
}
