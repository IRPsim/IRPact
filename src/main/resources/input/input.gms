* - identifier: InAffinityEntry
* - type: String
SET set_InAffinityEntry(*)

* - identifier: srcCag
* - type: Boolean
PARAMETER par_link_InAffinityEntry_InConsumerAgentGroup_srcCag(set_InAffinityEntry,set_InConsumerAgentGroup)

* - identifier: tarCag
* - type: Boolean
PARAMETER par_link_InAffinityEntry_InConsumerAgentGroup_tarCag(set_InAffinityEntry,set_InConsumerAgentGroup)

* - identifier: affinityValue
* - type: Float
PARAMETER par_InAffinityEntry_affinityValue(set_InAffinityEntry)

* - identifier: InAwareness
* - hidden: 1
* - type: String
SET set_InAwareness(*)

* - identifier: InThresholdAwareness
* - type: String
SET set_InThresholdAwareness(set_InAwareness)

* - identifier: awarenessThreshold
* - type: Float
PARAMETER par_InThresholdAwareness_awarenessThreshold(set_InThresholdAwareness)

* - identifier: InConsumerAgentGroup
* - type: String
SET set_InConsumerAgentGroup(*)

* - identifier: cagAttributes
* - type: Boolean
PARAMETER par_link_InConsumerAgentGroup_InConsumerAgentGroupAttribute_cagAttributes(set_InConsumerAgentGroup,set_InConsumerAgentGroupAttribute)

* - identifier: cagAwareness
* - type: Boolean
PARAMETER par_link_InConsumerAgentGroup_InAwareness_cagAwareness(set_InConsumerAgentGroup,set_InAwareness)

* - identifier: informationAuthority
* - type: Float
PARAMETER par_InConsumerAgentGroup_informationAuthority(set_InConsumerAgentGroup)

* - identifier: numberOfAgentsX
* - type: Integer
PARAMETER par_InConsumerAgentGroup_numberOfAgentsX(set_InConsumerAgentGroup)

* - identifier: InConsumerAgentGroupAttribute
* - type: String
SET set_InConsumerAgentGroupAttribute(*)

* - identifier: cagAttrName
* - type: Boolean
PARAMETER par_link_InConsumerAgentGroupAttribute_InAttributeName_cagAttrName(set_InConsumerAgentGroupAttribute,set_InAttributeName)

* - identifier: cagAttrDistribution
* - type: Boolean
PARAMETER par_link_InConsumerAgentGroupAttribute_InUnivariateDoubleDistribution_cagAttrDistribution(set_InConsumerAgentGroupAttribute,set_InUnivariateDoubleDistribution)

* - identifier: InConstantUnivariateDistribution
* - type: String
SET set_InConstantUnivariateDistribution(set_InUnivariateDoubleDistribution)

* - identifier: constDistValue
* - type: Float
PARAMETER par_InConstantUnivariateDistribution_constDistValue(set_InConstantUnivariateDistribution)

* - identifier: InUnivariateDoubleDistribution
* - hidden: 1
* - type: String
SET set_InUnivariateDoubleDistribution(*)

* - identifier: InConsumerAgentGroupColor
* - type: String
SET set_InConsumerAgentGroupColor(*)

* - identifier: group
* - type: Boolean
PARAMETER par_link_InConsumerAgentGroupColor_InConsumerAgentGroup_group(set_InConsumerAgentGroupColor,set_InConsumerAgentGroup)

* - identifier: color
* - type: Boolean
PARAMETER par_link_InConsumerAgentGroupColor_GraphvizColor_color(set_InConsumerAgentGroupColor,set_GraphvizColor)

* - identifier: InDistanceEvaluator
* - hidden: 1
* - type: String
SET set_InDistanceEvaluator(*)

* - identifier: InFreeNetworkTopology
* - type: String
SET set_InFreeNetworkTopology(set_InGraphTopologyScheme)

* - identifier: distanceEvaluator
* - type: Boolean
PARAMETER par_link_InFreeNetworkTopology_InDistanceEvaluator_distanceEvaluator(set_InFreeNetworkTopology,set_InDistanceEvaluator)

* - identifier: numberOfTies
* - type: Boolean
PARAMETER par_link_InFreeNetworkTopology_InNumberOfTies_numberOfTies(set_InFreeNetworkTopology,set_InNumberOfTies)

