package de.unileipzig.irpact.io.param.input.process.modular.ca.component.eval;

import de.unileipzig.irpact.commons.exception.ParsingException;
import de.unileipzig.irpact.commons.util.Rnd;
import de.unileipzig.irpact.core.logging.IRPLogging;
import de.unileipzig.irpact.core.logging.IRPSection;
import de.unileipzig.irpact.core.process.modular.ca.components.eval.DefaultActionModule;
import de.unileipzig.irpact.core.process.modular.components.core.Module;
import de.unileipzig.irpact.core.process.ra.RAConstants;
import de.unileipzig.irpact.core.process.ra.RAModelData;
import de.unileipzig.irpact.core.process.ra.alg.AttitudeGapRelativeAgreementAlgorithm;
import de.unileipzig.irpact.core.start.IRPactInputParser;
import de.unileipzig.irpact.develop.Dev;
import de.unileipzig.irpact.io.param.input.InRootUI;
import de.unileipzig.irpact.io.param.input.process.modular.ca.ModuleHelper;
import de.unileipzig.irpact.io.param.input.process.modular.ca.component.InConsumerAgentEvaluationModule;
import de.unileipzig.irpact.io.param.input.process.ra.uncert.InUncertainty;
import de.unileipzig.irptools.defstructure.annotation.Definition;
import de.unileipzig.irptools.defstructure.annotation.FieldDefinition;
import de.unileipzig.irptools.defstructure.annotation.GraphNode;
import de.unileipzig.irptools.util.CopyCache;
import de.unileipzig.irptools.util.TreeAnnotationResource;
import de.unileipzig.irptools.util.log.IRPLogger;

import java.lang.invoke.MethodHandles;

import static de.unileipzig.irpact.io.param.ParamUtil.*;
import static de.unileipzig.irpact.io.param.ParamUtil.varargs;

/**
 * @author Daniel Abitz
 */
@Definition(
        graphNode = @GraphNode(
                id = ModuleHelper.MODULAR_GRAPH,
                label = "Eval-Modul",
                color = COLOR_DARK_CYAN,
                border = COLOR_DARK_CYAN,
                shape = SHAPE_OCTAGON,
                tags = {"graphnode"}
        )
)
public class InDefaultActionModule_graphnode implements InConsumerAgentEvaluationModule {

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
        putClassPath(res, thisClass(), InRootUI.PROCESS_MODULAR2_COMPONENTS_EVAL_DEFAULTACTION);
        setShape(res, thisClass(), SHAPE_OCTAGON);
        setColor(res, thisClass(), COLOR_DARK_CYAN);

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
    public InUncertainty[] uncertainties = new InUncertainty[0];
    public void setUncertainty(InUncertainty uncertainty) {
        this.uncertainties = new InUncertainty[]{uncertainty};
    }
    public void setUncertainties(InUncertainty[] uncertainties) {
        this.uncertainties = uncertainties;
    }
    public InUncertainty[] getUncertainties() {
        return uncertainties;
    }

    public InDefaultActionModule_graphnode() {
    }

    @Override
    public InDefaultActionModule_graphnode copy(CopyCache cache) {
        return cache.copyIfAbsent(this, this::newCopy);
    }

    public InDefaultActionModule_graphnode newCopy(CopyCache cache) {
        InDefaultActionModule_graphnode copy = new InDefaultActionModule_graphnode();
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
    public DefaultActionModule parse(IRPactInputParser parser) throws ParsingException {
        if(parser.isRestored()) {
            Module module = ModuleHelper.searchModule(parser, getName());
            if(module instanceof DefaultActionModule) {
                return (DefaultActionModule) module;
            } else {
                throw new ParsingException("class mismatch");
            }
        }

        DefaultActionModule module = new DefaultActionModule();
        module.setName(getName());
        module.setEnvironment(parser.getEnvironment());
        module.setAdopterPoints(getAdopterPoints());
        module.setAwarePoints(getAwarePoints());
        module.setInterestedPoints(getInterestedPoints());
        module.setUnknownPoints(getUnknownPoints());

        AttitudeGapRelativeAgreementAlgorithm algorithm = new AttitudeGapRelativeAgreementAlgorithm();
        algorithm.setName(getName() + "_RA");
        algorithm.setEnvironment(parser.getEnvironment());
        Rnd raRnd = parser.deriveRnd();
        algorithm.setRandom(raRnd);
        algorithm.setAttitudeGap(getAttitudeGap());
        algorithm.setWeightes(getChanceNeutral(), getChanceConvergence(), getChanceDivergence());
        algorithm.setLogDataFallback(false);
        LOGGER.trace(IRPSection.INITIALIZATION_PARAMETER, "AttitudeGapRelativeAgreementAlgorithm '{}' uses seed: {}", algorithm.getName(), raRnd.getInitialSeed());
        module.setRelativeAgreementAlgorithm(algorithm);

        Object[] params = { module.getName(), module.getUncertaintyHandler().getManager(), getSpeedOfConvergence() };
        for(InUncertainty uncertainty: getUncertainties()) {
            uncertainty.setup(parser, params);
        }

        return module;
    }
}
