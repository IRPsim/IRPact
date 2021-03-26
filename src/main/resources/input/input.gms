* - identifier: InAffinityEntry
* - hidden: 1
* - type: String
SET set_InAffinityEntry(set_InEntity)

* - identifier: InComplexAffinityEntry
* - type: String
SET set_InComplexAffinityEntry(set_InAffinityEntry,set_InEntity)

* - description: Ausgangsgruppe
* - identifier: Ausgangsgruppe
* - type: Boolean
PARAMETER par_link_InComplexAffinityEntry_InConsumerAgentGroup_srcCag(set_InComplexAffinityEntry,set_InConsumerAgentGroup)

* - description: Zielgruppe
* - identifier: Zielgruppe
* - type: Boolean
PARAMETER par_link_InComplexAffinityEntry_InConsumerAgentGroup_tarCag(set_InComplexAffinityEntry,set_InConsumerAgentGroup)

* - description: Affinity-Wert
* - identifier: Affinity-Wert
* - type: Float
PARAMETER par_InComplexAffinityEntry_affinityValue(set_InComplexAffinityEntry)

* - identifier: InNameSplitAffinityEntry
* - type: String
SET set_InNameSplitAffinityEntry(set_InAffinityEntry,set_InEntity)

* - description: Affinity-Wert
* - identifier: Affinity-Wert
* - type: Float
PARAMETER par_InNameSplitAffinityEntry_affinityValue(set_InNameSplitAffinityEntry)

* - identifier: InConsumerAgentGroup
* - hidden: 1
* - type: String
SET set_InConsumerAgentGroup(set_InEntity)

* - identifier: InConsumerAgentGroupAttribute
* - hidden: 1
* - type: String
SET set_InConsumerAgentGroupAttribute(set_InEntity)

* - identifier: InDependentConsumerAgentGroupAttribute
* - hidden: 1
* - type: String
SET set_InDependentConsumerAgentGroupAttribute(set_InConsumerAgentGroupAttribute,set_InEntity)

* - identifier: InGeneralConsumerAgentGroup
* - type: String
SET set_InGeneralConsumerAgentGroup(set_InConsumerAgentGroup,set_InEntity)

* - description: Zu dieser Gruppe geh�rende Attribute.
* - identifier: Gruppenattribute
* - type: Boolean
PARAMETER par_link_InGeneralConsumerAgentGroup_InDependentConsumerAgentGroupAttribute_cagAttributes(set_InGeneralConsumerAgentGroup,set_InDependentConsumerAgentGroupAttribute)

* - description: Legt das Interessensschema fest.
* - identifier: Interessensschema
* - type: Boolean
PARAMETER par_link_InGeneralConsumerAgentGroup_InProductInterestSupplyScheme_cagInterest(set_InGeneralConsumerAgentGroup,set_InProductInterestSupplyScheme)

* - description: Legt das Schema f�r das finden von passenden Produkten fest
* - identifier: Schema f�r die Produktfindung
* - type: Boolean
PARAMETER par_link_InGeneralConsumerAgentGroup_InProductFindingScheme_productFindingSchemes(set_InGeneralConsumerAgentGroup,set_InProductFindingScheme)

* - description: Legt die Verteilungsfunktion f�r diese Gruppe fest
* - identifier: R�umliche Verteilungsfunktion
* - type: Boolean
PARAMETER par_link_InGeneralConsumerAgentGroup_InSpatialDistribution_spatialDistribution(set_InGeneralConsumerAgentGroup,set_InSpatialDistribution)

* - description: informationAuthority
* - identifier: [ungenutzt] informationAuthority
* - type: Float
PARAMETER par_InGeneralConsumerAgentGroup_informationAuthority(set_InGeneralConsumerAgentGroup)

* - identifier: InGeneralConsumerAgentGroupAttribute
* - type: String
SET set_InGeneralConsumerAgentGroupAttribute(set_InDependentConsumerAgentGroupAttribute,set_InConsumerAgentGroupAttribute,set_InEntity)

* - description: Name des Attributes f�r das Gruppenattribut.
* - identifier: Attributname
* - type: Boolean
PARAMETER par_link_InGeneralConsumerAgentGroupAttribute_InAttributeName_attrName(set_InGeneralConsumerAgentGroupAttribute,set_InAttributeName)

* - description: Legt die zu nutzende Verteilungsfunktion fest.
* - identifier: Verteilungsfunktion
* - type: Boolean
PARAMETER par_link_InGeneralConsumerAgentGroupAttribute_InUnivariateDoubleDistribution_dist(set_InGeneralConsumerAgentGroupAttribute,set_InUnivariateDoubleDistribution)

* - identifier: InIndependentConsumerAgentGroupAttribute
* - hidden: 1
* - type: String
SET set_InIndependentConsumerAgentGroupAttribute(set_InConsumerAgentGroupAttribute,set_InEntity)

* - identifier: InNameSplitConsumerAgentGroupAttribute
* - type: String
SET set_InNameSplitConsumerAgentGroupAttribute(set_InIndependentConsumerAgentGroupAttribute,set_InConsumerAgentGroupAttribute,set_InEntity)

* - description: Legt die Verteilungsfunktion fest, auf deren Basis die Attributwerte gezogen werden.
* - identifier: Verteilungsfunktion
* - type: Boolean
PARAMETER par_link_InNameSplitConsumerAgentGroupAttribute_InUnivariateDoubleDistribution_dist(set_InNameSplitConsumerAgentGroupAttribute,set_InUnivariateDoubleDistribution)

* - identifier: InPVactConsumerAgentGroup
* - type: String
SET set_InPVactConsumerAgentGroup(set_InConsumerAgentGroup,set_InEntity)

* - description: noveltySeeking
* - identifier: noveltySeeking
* - type: Boolean
PARAMETER par_link_InPVactConsumerAgentGroup_InUnivariateDoubleDistribution_noveltySeeking(set_InPVactConsumerAgentGroup,set_InUnivariateDoubleDistribution)

* - description: dependentJudgmentMaking
* - identifier: dependentJudgmentMaking
* - type: Boolean
PARAMETER par_link_InPVactConsumerAgentGroup_InUnivariateDoubleDistribution_dependentJudgmentMaking(set_InPVactConsumerAgentGroup,set_InUnivariateDoubleDistribution)

* - description: environmentalConcern
* - identifier: environmentalConcern
* - type: Boolean
PARAMETER par_link_InPVactConsumerAgentGroup_InUnivariateDoubleDistribution_environmentalConcern(set_InPVactConsumerAgentGroup,set_InUnivariateDoubleDistribution)

* - description: interestThreshold
* - identifier: interestThreshold
* - type: Boolean
PARAMETER par_link_InPVactConsumerAgentGroup_InUnivariateDoubleDistribution_interestThreshold(set_InPVactConsumerAgentGroup,set_InUnivariateDoubleDistribution)

* - description: financialThreshold
* - identifier: financialThreshold
* - type: Boolean
PARAMETER par_link_InPVactConsumerAgentGroup_InUnivariateDoubleDistribution_financialThreshold(set_InPVactConsumerAgentGroup,set_InUnivariateDoubleDistribution)

