package de.unileipzig.irpact.io.param.input.process.ra;

import de.unileipzig.irpact.commons.exception.ParsingException;
import de.unileipzig.irpact.commons.util.ExceptionUtil;
import de.unileipzig.irpact.core.logging.IRPLogging;
import de.unileipzig.irpact.core.logging.IRPSection;
import de.unileipzig.irpact.core.network.filter.MaxDistanceNodeFilterScheme;
import de.unileipzig.irpact.develop.PotentialProblem;
import de.unileipzig.irpact.core.start.IRPactInputParser;
import de.unileipzig.irpact.io.param.input.InRootUI;
import de.unileipzig.irptools.defstructure.annotation.Definition;
import de.unileipzig.irptools.defstructure.annotation.FieldDefinition;
import de.unileipzig.irptools.util.CopyCache;
import de.unileipzig.irptools.util.TreeAnnotationResource;
import de.unileipzig.irptools.util.log.IRPLogger;

import java.lang.invoke.MethodHandles;

import static de.unileipzig.irpact.io.param.ParamUtil.addEntry;
import static de.unileipzig.irpact.io.param.ParamUtil.putClassPath;

/**
 * @author Daniel Abitz
 */
@Definition
public class InMaxDistanceNodeFilterScheme implements InRAProcessPlanNodeFilterScheme {

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
        putClassPath(res, thisClass(), InRootUI.PROCESS_FILTER_MAX);
        addEntry(res, thisClass(), "maxDistance");
        addEntry(res, thisClass(), "inclusive");
    }

    private static final IRPLogger LOGGER = IRPLogging.getLogger(thisClass());

    public String _name;

    @FieldDefinition
    public double maxDistance;

    @FieldDefinition
    public boolean inclusive;

    public InMaxDistanceNodeFilterScheme() {
    }

    public InMaxDistanceNodeFilterScheme(String name, double maxDistance, boolean inclusive) {
        setName(name);
        setMaxDistance(maxDistance);
        setInclusive(inclusive);
    }

    @Override
    public InMaxDistanceNodeFilterScheme copy(CopyCache cache) {
        return cache.copyIfAbsent(this, this::newCopy);
    }

    public InMaxDistanceNodeFilterScheme newCopy(CopyCache cache) {
        InMaxDistanceNodeFilterScheme copy = new InMaxDistanceNodeFilterScheme();
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
    public MaxDistanceNodeFilterScheme parse(IRPactInputParser parser) throws ParsingException {
        if(getMaxDistance() < 0) {
            throw ExceptionUtil.create(ParsingException::new, "max distance {} < 0", getMaxDistance());
        }

        MaxDistanceNodeFilterScheme scheme = new MaxDistanceNodeFilterScheme();
        scheme.setName(getName());
        scheme.setMaxDistance(getMaxDistance());
        scheme.setInclusive(isInclusive());

        LOGGER.trace(IRPSection.INITIALIZATION_PARAMETER, "created RAProcessPlanMaxDistanceFilterScheme '{}': distance={}, inclusive={}", getName(), getMaxDistance(), isInclusive());

        return scheme;
    }
}
