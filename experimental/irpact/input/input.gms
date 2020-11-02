* - identifier: IBasicAffinitiesEntry
* - type: String
SET set_IBasicAffinitiesEntry(*)

* - identifier: from
* - type: Boolean
PARAMETER par_link_IBasicAffinitiesEntry_IConsumerAgentGroup_from(set_IBasicAffinitiesEntry,set_IConsumerAgentGroup)

* - identifier: to
* - type: Boolean
PARAMETER par_link_IBasicAffinitiesEntry_IConsumerAgentGroup_to(set_IBasicAffinitiesEntry,set_IConsumerAgentGroup)

* - identifier: affinityValue
* - type: Float
PARAMETER par_IBasicAffinitiesEntry_affinityValue(set_IBasicAffinitiesEntry)

* - identifier: IBasicAffinityMapping
* - type: String
SET set_IBasicAffinityMapping(*)

* - identifier: affinityEntries
* - type: Boolean
PARAMETER par_link_IBasicAffinityMapping_IBasicAffinitiesEntry_affinityEntries(set_IBasicAffinityMapping,set_IBasicAffinitiesEntry)

* - identifier: IConsumerAgentGroup
* - type: String
SET set_IConsumerAgentGroup(*)

* - identifier: cagAttributes
* - type: Boolean
PARAMETER par_link_IConsumerAgentGroup_IConsumerAgentGroupAttribute_cagAttributes(set_IConsumerAgentGroup,set_IConsumerAgentGroupAttribute)

* - identifier: productAwareness
* - type: Boolean
PARAMETER par_link_IConsumerAgentGroup_IAwareness_productAwareness(set_IConsumerAgentGroup,set_IAwareness)

* - identifier: informationAuthority
* - type: Float
PARAMETER par_IConsumerAgentGroup_informationAuthority(set_IConsumerAgentGroup)

* - identifier: numberOfAgents
* - type: Integer
PARAMETER par_IConsumerAgentGroup_numberOfAgents(set_IConsumerAgentGroup)

* - identifier: IConsumerAgentGroupAttribute
* - type: String
SET set_IConsumerAgentGroupAttribute(*)

* - identifier: cagAttrDistribution
* - type: Boolean
PARAMETER par_link_IConsumerAgentGroupAttribute_IUnivariateDoubleDistribution_cagAttrDistribution(set_IConsumerAgentGroupAttribute,set_IUnivariateDoubleDistribution)

* - identifier: IAwareness
* - hidden: 1
* - type: String
SET set_IAwareness(*)

* - identifier: IFixedProductAwareness
* - type: String
SET set_IFixedProductAwareness(*)

* - identifier: awarenessAgentGroup
* - type: Boolean
PARAMETER par_link_IFixedProductAwareness_IConsumerAgentGroup_awarenessAgentGroup(set_IFixedProductAwareness,set_IConsumerAgentGroup)

* - identifier: awarenessFixedProduct
* - type: Boolean
PARAMETER par_link_IFixedProductAwareness_IFixedProduct_awarenessFixedProduct(set_IFixedProductAwareness,set_IFixedProduct)

* - identifier: awarenessDistribution
* - type: Boolean
PARAMETER par_link_IFixedProductAwareness_IUnivariateDoubleDistribution_awarenessDistribution(set_IFixedProductAwareness,set_IUnivariateDoubleDistribution)

* - identifier: ISimpleAwareness
* - type: String
SET set_ISimpleAwareness(set_IAwareness)

* - identifier: placeholder
* - type: Integer
PARAMETER par_ISimpleAwareness_placeholder(set_ISimpleAwareness)

* - identifier: IThresholdAwareness
* - type: String
SET set_IThresholdAwareness(set_IAwareness)

* - identifier: awarenessThreshold
* - type: Float
PARAMETER par_IThresholdAwareness_awarenessThreshold(set_IThresholdAwareness)

* - identifier: IBooleanDistribution
* - type: String
SET set_IBooleanDistribution(set_IUnivariateDoubleDistribution)

* - identifier: boolDistSeed
* - type: Integer
PARAMETER par_IBooleanDistribution_boolDistSeed(set_IBooleanDistribution)

* - identifier: IConstantUnivariateDistribution
* - type: String
SET set_IConstantUnivariateDistribution(set_IUnivariateDoubleDistribution)

* - identifier: constDistValue
* - type: Float
PARAMETER par_IConstantUnivariateDistribution_constDistValue(set_IConstantUnivariateDistribution)

* - identifier: IFiniteMassPointsDiscreteDistribution
* - type: String
SET set_IFiniteMassPointsDiscreteDistribution(set_IUnivariateDoubleDistribution)

* - identifier: fmpSeed
* - type: Integer
PARAMETER par_IFiniteMassPointsDiscreteDistribution_fmpSeed(set_IFiniteMassPointsDiscreteDistribution)