* - description: adoptionThreshold
* - identifier: adoptionThreshold
* - type: Boolean
PARAMETER par_link_InPVactConsumerAgentGroup_InUnivariateDoubleDistribution_adoptionThreshold(set_InPVactConsumerAgentGroup,set_InUnivariateDoubleDistribution)

* - description: communication
* - identifier: communication
* - type: Boolean
PARAMETER par_link_InPVactConsumerAgentGroup_InUnivariateDoubleDistribution_communication(set_InPVactConsumerAgentGroup,set_InUnivariateDoubleDistribution)

* - description: rewire
* - identifier: rewire
* - type: Boolean
PARAMETER par_link_InPVactConsumerAgentGroup_InUnivariateDoubleDistribution_rewire(set_InPVactConsumerAgentGroup,set_InUnivariateDoubleDistribution)

* - description: initialAdopter
* - identifier: initialAdopter
* - type: Boolean
PARAMETER par_link_InPVactConsumerAgentGroup_InUnivariateDoubleDistribution_initialAdopter(set_InPVactConsumerAgentGroup,set_InUnivariateDoubleDistribution)

* - identifier: rateOfConvergence
* - type: Boolean
PARAMETER par_link_InPVactConsumerAgentGroup_InUnivariateDoubleDistribution_rateOfConvergence(set_InPVactConsumerAgentGroup,set_InUnivariateDoubleDistribution)

* - identifier: initialProductInterest
* - type: Boolean
PARAMETER par_link_InPVactConsumerAgentGroup_InUnivariateDoubleDistribution_initialProductInterest(set_InPVactConsumerAgentGroup,set_InUnivariateDoubleDistribution)

* - identifier: constructionRate
* - type: Boolean
PARAMETER par_link_InPVactConsumerAgentGroup_InUnivariateDoubleDistribution_constructionRate(set_InPVactConsumerAgentGroup,set_InUnivariateDoubleDistribution)

* - identifier: renovationRate
* - type: Boolean
PARAMETER par_link_InPVactConsumerAgentGroup_InUnivariateDoubleDistribution_renovationRate(set_InPVactConsumerAgentGroup,set_InUnivariateDoubleDistribution)

* - description: spatialDistribution
* - identifier: spatialDistribution
* - type: Boolean
PARAMETER par_link_InPVactConsumerAgentGroup_InSpatialDistribution_spatialDistribution(set_InPVactConsumerAgentGroup,set_InSpatialDistribution)

* - description: informationAuthority
* - identifier: informationAuthority
* - type: Float
PARAMETER par_InPVactConsumerAgentGroup_informationAuthority(set_InPVactConsumerAgentGroup)

* - identifier: InFixConsumerAgentPopulationSize
* - type: String
SET set_InFixConsumerAgentPopulationSize(set_InPopulationSize,set_InEntity)

* - description: Legt die Anzahl der Agenten bzw. die Populationsgr��e f�r diese Konsumergruppe fest.
* - identifier: Anzahl der Agenten
* - type: Integer
PARAMETER par_InFixConsumerAgentPopulationSize_size(set_InFixConsumerAgentPopulationSize)

* - description: Legt die Konsumergruppen fest, welche diese Populationsgr��e haben sollen.
* - identifier: Konsumergruppen
* - type: Boolean
PARAMETER par_link_InFixConsumerAgentPopulationSize_InConsumerAgentGroup_cags(set_InFixConsumerAgentPopulationSize,set_InConsumerAgentGroup)

* - identifier: InPopulationSize
* - hidden: 1
* - type: String
SET set_InPopulationSize(set_InEntity)

* - identifier: InRelativeExternConsumerAgentPopulationSize
* - type: String
SET set_InRelativeExternConsumerAgentPopulationSize(set_InPopulationSize,set_InEntity)

* - description: Legt gesamte Populationsgr��e f�r alle hier verwendeten Konsumergruppen fest. Falls die maximal m�gliche Anzahl genutzt werden soll, wird dieser Wert ignoriert.
* - identifier: Anzahl der Agenten
* - type: Integer
PARAMETER par_InRelativeExternConsumerAgentPopulationSize_totalSize(set_InRelativeExternConsumerAgentPopulationSize)

* - description: Legt fest, ob die gesamte Populationsgr��e aus der Datei berechnet werden soll.
* - identifier: Maximal m�gliche Population nutzen?
* - type: Boolean
PARAMETER par_InRelativeExternConsumerAgentPopulationSize_useMaximumSize(set_InRelativeExternConsumerAgentPopulationSize)

* - description: Legt fest, ob alle Gruppen aus der Datei f�r die Berechnung der Anteile genutzt werden sollen oder nur die hier spezifizierten.
* - identifier: Alle Konsumergruppenf�r die Anteilsberechnung ber�cksichtigen?
* - type: Boolean
PARAMETER par_InRelativeExternConsumerAgentPopulationSize_considerAllForShares(set_InRelativeExternConsumerAgentPopulationSize)

* - description: Legt die Konsumergruppen fest, welche ber�cksichtigt werden sollen.
* - identifier: Konsumergruppen
* - type: Boolean
PARAMETER par_link_InRelativeExternConsumerAgentPopulationSize_InConsumerAgentGroup_cags(set_InRelativeExternConsumerAgentPopulationSize,set_InConsumerAgentGroup)

* - description: Tabellarische Eingabedatei, aus der die Informationen gelesen werden sollen.
* - identifier: Tabellarische Datei
* - type: Boolean
PARAMETER par_link_InRelativeExternConsumerAgentPopulationSize_InSpatialTableFile_file(set_InRelativeExternConsumerAgentPopulationSize,set_InSpatialTableFile)

* - description: Schl�ssel, nachdem die Datei gefiltert wird. Wichtig: Der Daten in der Datei m�ssen dem Konsumergruppennamen entsprechen.
* - identifier: Auswahlschl�ssel
* - type: Boolean
PARAMETER par_link_InRelativeExternConsumerAgentPopulationSize_InAttributeName_selectKey(set_InRelativeExternConsumerAgentPopulationSize,set_InAttributeName)

* - description: Bin�re Daten f�r diverse Funktionalit�ten
* - identifier: Bin�re Daten
* - type: String
SET set_VisibleBinaryData(set_InEntity)

* - description: Spezielle ID der Daten, Verwendungszweck und Funktionsweise ist von den Daten selber abh�ngig.
* - identifier: ID
* - type: Integer
PARAMETER par_VisibleBinaryData_idVisible(set_VisibleBinaryData)

* - description: Bin�re Daten f�r den Transfer zwischen zwei Simulationsschritten.
* - hidden: 1
* - identifier: BinaryPersistData
* - type: String
SET set_BinaryPersistData(*)

* - identifier: id
* - type: Integer
PARAMETER par_BinaryPersistData_id(set_BinaryPersistData)

* - identifier: InBooleanDistribution
* - type: String
SET set_InBooleanDistribution(set_InUnivariateDoubleDistribution,set_InEntity)

* - description: Ungenutzter Platzhalter
* - identifier: ---
* - type: Integer
PARAMETER par_InBooleanDistribution_placeholderBooleanDist(set_InBooleanDistribution)

* - identifier: InConstantUnivariateDistribution
* - type: String
SET set_InConstantUnivariateDistribution(set_InUnivariateDoubleDistribution,set_InEntity)

