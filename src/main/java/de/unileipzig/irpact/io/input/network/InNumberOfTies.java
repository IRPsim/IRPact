package de.unileipzig.irpact.io.input.network;

import de.unileipzig.irpact.io.input.agent.consumer.InConsumerAgentGroup;
import de.unileipzig.irptools.defstructure.annotation.Definition;
import de.unileipzig.irptools.defstructure.annotation.FieldDefinition;
import de.unileipzig.irptools.util.TreeAnnotationResource;

import java.util.Objects;

/**
 * @author Daniel Abitz
 */
@Definition
public class InNumberOfTies {

    public static void initRes(TreeAnnotationResource res) {
        res.newElementBuilder()
                .setEdnLabel("Anzahl Kanten")
                .setEdnPriority(0)
                .putCache("Anzahl Kanten");
    }
    public static void applyRes(TreeAnnotationResource res) {
        res.putPath(
                InNumberOfTies.class,
                res.getCachedElement("Netzwerk"),
                res.getCachedElement("Topologie"),
                res.getCachedElement("Freie Topologie"),
                res.getCachedElement("Anzahl Kanten")
        );
    }

    public String _name;

    @FieldDefinition
    public InConsumerAgentGroup cag;

    @FieldDefinition
    public int count;

    public InNumberOfTies() {
    }

    public InNumberOfTies(String name, InConsumerAgentGroup cag, int count) {
        this._name = name;
        this.cag = cag;
        this.count = count;
    }

    public String getName() {
        return _name;
    }

    public InConsumerAgentGroup getCag() {
        return cag;
    }

    public int getCount() {
        return count;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof InNumberOfTies)) return false;
        InNumberOfTies that = (InNumberOfTies) o;
        return count == that.count && Objects.equals(_name, that._name) && Objects.equals(cag, that.cag);
    }

    @Override
    public int hashCode() {
        return Objects.hash(_name, cag, count);
    }

    @Override
    public String toString() {
        return "InNumberOfTies{" +
                "_name='" + _name + '\'' +
                ", cag=" + cag +
                ", count=" + count +
                '}';
    }
}
