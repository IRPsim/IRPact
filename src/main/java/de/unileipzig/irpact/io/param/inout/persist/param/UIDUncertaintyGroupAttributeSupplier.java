package de.unileipzig.irpact.io.param.inout.persist.param;

import de.unileipzig.irpact.core.process.ra.attributes.BasicUncertaintyGroupAttributeSupplier;
import de.unileipzig.irpact.io.param.ParamUtil;
import de.unileipzig.irptools.defstructure.annotation.Definition;
import de.unileipzig.irptools.defstructure.annotation.FieldDefinition;

/**
 * @author Daniel Abitz
 */
@Definition
public class UIDUncertaintyGroupAttributeSupplier {

    public String _name;

    @FieldDefinition
    public UIDUncertaintyGroupAttributeSupplierEntry[] entries = new UIDUncertaintyGroupAttributeSupplierEntry[0];

    public UIDUncertaintyGroupAttributeSupplier() {
    }

    public String getName() {
        return _name;
    }

    public UIDUncertaintyGroupAttributeSupplierEntry[] getEntries() {
        return entries;
    }

    private void createX() {
        BasicUncertaintyGroupAttributeSupplier object = new BasicUncertaintyGroupAttributeSupplier();
        int len = ParamUtil.len(getEntries());
        if(len > 0) {
            for(UIDUncertaintyGroupAttributeSupplierEntry entry: getEntries()) {
                //castCag
                //castUnc
                //castConv
            }
        }
    }
}
