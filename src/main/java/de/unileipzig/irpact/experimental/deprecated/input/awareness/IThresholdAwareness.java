package de.unileipzig.irpact.experimental.deprecated.input.awareness;

import de.unileipzig.irpact.commons.interest.ThresholdInterest;
import de.unileipzig.irpact.develop.TodoException;
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
    public <T> ThresholdInterest<T, ?> createInstance() {
//        ThresholdInterest<T> awa = new ThresholdInterest<>();
//        awa.setThreshold(awarenessThreshold);
//        return awa;
        throw new TodoException();
    }
}
