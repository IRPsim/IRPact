package de.unileipzig.irpact.experimental.deprecated.input.affinity;

import de.unileipzig.irptools.defstructure.annotation.Definition;
import de.unileipzig.irptools.defstructure.annotation.Edn;
import de.unileipzig.irptools.defstructure.annotation.FieldDefinition;

/**
 * @author Daniel Abitz
 */
@Definition(
        edn = @Edn(
                label = {"Agents", "Consumer", "Groups", "Affinity"}
        )
)
public class IBasicAffinityMapping {

    public String _name;

    @FieldDefinition
    public IBasicAffinitiesEntry[] affinityEntries;

    public IBasicAffinityMapping() {
    }
}
