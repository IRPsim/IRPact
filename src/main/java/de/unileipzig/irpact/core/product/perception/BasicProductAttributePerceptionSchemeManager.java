package de.unileipzig.irpact.core.product.perception;

import de.unileipzig.irpact.core.product.ProductAttribute;
import de.unileipzig.irpact.core.product.ProductGroupAttribute;

import java.util.Map;

/**
 * @author Daniel Abitz
 * @implNote Statt auf die Attribute direkt, wird sich auf die Gruppe bezogen. Hintergrund ist die Tatsache,
 *           das sonst eine Synchronisation von dynamisch erzeugten Attributen stattfinden muss.
 *           Die Gruppen sind fix und direkt ueber jedes ProductAttribut direkt zu bekommen und eignen
 *           sich somit als Schluessel.
 */
public class BasicProductAttributePerceptionSchemeManager implements ProductAttributePerceptionSchemeManager {

    private Map<ProductGroupAttribute, ProductAttributePerceptionScheme> mapping;

    public BasicProductAttributePerceptionSchemeManager(
            Map<ProductGroupAttribute, ProductAttributePerceptionScheme> mapping) {
        this.mapping = mapping;
    }

    @Override
    public ProductAttributePerceptionScheme getScheme(ProductAttribute attribute) {
        ProductGroupAttribute group = attribute.getGroup();
        return mapping.get(group);
    }
}
