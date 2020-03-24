package de.unileipzig.irpact.input;

class ConsumerAgentGroup {

    ///description: Name der Konsumentengruppe
    ///type: String
    ///identifier:
    ///unit:
    ///domain:
    ///validation:
    ///hidden:
    ///processing:
    String name;

    ///description: Informationskompetenz
    ///type: double
    ///identifier:
    ///unit:
    ///domain: [0,1]
    ///validation:
    ///hidden:
    ///processing:
    double informationAuthority;

    ///description: Attribute der Konsumentengruppe
    ///type: ConsumerAgentGroupAttribute
    ///identifier:
    ///unit:
    ///domain:
    ///validation:
    ///hidden:
    ///processing:
    ConsumerAgentGroupAttribute[] consumerAgentGroupAttributes;

    ///description: Präferenzen der Konsumentengruppe
    ///type: ValuePreference
    ///identifier:
    ///unit:
    ///domain:
    ///validation:
    ///hidden:
    ///processing:
    ValuePreference[] valuePreferences;


    ///description: Schema für die Suche und das Finden von Produkten
    ///type: ValuePreference
    ///identifier:
    ///unit:
    ///domain: [SchemaWithoutParameter|SchemaWith2Parameters]
    ///validation:
    ///hidden:
    ///processing:
    Schema productFindingScheme;


    ///description: Schema, welches die Entscheidungen beim Adoptionsprozess beschreibt
    ///type: ValuePreference
    ///identifier:
    ///unit:
    ///domain: [SchemaWithoutParameter|SchemaWith2Parameters]
    ///validation:
    ///hidden:
    ///processing:
    Schema productAdoptionDecisionScheme;
}
