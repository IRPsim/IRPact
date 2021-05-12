package de.unileipzig.irpact.io.param.input.process.ra;

import de.unileipzig.irpact.commons.distribution.UnivariateDoubleDistribution;
import de.unileipzig.irpact.commons.exception.ParsingException;
import de.unileipzig.irpact.core.agent.consumer.ConsumerAgentGroup;
import de.unileipzig.irpact.core.log.IRPLogging;
import de.unileipzig.irpact.core.log.IRPSection;
import de.unileipzig.irpact.core.process.ra.RAConstants;
import de.unileipzig.irpact.core.process.ra.RAProcessModel;
import de.unileipzig.irpact.core.process.ra.attributes.BasicUncertaintyGroupAttributeSupplier;
import de.unileipzig.irpact.io.param.ParamUtil;
import de.unileipzig.irpact.io.param.input.InputParser;
import de.unileipzig.irpact.io.param.input.agent.consumer.InConsumerAgentGroup;
import de.unileipzig.irpact.io.param.input.distribution.InUnivariateDoubleDistribution;
import de.unileipzig.irptools.defstructure.annotation.Definition;
import de.unileipzig.irptools.defstructure.annotation.FieldDefinition;
import de.unileipzig.irptools.util.CopyCache;
import de.unileipzig.irptools.util.TreeAnnotationResource;
import de.unileipzig.irptools.util.log.IRPLogger;

import java.lang.invoke.MethodHandles;

import static de.unileipzig.irpact.io.param.IOConstants.PROCESS_MODEL;
import static de.unileipzig.irpact.io.param.IOConstants.PROCESS_MODEL_RA_UNCERT;
import static de.unileipzig.irpact.io.param.ParamUtil.addEntry;
import static de.unileipzig.irpact.io.param.ParamUtil.putClassPath;

/**
 * @author Daniel Abitz
 */
@Definition
public class InPVactUncertaintyGroupAttribute implements InUncertaintyGroupAttribute {

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
        putClassPath(res, thisClass(), PROCESS_MODEL, InRAProcessModel.thisName(), PROCESS_MODEL_RA_UNCERT, thisName());
        addEntry(res, thisClass(), "noveltySeekingUncert");
        addEntry(res, thisClass(), "dependentJudgmentMakingUncert");
        addEntry(res, thisClass(), "environmentalConcernUncert");
        addEntry(res, thisClass(), "cags");
    }

    private static final IRPLogger LOGGER = IRPLogging.getLogger(thisClass());

    public String _name;

    @FieldDefinition
    public InConsumerAgentGroup[] cags;

    @FieldDefinition
    public InUnivariateDoubleDistribution[] noveltySeekingUncert;

    @FieldDefinition
    public InUnivariateDoubleDistribution[] dependentJudgmentMakingUncert;

    @FieldDefinition
    public InUnivariateDoubleDistribution[] environmentalConcernUncert;

    public InPVactUncertaintyGroupAttribute() {
    }

    @Override
    public InPVactUncertaintyGroupAttribute copy(CopyCache cache) {
        return cache.copyIfAbsent(this, this::newCopy);
    }

    public InPVactUncertaintyGroupAttribute newCopy(CopyCache cache) {
        InPVactUncertaintyGroupAttribute copy = new InPVactUncertaintyGroupAttribute();
        copy._name = _name;
        copy.cags = cache.copyArray(cags);
        copy.noveltySeekingUncert = cache.copyArray(noveltySeekingUncert);
        copy.dependentJudgmentMakingUncert = cache.copyArray(dependentJudgmentMakingUncert);
        copy.environmentalConcernUncert = cache.copyArray(environmentalConcernUncert);
        return copy;
    }

    public void setName(String name) {
        this._name = name;
    }

    @Override
    public String getName() {
        return _name;
    }

    @Override
    public Object parse(InputParser parser) throws ParsingException {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setup(InputParser parser, Object input) throws ParsingException {
        RAProcessModel processModel = (RAProcessModel) input;

        UnivariateDoubleDistribution novelDist = parser.parseEntityTo(getNoveltySeekingUncertainty());
        UnivariateDoubleDistribution depJudgDist = parser.parseEntityTo(getDependentJudgmentMakingUncertainty());
        UnivariateDoubleDistribution envConDist = parser.parseEntityTo(getEnvironmentalConcernUncertainty());

        for(InConsumerAgentGroup inCag: getGroups()) {
            ConsumerAgentGroup cag = parser.parseEntityTo(inCag);
            add(processModel, cag, RAConstants.NOVELTY_SEEKING, novelDist);
            add(processModel, cag, RAConstants.DEPENDENT_JUDGMENT_MAKING, depJudgDist);
            add(processModel, cag, RAConstants.ENVIRONMENTAL_CONCERN, envConDist);
        }
    }

    protected void add(
            RAProcessModel processModel,
            ConsumerAgentGroup cag,
            String attrName,
            UnivariateDoubleDistribution dist) throws ParsingException {

        processModel.getUncertaintySupplier().add(
                cag,
                new BasicUncertaintyGroupAttributeSupplier(
                        getName() + "_" + cag.getName(),
                        attrName,
                        dist
                )
        );
        LOGGER.trace(
                IRPSection.INITIALIZATION_PARAMETER,
                "add UncertaintySupplier for group '{}', attribute '{}', uncertainity '{}'",
                cag.getName(),
                attrName,
                dist.getName()
        );
    }

    public InConsumerAgentGroup[] getGroups() {
        return cags;
    }

    public void setGroups(InConsumerAgentGroup[] cags) {
        this.cags = cags;
    }

    public void setForAll(InUnivariateDoubleDistribution distribution) {
        setNoveltySeekingUncertainty(distribution);
        setDependentJudgmentMakingUncertainty(distribution);
        setEnvironmentalConcernUncertainty(distribution);
    }

    public InUnivariateDoubleDistribution getNoveltySeekingUncertainty() throws ParsingException {
        return ParamUtil.getInstance(noveltySeekingUncert, "noveltySeekingUncert");
    }

    public void setNoveltySeekingUncertainty(InUnivariateDoubleDistribution dist) {
        this.noveltySeekingUncert = new InUnivariateDoubleDistribution[]{dist};
    }

    public InUnivariateDoubleDistribution getDependentJudgmentMakingUncertainty() throws ParsingException {
        return ParamUtil.getInstance(dependentJudgmentMakingUncert, "dependentJudgmentMakingUncert");
    }

    public void setDependentJudgmentMakingUncertainty(InUnivariateDoubleDistribution dist) {
        this.dependentJudgmentMakingUncert = new InUnivariateDoubleDistribution[]{dist};
    }

    public InUnivariateDoubleDistribution getEnvironmentalConcernUncertainty() throws ParsingException {
        return ParamUtil.getInstance(environmentalConcernUncert, "environmentalConcernUncert");
    }

    public void setEnvironmentalConcernUncertainty(InUnivariateDoubleDistribution dist) {
        this.environmentalConcernUncert = new InUnivariateDoubleDistribution[]{dist};
    }
}
