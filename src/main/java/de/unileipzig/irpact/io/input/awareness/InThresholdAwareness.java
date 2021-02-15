package de.unileipzig.irpact.io.input.awareness;

import de.unileipzig.irpact.commons.awareness.Awareness;
import de.unileipzig.irpact.commons.awareness.ThresholdAwareness;
import de.unileipzig.irptools.defstructure.annotation.Definition;
import de.unileipzig.irptools.defstructure.annotation.FieldDefinition;
import de.unileipzig.irptools.util.TreeAnnotationResource;

import java.util.Objects;

/**
 * @author Daniel Abitz
 */
@Definition
public class InThresholdAwareness implements InAwareness {

    public static void initRes(TreeAnnotationResource res) {
        res.newElementBuilder()
                .setEdnLabel("Threshold")
                .setEdnPriority(0)
                .putCache("Threshold");
    }
    public static void applyRes(TreeAnnotationResource res) {
        res.putPath(
                InThresholdAwareness.class,
                res.getCachedElement("Agenten"),
                res.getCachedElement("Konsumer"),
                res.getCachedElement("Awareness"),
                res.getCachedElement("Threshold")
        );
    }

    public String _name;

    @FieldDefinition
    public double awarenessThreshold;

    public InThresholdAwareness() {
    }

    public InThresholdAwareness(String name, double threshold) {
        this._name = name;
        this.awarenessThreshold = threshold;
    }

    @Override
    public String getName() {
        return _name;
    }

    public double getAwarenessThreshold() {
        return awarenessThreshold;
    }

    @Override
    public <T> Awareness<T> createInstance() {
        ThresholdAwareness<T> ta = new ThresholdAwareness<>();
        ta.setThreshold(awarenessThreshold);
        return ta;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof InThresholdAwareness)) return false;
        InThresholdAwareness that = (InThresholdAwareness) o;
        return Double.compare(that.awarenessThreshold, awarenessThreshold) == 0 && Objects.equals(_name, that._name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(_name, awarenessThreshold);
    }
}
