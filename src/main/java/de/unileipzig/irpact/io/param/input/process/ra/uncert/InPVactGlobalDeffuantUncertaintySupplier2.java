package de.unileipzig.irpact.io.param.input.process.ra.uncert;

import de.unileipzig.irpact.commons.exception.ParsingException;
import de.unileipzig.irpact.core.logging.IRPLogging;
import de.unileipzig.irpact.core.process.ra.RAConstants;
import de.unileipzig.irpact.core.process2.uncert.GlobalDeffuantUncertainty;
import de.unileipzig.irpact.core.start.IRPactInputParser;
import de.unileipzig.irpact.io.param.input.InRootUI;
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
public class InPVactGlobalDeffuantUncertaintySupplier2 implements InUncertaintySupplier {

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
        putClassPath(res, thisClass(), InRootUI.PROCESS_UNCERT_GLDEFF);

        addEntry(res, thisClass(), "extremistParameter");
        addEntry(res, thisClass(), "extremistUncertainty");
        addEntry(res, thisClass(), "moderateUncertainty");
        addEntry(res, thisClass(), "lowerBoundInclusive");
        addEntry(res, thisClass(), "upperBoundInclusive");

        setDomain(res, thisClass(), "extremistParameter", DOMAIN_CLOSED_0_1);

        setDefault(res, thisClass(), "extremistParameter", asValue(RAConstants.DEFAULT_EXTREMIST_RATE));
        setDefault(res, thisClass(), "extremistUncertainty", asValue(RAConstants.DEFAULT_EXTREMIST_UNCERTAINTY));
        setDefault(res, thisClass(), "moderateUncertainty", asValue(RAConstants.DEFAULT_MODERATE_UNCERTAINTY));
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

    public InPVactGlobalDeffuantUncertaintySupplier2() {
    }

    @Override
    public String getName() {
        return _name;
    }

    public void setName(String name) {
        this._name = name;
    }

    public void setDefaultValues() {
        setExtremistParameter(RAConstants.DEFAULT_EXTREMIST_RATE);
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

    @Override
    public InPVactGlobalDeffuantUncertaintySupplier2 copy(CopyCache cache) {
        return cache.copyIfAbsent(this, this::newCopy);
    }

    public InPVactGlobalDeffuantUncertaintySupplier2 newCopy(CopyCache cache) {
        InPVactGlobalDeffuantUncertaintySupplier2 copy = new InPVactGlobalDeffuantUncertaintySupplier2();
        copy._name = _name;
        copy.extremistParameter = extremistParameter;
        copy.extremistUncertainty = extremistUncertainty;
        copy.moderateUncertainty = moderateUncertainty;
        copy.lowerBoundInclusive = lowerBoundInclusive;
        copy.upperBoundInclusive = upperBoundInclusive;
        return copy;
    }

    @Override
    public boolean requiresSetup() {
        return true;
    }

    @Override
    public GlobalDeffuantUncertainty parse(IRPactInputParser parser) throws ParsingException {
        if(parser.isRestored()) {
            throw new ParsingException("not supported");
        }

        LOGGER.trace("parse '{}': {}", thisClass(), getName());

        GlobalDeffuantUncertainty data = new GlobalDeffuantUncertainty();
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
