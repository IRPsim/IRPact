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
import de.unileipzig.irpact.io.param.LocalizedUiResource;
import de.unileipzig.irpact.io.param.input.process.ra.uncert.InUncertaintySupplier;
import de.unileipzig.irpact.io.param.input.process2.modular.util.CAMPMGraphSettings;
import de.unileipzig.irptools.defstructure.annotation.Definition;
import de.unileipzig.irptools.defstructure.annotation.DefinitionName;
import de.unileipzig.irptools.defstructure.annotation.FieldDefinition;
import de.unileipzig.irptools.defstructure.annotation.graph.GraphNode;
import de.unileipzig.irptools.defstructure.annotation.graph.Subsets;
import de.unileipzig.irptools.util.CopyCache;
import de.unileipzig.irptools.util.TreeAnnotationResource;
import de.unileipzig.irptools.util.log.IRPLogger;

import java.lang.invoke.MethodHandles;

import static de.unileipzig.irpact.io.param.input.TreeViewStructureEnum.PROCESS_MODEL4_PVACTMODULES_ACTION_COMMU;

/**
 * @author Daniel Abitz
 */
@Definition(
//        graphNode = @GraphNode(
//                id = MODULAR_GRAPH,
//                label = ACTION_LABEL,
//                shape = ACTION_SHAPE,
//                color = ACTION_COLOR,
//                border = ACTION_BORDER,
//                tags = {ACTION_GRAPHNODE}
//        ),
        graphNode3 = @GraphNode(
                graphId = CAMPMGraphSettings.GRAPH_ID,
                subsetsColor = @Subsets(
                        value = CAMPMGraphSettings.ACTION_COLOR
                ),
                subsetsBorder = @Subsets(
                        value = CAMPMGraphSettings.ACTION_BORDER
                ),
                subsetsShape = @Subsets(
                        value = CAMPMGraphSettings.ACTION_SHAPE
                )
        )
)
@LocalizedUiResource.PutClassPath(PROCESS_MODEL4_PVACTMODULES_ACTION_COMMU)
public class InCommunicationModule3 implements InConsumerAgentActionModule2 {

    private static final MethodHandles.Lookup L = MethodHandles.lookup();
    public static Class<?> thisClass() {
        return L.lookupClass();
    }
    public static String thisName() {
        return thisClass().getSimpleName();
    }

    @TreeAnnotationResource.Init
    public static void initRes(LocalizedUiResource res) {
    }
    @TreeAnnotationResource.Apply
    public static void applyRes(LocalizedUiResource res) {
        //setShapeColorFillBorder(res, thisClass(), ACTION_SHAPE, ACTION_COLOR, ACTION_FILL, ACTION_BORDER);
    }

    private static final IRPLogger LOGGER = IRPLogging.getLogger(thisClass());

    @DefinitionName
    public String name;
    @Override
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    @FieldDefinition
    @LocalizedUiResource.AddEntry
    @LocalizedUiResource.SimpleSet(
            intDefault = RAModelData.DEFAULT_ADOPTER_POINTS
    )
    public int adopterPoints = RAModelData.DEFAULT_ADOPTER_POINTS;
    public int getAdopterPoints() {
        return adopterPoints;
    }
    public void setAdopterPoints(int adopterPoints) {
        this.adopterPoints = adopterPoints;
    }

    @FieldDefinition
    @LocalizedUiResource.AddEntry
    @LocalizedUiResource.SimpleSet(
            intDefault = RAModelData.DEFAULT_INTERESTED_POINTS
    )
    public int interestedPoints = RAModelData.DEFAULT_INTERESTED_POINTS;
    public int getInterestedPoints() {
        return interestedPoints;
    }
    public void setInterestedPoints(int interestedPoints) {
        this.interestedPoints = interestedPoints;
    }

    @FieldDefinition
    @LocalizedUiResource.AddEntry
    @LocalizedUiResource.SimpleSet(
            intDefault = RAModelData.DEFAULT_AWARE_POINTS
    )
    public int awarePoints = RAModelData.DEFAULT_AWARE_POINTS;
    public int getAwarePoints() {
        return awarePoints;
    }
    public void setAwarePoints(int awarePoints) {
        this.awarePoints = awarePoints;
    }

