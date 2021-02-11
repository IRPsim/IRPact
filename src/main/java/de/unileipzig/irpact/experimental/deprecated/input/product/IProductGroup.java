package de.unileipzig.irpact.experimental.deprecated.input.product;

import de.unileipzig.irptools.defstructure.annotation.*;

/**
 * @author Daniel Abitz
 */
@Definition(
        edn = @Edn(
                label = {"Products/Groups"}
        )
)
public class IProductGroup {

    public String _name;

    @FieldDefinition(
            edn = @EdnParameter(
                    label = {"Products/Group-Attribute-Mapping"}
            )
    )
    public IProductGroupAttribute[] pgAttributes;

    public IProductGroup() {
    }

    public IProductGroup(String name, IProductGroupAttribute[] pgAttributes) {
        this._name = name;
        this.pgAttributes = pgAttributes;
    }

    public String getName() {
        return _name;
    }

    public IProductGroupAttribute[] getPgAttributes() {
        return pgAttributes;
    }
}
