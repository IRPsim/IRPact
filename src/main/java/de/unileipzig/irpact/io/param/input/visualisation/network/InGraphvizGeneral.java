package de.unileipzig.irpact.io.param.input.visualisation.network;

import de.unileipzig.irpact.commons.exception.ParsingException;
import de.unileipzig.irpact.core.logging.IRPLogging;
import de.unileipzig.irpact.io.param.input.InRootUI;
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
import java.util.Arrays;
import java.util.List;

import static de.unileipzig.irpact.io.param.ParamUtil.*;

/**
 * @author Daniel Abitz
 */
@Definition(global = true)
public class InGraphvizGeneral implements Copyable {

    private static final MethodHandles.Lookup L = MethodHandles.lookup();
    public static Class<?> thisClass() {
        return L.lookupClass();
    }
    public static String thisName() {
        return thisClass().getSimpleName();
    }

    protected static final String[] outputFormatFieldNames = {"outputFormatPNG"};
    protected static final XorWithoutUnselectRuleBuilder outputFormatBuilder = new XorWithoutUnselectRuleBuilder()
            .withKeyModifier(buildDefaultScalarNameOperator(thisClass()))
            .withTrueValue(Constants.TRUE1)
            .withFalseValue(Constants.FALSE0)
            .withKeys(outputFormatFieldNames);

    protected static final String[] layoutAlgorithmFieldNames = {"layoutAlgDOT", "layoutAlgNEATO", "layoutAlgFDP", "layoutAlgSFDP", "layoutAlgTWOPI", "layoutAlgCIRCO"};
    protected static final XorWithoutUnselectRuleBuilder layoutAlgorithmBuilder = new XorWithoutUnselectRuleBuilder()
            .withKeyModifier(buildDefaultScalarNameOperator(thisClass()))
            .withTrueValue(Constants.TRUE1)
            .withFalseValue(Constants.FALSE0)
            .withKeys(layoutAlgorithmFieldNames);

    public static void initRes(TreeAnnotationResource res) {
    }
    public static void applyRes(TreeAnnotationResource res) {
        putClassPath(res, thisClass(), InRootUI.SETT_VISUNETWORK_GENERAL);
        addEntry(res, thisClass(), "storeEndImage");
        addEntry(res, thisClass(), "storeDotFile");
        addEntry(res, thisClass(), "scaleFactor");
        addEntry(res, thisClass(), "fixedNeatoPosition");
        addEntry(res, thisClass(), "outputFormatPNG");
        addEntry(res, thisClass(), "layoutAlgDOT");
        addEntry(res, thisClass(), "layoutAlgNEATO");
        addEntry(res, thisClass(), "layoutAlgFDP");
        addEntry(res, thisClass(), "layoutAlgSFDP");
        addEntry(res, thisClass(), "layoutAlgTWOPI");
        addEntry(res, thisClass(), "layoutAlgCIRCO");

        setDefault(res, thisClass(), "storeEndImage", VALUE_FALSE);
        setDefault(res, thisClass(), "storeDotFile", VALUE_FALSE);
        setDefault(res, thisClass(), "scaleFactor", VALUE_ZERO);
        setDefault(res, thisClass(), "fixedNeatoPosition", VALUE_FALSE);
        setDefault(res, thisClass(), outputFormatFieldNames, VALUE_FALSE);
        setDefault(res, thisClass(), "outputFormatPNG", VALUE_TRUE);
        setDefault(res, thisClass(), layoutAlgorithmFieldNames, VALUE_FALSE);
        setDefault(res, thisClass(), "layoutAlgFDP", VALUE_TRUE);

        setRules(res, thisClass(), outputFormatFieldNames, outputFormatBuilder);
        setRules(res, thisClass(), layoutAlgorithmFieldNames, layoutAlgorithmBuilder);

        setDomain(res, thisClass(), "scaleFactor", GEQ0_DOMAIN);
    }

    private static final IRPLogger LOGGER = IRPLogging.getLogger(thisClass());

    @FieldDefinition
    public boolean storeEndImage = false;

    @FieldDefinition
    public boolean storeDotFile = false;

    @FieldDefinition
    public double scaleFactor = 0;

    @FieldDefinition
    public boolean fixedNeatoPosition = false;

    @FieldDefinition
    public boolean outputFormatPNG = true;

    @FieldDefinition
    public boolean layoutAlgDOT = false;

    @FieldDefinition
    public boolean layoutAlgNEATO = false;