    @FieldDefinition
    @LocalizedUiResource.AddEntry
    @LocalizedUiResource.SimpleSet(
            intDefault = RAModelData.DEFAULT_UNKNOWN_POINTS
    )
    public int unknownPoints = RAModelData.DEFAULT_UNKNOWN_POINTS;
    public int getUnknownPoints() {
        return unknownPoints;
    }
    public void setUnknownPoints(int unknownPoints) {
        this.unknownPoints = unknownPoints;
    }

    @FieldDefinition
    @LocalizedUiResource.AddEntry
    @LocalizedUiResource.SimpleSet(
            boolDomain = true,
            boolDefault = true
    )
    public boolean raEnabled = true;
    public void setRaEnabled(boolean raEnabled) {
        this.raEnabled = raEnabled;
    }
    public boolean isRaEnabled() {
        return raEnabled;
    }

    @FieldDefinition
    @LocalizedUiResource.AddEntry
    @LocalizedUiResource.SimpleSet(
            boolDomain = true,
            boolDefault = true
    )
    public boolean raLoggingEnabled = true;
    public void setRaLoggingEnabled(boolean raLoggingEnabled) {
        this.raLoggingEnabled = raLoggingEnabled;
    }
    public boolean isRaLoggingEnabled() {
        return raLoggingEnabled;
    }

    @FieldDefinition
    @LocalizedUiResource.AddEntry
    @LocalizedUiResource.SimpleSet(
            boolDomain = true,
            boolDefault = true
    )
    public boolean raOpinionLogging = true;
    public void setRaOpinionLogging(boolean raOpinionLogging) {
        this.raOpinionLogging = raOpinionLogging;
    }
    public boolean isRaOpinionLogging() {
        return raOpinionLogging;
    }

    @FieldDefinition
    @LocalizedUiResource.AddEntry
    @LocalizedUiResource.SimpleSet(
            boolDomain = true,
            boolDefault = true
    )
    public boolean raUnceraintyLogging = true;
    public void setRaUnceraintyLogging(boolean raUnceraintyLogging) {
        this.raUnceraintyLogging = raUnceraintyLogging;
    }
    public boolean isRaUnceraintyLogging() {
        return raUnceraintyLogging;
    }

    @FieldDefinition
    @LocalizedUiResource.AddEntry
    @LocalizedUiResource.SimpleSet(
            boolDomain = true,
            boolDefault = true
    )
    public boolean raPrintHeader = true;
    public void setRaPrintHeader(boolean raPrintHeader) {
        this.raPrintHeader = raPrintHeader;
    }
    public boolean isRaPrintHeader() {
        return raPrintHeader;
    }

    @FieldDefinition
    @LocalizedUiResource.AddEntry
    @LocalizedUiResource.SimpleSet(
            boolDomain = true
    )
    public boolean raKeepCsv = false;
    public void setRaKeepCsv(boolean raKeepCsv) {
        this.raKeepCsv = raKeepCsv;
    }
    public boolean isRaKeepCsv() {
        return raKeepCsv;
    }

    @FieldDefinition
    @LocalizedUiResource.AddEntry
    @LocalizedUiResource.SimpleSet(
            boolDomain = true,
            boolDefault = true
    )
    public boolean raStoreXlsx = true;
    public void setRaStoreXlsx(boolean raStoreXlsx) {
        this.raStoreXlsx = raStoreXlsx;
    }
    public boolean isRaStoreXlsx() {
        return raStoreXlsx;
    }

    @FieldDefinition
    @LocalizedUiResource.AddEntry
    @LocalizedUiResource.SimpleSet(
            decDefault = RAConstants.DEFAULT_SPEED_OF_CONVERGENCE
    )
    public double speedOfConvergence = RAConstants.DEFAULT_SPEED_OF_CONVERGENCE;
    public void setSpeedOfConvergence(double speedOfConvergence) {
        this.speedOfConvergence = speedOfConvergence;
    }
    public double getSpeedOfConvergence() {
        return speedOfConvergence;
    }

    @FieldDefinition
    @LocalizedUiResource.AddEntry
    @LocalizedUiResource.SimpleSet(
            decDefault = RAConstants.DEFAULT_ATTIDUTE_GAP
    )
    public double attitudeGap = RAConstants.DEFAULT_ATTIDUTE_GAP;
    public void setAttitudeGap(double attitudeGap) {
        this.attitudeGap = attitudeGap;
    }
    public double getAttitudeGap() {
        return attitudeGap;
    }

