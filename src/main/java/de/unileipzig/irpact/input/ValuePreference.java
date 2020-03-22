package de.unileipzig.irpact.input;

class ValuePreference {

    ///description: Name der Attributes
    ///type: String
    ///identifier:
    ///unit:
    ///domain:
    ///validation:
    ///hidden:
    ///processing:
    String name;

    ///description: Genutzte Verteilungsfunktion
    ///type: Distribution
    ///identifier:
    ///unit:
    ///domain: [BooleanDistribution|ConstantDistribution|RandomBoundedDistribution]
    ///validation:
    ///hidden:
    ///processing:
    Distribution distribution;
}
