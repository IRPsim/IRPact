* - identifier: IBasicAffinitiesEntry
* - type: String
SET set_IBasicAffinitiesEntry(*)

* - identifier: from
* - type: Boolean
PARAMETER par_link_IBasicAffinitiesEntry_IConsumerAgentGroup_from(set_IBasicAffinitiesEntry,set_IConsumerAgentGroup)

* - identifier: to
* - type: Boolean
PARAMETER par_link_IBasicAffinitiesEntry_IConsumerAgentGroup_to(set_IBasicAffinitiesEntry,set_IConsumerAgentGroup)

* - identifier: value
* - type: Float
PARAMETER par_IBasicAffinitiesEntry_value(set_IBasicAffinitiesEntry)

* - identifier: IBasicAffinityMapping
* - type: String
SET set_IBasicAffinityMapping(*)

* - identifier: entries
* - type: Boolean
PARAMETER par_link_IBasicAffinityMapping_IBasicAffinitiesEntry_entries(set_IBasicAffinityMapping,set_IBasicAffinitiesEntry)

* - identifier: IConsumerAgentGroup
* - type: String
SET set_IConsumerAgentGroup(*)

* - identifier: attributes
* - type: Boolean
PARAMETER par_link_IConsumerAgentGroup_IConsumerAgentGroupAttribute_attributes(set_IConsumerAgentGroup,set_IConsumerAgentGroupAttribute)

* - identifier: informationAuthority
* - type: Float
PARAMETER par_IConsumerAgentGroup_informationAuthority(set_IConsumerAgentGroup)

* - identifier: IConsumerAgentGroupAttribute
* - type: String
SET set_IConsumerAgentGroupAttribute(*)

* - identifier: distribution
* - type: Boolean
PARAMETER par_link_IConsumerAgentGroupAttribute_IUnivariateDoubleDistribution_distribution(set_IConsumerAgentGroupAttribute,set_IUnivariateDoubleDistribution)

* - identifier: IFixedProductAwareness
* - type: String
SET set_IFixedProductAwareness(*)

* - identifier: agentGroup
* - type: Boolean
PARAMETER par_link_IFixedProductAwareness_IConsumerAgentGroup_agentGroup(set_IFixedProductAwareness,set_IConsumerAgentGroup)

* - identifier: fixedProduct
* - type: Boolean
PARAMETER par_link_IFixedProductAwareness_IFixedProduct_fixedProduct(set_IFixedProductAwareness,set_IFixedProduct)

* - identifier: distribution
* - type: Boolean
PARAMETER par_link_IFixedProductAwareness_IUnivariateDoubleDistribution_distribution(set_IFixedProductAwareness,set_IUnivariateDoubleDistribution)

* - identifier: IBooleanDistribution
* - type: String
SET set_IBooleanDistribution(set_IUnivariateDoubleDistribution)

* - identifier: seed
* - type: Integer
PARAMETER par_IBooleanDistribution_seed(set_IBooleanDistribution)

* - identifier: IConstantUnivariateDistribution
* - type: String
SET set_IConstantUnivariateDistribution(set_IUnivariateDoubleDistribution)

* - identifier: value
* - type: Float
PARAMETER par_IConstantUnivariateDistribution_value(set_IConstantUnivariateDistribution)

* - identifier: IFiniteMassPointsDiscreteDistribution
* - type: String
SET set_IFiniteMassPointsDiscreteDistribution(set_IUnivariateDoubleDistribution)

* - identifier: seed
* - type: Integer
PARAMETER par_IFiniteMassPointsDiscreteDistribution_seed(set_IFiniteMassPointsDiscreteDistribution)

* - identifier: massPoints
* - type: Boolean
PARAMETER par_link_IFiniteMassPointsDiscreteDistribution_IMassPoint_massPoints(set_IFiniteMassPointsDiscreteDistribution,set_IMassPoint)

* - identifier: IMassPoint
* - type: String
SET set_IMassPoint(*)

* - identifier: value
* - type: Float
PARAMETER par_IMassPoint_value(set_IMassPoint)

* - identifier: weight
* - type: Float
PARAMETER par_IMassPoint_weight(set_IMassPoint)

* - identifier: IRandomBoundedDistribution
* - type: String
SET set_IRandomBoundedDistribution(set_IUnivariateDoubleDistribution)

* - identifier: lowerBound
* - type: Float
PARAMETER par_IRandomBoundedDistribution_lowerBound(set_IRandomBoundedDistribution)

* - identifier: upperBound
* - type: Float
PARAMETER par_IRandomBoundedDistribution_upperBound(set_IRandomBoundedDistribution)

* - identifier: seed
* - type: Integer
PARAMETER par_IRandomBoundedDistribution_seed(set_IRandomBoundedDistribution)

* - identifier: IUnivariateDoubleDistribution
* - hidden: 1
* - type: String
SET set_IUnivariateDoubleDistribution(*)

