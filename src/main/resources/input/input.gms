* - type: Integer
* - identifier: seed
SCALAR sca_seed

* - identifier: AgentGroup
* - type: String
SET set_AgentGroup(*)

* - domain: [1,]
* - default: 10
* - type: Integer
* - identifier: numberOfAgents
PARAMETER par_AgentGroup_numberOfAgents(set_AgentGroup)

* - domain: [0,1]
* - default: 0.5
* - type: Float
* - identifier: adaptionRate
PARAMETER par_AgentGroup_adaptionRate(set_AgentGroup)

* - identifier: Product
* - type: String
SET set_Product(*)
