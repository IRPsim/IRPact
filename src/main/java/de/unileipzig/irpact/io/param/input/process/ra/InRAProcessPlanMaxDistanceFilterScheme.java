package de.unileipzig.irpact.io.param.input.process.ra;

import de.unileipzig.irpact.commons.exception.ParsingException;
import de.unileipzig.irpact.commons.util.ExceptionUtil;
import de.unileipzig.irpact.core.logging.IRPLogging;
import de.unileipzig.irpact.core.logging.IRPSection;
import de.unileipzig.irpact.core.process.ra.filter.RAProcessPlanMaxDistanceFilterScheme;
import de.unileipzig.irpact.develop.PotentialProblem;
import de.unileipzig.irpact.core.start.IRPactInputParser;
import de.unileipzig.irptools.defstructure.annotation.Definition;
import de.unileipzig.irptools.defstructure.annotation.FieldDefinition;
import de.unileipzig.irptools.util.CopyCache;
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

    public InRAProcessPlanMaxDistanceFilterScheme(String name, double maxDistance, boolean inclusive) {
        setName(name);
        setMaxDistance(maxDistance);
        setInclusive(inclusive);
    }

    @Override
    public InRAProcessPlanMaxDistanceFilterScheme copy(CopyCache cache) {
        return cache.copyIfAbsent(this, this::newCopy);
    }

    public InRAProcessPlanMaxDistanceFilterScheme newCopy(CopyCache cache) {
        InRAProcessPlanMaxDistanceFilterScheme copy = new InRAProcessPlanMaxDistanceFilterScheme();
        copy._name = _name;
        copy.maxDistance = maxDistance;
        copy.inclusive = inclusive;
        return copy;
    }

    @PotentialProblem("teste, ob es andere return null gibt")
    @Override
    public String getName() {
        return _name;
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
    public Object parse(IRPactInputParser parser) throws ParsingException {
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
