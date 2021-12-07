package de.unileipzig.irpact.io.param.input.visualisation.network;

import de.unileipzig.irpact.commons.exception.ParsingException;
import de.unileipzig.irpact.core.logging.IRPLogging;
import de.unileipzig.irpact.io.param.LocalizedUiResource;
import de.unileipzig.irpact.io.param.input.TreeViewStructure;
import de.unileipzig.irptools.Constants;
import de.unileipzig.irptools.defstructure.annotation.Definition;
import de.unileipzig.irptools.defstructure.annotation.FieldDefinition;
import de.unileipzig.irptools.graphviz.LayoutAlgorithm;
import de.unileipzig.irptools.graphviz.OutputFormat;
import de.unileipzig.irptools.graphviz.StandardLayoutAlgorithm;
import de.unileipzig.irptools.graphviz.StandardOutputFormat;
import de.unileipzig.irptools.util.CopyCache;
import de.unileipzig.irptools.util.Copyable;
import de.unileipzig.irptools.util.TreeAnnotationResource;
import de.unileipzig.irptools.util.XorWithoutUnselectRuleBuilder;
import de.unileipzig.irptools.util.log.IRPLogger;

import java.lang.invoke.MethodHandles;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import static de.unileipzig.irpact.io.param.ParamUtil.*;
import static de.unileipzig.irpact.io.param.input.TreeViewStructureEnum.SETT_VISUNETWORK_GENERAL;

/**
 * @author Daniel Abitz
 */
@Definition(global = true)
@LocalizedUiResource.PutClassPath(SETT_VISUNETWORK_GENERAL)
@LocalizedUiResource.XorWithoutUnselectRule
public class InGraphvizGeneral implements Copyable {

    private static final MethodHandles.Lookup L = MethodHandles.lookup();
    public static Class<?> thisClass() {
        return L.lookupClass();
    }
    public static String thisName() {
        return thisClass().getSimpleName();
    }

//    protected static final String[] improvedLayoutFieldNames = {"positionBasedLayout", "freeLayout"};
//    protected static final XorWithoutUnselectRuleBuilder improvedLayoutBuilder = new XorWithoutUnselectRuleBuilder()
//            .withKeyModifier(buildDefaultScalarNameOperator(thisClass()))
//            .withTrueValue(Constants.TRUE1)
//            .withFalseValue(Constants.FALSE0)
//            .withKeys(improvedLayoutFieldNames);

    @TreeAnnotationResource.Init
    public static void initRes(LocalizedUiResource res) {
    }
    @TreeAnnotationResource.Apply
    public static void applyRes(LocalizedUiResource res) {
//        setRules(res, thisClass(), improvedLayoutFieldNames, improvedLayoutBuilder);
    }

    private static final IRPLogger LOGGER = IRPLogging.getLogger(thisClass());

    @FieldDefinition
    @LocalizedUiResource.AddEntry
    @LocalizedUiResource.SimpleSet(
            boolDomain = true
    )
    public boolean storeEndImage = false;

    @FieldDefinition
    @LocalizedUiResource.AddEntry
    @LocalizedUiResource.SimpleSet(
            boolDomain = true
    )
    public boolean storeDotFile = false;

    @FieldDefinition
    @LocalizedUiResource.AddEntry
    @LocalizedUiResource.SimpleSet(
            g0Domain = true,
            intDefault = 1000,
            pixelUnit = true
    )
    public double preferredImageWidth = 1000;

    @FieldDefinition
    @LocalizedUiResource.AddEntry
    @LocalizedUiResource.SimpleSet(
            g0Domain = true,
            intDefault = 1000,
            pixelUnit = true
    )
    public double preferredImageHeight = 1000;

    @FieldDefinition
    @LocalizedUiResource.AddEntry
    @LocalizedUiResource.SimpleSet(
            boolDomain = true,
            boolDefault = true
    )
    @LocalizedUiResource.XorWithoutUnselectRuleEntry
    public boolean positionBasedLayout = true;

    @FieldDefinition
    @LocalizedUiResource.AddEntry
    @LocalizedUiResource.SimpleSet(
            boolDomain = true
    )
    @LocalizedUiResource.XorWithoutUnselectRuleEntry
    public boolean freeLayout = false;

    @FieldDefinition
    @LocalizedUiResource.AddEntry
    @LocalizedUiResource.SimpleSet(
            boolDomain = true
    )
    public boolean keepAspectRatio = false;

    @FieldDefinition
    @LocalizedUiResource.AddEntry
    @LocalizedUiResource.SimpleSet(
            boolDomain = true
    )
    public boolean useDefaultPositionIfMissing = false;

