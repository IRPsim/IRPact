package de.unileipzig.irpact.io.param.input.affinity;

import de.unileipzig.irpact.commons.exception.ParsingException;
import de.unileipzig.irpact.core.start.IRPactInputParser;
import de.unileipzig.irpact.io.param.LocalizedUiResource;
import de.unileipzig.irpact.io.param.input.InRoot;
import de.unileipzig.irpact.core.start.InputParser;
import de.unileipzig.irpact.io.param.input.agent.consumer.InConsumerAgentGroup;
import de.unileipzig.irptools.defstructure.annotation.Definition;
import de.unileipzig.irptools.defstructure.annotation.DefinitionName;
import de.unileipzig.irptools.defstructure.annotation.FieldDefinition;
import de.unileipzig.irptools.util.CopyCache;
import de.unileipzig.irptools.util.TreeAnnotationResource;

import java.lang.invoke.MethodHandles;
import java.util.ArrayList;
import java.util.List;

import static de.unileipzig.irpact.io.param.ParamUtil.*;
import static de.unileipzig.irpact.io.param.input.TreeViewStructureEnum.AGENTS_CONSUMER_AFF_NAMESPLIT;

/**
 * @author Daniel Abitz
 */
@Definition
@LocalizedUiResource.PutClassPath(AGENTS_CONSUMER_AFF_NAMESPLIT)
public class InNameSplitAffinityEntry implements InAffinityEntry {

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
    public double affinityValue;

    public InNameSplitAffinityEntry() {
    }

    public InNameSplitAffinityEntry(InConsumerAgentGroup srcCag, InConsumerAgentGroup tarCag, double value) {
        this.name = concName(srcCag, tarCag);
        this.affinityValue = value;
    }

    public static InNameSplitAffinityEntry[] createAllWithSelfAffinity(InConsumerAgentGroup[] cags) {
        List<InNameSplitAffinityEntry> entries = new ArrayList<>();
        for(InConsumerAgentGroup from: cags) {
            for(InConsumerAgentGroup to: cags) {
                double value = from == to ? 1 : 0;
                entries.add(new InNameSplitAffinityEntry(from, to, value));
            }
        }
        return entries.toArray(new InNameSplitAffinityEntry[0]);
    }

    @Override
    public InNameSplitAffinityEntry copy(CopyCache cache) {
        return cache.copyIfAbsent(this, this::newCopy);
    }

    public InNameSplitAffinityEntry newCopy(CopyCache cache) {
        InNameSplitAffinityEntry copy = new InNameSplitAffinityEntry();
        copy.name = name;
        copy.affinityValue = affinityValue;
        return copy;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public InConsumerAgentGroup getSrcCag(InputParser p) throws ParsingException {
        IRPactInputParser parser = (IRPactInputParser) p;
        String name = getSrcCagName();
        InRoot root = parser.getRoot();
        return root.findConsumerAgentGroup(name);
    }

    @Override
    public InConsumerAgentGroup getTarCag(InputParser p) throws ParsingException {
        IRPactInputParser parser = (IRPactInputParser) p;
        String name = getTarCagName();
        InRoot root = parser.getRoot();
        return root.findConsumerAgentGroup(name);
    }

    @Override
    public double getAffinityValue() {
        return affinityValue;
    }

    @Override
    public String getSrcCagName() throws ParsingException {
        return firstPart(getName());
    }

    @Override
    public String getTarCagName() throws ParsingException {
        return secondPart(getName());
    }
}