* - identifier: initialWeight
* - type: Float
PARAMETER par_InFreeNetworkTopology_initialWeight(set_InFreeNetworkTopology)

* - identifier: InGraphTopologyScheme
* - hidden: 1
* - type: String
SET set_InGraphTopologyScheme(*)

* - identifier: InInverse
* - type: String
SET set_InInverse(set_InDistanceEvaluator)

* - identifier: placeholderInverse
* - type: Integer
PARAMETER par_InInverse_placeholderInverse(set_InInverse)

* - identifier: InNoDistance
* - type: String
SET set_InNoDistance(set_InDistanceEvaluator)

* - identifier: placeholderNoDistance
* - type: Integer
PARAMETER par_InNoDistance_placeholderNoDistance(set_InNoDistance)

* - identifier: InNumberOfTies
* - type: String
SET set_InNumberOfTies(*)

* - identifier: cag
* - type: Boolean
PARAMETER par_link_InNumberOfTies_InConsumerAgentGroup_cag(set_InNumberOfTies,set_InConsumerAgentGroup)

* - identifier: count
* - type: Integer
PARAMETER par_InNumberOfTies_count(set_InNumberOfTies)

* - identifier: InOrientationSupplier
* - type: String
SET set_InOrientationSupplier(*)

* - identifier: attrNameInOrientation
* - type: Boolean
PARAMETER par_link_InOrientationSupplier_InAttributeName_attrNameInOrientation(set_InOrientationSupplier,set_InAttributeName)

* - identifier: distInOrientation
* - type: Boolean
PARAMETER par_link_InOrientationSupplier_InUnivariateDoubleDistribution_distInOrientation(set_InOrientationSupplier,set_InUnivariateDoubleDistribution)

* - identifier: InProcessModel
* - hidden: 1
* - type: String
SET set_InProcessModel(*)

* - identifier: InRAProcessModel
* - type: String
SET set_InRAProcessModel(set_InProcessModel)

* - identifier: a
* - type: Float
PARAMETER par_InRAProcessModel_a(set_InRAProcessModel)

* - identifier: b
* - type: Float
PARAMETER par_InRAProcessModel_b(set_InRAProcessModel)

* - identifier: c
* - type: Float
PARAMETER par_InRAProcessModel_c(set_InRAProcessModel)

* - identifier: d
* - type: Float
PARAMETER par_InRAProcessModel_d(set_InRAProcessModel)

* - identifier: adopterPoints
* - type: Integer
PARAMETER par_InRAProcessModel_adopterPoints(set_InRAProcessModel)

* - identifier: interesetedPoints
* - type: Integer
PARAMETER par_InRAProcessModel_interesetedPoints(set_InRAProcessModel)

* - identifier: awarePoints
* - type: Integer
PARAMETER par_InRAProcessModel_awarePoints(set_InRAProcessModel)

* - identifier: unknownPoints
* - type: Integer
PARAMETER par_InRAProcessModel_unknownPoints(set_InRAProcessModel)

* - identifier: InSlopeSupplier
* - type: String
SET set_InSlopeSupplier(*)

* - identifier: attrNameSlope
* - type: Boolean
PARAMETER par_link_InSlopeSupplier_InAttributeName_attrNameSlope(set_InSlopeSupplier,set_InAttributeName)

* - identifier: distSlope
* - type: Boolean
PARAMETER par_link_InSlopeSupplier_InUnivariateDoubleDistribution_distSlope(set_InSlopeSupplier,set_InUnivariateDoubleDistribution)

* - identifier: InProductGroup
* - type: String
SET set_InProductGroup(*)

* - identifier: pgAttributes
* - type: Boolean
PARAMETER par_link_InProductGroup_InProductGroupAttribute_pgAttributes(set_InProductGroup,set_InProductGroupAttribute)

* - identifier: placeholderProduct
* - type: Integer
PARAMETER par_InProductGroup_placeholderProduct(set_InProductGroup)

* - identifier: InProductGroupAttribute
* - type: String
SET set_InProductGroupAttribute(*)

* - identifier: attrName
* - type: Boolean
PARAMETER par_link_InProductGroupAttribute_InAttributeName_attrName(set_InProductGroupAttribute,set_InAttributeName)

* - identifier: attrDistribution
* - type: Boolean
PARAMETER par_link_InProductGroupAttribute_InUnivariateDoubleDistribution_attrDistribution(set_InProductGroupAttribute,set_InUnivariateDoubleDistribution)