* - description: Legt den konstante R�ckgabewert fest.
* - identifier: Wert
* - type: Float
PARAMETER par_InConstantUnivariateDistribution_constDistValue(set_InConstantUnivariateDistribution)

* - identifier: InFiniteMassPointsDiscreteDistribution
* - type: String
SET set_InFiniteMassPointsDiscreteDistribution(set_InUnivariateDoubleDistribution,set_InEntity)

* - description: Legt die zu nutzenden Massepunkte fest.
* - identifier: Massepunkte
* - type: Boolean
PARAMETER par_link_InFiniteMassPointsDiscreteDistribution_InMassPoint_massPoints(set_InFiniteMassPointsDiscreteDistribution,set_InMassPoint)

* - identifier: InMassPoint
* - type: String
SET set_InMassPoint(set_InEntity)

* - description: Legt den Wert des Punktes fest.
* - identifier: Wert des Punktes
* - type: Float
PARAMETER par_InMassPoint_mpValue(set_InMassPoint)

* - description: Legt die Wichtung des Punktes fest.
* - identifier: Wichtung des Punktes
* - type: Float
PARAMETER par_InMassPoint_mpWeight(set_InMassPoint)

* - identifier: InRandomBoundedIntegerDistribution
* - type: String
SET set_InRandomBoundedIntegerDistribution(set_InUnivariateDoubleDistribution,set_InEntity)

* - description: Legt die Untergrenze fest.
* - identifier: Untergrenze (inklusiv)
* - type: Integer
PARAMETER par_InRandomBoundedIntegerDistribution_lowerBoundInt(set_InRandomBoundedIntegerDistribution)

* - description: Legt die Obergrenze fest.
* - identifier: Obergrenze (exklusiv)
* - type: Integer
PARAMETER par_InRandomBoundedIntegerDistribution_upperBoundInt(set_InRandomBoundedIntegerDistribution)

* - identifier: InUnivariateDoubleDistribution
* - hidden: 1
* - type: String
SET set_InUnivariateDoubleDistribution(set_InEntity)

* - identifier: InFile
* - hidden: 1
* - type: String
SET set_InFile(set_InEntity)

* - identifier: InPVFile
* - type: String
SET set_InPVFile(set_InFile,set_InEntity)

* - description: Ungenutzter Platzhalter
* - identifier: ---
* - type: Float
PARAMETER par_InPVFile_placeholderPVFile(set_InPVFile)

* - identifier: InSpatialTableFile
* - type: String
SET set_InSpatialTableFile(set_InFile,set_InEntity)

* - description: Ungenutzter Platzhalter
* - identifier: ---
* - type: Float
PARAMETER par_InSpatialTableFile_placeholderInSpatialFile(set_InSpatialTableFile)

* - identifier: InConsumerAgentGroupColor
* - type: String
SET set_InConsumerAgentGroupColor(*)

* - description: Legt die Gruppe fest
* - identifier: Konsumergruppe
* - type: Boolean
PARAMETER par_link_InConsumerAgentGroupColor_InConsumerAgentGroup_group(set_InConsumerAgentGroupColor,set_InConsumerAgentGroup)

* - description: Legt die Farbe fest
* - identifier: Farbe
* - type: Boolean
PARAMETER par_link_InConsumerAgentGroupColor_GraphvizColor_color(set_InConsumerAgentGroupColor,set_GraphvizColor)

* - identifier: InProductInterestSupplyScheme
* - hidden: 1
* - type: String
SET set_InProductInterestSupplyScheme(set_InEntity)

* - identifier: InProductThresholdInterestSupplyScheme
* - type: String
SET set_InProductThresholdInterestSupplyScheme(set_InProductInterestSupplyScheme,set_InEntity)

* - description: Grenzwert ab dem das Produkt von Interesse ist.
* - identifier: Grenzwert
* - type: Boolean
PARAMETER par_link_InProductThresholdInterestSupplyScheme_InUnivariateDoubleDistribution_interestDistribution(set_InProductThresholdInterestSupplyScheme,set_InUnivariateDoubleDistribution)

* - identifier: InCompleteGraphTopology
* - type: String
SET set_InCompleteGraphTopology(set_InGraphTopologyScheme,set_InEntity)

* - description: Legt das initiale Gewicht der Kanten fest.
* - identifier: Initiales Kantengewicht
* - type: Float
PARAMETER par_InCompleteGraphTopology_initialWeight(set_InCompleteGraphTopology)

* - identifier: InDistanceEvaluator
* - hidden: 1
* - type: String
SET set_InDistanceEvaluator(set_InEntity)

* - identifier: InFreeNetworkTopology
* - type: String
SET set_InFreeNetworkTopology(set_InGraphTopologyScheme,set_InEntity)

* - description: Legt die zu nutzende Distanzfunktion f�r den Einfluss des Abstandes fest.
* - identifier: Distanzfunktion
* - type: Boolean
PARAMETER par_link_InFreeNetworkTopology_InDistanceEvaluator_distanceEvaluator(set_InFreeNetworkTopology,set_InDistanceEvaluator)

* - description: Knotenanzahl
* - identifier: Knotenanzahl je KG
* - type: Boolean
PARAMETER par_link_InFreeNetworkTopology_InNumberOfTies_numberOfTies(set_InFreeNetworkTopology,set_InNumberOfTies)

* - description: Legt das initiale Gewicht der Kanten fest.
* - identifier: Initiales Kantengewicht
* - type: Float
PARAMETER par_InFreeNetworkTopology_initialWeight(set_InFreeNetworkTopology)

* - identifier: InGraphTopologyScheme
* - hidden: 1
* - type: String
SET set_InGraphTopologyScheme(set_InEntity)

* - identifier: InInverse
* - type: String
SET set_InInverse(set_InDistanceEvaluator,set_InEntity)

* - description: Ungenutzter Platzhalter
* - identifier: ---
* - type: Integer
PARAMETER par_InInverse_placeholderInverse(set_InInverse)

* - identifier: InNoDistance
* - type: String
SET set_InNoDistance(set_InDistanceEvaluator,set_InEntity)

* - description: Ungenutzter Platzhalter
* - identifier: ---
* - type: Integer
PARAMETER par_InNoDistance_placeholderNoDistance(set_InNoDistance)

* - identifier: InNumberOfTies
* - type: String
SET set_InNumberOfTies(set_InEntity)

* - description: Bestimmt die Konsumergruppen, welche diese Anzahl Kanten aufweisen sollen.
* - identifier: Konsumergruppen
* - type: Boolean
PARAMETER par_link_InNumberOfTies_InConsumerAgentGroup_cags(set_InNumberOfTies,set_InConsumerAgentGroup)

* - description: Legt die Anzahl der Kanten fest.
* - identifier: Kantenanzahl
* - type: Integer
PARAMETER par_InNumberOfTies_count(set_InNumberOfTies)

* - identifier: InUnlinkedGraphTopology
* - type: String
SET set_InUnlinkedGraphTopology(set_InGraphTopologyScheme,set_InEntity)

* - description: Ungenutzter Platzhalter
* - identifier: ---
* - type: Float
PARAMETER par_InUnlinkedGraphTopology_placeholderUnlinked(set_InUnlinkedGraphTopology)

* - identifier: InAutoUncertaintyGroupAttribute
* - type: String
SET set_InAutoUncertaintyGroupAttribute(set_InUncertaintyGroupAttribute,set_InEntity)

