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
public class InOrientationSupplier implements InEntity {

    public static void initRes(TreeAnnotationResource res) {
    }
    public static void applyRes(TreeAnnotationResource res) {
        res.putPath(
                InOrientationSupplier.class,
                res.getCachedElement("Prozessmodell"),
                res.getCachedElement("Relative Agreement"),
                res.getCachedElement("Datenerweiterung"),
                res.getCachedElement("Orientierung")
        );

        res.newEntryBuilder()
                .setGamsIdentifier("Verteilungsfunktion für die Orientierung")
                .setGamsDescription("Verteilungsfunktion")
                .store(InOrientationSupplier.class, "distInOrientation");

        res.newEntryBuilder()
                .setGamsIdentifier("Ziel-KGs für Orientierung")
                .setGamsDescription("Gruppen, denen die Werte hinzugefügt werden sollen.")
                .store(InOrientationSupplier.class, "oriCags");
    }

    public String _name;

    @FieldDefinition
    public InUnivariateDoubleDistribution distInOrientation;

    @FieldDefinition
    public InConsumerAgentGroup[] oriCags;

    public InOrientationSupplier() {
    }

    public InOrientationSupplier(String name, InUnivariateDoubleDistribution distribution) {
        this._name = name;
        this.distInOrientation = distribution;
    }

    @Override
    public String getName() {
        return _name;
    }

    public InUnivariateDoubleDistribution getDistribution() {
        return distInOrientation;
    }

    public InConsumerAgentGroup[] getConsumerAgentGroups() {
        return oriCags;
    }

    @Override
    public void setup(InputParser parser, Object input) throws ParsingException {
        RAProcessModel model = (RAProcessModel) input;

        UnivariateDoubleDistribution dist = parser.parseEntityTo(getDistribution());
        for(InConsumerAgentGroup inCag: getConsumerAgentGroups()) {
            ConsumerAgentGroup cag = parser.parseEntityTo(inCag);
            model.getOrientationSupplier().put(cag, dist);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof InOrientationSupplier)) return false;
        InOrientationSupplier that = (InOrientationSupplier) o;
        return Objects.equals(_name, that._name) && Objects.equals(distInOrientation, that.distInOrientation);
    }

    @Override
    public int hashCode() {
        return Objects.hash(_name, distInOrientation);
    }

    @Override
    public String toString() {
        return "InOrientationSupplier{" +
                "_name='" + _name + '\'' +
                ", distInOrientation=" + distInOrientation +
                '}';
    }
}
