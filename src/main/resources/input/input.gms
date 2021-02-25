* - identifier: InAffinityEntry
* - type: String
SET set_InAffinityEntry(set_InEntity)

* - identifier: srcCag
* - type: Boolean
PARAMETER par_link_InAffinityEntry_InConsumerAgentGroup_srcCag(set_InAffinityEntry,set_InConsumerAgentGroup)

* - identifier: tarCag
* - type: Boolean
PARAMETER par_link_InAffinityEntry_InConsumerAgentGroup_tarCag(set_InAffinityEntry,set_InConsumerAgentGroup)

* - identifier: affinityValue
* - type: Float
PARAMETER par_InAffinityEntry_affinityValue(set_InAffinityEntry)

* - identifier: InConsumerAgentGroup
* - type: String
SET set_InConsumerAgentGroup(set_InEntity)

* - description: Attribute
* - identifier: Attribute der KG
* - type: Boolean
PARAMETER par_link_InConsumerAgentGroup_InConsumerAgentGroupAttribute_cagAttributes(set_InConsumerAgentGroup,set_InConsumerAgentGroupAttribute)

* - description: genutzte Awareness
* - identifier: Awareness der KG
* - type: Boolean
PARAMETER par_link_InConsumerAgentGroup_InProductAwarenessSupplyScheme_cagAwareness(set_InConsumerAgentGroup,set_InProductAwarenessSupplyScheme)

* - description: Legt das Schema für das finden von passenden Produkten fest
* - identifier: Schema für die Produktfindung
* - type: Boolean
PARAMETER par_link_InConsumerAgentGroup_InProductFindingScheme_productFindingSchemes(set_InConsumerAgentGroup,set_InProductFindingScheme)

* - description: Legt die Verteilungsfunktion für diese Gruppe fest
* - identifier: Räumliche Verteilungsfunktion
* - type: Boolean
PARAMETER par_link_InConsumerAgentGroup_InSpatialDistribution_spatialDistribution(set_InConsumerAgentGroup,set_InSpatialDistribution)

* - description: informationAuthority
* - identifier: [ungenutzt] informationAuthority
* - type: Float
PARAMETER par_InConsumerAgentGroup_informationAuthority(set_InConsumerAgentGroup)

* - description: Anzahl der Agenten in der Konsumergruppe
* - identifier: Agenten in der KG
* - type: Integer
PARAMETER par_InConsumerAgentGroup_numberOfAgentsX(set_InConsumerAgentGroup)

* - identifier: InConsumerAgentGroupAttribute
* - type: String
SET set_InConsumerAgentGroupAttribute(set_InEntity)

* - description: Attributname
* - identifier: Name des KG-Attributes
* - type: Boolean
PARAMETER par_link_InConsumerAgentGroupAttribute_InAttributeName_cagAttrName(set_InConsumerAgentGroupAttribute,set_InAttributeName)

* - description: genutzte Funktion
* - identifier: Verteilungsfunktion des KG-Attributes
* - type: Boolean
PARAMETER par_link_InConsumerAgentGroupAttribute_InUnivariateDoubleDistribution_cagAttrDistribution(set_InConsumerAgentGroupAttribute,set_InUnivariateDoubleDistribution)

* - identifier: InProductAwarenessSupplyScheme
* - hidden: 1
* - type: String
SET set_InProductAwarenessSupplyScheme(set_InEntity)

* - identifier: InProductThresholdAwarenessSupplyScheme
* - type: String
SET set_InProductThresholdAwarenessSupplyScheme(set_InProductAwarenessSupplyScheme,set_InEntity)

* - description: Grenzwert ab dem das Produkt interessant wird
* - identifier: Grenzwert
* - type: Boolean
PARAMETER par_link_InProductThresholdAwarenessSupplyScheme_InUnivariateDoubleDistribution_awarenessDistribution(set_InProductThresholdAwarenessSupplyScheme,set_InUnivariateDoubleDistribution)

* - description: Binäre Daten für diverse Funktionalitäten
* - hidden: 0
* - identifier: Binäre Daten
* - type: String
SET set_VisibleBinaryData(set_InEntity)