* - description: Ungenutzter Platzhalter
* - identifier: ---
* - type: Float
PARAMETER par_InAutoUncertaintyGroupAttribute_placeholderAutoUncert(set_InAutoUncertaintyGroupAttribute)

* - identifier: InIndividualAttributeBasedUncertaintyGroupAttribute
* - type: String
SET set_InIndividualAttributeBasedUncertaintyGroupAttribute(set_InUncertaintyGroupAttribute,set_InEntity)

* - description: Bestimmt die Konsumergruppenattribute, welche mit dieser Unsicherheit behandelt werden sollen.
* - identifier: Konsumergruppenattribute
* - type: Boolean
PARAMETER par_link_InIndividualAttributeBasedUncertaintyGroupAttribute_InConsumerAgentGroupAttribute_cagAttrs(set_InIndividualAttributeBasedUncertaintyGroupAttribute,set_InConsumerAgentGroupAttribute)

* - description: Legt die zu nutzende Verteilungsfunktion fest.
* - identifier: Verteilungsfunktion
* - type: Boolean
PARAMETER par_link_InIndividualAttributeBasedUncertaintyGroupAttribute_InUnivariateDoubleDistribution_uncertDist(set_InIndividualAttributeBasedUncertaintyGroupAttribute,set_InUnivariateDoubleDistribution)

* - identifier: InIndividualAttributeBasedUncertaintyWithConvergenceGroupAttribute
* - type: String
SET set_InIndividualAttributeBasedUncertaintyWithConvergenceGroupAttribute(set_InUncertaintyGroupAttribute,set_InEntity)

* - description: Bestimmt die Konsumergruppenattribute, welche mit dieser Unsicherheit behandelt werden sollen.
* - identifier: Konsumergruppenattribute
* - type: Boolean
PARAMETER par_link_InIndividualAttributeBasedUncertaintyWithConvergenceGroupAttribute_InConsumerAgentGroupAttribute_cagAttrs(set_InIndividualAttributeBasedUncertaintyWithConvergenceGroupAttribute,set_InConsumerAgentGroupAttribute)

* - description: Legt die zu nutzende Verteilungsfunktion fest.
* - identifier: Verteilungsfunktion
* - type: Boolean
PARAMETER par_link_InIndividualAttributeBasedUncertaintyWithConvergenceGroupAttribute_InUnivariateDoubleDistribution_uncertDist(set_InIndividualAttributeBasedUncertaintyWithConvergenceGroupAttribute,set_InUnivariateDoubleDistribution)

* - description: Legt die Verteilungsfunktion f�r die Konvergenz fest.
* - identifier: Konvergenzfunktion
* - type: Boolean
PARAMETER par_link_InIndividualAttributeBasedUncertaintyWithConvergenceGroupAttribute_InUnivariateDoubleDistribution_convergenceDist(set_InIndividualAttributeBasedUncertaintyWithConvergenceGroupAttribute,set_InUnivariateDoubleDistribution)

* - identifier: InNameBasedUncertaintyGroupAttribute
* - type: String
SET set_InNameBasedUncertaintyGroupAttribute(set_InUncertaintyGroupAttribute,set_InEntity)

* - description: Bestimmt die Konsumergruppe, deren Attribute mit dieser Unsicherheit behandelt werden sollen.
* - identifier: Konsumergruppe
* - type: Boolean
PARAMETER par_link_InNameBasedUncertaintyGroupAttribute_InConsumerAgentGroup_cags(set_InNameBasedUncertaintyGroupAttribute,set_InConsumerAgentGroup)

* - description: Legt die Attributnamen fest, welche mit dieser Unsicherheit behandelt werden sollen.
* - identifier: Attributnamen
* - type: Boolean
PARAMETER par_link_InNameBasedUncertaintyGroupAttribute_InAttributeName_names(set_InNameBasedUncertaintyGroupAttribute,set_InAttributeName)

* - description: Legt die zu nutzende Verteilungsfunktion fest.
* - identifier: Verteilungsfunktion
* - type: Boolean
PARAMETER par_link_InNameBasedUncertaintyGroupAttribute_InUnivariateDoubleDistribution_uncertDist(set_InNameBasedUncertaintyGroupAttribute,set_InUnivariateDoubleDistribution)

* - identifier: InNameBasedUncertaintyWithConvergenceGroupAttribute
* - type: String
SET set_InNameBasedUncertaintyWithConvergenceGroupAttribute(set_InUncertaintyGroupAttribute,set_InEntity)

* - description: Bestimmt die Konsumergruppe, deren Attribute mit dieser Unsicherheit behandelt werden sollen.
* - identifier: Konsumergruppe
* - type: Boolean
PARAMETER par_link_InNameBasedUncertaintyWithConvergenceGroupAttribute_InConsumerAgentGroup_cags(set_InNameBasedUncertaintyWithConvergenceGroupAttribute,set_InConsumerAgentGroup)

* - description: Legt die Attributnamen fest, welche mit dieser Unsicherheit behandelt werden sollen.
* - identifier: Attributnamen
* - type: Boolean
PARAMETER par_link_InNameBasedUncertaintyWithConvergenceGroupAttribute_InAttributeName_names(set_InNameBasedUncertaintyWithConvergenceGroupAttribute,set_InAttributeName)

* - description: Legt die zu nutzende Verteilungsfunktion fest.
* - identifier: Verteilungsfunktion
* - type: Boolean
PARAMETER par_link_InNameBasedUncertaintyWithConvergenceGroupAttribute_InUnivariateDoubleDistribution_uncertDist(set_InNameBasedUncertaintyWithConvergenceGroupAttribute,set_InUnivariateDoubleDistribution)

* - description: Legt die Verteilungsfunktion f�r die Konvergenz fest.
* - identifier: Konvergenzfunktion
* - type: Boolean
PARAMETER par_link_InNameBasedUncertaintyWithConvergenceGroupAttribute_InUnivariateDoubleDistribution_convergenceDist(set_InNameBasedUncertaintyWithConvergenceGroupAttribute,set_InUnivariateDoubleDistribution)

* - identifier: InPVactUncertaintyGroupAttribute
* - type: String
SET set_InPVactUncertaintyGroupAttribute(set_InUncertaintyGroupAttribute,set_InEntity)

* - description: Bestimmt die Konsumergruppe, deren Attribute mit dieser Unsicherheit behandelt werden sollen.
* - identifier: Konsumergruppe
* - type: Boolean
PARAMETER par_link_InPVactUncertaintyGroupAttribute_InConsumerAgentGroup_cags(set_InPVactUncertaintyGroupAttribute,set_InConsumerAgentGroup)

* - description: Legt die zu nutzende Verteilungsfunktion f�r novelty seeking fest.
* - identifier: novelty seeking
* - type: Boolean
PARAMETER par_link_InPVactUncertaintyGroupAttribute_InUnivariateDoubleDistribution_noveltySeekingUncert(set_InPVactUncertaintyGroupAttribute,set_InUnivariateDoubleDistribution)

* - description: Legt die zu nutzende Verteilungsfunktion f�r dependent judgment making fest.
* - identifier: dependent judgment making
* - type: Boolean
PARAMETER par_link_InPVactUncertaintyGroupAttribute_InUnivariateDoubleDistribution_dependentJudgmentMakingUncert(set_InPVactUncertaintyGroupAttribute,set_InUnivariateDoubleDistribution)

