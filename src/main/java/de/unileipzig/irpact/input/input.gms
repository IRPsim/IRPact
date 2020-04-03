* - description: Gruppe von Konsumenten
* - type: String
* - identifier: Gruppe von Konsumenten
* - unit:
* - domain:
* - validation:
* - hidden:
* - processing:
SET set_ConsumerAgentGroup(*)

* - description: Informationskompetenz
* - type: float
* - identifier:
* - unit:
* - domain: [0,1]
* - validation:
* - hidden:
* - processing:
PARAMETER par_ConsumerAgentGroup_informationAuthority(set_ConsumerAgentGroup)

* - description: Attributgruppenzugehörigkeit
* - type: Boolean
* - identifier: Attributgruppenzugehörigkeit
* - unit:
* - domain:
* - validation:
* - hidden:
* - processing:
PARAMETER par_ConsumerAgentGroup_ConsumerAgentGroupAttribute(set_ConsumerAgentGroup,set_ConsumerAgentGroupAttribute)

* - description: Gruppe von Konsumentenattributen
* - type: String
* - identifier: Gruppe von Konsumentenattributen
* - unit:
* - domain:
* - validation:
* - hidden:
* - processing:
SET set_ConsumerAgentGroupAttribute(*)

* - description: Testwert 1
* - type: float
* - identifier: Testwert 1
* - unit:
* - domain: [0,1]
* - validation:
* - hidden:
* - processing:
PARAMETER par_ConsumerAgentGroupAttribute_value0(set_ConsumerAgentGroupAttribute)

* - description: Testwert 2
* - type: float
* - identifier: Testwert 2
* - unit:
* - domain: [10,20]
* - validation:
* - hidden:
* - processing:
PARAMETER par_ConsumerAgentGroupAttribute_value1(set_ConsumerAgentGroupAttribute)

* - description: Multiplier
* - type: float
* - identifier: Multiplier
* - unit:
* - domain:
* - validation:
* - hidden:
* - processing:
SCALAR sca_multiplier
