* - description: seed
* - type: Integer
* - identifier: seed
* - unit:
* - domain:
* - default:
* - validation:
* - hidden: 0
* - processing:
* - rule:
SCALAR sca_seed

* - description: AgentGroup
* - type: String
* - identifier: AgentGroup
* - default:
* - hidden: 0
* - icon:
* - fill:
* - border:
* - shape:
SET set_AgentGroup(*)

* - description: numberOfAgents
* - type: Integer
* - identifier: numberOfAgents
* - unit:
* - domain: [1,]
* - default: 10
* - validation:
* - hidden: 0
* - processing:
* - rule:
PARAMETER par_AgentGroup_numberOfAgents(set_AgentGroup)

* - description: adaptionRate
* - type: Float
* - identifier: adaptionRate
* - unit:
* - domain: [0,1]
* - default: 0.5
* - validation:
* - hidden: 0
* - processing:
* - rule:
PARAMETER par_AgentGroup_adaptionRate(set_AgentGroup)

* - description: Product
* - type: String
* - identifier: Product
* - default:
* - hidden: 0
* - icon:
* - fill:
* - border:
* - shape:
SET set_Product(*)