* - description: Legt die zu nutzende Verteilungsfunktion f�r environmental concern fest.
* - identifier: environmental concern
* - type: Boolean
PARAMETER par_link_InPVactUncertaintyGroupAttribute_InUnivariateDoubleDistribution_environmentalConcernUncert(set_InPVactUncertaintyGroupAttribute,set_InUnivariateDoubleDistribution)

* - identifier: InRAProcessModel
* - type: String
SET set_InRAProcessModel(set_InProcessModel,set_InEntity)

* - description: Legt den Einfluss der finanziellen Komponente fest.
* - identifier: Einfluss finanzielle Komponente (a)
* - type: Float
PARAMETER par_InRAProcessModel_a(set_InRAProcessModel)

* - description: Legt den Einfluss der novelty Komponente fest.
* - identifier: Einfluss novelty Komponente (b)
* - type: Float
PARAMETER par_InRAProcessModel_b(set_InRAProcessModel)

* - description: Legt den Umwelteinfluss fest.
* - identifier: Umwelteinfluss (c)
* - type: Float
PARAMETER par_InRAProcessModel_c(set_InRAProcessModel)

* - description: Legt den Einfluss der sozialen Komponente fest.
* - identifier: Einfluss soziale Komponente (d)
* - type: Float
PARAMETER par_InRAProcessModel_d(set_InRAProcessModel)

* - description: Legt den Einfluss f�r die Kommunikation mit Adoptern fest.
* - identifier: Einfluss der Kommunikation mit Adoptern
* - type: Integer
PARAMETER par_InRAProcessModel_adopterPoints(set_InRAProcessModel)

* - description: Legt den Einfluss f�r die Kommunikation mit Interessenten fest.
* - identifier: Einfluss der Kommunikation mit Interessenten
* - type: Integer
PARAMETER par_InRAProcessModel_interestedPoints(set_InRAProcessModel)

* - description: Legt den Einfluss f�r die Kommunikation mit Bewussten fest.
* - identifier: Einfluss der Kommunikation mit Bewussten
* - type: Integer
PARAMETER par_InRAProcessModel_awarePoints(set_InRAProcessModel)

* - description: Legt den Einfluss f�r die Kommunikation mit Unwissenden fest.
* - identifier: Einfluss der Kommunikation mit Unwissenden
* - type: Integer
PARAMETER par_InRAProcessModel_unknownPoints(set_InRAProcessModel)

* - description: Legt den Einflussfaktor 'a' der logistischen Funktion fest: logistic(a * -x).
* - identifier: Einflussfaktor der logistischen Funktion
* - type: Float
PARAMETER par_InRAProcessModel_logisticFactor(set_InRAProcessModel)

* - description: Legt die zu nutzende PV-Datei fest.
* - identifier: PV-Datei
* - type: Boolean
PARAMETER par_link_InRAProcessModel_InPVFile_pvFile(set_InRAProcessModel,set_InPVFile)

* - description: Bestimmt die Unsicherheiten, welche von diesem Modell verwendet werden.
* - identifier: Unsicherheiten
* - type: Boolean
PARAMETER par_link_InRAProcessModel_InUncertaintyGroupAttribute_uncertaintyGroupAttributes(set_InRAProcessModel,set_InUncertaintyGroupAttribute)

* - identifier: InUncertaintyGroupAttribute
* - hidden: 1
* - type: String
SET set_InUncertaintyGroupAttribute(set_InEntity)

* - identifier: InProcessModel
* - hidden: 1
* - type: String
SET set_InProcessModel(set_InEntity)

* - identifier: InBasicProductGroup
* - type: String
SET set_InBasicProductGroup(set_InProductGroup,set_InEntity)

* - description: Bestimmt die Attribute f�r diese Gruppe.
* - identifier: Attribute
* - type: Boolean
PARAMETER par_link_InBasicProductGroup_InDependentProductGroupAttribute_pgAttributes(set_InBasicProductGroup,set_InDependentProductGroupAttribute)

* - identifier: InBasicProductGroupAttribute
* - type: String
SET set_InBasicProductGroupAttribute(set_InDependentProductGroupAttribute,set_InProductGroupAttribute,set_InEntity)

* - description: Legt die Attributnamen fest.
* - identifier: Attributname
* - type: Boolean
PARAMETER par_link_InBasicProductGroupAttribute_InAttributeName_attrName(set_InBasicProductGroupAttribute,set_InAttributeName)

* - description: Legt die Verteilungsfunktion fest, auf deren Basis die Attributwerte gezogen werden.
* - identifier: Verteilungsfunktion
* - type: Boolean
PARAMETER par_link_InBasicProductGroupAttribute_InUnivariateDoubleDistribution_dist(set_InBasicProductGroupAttribute,set_InUnivariateDoubleDistribution)

* - identifier: InDependentProductGroupAttribute
* - hidden: 1
* - type: String
SET set_InDependentProductGroupAttribute(set_InProductGroupAttribute,set_InEntity)

* - identifier: InFixProduct
* - type: String
SET set_InFixProduct(set_InEntity)

* - description: Legt die Produktgruppe fest, zu der dieses Produkt geh�ren soll.
* - identifier: Produktgruppe
* - type: Boolean
PARAMETER par_link_InFixProduct_InProductGroup_refPG(set_InFixProduct,set_InProductGroup)

* - description: Bestimmt die Attribute f�r dieses Fixprodukt.
* - identifier: Attribute
* - type: Boolean
PARAMETER par_link_InFixProduct_InFixProductAttribute_fixPAttrs(set_InFixProduct,set_InFixProductAttribute)

* - identifier: InFixProductAttribute
* - type: String
SET set_InFixProductAttribute(set_InEntity)

* - description: Legt das Produktgruppenattribut fest, zu dem dieses Produkt geh�ren soll.
* - identifier: Produktgruppenattribut
* - type: Boolean
PARAMETER par_link_InFixProductAttribute_InProductGroupAttribute_refPGA(set_InFixProductAttribute,set_InProductGroupAttribute)

* - description: Bestimmt die Wert f�r dieses Attribut.
* - identifier: Wert
* - type: Float
PARAMETER par_InFixProductAttribute_fixPAvalue(set_InFixProductAttribute)

* - identifier: InFixProductFindingScheme
* - type: String
SET set_InFixProductFindingScheme(set_InProductFindingScheme,set_InEntity)

* - description: Legt das Fixprodukt fest, welches von diesem Schema verwendet werden soll.
* - identifier: Fixprodukt
* - type: Boolean
PARAMETER par_link_InFixProductFindingScheme_InFixProduct_refFixProduct(set_InFixProductFindingScheme,set_InFixProduct)

* - identifier: InIndependentProductGroupAttribute
* - hidden: 1
* - type: String
SET set_InIndependentProductGroupAttribute(set_InProductGroupAttribute,set_InEntity)

* - identifier: InNameSplitProductGroupAttribute
* - type: String
SET set_InNameSplitProductGroupAttribute(set_InIndependentProductGroupAttribute,set_InProductGroupAttribute,set_InEntity)

