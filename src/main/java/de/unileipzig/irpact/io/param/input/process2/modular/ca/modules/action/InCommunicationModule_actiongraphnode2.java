package de.unileipzig.irpact.io.param.input.process2.modular.ca.modules.action;

import de.unileipzig.irpact.commons.exception.ParsingException;
import de.unileipzig.irpact.core.logging.IRPLogging;
import de.unileipzig.irpact.core.logging.IRPSection;
import de.unileipzig.irpact.core.process.ra.RAConstants;
import de.unileipzig.irpact.core.process.ra.RAModelData;
import de.unileipzig.irpact.core.process2.modular.ca.ra.modules.action.CommunicationModule2;
import de.unileipzig.irpact.core.process2.raalg.AttitudeGap;
import de.unileipzig.irpact.core.process2.raalg.LoggableAttitudeGapRelativeAgreementAlgorithm2;
import de.unileipzig.irpact.core.start.IRPactInputParser;
import de.unileipzig.irpact.develop.Dev;
import de.unileipzig.irpact.io.param.input.InRootUI;
import de.unileipzig.irpact.io.param.input.process.ra.uncert.InUncertaintySupplier;
import de.unileipzig.irptools.defstructure.annotation.Definition;
import de.unileipzig.irptools.defstructure.annotation.FieldDefinition;
import de.unileipzig.irptools.defstructure.annotation.GraphNode;
import de.unileipzig.irptools.util.CopyCache;
import de.unileipzig.irptools.util.TreeAnnotationResource;
import de.unileipzig.irptools.util.log.IRPLogger;

import java.lang.invoke.MethodHandles;

import static de.unileipzig.irpact.io.param.ParamUtil.*;
import static de.unileipzig.irpact.io.param.input.process2.modular.ca.MPM2Settings.*;

/**
 * @author Daniel Abitz
 */
@Definition(
        graphNode = @GraphNode(
                id = MODULAR_GRAPH,
                label = ACTION_LABEL,
                shape = ACTION_SHAPE,
                color = ACTION_COLOR,
                border = ACTION_BORDER,
                tags = {ACTION_GRAPHNODE}
        )
)
public class InCommunicationModule_actiongraphnode2 implements InConsumerAgentActionModule2 {

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
        putClassPath(res, thisClass(), InRootUI.PROCESS_MODULAR3_MODULES_ACTION_COMMU);
        setShapeColorFillBorder(res, thisClass(), ACTION_SHAPE, ACTION_COLOR, ACTION_FILL, ACTION_BORDER);

        addEntryWithDefault(res, thisClass(), "adopterPoints", asValue(RAModelData.DEFAULT_ADOPTER_POINTS));
        addEntryWithDefault(res, thisClass(), "interestedPoints", asValue(RAModelData.DEFAULT_INTERESTED_POINTS));
        addEntryWithDefault(res, thisClass(), "awarePoints", asValue(RAModelData.DEFAULT_AWARE_POINTS));
        addEntryWithDefault(res, thisClass(), "unknownPoints", asValue(RAModelData.DEFAULT_UNKNOWN_POINTS));
        addEntryWithDefault(res, thisClass(), "speedOfConvergence", asValue(RAConstants.DEFAULT_SPEED_OF_CONVERGENCE));
        addEntryWithDefault(res, thisClass(), "attitudeGap", asValue(RAConstants.DEFAULT_ATTIDUTE_GAP));
        addEntryWithDefault(res, thisClass(), "chanceNeutral", asValue(RAConstants.DEFAULT_NEUTRAL_CHANCE));
        addEntryWithDefault(res, thisClass(), "chanceConvergence", asValue(RAConstants.DEFAULT_CONVERGENCE_CHANCE));
        addEntryWithDefault(res, thisClass(), "chanceDivergence", asValue(RAConstants.DEFAULT_DIVERGENCE_CHANCE));
        addEntry(res, thisClass(), "uncertainties");
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
    public int adopterPoints = RAModelData.DEFAULT_ADOPTER_POINTS;
    public int getAdopterPoints() {
        return adopterPoints;
    }
    public void setAdopterPoints(int adopterPoints) {
        this.adopterPoints = adopterPoints;
    }

    @FieldDefinition
    public int interestedPoints = RAModelData.DEFAULT_INTERESTED_POINTS;
    public int getInterestedPoints() {
        return interestedPoints;
    }
    public void setInterestedPoints(int interestedPoints) {
        this.interestedPoints = interestedPoints;
    }

    @FieldDefinition
    public int awarePoints = RAModelData.DEFAULT_AWARE_POINTS;
    public int getAwarePoints() {
        return awarePoints;
    }
    public void setAwarePoints(int awarePoints) {
        this.awarePoints = awarePoints;
    }

