* - description: Seed für den Zufallsgenerator
* - type: Integer
* - identifier: Zufallswert
* - unit: [x]
SCALAR sca_global_seed

* - description: Baumwert für den Zufallsgenerator
* - type: Integer
* - identifier: Baumwert
* - unit: [x]
SCALAR sca_global_baum

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