* - description: Spezielle ID der Daten, Verwendungszweck und Funktionsweise ist von den Daten selber abhängig.
* - hidden: 0
* - identifier: ID
* - type: Integer
PARAMETER par_VisibleBinaryData_idVisible(set_VisibleBinaryData)

* - description: Binäre Daten für diverse Funktionalitäten. Identisch zu VisibleBinaryData, aber für den internen Austausch gedacht und damit versteckt, um nicht im UI zu erscheinen.
* - hidden: 1
* - identifier: HiddenBinaryData
* - type: String
SET set_HiddenBinaryData(*)

* - description: Spezielle ID der Daten, Verwendungszweck und Funktionsweise ist von den Daten selber abhängig.
* - hidden: 1
* - identifier: HiddenBinaryDataID
* - type: Integer
PARAMETER par_HiddenBinaryData_idHidden(set_HiddenBinaryData)

* - identifier: InBooleanDistribution
* - type: String
SET set_InBooleanDistribution(set_InUnivariateDoubleDistribution,set_InEntity)

* - description: Platzhalter
* - identifier: -
* - type: Integer
PARAMETER par_InBooleanDistribution_placeholderBooleanDist(set_InBooleanDistribution)

* - identifier: InConstantUnivariateDistribution
* - type: String
SET set_InConstantUnivariateDistribution(set_InUnivariateDoubleDistribution,set_InEntity)

* - description: Konstante Rückgabewert
* - identifier: Wert
* - type: Float
PARAMETER par_InConstantUnivariateDistribution_constDistValue(set_InConstantUnivariateDistribution)

* - identifier: InFiniteMassPointsDiscreteDistribution
* - type: String
SET set_InFiniteMassPointsDiscreteDistribution(set_InUnivariateDoubleDistribution,set_InEntity)

* - description: Die zu nutzende Massepunkte
* - identifier: Massepunkte
* - type: Boolean
PARAMETER par_link_InFiniteMassPointsDiscreteDistribution_InMassPoint_massPoints(set_InFiniteMassPointsDiscreteDistribution,set_InMassPoint)

* - identifier: InMassPoint
* - type: String
SET set_InMassPoint(set_InEntity)

* - description: Wert des Punktes
* - identifier: Wert des Punktes
* - type: Float
PARAMETER par_InMassPoint_mpValue(set_InMassPoint)

* - description: Wichtung des Punktes
* - identifier: Wichtun des Punktesg
* - type: Float
PARAMETER par_InMassPoint_mpWeight(set_InMassPoint)

* - identifier: InRandomBoundedIntegerDistribution
* - type: String
SET set_InRandomBoundedIntegerDistribution(set_InUnivariateDoubleDistribution,set_InEntity)

* - description: Untergrenze
* - identifier: Untergrenze (inklusiv)
* - type: Integer
PARAMETER par_InRandomBoundedIntegerDistribution_lowerBoundInt(set_InRandomBoundedIntegerDistribution)

* - description: Obergrenze
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

* - identifier: placeholderPVFile
* - type: Float
PARAMETER par_InPVFile_placeholderPVFile(set_InPVFile)

* - identifier: InSpatialTableFile
* - type: String
SET set_InSpatialTableFile(set_InFile,set_InEntity)

* - identifier: placeholderInSpatialFile
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

* - identifier: InCompleteGraphTopology
* - type: String
SET set_InCompleteGraphTopology(set_InGraphTopologyScheme,set_InEntity)

* - description: Initiale Gewicht der Kanten
* - identifier: Initiale Kantengewicht
* - type: Float
PARAMETER par_InCompleteGraphTopology_initialWeight(set_InCompleteGraphTopology)

* - identifier: InDistanceEvaluator
* - hidden: 1
* - type: String
SET set_InDistanceEvaluator(set_InEntity)

* - identifier: InFreeNetworkTopology
* - type: String
SET set_InFreeNetworkTopology(set_InGraphTopologyScheme,set_InEntity)

