package de.unileipzig.irpact.io.param.input.process.ra.uncert;

import de.unileipzig.irpact.commons.exception.ParsingException;
import de.unileipzig.irpact.core.agent.consumer.ConsumerAgentGroup;
import de.unileipzig.irpact.core.logging.IRPLogging;
import de.unileipzig.irpact.core.logging.IRPSection;
import de.unileipzig.irpact.core.process.ra.RAConstants;
import de.unileipzig.irpact.core.process.ra.uncert.GlobalDeffuantUncertaintySupplier;
import de.unileipzig.irpact.core.process.ra.uncert.GlobalDeffuantUncertaintyData;
import de.unileipzig.irpact.core.process.ra.uncert.UncertaintyManager;
import de.unileipzig.irpact.io.param.ParamUtil;
import de.unileipzig.irpact.core.start.IRPactInputParser;
import de.unileipzig.irpact.io.param.input.agent.consumer.InConsumerAgentGroup;
import de.unileipzig.irpact.io.param.input.names.InAttributeName;
import de.unileipzig.irptools.defstructure.annotation.Definition;
import de.unileipzig.irptools.defstructure.annotation.FieldDefinition;
import de.unileipzig.irptools.util.CopyCache;
import de.unileipzig.irptools.util.TreeAnnotationResource;
import de.unileipzig.irptools.util.log.IRPLogger;

import java.lang.invoke.MethodHandles;

import static de.unileipzig.irpact.io.param.IOConstants.PROCESS_MODEL;
import static de.unileipzig.irpact.io.param.IOConstants.PROCESS_MODEL_RA_UNCERT;
import static de.unileipzig.irpact.io.param.ParamUtil.*;
import static de.unileipzig.irpact.io.param.ParamUtil.VALUE_FALSE;

/**
 * @author Daniel Abitz
 */
@Definition
public class InGlobalDeffuantUncertainty implements InUncertainty {

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
        putClassPath(res, thisClass(), PROCESS_MODEL, PROCESS_MODEL_RA_UNCERT, thisName());

        addEntry(res, thisClass(), "extremistParameter");
        addEntry(res, thisClass(), "extremistUncertainty");
        addEntry(res, thisClass(), "moderateUncertainty");
        addEntry(res, thisClass(), "lowerBoundInclusive");
        addEntry(res, thisClass(), "upperBoundInclusive");
        addEntry(res, thisClass(), "cags");
        addEntry(res, thisClass(), "attributeNames");

        setDefault(res, thisClass(), "extremistParameter", VALUE_NEG_ONE);
        setDefault(res, thisClass(), "extremistUncertainty", varargs(RAConstants.DEFAULT_EXTREMIST_UNCERTAINTY));
        setDefault(res, thisClass(), "moderateUncertainty", varargs(RAConstants.DEFAULT_MODERATE_UNCERTAINTY));
        setDefault(res, thisClass(), "lowerBoundInclusive", VALUE_FALSE);
        setDefault(res, thisClass(), "upperBoundInclusive", VALUE_FALSE);
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

    public InGlobalDeffuantUncertainty() {
    }

    @Override
    public String getName() {
        return _name;
    }

    public void setName(String name) {
        this._name = name;
    }

    public void setDefaultValues() {
        setExtremistParameter(-1);
        setExtremistUncertainty(RAConstants.DEFAULT_EXTREMIST_UNCERTAINTY);
        setModerateUncertainty(RAConstants.DEFAULT_MODERATE_UNCERTAINTY);
        setLowerBoundInclusive(false);
        setUpperBoundInclusive(false);
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
    public InGlobalDeffuantUncertainty copy(CopyCache cache) {
        return cache.copyIfAbsent(this, this::newCopy);
    }

    public InGlobalDeffuantUncertainty newCopy(CopyCache cache) {
        InGlobalDeffuantUncertainty copy = new InGlobalDeffuantUncertainty();
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
        String modelName = getAs(input, 0);
        UncertaintyManager uncertaintyManager = getAs(input, 1);
        int speedOfConvergence = getAs(input, 2);

        GlobalDeffuantUncertaintyData data = new GlobalDeffuantUncertaintyData();
        data.setName(getName() + "_data");
        data.setEnvironment(parser.getEnvironment());
        data.setExtremistParameter(getExtremistParameter());
        data.setExtremistUncertainty(getExtremistUncertainty());
        data.setModerateUncertainty(getModerateUncertainty());
        data.setLowerBoundInclusive(isLowerBoundInclusive());
        data.setUpperBoundInclusive(isUpperBoundInclusive());

        GlobalDeffuantUncertaintySupplier supplier = new GlobalDeffuantUncertaintySupplier();
        supplier.setName(getName() + "_supplier");
        supplier.setData(data);
        supplier.setSpeedOfConvergence(speedOfConvergence);

        for(InAttributeName attrName: getAttributeNames()) {
            data.addAttributeName(attrName.getName());
        }

        for(InConsumerAgentGroup inCag: getConsumerAgentGroups()) {
            ConsumerAgentGroup cag = parser.parseEntityTo(inCag);
            supplier.addConsumerAgentGroup(cag);
        }

        if(uncertaintyManager.register(supplier)) {
            LOGGER.trace(IRPSection.INITIALIZATION_PARAMETER, "added supplier '{}' to model '{}'", supplier.getName(), modelName);
        } else {
            throw new ParsingException("supplier '{}' already exists", supplier.getName());
        }
    }
}
