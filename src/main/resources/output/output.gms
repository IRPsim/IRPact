* - description: AgentGroup
* - type: String
* - identifier: AgentGroup
SET set_AgentGroup(*)

* - description: Anzahl der Agenten in der Gruppe
* - type: Integer
* - identifier: Agentenanzahl
* - domain: [1,]
* - default: 10
PARAMETER par_AgentGroup_numberOfAgents(set_AgentGroup)

* - description: Adaptionsrate für die Agenten dieser Gruppe
* - type: Float
* - identifier: Adaptionsrate
* - domain: [0,1]
* - default: 0.5
PARAMETER par_AgentGroup_adaptionRate(set_AgentGroup)

* - description: Product
* - type: String
* - identifier: Product
SET set_Product(*)

* - description: Name des Produktes
* - type: Integer
* - identifier: Produktname
PARAMETER par_Product_name(set_Product)

* - description: Anzahl der Adaptionen der Gruppe je Produkt im Jahr
* - type: Float
* - identifier: Produktadaptionen
PARAMETER par_out_table_AgentGroup_Product_adaptions(set_AgentGroup,set_Product)
