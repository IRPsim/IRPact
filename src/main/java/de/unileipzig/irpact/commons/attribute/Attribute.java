package de.unileipzig.irpact.commons.attribute;

import de.unileipzig.irpact.commons.util.DataType;
import de.unileipzig.irpact.util.Todo;

/**
 * @author Daniel Abitz
 */
@Todo("generischen typ entfernen, typ etc aber beibehalten")
@Todo("Produktspezifische Attribute einbauen -> ProductGroupSpecificAttributes -> get(String Name): ?Attribut?")
public interface Attribute<T> extends AttributeBase {

    default Attribute<T> copyAttribute() {
        throw new UnsupportedOperationException();
    }

    T getValue();

    void setValue(T value);

    DataType getType();

    default <R> R as(Class<R> c) {
        return c.cast(this);
    }
}
