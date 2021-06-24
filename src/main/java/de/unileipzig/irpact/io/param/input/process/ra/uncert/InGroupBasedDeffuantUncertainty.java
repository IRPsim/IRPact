package de.unileipzig.irpact.io.param.input.process.ra.uncert;

import de.unileipzig.irpact.commons.exception.ParsingException;
import de.unileipzig.irpact.core.agent.consumer.ConsumerAgentGroup;
import de.unileipzig.irpact.core.logging.IRPLogging;
import de.unileipzig.irpact.core.logging.IRPSection;
import de.unileipzig.irpact.core.process.ra.RAProcessModel;
import de.unileipzig.irpact.core.process.ra.uncert.GroupBasedDeffuantUncertaintyData;
import de.unileipzig.irpact.core.process.ra.uncert.GroupBasedDeffuantUncertaintySupplier;
import de.unileipzig.irpact.io.param.ParamUtil;
import de.unileipzig.irpact.io.param.input.IRPactInputParser;
import de.unileipzig.irpact.io.param.input.agent.consumer.InConsumerAgentGroup;
import de.unileipzig.irpact.io.param.input.names.InAttributeName;
import de.unileipzig.irptools.defstructure.annotation.Definition;
import de.unileipzig.irptools.defstructure.annotation.FieldDefinition;
import de.unileipzig.irptools.util.CopyCache;
import de.unileipzig.irptools.util.TreeAnnotationResource;
import de.unileipzig.irptools.util.log.IRPLogger;

import java.lang.invoke.MethodHandles;

/**
 * @author Daniel Abitz
 */
@Definition
public class InGroupBasedDeffuantUncertainty implements InUncertainty {

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
    }

    private static final IRPLogger LOGGER = IRPLogging.getLogger(thisClass());

    public String _name;

    @FieldDefinition
    public double extremistParameter;

    @FieldDefinition
    public double extremistUncertainty;

    @FieldDefinition
    public double moderateUncertainty;

    @FieldDefinition
    public boolean lowerBoundInclusive;

    @FieldDefinition
    public boolean upperBoundInclusive;

    @FieldDefinition
    public InConsumerAgentGroup[] cags;

    @FieldDefinition
    public InAttributeName[] attributeNames;

    public InGroupBasedDeffuantUncertainty() {
    }

    @Override
    public String getName() {
        return _name;
    }

    public void setName(String name) {
        this._name = name;
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

    public void setAttributeNames(InAttributeName[] attributeNames) {
        this.attributeNames = attributeNames;
    }

    public InAttributeName[] getAttributeNames() throws ParsingException {
        return ParamUtil.getNonEmptyArray(attributeNames, "attributeNames");
    }

    public void setConsumerAgentGroups(InConsumerAgentGroup[] cags) {
        this.cags = cags;
    }

    public InConsumerAgentGroup[] getConsumerAgentGroups() throws ParsingException {
        return ParamUtil.getNonEmptyArray(cags, "cags");
    }

    @Override
    public InGroupBasedDeffuantUncertainty copy(CopyCache cache) {
        return cache.copyIfAbsent(this, this::newCopy);
    }

    public InGroupBasedDeffuantUncertainty newCopy(CopyCache cache) {
        InGroupBasedDeffuantUncertainty copy = new InGroupBasedDeffuantUncertainty();
        copy._name = _name;
        copy.extremistParameter = extremistParameter;
        copy.extremistUncertainty = extremistUncertainty;
        copy.moderateUncertainty = moderateUncertainty;
        copy.lowerBoundInclusive = lowerBoundInclusive;
        copy.upperBoundInclusive = upperBoundInclusive;
        copy.cags = cache.copyArray(cags);
        copy.attributeNames = cache.copyArray(attributeNames);
        return copy;
    }

    @Override
    public boolean requiresSetup() {
        return true;
    }

    @Override
    public void setup(IRPactInputParser parser, Object input) throws ParsingException {
        RAProcessModel model = (RAProcessModel) input;

        for(InConsumerAgentGroup inCag: getConsumerAgentGroups()) {
            ConsumerAgentGroup cag = parser.parseEntityTo(inCag);
            GroupBasedDeffuantUncertaintyData data = new GroupBasedDeffuantUncertaintyData();
            data.setName(getName() + "_" + cag.getName() + "_data");
            data.setConsumerAgentGroup(cag);
            data.setExtremistParameter(getExtremistParameter());
            data.setExtremistUncertainty(getExtremistUncertainty());
            data.setModerateUncertainty(getModerateUncertainty());
            data.setLowerBoundInclusive(isLowerBoundInclusive());
            data.setUpperBoundInclusive(isUpperBoundInclusive());
            for(InAttributeName attrName: getAttributeNames()) {
                data.addAttributeName(attrName.getName());
            }

            GroupBasedDeffuantUncertaintySupplier supplier = new GroupBasedDeffuantUncertaintySupplier();
            supplier.setName(getName() + "_" + cag.getName() + "_" + "_supplier");
            supplier.setConsumerAgentGroup(cag);
            supplier.setData(data);
            supplier.setSpeedOfConvergence(model.getSpeedOfConvergence());

            if(model.getUncertaintyManager().register(supplier)) {
                LOGGER.trace(IRPSection.INITIALIZATION_PARAMETER, "added supplier '{}' to model '{}'", supplier.getName(), model.getName());
            } else {
                throw new ParsingException("supplier '{}' already exists", supplier.getName());
            }
        }
    }
}
