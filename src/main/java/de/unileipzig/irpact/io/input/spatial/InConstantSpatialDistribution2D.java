package de.unileipzig.irpact.io.input.spatial;

import de.unileipzig.irpact.core.spatial.ConstantSpatialDistribution;
import de.unileipzig.irpact.core.spatial.SpatialDistribution;
import de.unileipzig.irpact.core.spatial.twodim.BasicPoint2D;
import de.unileipzig.irpact.io.input.agent.consumer.InConsumerAgentGroup;
import de.unileipzig.irptools.defstructure.annotation.Definition;
import de.unileipzig.irptools.defstructure.annotation.FieldDefinition;
import de.unileipzig.irptools.util.TreeAnnotationResource;

import java.util.Objects;

/**
 * @author Daniel Abitz
 */
@Definition
public class InConstantSpatialDistribution2D implements InSpatialDistribution {

    public static void initRes(TreeAnnotationResource res) {
        res.newElementBuilder()
                .setEdnLabel("Verteilungsfunktion")
                .setEdnPriority(1)
                .putCache("Verteilungsfunktion");

        res.newElementBuilder()
                .setEdnLabel("Agenten-Mapping")
                .setEdnPriority(0)
                .putCache("Agenten-Mapping");
    }
    public static void applyRes(TreeAnnotationResource res) {
        res.putPath(
                InConstantSpatialDistribution2D.class,
                res.getCachedElement("Räumliche Modell"),
                res.getCachedElement("Verteilungsfunktion")
        );

        res.putPath(
                InConstantSpatialDistribution2D.class, "cagConstant",
                res.getCachedElement("Räumliche Modell"),
                res.getCachedElement("Verteilungsfunktion"),
                res.getCachedElement("Agenten-Mapping")
        );
    }

    public String _name;

    @FieldDefinition
    public InConsumerAgentGroup cagConstant;

    @FieldDefinition
    public double x;

    @FieldDefinition
    public double y;

    private ConstantSpatialDistribution instance;

    public InConstantSpatialDistribution2D() {
    }

    public InConstantSpatialDistribution2D(String name, InConsumerAgentGroup cag, double x, double y) {
        this._name = name;
        this.cagConstant = cag;
        this.x = x;
        this.y = y;
    }

    public String getName() {
        return _name;
    }

    public InConsumerAgentGroup getConsumerAgentGroup() {
        return cagConstant;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    @Override
    public SpatialDistribution getInstance() {
        if(instance == null) {
            instance = new ConstantSpatialDistribution();
            instance.setName(getName());
            instance.setInformation(new BasicPoint2D(getX(), getY()));
        }
        return instance;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof InConstantSpatialDistribution2D)) return false;
        InConstantSpatialDistribution2D that = (InConstantSpatialDistribution2D) o;
        return Double.compare(that.x, x) == 0 && Double.compare(that.y, y) == 0 && Objects.equals(_name, that._name) && Objects.equals(cagConstant, that.cagConstant);
    }

    @Override
    public int hashCode() {
        return Objects.hash(_name, cagConstant, x, y);
    }

    @Override
    public String toString() {
        return "InConstantSpatialDistribution2D{" +
                "_name='" + _name + '\'' +
                ", cagConstant=" + cagConstant +
                ", x=" + x +
                ", y=" + y +
                '}';
    }
}
