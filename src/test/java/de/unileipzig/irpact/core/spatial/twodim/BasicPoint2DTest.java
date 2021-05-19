package de.unileipzig.irpact.core.spatial.twodim;

import de.unileipzig.irpact.commons.spatial.attribute.BasicSpatialDoubleAttribute;
import de.unileipzig.irpact.commons.spatial.attribute.BasicSpatialStringAttribute;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Daniel Abitz
 */
class BasicPoint2DTest {

    @Test
    void testEquals() {
        BasicPoint2D p0 = new BasicPoint2D(1, 2);
        BasicPoint2D p1 = new BasicPoint2D(1, 2);
        BasicPoint2D p2 = new BasicPoint2D(2, 1);
        BasicPoint2D p3 = new BasicPoint2D(1, 2);
        p3.setId(1);

        assertEquals(p0, p1);
        assertNotEquals(p0, p2);
        assertNotEquals(p0, p3);
    }

    @Test
    void testEqualsWithAttributes() {
        BasicPoint2D p0 = new BasicPoint2D(1, 2);
        p0.addAttribute(new BasicSpatialStringAttribute("x", "a"));
        p0.addAttribute(new BasicSpatialDoubleAttribute("y", 1));

        BasicPoint2D p1 = new BasicPoint2D(1, 2);
        p1.addAttribute(new BasicSpatialStringAttribute("x", "a"));
        p1.addAttribute(new BasicSpatialDoubleAttribute("y", 1));

        assertTrue(p0.isEquals(p1));
        assertTrue(p1.isEquals(p0));

        ((BasicSpatialDoubleAttribute) p0.getAttribute("y")).setDoubleValue(3);
        assertFalse(p0.isEquals(p1));
        assertFalse(p1.isEquals(p0));

        ((BasicSpatialDoubleAttribute) p1.getAttribute("y")).setDoubleValue(3);
        assertTrue(p0.isEquals(p1));
        assertTrue(p1.isEquals(p0));
    }
}