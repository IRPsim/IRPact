* - description: seedtwo
* - type: Integer
* - identifier: seedtwo
* - unit: [x]
SCALAR sca_seed_two

* - description: AgentGroup
* - type: String
* - identifier: AgentGroup
SET set_AgentGroup(*)

* - description: numberOfAgents
* - type: Integer
* - identifier: numberOfAgents
* - domain: [1,]
* - default: 10
PARAMETER par_AgentGroup_numberOfAgents(set_AgentGroup)

* - description: adaptionRate
* - type: Float
* - identifier: adaptionRate
* - domain: [0,1]
* - default: 0.5
PARAMETER par_AgentGroup_adaptionRate(set_AgentGroup)

* - description: Product
* - type: String
* - identifier: Product
SET set_Product(*)

* - description: irrelevant
* - type: Float
* - identifier: irrelevant
PARAMETER par_Product_irrelevant(set_Product)
