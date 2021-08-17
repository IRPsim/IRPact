package de.unileipzig.irpact.core.spatial.twodim;

import de.unileipzig.irpact.commons.Nameable;

/**
 * @author Daniel Abitz
 */
public interface Point2DFactory extends Nameable {

    Point2D createNext();
}