    //helper
    protected Charset terminalCharset = StandardCharsets.UTF_8;
    public Charset getTerminalCharset() {
        return terminalCharset;
    }
    public void setTerminalCharset(Charset terminalCharset) {
        this.terminalCharset = terminalCharset;
    }

    //helper
    protected boolean preferSfdp = true;
    public void setPreferSfdp(boolean preferSfdp) {
        this.preferSfdp = preferSfdp;
    }
    public boolean isPreferSfdp() {
        return preferSfdp;
    }

    public InGraphvizGeneral() {
    }

    @Override
    public InGraphvizGeneral copy(CopyCache cache) {
        return cache.copyIfAbsent(this, this::newCopy);
    }

    public InGraphvizGeneral newCopy(CopyCache cache) {
        InGraphvizGeneral copy = new InGraphvizGeneral();
        copy.storeEndImage = storeEndImage;
        copy.storeDotFile = storeDotFile;
        copy.preferredImageWidth = preferredImageWidth;
        copy.preferredImageHeight = preferredImageHeight;
        copy.positionBasedLayout = positionBasedLayout;
        copy.freeLayout = freeLayout;
        copy.useDefaultPositionIfMissing = useDefaultPositionIfMissing;
        copy.keepAspectRatio = keepAspectRatio;
        copy.terminalCharset = terminalCharset;
        return copy;
    }

    public void setStoreEndImage(boolean storeEndImage) {
        this.storeEndImage = storeEndImage;
    }

    public boolean isStoreEndImage() {
        return storeEndImage;
    }

    public void setStoreDotFile(boolean storeDotFile) {
        this.storeDotFile = storeDotFile;
    }

    public boolean isStoreDotFile() {
        return storeDotFile;
    }

    public boolean isFixedNeatoPosition() {
        return positionBasedLayout;
    }

    public boolean shouldKeepAspectRatio() {
        return keepAspectRatio;
    }

    public void setKeepAspectRatio(boolean keepAspectRatio) {
        this.keepAspectRatio = keepAspectRatio;
    }

    public OutputFormat getOutputFormat() {
        return StandardOutputFormat.PNG;
    }

    public void setPreferredImageHeight(double preferredImageHeight) {
        this.preferredImageHeight = Math.max(0, preferredImageHeight);
    }

    public double getPreferredImageHeight() {
        return preferredImageHeight;
    }

    public void setPreferredImageWidth(double preferredImageWidth) {
        this.preferredImageWidth = Math.max(0, preferredImageWidth);
    }

    public double getPreferredImageWidth() {
        return preferredImageWidth;
    }

    public void setPreferredImageSize(double width, double height) {
        setPreferredImageWidth(width);
        setPreferredImageHeight(height);
    }

    public void setPreferredImageSize(double size) {
        setPreferredImageSize(size, size);
    }

    public void setUseDefaultPositionIfMissing(boolean useDefaultPositionIfMissing) {
        this.useDefaultPositionIfMissing = useDefaultPositionIfMissing;
    }

    public boolean isUseDefaultPositionIfMissing() {
        return useDefaultPositionIfMissing;
    }

    public LayoutAlgorithm getLayoutAlgorithm() throws ParsingException {
        List<LayoutAlgorithm> algorithms = new ArrayList<>();
        if(positionBasedLayout) algorithms.add(StandardLayoutAlgorithm.NEATO);
        if(freeLayout) algorithms.add(preferSfdp ? StandardLayoutAlgorithm.SFDP : StandardLayoutAlgorithm.FDP);

        switch(algorithms.size()) {
            case 0:
                throw new ParsingException("Missing algorithm");

            case 1:
                return algorithms.get(0);

            default:
                throw new ParsingException("Multiple algorithms: " + algorithms);
        }
    }

    public void setPositionBasedLayoutAlgorithm(boolean positionBased) {
        this.positionBasedLayout = positionBased;
        this.freeLayout = !positionBased;
    }

    public void setLayoutAlgorithm(LayoutAlgorithm algorithm) {
        positionBasedLayout = false;
        freeLayout = false;

        if(algorithm instanceof StandardLayoutAlgorithm) {
            StandardLayoutAlgorithm salg = (StandardLayoutAlgorithm) algorithm;
            switch(salg) {
                case NEATO:
                    positionBasedLayout = true;
                    break;

                case FDP:
                case SFDP:
                    freeLayout = true;
                    break;
                default:
                    throw new IllegalArgumentException("unsupported standard algorithm: " + algorithm);
            }
        } else {
            throw new IllegalArgumentException("unsupported algorithm: " + algorithm);
        }
    }
}
