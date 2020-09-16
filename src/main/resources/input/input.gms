* - description: Seed für den Zufallsgenerator
* - identifier: Zufallswert
* - type: Integer
* - unit: [x]
SCALAR sca_global_seed

* - description: AgentGroup
* - identifier: AgentGroup
* - type: String
SET set_AgentGroup(*)

* - default: 10
* - domain: [1,]
* - description: Anzahl der Agenten in der Gruppe
* - identifier: Agentenanzahl
* - type: Integer
PARAMETER par_AgentGroup_numberOfAgents(set_AgentGroup)

* - default: 0.5
* - domain: [0,1]
* - description: Adaptionsrate für die Agenten dieser Gruppe
* - identifier: Adaptionsrate
* - type: Float
PARAMETER par_AgentGroup_adaptionRate(set_AgentGroup)

* - description: Product
* - identifier: Product
* - type: String
SET set_Product(*)

* - description: Platzhalter
* - identifier: Productplaceholder
* - type: Integer
PARAMETER par_Product_placeholder(set_Product)

* - description: Distribution
* - hidden: 1
* - identifier: Distribution
* - type: String
SET set_Distribution(*)

* - description: Distribution1
* - identifier: Distribution1
* - type: String
SET set_Distribution1(set_Distribution)

* - description: value
* - identifier: value
* - type: Float
PARAMETER par_Distribution1_value(set_Distribution1)

* - description: Distribution2
* - identifier: Distribution2
* - type: String
SET set_Distribution2(set_Distribution)

* - description: value1
* - identifier: value1
* - type: Float
PARAMETER par_Distribution2_value1(set_Distribution2)

* - description: value2
* - identifier: value2
* - type: Float
PARAMETER par_Distribution2_value2(set_Distribution2)

* - description: Complex
* - identifier: Complex
* - type: String
SET set_Complex(*)

* - description: structEntry_x
* - identifier: structEntry_x
* - type: Float
PARAMETER par_Complex_structEntry_x(set_Complex)

* - description: structEntry_y
* - identifier: structEntry_y
* - type: Float
PARAMETER par_Complex_structEntry_y(set_Complex)

* - description: structEntry_z
* - identifier: structEntry_z
* - type: Float
PARAMETER par_Complex_structEntry_z(set_Complex)

* - description: structEntry_deeperStruct_a
* - identifier: structEntry_deeperStruct_a
* - type: Float
PARAMETER par_Complex_structEntry_deeperStruct_a(set_Complex)

* - description: structEntry_deeperStruct_b
* - identifier: structEntry_deeperStruct_b
* - type: Float
PARAMETER par_Complex_structEntry_deeperStruct_b(set_Complex)

* - description: structEntry_deeperStruct_c
* - identifier: structEntry_deeperStruct_c
* - type: Float
PARAMETER par_Complex_structEntry_deeperStruct_c(set_Complex)

* - description: distributionv1
* - identifier: distributionv1
* - type: Boolean
PARAMETER par_link_Complex_Distribution_distributionv1(set_Complex,set_Distribution)

* - description: distributionv2
* - identifier: distributionv2
* - type: Boolean
PARAMETER par_link_Complex_Distribution_distributionv2(set_Complex,set_Distribution)

