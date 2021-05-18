package de.unileipzig.irpact.io.param.input.network;

import de.unileipzig.irpact.commons.exception.ParsingException;
import de.unileipzig.irpact.core.network.SocialGraph;
import de.unileipzig.irpact.core.network.topology.CompleteGraphTopology;
import de.unileipzig.irpact.io.param.input.IRPactInputParser;
import de.unileipzig.irptools.defstructure.annotation.Definition;
import de.unileipzig.irptools.defstructure.annotation.FieldDefinition;
import de.unileipzig.irptools.util.CopyCache;
import de.unileipzig.irptools.util.TreeAnnotationResource;

import java.lang.invoke.MethodHandles;

import static de.unileipzig.irpact.io.param.IOConstants.NETWORK;
import static de.unileipzig.irpact.io.param.IOConstants.TOPOLOGY;
import static de.unileipzig.irpact.io.param.ParamUtil.addEntry;
import static de.unileipzig.irpact.io.param.ParamUtil.putClassPath;

/**
 * @author Daniel Abitz
 */
@Definition
public class InCompleteGraphTopology implements InGraphTopologyScheme {

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
        addEntry(res, thisClass(), "initialWeight");
    }

    public String _name;

    @FieldDefinition
    public double initialWeight;

    public InCompleteGraphTopology() {
    }

    public InCompleteGraphTopology(String name, double initialWeight) {
        this._name = name;
        this.initialWeight = initialWeight;
    }

    @Override
    public InCompleteGraphTopology copy(CopyCache cache) {
        return cache.copyIfAbsent(this, this::newCopy);
    }

    public InCompleteGraphTopology newCopy(CopyCache cache) {
        InCompleteGraphTopology copy = new InCompleteGraphTopology();
        copy._name = _name;
        copy.initialWeight = initialWeight;
        return copy;
    }

    @Override
    public String getName() {
        return _name;
    }

    public void setName(String name) {
        this._name = name;
    }

    public double getInitialWeight() {
        return initialWeight;
    }

    @Override
    public CompleteGraphTopology parse(IRPactInputParser parser) throws ParsingException {
        return new CompleteGraphTopology(SocialGraph.Type.COMMUNICATION, getName(), getInitialWeight());
    }
}
