package de.unileipzig.irpact.io.param.input.affinity;

import de.unileipzig.irpact.commons.exception.ParsingException;
import de.unileipzig.irpact.io.param.ParamUtil;
import de.unileipzig.irpact.core.start.InputParser;
import de.unileipzig.irpact.io.param.input.agent.consumer.InConsumerAgentGroup;
import de.unileipzig.irptools.defstructure.annotation.Definition;
import de.unileipzig.irptools.defstructure.annotation.FieldDefinition;
import de.unileipzig.irptools.util.CopyCache;
import de.unileipzig.irptools.util.TreeAnnotationResource;

import java.lang.invoke.MethodHandles;
import java.util.ArrayList;
import java.util.List;

import static de.unileipzig.irpact.io.param.IOConstants.*;
import static de.unileipzig.irpact.io.param.ParamUtil.*;

/**
 * @author Daniel Abitz
 */
@Definition
public class InComplexAffinityEntry implements InAffinityEntry {

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
        putClassPath(res, thisClass(), AGENTS, CONSUMER, CONSUMER_AFFINITY, thisName());
        addEntry(res, thisClass(), "srcCag");
        addEntry(res, thisClass(), "tarCag");
        addEntry(res, thisClass(), "affinityValue");
    }

    public String _name;

    @FieldDefinition
    public InConsumerAgentGroup[] srcCag;

    @FieldDefinition
    public InConsumerAgentGroup[] tarCag;

    @FieldDefinition
    public double affinityValue;

    public InComplexAffinityEntry() {
    }

    public InComplexAffinityEntry(String name, InConsumerAgentGroup src, InConsumerAgentGroup tar, double value) {
        this._name = name;
        setSrcCag(src);
        setTarCag(tar);
        setAffinityValue(value);
    }

    public static InComplexAffinityEntry[] buildAll(InConsumerAgentGroup[] grps, double value) {
        List<InComplexAffinityEntry> list = new ArrayList<>();
        for(InConsumerAgentGroup src: grps) {
            for(InConsumerAgentGroup tar: grps) {
                InComplexAffinityEntry entry = new InComplexAffinityEntry(
                        ParamUtil.concData(src, tar),
                        src,
                        tar,
                        value
                );
                list.add(entry);
            }
        }
        return list.toArray(new InComplexAffinityEntry[0]);
    }

    public static InComplexAffinityEntry[] buildSelfLinked(InConsumerAgentGroup[] grps) {
        List<InComplexAffinityEntry> list = new ArrayList<>();
        for(InConsumerAgentGroup src: grps) {
            for(InConsumerAgentGroup tar: grps) {
                InComplexAffinityEntry entry = new InComplexAffinityEntry(
                        ParamUtil.concData(src, tar),
                        src,
                        tar,
                        src == tar ? 1 : 0
                );
                list.add(entry);
            }
        }
        return list.toArray(new InComplexAffinityEntry[0]);
    }

    @Override
    public InComplexAffinityEntry copy(CopyCache cache) {
        return cache.copyIfAbsent(this, this::newCopy);
    }

    public InComplexAffinityEntry newCopy(CopyCache cache) {
        InComplexAffinityEntry copy = new InComplexAffinityEntry();
        copy._name = _name;
        copy.srcCag = cache.copyArray(srcCag);
        copy.tarCag = cache.copyArray(tarCag);
        copy.affinityValue = affinityValue;
        return copy;
    }

    @Override
    public String getName() {
        return _name;
    }

    public void setName(String name) {
        this._name = name;
    }

    public void setSrcCag(InConsumerAgentGroup srcCag) {
        this.srcCag = new InConsumerAgentGroup[]{srcCag};
    }

    public InConsumerAgentGroup getSrcCag() throws ParsingException {
        return ParamUtil.getInstance(srcCag, "Source-ConsumerAgentGroup");
    }

    @Override
    public InConsumerAgentGroup getSrcCag(InputParser parser) throws ParsingException {
        return getSrcCag();
    }

    @Override
    public String getSrcCagName() throws ParsingException {
        return getSrcCag().getName();
    }

    public void setTarCag(InConsumerAgentGroup tarCag) {
        this.tarCag = new InConsumerAgentGroup[]{tarCag};
    }

    public InConsumerAgentGroup getTarCag() throws ParsingException {
        return ParamUtil.getInstance(tarCag, "Target-ConsumerAgentGroup");
    }

    @Override
    public InConsumerAgentGroup getTarCag(InputParser parser) throws ParsingException {
        return getTarCag();
    }

    @Override
    public String getTarCagName() throws ParsingException {
        return getTarCag().getName();
    }

    public void setAffinityValue(double affinityValue) {
        this.affinityValue = affinityValue;
    }

    @Override
    public double getAffinityValue() {
        return affinityValue;
    }
}
