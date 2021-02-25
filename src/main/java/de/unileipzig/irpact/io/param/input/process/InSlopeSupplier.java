package de.unileipzig.irpact.io.param.input.process;

import de.unileipzig.irpact.commons.distribution.UnivariateDoubleDistribution;
import de.unileipzig.irpact.commons.exception.ParsingException;
import de.unileipzig.irpact.core.agent.consumer.ConsumerAgentGroup;
import de.unileipzig.irpact.core.process.ra.RAProcessModel;
import de.unileipzig.irpact.io.param.input.InEntity;
import de.unileipzig.irpact.io.param.input.InputParser;
import de.unileipzig.irpact.io.param.input.agent.consumer.InConsumerAgentGroup;
import de.unileipzig.irpact.io.param.input.distribution.InUnivariateDoubleDistribution;
import de.unileipzig.irptools.defstructure.annotation.Definition;
import de.unileipzig.irptools.defstructure.annotation.FieldDefinition;
import de.unileipzig.irptools.util.TreeAnnotationResource;

import java.util.Objects;

/**
 * @author Daniel Abitz
 */
@Definition
public class InSlopeSupplier implements InEntity {

    public static void initRes(TreeAnnotationResource res) {
    }
    public static void applyRes(TreeAnnotationResource res) {
        res.putPath(
                InSlopeSupplier.class,
                res.getCachedElement("Prozessmodell"),
                res.getCachedElement("Relative Agreement"),
                res.getCachedElement("Datenerweiterung"),
                res.getCachedElement("Neigung")
        );

        res.newEntryBuilder()
                .setGamsIdentifier("Verteilungsfunktion für die Neigung")
                .setGamsDescription("Verteilungsfunktion")
                .store(InSlopeSupplier.class, "distSlope");

        res.newEntryBuilder()
                .setGamsIdentifier("Ziel-KGs für Neigung")
                .setGamsDescription("Gruppen, denen die Werte hinzugefügt werden sollen.")
                .store(InSlopeSupplier.class, "slopeCags");
    }

    public String _name;

    @FieldDefinition
    public InUnivariateDoubleDistribution distSlope;

    @FieldDefinition
    public InConsumerAgentGroup[] slopeCags;

    public InSlopeSupplier() {
    }

    public InSlopeSupplier(String name, InUnivariateDoubleDistribution distribution) {
        this._name = name;
        this.distSlope = distribution;
    }

    @Override
    public String getName() {
        return _name;
    }

    public InUnivariateDoubleDistribution getDistribution() {
        return distSlope;
    }

    public InConsumerAgentGroup[] getConsumerAgentGroups() {
        return slopeCags;
    }

    @Override
    public void setup(InputParser parser, Object input) throws ParsingException {
        RAProcessModel model = (RAProcessModel) input;

        UnivariateDoubleDistribution dist = parser.parseEntityTo(getDistribution());
        for(InConsumerAgentGroup inCag: getConsumerAgentGroups()) {
            ConsumerAgentGroup cag = parser.parseEntityTo(inCag);
            model.getSlopeSupplier().put(cag, dist);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof InSlopeSupplier)) return false;
        InSlopeSupplier that = (InSlopeSupplier) o;
        return Objects.equals(_name, that._name) && Objects.equals(distSlope, that.distSlope);
    }

    @Override
    public int hashCode() {
        return Objects.hash(_name, distSlope);
    }

    @Override
    public String toString() {
        return "InSlopeSupplier{" +
                "_name='" + _name + '\'' +
                ", distSlope=" + distSlope +
                '}';
    }
}
