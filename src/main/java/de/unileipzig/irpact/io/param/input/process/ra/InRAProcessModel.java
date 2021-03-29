package de.unileipzig.irpact.io.param.input.process.ra;

import de.unileipzig.irpact.commons.util.Rnd;
import de.unileipzig.irpact.commons.exception.ParsingException;
import de.unileipzig.irpact.core.log.IRPLogging;
import de.unileipzig.irpact.core.log.IRPSection;
import de.unileipzig.irpact.core.process.ra.RAModelData;
import de.unileipzig.irpact.core.process.ra.RAProcessModel;
import de.unileipzig.irpact.core.process.ra.npv.NPVXlsxData;
import de.unileipzig.irpact.io.param.ParamUtil;
import de.unileipzig.irpact.io.param.input.InputParser;
import de.unileipzig.irpact.io.param.input.file.InPVFile;
import de.unileipzig.irpact.io.param.input.process.InProcessModel;
import de.unileipzig.irptools.defstructure.annotation.Definition;
import de.unileipzig.irptools.defstructure.annotation.FieldDefinition;
import de.unileipzig.irptools.util.TreeAnnotationResource;
import de.unileipzig.irptools.util.log.IRPLogger;

import java.lang.invoke.MethodHandles;

import static de.unileipzig.irpact.io.param.IOConstants.PROCESS_MODEL;
import static de.unileipzig.irpact.io.param.ParamUtil.addEntry;
import static de.unileipzig.irpact.io.param.ParamUtil.putClassPath;

/**
 * @author Daniel Abitz
 */
@Definition
public class InRAProcessModel implements InProcessModel {

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
        putClassPath(res, thisClass(), PROCESS_MODEL, thisName());

        addEntry(res, thisClass(), "a");
        addEntry(res, thisClass(), "b");
        addEntry(res, thisClass(), "c");
        addEntry(res, thisClass(), "d");

        addEntry(res, thisClass(), "adopterPoints");
        addEntry(res, thisClass(), "interestedPoints");
        addEntry(res, thisClass(), "awarePoints");
        addEntry(res, thisClass(), "unknownPoints");

        addEntry(res, thisClass(), "logisticFactor");

        addEntry(res, thisClass(), "nodeFilterScheme");
        addEntry(res, thisClass(), "pvFile");
        addEntry(res, thisClass(), "uncertaintyGroupAttributes");
    }

    private static final IRPLogger LOGGER = IRPLogging.getLogger(thisClass());

    public String _name;

    @FieldDefinition
    public double a;

    @FieldDefinition
    public double b;

    @FieldDefinition
    public double c;

    @FieldDefinition
    public double d;

    @FieldDefinition
    public int adopterPoints = 3;

    @FieldDefinition
    public int interestedPoints = 2;

    @FieldDefinition
    public int awarePoints = 1;

    @FieldDefinition
    public int unknownPoints = 0;

    @FieldDefinition
    public double logisticFactor;

    @FieldDefinition
    public InRAProcessPlanNodeFilterScheme[] nodeFilterScheme;

    @FieldDefinition
    public InPVFile[] pvFile;

    @FieldDefinition
    public InUncertaintyGroupAttribute[] uncertaintyGroupAttributes = new InUncertaintyGroupAttribute[0];

    public InRAProcessModel() {
    }

    public InRAProcessModel(
            String name,
            double a, double b, double c, double d,
            int adopterPoints, int interestedPoints, int awarePoints, int unknownPoints,
            double logisticFactor,
            InPVFile pvFile,
            InUncertaintyGroupAttribute[] uncertaintyGroupAttributes) {
        this._name = name;
        this.a = a;
        this.b = b;
        this.c = c;
        this.d = d;
        this.adopterPoints = adopterPoints;
        this.interestedPoints = interestedPoints;
        this.awarePoints = awarePoints;
        this.unknownPoints = unknownPoints;
        this.logisticFactor = logisticFactor;
        setPvFile(pvFile);
        this.uncertaintyGroupAttributes = uncertaintyGroupAttributes;
    }

    @Override
    public String getName() {
        return _name;
    }

    public void setName(String name) {
        this._name = name;
    }

    public double getA() {
        return a;
    }

    public void setA(double a) {
        this.a = a;
    }

    public double getB() {
        return b;
    }

    public void setB(double b) {
        this.b = b;
    }

    public double getC() {
        return c;
    }

    public void setC(double c) {
        this.c = c;
    }

    public double getD() {
        return d;
    }

    public void setD(double d) {
        this.d = d;
    }

    public int getAdopterPoints() {
        return adopterPoints;
    }

    public void setAdopterPoints(int adopterPoints) {
        this.adopterPoints = adopterPoints;
    }

    public int getInterestedPoints() {
        return interestedPoints;
    }

    public void setInterestedPoints(int interestedPoints) {
        this.interestedPoints = interestedPoints;
    }

    public int getAwarePoints() {
        return awarePoints;
    }

    public void setAwarePoints(int awarePoints) {
        this.awarePoints = awarePoints;
    }

    public int getUnknownPoints() {
        return unknownPoints;
    }

    public void setUnknownPoints(int unknownPoints) {
        this.unknownPoints = unknownPoints;
    }

    public double getLogisticFactor() {
        return logisticFactor;
    }

    public void setLogisticFactor(double logisticFactor) {
        this.logisticFactor = logisticFactor;
    }

    public InRAProcessPlanNodeFilterScheme getNodeFilterScheme() throws ParsingException {
        return ParamUtil.getInstance(nodeFilterScheme, "nodeFilterScheme");
    }

    public void setNodeFilterScheme(InRAProcessPlanNodeFilterScheme nodeFilterScheme) {
        this.nodeFilterScheme = new InRAProcessPlanNodeFilterScheme[]{nodeFilterScheme};
    }

    public InPVFile getPvFile() throws ParsingException {
        return ParamUtil.getInstance(pvFile, "PvFile");
    }

    public void setPvFile(InPVFile pvFile) {
        this.pvFile = new InPVFile[]{pvFile};
    }

    public InUncertaintyGroupAttribute[] getUncertaintyGroupAttributes() {
        return uncertaintyGroupAttributes;
    }

    public void setUncertaintyGroupAttributes(InUncertaintyGroupAttribute[] uncertaintyGroupAttributes) {
        this.uncertaintyGroupAttributes = uncertaintyGroupAttributes;
    }

    @Override
    public Object parse(InputParser parser) throws ParsingException {
        RAModelData data = new RAModelData();
        data.setA(getA());
        data.setB(getB());
        data.setC(getC());
        data.setD(getD());
        data.setAdopterPoints(getAdopterPoints());
        data.setInterestedPoints(getInterestedPoints());
        data.setAwarePoints(getAwarePoints());
        data.setUnknownPoints(getUnknownPoints());
        data.setLogisticFactor(getLogisticFactor());

        Rnd rnd = parser.deriveRnd();
        LOGGER.debug(IRPSection.INITIALIZATION_PARAMETER, "RAProcessModel '{}' uses seed: {}", getName(), rnd.getInitialSeed());

        RAProcessModel model = new RAProcessModel();
        model.setName(getName());
        model.setEnvironment(parser.getEnvironment());
        model.setModelData(data);
        model.setRnd(rnd);

        for(InUncertaintyGroupAttribute inUncert: getUncertaintyGroupAttributes()) {
            inUncert.setup(parser, model);
        }

        NPVXlsxData xlsxData = parser.parseEntityTo(getPvFile());
        model.setNpvData(xlsxData);

        return model;
    }
}
