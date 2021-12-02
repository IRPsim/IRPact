package de.unileipzig.irpact.io.param.input.network;

import de.unileipzig.irpact.commons.exception.ParsingException;
import de.unileipzig.irpact.io.param.LocalizedUiResource;
import de.unileipzig.irpact.io.param.ParamUtil;
import de.unileipzig.irpact.io.param.input.InIRPactEntity;
import de.unileipzig.irpact.io.param.input.agent.consumer.InConsumerAgentGroup;
import de.unileipzig.irptools.defstructure.annotation.Definition;
import de.unileipzig.irptools.defstructure.annotation.DefinitionName;
import de.unileipzig.irptools.defstructure.annotation.FieldDefinition;
import de.unileipzig.irptools.util.CopyCache;
import de.unileipzig.irptools.util.TreeAnnotationResource;

import java.lang.invoke.MethodHandles;

import static de.unileipzig.irpact.io.param.input.TreeViewStructureEnum.NETWORK_TOPO_FREE_TIES;

/**
 * @author Daniel Abitz
 */
@Definition
@LocalizedUiResource.PutClassPath(NETWORK_TOPO_FREE_TIES)
public class InNumberOfTies implements InIRPactEntity {

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
            geq0Domain = true,
            intDefault = 0
    )
    public int count;

    @FieldDefinition
    @LocalizedUiResource.AddEntry
    public InConsumerAgentGroup[] cags = new InConsumerAgentGroup[0];


    public InNumberOfTies() {
    }

    public InNumberOfTies(String name, InConsumerAgentGroup cag, int count) {
        this(name, new InConsumerAgentGroup[]{cag}, count);
    }

    public InNumberOfTies(String name, InConsumerAgentGroup[] cags, int count) {
        this.name = name;
        this.cags = cags;
        this.count = count;
    }

    @Override
    public InNumberOfTies copy(CopyCache cache) {
        return cache.copyIfAbsent(this, this::newCopy);
    }

    public InNumberOfTies newCopy(CopyCache cache) {
        InNumberOfTies copy = new InNumberOfTies();
        copy.name = name;
        copy.cags = cache.copyArray(cags);
        copy.count = count;
        return copy;
    }

    @Override
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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
