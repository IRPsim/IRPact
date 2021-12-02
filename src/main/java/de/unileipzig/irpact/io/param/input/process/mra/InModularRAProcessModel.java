package de.unileipzig.irpact.io.param.input.process.mra;

import de.unileipzig.irpact.commons.exception.ParsingException;
import de.unileipzig.irpact.commons.util.Rnd;
import de.unileipzig.irpact.core.logging.IRPLogging;
import de.unileipzig.irpact.core.logging.IRPSection;
import de.unileipzig.irpact.core.process.ProcessModelManager;
import de.unileipzig.irpact.core.process.mra.ModularRAProcessModel;
import de.unileipzig.irpact.core.process.mra.component.base.EvaluableComponent;
import de.unileipzig.irpact.core.process.ra.RAConstants;
import de.unileipzig.irpact.core.start.IRPactInputParser;
import de.unileipzig.irpact.develop.Dev;
import de.unileipzig.irpact.io.param.ParamUtil;
import de.unileipzig.irpact.io.param.input.TreeViewStructure;
import de.unileipzig.irpact.io.param.input.process.InProcessModel;
import de.unileipzig.irpact.io.param.input.process.mra.component.InEvaluableComponent;
import de.unileipzig.irpact.io.param.input.process.ra.uncert.InUncertaintySupplier;
import de.unileipzig.irptools.defstructure.annotation.Definition;
import de.unileipzig.irptools.defstructure.annotation.FieldDefinition;
import de.unileipzig.irptools.util.CopyCache;
import de.unileipzig.irptools.util.TreeAnnotationResource;
import de.unileipzig.irptools.util.log.IRPLogger;

import java.lang.invoke.MethodHandles;

import static de.unileipzig.irpact.io.param.ParamUtil.*;

/**
 * @author Daniel Abitz
 */
@Definition
public class InModularRAProcessModel implements InProcessModel {

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
        putClassPath(res, thisClass(), TreeViewStructure.PROCESS_MRA);

        addEntry(res, thisClass(), "speedOfConvergence");
        addEntry(res, thisClass(), "attitudeGap");
        addEntryWithDefaultAndDomain(res, thisClass(), "chanceNeutral", varargs(RAConstants.DEFAULT_NEUTRAL_CHANCE), DOMAIN_CLOSED_0_1);
        addEntryWithDefaultAndDomain(res, thisClass(), "chanceConvergence", varargs(RAConstants.DEFAULT_CONVERGENCE_CHANCE), DOMAIN_CLOSED_0_1);
        addEntryWithDefaultAndDomain(res, thisClass(), "chanceDivergence", varargs(RAConstants.DEFAULT_DIVERGENCE_CHANCE), DOMAIN_CLOSED_0_1);
        addEntry(res, thisClass(), "interestComponent");
        addEntry(res, thisClass(), "feasibilityComponent");
        addEntry(res, thisClass(), "decisionMakingComponent");
        addEntry(res, thisClass(), "actionComponent");


