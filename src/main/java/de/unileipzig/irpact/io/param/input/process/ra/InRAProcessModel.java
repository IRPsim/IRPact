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
import de.unileipzig.irpact.core.product.handler.NewProductHandler;
import de.unileipzig.irpact.io.param.ParamUtil;
import de.unileipzig.irpact.core.start.IRPactInputParser;
import de.unileipzig.irpact.core.start.InputParser;
import de.unileipzig.irpact.io.param.input.file.InPVFile;
import de.unileipzig.irpact.io.param.input.process.InProcessModel;
import de.unileipzig.irpact.io.param.input.process.ra.uncert.InUncertainty;
import de.unileipzig.irpact.io.param.input.product.initial.InNewProductHandler;
import de.unileipzig.irptools.defstructure.annotation.Definition;
import de.unileipzig.irptools.defstructure.annotation.FieldDefinition;
import de.unileipzig.irptools.util.CopyCache;
import de.unileipzig.irptools.util.TreeAnnotationResource;
import de.unileipzig.irptools.util.log.IRPLogger;

import java.lang.invoke.MethodHandles;
import java.util.Collection;

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

        addEntry(res, thisClass(), "skipAwareness");
        addEntry(res, thisClass(), "skipFeasibility");
        addEntry(res, thisClass(), "forceEvaluate");
        addEntry(res, thisClass(), "a");
        addEntry(res, thisClass(), "b");
        addEntry(res, thisClass(), "c");
        addEntry(res, thisClass(), "d");
        addEntry(res, thisClass(), "aWeight");
        addEntry(res, thisClass(), "bWeight");
        addEntry(res, thisClass(), "cWeight");
        addEntry(res, thisClass(), "dWeight");
        addEntry(res, thisClass(), "adopterPoints");
        addEntry(res, thisClass(), "interestedPoints");
        addEntry(res, thisClass(), "awarePoints");
        addEntry(res, thisClass(), "unknownPoints");
        addEntry(res, thisClass(), "logisticFactor");
        addEntry(res, thisClass(), "speedOfConvergence");
        addEntry(res, thisClass(), "attitudeGap");
        addEntry(res, thisClass(), "chanceNeutral");
        addEntry(res, thisClass(), "chanceConvergence");
        addEntry(res, thisClass(), "chanceDivergence");
        addEntry(res, thisClass(), "weightFT");
        addEntry(res, thisClass(), "weightNPV");
        addEntry(res, thisClass(), "weightSocial");
        addEntry(res, thisClass(), "weightLocal");
        addEntry(res, thisClass(), "communicationFactor");
        addEntry(res, thisClass(), "rewireFactor");
        addEntry(res, thisClass(), "adoptionCertaintyBase");
        addEntry(res, thisClass(), "adoptionCertaintyFactor");
        addEntry(res, thisClass(), "nodeFilterScheme");
        addEntry(res, thisClass(), "pvFile");
        addEntry(res, thisClass(), "uncertainties");
        addEntry(res, thisClass(), "newProductHandlers");


        setDomain(res, thisClass(), "skipAwareness", DOMAIN_BOOLEAN);
        setDomain(res, thisClass(), "skipFeasibility", DOMAIN_BOOLEAN);
        setDomain(res, thisClass(), "forceEvaluate", DOMAIN_BOOLEAN);
        setDomain(res, thisClass(), "chanceNeutral", DOMAIN_CLOSED_0_1);
        setDomain(res, thisClass(), "chanceConvergence", DOMAIN_CLOSED_0_1);
        setDomain(res, thisClass(), "chanceDivergence", DOMAIN_CLOSED_0_1);


        setDefault(res, thisClass(), "skipAwareness", VALUE_FALSE);
        setDefault(res, thisClass(), "skipFeasibility", VALUE_FALSE);
        setDefault(res, thisClass(), "forceEvaluate", VALUE_FALSE);
        setDefault(res, thisClass(), "a", VALUE_0_25);
        setDefault(res, thisClass(), "b", VALUE_0_25);
        setDefault(res, thisClass(), "c", VALUE_0_25);
        setDefault(res, thisClass(), "d", VALUE_0_25);
        setDefault(res, thisClass(), "aWeight", VALUE_1);
        setDefault(res, thisClass(), "bWeight", VALUE_1);
        setDefault(res, thisClass(), "cWeight", VALUE_1);
        setDefault(res, thisClass(), "dWeight", VALUE_1);
        setDefault(res, thisClass(), "weightFT", VALUE_0_5);
        setDefault(res, thisClass(), "weightNPV", VALUE_0_5);
        setDefault(res, thisClass(), "weightSocial", VALUE_0_5);
        setDefault(res, thisClass(), "weightLocal", VALUE_0_5);
        setDefault(res, thisClass(), "communicationFactor", VALUE_1);
        setDefault(res, thisClass(), "rewireFactor", VALUE_1);
        setDefault(res, thisClass(), "adoptionCertaintyBase", VALUE_1);
        setDefault(res, thisClass(), "adoptionCertaintyFactor", VALUE_1);
        setDefault(res, thisClass(), "adopterPoints", varargs(RAModelData.DEFAULT_ADOPTER_POINTS));
        setDefault(res, thisClass(), "interestedPoints", varargs(RAModelData.DEFAULT_INTERESTED_POINTS));
        setDefault(res, thisClass(), "awarePoints", varargs(RAModelData.DEFAULT_AWARE_POINTS));
        setDefault(res, thisClass(), "unknownPoints", varargs(RAModelData.DEFAULT_UNKNOWN_POINTS));
        setDefault(res, thisClass(), "logisticFactor", varargs(RAConstants.DEFAULT_LOGISTIC_FACTOR));
        setDefault(res, thisClass(), "speedOfConvergence", varargs(RAConstants.DEFAULT_SPEED_OF_CONVERGENCE));
        setDefault(res, thisClass(), "attitudeGap", varargs(RAConstants.DEFAULT_ATTIDUTE_GAP));
        setDefault(res, thisClass(), "chanceNeutral", varargs(RAConstants.DEFAULT_NEUTRAL_CHANCE));
        setDefault(res, thisClass(), "chanceConvergence", varargs(RAConstants.DEFAULT_CONVERGENCE_CHANCE));
        setDefault(res, thisClass(), "chanceDivergence", varargs(RAConstants.DEFAULT_DIVERGENCE_CHANCE));
    }

    private static final IRPLogger LOGGER = IRPLogging.getLogger(thisClass());

    public String _name;

    @FieldDefinition
    public boolean skipAwareness = false;

    @FieldDefinition
    public boolean skipFeasibility = false;

    @FieldDefinition
    public boolean forceEvaluate = false;

    @FieldDefinition
    public double a;

    @FieldDefinition
    public double b;

    @FieldDefinition
    public double c;

    @FieldDefinition
    public double d;

    @FieldDefinition
    public double aWeight = 1.0;

    @FieldDefinition
    public double bWeight = 1.0;

    @FieldDefinition
    public double cWeight = 1.0;

    @FieldDefinition
    public double dWeight = 1.0;

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
    public double attitudeGap;

    @FieldDefinition
    public double chanceNeutral;

    @FieldDefinition
    public double chanceConvergence;

    @FieldDefinition
    public double chanceDivergence;

    @FieldDefinition
    public double weightFT;

    @FieldDefinition
    public double weightNPV;

    @FieldDefinition
    public double weightSocial;

    @FieldDefinition
    public double weightLocal;

    @FieldDefinition
    public double communicationFactor = 1.0;

    @FieldDefinition
    public double rewireFactor = 1.0;

    @FieldDefinition
    public double adoptionCertaintyBase = 1.0;

    @FieldDefinition
    public double adoptionCertaintyFactor = 1.0;

    @FieldDefinition
    public InRAProcessPlanNodeFilterScheme[] nodeFilterScheme;

    @FieldDefinition
    public InPVFile[] pvFile;

    @FieldDefinition
    public InUncertainty[] uncertainties;

    @FieldDefinition
    public InNewProductHandler[] newProductHandlers = new InNewProductHandler[0];

    public InRAProcessModel() {
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
        copy.aWeight = aWeight;
        copy.bWeight = bWeight;
        copy.cWeight = cWeight;
        copy.dWeight = dWeight;
        copy.adopterPoints = adopterPoints;
        copy.interestedPoints = interestedPoints;
        copy.awarePoints = awarePoints;
        copy.unknownPoints = unknownPoints;
        copy.logisticFactor = logisticFactor;
        copy.speedOfConvergence = speedOfConvergence;
        copy.chanceNeutral = chanceNeutral;
        copy.chanceConvergence = chanceConvergence;
        copy.chanceDivergence = chanceDivergence;
        copy.attitudeGap = attitudeGap;
        copy.nodeFilterScheme = cache.copyArray(nodeFilterScheme);
        copy.pvFile = cache.copyArray(pvFile);
        copy.uncertainties = cache.copyArray(uncertainties);
        copy.weightFT = weightFT;
        copy.weightNPV = weightNPV;
        copy.weightSocial = weightSocial;
        copy.weightLocal = weightLocal;
        return copy;
    }

    @Override
    public String getName() {
        return _name;
    }

    public void setName(String name) {
        this._name = name;
    }

    public void setDefaultValues() {
        setABCD(0.25);
        setWeightsABCD(1.0);
        setAdopterPoints(RAModelData.DEFAULT_ADOPTER_POINTS);
        setInterestedPoints(RAModelData.DEFAULT_INTERESTED_POINTS);
        setAwarePoints(RAModelData.DEFAULT_AWARE_POINTS);
        setUnknownPoints(RAModelData.DEFAULT_UNKNOWN_POINTS);
        setLogisticFactor(RAConstants.DEFAULT_LOGISTIC_FACTOR);
        setSpeedOfConvergence(RAConstants.DEFAULT_SPEED_OF_CONVERGENCE);
        setAttitudeGap(RAConstants.DEFAULT_ATTIDUTE_GAP);
        setChanceNeutral(RAConstants.DEFAULT_NEUTRAL_CHANCE);
        setChanceConvergence(RAConstants.DEFAULT_CONVERGENCE_CHANCE);
        setChanceDivergence(RAConstants.DEFAULT_DIVERGENCE_CHANCE);
        setWeightFT(0.5);
        setWeightNPV(0.5);
        setWeightLocal(0.5);
        setWeightSocial(0.5);
    }

    public void setABCD(double value) {
        setA(value);
        setB(value);
        setC(value);
        setD(value);
    }

    public void setWeightsABCD(double value) {
        setAWeight(value);
        setBWeight(value);
        setCWeight(value);
        setDWeight(value);
    }

    public void setSkipAwareness(boolean skipAwareness) {
        this.skipAwareness = skipAwareness;
    }

    public boolean isSkipAwareness() {
        return skipAwareness;
    }

    public void setSkipFeasibility(boolean skipFeasibility) {
        this.skipFeasibility = skipFeasibility;
    }

    public boolean isSkipFeasibility() {
        return skipFeasibility;
    }

    public void setForceEvaluate(boolean forceEvaluate) {
        this.forceEvaluate = forceEvaluate;
    }

    public boolean isForceEvaluate() {
        return forceEvaluate;
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

    public double getAWeight() {
        return aWeight;
    }

    public void setAWeight(double aWeight) {
        this.aWeight = aWeight;
    }

    public double getBWeight() {
        return bWeight;
    }

    public void setBWeight(double bWeight) {
        this.bWeight = bWeight;
    }

    public double getCWeight() {
        return cWeight;
    }

    public void setCWeight(double cWeight) {
        this.cWeight = cWeight;
    }

    public double getDWeight() {
        return dWeight;
    }

    public void setDWeight(double dWeight) {
        this.dWeight = dWeight;
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

    public void setAttitudeGap(double attitudeGap) {
        this.attitudeGap = attitudeGap;
    }

    public double getAttitudeGap() {
        return attitudeGap;
    }

    public void setWeightFT(double weightFT) {
        this.weightFT = weightFT;
    }

    public double getWeightFT() {
        return weightFT;
    }

    public void setWeightNPV(double weightNPV) {
        this.weightNPV = weightNPV;
    }

    public double getWeightNPV() {
        return weightNPV;
    }

    public void setWeightSocial(double weightSocial) {
        this.weightSocial = weightSocial;
    }

    public double getWeightSocial() {
        return weightSocial;
    }

    public void setWeightLocal(double weightLocal) {
        this.weightLocal = weightLocal;
    }

    public double getWeightLocal() {
        return weightLocal;
    }

    public void setCommunicationFactor(double communicationFactor) {
        this.communicationFactor = communicationFactor;
    }

    public double getCommunicationFactor() {
        return communicationFactor;
    }

    public void setRewireFactor(double rewireFactor) {
        this.rewireFactor = rewireFactor;
    }

    public double getRewireFactor() {
        return rewireFactor;
    }

    public void setAdoptionCertaintyBase(double adoptionCertaintyBase) {
        this.adoptionCertaintyBase = adoptionCertaintyBase;
    }

    public double getAdoptionCertaintyBase() {
        return adoptionCertaintyBase;
    }

    public void setAdoptionCertaintyFactor(double adoptionCertaintyFactor) {
        this.adoptionCertaintyFactor = adoptionCertaintyFactor;
    }

    public double getAdoptionCertaintyFactor() {
        return adoptionCertaintyFactor;
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

    public void setUncertainty(InUncertainty uncertainty) {
        this.uncertainties = new InUncertainty[]{uncertainty};
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

    public void setNewProductHandlers(InNewProductHandler[] newProductHandlers) {
        this.newProductHandlers = newProductHandlers;
    }
    public void setNewProductHandlers(Collection<? extends InNewProductHandler> newProductHandlers) {
        setNewProductHandlers(newProductHandlers.toArray(new InNewProductHandler[0]));
    }
    public void addNewProductHandle(InNewProductHandler newProductHandler) {
        newProductHandlers = add(newProductHandlers, newProductHandler);
    }

    public InNewProductHandler[] getNewProductHandlers() {
        return newProductHandlers;
    }
    public boolean hasNewProductHandlers() {
        return len(newProductHandlers) > 0;
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
        data.setAWeight(getAWeight());
        data.setBWeight(getBWeight());
        data.setCWeight(getCWeight());
        data.setDWeight(getDWeight());
        data.setAdopterPoints(getAdopterPoints());
        data.setInterestedPoints(getInterestedPoints());
        data.setAwarePoints(getAwarePoints());
        data.setUnknownPoints(getUnknownPoints());
        data.setLogisticFactor(getLogisticFactor());
        data.setWeightLocal(getWeightLocal());
        data.setWeightSocial(getWeightSocial());
        data.setCommunicationFactor(getCommunicationFactor());
        data.setRewireFactor(getRewireFactor());

        Rnd rnd = parser.deriveRnd();
        LOGGER.trace(IRPSection.INITIALIZATION_PARAMETER, "RAProcessModel '{}' uses seed: {}", getName(), rnd.getInitialSeed());

        RAProcessModel model = new RAProcessModel();
        model.setName(getName());
        model.setEnvironment(parser.getEnvironment());
        model.setModelData(data);
        model.setRnd(rnd);
        model.setSpeedOfConvergence(getSpeedOfConvergence());

        model.setSkipAwareness(isSkipAwareness());
        model.setSkipFeasibility(isSkipFeasibility());
        model.setForceEvaluate(isForceEvaluate());
        LOGGER.trace(IRPSection.INITIALIZATION_PARAMETER, "[{}] skip awareness: {}", getName(), isSkipAwareness());
        LOGGER.trace(IRPSection.INITIALIZATION_PARAMETER, "[{}] skip feasibility: {}", getName(), isSkipFeasibility());
        LOGGER.trace(IRPSection.INITIALIZATION_PARAMETER, "[{}] force evaluate: {}", getName(), isForceEvaluate());

        model.setAdoptionCertaintyBase(adoptionCertaintyBase);
        model.setAdoptionCertaintyFactor(adoptionCertaintyFactor);
        LOGGER.trace(IRPSection.INITIALIZATION_PARAMETER, "[{}] adoptionCertaintyBase={}, adoptionCertaintyFactor={}, hasAdoptionCertainty={}", getName(), adoptionCertaintyBase, adoptionCertaintyFactor, model.hasAdoptionCertainty());

        Object[] params = { model.getName(), model.getUncertaintyManager(), model.getSpeedOfConvergence() };
        for(InUncertainty uncertainty: getUncertainties()) {
            uncertainty.setup(parser, params);
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
        algorithm.setAttitudeGap(getAttitudeGap());
        algorithm.setWeightes(getChanceNeutral(), getChanceConvergence(), getChanceDivergence());
        LOGGER.trace(IRPSection.INITIALIZATION_PARAMETER, "AttitudeGapRelativeAgreementAlgorithm '{}' uses seed: {}", algorithm.getName(), raRnd.getInitialSeed());
        model.setRelativeAgreementAlgorithm(algorithm);

        if(hasNewProductHandlers()) {
            for(InNewProductHandler inHandler: getNewProductHandlers()) {
                LOGGER.trace(IRPSection.INITIALIZATION_PARAMETER, "parse NewProductHandler '{}'", inHandler.getName());
                NewProductHandler handler = parser.parseEntityTo(inHandler);
                model.addNewProductHandler(handler);
            }
        }

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
