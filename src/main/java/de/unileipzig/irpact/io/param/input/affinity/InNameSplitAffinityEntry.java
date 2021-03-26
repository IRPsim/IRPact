package de.unileipzig.irpact.io.param.input.affinity;

import de.unileipzig.irpact.commons.exception.ParsingException;
import de.unileipzig.irpact.io.param.IOConstants;
import de.unileipzig.irpact.io.param.input.InRoot;
import de.unileipzig.irpact.io.param.input.InputParser;
import de.unileipzig.irpact.io.param.input.agent.consumer.InConsumerAgentGroup;
import de.unileipzig.irptools.defstructure.annotation.Definition;
import de.unileipzig.irptools.defstructure.annotation.FieldDefinition;
import de.unileipzig.irptools.util.TreeAnnotationResource;

import java.lang.invoke.MethodHandles;

import static de.unileipzig.irpact.io.param.IOConstants.*;
import static de.unileipzig.irpact.io.param.ParamUtil.*;

/**
 * @author Daniel Abitz
 */
@Definition
public class InNameSplitAffinityEntry implements InAffinityEntry {

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
        addEntry(res, thisClass(), "affinityValue");
    }

    public String _name;

    @FieldDefinition
    public double affinityValue;

    public InNameSplitAffinityEntry() {
    }

    public InNameSplitAffinityEntry(InConsumerAgentGroup srcCag, InConsumerAgentGroup tarCag, double value) {
        this._name = concName(srcCag, tarCag);
        this.affinityValue = value;
    }

    @Override
    public String getName() {
        return _name;
    }

    @Override
    public InConsumerAgentGroup getSrcCag(InputParser parser) throws ParsingException {
        String name = getSrcCagName();
        InRoot root = parser.getRoot();
        return root.findConsumerAgentGroup(name);
    }

    @Override
    public InConsumerAgentGroup getTarCag(InputParser parser) throws ParsingException {
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

    @Override
    public Object parse(InputParser parser) throws ParsingException {
        return this;
    }
}