        setDefault(res, thisClass(), "speedOfConvergence", varargs(RAConstants.DEFAULT_SPEED_OF_CONVERGENCE));
        setDefault(res, thisClass(), "logisticFactor", varargs(RAConstants.DEFAULT_LOGISTIC_FACTOR));
    }

    private static final IRPLogger LOGGER = IRPLogging.getLogger(thisClass());

    public String _name;

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
    public InEvaluableComponent[] interestComponent = new InEvaluableComponent[0];
    public InEvaluableComponent getInterestComponent() throws ParsingException {
        return ParamUtil.getInstance(interestComponent, "interestComponent");
    }
    public void setInterestComponent(InEvaluableComponent interestComponent) {
        this.interestComponent = new InEvaluableComponent[] {interestComponent};
    }

    @FieldDefinition
    public InEvaluableComponent[] feasibilityComponent = new InEvaluableComponent[0];
    public InEvaluableComponent getFeasibilityComponent() throws ParsingException {
        return ParamUtil.getInstance(feasibilityComponent, "feasibilityComponent");
    }
    public void setFeasibilityComponent(InEvaluableComponent feasibilityComponent) {
        this.feasibilityComponent = new InEvaluableComponent[] {feasibilityComponent};
    }

    @FieldDefinition
    public InEvaluableComponent[] decisionMakingComponent = new InEvaluableComponent[0];
    public InEvaluableComponent getDecisionMakingComponent() throws ParsingException {
        return ParamUtil.getInstance(decisionMakingComponent, "decisionMakingComponent");
    }
    public void setDecisionMakingComponent(InEvaluableComponent decisionMakingComponent) {
        this.decisionMakingComponent = new InEvaluableComponent[] {decisionMakingComponent};
    }

    @FieldDefinition
    public InEvaluableComponent[] actionComponent = new InEvaluableComponent[0];
    public InEvaluableComponent getActionComponent() throws ParsingException {
        return ParamUtil.getInstance(actionComponent, "actionComponent");
    }
    public void setActionComponent(InEvaluableComponent actionComponent) {
        this.actionComponent = new InEvaluableComponent[] {actionComponent};
    }

    @FieldDefinition
    public InUncertaintySupplier[] uncertainties;
    public void setUncertainty(InUncertaintySupplier uncertainty) {
        this.uncertainties = new InUncertaintySupplier[]{uncertainty};
    }
    public void setUncertainties(InUncertaintySupplier[] uncertainties) {
        this.uncertainties = uncertainties;
    }
    public InUncertaintySupplier[] getUncertainties() {
        return uncertainties;
    }

    public InModularRAProcessModel() {
    }

    @Override
    public InModularRAProcessModel copy(CopyCache cache) {
        return cache.copyIfAbsent(this, this::newCopy);
    }

    public InModularRAProcessModel newCopy(CopyCache cache) {
        InModularRAProcessModel copy = new InModularRAProcessModel();
        return Dev.throwException();
    }

    public void setDefaultValues() {
        setSpeedOfConvergence(RAConstants.DEFAULT_SPEED_OF_CONVERGENCE);
        setAttitudeGap(RAConstants.DEFAULT_ATTIDUTE_GAP);
        setChanceNeutral(RAConstants.DEFAULT_NEUTRAL_CHANCE);
        setChanceConvergence(RAConstants.DEFAULT_CONVERGENCE_CHANCE);
        setChanceDivergence(RAConstants.DEFAULT_DIVERGENCE_CHANCE);
    }

    @Override
    public String getName() {
        return _name;
    }

    public void setName(String name) {
        this._name = name;
    }

    @Override
    public ModularRAProcessModel parse(IRPactInputParser parser) throws ParsingException {
        if(parser.isRestored()) {
            ProcessModelManager manager = parser.getEnvironment().getProcessModels();
            ModularRAProcessModel model = (ModularRAProcessModel) manager.getProcessModel(getName());
            return update(parser, model);
        }

        Rnd rnd = parser.deriveRnd();
        LOGGER.trace(IRPSection.INITIALIZATION_PARAMETER, "ModularRAProcessModel '{}' uses seed: {}", getName(), rnd.getInitialSeed());

        ModularRAProcessModel model = new ModularRAProcessModel();
        model.setName(getName());
        model.setEnvironment(parser.getEnvironment());
        model.setRnd(rnd);
        model.setSpeedOfConvergence(getSpeedOfConvergence());

//        AttitudeGapRelativeAgreementAlgorithm algorithm = new AttitudeGapRelativeAgreementAlgorithm();
//        algorithm.setName(model.getName() + "_RA");
//        algorithm.setEnvironment(parser.getEnvironment());
//        Rnd raRnd = parser.deriveRnd();
//        algorithm.setRandom(raRnd);
//        algorithm.setAttitudeGap(getAttitudeGap());
//        algorithm.setWeightes(getChanceNeutral(), getChanceConvergence(), getChanceDivergence());
//        LOGGER.trace(IRPSection.INITIALIZATION_PARAMETER, "AttitudeGapRelativeAgreementAlgorithm '{}' uses seed: {}", algorithm.getName(), raRnd.getInitialSeed());
//        model.setRelativeAgreementAlgorithm(algorithm);

//        Object[] params = { model.getName(), model.getUncertaintyManager(), model.getSpeedOfConvergence() };
//        for(InUncertaintySupplier uncertainty: getUncertainties()) {
//            uncertainty.setup(parser, params);
//        }

        EvaluableComponent interestComponent = parser.parseEntityTo(getInterestComponent(), model);
        LOGGER.trace(IRPSection.INITIALIZATION_PARAMETER, "add interest component '{}' to '{}'", interestComponent.getName(), model.getName());
        model.setInterestComponent(interestComponent);

        EvaluableComponent feasibilityComponent = parser.parseEntityTo(getFeasibilityComponent(), model);
        LOGGER.trace(IRPSection.INITIALIZATION_PARAMETER, "add feasibility component '{}' to '{}'", feasibilityComponent.getName(), model.getName());
        model.setFeasibilityComponent(feasibilityComponent);

        EvaluableComponent decisionMakingComponent = parser.parseEntityTo(getDecisionMakingComponent(), model);
        LOGGER.trace(IRPSection.INITIALIZATION_PARAMETER, "add decision making component '{}' to '{}'", decisionMakingComponent.getName(), model.getName());
        model.setDecisionMakingComponent(decisionMakingComponent);

        EvaluableComponent actionComponent = parser.parseEntityTo(getActionComponent(), model);
        LOGGER.trace(IRPSection.INITIALIZATION_PARAMETER, "add action component '{}' to '{}'", actionComponent.getName(), model.getName());
        model.setActionComponent(actionComponent);

        return model;
    }

    public ModularRAProcessModel update(IRPactInputParser parser, ModularRAProcessModel restored) throws ParsingException {
        parser.update(getInterestComponent(), restored.getInterestComponent(), restored);
        parser.update(getFeasibilityComponent(), restored.getFeasibilityComponent(), restored);
        parser.update(getDecisionMakingComponent(), restored.getDecisionMakingComponent(), restored);
        parser.update(getActionComponent(), restored.getActionComponent(), restored);

        return restored;
    }
}
