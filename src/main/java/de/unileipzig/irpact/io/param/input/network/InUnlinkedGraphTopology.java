package de.unileipzig.irpact.io.param.input.network;

import de.unileipzig.irpact.commons.exception.ParsingException;
import de.unileipzig.irpact.core.network.SocialGraph;
import de.unileipzig.irpact.core.network.topology.UnlinkedGraphTopology;
import de.unileipzig.irpact.io.param.input.IRPactInputParser;
import de.unileipzig.irptools.defstructure.annotation.Definition;
import de.unileipzig.irptools.defstructure.annotation.FieldDefinition;
import de.unileipzig.irptools.util.CopyCache;
import de.unileipzig.irptools.util.TreeAnnotationResource;

import java.lang.invoke.MethodHandles;
import java.util.Objects;

import static de.unileipzig.irpact.io.param.IOConstants.*;
import static de.unileipzig.irpact.io.param.ParamUtil.addEntry;
import static de.unileipzig.irpact.io.param.ParamUtil.putClassPath;

/**
 * @author Daniel Abitz
 */
@Definition
public class InUnlinkedGraphTopology implements InGraphTopologyScheme {

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
        putClassPath(res, thisClass(), NETWORK, TOPOLOGY, thisName());
        addEntry(res, thisClass(), "placeholderUnlinked");
    }

    public String _name;

    @FieldDefinition
    public double placeholderUnlinked;

    public InUnlinkedGraphTopology() {
    }

    public InUnlinkedGraphTopology(String name) {
        this._name = name;
    }

    @Override
    public InUnlinkedGraphTopology copy(CopyCache cache) {
        return cache.copyIfAbsent(this, this::newCopy);
    }

    public InUnlinkedGraphTopology newCopy(CopyCache cache) {
        InUnlinkedGraphTopology copy = new InUnlinkedGraphTopology();
        copy._name = _name;
        return copy;
    }

    @Override
    public String getName() {
        return _name;
    }

    public void setName(String name) {
        this._name = name;
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
        return Objects.equals(_name, topology._name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(_name);
    }

    @Override
    public String toString() {
        return "InFreeNetworkTopology{" +
                "_name='" + _name + '\'' +
                '}';
    }
}
