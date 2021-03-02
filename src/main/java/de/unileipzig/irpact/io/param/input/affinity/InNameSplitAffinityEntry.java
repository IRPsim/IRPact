package de.unileipzig.irpact.io.param.input.affinity;

import de.unileipzig.irpact.commons.exception.ParsingException;
import de.unileipzig.irpact.io.param.input.InRoot;
import de.unileipzig.irpact.io.param.ParamUtil;
import de.unileipzig.irpact.io.param.input.InputParser;
import de.unileipzig.irpact.io.param.input.agent.consumer.InConsumerAgentGroup;
import de.unileipzig.irpact.util.AddToRoot;
import de.unileipzig.irptools.defstructure.annotation.Definition;
import de.unileipzig.irptools.defstructure.annotation.FieldDefinition;
import de.unileipzig.irptools.util.TreeAnnotationResource;

import java.lang.invoke.MethodHandles;

/**
 * @author Daniel Abitz
 */
@AddToRoot
@Definition
public class InNameSplitAffinityEntry implements InAffinityEntry {

    //damit ich bei copy&paste nie mehr vergesse die Klasse anzupassen :)
    private static final MethodHandles.Lookup L = MethodHandles.lookup();
    public static Class<?> thisClass() {
        return L.lookupClass();
    }

    public static void initRes(TreeAnnotationResource res) {
    }
    public static void applyRes(TreeAnnotationResource res) {
    }

    public String _name;

    @FieldDefinition
    public double affinityValue;

    public InNameSplitAffinityEntry() {
    }

    public InNameSplitAffinityEntry(InConsumerAgentGroup srcCag, InConsumerAgentGroup tarCag, double value) {
        this._name = ParamUtil.conc(srcCag, tarCag);
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

    public String getSrcCagName() throws ParsingException {
        return ParamUtil.firstPart(getName());
    }

    public String getTarCagName() throws ParsingException {
        return ParamUtil.secondPart(getName());
    }

    @Override
    public Object parse(InputParser parser) throws ParsingException {
        return this;
    }
}
