package de.unileipzig.irpact.start.hardcodeddemo.def.out;

import de.unileipzig.irpact.commons.CollectionUtil;
import de.unileipzig.irpact.start.hardcodeddemo.def.in.AgentGroup;
import de.unileipzig.irpact.start.hardcodeddemo.def.in.Product;
import de.unileipzig.irptools.defstructure.annotation.Definition;
import de.unileipzig.irptools.defstructure.annotation.FieldDefinition;

import java.util.List;

/**
 * @author Daniel Abitz
 */
@Definition(
        root = true
)
public class GlobalRoot {

    public static final List<Class<?>> CLASSES = CollectionUtil.arrayListOf(
            GlobalRoot.class,
            GlobalScalars.class,
            AgentGroup.class,
            Product.class
    );

    @FieldDefinition
    public GlobalScalars scalars;

    @FieldDefinition
    public AgentGroup[] agentGroups;

    @FieldDefinition
    public Product[] products;

    public GlobalRoot() {
    }
}