* - description: Evaluator für Abstände
* - identifier: Legt den Evaluator für die Abstände zwischen den Agenten fest.
* - type: Boolean
PARAMETER par_link_InFreeNetworkTopology_InDistanceEvaluator_distanceEvaluator(set_InFreeNetworkTopology,set_InDistanceEvaluator)

* - description: Knotenanzahl
* - identifier: Knotenanzahl je KG
* - type: Boolean
PARAMETER par_link_InFreeNetworkTopology_InNumberOfTies_numberOfTies(set_InFreeNetworkTopology,set_InNumberOfTies)

* - identifier: initialWeight
* - type: Float
PARAMETER par_InFreeNetworkTopology_initialWeight(set_InFreeNetworkTopology)

* - identifier: InGraphTopologyScheme
* - hidden: 1
* - type: String
SET set_InGraphTopologyScheme(set_InEntity)

* - identifier: InInverse
* - type: String
SET set_InInverse(set_InDistanceEvaluator,set_InEntity)

* - identifier: placeholderInverse
* - type: Integer
PARAMETER par_InInverse_placeholderInverse(set_InInverse)

* - identifier: InNoDistance
* - type: String
SET set_InNoDistance(set_InDistanceEvaluator,set_InEntity)

* - identifier: placeholderNoDistance
* - type: Integer
PARAMETER par_InNoDistance_placeholderNoDistance(set_InNoDistance)

* - identifier: InNumberOfTies
* - type: String
SET set_InNumberOfTies(set_InEntity)

* - description: Konsumergruppe
* - identifier: Zu nutzende Konsumergruppe
* - type: Boolean
PARAMETER par_link_InNumberOfTies_InConsumerAgentGroup_cag(set_InNumberOfTies,set_InConsumerAgentGroup)

* - description: Anzahl Kanten je Konsumergruppe
* - identifier: Anzahl Kanten
* - type: Integer
PARAMETER par_InNumberOfTies_count(set_InNumberOfTies)

* - identifier: InUnlinkedGraphTopology
* - type: String
SET set_InUnlinkedGraphTopology(set_InGraphTopologyScheme,set_InEntity)

* - description: Platzhalter
* - identifier: --
* - type: Float
PARAMETER par_InUnlinkedGraphTopology_placeholderUnlinked(set_InUnlinkedGraphTopology)

* - identifier: InAutoUncertaintyGroupAttribute
* - type: String
SET set_InAutoUncertaintyGroupAttribute(set_InUncertaintyGroupAttribute,set_InEntity)

* - description: Platzhalter
* - identifier: ---
* - type: Float
PARAMETER par_InAutoUncertaintyGroupAttribute_placeholderAutoUncert(set_InAutoUncertaintyGroupAttribute)

* - identifier: InCustomUncertaintyGroupAttribute
* - type: String
SET set_InCustomUncertaintyGroupAttribute(set_InUncertaintyGroupAttribute,set_InEntity)

* - description: -
* - identifier: Ziel-KGs
* - type: Boolean
PARAMETER par_link_InCustomUncertaintyGroupAttribute_InConsumerAgentGroup_cags(set_InCustomUncertaintyGroupAttribute,set_InConsumerAgentGroup)

* - description: -
* - identifier: Ziel-Attribute
* - type: Boolean
PARAMETER par_link_InCustomUncertaintyGroupAttribute_InAttributeName_names(set_InCustomUncertaintyGroupAttribute,set_InAttributeName)

* - description: -
* - identifier: Unsicherheit
* - type: Boolean
PARAMETER par_link_InCustomUncertaintyGroupAttribute_InUnivariateDoubleDistribution_uncertDist(set_InCustomUncertaintyGroupAttribute,set_InUnivariateDoubleDistribution)

* - description: -
* - identifier: Konvergenz
* - type: Boolean
PARAMETER par_link_InCustomUncertaintyGroupAttribute_InUnivariateDoubleDistribution_convergenceDist(set_InCustomUncertaintyGroupAttribute,set_InUnivariateDoubleDistribution)

* - identifier: InOrientationSupplier
* - type: String
SET set_InOrientationSupplier(set_InEntity)