* - identifier: massPoints
* - type: Boolean
PARAMETER par_link_IFiniteMassPointsDiscreteDistribution_IMassPoint_massPoints(set_IFiniteMassPointsDiscreteDistribution,set_IMassPoint)

* - identifier: IMassPoint
* - type: String
SET set_IMassPoint(*)

* - identifier: mpValue
* - type: Float
PARAMETER par_IMassPoint_mpValue(set_IMassPoint)

* - identifier: mpWeight
* - type: Float
PARAMETER par_IMassPoint_mpWeight(set_IMassPoint)

* - identifier: IRandomBoundedDistribution
* - type: String
SET set_IRandomBoundedDistribution(set_IUnivariateDoubleDistribution)

* - identifier: rndDistLowerBound
* - type: Float
PARAMETER par_IRandomBoundedDistribution_rndDistLowerBound(set_IRandomBoundedDistribution)

* - identifier: rndDistUpperBound
* - type: Float
PARAMETER par_IRandomBoundedDistribution_rndDistUpperBound(set_IRandomBoundedDistribution)

* - identifier: rndDistSeed
* - type: Integer
PARAMETER par_IRandomBoundedDistribution_rndDistSeed(set_IRandomBoundedDistribution)

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

* - identifier: topoSeed
* - type: Integer
PARAMETER par_IFastHeterogeneousSmallWorldTopology_topoSeed(set_IFastHeterogeneousSmallWorldTopology)

* - identifier: topoEntries
* - type: Boolean
PARAMETER par_link_IFastHeterogeneousSmallWorldTopology_IFastHeterogeneousSmallWorldTopologyEntry_topoEntries(set_IFastHeterogeneousSmallWorldTopology,set_IFastHeterogeneousSmallWorldTopologyEntry)

* - identifier: IFastHeterogeneousSmallWorldTopologyEntry
* - type: String
SET set_IFastHeterogeneousSmallWorldTopologyEntry(*)

* - identifier: topoGroup
* - type: Boolean
PARAMETER par_link_IFastHeterogeneousSmallWorldTopologyEntry_IConsumerAgentGroup_topoGroup(set_IFastHeterogeneousSmallWorldTopologyEntry,set_IConsumerAgentGroup)

* - identifier: beta
* - type: Float
PARAMETER par_IFastHeterogeneousSmallWorldTopologyEntry_beta(set_IFastHeterogeneousSmallWorldTopologyEntry)

* - identifier: z
* - type: Integer
PARAMETER par_IFastHeterogeneousSmallWorldTopologyEntry_z(set_IFastHeterogeneousSmallWorldTopologyEntry)

* - identifier: IFixedProduct
* - type: String
SET set_IFixedProduct(*)

* - identifier: fpGroup
* - type: Boolean
PARAMETER par_link_IFixedProduct_IProductGroup_fpGroup(set_IFixedProduct,set_IProductGroup)

* - identifier: fpAttributes
* - type: Boolean
PARAMETER par_link_IFixedProduct_IFixedProductAttribute_fpAttributes(set_IFixedProduct,set_IFixedProductAttribute)

* - identifier: IFixedProductAttribute
* - type: String
SET set_IFixedProductAttribute(*)

* - identifier: fpaGroupAttribute
* - type: Boolean
PARAMETER par_link_IFixedProductAttribute_IProductGroupAttribute_fpaGroupAttribute(set_IFixedProductAttribute,set_IProductGroupAttribute)

* - identifier: fpaValue
* - type: Float
PARAMETER par_IFixedProductAttribute_fpaValue(set_IFixedProductAttribute)

* - identifier: IProductGroup
* - type: String
SET set_IProductGroup(*)

* - identifier: pgAttributes
* - type: Boolean
PARAMETER par_link_IProductGroup_IProductGroupAttribute_pgAttributes(set_IProductGroup,set_IProductGroupAttribute)

* - identifier: IProductGroupAttribute
* - type: String
SET set_IProductGroupAttribute(*)

* - identifier: pgAttrDistribution
* - type: Boolean
PARAMETER par_link_IProductGroupAttribute_IUnivariateDoubleDistribution_pgAttrDistribution(set_IProductGroupAttribute,set_IUnivariateDoubleDistribution)

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
* - identifier: contDelay
* - type: Integer
PARAMETER par_IContinuousTimeModel_contDelay(set_IContinuousTimeModel)

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
* - identifier: discDelay
* - type: Integer
PARAMETER par_IDiscreteTimeModel_discDelay(set_IDiscreteTimeModel)

* - identifier: ITimeModel
* - hidden: 1
* - type: String
SET set_ITimeModel(*)

* - identifier: debugLevel
* - type: Integer
SCALAR sca_global_debugLevel
