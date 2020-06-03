* - description: seed
* - type: Integer
* - identifier: seed
* - unit: [x]
* - hidden: 0
SCALAR sca_seed

* - description: seed_two
* - type: Integer
* - identifier: seed_two
* - unit: [x]
* - hidden: 0
SCALAR sca_seed_two

* - description: AgentGroup
* - type: String
* - identifier: AgentGroup
* - hidden: 0
SET set_AgentGroup(*)

* - description: numberOfAgents
* - type: Integer
* - identifier: numberOfAgents
* - domain: [1,]
* - default: 10
* - hidden: 0
PARAMETER par_AgentGroup_numberOfAgents(set_AgentGroup)

* - description: adaptionRate
* - type: Float
* - identifier: adaptionRate
* - domain: [0,1]
* - default: 0.5
* - hidden: 0
PARAMETER par_AgentGroup_adaptionRate(set_AgentGroup)

* - description: Product
* - type: String
* - identifier: Product
* - hidden: 0
SET set_Product(*)

* - description: irrelevant
* - type: Float
* - identifier: irrelevant
* - hidden: 0
PARAMETER par_Product_irrelevant(set_Product)