* - description: Legt die Verteilungsfunktion fest, auf deren Basis die Attributwerte gezogen werden.
* - identifier: Verteilungsfunktion
* - type: Boolean
PARAMETER par_link_InNameSplitProductGroupAttribute_InUnivariateDoubleDistribution_dist(set_InNameSplitProductGroupAttribute,set_InUnivariateDoubleDistribution)

* - identifier: InProductFindingScheme
* - hidden: 1
* - type: String
SET set_InProductFindingScheme(set_InEntity)

* - identifier: InProductGroup
* - hidden: 1
* - type: String
SET set_InProductGroup(set_InEntity)

* - identifier: InProductGroupAttribute
* - hidden: 1
* - type: String
SET set_InProductGroupAttribute(set_InEntity)

* - identifier: InCustomFileSelectedGroupedSpatialDistribution2D
* - type: String
SET set_InCustomFileSelectedGroupedSpatialDistribution2D(set_InSpatialDistribution,set_InEntity)

* - description: Legt die Verteilungsfunktion fest, welche die X-Position bestimmt.
* - identifier: X-Position
* - type: Boolean
PARAMETER par_link_InCustomFileSelectedGroupedSpatialDistribution2D_InUnivariateDoubleDistribution_xPosSupplier(set_InCustomFileSelectedGroupedSpatialDistribution2D,set_InUnivariateDoubleDistribution)

* - description: Legt die Verteilungsfunktion fest, welche die Y-Position bestimmt.
* - identifier: Y-Position
* - type: Boolean
PARAMETER par_link_InCustomFileSelectedGroupedSpatialDistribution2D_InUnivariateDoubleDistribution_yPosSupplier(set_InCustomFileSelectedGroupedSpatialDistribution2D,set_InUnivariateDoubleDistribution)

* - identifier: file
* - type: Boolean
PARAMETER par_link_InCustomFileSelectedGroupedSpatialDistribution2D_InSpatialTableFile_file(set_InCustomFileSelectedGroupedSpatialDistribution2D,set_InSpatialTableFile)

* - description: Bestimmt den Schl�ssel f�r die Auswahl der Milieuinformation in der tabellarischen Datei (Spaltenname).
* - identifier: Auswahlschl�ssel
* - type: Boolean
PARAMETER par_link_InCustomFileSelectedGroupedSpatialDistribution2D_InAttributeName_selectKey(set_InCustomFileSelectedGroupedSpatialDistribution2D,set_InAttributeName)

* - description: Bestimmt den Schl�ssel f�r die Gruppierung und Wichtung anhand der tabellarischen Datei (Spaltenname).
* - identifier: Gruppierungsschl�ssel
* - type: Boolean
PARAMETER par_link_InCustomFileSelectedGroupedSpatialDistribution2D_InAttributeName_groupKey(set_InCustomFileSelectedGroupedSpatialDistribution2D,set_InAttributeName)

* - identifier: InCustomFileSelectedSpatialDistribution2D
* - type: String
SET set_InCustomFileSelectedSpatialDistribution2D(set_InSpatialDistribution,set_InEntity)

* - description: Legt die Verteilungsfunktion fest, welche die X-Position bestimmt.
* - identifier: X-Position
* - type: Boolean
PARAMETER par_link_InCustomFileSelectedSpatialDistribution2D_InUnivariateDoubleDistribution_xPosSupplier(set_InCustomFileSelectedSpatialDistribution2D,set_InUnivariateDoubleDistribution)

* - description: Legt die Verteilungsfunktion fest, welche die Y-Position bestimmt.
* - identifier: Y-Position
* - type: Boolean
PARAMETER par_link_InCustomFileSelectedSpatialDistribution2D_InUnivariateDoubleDistribution_yPosSupplier(set_InCustomFileSelectedSpatialDistribution2D,set_InUnivariateDoubleDistribution)

* - description: Legt die Datei fest, aus der die r�umlichen Informationen gelesen werden sollen.
* - identifier: Tabellarische Datei
* - type: Boolean
PARAMETER par_link_InCustomFileSelectedSpatialDistribution2D_InSpatialTableFile_attrFile(set_InCustomFileSelectedSpatialDistribution2D,set_InSpatialTableFile)

* - description: Bestimmt den Schl�ssel f�r die Auswahl der Milieuinformation in der tabellarischen Datei (Spaltenname).
* - identifier: Auswahlschl�ssel
* - type: Boolean
PARAMETER par_link_InCustomFileSelectedSpatialDistribution2D_InAttributeName_selectKey(set_InCustomFileSelectedSpatialDistribution2D,set_InAttributeName)

* - identifier: InCustomFileSpatialDistribution2D
* - type: String
SET set_InCustomFileSpatialDistribution2D(set_InSpatialDistribution,set_InEntity)

* - description: Legt die Verteilungsfunktion fest, welche die X-Position bestimmt.
* - identifier: X-Position
* - type: Boolean
PARAMETER par_link_InCustomFileSpatialDistribution2D_InUnivariateDoubleDistribution_xPosSupplier(set_InCustomFileSpatialDistribution2D,set_InUnivariateDoubleDistribution)

* - description: Legt die Verteilungsfunktion fest, welche die Y-Position bestimmt.
* - identifier: Y-Position
* - type: Boolean
PARAMETER par_link_InCustomFileSpatialDistribution2D_InUnivariateDoubleDistribution_yPosSupplier(set_InCustomFileSpatialDistribution2D,set_InUnivariateDoubleDistribution)

* - description: Legt die Datei fest, aus der die r�umlichen Informationen gelesen werden sollen.
* - identifier: Tabellarische Datei
* - type: Boolean
PARAMETER par_link_InCustomFileSpatialDistribution2D_InSpatialTableFile_attrFile(set_InCustomFileSpatialDistribution2D,set_InSpatialTableFile)

* - identifier: InFileSelectedGroupedSpatialDistribution2D
* - type: String
SET set_InFileSelectedGroupedSpatialDistribution2D(set_InSpatialDistribution,set_InEntity)

* - description: Bestimmt den Schl�ssel f�r die Auswahl der X-Position in der tabellarischen Datei (Spaltenname).
* - identifier: X-Schl�ssel
* - type: Boolean
PARAMETER par_link_InFileSelectedGroupedSpatialDistribution2D_InAttributeName_xPositionKey(set_InFileSelectedGroupedSpatialDistribution2D,set_InAttributeName)

* - description: Bestimmt den Schl�ssel f�r die Auswahl der Y-Position in der tabellarischen Datei (Spaltenname).
* - identifier: Y-Schl�ssel
* - type: Boolean
PARAMETER par_link_InFileSelectedGroupedSpatialDistribution2D_InAttributeName_yPositionKey(set_InFileSelectedGroupedSpatialDistribution2D,set_InAttributeName)

* - identifier: file
* - type: Boolean
PARAMETER par_link_InFileSelectedGroupedSpatialDistribution2D_InSpatialTableFile_file(set_InFileSelectedGroupedSpatialDistribution2D,set_InSpatialTableFile)

* - description: Bestimmt den Schl�ssel f�r die Auswahl der Milieuinformation in der tabellarischen Datei (Spaltenname).
* - identifier: Auswahlschl�ssel
* - type: Boolean
PARAMETER par_link_InFileSelectedGroupedSpatialDistribution2D_InAttributeName_selectKey(set_InFileSelectedGroupedSpatialDistribution2D,set_InAttributeName)

