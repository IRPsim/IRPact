package de.unileipzig.irpact.io.param.input.process.ra;

import de.unileipzig.irpact.commons.exception.ParsingException;
import de.unileipzig.irpact.core.logging.IRPLogging;
import de.unileipzig.irpact.core.logging.IRPSection;
import de.unileipzig.irpact.core.network.filter.EntireNetworkNodeFilterScheme;
import de.unileipzig.irpact.core.start.IRPactInputParser;
import de.unileipzig.irpact.io.param.LocalizedUiResource;
import de.unileipzig.irptools.defstructure.annotation.Definition;
import de.unileipzig.irptools.defstructure.annotation.DefinitionName;
import de.unileipzig.irptools.defstructure.annotation.FieldDefinition;
import de.unileipzig.irptools.util.CopyCache;
import de.unileipzig.irptools.util.TreeAnnotationResource;
import de.unileipzig.irptools.util.log.IRPLogger;

import java.lang.invoke.MethodHandles;

import static de.unileipzig.irpact.io.param.input.TreeViewStructureEnum.PROCESS_MODEL4_DISTANCE_ENTIRE;

/**
 * @author Daniel Abitz
 */
@Definition
@LocalizedUiResource.PutClassPath(PROCESS_MODEL4_DISTANCE_ENTIRE)
public class InEntireNetworkNodeFilterDistanceScheme implements InNodeDistanceFilterScheme {

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
    public double placeholder;

    public InEntireNetworkNodeFilterDistanceScheme() {
    }

    public InEntireNetworkNodeFilterDistanceScheme(String name) {
        setName(name);
    }

    @Override
    public InEntireNetworkNodeFilterDistanceScheme copy(CopyCache cache) {
        return cache.copyIfAbsent(this, this::newCopy);
    }

    public InEntireNetworkNodeFilterDistanceScheme newCopy(CopyCache cache) {
        InEntireNetworkNodeFilterDistanceScheme copy = new InEntireNetworkNodeFilterDistanceScheme();
        copy.name = name;
        return copy;
    }

    @Override
    public String getName() {
        return null;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public EntireNetworkNodeFilterScheme parse(IRPactInputParser parser) throws ParsingException {
        EntireNetworkNodeFilterScheme scheme = createScheme();
        LOGGER.trace(IRPSection.INITIALIZATION_PARAMETER, "created EntireNetworkNodeFilterScheme '{}'", getName());
        return scheme;
    }

    @Override
    public EntireNetworkNodeFilterScheme createScheme() {
        EntireNetworkNodeFilterScheme scheme = new EntireNetworkNodeFilterScheme();
        scheme.setName(getName());
        return scheme;
    }
}
