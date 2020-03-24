package de.unileipzig.irpact.input.old.v2;

import de.unileipzig.irpact.commons.distribution.Distribution;

class ProductGroup {

    String name;

    ///domain: [Dist#1|Dist#2|Dist#3]
    ///reference: [BooleanDistribution|ConstantDistribution|RandomDistribution]
    Distribution distribution;
}