* - description: Bestimmt den Schl�ssel f�r die Gruppierung und Wichtung anhand der tabellarischen Datei (Spaltenname).
* - identifier: Gruppierungsschl�ssel
* - type: Boolean
PARAMETER par_link_InFileSelectedGroupedSpatialDistribution2D_InAttributeName_groupKey(set_InFileSelectedGroupedSpatialDistribution2D,set_InAttributeName)

* - identifier: InFileSelectedSpatialDistribution2D
* - type: String
SET set_InFileSelectedSpatialDistribution2D(set_InSpatialDistribution,set_InEntity)

* - description: Bestimmt den Schl�ssel f�r die Auswahl der X-Position in der tabellarischen Datei (Spaltenname).
* - identifier: X-Schl�ssel
* - type: Boolean
PARAMETER par_link_InFileSelectedSpatialDistribution2D_InAttributeName_xPositionKey(set_InFileSelectedSpatialDistribution2D,set_InAttributeName)

* - description: Bestimmt den Schl�ssel f�r die Auswahl der Y-Position in der tabellarischen Datei (Spaltenname).
* - identifier: Y-Schl�ssel
* - type: Boolean
PARAMETER par_link_InFileSelectedSpatialDistribution2D_InAttributeName_yPositionKey(set_InFileSelectedSpatialDistribution2D,set_InAttributeName)

* - description: Legt die Datei fest, aus der die r�umlichen Informationen gelesen werden sollen.
* - identifier: Tabellarische Datei
* - type: Boolean
PARAMETER par_link_InFileSelectedSpatialDistribution2D_InSpatialTableFile_attrFile(set_InFileSelectedSpatialDistribution2D,set_InSpatialTableFile)

* - description: Bestimmt den Schl�ssel f�r die Auswahl der Milieuinformation in der tabellarischen Datei (Spaltenname).
* - identifier: Auswahlschl�ssel
* - type: Boolean
PARAMETER par_link_InFileSelectedSpatialDistribution2D_InAttributeName_selectKey(set_InFileSelectedSpatialDistribution2D,set_InAttributeName)

* - identifier: InFileSpatialDistribution2D
* - type: String
SET set_InFileSpatialDistribution2D(set_InSpatialDistribution,set_InEntity)

* - description: Bestimmt den Schl�ssel f�r die Auswahl der X-Position in der tabellarischen Datei (Spaltenname).
* - identifier: X-Schl�ssel
* - type: Boolean
PARAMETER par_link_InFileSpatialDistribution2D_InAttributeName_xPositionKey(set_InFileSpatialDistribution2D,set_InAttributeName)

* - description: Bestimmt den Schl�ssel f�r die Auswahl der Y-Position in der tabellarischen Datei (Spaltenname).
* - identifier: Y-Schl�ssel
* - type: Boolean
PARAMETER par_link_InFileSpatialDistribution2D_InAttributeName_yPositionKey(set_InFileSpatialDistribution2D,set_InAttributeName)

* - description: Legt die Datei fest, aus der die r�umlichen Informationen gelesen werden sollen.
* - identifier: Tabellarische Datei
* - type: Boolean
PARAMETER par_link_InFileSpatialDistribution2D_InSpatialTableFile_attrFile(set_InFileSpatialDistribution2D,set_InSpatialTableFile)

* - identifier: InSpatialDistribution
* - hidden: 1
* - type: String
SET set_InSpatialDistribution(set_InEntity)

* - identifier: InSpace2D
* - type: String
SET set_InSpace2D(set_InSpatialModel,set_InEntity)

* - description: Nutzt die Manhattenmetrik f�r die Berechnung der Abst�nde.
* - identifier: Manhattenmetrik
* - type: Boolean
PARAMETER par_InSpace2D_useManhatten(set_InSpace2D)

* - description: Nutzt die euklidische Metrik f�r die Berechnung der Abst�nde.
* - identifier: Euklidische Metrik
* - type: Boolean
PARAMETER par_InSpace2D_useEuclid(set_InSpace2D)

* - description: Nutzt die Maximumsmetrik f�r die Berechnung der Abst�nde.
* - identifier: Maximumsmetrik
* - type: Boolean
PARAMETER par_InSpace2D_useMaximum(set_InSpace2D)

* - description: Nutzt die Haversine-Formel f�r die Berechnung der Entfernungen. Die Berechnungen finden auf Kilometerbasis statt.
* - identifier: Haversine (km)
* - type: Boolean
PARAMETER par_InSpace2D_useHaversine(set_InSpace2D)

* - identifier: InSpatialModel
* - hidden: 1
* - type: String
SET set_InSpatialModel(set_InEntity)

* - identifier: InDiscreteTimeModel
* - type: String
SET set_InDiscreteTimeModel(set_InTimeModel,set_InEntity)

* - description: Legt die Zeit fest, welche pro diskreten Schritt vergeht. Der Wert muss in Millisekunden angegeben werden.
* - identifier: Zeit pro Schritt
* - unit: [ms]
* - type: Integer
PARAMETER par_InDiscreteTimeModel_timePerTickInMs(set_InDiscreteTimeModel)

* - identifier: InTimeModel
* - hidden: 1
* - type: String
SET set_InTimeModel(set_InEntity)

* - identifier: InUnitStepDiscreteTimeModel
* - type: String
SET set_InUnitStepDiscreteTimeModel(set_InTimeModel,set_InEntity)

* - description: Legt die Zeitdauer fest, welche pro diskreten Schritt vergeht.
* - identifier: Zeitdauer
* - type: Integer
PARAMETER par_InUnitStepDiscreteTimeModel_amountOfTime(set_InUnitStepDiscreteTimeModel)

* - description: Verwendet Millisekunden alsEinheit f�r die Zeitdauer.
* - identifier: Millisekunden
* - type: Boolean
PARAMETER par_InUnitStepDiscreteTimeModel_useMs(set_InUnitStepDiscreteTimeModel)

* - description: Verwendet Sekunden alsEinheit f�r die Zeitdauer.
* - identifier: Sekunden
* - type: Boolean
PARAMETER par_InUnitStepDiscreteTimeModel_useSec(set_InUnitStepDiscreteTimeModel)

* - description: Verwendet Minuten alsEinheit f�r die Zeitdauer.
* - identifier: Minuten
* - type: Boolean
PARAMETER par_InUnitStepDiscreteTimeModel_useMin(set_InUnitStepDiscreteTimeModel)

* - description: Verwendet Stunden alsEinheit f�r die Zeitdauer.
* - identifier: Stunden
* - type: Boolean
PARAMETER par_InUnitStepDiscreteTimeModel_useH(set_InUnitStepDiscreteTimeModel)

* - description: Verwendet Tage alsEinheit f�r die Zeitdauer.
* - identifier: Tage
* - type: Boolean
PARAMETER par_InUnitStepDiscreteTimeModel_useD(set_InUnitStepDiscreteTimeModel)

* - description: Verwendet Wochen alsEinheit f�r die Zeitdauer.
* - identifier: Wochen
* - type: Boolean
PARAMETER par_InUnitStepDiscreteTimeModel_useW(set_InUnitStepDiscreteTimeModel)

* - description: Verwendet Monate alsEinheit f�r die Zeitdauer.
* - identifier: Monate
* - type: Boolean
PARAMETER par_InUnitStepDiscreteTimeModel_useM(set_InUnitStepDiscreteTimeModel)

