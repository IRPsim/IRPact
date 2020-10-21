package de.unileipzig.irpact.v2.io.input.affinity;

import de.unileipzig.irpact.v2.def.ToDo;
import de.unileipzig.irptools.defstructure.annotation.Definition;
import de.unileipzig.irptools.defstructure.annotation.Edn;
import de.unileipzig.irptools.defstructure.annotation.FieldDefinition;

/**
 * @author Daniel Abitz
 */
@Definition(
        edn = @Edn(
                path = "Agents/Consumer/Groups/Affinity/Entry"
        )
)
public class IBasicAffinitiesEntry {

    public String _name;

    @ToDo("mit ConsumerAgentGroup ersetzen")
    @FieldDefinition
    public String fromGroup;

    @ToDo("mit ConsumerAgentGroup ersetzen")
    @FieldDefinition
    public String toGroup;

    @FieldDefinition
    public double value;

    public IBasicAffinitiesEntry() {
    }
}