* - description: Verteilungsfunktion
* - identifier: Verteilungsfunktion für die Orientierung
* - type: Boolean
PARAMETER par_link_InOrientationSupplier_InUnivariateDoubleDistribution_distInOrientation(set_InOrientationSupplier,set_InUnivariateDoubleDistribution)

* - description: Gruppen, denen die Werte hinzugefügt werden sollen.
* - identifier: Ziel-KGs für Orientierung
* - type: Boolean
PARAMETER par_link_InOrientationSupplier_InConsumerAgentGroup_oriCags(set_InOrientationSupplier,set_InConsumerAgentGroup)

* - identifier: InProcessModel
* - hidden: 1
* - type: String
SET set_InProcessModel(set_InEntity)

* - identifier: InRAProcessModel
* - type: String
SET set_InRAProcessModel(set_InProcessModel,set_InEntity)

* - description: a
* - identifier: a
* - type: Float
PARAMETER par_InRAProcessModel_a(set_InRAProcessModel)

* - description: b
* - identifier: b
* - type: Float
PARAMETER par_InRAProcessModel_b(set_InRAProcessModel)

* - description: c
* - identifier: c
* - type: Float
PARAMETER par_InRAProcessModel_c(set_InRAProcessModel)

* - description: d
* - identifier: d
* - type: Float
PARAMETER par_InRAProcessModel_d(set_InRAProcessModel)

* - description: -
* - identifier: Adopter-Punkte
* - type: Integer
PARAMETER par_InRAProcessModel_adopterPoints(set_InRAProcessModel)

* - description: -
* - identifier: Interessenten-Punkte
* - type: Integer
PARAMETER par_InRAProcessModel_interestedPoints(set_InRAProcessModel)

* - description: -
* - identifier: Aware-Punkte
* - type: Integer
PARAMETER par_InRAProcessModel_awarePoints(set_InRAProcessModel)

* - description: -
* - identifier: Unbekannt-Punkte
* - type: Integer
PARAMETER par_InRAProcessModel_unknownPoints(set_InRAProcessModel)

* - description: -
* - identifier: PV Datei
* - type: Boolean
PARAMETER par_link_InRAProcessModel_InPVFile_pvFile(set_InRAProcessModel,set_InPVFile)

* - description: -
* - identifier: Datenerweiterung-Neigung
* - type: Boolean
PARAMETER par_link_InRAProcessModel_InSlopeSupplier_slopeSuppliers(set_InRAProcessModel,set_InSlopeSupplier)

* - description: -
* - identifier: Datenerweiterung-Orientierung
* - type: Boolean
PARAMETER par_link_InRAProcessModel_InOrientationSupplier_orientationSuppliers(set_InRAProcessModel,set_InOrientationSupplier)

* - description: -
* - identifier: Attribute für Unsicherheit
* - type: Boolean
PARAMETER par_link_InRAProcessModel_InUncertaintyGroupAttribute_uncertaintyGroupAttributes(set_InRAProcessModel,set_InUncertaintyGroupAttribute)

* - identifier: InSlopeSupplier
* - type: String
SET set_InSlopeSupplier(set_InEntity)

* - description: Verteilungsfunktion
* - identifier: Verteilungsfunktion für die Neigung
* - type: Boolean
PARAMETER par_link_InSlopeSupplier_InUnivariateDoubleDistribution_distSlope(set_InSlopeSupplier,set_InUnivariateDoubleDistribution)

* - description: Gruppen, denen die Werte hinzugefügt werden sollen.
* - identifier: Ziel-KGs für Neigung
* - type: Boolean
PARAMETER par_link_InSlopeSupplier_InConsumerAgentGroup_slopeCags(set_InSlopeSupplier,set_InConsumerAgentGroup)

* - identifier: InUncertaintyGroupAttribute
* - hidden: 1
* - type: String
SET set_InUncertaintyGroupAttribute(set_InEntity)

* - identifier: InFixProduct
* - type: String
SET set_InFixProduct(set_InEntity)

* - description: Zugehörige Gruppe
* - identifier: Produktgruppe
* - type: Boolean
PARAMETER par_link_InFixProduct_InProductGroup_refPG(set_InFixProduct,set_InProductGroup)

