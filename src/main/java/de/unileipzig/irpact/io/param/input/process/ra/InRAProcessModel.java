package de.unileipzig.irpact.io.param.input.process.ra;

import de.unileipzig.irpact.commons.util.Rnd;
import de.unileipzig.irpact.commons.exception.ParsingException;
import de.unileipzig.irpact.core.logging.IRPLogging;
import de.unileipzig.irpact.core.logging.IRPSection;
import de.unileipzig.irpact.core.process.ProcessModelManager;
import de.unileipzig.irpact.core.process.filter.DisabledProcessPlanNodeFilterScheme;
import de.unileipzig.irpact.core.process.filter.ProcessPlanNodeFilterScheme;
import de.unileipzig.irpact.core.process.ra.RAConstants;
import de.unileipzig.irpact.core.process.ra.RAModelData;
import de.unileipzig.irpact.core.process.ra.RAProcessModel;
import de.unileipzig.irpact.core.process.ra.npv.NPVXlsxData;
import de.unileipzig.irpact.core.process.ra.alg.AttitudeGapRelativeAgreementAlgorithm;
import de.unileipzig.irpact.io.param.ParamUtil;
import de.unileipzig.irpact.io.param.input.IRPactInputParser;
import de.unileipzig.irpact.io.param.input.InputParser;
import de.unileipzig.irpact.io.param.input.file.InPVFile;
import de.unileipzig.irpact.io.param.input.process.InProcessModel;
import de.unileipzig.irpact.io.param.input.process.ra.uncert.InUncertainty;
import de.unileipzig.irptools.defstructure.annotation.Definition;
import de.unileipzig.irptools.defstructure.annotation.FieldDefinition;
import de.unileipzig.irptools.util.CopyCache;
import de.unileipzig.irptools.util.TreeAnnotationResource;
import de.unileipzig.irptools.util.log.IRPLogger;

import java.lang.invoke.MethodHandles;

