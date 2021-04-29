package de.unileipzig.irpact.core.product;

import de.unileipzig.irpact.core.simulation.SimulationEntity;
import de.unileipzig.irpact.develop.Todo;

import java.util.Collection;

/**
 * @author Daniel Abitz
 */
@Todo("Produktspezifische Attribute besser implementieren")
/*
Grobe Idee: Mapping zwischen den Attributen und einem Produkt
    -> generische Attribute (DoubleAttribute)

    - ProductRelatedAttribute<?> extends Attribute<?>
        -> return
        -> CAG__PG__name

 */
public interface Product extends SimulationEntity {

    ProductGroup getGroup();

    Collection<ProductAttribute> getAttributes();

    boolean hasAttribute(String name);

    ProductAttribute getAttribute(String name);

    boolean isFixed();

    boolean isNotFixed();
}
