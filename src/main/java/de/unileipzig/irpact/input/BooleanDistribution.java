package de.unileipzig.irpact.input;

class BooleanDistribution implements Distribution {

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

    ///description: Genutzte Schwellwert
    ///type: double
    ///identifier:
    ///unit:
    ///domain:
    ///default: -1
    ///validation:
    ///hidden:
    ///processing:
    double threshold;
}