import static de.unileipzig.irpact.io.param.IOConstants.PROCESS_MODEL;
import static de.unileipzig.irpact.io.param.ParamUtil.*;

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
        addEntry(res, thisClass(), "speedOfConvergence");
        addEntry(res, thisClass(), "attitudeGab");
        addEntry(res, thisClass(), "chanceNeutral");
        addEntry(res, thisClass(), "chanceConvergence");
        addEntry(res, thisClass(), "chanceDivergence");
        addEntry(res, thisClass(), "nodeFilterScheme");
        addEntry(res, thisClass(), "pvFile");
        addEntry(res, thisClass(), "uncertainties");


        setDomain(res, thisClass(), "chanceNeutral", CLOSED_0_1_DOMAIN);
        setDomain(res, thisClass(), "chanceConvergence", CLOSED_0_1_DOMAIN);
        setDomain(res, thisClass(), "chanceDivergence", CLOSED_0_1_DOMAIN);


        setDefault(res, thisClass(), "a", varargs(0.25));
        setDefault(res, thisClass(), "b", varargs(0.25));
        setDefault(res, thisClass(), "c", varargs(0.25));
        setDefault(res, thisClass(), "d", varargs(0.25));
        setDefault(res, thisClass(), "adopterPoints", varargs(RAModelData.DEFAULT_ADOPTER_POINTS));
        setDefault(res, thisClass(), "interestedPoints", varargs(RAModelData.DEFAULT_INTERESTED_POINTS));
        setDefault(res, thisClass(), "awarePoints", varargs(RAModelData.DEFAULT_AWARE_POINTS));
        setDefault(res, thisClass(), "unknownPoints", varargs(RAModelData.DEFAULT_UNKNOWN_POINTS));
        setDefault(res, thisClass(), "logisticFactor", varargs(RAConstants.DEFAULT_LOGISTIC_FACTOR));
        setDefault(res, thisClass(), "speedOfConvergence", varargs(RAConstants.DEFAULT_SPEED_OF_CONVERGENCE));
        setDefault(res, thisClass(), "attitudeGab", varargs(RAConstants.DEFAULT_ATTIDUTE_GAB));
        setDefault(res, thisClass(), "chanceNeutral", varargs(RAConstants.DEFAULT_NEUTRAL_CHANCE));
        setDefault(res, thisClass(), "chanceConvergence", varargs(RAConstants.DEFAULT_CONVERGENCE_CHANCE));
        setDefault(res, thisClass(), "chanceDivergence", varargs(RAConstants.DEFAULT_DIVERGENCE_CHANCE));
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
    public int adopterPoints;

    @FieldDefinition
    public int interestedPoints;

    @FieldDefinition
    public int awarePoints;

    @FieldDefinition
    public int unknownPoints;

    @FieldDefinition
    public double logisticFactor;

    @FieldDefinition
    public double speedOfConvergence;

    @FieldDefinition
    public double attitudeGab;

    @FieldDefinition
    public double chanceNeutral;

    @FieldDefinition
    public double chanceConvergence;

    @FieldDefinition
    public double chanceDivergence;

    @FieldDefinition
    public InRAProcessPlanNodeFilterScheme[] nodeFilterScheme;

    @FieldDefinition
    public InPVFile[] pvFile;

    @FieldDefinition
    public InUncertainty[] uncertainties;

    public InRAProcessModel() {
    }

    public InRAProcessModel(
            String name,
            double a, double b, double c, double d,
            int adopterPoints, int interestedPoints, int awarePoints, int unknownPoints,
            double logisticFactor,
            InRAProcessPlanNodeFilterScheme filterScheme,
            InPVFile pvFile,
            InUncertainty[] uncertainties) {
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
        setNodeFilterScheme(filterScheme);
        setPvFile(pvFile);
        setUncertainties(uncertainties);
    }

    @Override
    public InRAProcessModel copy(CopyCache cache) {
        return cache.copyIfAbsent(this, this::newCopy);
    }

    public InRAProcessModel newCopy(CopyCache cache) {
        InRAProcessModel copy = new InRAProcessModel();
        copy.a = a;
        copy.b = b;
        copy.c = c;
        copy.d = d;
        copy.adopterPoints = adopterPoints;
        copy.interestedPoints = interestedPoints;
        copy.awarePoints = awarePoints;
        copy.unknownPoints = unknownPoints;
        copy.logisticFactor = logisticFactor;
        copy.speedOfConvergence = speedOfConvergence;
        copy.chanceNeutral = chanceNeutral;
        copy.chanceConvergence = chanceConvergence;
        copy.chanceDivergence = chanceDivergence;
        copy.attitudeGab = attitudeGab;
        copy.nodeFilterScheme = cache.copyArray(nodeFilterScheme);
        copy.pvFile = cache.copyArray(pvFile);
        copy.uncertainties = cache.copyArray(uncertainties);
        return copy;
    }

    @Override
    public String getName() {
        return _name;
    }

    public void setName(String name) {
        this._name = name;
    }

    public void setABCD(double value) {
        setA(value);
        setB(value);
        setC(value);
        setD(value);
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

    public void setDefaultPoints() {
        setAdopterPoints(RAModelData.DEFAULT_ADOPTER_POINTS);
        setInterestedPoints(RAModelData.DEFAULT_INTERESTED_POINTS);
        setAwarePoints(RAModelData.DEFAULT_AWARE_POINTS);
        setUnknownPoints(RAModelData.DEFAULT_UNKNOWN_POINTS);
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

    public void setSpeedOfConvergence(double speedOfConvergence) {
        this.speedOfConvergence = speedOfConvergence;
    }

    public double getSpeedOfConvergence() {
        return speedOfConvergence;
    }

    public void setChanceNeutral(double chanceNeutral) {
        this.chanceNeutral = chanceNeutral;
    }

    public double getChanceNeutral() {
        return chanceNeutral;
    }

    public void setChanceConvergence(double chanceConvergence) {
        this.chanceConvergence = chanceConvergence;
    }

    public double getChanceConvergence() {
        return chanceConvergence;
    }

    public void setChanceDivergence(double chanceDivergence) {
        this.chanceDivergence = chanceDivergence;
    }

    public double getChanceDivergence() {
        return chanceDivergence;
    }

    public void setAttitudeGab(double attitudeGab) {
        this.attitudeGab = attitudeGab;
    }

    public double getAttitudeGab() {
        return attitudeGab;
    }

    public boolean hasNodeFilterScheme() {
        return ParamUtil.len(nodeFilterScheme) > 0;
    }

    public InRAProcessPlanNodeFilterScheme getNodeFilterScheme() throws ParsingException {
        return ParamUtil.getInstance(nodeFilterScheme, "nodeFilterScheme");
    }

    public void setNodeFilterScheme(InRAProcessPlanNodeFilterScheme nodeFilterScheme) {
        if(nodeFilterScheme == null) {
            this.nodeFilterScheme = new InRAProcessPlanNodeFilterScheme[0];
        } else {
            this.nodeFilterScheme = new InRAProcessPlanNodeFilterScheme[]{nodeFilterScheme};
        }
    }

    public boolean hasPvFile() {
        return pvFile != null && pvFile.length > 0;
    }

    public InPVFile getPvFile() throws ParsingException {
        return ParamUtil.getInstance(pvFile, "PvFile");
    }

    public void setPvFile(InPVFile pvFile) {
        this.pvFile = new InPVFile[]{pvFile};
    }

    public void setUncertainties(InUncertainty[] uncertainties) {
        this.uncertainties = uncertainties;
    }

    public InUncertainty[] getUncertainties() {
        return uncertainties;
    }

    public NPVXlsxData getNPVData(InputParser parser) throws ParsingException {
        return parser.parseEntityTo(getPvFile());
    }

    @Override
    public RAProcessModel parse(IRPactInputParser parser) throws ParsingException {
        if(parser.isRestored()) {
            ProcessModelManager manager = parser.getEnvironment().getProcessModels();
            RAProcessModel model = (RAProcessModel) manager.getProcessModel(getName());
            return update(parser, model);
        }
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
        LOGGER.trace(IRPSection.INITIALIZATION_PARAMETER, "RAProcessModel '{}' uses seed: {}", getName(), rnd.getInitialSeed());

        RAProcessModel model = new RAProcessModel();
        model.setName(getName());
        model.setEnvironment(parser.getEnvironment());
        model.setModelData(data);
        model.setRnd(rnd);
        model.setSpeedOfConvergence(getSpeedOfConvergence());

        for(InUncertainty uncertainty: getUncertainties()) {
            uncertainty.setup(parser, model);
        }

        if(hasNodeFilterScheme()) {
            InRAProcessPlanNodeFilterScheme inFilterScheme = getNodeFilterScheme();
            ProcessPlanNodeFilterScheme filterScheme = parser.parseEntityTo(inFilterScheme);
            model.setNodeFilterScheme(filterScheme);
            LOGGER.trace(IRPSection.INITIALIZATION_PARAMETER, "set node filter scheme '{}'", filterScheme.getName());
        } else {
            model.setNodeFilterScheme(DisabledProcessPlanNodeFilterScheme.INSTANCE);
            LOGGER.trace(IRPSection.INITIALIZATION_PARAMETER, "no node filter scheme specified");
        }

        applyPvFile(parser, model);

        AttitudeGapRelativeAgreementAlgorithm algorithm = new AttitudeGapRelativeAgreementAlgorithm();
        algorithm.setName(model.getName() + "_RA");
        algorithm.setEnvironment(parser.getEnvironment());
        Rnd raRnd = parser.deriveRnd();
        algorithm.setRandom(raRnd);
        algorithm.setAttitudeGap(getAttitudeGab());
        algorithm.setWeightes(getChanceNeutral(), getChanceConvergence(), getChanceDivergence());
        algorithm.setLogDataFallback(false);
        LOGGER.trace(IRPSection.INITIALIZATION_PARAMETER, "AttitudeGapRelativeAgreementAlgorithm '{}' uses seed: {}", algorithm.getName(), raRnd.getInitialSeed());
        model.setRelativeAgreementAlgorithm(algorithm);

        return model;
    }

    public RAProcessModel update(IRPactInputParser parser, RAProcessModel restored) throws ParsingException {
        applyPvFile(parser, restored);
        return restored;
    }

    private void applyPvFile(IRPactInputParser parser, RAProcessModel model) throws ParsingException {
        if(hasPvFile()) {
            LOGGER.trace(IRPSection.INITIALIZATION_PARAMETER, "load pv file '{}'" , getPvFile().getName());
            NPVXlsxData xlsxData = getNPVData(parser);
            model.setNpvData(xlsxData);
        } else {
            LOGGER.trace("no pv file found");
        }
    }
}