* - description: Attribute
* - identifier: Attribute
* - type: Boolean
PARAMETER par_link_InFixProduct_InFixProductAttribute_fixPAttrs(set_InFixProduct,set_InFixProductAttribute)

* - identifier: InFixProductAttribute
* - type: String
SET set_InFixProductAttribute(set_InEntity)

* - description: Gruppenattribut
* - identifier: Gruppenattribut
* - type: Boolean
PARAMETER par_link_InFixProductAttribute_InProductGroupAttribute_refPGA(set_InFixProductAttribute,set_InProductGroupAttribute)

* - description: Wert
* - identifier: Fixierte Wert
* - type: Float
PARAMETER par_InFixProductAttribute_fixPAvalue(set_InFixProductAttribute)

* - identifier: InFixProductFindingScheme
* - type: String
SET set_InFixProductFindingScheme(set_InProductFindingScheme,set_InEntity)

* - description: Produkt
* - identifier: Produkt
* - type: Boolean
PARAMETER par_link_InFixProductFindingScheme_InFixProduct_refFixProduct(set_InFixProductFindingScheme,set_InFixProduct)

* - identifier: InProductFindingScheme
* - hidden: 1
* - type: String
SET set_InProductFindingScheme(set_InEntity)

* - identifier: InProductGroup
* - type: String
SET set_InProductGroup(set_InEntity)

* - identifier: pgAttributes
* - type: Boolean
PARAMETER par_link_InProductGroup_InProductGroupAttribute_pgAttributes(set_InProductGroup,set_InProductGroupAttribute)

* - identifier: InProductGroupAttribute
* - type: String
SET set_InProductGroupAttribute(set_InEntity)

* - identifier: attrName
* - type: Boolean
PARAMETER par_link_InProductGroupAttribute_InAttributeName_attrName(set_InProductGroupAttribute,set_InAttributeName)

* - identifier: attrDistribution
* - type: Boolean
PARAMETER par_link_InProductGroupAttribute_InUnivariateDoubleDistribution_attrDistribution(set_InProductGroupAttribute,set_InUnivariateDoubleDistribution)

* - identifier: InCustomSelectedGroupedSpatialDistribution2D
* - type: String
SET set_InCustomSelectedGroupedSpatialDistribution2D(set_InSpatialDistribution,set_InEntity)

* - description: X-Position
* - identifier: X-Position1
* - type: Boolean
PARAMETER par_link_InCustomSelectedGroupedSpatialDistribution2D_InUnivariateDoubleDistribution_xPosSupplier(set_InCustomSelectedGroupedSpatialDistribution2D,set_InUnivariateDoubleDistribution)

* - description: Y-Position
* - identifier: Y-Position1
* - type: Boolean
PARAMETER par_link_InCustomSelectedGroupedSpatialDistribution2D_InUnivariateDoubleDistribution_yPosSupplier(set_InCustomSelectedGroupedSpatialDistribution2D,set_InUnivariateDoubleDistribution)

* - description: Zu nutzende Tabelle für weitere Informationen
* - identifier: Tabellendaten1
* - type: Boolean
PARAMETER par_link_InCustomSelectedGroupedSpatialDistribution2D_InSpatialTableFile_file(set_InCustomSelectedGroupedSpatialDistribution2D,set_InSpatialTableFile)

* - description: Dieser Schlüssel wird verwendet, um die Daten zu filtern (z.B. Milieu).
* - identifier: Filterschlüssel1
* - type: Boolean
PARAMETER par_link_InCustomSelectedGroupedSpatialDistribution2D_InAttributeName_selectKey(set_InCustomSelectedGroupedSpatialDistribution2D,set_InAttributeName)

* - description: Dieser Schlüssel wird verwendet, um die Daten zu für die Wichtung zu gruppieren.
* - identifier: Gruppierungsschlüssel1
* - type: Boolean
PARAMETER par_link_InCustomSelectedGroupedSpatialDistribution2D_InAttributeName_groupKey(set_InCustomSelectedGroupedSpatialDistribution2D,set_InAttributeName)

