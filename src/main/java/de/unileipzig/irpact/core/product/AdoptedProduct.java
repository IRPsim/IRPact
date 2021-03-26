package de.unileipzig.irpact.core.product;

import de.unileipzig.irpact.commons.ChecksumComparable;
import de.unileipzig.irpact.commons.time.Timestamp;
import de.unileipzig.irpact.core.need.Need;

/**
 * Combines the adopted product with the satisfied need and the timestamp of the adoption.
 *
 * @author Daniel Abitz
 */
public interface AdoptedProduct extends ChecksumComparable {

    Need getNeed();

    Product getProduct();

    Timestamp getTimestamp();
}
