package de.unileipzig.irpact.input;

class SchemaWith2Parameters implements Schema {

    ///description: Name des Schemas
    ///type: String
    ///identifier:
    ///unit:
    ///domain:
    ///validation:
    ///hidden:
    ///processing:
    String name;

    ///description: Typ des genutzten Schemas
    ///type: String
    ///identifier:
    ///unit:
    ///domain:
    ///validation:
    ///hidden:
    ///processing:
    String type;

    ///description: Erste Eingabeparameter
    ///type: long
    ///identifier:
    ///unit:
    ///domain:
    ///validation:
    ///hidden:
    ///processing:
    long value0;

    ///description: Zweite Eingabeparameter
    ///type: long
    ///identifier:
    ///unit:
    ///domain:
    ///validation:
    ///hidden:
    ///processing:
    long value1;
}
