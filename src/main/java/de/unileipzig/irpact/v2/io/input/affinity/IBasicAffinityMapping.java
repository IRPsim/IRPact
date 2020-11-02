package de.unileipzig.irpact.v2.io.input.affinity;

import de.unileipzig.irptools.defstructure.annotation.Definition;
import de.unileipzig.irptools.defstructure.annotation.Edn;
import de.unileipzig.irptools.defstructure.annotation.FieldDefinition;

/**
 * @author Daniel Abitz
 */
@Definition(
        edn = @Edn(
                path = {"Agents", "Consumer", "Groups", "Affinity"}
        )
)
public class IBasicAffinityMapping {

    public String _name;

    @FieldDefinition
    public IBasicAffinitiesEntry[] affinityEntries;

    public IBasicAffinityMapping() {
    }
}
