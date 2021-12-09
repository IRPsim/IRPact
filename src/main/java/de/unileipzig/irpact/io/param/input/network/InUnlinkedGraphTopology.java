package de.unileipzig.irpact.io.param.input.network;

import de.unileipzig.irpact.commons.exception.ParsingException;
import de.unileipzig.irpact.core.network.SocialGraph;
import de.unileipzig.irpact.core.network.topology.UnlinkedGraphTopology;
import de.unileipzig.irpact.core.start.IRPactInputParser;
import de.unileipzig.irpact.io.param.LocalizedUiResource;
import de.unileipzig.irptools.defstructure.annotation.Definition;
import de.unileipzig.irptools.defstructure.annotation.DefinitionName;
import de.unileipzig.irptools.defstructure.annotation.FieldDefinition;
import de.unileipzig.irptools.util.CopyCache;
import de.unileipzig.irptools.util.TreeAnnotationResource;

import java.lang.invoke.MethodHandles;
import java.util.Objects;

import static de.unileipzig.irpact.io.param.input.TreeViewStructureEnum.NETWORK_TOPO_UNLINKED;

/**
 * @author Daniel Abitz
 */
@Definition
@LocalizedUiResource.PutClassPath(NETWORK_TOPO_UNLINKED)
public class InUnlinkedGraphTopology implements InGraphTopologyScheme {

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

    @DefinitionName
    public String name;

    @FieldDefinition
    @LocalizedUiResource.AddEntry
    public double placeholderUnlinked;

    public InUnlinkedGraphTopology() {
    }

    public InUnlinkedGraphTopology(String name) {
        this.name = name;
    }

    @Override
    public InUnlinkedGraphTopology copy(CopyCache cache) {
        return cache.copyIfAbsent(this, this::newCopy);
    }

    public InUnlinkedGraphTopology newCopy(CopyCache cache) {
        InUnlinkedGraphTopology copy = new InUnlinkedGraphTopology();
        copy.name = name;
        return copy;
    }

    @Override
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public UnlinkedGraphTopology parse(IRPactInputParser parser) throws ParsingException {
        return new UnlinkedGraphTopology(SocialGraph.Type.COMMUNICATION, getName());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof InUnlinkedGraphTopology)) return false;
        InUnlinkedGraphTopology topology = (InUnlinkedGraphTopology) o;
        return Objects.equals(name, topology.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }

    @Override
    public String toString() {
        return "InFreeNetworkTopology{" +
                "_name='" + name + '\'' +
                '}';
    }
}