* - identifier: InConstantSpatialDistribution2D
* - type: String
SET set_InConstantSpatialDistribution2D(set_InSpatialDistribution)

* - identifier: cagConstant
* - type: Boolean
PARAMETER par_link_InConstantSpatialDistribution2D_InConsumerAgentGroup_cagConstant(set_InConstantSpatialDistribution2D,set_InConsumerAgentGroup)

* - identifier: x
* - type: Float
PARAMETER par_InConstantSpatialDistribution2D_x(set_InConstantSpatialDistribution2D)

* - identifier: y
* - type: Float
PARAMETER par_InConstantSpatialDistribution2D_y(set_InConstantSpatialDistribution2D)

* - identifier: InSpace2D
* - type: String
SET set_InSpace2D(set_InSpatialModel)

* - identifier: useEuclid
* - type: Boolean
PARAMETER par_InSpace2D_useEuclid(set_InSpace2D)

* - identifier: InSpatialDistribution
* - hidden: 1
* - type: String
SET set_InSpatialDistribution(*)

* - identifier: InSpatialModel
* - hidden: 1
* - type: String
SET set_InSpatialModel(*)

* - identifier: InDiscreteTimeModel
* - type: String
SET set_InDiscreteTimeModel(set_InTimeModel)

* - identifier: timePerTickInMs
* - type: Integer
PARAMETER par_InDiscreteTimeModel_timePerTickInMs(set_InDiscreteTimeModel)

* - identifier: InTimeModel
* - hidden: 1
* - type: String
SET set_InTimeModel(*)

* - identifier: InAttributeName
* - type: String
SET set_InAttributeName(*)

* - identifier: placeholder
* - type: Integer
PARAMETER par_InAttributeName_placeholder(set_InAttributeName)

* - identifier: seed
* - type: Integer
SCALAR sca_InGeneral_seed

* - identifier: runOptActDemo
* - type: Boolean
SCALAR sca_InGeneral_runOptActDemo

* - identifier: logGraphCreation
* - type: Boolean
SCALAR sca_InGeneral_logGraphCreation

* - identifier: logAgentCreation
* - type: Boolean
SCALAR sca_InGeneral_logAgentCreation

* - description: Version von IRPact für dieses Szenario.
* - hidden: 1
* - identifier: InVersion
* - type: String
SET set_InVersion(*)

* - identifier: placeholderVersion
* - type: Integer
PARAMETER par_InVersion_placeholderVersion(set_InVersion)

* - description: Simulationshorizont
* - hidden: 1
* - identifier: SH
* - type: TimeSeries
SET set_ii(*)

* - description: Mehrwertsteuer
* - identifier: MWST
* - type: Float
SCALAR sca_Tax_PS_vat

* - default: 1
* - domain: [0|1]
* - description: Wahl Deutschland
* - identifier: Wahl Deutschland
* - rule: IF (sca_X_MS_DE_country == 1, sca_X_MS_CH_country = 0)
* - rule: IF (sca_X_MS_DE_country == 0, sca_X_MS_CH_country = 1)
* - type: Boolean
SCALAR sca_X_MS_DE_country

* - default: 0
* - domain: [0|1]
* - description: Wahl Schweiz
* - identifier: Wahl Schweiz
* - rule: IF (sca_X_MS_CH_country == 1, sca_X_MS_DE_country = 0)
* - rule: IF (sca_X_MS_CH_country == 0, sca_X_MS_DE_country = 1)
* - type: Boolean
SCALAR sca_X_MS_CH_country

* - description: Marktpreis Strom
* - identifier: Marktpreis Strom
* - unit: [EUR / MWh]
* - type: Float
PARAMETER par_C_MS_E(set_ii)

* - domain: [0, 1]
* - description: Bitte geben Sie hier an, wie die Technologiekosten welchem Sektor (Strom, Wärme, Reserve etc.) (anteilig) zugeordnet werden sollen.
* - identifier: Sektorzuweisung der Technologiekosten
* - type: Float
PARAMETER par_SOH_pss_sector(set_sector,set_pss)

* - default: 0
* - description: Bitte geben Sie hier die an die verschiedenen Akteure (Netz-, Politik- und Vertriebsseite) zu zahlenden Arbeitstarife für den Strom-Netzbezug an
* - identifier: Strom-Arbeitstarife Netzbezug
* - unit: [EUR / MWh]
* - type: Float
PARAMETER par_F_E_EGrid_energy(set_ii,set_side_fares,set_pss)

