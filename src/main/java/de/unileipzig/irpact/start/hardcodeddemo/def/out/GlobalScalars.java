package de.unileipzig.irpact.start.hardcodeddemo.def.out;

import de.unileipzig.irpact.start.hardcodeddemo.def.in.AgentGroup;
import de.unileipzig.irpact.start.hardcodeddemo.def.in.Product;
import de.unileipzig.irptools.defstructure.annotation.Definition;
import de.unileipzig.irptools.defstructure.annotation.EdnParameter;
import de.unileipzig.irptools.defstructure.annotation.FieldDefinition;
import de.unileipzig.irptools.defstructure.annotation.TableInfo;
import de.unileipzig.irptools.util.Table;

/**
 * @author Daniel Abitz
 */
@Definition(
        global = true
)
public class GlobalScalars {

    @FieldDefinition(
            edn = @EdnParameter(
                    path = "Results"
            )
    )
    @TableInfo(first = AgentGroup.class, second = Product.class, value = double.class)
    public Table<AgentGroup, Product, Double> adaptions;

    public GlobalScalars() {
    }

    public GlobalScalars(Table<AgentGroup, Product, Double> adaptions) {
        this.adaptions = adaptions;
    }

    public Table<AgentGroup, Product, Double> getAdaptions() {
        return adaptions;
    }

    @Override
    public String toString() {
        return "GlobalScalars{" +
                "adaptions=" + adaptions +
                '}';
    }
}
