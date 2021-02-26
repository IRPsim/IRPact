package de.unileipzig.irpact.io.param.input.network;

import de.unileipzig.irpact.commons.exception.ParsingException;
import de.unileipzig.irpact.io.param.input.InEntity;
import de.unileipzig.irpact.io.param.input.InUtil;
import de.unileipzig.irpact.io.param.input.InputParser;
import de.unileipzig.irpact.io.param.input.agent.consumer.InConsumerAgentGroup;
import de.unileipzig.irptools.defstructure.annotation.Definition;
import de.unileipzig.irptools.defstructure.annotation.FieldDefinition;
import de.unileipzig.irptools.util.TreeAnnotationResource;

import java.lang.invoke.MethodHandles;

/**
 * @author Daniel Abitz
 */
@Definition
public class InNumberOfTies implements InEntity {

    //damit ich bei copy&paste nie mehr vergesse die Klasse anzupassen :)
    private static final MethodHandles.Lookup L = MethodHandles.lookup();
    public static Class<?> thisClass() {
        return L.lookupClass();
    }

    public static void initRes(TreeAnnotationResource res) {
    }
    public static void applyRes(TreeAnnotationResource res) {
        res.putPath(
                InNumberOfTies.class,
                res.getCachedElement("Netzwerk"),
                res.getCachedElement("Topologie"),
                res.getCachedElement("Freie Topologie"),
                res.getCachedElement("Anzahl Kanten")
        );

        res.newEntryBuilder()
                .setGamsIdentifier("Zu nutzende Konsumergruppe")
                .setGamsDescription("Konsumergruppe")
                .store(InNumberOfTies.class, "cag");

        res.newEntryBuilder()
                .setGamsIdentifier("Anzahl Kanten")
                .setGamsDescription("Anzahl Kanten je Konsumergruppe")
                .store(InNumberOfTies.class, "count");
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
    public String getName() {
        return _name;
    }

    public InConsumerAgentGroup[] getConsumerAgentGroups() throws ParsingException {
        return InUtil.getArray(cags, "ConsumerAgentGroup");
    }

    public int getCount() {
        return count;
    }

    @Override
    public Object parse(InputParser parser) throws ParsingException {
        return this;
    }
}
