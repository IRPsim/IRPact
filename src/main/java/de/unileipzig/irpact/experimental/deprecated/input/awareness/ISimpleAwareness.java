package de.unileipzig.irpact.experimental.deprecated.input.awareness;

import de.unileipzig.irpact.commons.awareness.SimpleAwareness;
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
public class ISimpleAwareness implements IAwareness {

    public String _name;

    @FieldDefinition
    public int placeholder;

    public ISimpleAwareness() {
    }

    public ISimpleAwareness(String name) {
        this._name = name;
    }

    @Override
    public <T> SimpleAwareness<T> createInstance() {
        return new SimpleAwareness<>();
    }
}
