package de.unileipzig.irpact.io.param.input.process.ra.uncert;

import de.unileipzig.irpact.commons.exception.ParsingException;
import de.unileipzig.irpact.core.logging.IRPLogging;
import de.unileipzig.irpact.core.process.ra.RAConstants;
import de.unileipzig.irpact.core.process2.uncert.IndividualGlobalModerateExtremistUncertaintySupplier;
import de.unileipzig.irpact.core.start.IRPactInputParser;
import de.unileipzig.irpact.io.param.LocalizedUiResource;
import de.unileipzig.irptools.defstructure.annotation.Definition;
import de.unileipzig.irptools.defstructure.annotation.DefinitionName;
import de.unileipzig.irptools.defstructure.annotation.FieldDefinition;
import de.unileipzig.irptools.util.CopyCache;
import de.unileipzig.irptools.util.TreeAnnotationResource;
import de.unileipzig.irptools.util.log.IRPLogger;

import java.lang.invoke.MethodHandles;

import static de.unileipzig.irpact.io.param.input.TreeViewStructureEnum.PROCESS_MODEL4_UNCERT_PVACTIMEU;

/**
 * @author Daniel Abitz
 */
@Definition
@LocalizedUiResource.PutClassPath(PROCESS_MODEL4_UNCERT_PVACTIMEU)
public class InPVactIndividualGlobalModerateExtremistUncertaintySupplier implements InUncertaintySupplier {

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
    }

    private static final IRPLogger LOGGER = IRPLogging.getLogger(thisClass());

    @DefinitionName
    public String name;

    @FieldDefinition
    @LocalizedUiResource.AddEntry
    @LocalizedUiResource.SimpleSet(
            closed01Domain = true,
            decDefault = RAConstants.DEFAULT_EXTREMIST_RATE
    )
    public double extremistParameter;

    @FieldDefinition
    @LocalizedUiResource.AddEntry
    @LocalizedUiResource.SimpleSet(
            decDefault = RAConstants.DEFAULT_EXTREMIST_UNCERTAINTY
    )
    public double extremistUncertainty;

    @FieldDefinition
    @LocalizedUiResource.AddEntry
    @LocalizedUiResource.SimpleSet(
            decDefault = RAConstants.DEFAULT_MODERATE_UNCERTAINTY
    )
    public double moderateUncertainty;

    @FieldDefinition
    @LocalizedUiResource.AddEntry
    @LocalizedUiResource.SimpleSet(
            boolDomain = true,
            boolDefault = true
    )
    public boolean lowerBoundInclusive;

    @FieldDefinition
    @LocalizedUiResource.AddEntry
    @LocalizedUiResource.SimpleSet(
            boolDomain = true,
            boolDefault = true
    )
    public boolean upperBoundInclusive;

    public InPVactIndividualGlobalModerateExtremistUncertaintySupplier() {
    }

    @Override
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setExtremistParameter(double extremistParameter) {
        this.extremistParameter = extremistParameter;
    }

    public double getExtremistParameter() {
        return extremistParameter;
    }

    public void setExtremistUncertainty(double extremistUncertainty) {
        this.extremistUncertainty = extremistUncertainty;
    }

    public double getExtremistUncertainty() {
        return extremistUncertainty;
    }

    public void setModerateUncertainty(double moderateUncertainty) {
        this.moderateUncertainty = moderateUncertainty;
    }

    public double getModerateUncertainty() {
        return moderateUncertainty;
    }

    public void setLowerBoundInclusive(boolean lowerBoundInclusive) {
        this.lowerBoundInclusive = lowerBoundInclusive;
    }

    public boolean isLowerBoundInclusive() {
        return lowerBoundInclusive;
    }

    public void setUpperBoundInclusive(boolean upperBoundInclusive) {
        this.upperBoundInclusive = upperBoundInclusive;
    }

    public boolean isUpperBoundInclusive() {
        return upperBoundInclusive;
    }

    @Override
    public InPVactIndividualGlobalModerateExtremistUncertaintySupplier copy(CopyCache cache) {
        return cache.copyIfAbsent(this, this::newCopy);
    }

    public InPVactIndividualGlobalModerateExtremistUncertaintySupplier newCopy(CopyCache cache) {
        InPVactIndividualGlobalModerateExtremistUncertaintySupplier copy = new InPVactIndividualGlobalModerateExtremistUncertaintySupplier();
        copy.name = name;
        copy.extremistParameter = extremistParameter;
        copy.extremistUncertainty = extremistUncertainty;
        copy.moderateUncertainty = moderateUncertainty;
        copy.lowerBoundInclusive = lowerBoundInclusive;
        copy.upperBoundInclusive = upperBoundInclusive;
        return copy;
    }

    @Override
    public IndividualGlobalModerateExtremistUncertaintySupplier parse(IRPactInputParser parser) throws ParsingException {
        if(parser.isRestored()) {
            throw new ParsingException("not supported");
        }

        LOGGER.trace("parse '{}': {}", thisClass(), getName());

        IndividualGlobalModerateExtremistUncertaintySupplier data = new IndividualGlobalModerateExtremistUncertaintySupplier();
        data.setName(getName());
        data.setEnvironment(parser.getEnvironment());
        data.setExtremistParameter(getExtremistParameter());
        data.setExtremistUncertainty(getExtremistUncertainty());
        data.setModerateUncertainty(getModerateUncertainty());
        data.setLowerBoundInclusive(isLowerBoundInclusive());
        data.setUpperBoundInclusive(isUpperBoundInclusive());

        for(String attrName: RAConstants.UNCERTAINTY_ATTRIBUTES) {
            data.addAttributeName(attrName);
        }

        return data;
    }
}
