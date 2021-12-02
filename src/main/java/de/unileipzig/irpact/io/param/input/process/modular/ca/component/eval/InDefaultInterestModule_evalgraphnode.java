package de.unileipzig.irpact.io.param.input.process.modular.ca.component.eval;

import de.unileipzig.irpact.commons.exception.ParsingException;
import de.unileipzig.irpact.core.logging.IRPLogging;
import de.unileipzig.irpact.core.logging.IRPSection;
import de.unileipzig.irpact.core.process.modular.ca.components.eval.DefaultInterestModule;
import de.unileipzig.irpact.core.process.ra.RAConstants;
import de.unileipzig.irpact.core.process.ra.RAModelData;
import de.unileipzig.irpact.core.start.IRPactInputParser;
import de.unileipzig.irpact.develop.Dev;
import de.unileipzig.irpact.develop.ToRemove;
import de.unileipzig.irpact.io.param.input.TreeViewStructure;
import de.unileipzig.irpact.io.param.input.process.modular.ca.MPMSettings;
import de.unileipzig.irpact.io.param.input.process.modular.ca.component.InConsumerAgentEvaluationModule;
import de.unileipzig.irpact.io.param.input.process.ra.uncert.InUncertaintySupplier;
import de.unileipzig.irptools.defstructure.annotation.Definition;
import de.unileipzig.irptools.defstructure.annotation.FieldDefinition;
import de.unileipzig.irptools.defstructure.annotation.GraphNode;
import de.unileipzig.irptools.util.CopyCache;
import de.unileipzig.irptools.util.TreeAnnotationResource;
import de.unileipzig.irptools.util.log.IRPLogger;

import java.lang.invoke.MethodHandles;

import static de.unileipzig.irpact.io.param.ParamUtil.*;
import static de.unileipzig.irpact.io.param.input.process.modular.ca.MPMSettings.*;
import static de.unileipzig.irpact.io.param.input.process.modular.ca.MPMSettings.EVAL_GRAPHNODE;

/**
 * @author Daniel Abitz
 */
@Definition(
        graphNode = @GraphNode(
                id = MPM_GRAPH,
                label = EVAL_LABEL,
                shape = EVAL_SHAPE,
                color = EVAL_COLOR,
                border = EVAL_BORDER,
                tags = {EVAL_GRAPHNODE}
        )
)
@ToRemove
public class InDefaultInterestModule_evalgraphnode implements InConsumerAgentEvaluationModule {

    private static final MethodHandles.Lookup L = MethodHandles.lookup();
    public static Class<?> thisClass() {
        return L.lookupClass();
    }
    public static String thisName() {
        return thisClass().getSimpleName();
    }

    @TreeAnnotationResource.Init
    public static void initRes(TreeAnnotationResource res) {
    }
    @TreeAnnotationResource.Apply
    public static void applyRes(TreeAnnotationResource res) {
        putClassPath(res, thisClass(), TreeViewStructure.PROCESS_MODULAR2_COMPONENTS_EVAL_DEFAULTINTEREST);
        setShapeColorBorder(res, thisClass(), EVAL_SHAPE, EVAL_COLOR, EVAL_BORDER);

        addEntry(res, thisClass(), "adopterPoints");
        addEntry(res, thisClass(), "interestedPoints");
        addEntry(res, thisClass(), "awarePoints");
        addEntry(res, thisClass(), "unknownPoints");
        addEntry(res, thisClass(), "speedOfConvergence");
        addEntry(res, thisClass(), "attitudeGap");
        addEntryWithDefaultAndDomain(res, thisClass(), "chanceNeutral", varargs(RAConstants.DEFAULT_NEUTRAL_CHANCE), DOMAIN_CLOSED_0_1);
        addEntryWithDefaultAndDomain(res, thisClass(), "chanceConvergence", varargs(RAConstants.DEFAULT_CONVERGENCE_CHANCE), DOMAIN_CLOSED_0_1);
        addEntryWithDefaultAndDomain(res, thisClass(), "chanceDivergence", varargs(RAConstants.DEFAULT_DIVERGENCE_CHANCE), DOMAIN_CLOSED_0_1);
        addEntry(res, thisClass(), "uncertainties");


        setDefault(res, thisClass(), "adopterPoints", varargs(RAModelData.DEFAULT_ADOPTER_POINTS));
        setDefault(res, thisClass(), "interestedPoints", varargs(RAModelData.DEFAULT_INTERESTED_POINTS));
        setDefault(res, thisClass(), "awarePoints", varargs(RAModelData.DEFAULT_AWARE_POINTS));
        setDefault(res, thisClass(), "unknownPoints", varargs(RAModelData.DEFAULT_UNKNOWN_POINTS));
        setDefault(res, thisClass(), "speedOfConvergence", varargs(RAConstants.DEFAULT_SPEED_OF_CONVERGENCE));
        setDefault(res, thisClass(), "logisticFactor", varargs(RAConstants.DEFAULT_LOGISTIC_FACTOR));
    }

    private static final IRPLogger LOGGER = IRPLogging.getLogger(thisClass());

    public String _name;
    @Override
    public String getName() {
        return _name;
    }
    public void setName(String name) {
        this._name = name;
    }

    @FieldDefinition
    public int adopterPoints;
    public int getAdopterPoints() {
        return adopterPoints;
    }
    public void setAdopterPoints(int adopterPoints) {
        this.adopterPoints = adopterPoints;
    }