    @FieldDefinition
    public int unknownPoints = RAModelData.DEFAULT_UNKNOWN_POINTS;
    public int getUnknownPoints() {
        return unknownPoints;
    }
    public void setUnknownPoints(int unknownPoints) {
        this.unknownPoints = unknownPoints;
    }

    @FieldDefinition
    public double speedOfConvergence = RAConstants.DEFAULT_SPEED_OF_CONVERGENCE;
    public void setSpeedOfConvergence(double speedOfConvergence) {
        this.speedOfConvergence = speedOfConvergence;
    }
    public double getSpeedOfConvergence() {
        return speedOfConvergence;
    }

    @FieldDefinition
    public double attitudeGap = RAConstants.DEFAULT_ATTIDUTE_GAP;
    public void setAttitudeGap(double attitudeGap) {
        this.attitudeGap = attitudeGap;
    }
    public double getAttitudeGap() {
        return attitudeGap;
    }

    @FieldDefinition
    public double chanceNeutral = RAConstants.DEFAULT_NEUTRAL_CHANCE;
    public void setChanceNeutral(double chanceNeutral) {
        this.chanceNeutral = chanceNeutral;
    }
    public double getChanceNeutral() {
        return chanceNeutral;
    }

    @FieldDefinition
    public double chanceConvergence = RAConstants.DEFAULT_CONVERGENCE_CHANCE;
    public void setChanceConvergence(double chanceConvergence) {
        this.chanceConvergence = chanceConvergence;
    }
    public double getChanceConvergence() {
        return chanceConvergence;
    }

    @FieldDefinition
    public double chanceDivergence = RAConstants.DEFAULT_DIVERGENCE_CHANCE;
    public void setChanceDivergence(double chanceDivergence) {
        this.chanceDivergence = chanceDivergence;
    }
    public double getChanceDivergence() {
        return chanceDivergence;
    }

    @FieldDefinition
    public InUncertaintySupplier[] uncertaintySuppliers;
    public void setUncertainty(InUncertaintySupplier uncertainty) {
        this.uncertaintySuppliers = new InUncertaintySupplier[]{uncertainty};
    }
    public void setUncertaintySuppliers(InUncertaintySupplier[] uncertaintySuppliers) {
        this.uncertaintySuppliers = uncertaintySuppliers;
    }
    public InUncertaintySupplier[] getUncertaintySuppliers() {
        return uncertaintySuppliers;
    }

    public InCommunicationModule_actiongraphnode2() {
    }

    public InCommunicationModule_actiongraphnode2(String name) {
        setName(name);
    }

    @Override
    public InCommunicationModule_actiongraphnode2 copy(CopyCache cache) {
        return cache.copyIfAbsent(this, this::newCopy);
    }

    public InCommunicationModule_actiongraphnode2 newCopy(CopyCache cache) {
        InCommunicationModule_actiongraphnode2 copy = new InCommunicationModule_actiongraphnode2();
        return Dev.throwException();
    }

    @Override
    public CommunicationModule2 parse(IRPactInputParser parser) throws ParsingException {
        if(parser.isRestored()) {
            throw new UnsupportedOperationException();
        }

        LOGGER.trace(IRPSection.INITIALIZATION_PARAMETER, "parse module {} '{}", thisName(), getName());

        CommunicationModule2 module = new CommunicationModule2();
        module.setName(getName());
        module.setAdopterPoints(getAdopterPoints());
        module.setInterestedPoints(getInterestedPoints());
        module.setAwarePoints(getAwarePoints());
        module.setUnknownPoints(getUnknownPoints());

        for(InUncertaintySupplier supplier: getUncertaintySuppliers()) {
            module.addUncertaintySupplier(parser.parseEntityTo(supplier));
        }

        LoggableAttitudeGapRelativeAgreementAlgorithm2 alg = new LoggableAttitudeGapRelativeAgreementAlgorithm2();
        alg.setName(getName() + "_RAALG");
        alg.setBaseName(getName());
        alg.setRnd(parser.deriveRnd());
        alg.setAttitudeGap(getAttitudeGap());
        alg.setSpeedOfConvergence(getSpeedOfConvergence());
        alg.setWeight(AttitudeGap.NEUTRAL, getChanceNeutral());
        alg.setWeight(AttitudeGap.CONVERGENCE, getChanceConvergence());
        alg.setWeight(AttitudeGap.DIVERGENCE, getChanceDivergence());
        alg.setLoggingMode(LoggableAttitudeGapRelativeAgreementAlgorithm2.LoggingMode.OPINION);
        LOGGER.trace(IRPSection.INITIALIZATION_PARAMETER, "AttitudeGapRelativeAgreementAlgorithm '{}' uses seed: {}", alg.getName(), alg.getRnd().getInitialSeed());
        module.setRelativeAgreementAlgorithm(alg);

        return module;
    }
}
