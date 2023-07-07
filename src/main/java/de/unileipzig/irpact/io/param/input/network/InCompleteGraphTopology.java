package de.unileipzig.irpact.io.param.input.network;

import de.unileipzig.irpact.commons.exception.ParsingException;
import de.unileipzig.irpact.core.network.SocialGraph;
import de.unileipzig.irpact.core.network.topology.CompleteGraphTopology;
import de.unileipzig.irpact.core.start.IRPactInputParser;
import de.unileipzig.irpact.io.param.LocalizedUiResource;
import de.unileipzig.irptools.defstructure.annotation.Definition;
import de.unileipzig.irptools.defstructure.annotation.DefinitionName;
import de.unileipzig.irptools.defstructure.annotation.FieldDefinition;
import de.unileipzig.irptools.util.CopyCache;
import de.unileipzig.irptools.util.TreeAnnotationResource;

import java.lang.invoke.MethodHandles;

import static de.unileipzig.irpact.io.param.input.TreeViewStructureEnum.NETWORK_TOPO_COMPLETE;

/**
 * @author Daniel Abitz
 */
@Definition
@LocalizedUiResource.PutClassPath(NETWORK_TOPO_COMPLETE)
public class InCompleteGraphTopology implements InGraphTopologyScheme {

    private static final MethodHandles.Lookup L = MethodHandles.lookup();
    public static Class<?> thisClass() {
        return L.lookupClass();
    }
    public static String thisName() {
        return thisClass().getSimpleName();
    }

    @TreeAnnotationResource.Init
    public static void initRes(TreeAnnotationResource res) {
    }
    @TreeAnnotationResource.Apply
    public static void applyRes(TreeAnnotationResource res) {
    }

    @DefinitionName
    public String name;

    @FieldDefinition
    @LocalizedUiResource.AddEntry
    @LocalizedUiResource.SimpleSet(
            decDefault = 0,
            hidden = true
    )
    public double initialWeight;

    public InCompleteGraphTopology() {
    }

    public InCompleteGraphTopology(String name, double initialWeight) {
        this.name = name;
        this.initialWeight = initialWeight;
    }

    @Override
    public InCompleteGraphTopology copy(CopyCache cache) {
        return cache.copyIfAbsent(this, this::newCopy);
    }

    public InCompleteGraphTopology newCopy(CopyCache cache) {
        InCompleteGraphTopology copy = new InCompleteGraphTopology();
        copy.name = name;
        copy.initialWeight = initialWeight;
        return copy;
    }

    @Override
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getInitialWeight() {
        return initialWeight;
    }

    @Override
    public CompleteGraphTopology parse(IRPactInputParser parser) throws ParsingException {
        return new CompleteGraphTopology(SocialGraph.Type.COMMUNICATION, getName(), getInitialWeight());
    }
}