* - identifier: IFastHeterogeneousSmallWorldTopology
* - type: String
SET set_IFastHeterogeneousSmallWorldTopology(*)

* - identifier: edgeType
* - type: Integer
PARAMETER par_IFastHeterogeneousSmallWorldTopology_edgeType(set_IFastHeterogeneousSmallWorldTopology)

* - identifier: isSelfReferential
* - type: Boolean
PARAMETER par_IFastHeterogeneousSmallWorldTopology_isSelfReferential(set_IFastHeterogeneousSmallWorldTopology)

* - identifier: initialWeight
* - type: Float
PARAMETER par_IFastHeterogeneousSmallWorldTopology_initialWeight(set_IFastHeterogeneousSmallWorldTopology)

* - identifier: seed
* - type: Integer
PARAMETER par_IFastHeterogeneousSmallWorldTopology_seed(set_IFastHeterogeneousSmallWorldTopology)

* - identifier: entries
* - type: Boolean
PARAMETER par_link_IFastHeterogeneousSmallWorldTopology_IFastHeterogeneousSmallWorldTopologyEntry_entries(set_IFastHeterogeneousSmallWorldTopology,set_IFastHeterogeneousSmallWorldTopologyEntry)

* - identifier: IFastHeterogeneousSmallWorldTopologyEntry
* - type: String
SET set_IFastHeterogeneousSmallWorldTopologyEntry(*)

* - identifier: group
* - type: Boolean
PARAMETER par_link_IFastHeterogeneousSmallWorldTopologyEntry_IConsumerAgentGroup_group(set_IFastHeterogeneousSmallWorldTopologyEntry,set_IConsumerAgentGroup)

* - identifier: beta
* - type: Float
PARAMETER par_IFastHeterogeneousSmallWorldTopologyEntry_beta(set_IFastHeterogeneousSmallWorldTopologyEntry)

* - identifier: z
* - type: Integer
PARAMETER par_IFastHeterogeneousSmallWorldTopologyEntry_z(set_IFastHeterogeneousSmallWorldTopologyEntry)

* - identifier: IFixedProduct
* - type: String
SET set_IFixedProduct(*)

* - identifier: group
* - type: Boolean
PARAMETER par_link_IFixedProduct_IProductGroup_group(set_IFixedProduct,set_IProductGroup)

* - identifier: attributes
* - type: Boolean
PARAMETER par_link_IFixedProduct_IFixedProductAttribute_attributes(set_IFixedProduct,set_IFixedProductAttribute)

* - identifier: IFixedProductAttribute
* - type: String
SET set_IFixedProductAttribute(*)

* - identifier: groupAttribute
* - type: Boolean
PARAMETER par_link_IFixedProductAttribute_IProductGroupAttribute_groupAttribute(set_IFixedProductAttribute,set_IProductGroupAttribute)

* - identifier: value
* - type: Float
PARAMETER par_IFixedProductAttribute_value(set_IFixedProductAttribute)

* - identifier: IProductGroup
* - type: String
SET set_IProductGroup(*)

* - identifier: attributes
* - type: Boolean
PARAMETER par_link_IProductGroup_IProductGroupAttribute_attributes(set_IProductGroup,set_IProductGroupAttribute)

* - identifier: IProductGroupAttribute
* - type: String
SET set_IProductGroupAttribute(*)

* - identifier: distribution
* - type: Boolean
PARAMETER par_link_IProductGroupAttribute_IUnivariateDoubleDistribution_distribution(set_IProductGroupAttribute,set_IUnivariateDoubleDistribution)

* - identifier: ISpace2D
* - type: String
SET set_ISpace2D(*)

* - identifier: metricID
* - type: Integer
PARAMETER par_ISpace2D_metricID(set_ISpace2D)

* - identifier: IContinuousTimeModel
* - type: String
SET set_IContinuousTimeModel(set_ITimeModel)

* - default: 86400
* - identifier: dilation
* - type: Float
PARAMETER par_IContinuousTimeModel_dilation(set_IContinuousTimeModel)

* - default: 86400000
* - identifier: delay
* - type: Integer
PARAMETER par_IContinuousTimeModel_delay(set_IContinuousTimeModel)

* - identifier: IDiscreteTimeModel
* - type: String
SET set_IDiscreteTimeModel(set_ITimeModel)

* - default: 1
* - identifier: delta
* - type: Integer
PARAMETER par_IDiscreteTimeModel_delta(set_IDiscreteTimeModel)

* - default: 900000
* - identifier: timePerTickInMs
* - type: Integer
PARAMETER par_IDiscreteTimeModel_timePerTickInMs(set_IDiscreteTimeModel)

* - default: 86400000
* - identifier: delay
* - type: Integer
PARAMETER par_IDiscreteTimeModel_delay(set_IDiscreteTimeModel)

* - identifier: ITimeModel
* - hidden: 1
* - type: String
SET set_ITimeModel(*)

* - identifier: debugLevel
* - type: Integer
SCALAR sca_global_debugLevel
