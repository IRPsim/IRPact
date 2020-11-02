package de.unileipzig.irpact.v2.io.input.product;

import de.unileipzig.irptools.defstructure.annotation.Definition;
import de.unileipzig.irptools.defstructure.annotation.Edn;
import de.unileipzig.irptools.defstructure.annotation.EdnParameter;
import de.unileipzig.irptools.defstructure.annotation.FieldDefinition;

/**
 * @author Daniel Abitz
 */
@Definition(
        edn = @Edn(
                path = {"Products/Groups"}
        )
)
public class IProductGroup {

    public String _name;

    @FieldDefinition(
            edn = @EdnParameter(
                    path = {"Products/Group-Attribute-Mapping"}
            )
    )
    public IProductGroupAttribute[] pgAttributes;

    public IProductGroup() {
    }

    public String getName() {
        return _name;
    }

    public IProductGroupAttribute[] getPgAttributes() {
        return pgAttributes;
    }
}
