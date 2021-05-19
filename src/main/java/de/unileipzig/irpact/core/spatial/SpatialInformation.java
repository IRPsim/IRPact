package de.unileipzig.irpact.core.spatial;

import de.unileipzig.irpact.commons.checksum.ChecksumComparable;
import de.unileipzig.irpact.commons.attribute.AttributeAccess;
import de.unileipzig.irpact.commons.attribute.AttributeType;
import de.unileipzig.irpact.commons.spatial.attribute.SpatialAttribute;
import de.unileipzig.irpact.core.process.ra.RAConstants;

import java.util.Collection;

/**
 * @author Daniel Abitz
 */
public interface SpatialInformation extends ChecksumComparable {

    static int tryGetId(SpatialInformation information) {
        SpatialAttribute attr = information.getAttribute(RAConstants.ID);
        if(attr != null && attr.isType(AttributeType.VALUE)) {
            return attr.asValueAttribute().getIntValue();
        } else {
            return -1;
        }
    }

    boolean hasId();

    boolean isId(int id);

    default boolean hasSameId(SpatialInformation other) {
        return getId() == other.getId();
    }

    int getId();

    AttributeAccess getAttributeAccess();

    Collection<SpatialAttribute> getAttributes();

    SpatialAttribute getAttribute(String name);

    boolean hasAttribute(String name);

    void addAttribute(SpatialAttribute attribute);

    SpatialInformation emptyCopy();

    SpatialInformation fullCopy();

    boolean isEquals(SpatialInformation other);
}
