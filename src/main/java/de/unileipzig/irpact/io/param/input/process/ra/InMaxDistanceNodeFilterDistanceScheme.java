package de.unileipzig.irpact.io.param.input.process.ra;

import de.unileipzig.irpact.commons.exception.ParsingException;
import de.unileipzig.irpact.commons.util.StringUtil;
import de.unileipzig.irpact.core.logging.IRPLogging;
import de.unileipzig.irpact.core.logging.IRPSection;
import de.unileipzig.irpact.core.network.filter.MaxDistanceNodeFilterScheme;
import de.unileipzig.irpact.core.start.IRPactInputParser;
import de.unileipzig.irpact.develop.PotentialProblem;
import de.unileipzig.irpact.io.param.LocalizedUiResource;
import de.unileipzig.irptools.defstructure.annotation.Definition;
import de.unileipzig.irptools.defstructure.annotation.DefinitionName;
import de.unileipzig.irptools.defstructure.annotation.FieldDefinition;
import de.unileipzig.irptools.util.CopyCache;
import de.unileipzig.irptools.util.TreeAnnotationResource;
import de.unileipzig.irptools.util.log.IRPLogger;

import java.lang.invoke.MethodHandles;

import static de.unileipzig.irpact.io.param.input.TreeViewStructureEnum.PROCESS_MODEL4_DISTANCE_MAX;

/**
 * @author Daniel Abitz
 */
@Definition
@LocalizedUiResource.PutClassPath(PROCESS_MODEL4_DISTANCE_MAX)
public class InMaxDistanceNodeFilterDistanceScheme implements InNodeDistanceFilterScheme {

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
            geq0Domain = true,
            decDefault = 0
    )
    public double maxDistance;

    @FieldDefinition
    @LocalizedUiResource.AddEntry
    @LocalizedUiResource.SimpleSet(
            boolDomain = true,
            boolDefault = true
    )
    public boolean inclusive;

    public InMaxDistanceNodeFilterDistanceScheme() {
    }

    public InMaxDistanceNodeFilterDistanceScheme(String name, double maxDistance, boolean inclusive) {
        setName(name);
        setMaxDistance(maxDistance);
        setInclusive(inclusive);
    }

    @Override
    public InMaxDistanceNodeFilterDistanceScheme copy(CopyCache cache) {
        return cache.copyIfAbsent(this, this::newCopy);
    }

    public InMaxDistanceNodeFilterDistanceScheme newCopy(CopyCache cache) {
        InMaxDistanceNodeFilterDistanceScheme copy = new InMaxDistanceNodeFilterDistanceScheme();
        copy.name = name;
        copy.maxDistance = maxDistance;
        copy.inclusive = inclusive;
        return copy;
    }

    @PotentialProblem("teste, ob es andere return null gibt")
    @Override
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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
        MaxDistanceNodeFilterScheme scheme = createScheme();
        LOGGER.trace(IRPSection.INITIALIZATION_PARAMETER, "created RAProcessPlanMaxDistanceFilterScheme '{}': distance={}, inclusive={}", getName(), getMaxDistance(), isInclusive());
        return scheme;
    }

    @Override
    public MaxDistanceNodeFilterScheme createScheme() {
        if(getMaxDistance() < 0) {
            throw new IllegalArgumentException(StringUtil.format("max distance {} < 0", getMaxDistance()));
        }

        MaxDistanceNodeFilterScheme scheme = new MaxDistanceNodeFilterScheme();
        scheme.setName(getName());
        scheme.setMaxDistance(getMaxDistance());
        scheme.setInclusive(isInclusive());
        return scheme;
    }
}
