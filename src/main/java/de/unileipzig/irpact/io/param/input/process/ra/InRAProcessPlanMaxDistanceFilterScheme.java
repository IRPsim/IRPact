package de.unileipzig.irpact.io.param.input.process.ra;

import de.unileipzig.irpact.commons.exception.ParsingException;
import de.unileipzig.irpact.commons.util.ExceptionUtil;
import de.unileipzig.irpact.core.log.IRPLogging;
import de.unileipzig.irpact.core.log.IRPSection;
import de.unileipzig.irpact.core.process.ra.filter.RAProcessPlanMaxDistanceFilterScheme;
import de.unileipzig.irpact.io.param.input.InputParser;
import de.unileipzig.irptools.defstructure.annotation.Definition;
import de.unileipzig.irptools.defstructure.annotation.FieldDefinition;
import de.unileipzig.irptools.util.TreeAnnotationResource;
import de.unileipzig.irptools.util.log.IRPLogger;

import java.lang.invoke.MethodHandles;

import static de.unileipzig.irpact.io.param.IOConstants.*;
import static de.unileipzig.irpact.io.param.ParamUtil.addEntry;
import static de.unileipzig.irpact.io.param.ParamUtil.putClassPath;

/**
 * @author Daniel Abitz
 */
@Definition
public class InRAProcessPlanMaxDistanceFilterScheme implements InRAProcessPlanNodeFilterScheme {

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
        putClassPath(res, thisClass(), PROCESS_MODEL, PROCESS_FILTER, thisName());
        addEntry(res, thisClass(), "maxDistance");
        addEntry(res, thisClass(), "inclusive");
    }

    private static final IRPLogger LOGGER = IRPLogging.getLogger(thisClass());

    public String _name;

    @FieldDefinition
    public double maxDistance;

    @FieldDefinition
    public boolean inclusive;

    public InRAProcessPlanMaxDistanceFilterScheme() {
    }

    @Override
    public String getName() {
        return null;
    }

    public void setName(String name) {
        this._name = name;
    }

    public double getMaxDistance() {
        return maxDistance;
    }

    public void setMaxDistance(double maxDistance) {
        this.maxDistance = maxDistance;
    }

    public boolean isInclusive() {
        return inclusive;
    }

    public void setInclusive(boolean inclusive) {
        this.inclusive = inclusive;
    }

    @Override
    public Object parse(InputParser parser) throws ParsingException {
        if(getMaxDistance() < 0) {
            throw ExceptionUtil.create(ParsingException::new, "max distance {} < 0", getMaxDistance());
        }

        RAProcessPlanMaxDistanceFilterScheme scheme = new RAProcessPlanMaxDistanceFilterScheme();
        scheme.setName(getName());
        scheme.setMaxDistance(getMaxDistance());
        scheme.setInclusive(isInclusive());

        LOGGER.trace(IRPSection.INITIALIZATION_PARAMETER, "created RAProcessPlanMaxDistanceFilterScheme '{}': distance={}, inclusive={}", getName(), getMaxDistance(), isInclusive());

        return scheme;
    }
}