    @FieldDefinition
    public boolean layoutAlgFDP = true;

    @FieldDefinition
    public boolean layoutAlgSFDP = false;

    @FieldDefinition
    public boolean layoutAlgTWOPI = false;

    @FieldDefinition
    public boolean layoutAlgCIRCO = false;

    //helper
    protected Charset terminalCharset = StandardCharsets.UTF_8;
    public Charset getTerminalCharset() {
        return terminalCharset;
    }
    public void setTerminalCharset(Charset terminalCharset) {
        this.terminalCharset = terminalCharset;
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
        copy.fixedNeatoPosition = fixedNeatoPosition;
        copy.scaleFactor = scaleFactor;
        copy.outputFormatPNG = outputFormatPNG;
        copy.layoutAlgDOT = layoutAlgDOT;
        copy.layoutAlgNEATO = layoutAlgNEATO;
        copy.layoutAlgFDP = layoutAlgFDP;
        copy.layoutAlgSFDP = layoutAlgSFDP;
        copy.layoutAlgTWOPI = layoutAlgTWOPI;
        copy.layoutAlgCIRCO = layoutAlgCIRCO;
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

    public void setFixedNeatoPosition(boolean fixedNeatoPosition) {
        this.fixedNeatoPosition = fixedNeatoPosition;
    }

    public boolean isFixedNeatoPosition() {
        return fixedNeatoPosition;
    }

    public void setScaleFactor(double scaleFactor) {
        this.scaleFactor = scaleFactor;
    }

    public double getScaleFactor() {
        return scaleFactor;
    }

    public OutputFormat getOutputFormat() throws ParsingException {
        List<OutputFormat> formats = new ArrayList<>();
        if(outputFormatPNG) formats.add(StandardOutputFormat.PNG);

        switch(formats.size()) {
            case 0:
                throw new ParsingException("Missing formats");

            case 1:
                return formats.get(0);

            default:
                throw new ParsingException("Multiple formats: " + formats);
        }
    }

    public void setOutputFormat(OutputFormat format) {
        outputFormatPNG = false;

        if(format == StandardOutputFormat.PNG) {
            outputFormatPNG = true;
        } else {
            throw new IllegalArgumentException("unsupported format: " + format);
        }
    }

    public LayoutAlgorithm getLayoutAlgorithm() throws ParsingException {
        List<LayoutAlgorithm> algorithms = new ArrayList<>();
        if(layoutAlgDOT) algorithms.add(StandardLayoutAlgorithm.DOT);
        if(layoutAlgNEATO) algorithms.add(StandardLayoutAlgorithm.NEATO);
        if(layoutAlgFDP) algorithms.add(StandardLayoutAlgorithm.FDP);
        if(layoutAlgSFDP) algorithms.add(StandardLayoutAlgorithm.SFDP);
        if(layoutAlgTWOPI) algorithms.add(StandardLayoutAlgorithm.TWOPI);
        if(layoutAlgCIRCO) algorithms.add(StandardLayoutAlgorithm.CIRCO);

        switch(algorithms.size()) {
            case 0:
                throw new ParsingException("Missing algorithm");

            case 1:
                return algorithms.get(0);

            default:
                throw new ParsingException("Multiple algorithms: " + algorithms);
        }
    }

    public void setLayoutAlgorithm(LayoutAlgorithm algorithm) {
        layoutAlgDOT = false;
        layoutAlgNEATO = false;
        layoutAlgFDP = false;
        layoutAlgSFDP = false;
        layoutAlgTWOPI = false;
        layoutAlgCIRCO = false;

        if(algorithm instanceof StandardLayoutAlgorithm) {
            StandardLayoutAlgorithm salg = (StandardLayoutAlgorithm) algorithm;
            switch(salg) {
                case DOT:
                    layoutAlgDOT = true;
                    break;
                case NEATO:
                    layoutAlgNEATO = true;
                    break;
                case FDP:
                    layoutAlgFDP = true;
                    break;
                case SFDP:
                    layoutAlgSFDP = true;
                    break;
                case TWOPI:
                    layoutAlgTWOPI = true;
                    break;
                case CIRCO:
                    layoutAlgCIRCO = true;
                    break;
                default:
                    throw new IllegalArgumentException("unsupported standard algorithm: " + algorithm);
            }
        } else {
            throw new IllegalArgumentException("unsupported algorithm: " + algorithm);
        }
    }
}