    @FieldDefinition
    @LocalizedUiResource.AddEntry
    @LocalizedUiResource.SimpleSet(
            decDefault = RAConstants.DEFAULT_NEUTRAL_CHANCE
    )
    public double chanceNeutral = RAConstants.DEFAULT_NEUTRAL_CHANCE;
    public void setChanceNeutral(double chanceNeutral) {
        this.chanceNeutral = chanceNeutral;
    }
    public double getChanceNeutral() {
        return chanceNeutral;
    }

    @FieldDefinition
    @LocalizedUiResource.AddEntry
    @LocalizedUiResource.SimpleSet(
            decDefault = RAConstants.DEFAULT_CONVERGENCE_CHANCE
    )
    public double chanceConvergence = RAConstants.DEFAULT_CONVERGENCE_CHANCE;
    public void setChanceConvergence(double chanceConvergence) {
        this.chanceConvergence = chanceConvergence;
    }
    public double getChanceConvergence() {
        return chanceConvergence;
    }

    @FieldDefinition
    @LocalizedUiResource.AddEntry
    @LocalizedUiResource.SimpleSet(
            decDefault = RAConstants.DEFAULT_DIVERGENCE_CHANCE
    )
    public double chanceDivergence = RAConstants.DEFAULT_DIVERGENCE_CHANCE;
    public void setChanceDivergence(double chanceDivergence) {
        this.chanceDivergence = chanceDivergence;
    }
    public double getChanceDivergence() {
        return chanceDivergence;
    }

    @FieldDefinition
    @LocalizedUiResource.AddEntry
    public InUncertaintySupplier[] uncertaintySuppliers;
    public void setUncertaintySupplier(InUncertaintySupplier uncertainty) {
        this.uncertaintySuppliers = new InUncertaintySupplier[]{uncertainty};
    }
    public void setUncertaintySuppliers(InUncertaintySupplier[] uncertaintySuppliers) {
        this.uncertaintySuppliers = uncertaintySuppliers;
    }
    public InUncertaintySupplier[] getUncertaintySuppliers() {
        return uncertaintySuppliers;
    }

    public InCommunicationModule3() {
    }

    public InCommunicationModule3(String name) {
        setName(name);
    }

    @Override
    public InCommunicationModule3 copy(CopyCache cache) {
        return cache.copyIfAbsent(this, this::newCopy);
    }

    public InCommunicationModule3 newCopy(CopyCache cache) {
        InCommunicationModule3 copy = new InCommunicationModule3();
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
        module.setRaEnabled(isRaEnabled());

        for(InUncertaintySupplier supplier: getUncertaintySuppliers()) {
            module.addUncertaintySupplier(parser.parseEntityTo(supplier));
        }

        LoggableAttitudeGapRelativeAgreementAlgorithm2 alg = new LoggableAttitudeGapRelativeAgreementAlgorithm2();
        alg.setName(getName() + "_RAALG");
        alg.setLoggingEnabled(isRaLoggingEnabled());
        alg.setStoreXlsx(isRaStoreXlsx());
        alg.setKeepCsv(isRaKeepCsv());
        alg.setPrintHeader(isRaPrintHeader());
        alg.setBaseName(getName());
        alg.setRnd(parser.deriveRnd());
        alg.setAttitudeGap(getAttitudeGap());
        alg.setSpeedOfConvergence(getSpeedOfConvergence());
        alg.setWeight(AttitudeGap.NEUTRAL, getChanceNeutral());
        alg.setWeight(AttitudeGap.CONVERGENCE, getChanceConvergence());
        alg.setWeight(AttitudeGap.DIVERGENCE, getChanceDivergence());
        alg.setLoggingMode(LoggableAttitudeGapRelativeAgreementAlgorithm2.LoggingMode.get(isRaOpinionLogging(), isRaUnceraintyLogging()));
        LOGGER.trace(IRPSection.INITIALIZATION_PARAMETER, "LoggableAttitudeGapRelativeAgreementAlgorithm2 '{}' uses seed: {}", alg.getName(), alg.getRnd().getInitialSeed());
        LOGGER.trace("LoggableAttitudeGapRelativeAgreementAlgorithm2 '{}' enabled={}, logging={}, mode={}", alg.getName(), isRaEnabled(), alg.isLoggingEnabled(), alg.getLoggingMode());
        module.setRelativeAgreementAlgorithm(alg);

        return module;
    }
}