* - description: Lastgang
* - hidden: 1
* - identifier: Lastgang
* - type: String
SET set_load_DS(set_pss)

* - description: Strom-Verbrauchertechnologie
* - identifier: Strom-Verbrauchertechnologie
* - type: String
SET set_load_DS_E(set_load_DSLOA)

* - domain: [0,)
* - description: Bitte geben Sie hier ein gewünschtes elektrische Lastprofil ein
* - identifier: Elektrisches Lastprofil
* - unit: [MWh]
* - type: Float
PARAMETER par_L_DS_E(set_ii,set_load_DS_E)

* - description: Verbrauchertechnologie
* - hidden: 1
* - identifier: Verbrauchertechnologie
* - type: String
SET set_load_DSLOA(set_load_DS)

* - description: Prosumstorer
* - hidden: 1
* - identifier: Prosumstorer
* - type: String
SET set_pss(*)

* - default: E
* - description: Energiesektor
* - hidden: 1
* - identifier: Energiesektor
* - type: String
SET set_sector(*)

* - description: Marktteilnehmer
* - hidden: 1
* - identifier: MT
* - type: String
SET set_side(*)

* - description: Kundengruppe in IRPact
* - identifier: KG
* - type: String
SET set_side_cust(set_side)

* - default: 10
* - description: Anzahl der Kunden
* - identifier: KGA
* - type: Integer
PARAMETER par_S_DS(set_side_cust)

* - default: 5
* - description: Erhöht die Anzahl der Kunden in der Gruppe um den gewünschten Wert.
* - identifier: KGAM
* - type: Integer
PARAMETER par_kg_modifier(set_side_cust)

* - description: Stromsparte je Kundengruppe
* - identifier: SK
* - unit: [EUR]
* - type: Float
PARAMETER par_IuO_ESector_CustSide(set_ii,set_side_cust)

* - default: SMS, NS, PS
* - description: Tarifteilnehmer
* - hidden: 1
* - identifier: Tarifteilnehmer
* - type: String
SET set_side_fares(set_side)

* - description: Erzeugertechnologie
* - hidden: 1
* - identifier: Erzeugertechnologie
* - type: String
SET set_tech_DEGEN(set_tech_DES)

* - description: Dezentrale Energietechnologie
* - hidden: 1
* - identifier: Dezentrale Energietechnologie
* - type: String
SET set_tech_DES(set_pss)

* - description: Stromspeicher
* - identifier: Stromspeicher
* - type: String
SET set_tech_DES_ES(set_tech_DESTO)

* - default: 0
* - domain: [0,)
* - description: Bitte tragen Sie hier die spezifische Förderung der öffentlichen Hand für Stromspeicher ein
* - identifier: Förderung für Stromspeicher durch Politik
* - unit: [EUR / MWh]
* - type: Float
PARAMETER par_Inc_PS_ES(set_tech_DES_ES)

* - description: PV-Anlage
* - identifier: PV-Anlage
* - type: String
SET set_tech_DES_PV(set_tech_DEGEN)

* - default: 0
* - domain: [0,)
* - description: Bitte geben Sie hier die gesamte Modulfläche der PV-Anlage an
* - identifier: Modulfläche PV-Anlage
* - unit: [m2]
* - type: Float
PARAMETER par_A_DES_PV(set_tech_DES_PV)

* - description: Speichertechnologie
* - hidden: 1
* - identifier: Speichertechnologie
* - type: String
SET set_tech_DESTO(set_tech_DES)

* - identifier: AgentGroup
* - type: String
SET set_AgentGroup(*)

* - description: Anzahl der Agenten
* - identifier: numberOfAgents
* - type: Integer
PARAMETER par_AgentGroup_numberOfAgents(set_AgentGroup)

* - description: Farbe, welche diese Gruppe im Graphen haben soll. Wichtig: es wird nur der erste Wert verwendet! Falls keine Farbe gewählt wird, ist die Farbe schwarz.
* - identifier: agentColor
* - type: Boolean
PARAMETER par_link_AgentGroup_GraphvizColor_agentColor(set_AgentGroup,set_GraphvizColor)

* - identifier: IGraphTopology
* - hidden: 1
* - type: String
SET set_IGraphTopology(*)

* - identifier: IWattsStrogatzModel
* - type: String
SET set_IWattsStrogatzModel(set_IGraphTopology)

