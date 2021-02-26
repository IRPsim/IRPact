package de.unileipzig.irpact.io.param.input.affinity;

import de.unileipzig.irpact.commons.exception.ParsingException;
import de.unileipzig.irpact.io.param.input.InUtil;
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
public class InComplexAffinityEntry implements InAffinityEntry {

    //damit ich bei copy&paste nie mehr vergesse die Klasse anzupassen :)
    private static final MethodHandles.Lookup L = MethodHandles.lookup();
    public static Class<?> thisClass() {
        return L.lookupClass();
    }

    public static void initRes(TreeAnnotationResource res) {
    }
    public static void applyRes(TreeAnnotationResource res) {
        res.putPath(
                thisClass(),
                res.getCachedElement("Agenten"),
                res.getCachedElement("Konsumer"),
                res.getCachedElement("Affinity")
        );

        res.newEntryBuilder()
                .setGamsIdentifier("Ausgangsgruppe")
                .setGamsDescription("Ausgangsgruppe")
                .store(thisClass(), "srcCag");
        res.newEntryBuilder()
                .setGamsIdentifier("Zielgruppe")
                .setGamsDescription("Zielgruppe")
                .store(thisClass(), "tarCag");
        res.newEntryBuilder()
                .setGamsIdentifier("Affinity-Wert")
                .setGamsDescription("Affinity-Wert")
                .store(thisClass(), "affinityValue");
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

    @Override
    public String getName() {
        return _name;
    }

    public void setSrcCag(InConsumerAgentGroup srcCag) {
        this.srcCag = new InConsumerAgentGroup[]{srcCag};
    }

    public InConsumerAgentGroup getSrcCag() throws ParsingException {
        return InUtil.getInstance(srcCag, "Source-ConsumerAgentGroup");
    }

    @Override
    public InConsumerAgentGroup getSrcCag(InputParser parser) throws ParsingException {
        return getSrcCag();
    }

    public void setTarCag(InConsumerAgentGroup tarCag) {
        this.tarCag = new InConsumerAgentGroup[]{tarCag};
    }

    public InConsumerAgentGroup getTarCag() throws ParsingException {
        return InUtil.getInstance(tarCag, "Target-ConsumerAgentGroup");
    }

    @Override
    public InConsumerAgentGroup getTarCag(InputParser parser) throws ParsingException {
        return getTarCag();
    }

    public void setAffinityValue(double affinityValue) {
        this.affinityValue = affinityValue;
    }

    @Override
    public double getAffinityValue() {
        return affinityValue;
    }

    @Override
    public Object parse(InputParser parser) throws ParsingException {
        return this;
    }
}
