package de.unileipzig.irpact.io.param.input.network;

import de.unileipzig.irpact.commons.exception.ParsingException;
import de.unileipzig.irpact.io.param.input.InIRPactEntity;
import de.unileipzig.irpact.io.param.ParamUtil;
import de.unileipzig.irpact.io.param.input.agent.consumer.InConsumerAgentGroup;
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
public class InNumberOfTies implements InIRPactEntity {

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
        putClassPath(res, thisClass(), NETWORK, TOPOLOGY, InFreeNetworkTopology.thisName(), thisName());
        addEntry(res, thisClass(), "count");
        addEntry(res, thisClass(), "cags");
    }

    public String _name;

    @FieldDefinition
    public InConsumerAgentGroup[] cags;

    @FieldDefinition
    public int count;

    public InNumberOfTies() {
    }

    public InNumberOfTies(String name, InConsumerAgentGroup cag, int count) {
        this(name, new InConsumerAgentGroup[]{cag}, count);
    }

    public InNumberOfTies(String name, InConsumerAgentGroup[] cags, int count) {
        this._name = name;
        this.cags = cags;
        this.count = count;
    }

    @Override
    public InNumberOfTies copy(CopyCache cache) {
        return cache.copyIfAbsent(this, this::newCopy);
    }

    public InNumberOfTies newCopy(CopyCache cache) {
        InNumberOfTies copy = new InNumberOfTies();
        copy._name = _name;
        copy.cags = cache.copyArray(cags);
        copy.count = count;
        return copy;
    }

    @Override
    public String getName() {
        return _name;
    }

    public void setName(String name) {
        this._name = name;
    }

    public InConsumerAgentGroup[] getConsumerAgentGroups() throws ParsingException {
        return ParamUtil.getNonEmptyArray(cags, "ConsumerAgentGroup");
    }

    public void setConsumerAgentGroups(InConsumerAgentGroup[] cags) {
        this.cags = cags;
    }

    public void setConsumerAgentGroup(InConsumerAgentGroup cag) {
        this.cags = new InConsumerAgentGroup[]{cag};
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