* - description: IWattsStrogatzModel_wsmK
* - identifier: wsmK
* - type: Integer
PARAMETER par_IWattsStrogatzModel_wsmK(set_IWattsStrogatzModel)

* - description: IWattsStrogatzModel_wsmBeta
* - identifier: wsmBeta
* - type: Float
PARAMETER par_IWattsStrogatzModel_wsmBeta(set_IWattsStrogatzModel)

* - description: IWattsStrogatzModel_wsmSelfReferential
* - identifier: wsmSelfReferential
* - type: Boolean
PARAMETER par_IWattsStrogatzModel_wsmSelfReferential(set_IWattsStrogatzModel)

* - description: IWattsStrogatzModel_wsmSeed
* - identifier: wsmSeed
* - type: Integer
PARAMETER par_IWattsStrogatzModel_wsmSeed(set_IWattsStrogatzModel)

* - description: Soll diese Topology verwendet werden? Hinweis: es wird nur die erste gültige genutzt! Der Rest wird ignoriert!
* - identifier: wsmUseThis
* - type: Boolean
PARAMETER par_IWattsStrogatzModel_wsmUseThis(set_IWattsStrogatzModel)

* - identifier: IFreeMultiGraphTopology
* - type: String
SET set_IFreeMultiGraphTopology(set_IGraphTopology)

* - description: IFreeMultiGraphTopology_ftEdgeCount
* - identifier: ftEdgeCount
* - type: Integer
PARAMETER par_IFreeMultiGraphTopology_ftEdgeCount(set_IFreeMultiGraphTopology)

* - description: IFreeMultiGraphTopology_ftSelfReferential
* - identifier: ftSelfReferential
* - type: Boolean
PARAMETER par_IFreeMultiGraphTopology_ftSelfReferential(set_IFreeMultiGraphTopology)

* - description: IFreeMultiGraphTopology_ftSeed
* - identifier: ftSeed
* - type: Integer
PARAMETER par_IFreeMultiGraphTopology_ftSeed(set_IFreeMultiGraphTopology)

* - description: Soll diese Topology verwendet werden? Hinweis: es wird nur die erste gültige genutzt! Der Rest wird ignoriert!
* - identifier: ftUseThis
* - type: Boolean
PARAMETER par_IFreeMultiGraphTopology_ftUseThis(set_IFreeMultiGraphTopology)

* - identifier: GraphvizColor
* - type: String
SET set_GraphvizColor(*)

* - description: RGBA Code der Farbe im Dezimalsystem.
* - identifier: RGBA-Wert
* - type: Integer
PARAMETER par_GraphvizColor_rgba(set_GraphvizColor)

* - identifier: GraphvizLayoutAlgorithm
* - type: String
SET set_GraphvizLayoutAlgorithm(*)

* - default: -1
* - description: -1: eigenes Layout, 0: DOT, 1: NEATO, 2: FDP, 3: SFDP, 4: TWOPI, 5: CIRCO
* - identifier: Layout-ID
* - type: Integer
PARAMETER par_GraphvizLayoutAlgorithm_layoutId(set_GraphvizLayoutAlgorithm)

* - description: Verwendet dieses Layout.
* - identifier: Layout nutzen?
* - type: Boolean
PARAMETER par_GraphvizLayoutAlgorithm_useLayout(set_GraphvizLayoutAlgorithm)

* - identifier: GraphvizOutputFormat
* - type: String
SET set_GraphvizOutputFormat(*)

* - default: -1
* - description: -1: eigenes Format, 0: PNG, 1: SVG
* - identifier: Format-ID
* - type: Integer
PARAMETER par_GraphvizOutputFormat_formatId(set_GraphvizOutputFormat)

* - description: Verwendet dieses Ausgabeformat.
* - identifier: Format nutzen?
* - type: Boolean
PARAMETER par_GraphvizOutputFormat_useFormat(set_GraphvizOutputFormat)

* - description: Fixiert die Positionen fuer das neato-Layout (-n).
* - identifier: Knotenpositionen fixieren?
* - type: Boolean
SCALAR sca_GraphvizGlobal_fixedNeatoPosition

* - description: Legt den Skalierungsfaktor fest (-s). Der Wert 0 deaktiviert ihn.
* - identifier: Skalierungsfaktor
* - type: Float
SCALAR sca_GraphvizGlobal_scaleFactor
