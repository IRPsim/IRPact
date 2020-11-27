package de.unileipzig.irpact.io.input.product;

import de.unileipzig.irptools.defstructure.annotation.Definition;
import de.unileipzig.irptools.defstructure.annotation.Edn;
import de.unileipzig.irptools.defstructure.annotation.EdnParameter;
import de.unileipzig.irptools.defstructure.annotation.FieldDefinition;

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