* - identifier: InCustomSelectedSpatialDistribution2D
* - type: String
SET set_InCustomSelectedSpatialDistribution2D(set_InSpatialDistribution,set_InEntity)

* - description: X-Position
* - identifier: X-Position0
* - type: Boolean
PARAMETER par_link_InCustomSelectedSpatialDistribution2D_InUnivariateDoubleDistribution_xPosSupplier(set_InCustomSelectedSpatialDistribution2D,set_InUnivariateDoubleDistribution)

* - description: Y-Position
* - identifier: Y-Position0
* - type: Boolean
PARAMETER par_link_InCustomSelectedSpatialDistribution2D_InUnivariateDoubleDistribution_yPosSupplier(set_InCustomSelectedSpatialDistribution2D,set_InUnivariateDoubleDistribution)

* - description: Zu nutzende Tabelle für weitere Informationen
* - identifier: Tabellendaten0
* - type: Boolean
PARAMETER par_link_InCustomSelectedSpatialDistribution2D_InSpatialTableFile_attrFile(set_InCustomSelectedSpatialDistribution2D,set_InSpatialTableFile)

* - description: Dieser Schlüssel wird verwendet, um die Daten zu filtern (z.B. Milieu).
* - identifier: Filterschlüssel0
* - type: Boolean
PARAMETER par_link_InCustomSelectedSpatialDistribution2D_InAttributeName_selectKey(set_InCustomSelectedSpatialDistribution2D,set_InAttributeName)

* - identifier: InCustomSpatialDistribution2D
* - type: String
SET set_InCustomSpatialDistribution2D(set_InSpatialDistribution,set_InEntity)

* - description: X-Position
* - identifier: X-Position
* - type: Boolean
PARAMETER par_link_InCustomSpatialDistribution2D_InUnivariateDoubleDistribution_xPosSupplier(set_InCustomSpatialDistribution2D,set_InUnivariateDoubleDistribution)

* - description: Y-Position
* - identifier: Y-Position
* - type: Boolean
PARAMETER par_link_InCustomSpatialDistribution2D_InUnivariateDoubleDistribution_yPosSupplier(set_InCustomSpatialDistribution2D,set_InUnivariateDoubleDistribution)

* - description: Zu nutzende Tabelle für weitere Informationen
* - identifier: Tabellendaten
* - type: Boolean
PARAMETER par_link_InCustomSpatialDistribution2D_InSpatialTableFile_attrFile(set_InCustomSpatialDistribution2D,set_InSpatialTableFile)

* - identifier: InSpatialDistribution
* - hidden: 1
* - type: String
SET set_InSpatialDistribution(set_InEntity)

* - identifier: InSpace2D
* - type: String
SET set_InSpace2D(set_InSpatialModel,set_InEntity)

* - identifier: useEuclid
* - type: Boolean
PARAMETER par_InSpace2D_useEuclid(set_InSpace2D)

* - identifier: InSpatialModel
* - hidden: 1
* - type: String
SET set_InSpatialModel(set_InEntity)

* - identifier: InDiscreteTimeModel
* - type: String
SET set_InDiscreteTimeModel(set_InTimeModel,set_InEntity)

* - description: Zeit pro Schritt
* - identifier: Zeit pro Schritt
* - unit: [ms]
* - type: Integer
PARAMETER par_InDiscreteTimeModel_timePerTickInMs(set_InDiscreteTimeModel)

* - identifier: InTimeModel
* - hidden: 1
* - type: String
SET set_InTimeModel(set_InEntity)

* - identifier: InAttributeName
* - type: String
SET set_InAttributeName(set_InEntity)

* - description: Platzhalter
* - identifier: ----
* - type: Integer
PARAMETER par_InAttributeName_placeholder(set_InAttributeName)

* - identifier: InEntity
* - hidden: 1
* - type: String
SET set_InEntity(*)

* - default: -1
* - description: Setzt den Seed für den Zufallsgenerator der Simulation. Falls er auf -1 gesetzt wird, wird ein zufälliger Seed generiert.
* - identifier: Zufallsgenerator (seed)
* - type: Integer
SCALAR sca_InGeneral_seed