    @FieldDefinition
    public int interestedPoints;
    public int getInterestedPoints() {
        return interestedPoints;
    }
    public void setInterestedPoints(int interestedPoints) {
        this.interestedPoints = interestedPoints;
    }

    @FieldDefinition
    public int awarePoints;
    public int getAwarePoints() {
        return awarePoints;
    }
    public void setAwarePoints(int awarePoints) {
        this.awarePoints = awarePoints;
    }

    @FieldDefinition
    public int unknownPoints;
    public int getUnknownPoints() {
        return unknownPoints;
    }
    public void setUnknownPoints(int unknownPoints) {
        this.unknownPoints = unknownPoints;
    }

    @FieldDefinition
    public double speedOfConvergence;
    public void setSpeedOfConvergence(double speedOfConvergence) {
        this.speedOfConvergence = speedOfConvergence;
    }
    public double getSpeedOfConvergence() {
        return speedOfConvergence;
    }

    @FieldDefinition
    public double attitudeGap;
    public void setAttitudeGap(double attitudeGap) {
        this.attitudeGap = attitudeGap;
    }
    public double getAttitudeGap() {
        return attitudeGap;
    }

    @FieldDefinition
    public double chanceNeutral;
    public void setChanceNeutral(double chanceNeutral) {
        this.chanceNeutral = chanceNeutral;
    }
    public double getChanceNeutral() {
        return chanceNeutral;
    }

    @FieldDefinition
    public double chanceConvergence;
    public void setChanceConvergence(double chanceConvergence) {
        this.chanceConvergence = chanceConvergence;
    }
    public double getChanceConvergence() {
        return chanceConvergence;
    }

    @FieldDefinition
    public double chanceDivergence;
    public void setChanceDivergence(double chanceDivergence) {
        this.chanceDivergence = chanceDivergence;
    }
    public double getChanceDivergence() {
        return chanceDivergence;
    }

    @FieldDefinition
    public InUncertaintySupplier[] uncertainties = new InUncertaintySupplier[0];
    public void setUncertainty(InUncertaintySupplier uncertainty) {
        this.uncertainties = new InUncertaintySupplier[]{uncertainty};
    }
    public void setUncertainties(InUncertaintySupplier[] uncertainties) {
        this.uncertainties = uncertainties;
    }
    public InUncertaintySupplier[] getUncertainties() {
        return uncertainties;
    }

    public InDefaultInterestModule_evalgraphnode() {
    }

    @Override
    public InDefaultInterestModule_evalgraphnode copy(CopyCache cache) {
        return cache.copyIfAbsent(this, this::newCopy);
    }

    public InDefaultInterestModule_evalgraphnode newCopy(CopyCache cache) {
        InDefaultInterestModule_evalgraphnode copy = new InDefaultInterestModule_evalgraphnode();
        return Dev.throwException();
    }

    public void setDefaultValues() {
        setAdopterPoints(RAModelData.DEFAULT_ADOPTER_POINTS);
        setInterestedPoints(RAModelData.DEFAULT_INTERESTED_POINTS);
        setAwarePoints(RAModelData.DEFAULT_AWARE_POINTS);
        setUnknownPoints(RAModelData.DEFAULT_UNKNOWN_POINTS);
        setSpeedOfConvergence(RAConstants.DEFAULT_SPEED_OF_CONVERGENCE);
        setAttitudeGap(RAConstants.DEFAULT_ATTIDUTE_GAP);
        setChanceNeutral(RAConstants.DEFAULT_NEUTRAL_CHANCE);
        setChanceConvergence(RAConstants.DEFAULT_CONVERGENCE_CHANCE);
        setChanceDivergence(RAConstants.DEFAULT_DIVERGENCE_CHANCE);
    }

    @Override
    public DefaultInterestModule parse(IRPactInputParser parser) throws ParsingException {
        if(parser.isRestored()) {
            return MPMSettings.searchModule(parser, thisName(), DefaultInterestModule.class);
        }

        LOGGER.trace(IRPSection.INITIALIZATION_PARAMETER, "parse module {} '{}'", thisName(), getName());

        DefaultInterestModule module = new DefaultInterestModule();
        module.setName(getName());
        module.setEnvironment(parser.getEnvironment());
        module.setAdopterPoints(getAdopterPoints());
        module.setAwarePoints(getAwarePoints());
        module.setInterestedPoints(getInterestedPoints());
        module.setUnknownPoints(getUnknownPoints());

//        AttitudeGapRelativeAgreementAlgorithm algorithm = new AttitudeGapRelativeAgreementAlgorithm();
//        algorithm.setName(getName() + "_RA");
//        algorithm.setEnvironment(parser.getEnvironment());
//        Rnd raRnd = parser.deriveRnd();
//        algorithm.setRandom(raRnd);
//        algorithm.setAttitudeGap(getAttitudeGap());
//        algorithm.setWeightes(getChanceNeutral(), getChanceConvergence(), getChanceDivergence());
//        LOGGER.trace(IRPSection.INITIALIZATION_PARAMETER, "AttitudeGapRelativeAgreementAlgorithm '{}' uses seed: {}", algorithm.getName(), raRnd.getInitialSeed());
//        module.setRelativeAgreementAlgorithm(algorithm);
//
//        Object[] params = { module.getName(), module.getUncertaintyHandler().getManager(), getSpeedOfConvergence() };
//        for(InUncertaintySupplier uncertainty: getUncertainties()) {
//            uncertainty.setup(parser, params);
//        }

        return module;
    }
}
