* - description: AgentGroup
* - type: String
* - identifier: AgentGroup
SET set_AgentGroup(*)

* - description: numberOfAgents
* - type: Integer
* - identifier: numberOfAgents
PARAMETER par_AgentGroup_numberOfAgents(set_AgentGroup)

* - description: adoptionRate
* - type: Float
* - identifier: adoptionRate
PARAMETER par_AgentGroup_adoptionRate(set_AgentGroup)

* - description: seedAgentGroup
* - type: Integer
* - identifier: seedAgentGroup
PARAMETER par_AgentGroup_seed(set_AgentGroup)

* - description: needsAgentGroup
* - type: Boolean
* - identifier: needsAgentGroup
PARAMETER par_link_AgentGroup_Need_needs(set_AgentGroup,set_Need)

* - description: RandomBoundedDistribution
* - type: String
* - identifier: RandomBoundedDistribution
SET set_RandomBoundedDistribution(set_UnivariateDistribution)

* - description: seedRandomBoundedDistribution
* - type: Integer
* - identifier: seedRandomBoundedDistribution
PARAMETER par_RandomBoundedDistribution_seed(set_RandomBoundedDistribution)

* - description: lowerBound
* - type: Float
* - identifier: lowerBound
PARAMETER par_RandomBoundedDistribution_lowerBound(set_RandomBoundedDistribution)

* - description: upperBound
* - type: Float
* - identifier: upperBound
PARAMETER par_RandomBoundedDistribution_upperBound(set_RandomBoundedDistribution)

* - description: RandomDistribution
* - type: String
* - identifier: RandomDistribution
SET set_RandomDistribution(set_UnivariateDistribution)

* - description: seedRandomDistribution
* - type: Integer
* - identifier: seedRandomDistribution
PARAMETER par_RandomDistribution_seed(set_RandomDistribution)

* - description: UnivariateDistribution
* - type: String
* - identifier: UnivariateDistribution
SET set_UnivariateDistribution(*)

* - description: Need
* - type: String
* - identifier: Need
SET set_Need(*)

* - description: FixedProduct
* - type: String
* - identifier: FixedProduct
SET set_FixedProduct(*)

* - description: groupFixedProduct
* - type: Boolean
* - identifier: groupFixedProduct
PARAMETER par_link_FixedProduct_ProductGroup_group(set_FixedProduct,set_ProductGroup)

* - description: specifiedAttributes
* - type: Float
* - identifier: specifiedAttributes
PARAMETER par_map_FixedProduct_ProductGroupAttribute_specifiedAttributes(set_FixedProduct,set_ProductGroupAttribute)

* - description: ProductGroup
* - type: String
* - identifier: ProductGroup
SET set_ProductGroup(*)

* - description: attributesProductGroup
* - type: Boolean
* - identifier: attributesProductGroup
PARAMETER par_link_ProductGroup_ProductGroupAttribute_attributes(set_ProductGroup,set_ProductGroupAttribute)

* - description: needsSatisfied
* - type: Boolean
* - identifier: needsSatisfied
PARAMETER par_link_ProductGroup_Need_needsSatisfied(set_ProductGroup,set_Need)

* - description: ProductGroupAttribute
* - type: String
* - identifier: ProductGroupAttribute
SET set_ProductGroupAttribute(*)

* - description: distributionProductGroupAttribute
* - type: Boolean
* - identifier: distributionProductGroupAttribute
PARAMETER par_link_ProductGroupAttribute_UnivariateDistribution_distribution(set_ProductGroupAttribute,set_UnivariateDistribution)

* - description: AdaptionRate
* - type: String
* - identifier: AdaptionRate
SET set_AdaptionRate(*)

* - description: groupAdaptionRate
* - type: Boolean
* - identifier: groupAdaptionRate
PARAMETER par_out_link_AdaptionRate_AgentGroup_group(set_AdaptionRate,set_AgentGroup)

* - description: needAdaptionRate
* - type: Boolean
* - identifier: needAdaptionRate
PARAMETER par_out_link_AdaptionRate_Need_need(set_AdaptionRate,set_Need)

* - description: rate
* - type: Float
* - identifier: rate
PARAMETER par_out_AdaptionRate_rate(set_AdaptionRate)
