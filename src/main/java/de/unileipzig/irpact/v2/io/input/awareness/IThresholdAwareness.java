package de.unileipzig.irpact.v2.io.input.awareness;

import de.unileipzig.irpact.v2.commons.awareness.Awareness;
import de.unileipzig.irpact.v2.commons.awareness.ThresholdAwareness;
import de.unileipzig.irptools.defstructure.annotation.Definition;
import de.unileipzig.irptools.defstructure.annotation.Edn;
import de.unileipzig.irptools.defstructure.annotation.FieldDefinition;

/**
 * @author Daniel Abitz
 */
@Definition(
        edn = @Edn(
                label = {"Agents/Consumer/Awareness/Simple"}
        )
)
public class IThresholdAwareness implements IAwareness {

    public String _name;

    @FieldDefinition
    public double awarenessThreshold;

    public IThresholdAwareness() {
    }

    public IThresholdAwareness(String name, double awarenessThreshold) {
        this._name = name;
        this.awarenessThreshold = awarenessThreshold;
    }

    @Override
    public <T> ThresholdAwareness<T> createInstance() {
        ThresholdAwareness<T> awa = new ThresholdAwareness<>();
        awa.setThreshold(awarenessThreshold);
        return awa;
    }
}
