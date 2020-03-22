package de.unileipzig.irpact.input;

class ConstantDistribution implements Distribution {

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

    ///description: Wert, welchen die Funktion zurück geben soll
    ///type: double
    ///identifier:
    ///unit:
    ///domain:
    ///validation:
    ///hidden:
    ///processing:
    double value;
}
