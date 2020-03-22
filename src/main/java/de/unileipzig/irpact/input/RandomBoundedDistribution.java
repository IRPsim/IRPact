package de.unileipzig.irpact.input;

class RandomBoundedDistribution implements Distribution {

    ///description: Name der Verteilungsfunktion
    ///type: String
    ///identifier:
    ///unit:
    ///domain:
    ///validation:
    ///hidden:
    ///processing:
    String name;

    ///description: Typ der genutzten Verteilungsfunktion
    ///type: String
    ///identifier:
    ///unit:
    ///domain:
    ///validation:
    ///hidden:
    ///processing:
    String type;

    ///description: Typ der genutzten Verteilungsfunktion
    ///type: long
    ///identifier:
    ///unit:
    ///domain:
    ///default: -1
    ///validation:
    ///hidden:
    ///processing:
    long seed;

    ///description: Unterer Grenzwert
    ///type: double
    ///identifier:
    ///unit:
    ///domain:
    ///validation:
    ///hidden:
    ///processing:
    double lowerBound;

    ///description: Oberer Grenzwert
    ///type: double
    ///identifier:
    ///unit:
    ///domain:
    ///validation:
    ///hidden:
    ///processing:
    double upperBound;
}