* - default: 5
* - description: Setzt den Timeout der Simulation in Millisekunden. Diese Einstellung dient dazu die Simulation zu beenden, falls sie unerwartet abstürzt. Im Laufe der Simulation wird der Timeout unentwegt zurück gesetzt. Sollte es zu einem unerwarteten Fehler kommen und die Rücksetzung ausbleiben, wird die Simulation abgebrochen und beendet. Werte kleiner 1 deaktivieren den Timeout vollständig.
* - identifier: Timeout
* - unit: [ms]
* - type: Integer
SCALAR sca_InGeneral_timeout

* - description: [TEMPORÄRE OPTION] Setzt das finale Jahr der Simulation. Wichtig: Der Wert ist inklusiv. Es wird dabei immer mindestens ein Jahr simuliert, auch wenn der Wert kleiner ist als das des Ausgangsjahres.
* - identifier: Endjahr der Simulation
* - type: Integer
SCALAR sca_InGeneral_endYear

* - description: [TEMPORÄRE OPTION] Falls gesetzt, wird die optact-Demo gestartet. Anderenfalls wird IRPact gestartet.
* - identifier: optact-Testmodell ausführen
* - type: Boolean
SCALAR sca_InGeneral_runOptActDemo

* - default: 6
* - domain: [0, 6]
* - description: Setzt das zu nutzende Logging-Level in IRPact, folgende Werte werden unterstützt: 0 = OFF, 1 = TRACE, 2 = DEBUG, 3 = INFO, 4 = WARN, 5 = ERROR, 6 = ALL. Das Level ist der Hauptfilter für alle log-Operationen.
* - identifier: Logging-Level
* - type: Integer
SCALAR sca_InGeneral_logLevel

* - domain: [0|1]
* - description: [SPAM] Ob alles geloggt werden soll. Falls ja, überschreibt diese Option alles.
* - identifier: log alles
* - type: Boolean
SCALAR sca_InGeneral_logAll

* - domain: [0|1]
* - description: [SPAM] Ob Aufrufe der Tools-Bibliothek geloggt werden sollen.
* - identifier: log Tools-Aufrufe
* - type: Boolean
SCALAR sca_InGeneral_logTools

* - domain: [0|1]
* - description: Ob die Initialisierung der Parameter geloggt werden soll.
* - identifier: log Initialisierung der Parameter
* - type: Boolean
SCALAR sca_InGeneral_logParamInit

* - domain: [0|1]
* - description: [SPAM] Ob die Grapherzeugung geloggt werden soll.
* - identifier: log Grapherzeugung
* - type: Boolean
SCALAR sca_InGeneral_logGraphCreation

* - domain: [0|1]
* - description: [SPAM] Ob die Agentenerzeugung geloggt werden soll.
* - identifier: log Agentenerzeugung
* - type: Boolean
SCALAR sca_InGeneral_logAgentCreation

* - domain: [0|1]
* - description: [SPAM] Ob die Erstellung der Simulation (Agentenplatform) geloggt werden soll.
* - identifier: log Platformerstellung
* - type: Boolean
SCALAR sca_InGeneral_logPlatformCreation

* - domain: [0|1]
* - description: [SPAM] Ob der Lebenszyklus geloggt werden soll.
* - identifier: log Simulationszyklen
* - type: Boolean
SCALAR sca_InGeneral_logSimulationLifecycle

* - domain: [0|1]
* - description: [SPAM] Ob die Agenten während der Simulation geloggt werden sollen.
* - identifier: log Agenten während Simulation
* - type: Boolean
SCALAR sca_InGeneral_logSimulationAgent

* - domain: [0|1]
* - description: [SPAM] Ob spezielle Jadex-Ausgaben geloggt werden sollen. (Anmerkung: Diese Ausgaben von Jadex selbst generiert.)
* - identifier: log Jadex Systemnachrichten
* - type: Boolean
SCALAR sca_InGeneral_logJadexSystemOut

* - description: Version von IRPact für dieses Szenario.
* - hidden: 1
* - identifier: InVersion
* - type: String
SET set_InVersion(set_InEntity)

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