* - identifier: InAttributeName
* - type: String
SET set_InAttributeName(set_InEntity)

* - description: Ungenutzter Platzhalter
* - identifier: ---
* - type: Integer
PARAMETER par_InAttributeName_placeholder(set_InAttributeName)

* - identifier: InEntity
* - hidden: 1
* - type: String
SET set_InEntity(*)

* - description: Setzt den Seed f�r den Zufallsgenerator der Simulation. Falls er auf -1 gesetzt wird, wird ein zuf�lliger Seed generiert.
* - identifier: Zufallsgenerator (seed)
* - type: Integer
SCALAR sca_InGeneral_seed

* - description: Setzt den Timeout der Simulation in Millisekunden. Diese Einstellung dient dazu die Simulation zu beenden, falls sie unerwartet abst�rzt. Im Laufe der Simulation wird der Timeout unentwegt zur�ck gesetzt. Sollte es zu einem unerwarteten Fehler kommen und die R�cksetzung ausbleiben, wird die Simulation abgebrochen und beendet. Werte kleiner 1 deaktivieren den Timeout vollst�ndig.
* - identifier: Timeout
* - unit: [ms]
* - type: Integer
SCALAR sca_InGeneral_timeout

* - description: Setzt das finale Jahr der Simulation. Wichtig: Der Wert ist inklusiv. Es wird dabei immer mindestens ein Jahr simuliert, auch wenn der Wert kleiner ist als das des Ausgangsjahres.
* - identifier: [Testoption] Endjahr der Simulation
* - type: Integer
SCALAR sca_InGeneral_endYear

* - domain: [0|1]
* - description: Falls gesetzt, werden automatisch spezielle Einstellungen f�r PVact bei der Initialisierung erg�nzt. Dies betrifft aktuell Produkte und die Prozessmodellzuordnung.
* - identifier: PVact
* - type: Boolean
SCALAR sca_InGeneral_runPVAct

* - domain: [0|1]
* - description: Falls gesetzt, wird die optact-Demo gestartet. Anderenfalls wird IRPact gestartet.
* - identifier: [Testoption] optact-Testmodell ausf�hren
* - type: Boolean
SCALAR sca_InGeneral_runOptActDemo

* - domain: [0,6]
* - description: Setzt das zu nutzende Logging-Level in IRPact, folgende Werte werden unterst�tzt: 0 = OFF, 1 = TRACE, 2 = DEBUG, 3 = INFO, 4 = WARN, 5 = ERROR, 6 = ALL. Das Level ist der Hauptfilter f�r alle log-Operationen.
* - identifier: Logging-Level
* - type: Integer
SCALAR sca_InGeneral_logLevel

* - domain: [0|1]
* - description: [SPAM] Ob alles geloggt werden soll. Falls ja, �berschreibt diese Option alles.
* - identifier: Alles loggen?
* - type: Boolean
SCALAR sca_InGeneral_logAll

* - domain: [0|1]
* - description: [SPAM] Ob alle Aufrufe der Tools-Bibliothek geloggt werden sollen.
* - identifier: Tools-Bibliothek loggen?
* - type: Boolean
SCALAR sca_InGeneral_logAllTools

* - domain: [0|1]
* - description: [SPAM] logToolsCore
* - identifier: logToolsCore
* - type: Boolean
SCALAR sca_InGeneral_logToolsCore

* - domain: [0|1]
* - description: [SPAM] logToolsDefinition
* - identifier: logToolsDefinition
* - type: Boolean
SCALAR sca_InGeneral_logToolsDefinition

* - domain: [0|1]
* - description: logInitialization
* - identifier: logInitialization
* - type: Boolean
SCALAR sca_InGeneral_logInitialization

* - domain: [0|1]
* - description: [SPAM] logParamInit
* - identifier: logParamInit
* - type: Boolean
SCALAR sca_InGeneral_logParamInit

* - domain: [0|1]
* - description: [SPAM] logGraphCreation
* - identifier: logGraphCreation
* - type: Boolean
SCALAR sca_InGeneral_logGraphCreation

* - domain: [0|1]
* - description: [SPAM] logAgentCreation
* - identifier: logAgentCreation
* - type: Boolean
SCALAR sca_InGeneral_logAgentCreation

* - domain: [0|1]
* - description: [SPAM] logPlatformCreation
* - identifier: logPlatformCreation
* - type: Boolean
SCALAR sca_InGeneral_logPlatformCreation

* - domain: [0|1]
* - description: [SPAM] logSimulationLifecycle
* - identifier: logSimulationLifecycle
* - type: Boolean
SCALAR sca_InGeneral_logSimulationLifecycle

* - domain: [0|1]
* - description: [SPAM] logSimulationAgent
* - identifier: logSimulationAgent
* - type: Boolean
SCALAR sca_InGeneral_logSimulationAgent

* - domain: [0|1]
* - description: [SPAM] logJadexSystemOut
* - identifier: logJadexSystemOut
* - type: Boolean
SCALAR sca_InGeneral_logJadexSystemOut

* - description: Die Version von IRPact, welche bei der Erstellung des Szenarios aktuell war.
* - hidden: 1
* - identifier: IRPact Version
* - type: String
SET set_InVersion(set_InEntity)

* - description: Ungenutzter Platzhalter
* - identifier: ---
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
* - description: Bitte geben Sie hier an, wie die Technologiekosten welchem Sektor (Strom, W�rme, Reserve etc.) (anteilig) zugeordnet werden sollen.
* - identifier: Sektorzuweisung der Technologiekosten
* - type: Float
PARAMETER par_SOH_pss_sector(set_sector,set_pss)

* - default: 0
* - description: Bitte geben Sie hier die an die verschiedenen Akteure (Netz-, Politik- und Vertriebsseite) zu zahlenden Arbeitstarife f�r den Strom-Netzbezug an
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
* - description: Bitte geben Sie hier ein gew�nschtes elektrische Lastprofil ein
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
* - description: Erh�ht die Anzahl der Kunden in der Gruppe um den gew�nschten Wert.
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
* - description: Bitte tragen Sie hier die spezifische F�rderung der �ffentlichen Hand f�r Stromspeicher ein
* - identifier: F�rderung f�r Stromspeicher durch Politik
* - unit: [EUR / MWh]
* - type: Float
PARAMETER par_Inc_PS_ES(set_tech_DES_ES)

* - description: PV-Anlage
* - identifier: PV-Anlage
* - type: String
SET set_tech_DES_PV(set_tech_DEGEN)

* - default: 0
* - domain: [0,)
* - description: Bitte geben Sie hier die gesamte Modulfl�che der PV-Anlage an
* - identifier: Modulfl�che PV-Anlage
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

* - description: Farbe, welche diese Gruppe im Graphen haben soll. Wichtig: es wird nur der erste Wert verwendet! Falls keine Farbe gew�hlt wird, ist die Farbe schwarz.
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

* - description: Soll diese Topology verwendet werden? Hinweis: es wird nur die erste g�ltige genutzt! Der Rest wird ignoriert!
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

* - description: Soll diese Topology verwendet werden? Hinweis: es wird nur die erste g�ltige genutzt! Der Rest wird ignoriert!
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
