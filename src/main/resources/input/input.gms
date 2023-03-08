* - identifier: InAffinities
* - type: String
SET set_InAffinities(*)

* - description: Die Affinitäte, welche in dieser Menge enthalten sein sollen.
* - identifier: Einträge
* - type: Boolean
PARAMETER par_link_InAffinities_InAffinityEntry_entries(set_InAffinities,set_InAffinityEntry)

* - identifier: InAffinityEntry
* - hidden: 1
* - type: String
SET set_InAffinityEntry(*)

* - identifier: InComplexAffinityEntry
* - type: String
SET set_InComplexAffinityEntry(set_InAffinityEntry)

* - description: Ausgangsgruppe
* - identifier: Ausgangsgruppe
* - type: Boolean
PARAMETER par_link_InComplexAffinityEntry_InConsumerAgentGroup_srcCag(set_InComplexAffinityEntry,set_InConsumerAgentGroup)

* - description: Zielgruppe
* - identifier: Zielgruppe
* - type: Boolean
PARAMETER par_link_InComplexAffinityEntry_InConsumerAgentGroup_tarCag(set_InComplexAffinityEntry,set_InConsumerAgentGroup)

* - description: Der Affinitätswert der Ausgangsgruppe gegenüber der Zielgruppe.
* - identifier: Wert
* - type: Float
PARAMETER par_InComplexAffinityEntry_affinityValue(set_InComplexAffinityEntry)

* - identifier: InNameSplitAffinityEntry
* - type: String
SET set_InNameSplitAffinityEntry(set_InAffinityEntry)

* - description: Der Affinitätswert der Ausgangsgruppe gegenüber der Zielgruppe.
* - identifier: Wert
* - type: Float
PARAMETER par_InNameSplitAffinityEntry_affinityValue(set_InNameSplitAffinityEntry)

* - identifier: InConsumerAgentGroup
* - hidden: 1
* - type: String
SET set_InConsumerAgentGroup(*)

* - identifier: InConsumerAgentGroupAttribute
* - hidden: 1
* - type: String
SET set_InConsumerAgentGroupAttribute(*)

* - identifier: InDependentConsumerAgentGroupAttribute
* - hidden: 1
* - type: String
SET set_InDependentConsumerAgentGroupAttribute(set_InConsumerAgentGroupAttribute)

* - identifier: InGeneralConsumerAgentAnnualGroupAttribute
* - type: String
SET set_InGeneralConsumerAgentAnnualGroupAttribute(set_InDependentConsumerAgentGroupAttribute)

* - description: Name des Attributes für das Gruppenattribut.
* - identifier: Attributname
* - type: Boolean
PARAMETER par_link_InGeneralConsumerAgentAnnualGroupAttribute_InAttributeName_attributeName(set_InGeneralConsumerAgentAnnualGroupAttribute,set_InAttributeName)

* - description: Legt die zu nutzende Verteilungsfunktion fest.
* - identifier: Verteilungsfunktion
* - type: Boolean
PARAMETER par_link_InGeneralConsumerAgentAnnualGroupAttribute_InUnivariateDoubleDistribution_distribution(set_InGeneralConsumerAgentAnnualGroupAttribute,set_InUnivariateDoubleDistribution)

* - identifier: InGeneralConsumerAgentGroup
* - type: String
SET set_InGeneralConsumerAgentGroup(set_InConsumerAgentGroup)

* - description: Zu dieser Gruppe gehörendes Attribut.
* - identifier: Gruppenattribute
* - type: Boolean
PARAMETER par_link_InGeneralConsumerAgentGroup_InDependentConsumerAgentGroupAttribute_cagAttributes(set_InGeneralConsumerAgentGroup,set_InDependentConsumerAgentGroupAttribute)

* - description: Legt das Interessensschema fest.
* - identifier: Interessensschema
* - type: Boolean
PARAMETER par_link_InGeneralConsumerAgentGroup_InProductInterestSupplyScheme_cagInterest(set_InGeneralConsumerAgentGroup,set_InProductInterestSupplyScheme)

* - description: Legt das Schema für das finden von passenden Produkten fest
* - identifier: Schema für die Produktfindung
* - type: Boolean
PARAMETER par_link_InGeneralConsumerAgentGroup_InProductFindingScheme_productFindingSchemes(set_InGeneralConsumerAgentGroup,set_InProductFindingScheme)

* - description: Legt die Verteilungsfunktion für diese Gruppe fest
* - identifier: Räumliche Verteilungsfunktion
* - type: Boolean
PARAMETER par_link_InGeneralConsumerAgentGroup_InSpatialDistribution_spatialDistribution(set_InGeneralConsumerAgentGroup,set_InSpatialDistribution)

* - identifier: InGeneralConsumerAgentGroupAttribute
* - type: String
SET set_InGeneralConsumerAgentGroupAttribute(set_InDependentConsumerAgentGroupAttribute)

* - description: Name des Attributes für das Gruppenattribut.
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
SET set_InIndependentConsumerAgentGroupAttribute(set_InConsumerAgentGroupAttribute)

* - identifier: InNameSplitConsumerAgentGroupAttribute
* - type: String
SET set_InNameSplitConsumerAgentGroupAttribute(set_InIndependentConsumerAgentGroupAttribute)

* - description: Legt die Verteilungsfunktion fest, auf deren Basis die Attributwerte gezogen werden.
* - identifier: Verteilungsfunktion
* - type: Boolean
PARAMETER par_link_InNameSplitConsumerAgentGroupAttribute_InUnivariateDoubleDistribution_dist(set_InNameSplitConsumerAgentGroupAttribute,set_InUnivariateDoubleDistribution)

* - identifier: InPVactConsumerAgentGroup
* - type: String
SET set_InPVactConsumerAgentGroup(set_InConsumerAgentGroup)

* - description: todo
* - identifier: novelty seeking
* - type: Boolean
PARAMETER par_link_InPVactConsumerAgentGroup_InUnivariateDoubleDistribution_noveltySeeking(set_InPVactConsumerAgentGroup,set_InUnivariateDoubleDistribution)

* - description: todo
* - identifier: dependent judgment making
* - type: Boolean
PARAMETER par_link_InPVactConsumerAgentGroup_InUnivariateDoubleDistribution_dependentJudgmentMaking(set_InPVactConsumerAgentGroup,set_InUnivariateDoubleDistribution)

* - description: todo
* - identifier: environmental concern
* - type: Boolean
PARAMETER par_link_InPVactConsumerAgentGroup_InUnivariateDoubleDistribution_environmentalConcern(set_InPVactConsumerAgentGroup,set_InUnivariateDoubleDistribution)

* - description: todo
* - identifier: interest threshold
* - type: Boolean
PARAMETER par_link_InPVactConsumerAgentGroup_InUnivariateDoubleDistribution_interestThreshold(set_InPVactConsumerAgentGroup,set_InUnivariateDoubleDistribution)

* - description: todo
* - identifier: financial threshold
* - type: Boolean
PARAMETER par_link_InPVactConsumerAgentGroup_InUnivariateDoubleDistribution_financialThreshold(set_InPVactConsumerAgentGroup,set_InUnivariateDoubleDistribution)

* - description: todo
* - identifier: adoption threshold
* - type: Boolean
PARAMETER par_link_InPVactConsumerAgentGroup_InUnivariateDoubleDistribution_adoptionThreshold(set_InPVactConsumerAgentGroup,set_InUnivariateDoubleDistribution)

* - description: todo
* - identifier: communication
* - type: Boolean
PARAMETER par_link_InPVactConsumerAgentGroup_InUnivariateDoubleDistribution_communication(set_InPVactConsumerAgentGroup,set_InUnivariateDoubleDistribution)

* - description: todo
* - identifier: rewire
* - type: Boolean
PARAMETER par_link_InPVactConsumerAgentGroup_InUnivariateDoubleDistribution_rewire(set_InPVactConsumerAgentGroup,set_InUnivariateDoubleDistribution)

* - description: todo
* - identifier: initial adopter
* - type: Boolean
PARAMETER par_link_InPVactConsumerAgentGroup_InUnivariateDoubleDistribution_initialAdopter(set_InPVactConsumerAgentGroup,set_InUnivariateDoubleDistribution)

* - description: todo
* - identifier: rate of cnvergence
* - type: Boolean
PARAMETER par_link_InPVactConsumerAgentGroup_InUnivariateDoubleDistribution_rateOfConvergence(set_InPVactConsumerAgentGroup,set_InUnivariateDoubleDistribution)

* - description: todo
* - identifier: initial product interest
* - type: Boolean
PARAMETER par_link_InPVactConsumerAgentGroup_InUnivariateDoubleDistribution_initialProductInterest(set_InPVactConsumerAgentGroup,set_InUnivariateDoubleDistribution)

* - description: todo
* - identifier: initial product awareness
* - type: Boolean
PARAMETER par_link_InPVactConsumerAgentGroup_InUnivariateDoubleDistribution_initialProductAwareness(set_InPVactConsumerAgentGroup,set_InUnivariateDoubleDistribution)

* - description: todo
* - identifier: construction rate
* - type: Boolean
PARAMETER par_link_InPVactConsumerAgentGroup_InUnivariateDoubleDistribution_constructionRate(set_InPVactConsumerAgentGroup,set_InUnivariateDoubleDistribution)

* - description: todo
* - identifier: renovation rate
* - type: Boolean
PARAMETER par_link_InPVactConsumerAgentGroup_InUnivariateDoubleDistribution_renovationRate(set_InPVactConsumerAgentGroup,set_InUnivariateDoubleDistribution)

* - description: todo
* - identifier: spatial distribution
* - type: Boolean
PARAMETER par_link_InPVactConsumerAgentGroup_InSpatialDistribution_spatialDistribution(set_InPVactConsumerAgentGroup,set_InSpatialDistribution)

* - identifier: InFixConsumerAgentPopulation
* - type: String
SET set_InFixConsumerAgentPopulation(set_InAgentPopulation)

* - description: Legt die Anzahl der Agenten bzw. die Populationsgröße für diese Konsumergruppe fest.
* - identifier: Anzahl der Agenten
* - type: Integer
PARAMETER par_InFixConsumerAgentPopulation_size(set_InFixConsumerAgentPopulation)

* - description: Legt die Konsumergruppen fest, welche diese Populationsgröße haben sollen.
* - identifier: Konsumergruppen
* - type: Boolean
PARAMETER par_link_InFixConsumerAgentPopulation_InConsumerAgentGroup_cags(set_InFixConsumerAgentPopulation,set_InConsumerAgentGroup)

* - identifier: InAgentPopulation
* - hidden: 1
* - type: String
SET set_InAgentPopulation(*)

* - identifier: InFileBasedConsumerAgentPopulation
* - type: String
SET set_InFileBasedConsumerAgentPopulation(set_InAgentPopulation)

* - description: Legt die maximale Populationsgröße für alle hier verwendeten Konsumergruppen fest. Falls der Wert kleiner 0 ist, wird die maximal mögliche Anzahl verwendet.
* - identifier: Gewünschte Anzahl der Agenten
* - type: Integer
PARAMETER par_InFileBasedConsumerAgentPopulation_desiredSize(set_InFileBasedConsumerAgentPopulation)

* - description: Legt fest, ob die gesamte Populationsgröße aus der Datei verwendet werden soll. Falls diese Funktion gesetzt ist, wird die gewünschte Anzahl der Agenten ignoriert.
* - identifier: Maximal mögliche Anzahl nutzen?
* - type: Boolean
PARAMETER par_InFileBasedConsumerAgentPopulation_useAll(set_InFileBasedConsumerAgentPopulation)

* - description: Falls diese Funktion aktiviert ist, muss die gewünschte Anzahl Agenten existieren. Ist zum Beispiel eine Population von 10000 gewünscht, aber die Datei enthält nur 1000 Agenten, so wird ein Fehler geworfen. Falls die Funktion deaktiviert ist, wird die Population im Notfall angepasst.
* - identifier: Populationsgröße erzwingen?
* - type: Boolean
PARAMETER par_InFileBasedConsumerAgentPopulation_requiresDesiredTotalSize(set_InFileBasedConsumerAgentPopulation)

* - description: Legt die Konsumergruppen fest, welche berücksichtigt werden sollen.
* - identifier: Konsumergruppen
* - type: Boolean
PARAMETER par_link_InFileBasedConsumerAgentPopulation_InConsumerAgentGroup_cags(set_InFileBasedConsumerAgentPopulation,set_InConsumerAgentGroup)

* - description: Tabellarische Eingabedatei, aus der die Informationen gelesen werden sollen.
* - identifier: Tabellarische Datei
* - type: Boolean
PARAMETER par_link_InFileBasedConsumerAgentPopulation_InSpatialTableFile_file(set_InFileBasedConsumerAgentPopulation,set_InSpatialTableFile)

* - description: Schlüssel, nachdem die Datei gefiltert wird. Wichtig: Die entsprechenden Daten in der Datei müssen dem Konsumergruppennamen entsprechen.
* - identifier: Auswahlschlüssel
* - type: Boolean
PARAMETER par_link_InFileBasedConsumerAgentPopulation_InAttributeName_selectKey(set_InFileBasedConsumerAgentPopulation,set_InAttributeName)

* - identifier: InFileBasedPVactConsumerAgentPopulation
* - type: String
SET set_InFileBasedPVactConsumerAgentPopulation(set_InAgentPopulation)

* - description: Legt die maximale Populationsgröße für alle hier verwendeten Konsumergruppen fest. Falls der Wert kleiner 0 ist, wird die maximal mögliche Anzahl verwendet.
* - identifier: Gewünschte Anzahl der Agenten
* - type: Integer
PARAMETER par_InFileBasedPVactConsumerAgentPopulation_desiredSize(set_InFileBasedPVactConsumerAgentPopulation)

* - description: Legt fest, ob die gesamte Populationsgröße aus der Datei verwendet werden soll. Falls diese Funktion gesetzt ist, wird die gewünschte Anzahl der Agenten ignoriert.
* - identifier: Maximal mögliche Anzahl nutzen?
* - type: Boolean
PARAMETER par_InFileBasedPVactConsumerAgentPopulation_useAll(set_InFileBasedPVactConsumerAgentPopulation)

* - description: Falls diese Funktion aktiviert ist, muss die gewünschte Anzahl Agenten existieren. Ist zum Beispiel eine Population von 10000 gewünscht, aber die Datei enthält nur 1000 Agenten, so wird ein Fehler geworfen. Falls die Funktion deaktiviert ist, wird die Population im Notfall angepasst.
* - identifier: Populationsgröße erzwingen?
* - type: Boolean
PARAMETER par_InFileBasedPVactConsumerAgentPopulation_requiresDesiredTotalSize(set_InFileBasedPVactConsumerAgentPopulation)

* - description: Legt die Konsumergruppen fest, welche berücksichtigt werden sollen.
* - identifier: Konsumergruppen
* - type: Boolean
PARAMETER par_link_InFileBasedPVactConsumerAgentPopulation_InConsumerAgentGroup_cags(set_InFileBasedPVactConsumerAgentPopulation,set_InConsumerAgentGroup)

* - description: Tabellarische Eingabedatei, aus der die Informationen gelesen werden sollen.
* - identifier: Tabellarische Datei
* - type: Boolean
PARAMETER par_link_InFileBasedPVactConsumerAgentPopulation_InSpatialTableFile_file(set_InFileBasedPVactConsumerAgentPopulation,set_InSpatialTableFile)

* - description: Binäre Daten für diverse Funktionalitäten
* - identifier: Binäre Daten
* - type: String
SET set_VisibleBinaryData(*)

* - description: Spezielle ID der Daten, Verwendungszweck und Funktionsweise ist von den Daten selber abhängig.
* - identifier: ID
* - type: Integer
PARAMETER par_VisibleBinaryData_idVisible(set_VisibleBinaryData)

* - hidden: 1
* - identifier: BinaryPersistData
* - type: String
SET set_BinaryPersistData(*)

* - hidden: 1
* - identifier: BinaryPersistData_id
* - type: Integer
PARAMETER par_BinaryPersistData_id(set_BinaryPersistData)

* - identifier: InBernoulliDistribution
* - type: String
SET set_InBernoulliDistribution(set_InUnivariateDoubleDistribution)

* - description: Wahrscheinlichkeit p
* - identifier: p
* - type: Float
PARAMETER par_InBernoulliDistribution_p(set_InBernoulliDistribution)

* - default: 1
* - description: Dieser Wert wird mit der Wahrscheinlichkeit p zurück gegeben.
* - identifier: x
* - type: Float
PARAMETER par_InBernoulliDistribution_trueValue(set_InBernoulliDistribution)

* - default: 0
* - description: Dieser Wert wird mit der Wahrscheinlichkeit 1-p zurück gegeben.
* - identifier: y
* - type: Float
PARAMETER par_InBernoulliDistribution_falseValue(set_InBernoulliDistribution)

* - identifier: InBooleanDistribution
* - type: String
SET set_InBooleanDistribution(set_InUnivariateDoubleDistribution)

* - default: 1
* - description: Der Wert, welcher zurückgegeben wird, wenn die Funktion intern 1 (WAHR) ergibt.
* - identifier: x
* - type: Float
PARAMETER par_InBooleanDistribution_trueValue(set_InBooleanDistribution)

* - default: 0
* - description: Der Wert, welcher zurückgegeben wird, wenn die Funktion intern 0 (FALSCH) ergibt.
* - identifier: y
* - type: Float
PARAMETER par_InBooleanDistribution_falseValue(set_InBooleanDistribution)

* - identifier: InBoundedNormalDistribution
* - type: String
SET set_InBoundedNormalDistribution(set_InUnivariateDoubleDistribution)

* - default: 1
* - description: Die zu nutzende Standardabweichung.
* - identifier: Standardabweichung
* - type: Float
PARAMETER par_InBoundedNormalDistribution_standardDeviation(set_InBoundedNormalDistribution)

* - default: 0
* - description: Der zu nutzende Mittelwert.
* - identifier: Mittelwert
* - type: Float
PARAMETER par_InBoundedNormalDistribution_mean(set_InBoundedNormalDistribution)

* - description: Legt die Untergrenze fest.
* - identifier: Untergrenze (inklusiv)
* - type: Float
PARAMETER par_InBoundedNormalDistribution_lowerBound(set_InBoundedNormalDistribution)

* - description: Legt die Obergrenze fest.
* - identifier: Obergrenze (inklusiv)
* - type: Float
PARAMETER par_InBoundedNormalDistribution_upperBound(set_InBoundedNormalDistribution)

* - identifier: InDiracUnivariateDistribution
* - type: String
SET set_InDiracUnivariateDistribution(set_InUnivariateDoubleDistribution)

* - description: Legt den konstante Rückgabewert fest.
* - identifier: x
* - type: Float
PARAMETER par_InDiracUnivariateDistribution_value(set_InDiracUnivariateDistribution)

* - identifier: InFiniteMassPointsDiscreteDistribution
* - type: String
SET set_InFiniteMassPointsDiscreteDistribution(set_InUnivariateDoubleDistribution)

* - description: Legt die zu nutzenden Massepunkte fest.
* - identifier: Massepunkte
* - type: Boolean
PARAMETER par_link_InFiniteMassPointsDiscreteDistribution_InMassPoint_massPoints(set_InFiniteMassPointsDiscreteDistribution,set_InMassPoint)

* - identifier: InMassPoint
* - type: String
SET set_InMassPoint(*)

* - description: Legt den Wert des Punktes fest.
* - identifier: Wert des Punktes
* - type: Float
PARAMETER par_InMassPoint_mpValue(set_InMassPoint)

* - description: Legt die Wichtung des Punktes fest.
* - identifier: Wichtung des Punktes
* - type: Float
PARAMETER par_InMassPoint_mpWeight(set_InMassPoint)

* - identifier: InNormalDistribution
* - type: String
SET set_InNormalDistribution(set_InUnivariateDoubleDistribution)

* - default: 1
* - description: Die zu nutzende Standardabweichung.
* - identifier: Standardabweichung
* - type: Float
PARAMETER par_InNormalDistribution_standardDeviation(set_InNormalDistribution)

* - default: 0
* - description: Der zu nutzende Mittelwert.
* - identifier: Mittelwert
* - type: Float
PARAMETER par_InNormalDistribution_mean(set_InNormalDistribution)

* - default: 1
* - domain: [0|1]
* - description: Es wird nicht gerundet. Die Zahl bleibt unverändert.
* - identifier: Kein Runden
* - rule: IF (par_InNormalDistribution_modeNoRounding == 1, par_InNormalDistribution_modeFloor = 0)
* - rule: IF (par_InNormalDistribution_modeNoRounding == 1, par_InNormalDistribution_modeCeil = 0)
* - rule: IF (par_InNormalDistribution_modeNoRounding == 1, par_InNormalDistribution_modeRound = 0)
* - rule: IF (par_InNormalDistribution_modeNoRounding == 0, par_InNormalDistribution_modeNoRounding = 1)
* - rule: IF (par_InNormalDistribution_modeNoRounding == 0, par_InNormalDistribution_modeFloor = 0)
* - rule: IF (par_InNormalDistribution_modeNoRounding == 0, par_InNormalDistribution_modeCeil = 0)
* - rule: IF (par_InNormalDistribution_modeNoRounding == 0, par_InNormalDistribution_modeRound = 0)
* - type: Boolean
PARAMETER par_InNormalDistribution_modeNoRounding(set_InNormalDistribution)

* - default: 0
* - domain: [0|1]
* - description: Die Zahl wird immer abgerundet.
* - identifier: Abrunden
* - rule: IF (par_InNormalDistribution_modeFloor == 1, par_InNormalDistribution_modeNoRounding = 0)
* - rule: IF (par_InNormalDistribution_modeFloor == 1, par_InNormalDistribution_modeCeil = 0)
* - rule: IF (par_InNormalDistribution_modeFloor == 1, par_InNormalDistribution_modeRound = 0)
* - rule: IF (par_InNormalDistribution_modeFloor == 0, par_InNormalDistribution_modeNoRounding = 0)
* - rule: IF (par_InNormalDistribution_modeFloor == 0, par_InNormalDistribution_modeFloor = 1)
* - rule: IF (par_InNormalDistribution_modeFloor == 0, par_InNormalDistribution_modeCeil = 0)
* - rule: IF (par_InNormalDistribution_modeFloor == 0, par_InNormalDistribution_modeRound = 0)
* - type: Boolean
PARAMETER par_InNormalDistribution_modeFloor(set_InNormalDistribution)

* - default: 0
* - domain: [0|1]
* - description: Die Zahl wird immer aufgerundet.
* - identifier: Aufrunden
* - rule: IF (par_InNormalDistribution_modeCeil == 1, par_InNormalDistribution_modeNoRounding = 0)
* - rule: IF (par_InNormalDistribution_modeCeil == 1, par_InNormalDistribution_modeFloor = 0)
* - rule: IF (par_InNormalDistribution_modeCeil == 1, par_InNormalDistribution_modeRound = 0)
* - rule: IF (par_InNormalDistribution_modeCeil == 0, par_InNormalDistribution_modeNoRounding = 0)
* - rule: IF (par_InNormalDistribution_modeCeil == 0, par_InNormalDistribution_modeFloor = 0)
* - rule: IF (par_InNormalDistribution_modeCeil == 0, par_InNormalDistribution_modeCeil = 1)
* - rule: IF (par_InNormalDistribution_modeCeil == 0, par_InNormalDistribution_modeRound = 0)
* - type: Boolean
PARAMETER par_InNormalDistribution_modeCeil(set_InNormalDistribution)

* - default: 0
* - domain: [0|1]
* - description: Alles kleiner ,5 wird abgerundet und alles größer bzw. gleich ,5 wird aufgerundet.
* - identifier: Kaufmännisches Runden
* - rule: IF (par_InNormalDistribution_modeRound == 1, par_InNormalDistribution_modeNoRounding = 0)
* - rule: IF (par_InNormalDistribution_modeRound == 1, par_InNormalDistribution_modeFloor = 0)
* - rule: IF (par_InNormalDistribution_modeRound == 1, par_InNormalDistribution_modeCeil = 0)
* - rule: IF (par_InNormalDistribution_modeRound == 0, par_InNormalDistribution_modeNoRounding = 0)
* - rule: IF (par_InNormalDistribution_modeRound == 0, par_InNormalDistribution_modeFloor = 0)
* - rule: IF (par_InNormalDistribution_modeRound == 0, par_InNormalDistribution_modeCeil = 0)
* - rule: IF (par_InNormalDistribution_modeRound == 0, par_InNormalDistribution_modeRound = 1)
* - type: Boolean
PARAMETER par_InNormalDistribution_modeRound(set_InNormalDistribution)

* - identifier: InSlowTruncatedNormalDistribution
* - type: String
SET set_InSlowTruncatedNormalDistribution(set_InUnivariateDoubleDistribution)

* - default: 1
* - description: Legt die Standardabweichung fest.
* - identifier: Standardabweichung
* - type: Float
PARAMETER par_InSlowTruncatedNormalDistribution_standardDeviation(set_InSlowTruncatedNormalDistribution)

* - default: 0
* - description: Legt den Mittelwert fest.
* - identifier: Mittelwert
* - type: Float
PARAMETER par_InSlowTruncatedNormalDistribution_mean(set_InSlowTruncatedNormalDistribution)

* - default: -1
* - description: Legt die untere Grenze fest.
* - identifier: Untere Grenze
* - type: Float
PARAMETER par_InSlowTruncatedNormalDistribution_lowerBound(set_InSlowTruncatedNormalDistribution)

* - default: 1
* - description: Gibt an, ob die untere Grenze inklusive sein soll.
* - identifier: Untere Grenze inklusive?
* - type: Boolean
PARAMETER par_InSlowTruncatedNormalDistribution_lowerBoundInclusive(set_InSlowTruncatedNormalDistribution)

* - default: 1
* - description: Legt die obere Grenze fest.
* - identifier: Obere Grenze
* - type: Float
PARAMETER par_InSlowTruncatedNormalDistribution_upperBound(set_InSlowTruncatedNormalDistribution)

* - default: 1
* - description: Gibt an, ob die obere Grenze inklusive sein soll.
* - identifier: Obere Grenze inklusive?
* - type: Boolean
PARAMETER par_InSlowTruncatedNormalDistribution_upperBoundInclusive(set_InSlowTruncatedNormalDistribution)

* - default: 1
* - domain: [0|1]
* - description: Es wird nicht gerundet. Die Zahl bleibt unverändert.
* - identifier: Kein Runden
* - rule: IF (par_InSlowTruncatedNormalDistribution_modeNoRounding == 1, par_InSlowTruncatedNormalDistribution_modeFloor = 0)
* - rule: IF (par_InSlowTruncatedNormalDistribution_modeNoRounding == 1, par_InSlowTruncatedNormalDistribution_modeCeil = 0)
* - rule: IF (par_InSlowTruncatedNormalDistribution_modeNoRounding == 1, par_InSlowTruncatedNormalDistribution_modeRound = 0)
* - rule: IF (par_InSlowTruncatedNormalDistribution_modeNoRounding == 0, par_InSlowTruncatedNormalDistribution_modeNoRounding = 1)
* - rule: IF (par_InSlowTruncatedNormalDistribution_modeNoRounding == 0, par_InSlowTruncatedNormalDistribution_modeFloor = 0)
* - rule: IF (par_InSlowTruncatedNormalDistribution_modeNoRounding == 0, par_InSlowTruncatedNormalDistribution_modeCeil = 0)
* - rule: IF (par_InSlowTruncatedNormalDistribution_modeNoRounding == 0, par_InSlowTruncatedNormalDistribution_modeRound = 0)
* - type: Boolean
PARAMETER par_InSlowTruncatedNormalDistribution_modeNoRounding(set_InSlowTruncatedNormalDistribution)

* - default: 0
* - domain: [0|1]
* - description: Die Zahl wird immer abgerundet.
* - identifier: Abrunden
* - rule: IF (par_InSlowTruncatedNormalDistribution_modeFloor == 1, par_InSlowTruncatedNormalDistribution_modeNoRounding = 0)
* - rule: IF (par_InSlowTruncatedNormalDistribution_modeFloor == 1, par_InSlowTruncatedNormalDistribution_modeCeil = 0)
* - rule: IF (par_InSlowTruncatedNormalDistribution_modeFloor == 1, par_InSlowTruncatedNormalDistribution_modeRound = 0)
* - rule: IF (par_InSlowTruncatedNormalDistribution_modeFloor == 0, par_InSlowTruncatedNormalDistribution_modeNoRounding = 0)
* - rule: IF (par_InSlowTruncatedNormalDistribution_modeFloor == 0, par_InSlowTruncatedNormalDistribution_modeFloor = 1)
* - rule: IF (par_InSlowTruncatedNormalDistribution_modeFloor == 0, par_InSlowTruncatedNormalDistribution_modeCeil = 0)
* - rule: IF (par_InSlowTruncatedNormalDistribution_modeFloor == 0, par_InSlowTruncatedNormalDistribution_modeRound = 0)
* - type: Boolean
PARAMETER par_InSlowTruncatedNormalDistribution_modeFloor(set_InSlowTruncatedNormalDistribution)

* - default: 0
* - domain: [0|1]
* - description: Die Zahl wird immer aufgerundet.
* - identifier: Aufrunden
* - rule: IF (par_InSlowTruncatedNormalDistribution_modeCeil == 1, par_InSlowTruncatedNormalDistribution_modeNoRounding = 0)
* - rule: IF (par_InSlowTruncatedNormalDistribution_modeCeil == 1, par_InSlowTruncatedNormalDistribution_modeFloor = 0)
* - rule: IF (par_InSlowTruncatedNormalDistribution_modeCeil == 1, par_InSlowTruncatedNormalDistribution_modeRound = 0)
* - rule: IF (par_InSlowTruncatedNormalDistribution_modeCeil == 0, par_InSlowTruncatedNormalDistribution_modeNoRounding = 0)
* - rule: IF (par_InSlowTruncatedNormalDistribution_modeCeil == 0, par_InSlowTruncatedNormalDistribution_modeFloor = 0)
* - rule: IF (par_InSlowTruncatedNormalDistribution_modeCeil == 0, par_InSlowTruncatedNormalDistribution_modeCeil = 1)
* - rule: IF (par_InSlowTruncatedNormalDistribution_modeCeil == 0, par_InSlowTruncatedNormalDistribution_modeRound = 0)
* - type: Boolean
PARAMETER par_InSlowTruncatedNormalDistribution_modeCeil(set_InSlowTruncatedNormalDistribution)

* - default: 0
* - domain: [0|1]
* - description: Alles kleiner ,5 wird abgerundet und alles größer bzw. gleich ,5 wird aufgerundet.
* - identifier: Kaufmännisches Runden
* - rule: IF (par_InSlowTruncatedNormalDistribution_modeRound == 1, par_InSlowTruncatedNormalDistribution_modeNoRounding = 0)
* - rule: IF (par_InSlowTruncatedNormalDistribution_modeRound == 1, par_InSlowTruncatedNormalDistribution_modeFloor = 0)
* - rule: IF (par_InSlowTruncatedNormalDistribution_modeRound == 1, par_InSlowTruncatedNormalDistribution_modeCeil = 0)
* - rule: IF (par_InSlowTruncatedNormalDistribution_modeRound == 0, par_InSlowTruncatedNormalDistribution_modeNoRounding = 0)
* - rule: IF (par_InSlowTruncatedNormalDistribution_modeRound == 0, par_InSlowTruncatedNormalDistribution_modeFloor = 0)
* - rule: IF (par_InSlowTruncatedNormalDistribution_modeRound == 0, par_InSlowTruncatedNormalDistribution_modeCeil = 0)
* - rule: IF (par_InSlowTruncatedNormalDistribution_modeRound == 0, par_InSlowTruncatedNormalDistribution_modeRound = 1)
* - type: Boolean
PARAMETER par_InSlowTruncatedNormalDistribution_modeRound(set_InSlowTruncatedNormalDistribution)

* - identifier: InTruncatedNormalDistribution
* - type: String
SET set_InTruncatedNormalDistribution(set_InUnivariateDoubleDistribution)

* - default: 1
* - description: Die Standardabweichung der Normalverteilung.
* - identifier: Standardabweichung
* - type: Float
PARAMETER par_InTruncatedNormalDistribution_standardDeviation(set_InTruncatedNormalDistribution)

* - default: 0
* - description: Der Mittelwert der Normalverteilung.
* - identifier: Mittelwert
* - type: Float
PARAMETER par_InTruncatedNormalDistribution_mean(set_InTruncatedNormalDistribution)

* - default: -1
* - description: Untere Grenze des Bereiches für die gültigen Werte.
* - identifier: Untere Grenze
* - type: Float
PARAMETER par_InTruncatedNormalDistribution_lowerBound(set_InTruncatedNormalDistribution)

* - default: 1
* - description: Obere Grenze des Bereiches für die gültigen Werte.
* - identifier: Obere Grenze
* - type: Float
PARAMETER par_InTruncatedNormalDistribution_upperBound(set_InTruncatedNormalDistribution)

* - default: 1
* - domain: [0|1]
* - description: Es wird nicht gerundet. Die Zahl bleibt unverändert.
* - identifier: Kein Runden
* - rule: IF (par_InTruncatedNormalDistribution_modeNoRounding == 1, par_InTruncatedNormalDistribution_modeFloor = 0)
* - rule: IF (par_InTruncatedNormalDistribution_modeNoRounding == 1, par_InTruncatedNormalDistribution_modeCeil = 0)
* - rule: IF (par_InTruncatedNormalDistribution_modeNoRounding == 1, par_InTruncatedNormalDistribution_modeRound = 0)
* - rule: IF (par_InTruncatedNormalDistribution_modeNoRounding == 0, par_InTruncatedNormalDistribution_modeNoRounding = 1)
* - rule: IF (par_InTruncatedNormalDistribution_modeNoRounding == 0, par_InTruncatedNormalDistribution_modeFloor = 0)
* - rule: IF (par_InTruncatedNormalDistribution_modeNoRounding == 0, par_InTruncatedNormalDistribution_modeCeil = 0)
* - rule: IF (par_InTruncatedNormalDistribution_modeNoRounding == 0, par_InTruncatedNormalDistribution_modeRound = 0)
* - type: Boolean
PARAMETER par_InTruncatedNormalDistribution_modeNoRounding(set_InTruncatedNormalDistribution)

* - default: 0
* - domain: [0|1]
* - description: Die Zahl wird immer abgerundet.
* - identifier: Abrunden
* - rule: IF (par_InTruncatedNormalDistribution_modeFloor == 1, par_InTruncatedNormalDistribution_modeNoRounding = 0)
* - rule: IF (par_InTruncatedNormalDistribution_modeFloor == 1, par_InTruncatedNormalDistribution_modeCeil = 0)
* - rule: IF (par_InTruncatedNormalDistribution_modeFloor == 1, par_InTruncatedNormalDistribution_modeRound = 0)
* - rule: IF (par_InTruncatedNormalDistribution_modeFloor == 0, par_InTruncatedNormalDistribution_modeNoRounding = 0)
* - rule: IF (par_InTruncatedNormalDistribution_modeFloor == 0, par_InTruncatedNormalDistribution_modeFloor = 1)
* - rule: IF (par_InTruncatedNormalDistribution_modeFloor == 0, par_InTruncatedNormalDistribution_modeCeil = 0)
* - rule: IF (par_InTruncatedNormalDistribution_modeFloor == 0, par_InTruncatedNormalDistribution_modeRound = 0)
* - type: Boolean
PARAMETER par_InTruncatedNormalDistribution_modeFloor(set_InTruncatedNormalDistribution)

* - default: 0
* - domain: [0|1]
* - description: Die Zahl wird immer aufgerundet.
* - identifier: Aufrunden
* - rule: IF (par_InTruncatedNormalDistribution_modeCeil == 1, par_InTruncatedNormalDistribution_modeNoRounding = 0)
* - rule: IF (par_InTruncatedNormalDistribution_modeCeil == 1, par_InTruncatedNormalDistribution_modeFloor = 0)
* - rule: IF (par_InTruncatedNormalDistribution_modeCeil == 1, par_InTruncatedNormalDistribution_modeRound = 0)
* - rule: IF (par_InTruncatedNormalDistribution_modeCeil == 0, par_InTruncatedNormalDistribution_modeNoRounding = 0)
* - rule: IF (par_InTruncatedNormalDistribution_modeCeil == 0, par_InTruncatedNormalDistribution_modeFloor = 0)
* - rule: IF (par_InTruncatedNormalDistribution_modeCeil == 0, par_InTruncatedNormalDistribution_modeCeil = 1)
* - rule: IF (par_InTruncatedNormalDistribution_modeCeil == 0, par_InTruncatedNormalDistribution_modeRound = 0)
* - type: Boolean
PARAMETER par_InTruncatedNormalDistribution_modeCeil(set_InTruncatedNormalDistribution)

* - default: 0
* - domain: [0|1]
* - description: Alles kleiner ,5 wird abgerundet und alles größer bzw. gleich ,5 wird aufgerundet.
* - identifier: Kaufmännisches Runden
* - rule: IF (par_InTruncatedNormalDistribution_modeRound == 1, par_InTruncatedNormalDistribution_modeNoRounding = 0)
* - rule: IF (par_InTruncatedNormalDistribution_modeRound == 1, par_InTruncatedNormalDistribution_modeFloor = 0)
* - rule: IF (par_InTruncatedNormalDistribution_modeRound == 1, par_InTruncatedNormalDistribution_modeCeil = 0)
* - rule: IF (par_InTruncatedNormalDistribution_modeRound == 0, par_InTruncatedNormalDistribution_modeNoRounding = 0)
* - rule: IF (par_InTruncatedNormalDistribution_modeRound == 0, par_InTruncatedNormalDistribution_modeFloor = 0)
* - rule: IF (par_InTruncatedNormalDistribution_modeRound == 0, par_InTruncatedNormalDistribution_modeCeil = 0)
* - rule: IF (par_InTruncatedNormalDistribution_modeRound == 0, par_InTruncatedNormalDistribution_modeRound = 1)
* - type: Boolean
PARAMETER par_InTruncatedNormalDistribution_modeRound(set_InTruncatedNormalDistribution)

* - identifier: InUnivariateDoubleDistribution
* - hidden: 1
* - type: String
SET set_InUnivariateDoubleDistribution(*)

* - identifier: InFile
* - hidden: 1
* - type: String
SET set_InFile(*)

* - identifier: InPVFile
* - type: String
SET set_InPVFile(set_InFile)

* - description: Ungenutzter Platzhalter
* - identifier: ---
* - type: Float
PARAMETER par_InPVFile_placeholderPVFile(set_InPVFile)

* - identifier: InRealAdoptionDataFile
* - type: String
SET set_InRealAdoptionDataFile(set_InFile)

* - description: Ungenutzter Platzhalter
* - identifier: ---
* - type: Float
PARAMETER par_InRealAdoptionDataFile_placeholder(set_InRealAdoptionDataFile)

* - identifier: InSpatialTableFile
* - type: String
SET set_InSpatialTableFile(set_InFile)

* - default: 1
* - domain: [0,)
* - description: Gibt an, wie viel die räumlichen Daten im Vergleich zur Realität abbilden.
* - identifier: Abdeckungsfaktor
* - type: Float
PARAMETER par_InSpatialTableFile_coverage(set_InSpatialTableFile)

* - identifier: InGenericOutputImage
* - type: String
SET set_InGenericOutputImage(set_InOutputImage)

* - default: 1
* - domain: [0|1]
* - description: Erzeugt das Bild mittels gnuplot.
* - identifier: Programm: gnuplot
* - rule: IF (par_InGenericOutputImage_useGnuplot == 1, par_InGenericOutputImage_useR = 0)
* - rule: IF (par_InGenericOutputImage_useGnuplot == 0, par_InGenericOutputImage_useGnuplot = 1)
* - rule: IF (par_InGenericOutputImage_useGnuplot == 0, par_InGenericOutputImage_useR = 0)
* - type: Boolean
PARAMETER par_InGenericOutputImage_useGnuplot(set_InGenericOutputImage)

* - default: 0
* - domain: [0|1]
* - description: Erzeugt das Bild mittels R.
* - identifier: Programm: R
* - rule: IF (par_InGenericOutputImage_useR == 1, par_InGenericOutputImage_useGnuplot = 0)
* - rule: IF (par_InGenericOutputImage_useR == 0, par_InGenericOutputImage_useGnuplot = 0)
* - rule: IF (par_InGenericOutputImage_useR == 0, par_InGenericOutputImage_useR = 1)
* - type: Boolean
PARAMETER par_InGenericOutputImage_useR(set_InGenericOutputImage)

* - default: 1
* - domain: [0|1]
* - description: Stellt die jährlichen Adoptionen, aufgeschlüsselt nach PLZ, in einer line chart dar.
* - identifier: Darstellung: jährliche Adoptionen (PLZ)
* - rule: IF (par_InGenericOutputImage_annualZip == 1, par_InGenericOutputImage_annualZipWithReal = 0)
* - rule: IF (par_InGenericOutputImage_annualZip == 1, par_InGenericOutputImage_annualZipWithRealTotal = 0)
* - rule: IF (par_InGenericOutputImage_annualZip == 1, par_InGenericOutputImage_cumulativeAnnualPhase = 0)
* - rule: IF (par_InGenericOutputImage_annualZip == 1, par_InGenericOutputImage_cumulativeAnnualPhase2 = 0)
* - rule: IF (par_InGenericOutputImage_annualZip == 1, par_InGenericOutputImage_annualInterest2D = 0)
* - rule: IF (par_InGenericOutputImage_annualZip == 1, par_InGenericOutputImage_annualPhaseOverview = 0)
* - rule: IF (par_InGenericOutputImage_annualZip == 0, par_InGenericOutputImage_annualZip = 1)
* - rule: IF (par_InGenericOutputImage_annualZip == 0, par_InGenericOutputImage_annualZipWithReal = 0)
* - rule: IF (par_InGenericOutputImage_annualZip == 0, par_InGenericOutputImage_annualZipWithRealTotal = 0)
* - rule: IF (par_InGenericOutputImage_annualZip == 0, par_InGenericOutputImage_cumulativeAnnualPhase = 0)
* - rule: IF (par_InGenericOutputImage_annualZip == 0, par_InGenericOutputImage_cumulativeAnnualPhase2 = 0)
* - rule: IF (par_InGenericOutputImage_annualZip == 0, par_InGenericOutputImage_annualInterest2D = 0)
* - rule: IF (par_InGenericOutputImage_annualZip == 0, par_InGenericOutputImage_annualPhaseOverview = 0)
* - type: Boolean
PARAMETER par_InGenericOutputImage_annualZip(set_InGenericOutputImage)

* - default: 0
* - domain: [0|1]
* - description: Stellt die jährlichen Adoptionen, aufgeschlüsselt nach PLZ, in einer line chart dar. Zusätzlich werden die realen Adoptionen mit dargestellt.
* - identifier: Darstellung: jährliche Adoptionen im Vergleich zu realen Daten (PLZ)
* - rule: IF (par_InGenericOutputImage_annualZipWithReal == 1, par_InGenericOutputImage_annualZip = 0)
* - rule: IF (par_InGenericOutputImage_annualZipWithReal == 1, par_InGenericOutputImage_annualZipWithRealTotal = 0)
* - rule: IF (par_InGenericOutputImage_annualZipWithReal == 1, par_InGenericOutputImage_cumulativeAnnualPhase = 0)
* - rule: IF (par_InGenericOutputImage_annualZipWithReal == 1, par_InGenericOutputImage_cumulativeAnnualPhase2 = 0)
* - rule: IF (par_InGenericOutputImage_annualZipWithReal == 1, par_InGenericOutputImage_annualInterest2D = 0)
* - rule: IF (par_InGenericOutputImage_annualZipWithReal == 1, par_InGenericOutputImage_annualPhaseOverview = 0)
* - rule: IF (par_InGenericOutputImage_annualZipWithReal == 0, par_InGenericOutputImage_annualZip = 0)
* - rule: IF (par_InGenericOutputImage_annualZipWithReal == 0, par_InGenericOutputImage_annualZipWithReal = 1)
* - rule: IF (par_InGenericOutputImage_annualZipWithReal == 0, par_InGenericOutputImage_annualZipWithRealTotal = 0)
* - rule: IF (par_InGenericOutputImage_annualZipWithReal == 0, par_InGenericOutputImage_cumulativeAnnualPhase = 0)
* - rule: IF (par_InGenericOutputImage_annualZipWithReal == 0, par_InGenericOutputImage_cumulativeAnnualPhase2 = 0)
* - rule: IF (par_InGenericOutputImage_annualZipWithReal == 0, par_InGenericOutputImage_annualInterest2D = 0)
* - rule: IF (par_InGenericOutputImage_annualZipWithReal == 0, par_InGenericOutputImage_annualPhaseOverview = 0)
* - type: Boolean
PARAMETER par_InGenericOutputImage_annualZipWithReal(set_InGenericOutputImage)

* - default: 0
* - domain: [0|1]
* - description: Stellt die jährlichen Adoptionen in einer line chart dar. Zusätzlich werden die realen Adoptionen mit dargestellt.
* - identifier: Darstellung: jährliche Adoptionen im Vergleich zu realen Daten
* - rule: IF (par_InGenericOutputImage_annualZipWithRealTotal == 1, par_InGenericOutputImage_annualZip = 0)
* - rule: IF (par_InGenericOutputImage_annualZipWithRealTotal == 1, par_InGenericOutputImage_annualZipWithReal = 0)
* - rule: IF (par_InGenericOutputImage_annualZipWithRealTotal == 1, par_InGenericOutputImage_cumulativeAnnualPhase = 0)
* - rule: IF (par_InGenericOutputImage_annualZipWithRealTotal == 1, par_InGenericOutputImage_cumulativeAnnualPhase2 = 0)
* - rule: IF (par_InGenericOutputImage_annualZipWithRealTotal == 1, par_InGenericOutputImage_annualInterest2D = 0)
* - rule: IF (par_InGenericOutputImage_annualZipWithRealTotal == 1, par_InGenericOutputImage_annualPhaseOverview = 0)
* - rule: IF (par_InGenericOutputImage_annualZipWithRealTotal == 0, par_InGenericOutputImage_annualZip = 0)
* - rule: IF (par_InGenericOutputImage_annualZipWithRealTotal == 0, par_InGenericOutputImage_annualZipWithReal = 0)
* - rule: IF (par_InGenericOutputImage_annualZipWithRealTotal == 0, par_InGenericOutputImage_annualZipWithRealTotal = 1)
* - rule: IF (par_InGenericOutputImage_annualZipWithRealTotal == 0, par_InGenericOutputImage_cumulativeAnnualPhase = 0)
* - rule: IF (par_InGenericOutputImage_annualZipWithRealTotal == 0, par_InGenericOutputImage_cumulativeAnnualPhase2 = 0)
* - rule: IF (par_InGenericOutputImage_annualZipWithRealTotal == 0, par_InGenericOutputImage_annualInterest2D = 0)
* - rule: IF (par_InGenericOutputImage_annualZipWithRealTotal == 0, par_InGenericOutputImage_annualPhaseOverview = 0)
* - type: Boolean
PARAMETER par_InGenericOutputImage_annualZipWithRealTotal(set_InGenericOutputImage)

* - default: 0
* - domain: [0|1]
* - description: Stellt die kumulierten jährlichen Adoptionen, aufgeschlüsselt nach der Adoptionsphase, in einer stacked bar chart dar. Die initialen Adopter werden nicht berücksichtigt.
* - identifier: Darstellung: kumulierte jährliche Adoptionen (Phase) (ohne Initiale)
* - rule: IF (par_InGenericOutputImage_cumulativeAnnualPhase == 1, par_InGenericOutputImage_annualZip = 0)
* - rule: IF (par_InGenericOutputImage_cumulativeAnnualPhase == 1, par_InGenericOutputImage_annualZipWithReal = 0)
* - rule: IF (par_InGenericOutputImage_cumulativeAnnualPhase == 1, par_InGenericOutputImage_annualZipWithRealTotal = 0)
* - rule: IF (par_InGenericOutputImage_cumulativeAnnualPhase == 1, par_InGenericOutputImage_cumulativeAnnualPhase2 = 0)
* - rule: IF (par_InGenericOutputImage_cumulativeAnnualPhase == 1, par_InGenericOutputImage_annualInterest2D = 0)
* - rule: IF (par_InGenericOutputImage_cumulativeAnnualPhase == 1, par_InGenericOutputImage_annualPhaseOverview = 0)
* - rule: IF (par_InGenericOutputImage_cumulativeAnnualPhase == 0, par_InGenericOutputImage_annualZip = 0)
* - rule: IF (par_InGenericOutputImage_cumulativeAnnualPhase == 0, par_InGenericOutputImage_annualZipWithReal = 0)
* - rule: IF (par_InGenericOutputImage_cumulativeAnnualPhase == 0, par_InGenericOutputImage_annualZipWithRealTotal = 0)
* - rule: IF (par_InGenericOutputImage_cumulativeAnnualPhase == 0, par_InGenericOutputImage_cumulativeAnnualPhase = 1)
* - rule: IF (par_InGenericOutputImage_cumulativeAnnualPhase == 0, par_InGenericOutputImage_cumulativeAnnualPhase2 = 0)
* - rule: IF (par_InGenericOutputImage_cumulativeAnnualPhase == 0, par_InGenericOutputImage_annualInterest2D = 0)
* - rule: IF (par_InGenericOutputImage_cumulativeAnnualPhase == 0, par_InGenericOutputImage_annualPhaseOverview = 0)
* - type: Boolean
PARAMETER par_InGenericOutputImage_cumulativeAnnualPhase(set_InGenericOutputImage)

* - default: 0
* - domain: [0|1]
* - description: Stellt die kumulierten jährlichen Adoptionen, aufgeschlüsselt nach der Adoptionsphase, in einer stacked bar chart dar. Die initialen Adopter werden bei den Daten berücksichtigt, aber nicht direkt visualisiert.
* - identifier: Darstellung: kumulierte jährliche Adoptionen (Phase) (mit Initialen)
* - rule: IF (par_InGenericOutputImage_cumulativeAnnualPhase2 == 1, par_InGenericOutputImage_annualZip = 0)
* - rule: IF (par_InGenericOutputImage_cumulativeAnnualPhase2 == 1, par_InGenericOutputImage_annualZipWithReal = 0)
* - rule: IF (par_InGenericOutputImage_cumulativeAnnualPhase2 == 1, par_InGenericOutputImage_annualZipWithRealTotal = 0)
* - rule: IF (par_InGenericOutputImage_cumulativeAnnualPhase2 == 1, par_InGenericOutputImage_cumulativeAnnualPhase = 0)
* - rule: IF (par_InGenericOutputImage_cumulativeAnnualPhase2 == 1, par_InGenericOutputImage_annualInterest2D = 0)
* - rule: IF (par_InGenericOutputImage_cumulativeAnnualPhase2 == 1, par_InGenericOutputImage_annualPhaseOverview = 0)
* - rule: IF (par_InGenericOutputImage_cumulativeAnnualPhase2 == 0, par_InGenericOutputImage_annualZip = 0)
* - rule: IF (par_InGenericOutputImage_cumulativeAnnualPhase2 == 0, par_InGenericOutputImage_annualZipWithReal = 0)
* - rule: IF (par_InGenericOutputImage_cumulativeAnnualPhase2 == 0, par_InGenericOutputImage_annualZipWithRealTotal = 0)
* - rule: IF (par_InGenericOutputImage_cumulativeAnnualPhase2 == 0, par_InGenericOutputImage_cumulativeAnnualPhase = 0)
* - rule: IF (par_InGenericOutputImage_cumulativeAnnualPhase2 == 0, par_InGenericOutputImage_cumulativeAnnualPhase2 = 1)
* - rule: IF (par_InGenericOutputImage_cumulativeAnnualPhase2 == 0, par_InGenericOutputImage_annualInterest2D = 0)
* - rule: IF (par_InGenericOutputImage_cumulativeAnnualPhase2 == 0, par_InGenericOutputImage_annualPhaseOverview = 0)
* - type: Boolean
PARAMETER par_InGenericOutputImage_cumulativeAnnualPhase2(set_InGenericOutputImage)

* - default: 0
* - domain: [0|1]
* - description: Stellt die Entwicklung des jährlichen Interesses dar.
* - identifier: Darstellung: Jährliches Interesse (2D)
* - rule: IF (par_InGenericOutputImage_annualInterest2D == 1, par_InGenericOutputImage_annualZip = 0)
* - rule: IF (par_InGenericOutputImage_annualInterest2D == 1, par_InGenericOutputImage_annualZipWithReal = 0)
* - rule: IF (par_InGenericOutputImage_annualInterest2D == 1, par_InGenericOutputImage_annualZipWithRealTotal = 0)
* - rule: IF (par_InGenericOutputImage_annualInterest2D == 1, par_InGenericOutputImage_cumulativeAnnualPhase = 0)
* - rule: IF (par_InGenericOutputImage_annualInterest2D == 1, par_InGenericOutputImage_cumulativeAnnualPhase2 = 0)
* - rule: IF (par_InGenericOutputImage_annualInterest2D == 1, par_InGenericOutputImage_annualPhaseOverview = 0)
* - rule: IF (par_InGenericOutputImage_annualInterest2D == 0, par_InGenericOutputImage_annualZip = 0)
* - rule: IF (par_InGenericOutputImage_annualInterest2D == 0, par_InGenericOutputImage_annualZipWithReal = 0)
* - rule: IF (par_InGenericOutputImage_annualInterest2D == 0, par_InGenericOutputImage_annualZipWithRealTotal = 0)
* - rule: IF (par_InGenericOutputImage_annualInterest2D == 0, par_InGenericOutputImage_cumulativeAnnualPhase = 0)
* - rule: IF (par_InGenericOutputImage_annualInterest2D == 0, par_InGenericOutputImage_cumulativeAnnualPhase2 = 0)
* - rule: IF (par_InGenericOutputImage_annualInterest2D == 0, par_InGenericOutputImage_annualInterest2D = 1)
* - rule: IF (par_InGenericOutputImage_annualInterest2D == 0, par_InGenericOutputImage_annualPhaseOverview = 0)
* - type: Boolean
PARAMETER par_InGenericOutputImage_annualInterest2D(set_InGenericOutputImage)

* - default: 0
* - domain: [0|1]
* - description: Stellt die Entwicklung des Phasen dar.
* - identifier: Darstellung: Jährliche Phasenübersicht
* - rule: IF (par_InGenericOutputImage_annualPhaseOverview == 1, par_InGenericOutputImage_annualZip = 0)
* - rule: IF (par_InGenericOutputImage_annualPhaseOverview == 1, par_InGenericOutputImage_annualZipWithReal = 0)
* - rule: IF (par_InGenericOutputImage_annualPhaseOverview == 1, par_InGenericOutputImage_annualZipWithRealTotal = 0)
* - rule: IF (par_InGenericOutputImage_annualPhaseOverview == 1, par_InGenericOutputImage_cumulativeAnnualPhase = 0)
* - rule: IF (par_InGenericOutputImage_annualPhaseOverview == 1, par_InGenericOutputImage_cumulativeAnnualPhase2 = 0)
* - rule: IF (par_InGenericOutputImage_annualPhaseOverview == 1, par_InGenericOutputImage_annualInterest2D = 0)
* - rule: IF (par_InGenericOutputImage_annualPhaseOverview == 0, par_InGenericOutputImage_annualZip = 0)
* - rule: IF (par_InGenericOutputImage_annualPhaseOverview == 0, par_InGenericOutputImage_annualZipWithReal = 0)
* - rule: IF (par_InGenericOutputImage_annualPhaseOverview == 0, par_InGenericOutputImage_annualZipWithRealTotal = 0)
* - rule: IF (par_InGenericOutputImage_annualPhaseOverview == 0, par_InGenericOutputImage_cumulativeAnnualPhase = 0)
* - rule: IF (par_InGenericOutputImage_annualPhaseOverview == 0, par_InGenericOutputImage_cumulativeAnnualPhase2 = 0)
* - rule: IF (par_InGenericOutputImage_annualPhaseOverview == 0, par_InGenericOutputImage_annualInterest2D = 0)
* - rule: IF (par_InGenericOutputImage_annualPhaseOverview == 0, par_InGenericOutputImage_annualPhaseOverview = 1)
* - type: Boolean
PARAMETER par_InGenericOutputImage_annualPhaseOverview(set_InGenericOutputImage)

* - default: 0
* - domain: [0|1]
* - description: Speichert auf Wunsch das Skript für die Bilderzeugung.
* - identifier: Skript speichern?
* - type: Boolean
PARAMETER par_InGenericOutputImage_storeScript(set_InGenericOutputImage)

* - default: 0
* - domain: [0|1]
* - description: Speichert auf Wunsch die Daten für die Bilderzeugung.
* - identifier: Daten speichern?
* - type: Boolean
PARAMETER par_InGenericOutputImage_storeData(set_InGenericOutputImage)

* - default: 1
* - domain: [0|1]
* - description: Erzeugt und speichert auf Wunsch das erzeugte Bild.
* - identifier: Bild speichern?
* - type: Boolean
PARAMETER par_InGenericOutputImage_storeImage(set_InGenericOutputImage)

* - default: 1280
* - domain: (0,)
* - description: Gibt die Bildbreite in Pixeln an.
* - identifier: Bildbreite
* - unit: [Pixel]
* - type: Integer
PARAMETER par_InGenericOutputImage_imageWidth(set_InGenericOutputImage)

* - default: 720
* - domain: (0,)
* - description: Gibt die Bildhöhe in Pixeln an.
* - identifier: Bildhöhe
* - unit: [Pixel]
* - type: Integer
PARAMETER par_InGenericOutputImage_imageHeight(set_InGenericOutputImage)

* - default: 1
* - domain: (0,)
* - description: Gibt die Linienstärke an. Dieser Wert hat nur auf line plots Einfluss.
* - identifier: Linienstärke
* - type: Float
PARAMETER par_InGenericOutputImage_linewidth(set_InGenericOutputImage)

* - description: Legt die realen zu nutzenden Adoptionsdaten fest.
* - identifier: Reale Adoptionsdaten
* - type: Boolean
PARAMETER par_link_InGenericOutputImage_InRealAdoptionDataFile_realAdoptionDataFile(set_InGenericOutputImage,set_InRealAdoptionDataFile)

* - identifier: InGnuPlotOutputImage
* - type: String
SET set_InGnuPlotOutputImage(set_InOutputImage)

* - default: 1
* - domain: [0|1]
* - description: Stellt die jährlichen Adoptionen, aufgeschlüsselt nach PLZ, in einer line chart dar.
* - identifier: Darstellung: jährliche Adoptionen (PLZ)
* - rule: IF (par_InGnuPlotOutputImage_annualZip == 1, par_InGnuPlotOutputImage_annualZipWithReal = 0)
* - rule: IF (par_InGnuPlotOutputImage_annualZip == 1, par_InGnuPlotOutputImage_annualZipWithRealTotal = 0)
* - rule: IF (par_InGnuPlotOutputImage_annualZip == 1, par_InGnuPlotOutputImage_cumulativeAnnualPhase = 0)
* - rule: IF (par_InGnuPlotOutputImage_annualZip == 1, par_InGnuPlotOutputImage_cumulativeAnnualPhase2 = 0)
* - rule: IF (par_InGnuPlotOutputImage_annualZip == 1, par_InGnuPlotOutputImage_annualInterest2D = 0)
* - rule: IF (par_InGnuPlotOutputImage_annualZip == 1, par_InGnuPlotOutputImage_annualPhaseOverview = 0)
* - rule: IF (par_InGnuPlotOutputImage_annualZip == 0, par_InGnuPlotOutputImage_annualZip = 1)
* - rule: IF (par_InGnuPlotOutputImage_annualZip == 0, par_InGnuPlotOutputImage_annualZipWithReal = 0)
* - rule: IF (par_InGnuPlotOutputImage_annualZip == 0, par_InGnuPlotOutputImage_annualZipWithRealTotal = 0)
* - rule: IF (par_InGnuPlotOutputImage_annualZip == 0, par_InGnuPlotOutputImage_cumulativeAnnualPhase = 0)
* - rule: IF (par_InGnuPlotOutputImage_annualZip == 0, par_InGnuPlotOutputImage_cumulativeAnnualPhase2 = 0)
* - rule: IF (par_InGnuPlotOutputImage_annualZip == 0, par_InGnuPlotOutputImage_annualInterest2D = 0)
* - rule: IF (par_InGnuPlotOutputImage_annualZip == 0, par_InGnuPlotOutputImage_annualPhaseOverview = 0)
* - type: Boolean
PARAMETER par_InGnuPlotOutputImage_annualZip(set_InGnuPlotOutputImage)

* - default: 0
* - domain: [0|1]
* - description: Stellt die jährlichen Adoptionen, aufgeschlüsselt nach PLZ, in einer line chart dar. Zusätzlich werden die realen Adoptionen mit dargestellt.
* - identifier: Darstellung: jährliche Adoptionen im Vergleich zu realen Daten (PLZ)
* - rule: IF (par_InGnuPlotOutputImage_annualZipWithReal == 1, par_InGnuPlotOutputImage_annualZip = 0)
* - rule: IF (par_InGnuPlotOutputImage_annualZipWithReal == 1, par_InGnuPlotOutputImage_annualZipWithRealTotal = 0)
* - rule: IF (par_InGnuPlotOutputImage_annualZipWithReal == 1, par_InGnuPlotOutputImage_cumulativeAnnualPhase = 0)
* - rule: IF (par_InGnuPlotOutputImage_annualZipWithReal == 1, par_InGnuPlotOutputImage_cumulativeAnnualPhase2 = 0)
* - rule: IF (par_InGnuPlotOutputImage_annualZipWithReal == 1, par_InGnuPlotOutputImage_annualInterest2D = 0)
* - rule: IF (par_InGnuPlotOutputImage_annualZipWithReal == 1, par_InGnuPlotOutputImage_annualPhaseOverview = 0)
* - rule: IF (par_InGnuPlotOutputImage_annualZipWithReal == 0, par_InGnuPlotOutputImage_annualZip = 0)
* - rule: IF (par_InGnuPlotOutputImage_annualZipWithReal == 0, par_InGnuPlotOutputImage_annualZipWithReal = 1)
* - rule: IF (par_InGnuPlotOutputImage_annualZipWithReal == 0, par_InGnuPlotOutputImage_annualZipWithRealTotal = 0)
* - rule: IF (par_InGnuPlotOutputImage_annualZipWithReal == 0, par_InGnuPlotOutputImage_cumulativeAnnualPhase = 0)
* - rule: IF (par_InGnuPlotOutputImage_annualZipWithReal == 0, par_InGnuPlotOutputImage_cumulativeAnnualPhase2 = 0)
* - rule: IF (par_InGnuPlotOutputImage_annualZipWithReal == 0, par_InGnuPlotOutputImage_annualInterest2D = 0)
* - rule: IF (par_InGnuPlotOutputImage_annualZipWithReal == 0, par_InGnuPlotOutputImage_annualPhaseOverview = 0)
* - type: Boolean
PARAMETER par_InGnuPlotOutputImage_annualZipWithReal(set_InGnuPlotOutputImage)

* - default: 0
* - domain: [0|1]
* - description: Stellt die jährlichen Adoptionen in einer line chart dar. Zusätzlich werden die realen Adoptionen mit dargestellt.
* - identifier: Darstellung: jährliche Adoptionen im Vergleich zu realen Daten
* - rule: IF (par_InGnuPlotOutputImage_annualZipWithRealTotal == 1, par_InGnuPlotOutputImage_annualZip = 0)
* - rule: IF (par_InGnuPlotOutputImage_annualZipWithRealTotal == 1, par_InGnuPlotOutputImage_annualZipWithReal = 0)
* - rule: IF (par_InGnuPlotOutputImage_annualZipWithRealTotal == 1, par_InGnuPlotOutputImage_cumulativeAnnualPhase = 0)
* - rule: IF (par_InGnuPlotOutputImage_annualZipWithRealTotal == 1, par_InGnuPlotOutputImage_cumulativeAnnualPhase2 = 0)
* - rule: IF (par_InGnuPlotOutputImage_annualZipWithRealTotal == 1, par_InGnuPlotOutputImage_annualInterest2D = 0)
* - rule: IF (par_InGnuPlotOutputImage_annualZipWithRealTotal == 1, par_InGnuPlotOutputImage_annualPhaseOverview = 0)
* - rule: IF (par_InGnuPlotOutputImage_annualZipWithRealTotal == 0, par_InGnuPlotOutputImage_annualZip = 0)
* - rule: IF (par_InGnuPlotOutputImage_annualZipWithRealTotal == 0, par_InGnuPlotOutputImage_annualZipWithReal = 0)
* - rule: IF (par_InGnuPlotOutputImage_annualZipWithRealTotal == 0, par_InGnuPlotOutputImage_annualZipWithRealTotal = 1)
* - rule: IF (par_InGnuPlotOutputImage_annualZipWithRealTotal == 0, par_InGnuPlotOutputImage_cumulativeAnnualPhase = 0)
* - rule: IF (par_InGnuPlotOutputImage_annualZipWithRealTotal == 0, par_InGnuPlotOutputImage_cumulativeAnnualPhase2 = 0)
* - rule: IF (par_InGnuPlotOutputImage_annualZipWithRealTotal == 0, par_InGnuPlotOutputImage_annualInterest2D = 0)
* - rule: IF (par_InGnuPlotOutputImage_annualZipWithRealTotal == 0, par_InGnuPlotOutputImage_annualPhaseOverview = 0)
* - type: Boolean
PARAMETER par_InGnuPlotOutputImage_annualZipWithRealTotal(set_InGnuPlotOutputImage)

* - default: 0
* - domain: [0|1]
* - description: Stellt die kumulierten jährlichen Adoptionen, aufgeschlüsselt nach der Adoptionsphase, in einer stacked bar chart dar. Die initialen Adopter werden nicht berücksichtigt.
* - identifier: Darstellung: kumulierte jährliche Adoptionen (Phase) (ohne Initiale)
* - rule: IF (par_InGnuPlotOutputImage_cumulativeAnnualPhase == 1, par_InGnuPlotOutputImage_annualZip = 0)
* - rule: IF (par_InGnuPlotOutputImage_cumulativeAnnualPhase == 1, par_InGnuPlotOutputImage_annualZipWithReal = 0)
* - rule: IF (par_InGnuPlotOutputImage_cumulativeAnnualPhase == 1, par_InGnuPlotOutputImage_annualZipWithRealTotal = 0)
* - rule: IF (par_InGnuPlotOutputImage_cumulativeAnnualPhase == 1, par_InGnuPlotOutputImage_cumulativeAnnualPhase2 = 0)
* - rule: IF (par_InGnuPlotOutputImage_cumulativeAnnualPhase == 1, par_InGnuPlotOutputImage_annualInterest2D = 0)
* - rule: IF (par_InGnuPlotOutputImage_cumulativeAnnualPhase == 1, par_InGnuPlotOutputImage_annualPhaseOverview = 0)
* - rule: IF (par_InGnuPlotOutputImage_cumulativeAnnualPhase == 0, par_InGnuPlotOutputImage_annualZip = 0)
* - rule: IF (par_InGnuPlotOutputImage_cumulativeAnnualPhase == 0, par_InGnuPlotOutputImage_annualZipWithReal = 0)
* - rule: IF (par_InGnuPlotOutputImage_cumulativeAnnualPhase == 0, par_InGnuPlotOutputImage_annualZipWithRealTotal = 0)
* - rule: IF (par_InGnuPlotOutputImage_cumulativeAnnualPhase == 0, par_InGnuPlotOutputImage_cumulativeAnnualPhase = 1)
* - rule: IF (par_InGnuPlotOutputImage_cumulativeAnnualPhase == 0, par_InGnuPlotOutputImage_cumulativeAnnualPhase2 = 0)
* - rule: IF (par_InGnuPlotOutputImage_cumulativeAnnualPhase == 0, par_InGnuPlotOutputImage_annualInterest2D = 0)
* - rule: IF (par_InGnuPlotOutputImage_cumulativeAnnualPhase == 0, par_InGnuPlotOutputImage_annualPhaseOverview = 0)
* - type: Boolean
PARAMETER par_InGnuPlotOutputImage_cumulativeAnnualPhase(set_InGnuPlotOutputImage)

* - default: 0
* - domain: [0|1]
* - description: Stellt die kumulierten jährlichen Adoptionen, aufgeschlüsselt nach der Adoptionsphase, in einer stacked bar chart dar. Die initialen Adopter werden bei den Daten berücksichtigt, aber nicht direkt visualisiert.
* - identifier: Darstellung: kumulierte jährliche Adoptionen (Phase) (mit Initialen)
* - rule: IF (par_InGnuPlotOutputImage_cumulativeAnnualPhase2 == 1, par_InGnuPlotOutputImage_annualZip = 0)
* - rule: IF (par_InGnuPlotOutputImage_cumulativeAnnualPhase2 == 1, par_InGnuPlotOutputImage_annualZipWithReal = 0)
* - rule: IF (par_InGnuPlotOutputImage_cumulativeAnnualPhase2 == 1, par_InGnuPlotOutputImage_annualZipWithRealTotal = 0)
* - rule: IF (par_InGnuPlotOutputImage_cumulativeAnnualPhase2 == 1, par_InGnuPlotOutputImage_cumulativeAnnualPhase = 0)
* - rule: IF (par_InGnuPlotOutputImage_cumulativeAnnualPhase2 == 1, par_InGnuPlotOutputImage_annualInterest2D = 0)
* - rule: IF (par_InGnuPlotOutputImage_cumulativeAnnualPhase2 == 1, par_InGnuPlotOutputImage_annualPhaseOverview = 0)
* - rule: IF (par_InGnuPlotOutputImage_cumulativeAnnualPhase2 == 0, par_InGnuPlotOutputImage_annualZip = 0)
* - rule: IF (par_InGnuPlotOutputImage_cumulativeAnnualPhase2 == 0, par_InGnuPlotOutputImage_annualZipWithReal = 0)
* - rule: IF (par_InGnuPlotOutputImage_cumulativeAnnualPhase2 == 0, par_InGnuPlotOutputImage_annualZipWithRealTotal = 0)
* - rule: IF (par_InGnuPlotOutputImage_cumulativeAnnualPhase2 == 0, par_InGnuPlotOutputImage_cumulativeAnnualPhase = 0)
* - rule: IF (par_InGnuPlotOutputImage_cumulativeAnnualPhase2 == 0, par_InGnuPlotOutputImage_cumulativeAnnualPhase2 = 1)
* - rule: IF (par_InGnuPlotOutputImage_cumulativeAnnualPhase2 == 0, par_InGnuPlotOutputImage_annualInterest2D = 0)
* - rule: IF (par_InGnuPlotOutputImage_cumulativeAnnualPhase2 == 0, par_InGnuPlotOutputImage_annualPhaseOverview = 0)
* - type: Boolean
PARAMETER par_InGnuPlotOutputImage_cumulativeAnnualPhase2(set_InGnuPlotOutputImage)

* - default: 0
* - domain: [0|1]
* - description: Stellt die Entwicklung des jährlichen Interesses dar.
* - identifier: Darstellung: Jährliches Interesse (2D)
* - rule: IF (par_InGnuPlotOutputImage_annualInterest2D == 1, par_InGnuPlotOutputImage_annualZip = 0)
* - rule: IF (par_InGnuPlotOutputImage_annualInterest2D == 1, par_InGnuPlotOutputImage_annualZipWithReal = 0)
* - rule: IF (par_InGnuPlotOutputImage_annualInterest2D == 1, par_InGnuPlotOutputImage_annualZipWithRealTotal = 0)
* - rule: IF (par_InGnuPlotOutputImage_annualInterest2D == 1, par_InGnuPlotOutputImage_cumulativeAnnualPhase = 0)
* - rule: IF (par_InGnuPlotOutputImage_annualInterest2D == 1, par_InGnuPlotOutputImage_cumulativeAnnualPhase2 = 0)
* - rule: IF (par_InGnuPlotOutputImage_annualInterest2D == 1, par_InGnuPlotOutputImage_annualPhaseOverview = 0)
* - rule: IF (par_InGnuPlotOutputImage_annualInterest2D == 0, par_InGnuPlotOutputImage_annualZip = 0)
* - rule: IF (par_InGnuPlotOutputImage_annualInterest2D == 0, par_InGnuPlotOutputImage_annualZipWithReal = 0)
* - rule: IF (par_InGnuPlotOutputImage_annualInterest2D == 0, par_InGnuPlotOutputImage_annualZipWithRealTotal = 0)
* - rule: IF (par_InGnuPlotOutputImage_annualInterest2D == 0, par_InGnuPlotOutputImage_cumulativeAnnualPhase = 0)
* - rule: IF (par_InGnuPlotOutputImage_annualInterest2D == 0, par_InGnuPlotOutputImage_cumulativeAnnualPhase2 = 0)
* - rule: IF (par_InGnuPlotOutputImage_annualInterest2D == 0, par_InGnuPlotOutputImage_annualInterest2D = 1)
* - rule: IF (par_InGnuPlotOutputImage_annualInterest2D == 0, par_InGnuPlotOutputImage_annualPhaseOverview = 0)
* - type: Boolean
PARAMETER par_InGnuPlotOutputImage_annualInterest2D(set_InGnuPlotOutputImage)

* - default: 0
* - domain: [0|1]
* - description: Stellt die Entwicklung des Phasen dar.
* - identifier: Darstellung: Jährliche Phasenübersicht
* - rule: IF (par_InGnuPlotOutputImage_annualPhaseOverview == 1, par_InGnuPlotOutputImage_annualZip = 0)
* - rule: IF (par_InGnuPlotOutputImage_annualPhaseOverview == 1, par_InGnuPlotOutputImage_annualZipWithReal = 0)
* - rule: IF (par_InGnuPlotOutputImage_annualPhaseOverview == 1, par_InGnuPlotOutputImage_annualZipWithRealTotal = 0)
* - rule: IF (par_InGnuPlotOutputImage_annualPhaseOverview == 1, par_InGnuPlotOutputImage_cumulativeAnnualPhase = 0)
* - rule: IF (par_InGnuPlotOutputImage_annualPhaseOverview == 1, par_InGnuPlotOutputImage_cumulativeAnnualPhase2 = 0)
* - rule: IF (par_InGnuPlotOutputImage_annualPhaseOverview == 1, par_InGnuPlotOutputImage_annualInterest2D = 0)
* - rule: IF (par_InGnuPlotOutputImage_annualPhaseOverview == 0, par_InGnuPlotOutputImage_annualZip = 0)
* - rule: IF (par_InGnuPlotOutputImage_annualPhaseOverview == 0, par_InGnuPlotOutputImage_annualZipWithReal = 0)
* - rule: IF (par_InGnuPlotOutputImage_annualPhaseOverview == 0, par_InGnuPlotOutputImage_annualZipWithRealTotal = 0)
* - rule: IF (par_InGnuPlotOutputImage_annualPhaseOverview == 0, par_InGnuPlotOutputImage_cumulativeAnnualPhase = 0)
* - rule: IF (par_InGnuPlotOutputImage_annualPhaseOverview == 0, par_InGnuPlotOutputImage_cumulativeAnnualPhase2 = 0)
* - rule: IF (par_InGnuPlotOutputImage_annualPhaseOverview == 0, par_InGnuPlotOutputImage_annualInterest2D = 0)
* - rule: IF (par_InGnuPlotOutputImage_annualPhaseOverview == 0, par_InGnuPlotOutputImage_annualPhaseOverview = 1)
* - type: Boolean
PARAMETER par_InGnuPlotOutputImage_annualPhaseOverview(set_InGnuPlotOutputImage)

* - default: 0
* - domain: [0|1]
* - description: Speichert auf Wunsch das Skript für die Bilderzeugung.
* - identifier: Skript speichern?
* - type: Boolean
PARAMETER par_InGnuPlotOutputImage_storeScript(set_InGnuPlotOutputImage)

* - default: 0
* - domain: [0|1]
* - description: Speichert auf Wunsch die Daten für die Bilderzeugung.
* - identifier: Daten speichern?
* - type: Boolean
PARAMETER par_InGnuPlotOutputImage_storeData(set_InGnuPlotOutputImage)

* - default: 1
* - domain: [0|1]
* - description: Erzeugt und speichert auf Wunsch das erzeugte Bild.
* - identifier: Bild speichern?
* - type: Boolean
PARAMETER par_InGnuPlotOutputImage_storeImage(set_InGnuPlotOutputImage)

* - default: 1280
* - domain: (0,)
* - description: Gibt die Bildbreite in Pixeln an.
* - identifier: Bildbreite
* - unit: [Pixel]
* - type: Integer
PARAMETER par_InGnuPlotOutputImage_imageWidth(set_InGnuPlotOutputImage)

* - default: 720
* - domain: (0,)
* - description: Gibt die Bildhöhe in Pixeln an.
* - identifier: Bildhöhe
* - unit: [Pixel]
* - type: Integer
PARAMETER par_InGnuPlotOutputImage_imageHeight(set_InGnuPlotOutputImage)

* - default: 1
* - domain: (0,)
* - description: Gibt die Linienstärke an. Dieser Wert hat nur auf line plots Einfluss.
* - identifier: Linienstärke
* - type: Float
PARAMETER par_InGnuPlotOutputImage_linewidth(set_InGnuPlotOutputImage)

* - description: Legt die realen zu nutzenden Adoptionsdaten fest.
* - identifier: Reale Adoptionsdaten
* - type: Boolean
PARAMETER par_link_InGnuPlotOutputImage_InRealAdoptionDataFile_realAdoptionDataFile(set_InGnuPlotOutputImage,set_InRealAdoptionDataFile)

* - identifier: InOutputImage
* - hidden: 1
* - type: String
SET set_InOutputImage(*)

* - identifier: InROutputImage
* - type: String
SET set_InROutputImage(set_InOutputImage)

* - default: 1
* - domain: [0|1]
* - description: Stellt die jährlichen Adoptionen, aufgeschlüsselt nach PLZ, in einer line chart dar.
* - identifier: Darstellung: jährliche Adoptionen (PLZ)
* - rule: IF (par_InROutputImage_annualZip == 1, par_InROutputImage_annualZipWithReal = 0)
* - rule: IF (par_InROutputImage_annualZip == 1, par_InROutputImage_annualZipWithRealTotal = 0)
* - rule: IF (par_InROutputImage_annualZip == 1, par_InROutputImage_cumulativeAnnualPhase = 0)
* - rule: IF (par_InROutputImage_annualZip == 1, par_InROutputImage_cumulativeAnnualPhase2 = 0)
* - rule: IF (par_InROutputImage_annualZip == 1, par_InROutputImage_annualInterest2D = 0)
* - rule: IF (par_InROutputImage_annualZip == 1, par_InROutputImage_annualPhaseOverview = 0)
* - rule: IF (par_InROutputImage_annualZip == 0, par_InROutputImage_annualZip = 1)
* - rule: IF (par_InROutputImage_annualZip == 0, par_InROutputImage_annualZipWithReal = 0)
* - rule: IF (par_InROutputImage_annualZip == 0, par_InROutputImage_annualZipWithRealTotal = 0)
* - rule: IF (par_InROutputImage_annualZip == 0, par_InROutputImage_cumulativeAnnualPhase = 0)
* - rule: IF (par_InROutputImage_annualZip == 0, par_InROutputImage_cumulativeAnnualPhase2 = 0)
* - rule: IF (par_InROutputImage_annualZip == 0, par_InROutputImage_annualInterest2D = 0)
* - rule: IF (par_InROutputImage_annualZip == 0, par_InROutputImage_annualPhaseOverview = 0)
* - type: Boolean
PARAMETER par_InROutputImage_annualZip(set_InROutputImage)

* - default: 0
* - domain: [0|1]
* - description: Stellt die jährlichen Adoptionen, aufgeschlüsselt nach PLZ, in einer line chart dar. Zusätzlich werden die realen Adoptionen mit dargestellt.
* - identifier: Darstellung: jährliche Adoptionen im Vergleich zu realen Daten (PLZ)
* - rule: IF (par_InROutputImage_annualZipWithReal == 1, par_InROutputImage_annualZip = 0)
* - rule: IF (par_InROutputImage_annualZipWithReal == 1, par_InROutputImage_annualZipWithRealTotal = 0)
* - rule: IF (par_InROutputImage_annualZipWithReal == 1, par_InROutputImage_cumulativeAnnualPhase = 0)
* - rule: IF (par_InROutputImage_annualZipWithReal == 1, par_InROutputImage_cumulativeAnnualPhase2 = 0)
* - rule: IF (par_InROutputImage_annualZipWithReal == 1, par_InROutputImage_annualInterest2D = 0)
* - rule: IF (par_InROutputImage_annualZipWithReal == 1, par_InROutputImage_annualPhaseOverview = 0)
* - rule: IF (par_InROutputImage_annualZipWithReal == 0, par_InROutputImage_annualZip = 0)
* - rule: IF (par_InROutputImage_annualZipWithReal == 0, par_InROutputImage_annualZipWithReal = 1)
* - rule: IF (par_InROutputImage_annualZipWithReal == 0, par_InROutputImage_annualZipWithRealTotal = 0)
* - rule: IF (par_InROutputImage_annualZipWithReal == 0, par_InROutputImage_cumulativeAnnualPhase = 0)
* - rule: IF (par_InROutputImage_annualZipWithReal == 0, par_InROutputImage_cumulativeAnnualPhase2 = 0)
* - rule: IF (par_InROutputImage_annualZipWithReal == 0, par_InROutputImage_annualInterest2D = 0)
* - rule: IF (par_InROutputImage_annualZipWithReal == 0, par_InROutputImage_annualPhaseOverview = 0)
* - type: Boolean
PARAMETER par_InROutputImage_annualZipWithReal(set_InROutputImage)

* - default: 0
* - domain: [0|1]
* - description: Stellt die jährlichen Adoptionen in einer line chart dar. Zusätzlich werden die realen Adoptionen mit dargestellt.
* - identifier: Darstellung: jährliche Adoptionen im Vergleich zu realen Daten
* - rule: IF (par_InROutputImage_annualZipWithRealTotal == 1, par_InROutputImage_annualZip = 0)
* - rule: IF (par_InROutputImage_annualZipWithRealTotal == 1, par_InROutputImage_annualZipWithReal = 0)
* - rule: IF (par_InROutputImage_annualZipWithRealTotal == 1, par_InROutputImage_cumulativeAnnualPhase = 0)
* - rule: IF (par_InROutputImage_annualZipWithRealTotal == 1, par_InROutputImage_cumulativeAnnualPhase2 = 0)
* - rule: IF (par_InROutputImage_annualZipWithRealTotal == 1, par_InROutputImage_annualInterest2D = 0)
* - rule: IF (par_InROutputImage_annualZipWithRealTotal == 1, par_InROutputImage_annualPhaseOverview = 0)
* - rule: IF (par_InROutputImage_annualZipWithRealTotal == 0, par_InROutputImage_annualZip = 0)
* - rule: IF (par_InROutputImage_annualZipWithRealTotal == 0, par_InROutputImage_annualZipWithReal = 0)
* - rule: IF (par_InROutputImage_annualZipWithRealTotal == 0, par_InROutputImage_annualZipWithRealTotal = 1)
* - rule: IF (par_InROutputImage_annualZipWithRealTotal == 0, par_InROutputImage_cumulativeAnnualPhase = 0)
* - rule: IF (par_InROutputImage_annualZipWithRealTotal == 0, par_InROutputImage_cumulativeAnnualPhase2 = 0)
* - rule: IF (par_InROutputImage_annualZipWithRealTotal == 0, par_InROutputImage_annualInterest2D = 0)
* - rule: IF (par_InROutputImage_annualZipWithRealTotal == 0, par_InROutputImage_annualPhaseOverview = 0)
* - type: Boolean
PARAMETER par_InROutputImage_annualZipWithRealTotal(set_InROutputImage)

* - default: 0
* - domain: [0|1]
* - description: Stellt die kumulierten jährlichen Adoptionen, aufgeschlüsselt nach der Adoptionsphase, in einer stacked bar chart dar. Die initialen Adopter werden nicht berücksichtigt.
* - identifier: Darstellung: kumulierte jährliche Adoptionen (Phase) (ohne Initiale)
* - rule: IF (par_InROutputImage_cumulativeAnnualPhase == 1, par_InROutputImage_annualZip = 0)
* - rule: IF (par_InROutputImage_cumulativeAnnualPhase == 1, par_InROutputImage_annualZipWithReal = 0)
* - rule: IF (par_InROutputImage_cumulativeAnnualPhase == 1, par_InROutputImage_annualZipWithRealTotal = 0)
* - rule: IF (par_InROutputImage_cumulativeAnnualPhase == 1, par_InROutputImage_cumulativeAnnualPhase2 = 0)
* - rule: IF (par_InROutputImage_cumulativeAnnualPhase == 1, par_InROutputImage_annualInterest2D = 0)
* - rule: IF (par_InROutputImage_cumulativeAnnualPhase == 1, par_InROutputImage_annualPhaseOverview = 0)
* - rule: IF (par_InROutputImage_cumulativeAnnualPhase == 0, par_InROutputImage_annualZip = 0)
* - rule: IF (par_InROutputImage_cumulativeAnnualPhase == 0, par_InROutputImage_annualZipWithReal = 0)
* - rule: IF (par_InROutputImage_cumulativeAnnualPhase == 0, par_InROutputImage_annualZipWithRealTotal = 0)
* - rule: IF (par_InROutputImage_cumulativeAnnualPhase == 0, par_InROutputImage_cumulativeAnnualPhase = 1)
* - rule: IF (par_InROutputImage_cumulativeAnnualPhase == 0, par_InROutputImage_cumulativeAnnualPhase2 = 0)
* - rule: IF (par_InROutputImage_cumulativeAnnualPhase == 0, par_InROutputImage_annualInterest2D = 0)
* - rule: IF (par_InROutputImage_cumulativeAnnualPhase == 0, par_InROutputImage_annualPhaseOverview = 0)
* - type: Boolean
PARAMETER par_InROutputImage_cumulativeAnnualPhase(set_InROutputImage)

* - default: 0
* - domain: [0|1]
* - description: Stellt die kumulierten jährlichen Adoptionen, aufgeschlüsselt nach der Adoptionsphase, in einer stacked bar chart dar. Die initialen Adopter werden bei den Daten berücksichtigt, aber nicht direkt visualisiert.
* - identifier: Darstellung: kumulierte jährliche Adoptionen (Phase) (mit Initialen)
* - rule: IF (par_InROutputImage_cumulativeAnnualPhase2 == 1, par_InROutputImage_annualZip = 0)
* - rule: IF (par_InROutputImage_cumulativeAnnualPhase2 == 1, par_InROutputImage_annualZipWithReal = 0)
* - rule: IF (par_InROutputImage_cumulativeAnnualPhase2 == 1, par_InROutputImage_annualZipWithRealTotal = 0)
* - rule: IF (par_InROutputImage_cumulativeAnnualPhase2 == 1, par_InROutputImage_cumulativeAnnualPhase = 0)
* - rule: IF (par_InROutputImage_cumulativeAnnualPhase2 == 1, par_InROutputImage_annualInterest2D = 0)
* - rule: IF (par_InROutputImage_cumulativeAnnualPhase2 == 1, par_InROutputImage_annualPhaseOverview = 0)
* - rule: IF (par_InROutputImage_cumulativeAnnualPhase2 == 0, par_InROutputImage_annualZip = 0)
* - rule: IF (par_InROutputImage_cumulativeAnnualPhase2 == 0, par_InROutputImage_annualZipWithReal = 0)
* - rule: IF (par_InROutputImage_cumulativeAnnualPhase2 == 0, par_InROutputImage_annualZipWithRealTotal = 0)
* - rule: IF (par_InROutputImage_cumulativeAnnualPhase2 == 0, par_InROutputImage_cumulativeAnnualPhase = 0)
* - rule: IF (par_InROutputImage_cumulativeAnnualPhase2 == 0, par_InROutputImage_cumulativeAnnualPhase2 = 1)
* - rule: IF (par_InROutputImage_cumulativeAnnualPhase2 == 0, par_InROutputImage_annualInterest2D = 0)
* - rule: IF (par_InROutputImage_cumulativeAnnualPhase2 == 0, par_InROutputImage_annualPhaseOverview = 0)
* - type: Boolean
PARAMETER par_InROutputImage_cumulativeAnnualPhase2(set_InROutputImage)

* - default: 0
* - domain: [0|1]
* - description: Stellt die Entwicklung des jährlichen Interesses dar.
* - identifier: Darstellung: Jährliches Interesse (2D)
* - rule: IF (par_InROutputImage_annualInterest2D == 1, par_InROutputImage_annualZip = 0)
* - rule: IF (par_InROutputImage_annualInterest2D == 1, par_InROutputImage_annualZipWithReal = 0)
* - rule: IF (par_InROutputImage_annualInterest2D == 1, par_InROutputImage_annualZipWithRealTotal = 0)
* - rule: IF (par_InROutputImage_annualInterest2D == 1, par_InROutputImage_cumulativeAnnualPhase = 0)
* - rule: IF (par_InROutputImage_annualInterest2D == 1, par_InROutputImage_cumulativeAnnualPhase2 = 0)
* - rule: IF (par_InROutputImage_annualInterest2D == 1, par_InROutputImage_annualPhaseOverview = 0)
* - rule: IF (par_InROutputImage_annualInterest2D == 0, par_InROutputImage_annualZip = 0)
* - rule: IF (par_InROutputImage_annualInterest2D == 0, par_InROutputImage_annualZipWithReal = 0)
* - rule: IF (par_InROutputImage_annualInterest2D == 0, par_InROutputImage_annualZipWithRealTotal = 0)
* - rule: IF (par_InROutputImage_annualInterest2D == 0, par_InROutputImage_cumulativeAnnualPhase = 0)
* - rule: IF (par_InROutputImage_annualInterest2D == 0, par_InROutputImage_cumulativeAnnualPhase2 = 0)
* - rule: IF (par_InROutputImage_annualInterest2D == 0, par_InROutputImage_annualInterest2D = 1)
* - rule: IF (par_InROutputImage_annualInterest2D == 0, par_InROutputImage_annualPhaseOverview = 0)
* - type: Boolean
PARAMETER par_InROutputImage_annualInterest2D(set_InROutputImage)

* - default: 0
* - domain: [0|1]
* - description: Stellt die Entwicklung des Phasen dar.
* - identifier: Darstellung: Jährliche Phasenübersicht
* - rule: IF (par_InROutputImage_annualPhaseOverview == 1, par_InROutputImage_annualZip = 0)
* - rule: IF (par_InROutputImage_annualPhaseOverview == 1, par_InROutputImage_annualZipWithReal = 0)
* - rule: IF (par_InROutputImage_annualPhaseOverview == 1, par_InROutputImage_annualZipWithRealTotal = 0)
* - rule: IF (par_InROutputImage_annualPhaseOverview == 1, par_InROutputImage_cumulativeAnnualPhase = 0)
* - rule: IF (par_InROutputImage_annualPhaseOverview == 1, par_InROutputImage_cumulativeAnnualPhase2 = 0)
* - rule: IF (par_InROutputImage_annualPhaseOverview == 1, par_InROutputImage_annualInterest2D = 0)
* - rule: IF (par_InROutputImage_annualPhaseOverview == 0, par_InROutputImage_annualZip = 0)
* - rule: IF (par_InROutputImage_annualPhaseOverview == 0, par_InROutputImage_annualZipWithReal = 0)
* - rule: IF (par_InROutputImage_annualPhaseOverview == 0, par_InROutputImage_annualZipWithRealTotal = 0)
* - rule: IF (par_InROutputImage_annualPhaseOverview == 0, par_InROutputImage_cumulativeAnnualPhase = 0)
* - rule: IF (par_InROutputImage_annualPhaseOverview == 0, par_InROutputImage_cumulativeAnnualPhase2 = 0)
* - rule: IF (par_InROutputImage_annualPhaseOverview == 0, par_InROutputImage_annualInterest2D = 0)
* - rule: IF (par_InROutputImage_annualPhaseOverview == 0, par_InROutputImage_annualPhaseOverview = 1)
* - type: Boolean
PARAMETER par_InROutputImage_annualPhaseOverview(set_InROutputImage)

* - default: 0
* - domain: [0|1]
* - description: Speichert auf Wunsch das Skript für die Bilderzeugung.
* - identifier: Skript speichern?
* - type: Boolean
PARAMETER par_InROutputImage_storeScript(set_InROutputImage)

* - default: 0
* - domain: [0|1]
* - description: Speichert auf Wunsch die Daten für die Bilderzeugung.
* - identifier: Daten speichern?
* - type: Boolean
PARAMETER par_InROutputImage_storeData(set_InROutputImage)

* - default: 1
* - domain: [0|1]
* - description: Erzeugt und speichert auf Wunsch das erzeugte Bild.
* - identifier: Bild speichern?
* - type: Boolean
PARAMETER par_InROutputImage_storeImage(set_InROutputImage)

* - default: 1280
* - domain: (0,)
* - description: Gibt die Bildbreite in Pixeln an.
* - identifier: Bildbreite
* - unit: [Pixel]
* - type: Integer
PARAMETER par_InROutputImage_imageWidth(set_InROutputImage)

* - default: 720
* - domain: (0,)
* - description: Gibt die Bildhöhe in Pixeln an.
* - identifier: Bildhöhe
* - unit: [Pixel]
* - type: Integer
PARAMETER par_InROutputImage_imageHeight(set_InROutputImage)

* - default: 1
* - domain: (0,)
* - description: Gibt die Linienstärke an. Dieser Wert hat nur auf line plots Einfluss.
* - identifier: Linienstärke
* - type: Float
PARAMETER par_InROutputImage_linewidth(set_InROutputImage)

* - description: Legt die realen zu nutzenden Adoptionsdaten fest.
* - identifier: Reale Adoptionsdaten
* - type: Boolean
PARAMETER par_link_InROutputImage_InRealAdoptionDataFile_realAdoptionDataFile(set_InROutputImage,set_InRealAdoptionDataFile)

* - identifier: InBucketAnalyser
* - type: String
SET set_InBucketAnalyser(set_InPostDataAnalysis)

* - default: 1
* - domain: [0|1]
* - description: todo
* - identifier: enabled
* - type: Boolean
PARAMETER par_InBucketAnalyser_enabled(set_InBucketAnalyser)

* - default: 0
* - domain: [0|1]
* - description: todo
* - identifier: storeCsv
* - type: Boolean
PARAMETER par_InBucketAnalyser_storeCsv(set_InBucketAnalyser)

* - default: 1
* - domain: [0|1]
* - description: todo
* - identifier: storeXlsx
* - type: Boolean
PARAMETER par_InBucketAnalyser_storeXlsx(set_InBucketAnalyser)

* - default: 0.1
* - domain: (0,)
* - description: todo
* - identifier: bucketRange
* - type: Float
PARAMETER par_InBucketAnalyser_bucketRange(set_InBucketAnalyser)

* - description: todo
* - identifier: loggingModule
* - type: Boolean
PARAMETER par_link_InBucketAnalyser_InConsumerAgentCalculationLoggingModule2_loggingModule(set_InBucketAnalyser,set_InConsumerAgentCalculationLoggingModule2)

* - identifier: InNeighbourhoodOverview
* - type: String
SET set_InNeighbourhoodOverview(set_InPostDataAnalysis)

* - default: 0
* - domain: [0|1]
* - description: Mittels dieser Option kann die Analyse aktiviert bzw. deaktiviert.
* - identifier: Verwenden?
* - type: Boolean
PARAMETER par_InNeighbourhoodOverview_enabled(set_InNeighbourhoodOverview)

* - default: 0
* - domain: [0|1]
* - description: todo
* - identifier: Als csv speichern?
* - type: Boolean
PARAMETER par_InNeighbourhoodOverview_storeCsv(set_InNeighbourhoodOverview)

* - default: 0
* - domain: [0|1]
* - description: todo
* - identifier: Als xlsx speichern?
* - type: Boolean
PARAMETER par_InNeighbourhoodOverview_storeXlsx(set_InNeighbourhoodOverview)

* - description: todo
* - identifier: Netzwerkfilter für räumliche Sicht
* - type: Boolean
PARAMETER par_link_InNeighbourhoodOverview_InNodeDistanceFilterScheme_nodeFilterScheme(set_InNeighbourhoodOverview,set_InNodeDistanceFilterScheme)

* - identifier: InPostDataAnalysis
* - hidden: 1
* - type: String
SET set_InPostDataAnalysis(*)

* - identifier: InAdoptionPhaseOverviewImage
* - type: String
SET set_InAdoptionPhaseOverviewImage(set_InLoggingResultImage2)

* - default: 1
* - domain: [0|1]
* - description: todo
* - identifier: enabled
* - type: Boolean
PARAMETER par_InAdoptionPhaseOverviewImage_enabled(set_InAdoptionPhaseOverviewImage)

* - default: 1
* - domain: [0|1]
* - description: todo
* - identifier: useGnuplot
* - rule: IF (par_InAdoptionPhaseOverviewImage_useGnuplot == 1, par_InAdoptionPhaseOverviewImage_useR = 0)
* - rule: IF (par_InAdoptionPhaseOverviewImage_useGnuplot == 0, par_InAdoptionPhaseOverviewImage_useGnuplot = 1)
* - rule: IF (par_InAdoptionPhaseOverviewImage_useGnuplot == 0, par_InAdoptionPhaseOverviewImage_useR = 0)
* - type: Boolean
PARAMETER par_InAdoptionPhaseOverviewImage_useGnuplot(set_InAdoptionPhaseOverviewImage)

* - default: 0
* - domain: [0|1]
* - description: todo
* - identifier: useR
* - rule: IF (par_InAdoptionPhaseOverviewImage_useR == 1, par_InAdoptionPhaseOverviewImage_useGnuplot = 0)
* - rule: IF (par_InAdoptionPhaseOverviewImage_useR == 0, par_InAdoptionPhaseOverviewImage_useGnuplot = 0)
* - rule: IF (par_InAdoptionPhaseOverviewImage_useR == 0, par_InAdoptionPhaseOverviewImage_useR = 1)
* - type: Boolean
PARAMETER par_InAdoptionPhaseOverviewImage_useR(set_InAdoptionPhaseOverviewImage)

* - default: 0
* - domain: [0|1]
* - description: todo
* - identifier: storeScript
* - type: Boolean
PARAMETER par_InAdoptionPhaseOverviewImage_storeScript(set_InAdoptionPhaseOverviewImage)

* - default: 0
* - domain: [0|1]
* - description: todo
* - identifier: storeData
* - type: Boolean
PARAMETER par_InAdoptionPhaseOverviewImage_storeData(set_InAdoptionPhaseOverviewImage)

* - default: 1
* - domain: [0|1]
* - description: todo
* - identifier: storeImage
* - type: Boolean
PARAMETER par_InAdoptionPhaseOverviewImage_storeImage(set_InAdoptionPhaseOverviewImage)

* - default: 1280
* - domain: (0,)
* - description: todo
* - identifier: imageWidth
* - unit: [Pixel]
* - type: Integer
PARAMETER par_InAdoptionPhaseOverviewImage_imageWidth(set_InAdoptionPhaseOverviewImage)

* - default: 720
* - domain: (0,)
* - description: todo
* - identifier: imageHeight
* - unit: [Pixel]
* - type: Integer
PARAMETER par_InAdoptionPhaseOverviewImage_imageHeight(set_InAdoptionPhaseOverviewImage)

* - default: 0.8
* - domain: (0,)
* - description: todo
* - identifier: boxWidth
* - type: Float
PARAMETER par_InAdoptionPhaseOverviewImage_boxWidth(set_InAdoptionPhaseOverviewImage)

* - default: 0
* - description: todo
* - identifier: customImageId
* - type: Integer
PARAMETER par_InAdoptionPhaseOverviewImage_customImageId(set_InAdoptionPhaseOverviewImage)

* - description: Bestimmt die verwendete Farbpalette für die Abbildung. Falls keine ausgewählt wurde, wird das Standardfarbschema verwendet.
* - identifier: Zu nutzende Farben
* - type: Boolean
PARAMETER par_link_InAdoptionPhaseOverviewImage_InColorPalette_colorPalette(set_InAdoptionPhaseOverviewImage,set_InColorPalette)

* - identifier: InAnnualBucketImage
* - type: String
SET set_InAnnualBucketImage(set_InLoggingResultImage2)

* - default: 1
* - domain: [0|1]
* - description: todo
* - identifier: enabled
* - type: Boolean
PARAMETER par_InAnnualBucketImage_enabled(set_InAnnualBucketImage)

* - default: 1
* - domain: [0|1]
* - description: todo
* - identifier: useGnuplot
* - rule: IF (par_InAnnualBucketImage_useGnuplot == 1, par_InAnnualBucketImage_useR = 0)
* - rule: IF (par_InAnnualBucketImage_useGnuplot == 0, par_InAnnualBucketImage_useGnuplot = 1)
* - rule: IF (par_InAnnualBucketImage_useGnuplot == 0, par_InAnnualBucketImage_useR = 0)
* - type: Boolean
PARAMETER par_InAnnualBucketImage_useGnuplot(set_InAnnualBucketImage)

* - default: 0
* - domain: [0|1]
* - description: todo
* - identifier: useR
* - rule: IF (par_InAnnualBucketImage_useR == 1, par_InAnnualBucketImage_useGnuplot = 0)
* - rule: IF (par_InAnnualBucketImage_useR == 0, par_InAnnualBucketImage_useGnuplot = 0)
* - rule: IF (par_InAnnualBucketImage_useR == 0, par_InAnnualBucketImage_useR = 1)
* - type: Boolean
PARAMETER par_InAnnualBucketImage_useR(set_InAnnualBucketImage)

* - default: 0
* - domain: [0|1]
* - description: todo
* - identifier: storeScript
* - type: Boolean
PARAMETER par_InAnnualBucketImage_storeScript(set_InAnnualBucketImage)

* - default: 0
* - domain: [0|1]
* - description: todo
* - identifier: storeData
* - type: Boolean
PARAMETER par_InAnnualBucketImage_storeData(set_InAnnualBucketImage)

* - default: 1
* - domain: [0|1]
* - description: todo
* - identifier: storeImage
* - type: Boolean
PARAMETER par_InAnnualBucketImage_storeImage(set_InAnnualBucketImage)

* - default: 1280
* - domain: (0,)
* - description: todo
* - identifier: imageWidth
* - unit: [Pixel]
* - type: Integer
PARAMETER par_InAnnualBucketImage_imageWidth(set_InAnnualBucketImage)

* - default: 1280
* - domain: (0,)
* - description: todo
* - identifier: imageHeight
* - unit: [Pixel]
* - type: Integer
PARAMETER par_InAnnualBucketImage_imageHeight(set_InAnnualBucketImage)

* - default: 1.0
* - domain: (0,)
* - description: todo
* - identifier: boxWidth
* - type: Float
PARAMETER par_InAnnualBucketImage_boxWidth(set_InAnnualBucketImage)

* - default: 0
* - domain: [0|1]
* - description: todo
* - identifier: Benutzerdefinierte Y-Achse nutzen?
* - type: Boolean
PARAMETER par_InAnnualBucketImage_useCustomYRange(set_InAnnualBucketImage)

* - default: 0.0
* - description: todo
* - identifier: Min Y (Achse)
* - type: Float
PARAMETER par_InAnnualBucketImage_minY(set_InAnnualBucketImage)

* - default: 0.0
* - description: todo
* - identifier: Max Y (Achse)
* - type: Float
PARAMETER par_InAnnualBucketImage_maxY(set_InAnnualBucketImage)

* - default: 0.1
* - domain: (0,)
* - description: todo
* - identifier: Bereichsgröße
* - type: Float
PARAMETER par_InAnnualBucketImage_bucketSize(set_InAnnualBucketImage)

* - default: 1
* - domain: [0,)
* - description: todo
* - identifier: Anzahl Nachkommastellen (Legende)
* - type: Integer
PARAMETER par_InAnnualBucketImage_fractionDigits(set_InAnnualBucketImage)

* - default: 0
* - description: todo
* - identifier: Benutzerdefinierte Bild-Id
* - type: Integer
PARAMETER par_InAnnualBucketImage_customImageId(set_InAnnualBucketImage)

* - description: Bestimmt die verwendete Farbpalette für die Abbildung. Falls keine ausgewählt wurde, wird das Standardfarbschema verwendet.
* - identifier: Zu nutzende Farben
* - type: Boolean
PARAMETER par_link_InAnnualBucketImage_InColorPalette_colorPalette(set_InAnnualBucketImage,set_InColorPalette)

* - description: todo
* - identifier: Loggingmodule
* - type: Boolean
PARAMETER par_link_InAnnualBucketImage_InConsumerAgentCalculationLoggingModule2_loggingModule(set_InAnnualBucketImage,set_InConsumerAgentCalculationLoggingModule2)

* - identifier: InAnnualInterestImage
* - type: String
SET set_InAnnualInterestImage(set_InLoggingResultImage2)

* - default: 1
* - domain: [0|1]
* - description: todo
* - identifier: enabled
* - type: Boolean
PARAMETER par_InAnnualInterestImage_enabled(set_InAnnualInterestImage)

* - default: 1
* - domain: [0|1]
* - description: todo
* - identifier: useGnuplot
* - rule: IF (par_InAnnualInterestImage_useGnuplot == 1, par_InAnnualInterestImage_useR = 0)
* - rule: IF (par_InAnnualInterestImage_useGnuplot == 0, par_InAnnualInterestImage_useGnuplot = 1)
* - rule: IF (par_InAnnualInterestImage_useGnuplot == 0, par_InAnnualInterestImage_useR = 0)
* - type: Boolean
PARAMETER par_InAnnualInterestImage_useGnuplot(set_InAnnualInterestImage)

* - default: 0
* - domain: [0|1]
* - description: todo
* - identifier: useR
* - rule: IF (par_InAnnualInterestImage_useR == 1, par_InAnnualInterestImage_useGnuplot = 0)
* - rule: IF (par_InAnnualInterestImage_useR == 0, par_InAnnualInterestImage_useGnuplot = 0)
* - rule: IF (par_InAnnualInterestImage_useR == 0, par_InAnnualInterestImage_useR = 1)
* - type: Boolean
PARAMETER par_InAnnualInterestImage_useR(set_InAnnualInterestImage)

* - default: 0
* - domain: [0|1]
* - description: todo
* - identifier: storeScript
* - type: Boolean
PARAMETER par_InAnnualInterestImage_storeScript(set_InAnnualInterestImage)

* - default: 0
* - domain: [0|1]
* - description: todo
* - identifier: storeData
* - type: Boolean
PARAMETER par_InAnnualInterestImage_storeData(set_InAnnualInterestImage)

* - default: 1
* - domain: [0|1]
* - description: todo
* - identifier: storeImage
* - type: Boolean
PARAMETER par_InAnnualInterestImage_storeImage(set_InAnnualInterestImage)

* - default: 1280
* - domain: (0,)
* - description: todo
* - identifier: imageWidth
* - unit: [Pixel]
* - type: Integer
PARAMETER par_InAnnualInterestImage_imageWidth(set_InAnnualInterestImage)

* - default: 720
* - domain: (0,)
* - description: todo
* - identifier: imageHeight
* - unit: [Pixel]
* - type: Integer
PARAMETER par_InAnnualInterestImage_imageHeight(set_InAnnualInterestImage)

* - default: 1
* - domain: (0,)
* - description: todo
* - identifier: linewidth
* - type: Integer
PARAMETER par_InAnnualInterestImage_linewidth(set_InAnnualInterestImage)

* - default: 0
* - domain: [0|1]
* - description: todo
* - identifier: Benutzerdefinierten Minimalwert (Min Y) für die Y-Achse nutzen?
* - type: Boolean
PARAMETER par_InAnnualInterestImage_useCustomYRangeMin(set_InAnnualInterestImage)

* - default: 0.0
* - description: todo
* - identifier: Min Y (Achse)
* - type: Float
PARAMETER par_InAnnualInterestImage_minY(set_InAnnualInterestImage)

* - default: 0
* - domain: [0|1]
* - description: todo
* - identifier: Benutzerdefinierten Maximalwert (Max Y) für die Y-Achse nutzen?
* - type: Boolean
PARAMETER par_InAnnualInterestImage_useCustomYRangeMax(set_InAnnualInterestImage)

* - default: 0.0
* - description: todo
* - identifier: Max Y (Achse)
* - type: Float
PARAMETER par_InAnnualInterestImage_maxY(set_InAnnualInterestImage)

* - default: 0
* - domain: [0,10]
* - description: todo
* - identifier: Benutzerdefinierte Bild-Id
* - type: Integer
PARAMETER par_InAnnualInterestImage_customImageId(set_InAnnualInterestImage)

* - description: Bestimmt die verwendete Farbpalette für die Abbildung. Falls keine ausgewählt wurde, wird das Standardfarbschema verwendet.
* - identifier: Zu nutzende Farben
* - type: Boolean
PARAMETER par_link_InAnnualInterestImage_InColorPalette_colorPalette(set_InAnnualInterestImage,set_InColorPalette)

* - identifier: InAnnualMilieuImage
* - type: String
SET set_InAnnualMilieuImage(set_InLoggingResultImage2)

* - default: 1
* - domain: [0|1]
* - description: todo
* - identifier: enabled
* - type: Boolean
PARAMETER par_InAnnualMilieuImage_enabled(set_InAnnualMilieuImage)

* - default: 1
* - domain: [0|1]
* - description: todo
* - identifier: useGnuplot
* - rule: IF (par_InAnnualMilieuImage_useGnuplot == 1, par_InAnnualMilieuImage_useR = 0)
* - rule: IF (par_InAnnualMilieuImage_useGnuplot == 0, par_InAnnualMilieuImage_useGnuplot = 1)
* - rule: IF (par_InAnnualMilieuImage_useGnuplot == 0, par_InAnnualMilieuImage_useR = 0)
* - type: Boolean
PARAMETER par_InAnnualMilieuImage_useGnuplot(set_InAnnualMilieuImage)

* - default: 0
* - domain: [0|1]
* - description: todo
* - identifier: useR
* - rule: IF (par_InAnnualMilieuImage_useR == 1, par_InAnnualMilieuImage_useGnuplot = 0)
* - rule: IF (par_InAnnualMilieuImage_useR == 0, par_InAnnualMilieuImage_useGnuplot = 0)
* - rule: IF (par_InAnnualMilieuImage_useR == 0, par_InAnnualMilieuImage_useR = 1)
* - type: Boolean
PARAMETER par_InAnnualMilieuImage_useR(set_InAnnualMilieuImage)

* - default: 0
* - domain: [0|1]
* - description: todo
* - identifier: storeScript
* - type: Boolean
PARAMETER par_InAnnualMilieuImage_storeScript(set_InAnnualMilieuImage)

* - default: 0
* - domain: [0|1]
* - description: todo
* - identifier: storeData
* - type: Boolean
PARAMETER par_InAnnualMilieuImage_storeData(set_InAnnualMilieuImage)

* - default: 1
* - domain: [0|1]
* - description: todo
* - identifier: storeImage
* - type: Boolean
PARAMETER par_InAnnualMilieuImage_storeImage(set_InAnnualMilieuImage)

* - default: 1280
* - domain: (0,)
* - description: todo
* - identifier: imageWidth
* - unit: [Pixel]
* - type: Integer
PARAMETER par_InAnnualMilieuImage_imageWidth(set_InAnnualMilieuImage)

* - default: 1280
* - domain: (0,)
* - description: todo
* - identifier: imageHeight
* - unit: [Pixel]
* - type: Integer
PARAMETER par_InAnnualMilieuImage_imageHeight(set_InAnnualMilieuImage)

* - default: 1.0
* - domain: (0,)
* - description: todo
* - identifier: boxWidth
* - type: Float
PARAMETER par_InAnnualMilieuImage_boxWidth(set_InAnnualMilieuImage)

* - default: 1
* - domain: [0|1]
* - description: todo
* - identifier: Initiale Adoptionen anzeigen?
* - type: Boolean
PARAMETER par_InAnnualMilieuImage_showInitial(set_InAnnualMilieuImage)

* - default: 0
* - domain: [0|1]
* - description: todo
* - identifier: Benutzerdefinierte Y-Achse nutzen?
* - type: Boolean
PARAMETER par_InAnnualMilieuImage_useCustomYRange(set_InAnnualMilieuImage)

* - default: 0.0
* - description: todo
* - identifier: Min Y (Achse)
* - type: Float
PARAMETER par_InAnnualMilieuImage_minY(set_InAnnualMilieuImage)

* - default: 0.0
* - description: todo
* - identifier: Max Y (Achse)
* - type: Float
PARAMETER par_InAnnualMilieuImage_maxY(set_InAnnualMilieuImage)

* - default: 0
* - description: todo
* - identifier: Benutzerdefinierte Bild-Id
* - type: Integer
PARAMETER par_InAnnualMilieuImage_customImageId(set_InAnnualMilieuImage)

* - description: Bestimmt die verwendete Farbpalette für die Abbildung. Falls keine ausgewählt wurde, wird das Standardfarbschema verwendet.
* - identifier: Zu nutzende Farben
* - type: Boolean
PARAMETER par_link_InAnnualMilieuImage_InColorPalette_colorPalette(set_InAnnualMilieuImage,set_InColorPalette)

* - identifier: InComparedAnnualImage
* - type: String
SET set_InComparedAnnualImage(set_InLoggingResultImage2)

* - default: 1
* - domain: [0|1]
* - description: todo
* - identifier: enabled
* - type: Boolean
PARAMETER par_InComparedAnnualImage_enabled(set_InComparedAnnualImage)

* - default: 1
* - domain: [0|1]
* - description: todo
* - identifier: useGnuplot
* - rule: IF (par_InComparedAnnualImage_useGnuplot == 1, par_InComparedAnnualImage_useR = 0)
* - rule: IF (par_InComparedAnnualImage_useGnuplot == 0, par_InComparedAnnualImage_useGnuplot = 1)
* - rule: IF (par_InComparedAnnualImage_useGnuplot == 0, par_InComparedAnnualImage_useR = 0)
* - type: Boolean
PARAMETER par_InComparedAnnualImage_useGnuplot(set_InComparedAnnualImage)

* - default: 0
* - domain: [0|1]
* - description: todo
* - identifier: useR
* - rule: IF (par_InComparedAnnualImage_useR == 1, par_InComparedAnnualImage_useGnuplot = 0)
* - rule: IF (par_InComparedAnnualImage_useR == 0, par_InComparedAnnualImage_useGnuplot = 0)
* - rule: IF (par_InComparedAnnualImage_useR == 0, par_InComparedAnnualImage_useR = 1)
* - type: Boolean
PARAMETER par_InComparedAnnualImage_useR(set_InComparedAnnualImage)

* - default: 0
* - domain: [0|1]
* - description: todo
* - identifier: storeScript
* - type: Boolean
PARAMETER par_InComparedAnnualImage_storeScript(set_InComparedAnnualImage)

* - default: 0
* - domain: [0|1]
* - description: todo
* - identifier: storeData
* - type: Boolean
PARAMETER par_InComparedAnnualImage_storeData(set_InComparedAnnualImage)

* - default: 1
* - domain: [0|1]
* - description: todo
* - identifier: storeImage
* - type: Boolean
PARAMETER par_InComparedAnnualImage_storeImage(set_InComparedAnnualImage)

* - default: 1280
* - domain: (0,)
* - description: todo
* - identifier: imageWidth
* - unit: [Pixel]
* - type: Integer
PARAMETER par_InComparedAnnualImage_imageWidth(set_InComparedAnnualImage)

* - default: 720
* - domain: (0,)
* - description: todo
* - identifier: imageHeight
* - unit: [Pixel]
* - type: Integer
PARAMETER par_InComparedAnnualImage_imageHeight(set_InComparedAnnualImage)

* - default: 1
* - domain: (0,)
* - description: todo
* - identifier: linewidth
* - type: Integer
PARAMETER par_InComparedAnnualImage_linewidth(set_InComparedAnnualImage)

* - default: 1
* - domain: [0|1]
* - description: todo
* - identifier: skipInvalidZips
* - type: Boolean
PARAMETER par_InComparedAnnualImage_skipInvalidZips(set_InComparedAnnualImage)

* - default: 1
* - domain: [0|1]
* - description: todo
* - identifier: showPreYear
* - type: Boolean
PARAMETER par_InComparedAnnualImage_showPreYear(set_InComparedAnnualImage)

* - default: 0
* - domain: [0|1]
* - description: todo
* - identifier: startAtMinValue
* - type: Boolean
PARAMETER par_InComparedAnnualImage_startAtMinValue(set_InComparedAnnualImage)

* - default: 1
* - domain: [0|1]
* - description: todo
* - identifier: autoTickY
* - type: Boolean
PARAMETER par_InComparedAnnualImage_autoTickY(set_InComparedAnnualImage)

* - default: 0
* - domain: [0,10]
* - description: todo
* - identifier: customImageId
* - type: Integer
PARAMETER par_InComparedAnnualImage_customImageId(set_InComparedAnnualImage)

* - description: Bestimmt die verwendete Farbpalette für die Abbildung. Falls keine ausgewählt wurde, wird das Standardfarbschema verwendet.
* - identifier: Zu nutzende Farben
* - type: Boolean
PARAMETER par_link_InComparedAnnualImage_InColorPalette_colorPalette(set_InComparedAnnualImage,set_InColorPalette)

* - description: todo
* - identifier: realData
* - type: Boolean
PARAMETER par_link_InComparedAnnualImage_InRealAdoptionDataFile_realData(set_InComparedAnnualImage,set_InRealAdoptionDataFile)

* - identifier: InComparedAnnualZipImage
* - type: String
SET set_InComparedAnnualZipImage(set_InLoggingResultImage2)

* - default: 1
* - domain: [0|1]
* - description: todo
* - identifier: enabled
* - type: Boolean
PARAMETER par_InComparedAnnualZipImage_enabled(set_InComparedAnnualZipImage)

* - default: 1
* - domain: [0|1]
* - description: todo
* - identifier: useGnuplot
* - rule: IF (par_InComparedAnnualZipImage_useGnuplot == 1, par_InComparedAnnualZipImage_useR = 0)
* - rule: IF (par_InComparedAnnualZipImage_useGnuplot == 0, par_InComparedAnnualZipImage_useGnuplot = 1)
* - rule: IF (par_InComparedAnnualZipImage_useGnuplot == 0, par_InComparedAnnualZipImage_useR = 0)
* - type: Boolean
PARAMETER par_InComparedAnnualZipImage_useGnuplot(set_InComparedAnnualZipImage)

* - default: 0
* - domain: [0|1]
* - description: todo
* - identifier: useR
* - rule: IF (par_InComparedAnnualZipImage_useR == 1, par_InComparedAnnualZipImage_useGnuplot = 0)
* - rule: IF (par_InComparedAnnualZipImage_useR == 0, par_InComparedAnnualZipImage_useGnuplot = 0)
* - rule: IF (par_InComparedAnnualZipImage_useR == 0, par_InComparedAnnualZipImage_useR = 1)
* - type: Boolean
PARAMETER par_InComparedAnnualZipImage_useR(set_InComparedAnnualZipImage)

* - default: 0
* - domain: [0|1]
* - description: todo
* - identifier: storeScript
* - type: Boolean
PARAMETER par_InComparedAnnualZipImage_storeScript(set_InComparedAnnualZipImage)

* - default: 0
* - domain: [0|1]
* - description: todo
* - identifier: storeData
* - type: Boolean
PARAMETER par_InComparedAnnualZipImage_storeData(set_InComparedAnnualZipImage)

* - default: 1
* - domain: [0|1]
* - description: todo
* - identifier: storeImage
* - type: Boolean
PARAMETER par_InComparedAnnualZipImage_storeImage(set_InComparedAnnualZipImage)

* - default: 1280
* - domain: (0,)
* - description: todo
* - identifier: imageWidth
* - unit: [Pixel]
* - type: Integer
PARAMETER par_InComparedAnnualZipImage_imageWidth(set_InComparedAnnualZipImage)

* - default: 720
* - domain: (0,)
* - description: todo
* - identifier: imageHeight
* - unit: [Pixel]
* - type: Integer
PARAMETER par_InComparedAnnualZipImage_imageHeight(set_InComparedAnnualZipImage)

* - default: 1
* - domain: (0,)
* - description: todo
* - identifier: linewidth
* - type: Integer
PARAMETER par_InComparedAnnualZipImage_linewidth(set_InComparedAnnualZipImage)

* - default: 1
* - domain: [0|1]
* - description: todo
* - identifier: skipInvalidZips
* - type: Boolean
PARAMETER par_InComparedAnnualZipImage_skipInvalidZips(set_InComparedAnnualZipImage)

* - default: 1
* - domain: [0|1]
* - description: todo
* - identifier: showPreYear
* - type: Boolean
PARAMETER par_InComparedAnnualZipImage_showPreYear(set_InComparedAnnualZipImage)

* - default: 0
* - domain: [0|1]
* - description: todo
* - identifier: startAtMinValue
* - type: Boolean
PARAMETER par_InComparedAnnualZipImage_startAtMinValue(set_InComparedAnnualZipImage)

* - default: 1
* - domain: [0|1]
* - description: todo
* - identifier: autoTickY
* - type: Boolean
PARAMETER par_InComparedAnnualZipImage_autoTickY(set_InComparedAnnualZipImage)

* - default: 0
* - domain: [0,10]
* - description: todo
* - identifier: customImageId
* - type: Integer
PARAMETER par_InComparedAnnualZipImage_customImageId(set_InComparedAnnualZipImage)

* - description: Bestimmt die verwendete Farbpalette für die Abbildung. Falls keine ausgewählt wurde, wird das Standardfarbschema verwendet.
* - identifier: Zu nutzende Farben
* - type: Boolean
PARAMETER par_link_InComparedAnnualZipImage_InColorPalette_colorPalette(set_InComparedAnnualZipImage,set_InColorPalette)

* - description: todo
* - identifier: realData
* - type: Boolean
PARAMETER par_link_InComparedAnnualZipImage_InRealAdoptionDataFile_realData(set_InComparedAnnualZipImage,set_InRealAdoptionDataFile)

* - identifier: InComparedCumulatedAnnualImage
* - type: String
SET set_InComparedCumulatedAnnualImage(set_InLoggingResultImage2)

* - default: 1
* - domain: [0|1]
* - description: todo
* - identifier: enabled
* - type: Boolean
PARAMETER par_InComparedCumulatedAnnualImage_enabled(set_InComparedCumulatedAnnualImage)

* - default: 1
* - domain: [0|1]
* - description: todo
* - identifier: useGnuplot
* - rule: IF (par_InComparedCumulatedAnnualImage_useGnuplot == 1, par_InComparedCumulatedAnnualImage_useR = 0)
* - rule: IF (par_InComparedCumulatedAnnualImage_useGnuplot == 0, par_InComparedCumulatedAnnualImage_useGnuplot = 1)
* - rule: IF (par_InComparedCumulatedAnnualImage_useGnuplot == 0, par_InComparedCumulatedAnnualImage_useR = 0)
* - type: Boolean
PARAMETER par_InComparedCumulatedAnnualImage_useGnuplot(set_InComparedCumulatedAnnualImage)

* - default: 0
* - domain: [0|1]
* - description: todo
* - identifier: useR
* - rule: IF (par_InComparedCumulatedAnnualImage_useR == 1, par_InComparedCumulatedAnnualImage_useGnuplot = 0)
* - rule: IF (par_InComparedCumulatedAnnualImage_useR == 0, par_InComparedCumulatedAnnualImage_useGnuplot = 0)
* - rule: IF (par_InComparedCumulatedAnnualImage_useR == 0, par_InComparedCumulatedAnnualImage_useR = 1)
* - type: Boolean
PARAMETER par_InComparedCumulatedAnnualImage_useR(set_InComparedCumulatedAnnualImage)

* - default: 0
* - domain: [0|1]
* - description: todo
* - identifier: storeScript
* - type: Boolean
PARAMETER par_InComparedCumulatedAnnualImage_storeScript(set_InComparedCumulatedAnnualImage)

* - default: 0
* - domain: [0|1]
* - description: todo
* - identifier: storeData
* - type: Boolean
PARAMETER par_InComparedCumulatedAnnualImage_storeData(set_InComparedCumulatedAnnualImage)

* - default: 1
* - domain: [0|1]
* - description: todo
* - identifier: storeImage
* - type: Boolean
PARAMETER par_InComparedCumulatedAnnualImage_storeImage(set_InComparedCumulatedAnnualImage)

* - default: 1280
* - domain: (0,)
* - description: todo
* - identifier: imageWidth
* - unit: [Pixel]
* - type: Integer
PARAMETER par_InComparedCumulatedAnnualImage_imageWidth(set_InComparedCumulatedAnnualImage)

* - default: 720
* - domain: (0,)
* - description: todo
* - identifier: imageHeight
* - unit: [Pixel]
* - type: Integer
PARAMETER par_InComparedCumulatedAnnualImage_imageHeight(set_InComparedCumulatedAnnualImage)

* - default: 1
* - domain: (0,)
* - description: todo
* - identifier: linewidth
* - type: Integer
PARAMETER par_InComparedCumulatedAnnualImage_linewidth(set_InComparedCumulatedAnnualImage)

* - default: 1
* - domain: [0|1]
* - description: todo
* - identifier: skipInvalidZips
* - type: Boolean
PARAMETER par_InComparedCumulatedAnnualImage_skipInvalidZips(set_InComparedCumulatedAnnualImage)

* - default: 1
* - domain: [0|1]
* - description: todo
* - identifier: showPreYear
* - type: Boolean
PARAMETER par_InComparedCumulatedAnnualImage_showPreYear(set_InComparedCumulatedAnnualImage)

* - default: 0
* - domain: [0|1]
* - description: todo
* - identifier: startAtMinValue
* - type: Boolean
PARAMETER par_InComparedCumulatedAnnualImage_startAtMinValue(set_InComparedCumulatedAnnualImage)

* - default: 1
* - domain: [0|1]
* - description: todo
* - identifier: autoTickY
* - type: Boolean
PARAMETER par_InComparedCumulatedAnnualImage_autoTickY(set_InComparedCumulatedAnnualImage)

* - default: 0
* - domain: [0,10]
* - description: todo
* - identifier: customImageId
* - type: Integer
PARAMETER par_InComparedCumulatedAnnualImage_customImageId(set_InComparedCumulatedAnnualImage)

* - description: Bestimmt die verwendete Farbpalette für die Abbildung. Falls keine ausgewählt wurde, wird das Standardfarbschema verwendet.
* - identifier: Zu nutzende Farben
* - type: Boolean
PARAMETER par_link_InComparedCumulatedAnnualImage_InColorPalette_colorPalette(set_InComparedCumulatedAnnualImage,set_InColorPalette)

* - description: todo
* - identifier: realData
* - type: Boolean
PARAMETER par_link_InComparedCumulatedAnnualImage_InRealAdoptionDataFile_realData(set_InComparedCumulatedAnnualImage,set_InRealAdoptionDataFile)

* - identifier: InComparedCumulatedAnnualZipImage
* - type: String
SET set_InComparedCumulatedAnnualZipImage(set_InLoggingResultImage2)

* - default: 1
* - domain: [0|1]
* - description: todo
* - identifier: enabled
* - type: Boolean
PARAMETER par_InComparedCumulatedAnnualZipImage_enabled(set_InComparedCumulatedAnnualZipImage)

* - default: 1
* - domain: [0|1]
* - description: todo
* - identifier: useGnuplot
* - rule: IF (par_InComparedCumulatedAnnualZipImage_useGnuplot == 1, par_InComparedCumulatedAnnualZipImage_useR = 0)
* - rule: IF (par_InComparedCumulatedAnnualZipImage_useGnuplot == 0, par_InComparedCumulatedAnnualZipImage_useGnuplot = 1)
* - rule: IF (par_InComparedCumulatedAnnualZipImage_useGnuplot == 0, par_InComparedCumulatedAnnualZipImage_useR = 0)
* - type: Boolean
PARAMETER par_InComparedCumulatedAnnualZipImage_useGnuplot(set_InComparedCumulatedAnnualZipImage)

* - default: 0
* - domain: [0|1]
* - description: todo
* - identifier: useR
* - rule: IF (par_InComparedCumulatedAnnualZipImage_useR == 1, par_InComparedCumulatedAnnualZipImage_useGnuplot = 0)
* - rule: IF (par_InComparedCumulatedAnnualZipImage_useR == 0, par_InComparedCumulatedAnnualZipImage_useGnuplot = 0)
* - rule: IF (par_InComparedCumulatedAnnualZipImage_useR == 0, par_InComparedCumulatedAnnualZipImage_useR = 1)
* - type: Boolean
PARAMETER par_InComparedCumulatedAnnualZipImage_useR(set_InComparedCumulatedAnnualZipImage)

* - default: 0
* - domain: [0|1]
* - description: todo
* - identifier: storeScript
* - type: Boolean
PARAMETER par_InComparedCumulatedAnnualZipImage_storeScript(set_InComparedCumulatedAnnualZipImage)

* - default: 0
* - domain: [0|1]
* - description: todo
* - identifier: storeData
* - type: Boolean
PARAMETER par_InComparedCumulatedAnnualZipImage_storeData(set_InComparedCumulatedAnnualZipImage)

* - default: 1
* - domain: [0|1]
* - description: todo
* - identifier: storeImage
* - type: Boolean
PARAMETER par_InComparedCumulatedAnnualZipImage_storeImage(set_InComparedCumulatedAnnualZipImage)

* - default: 1280
* - domain: (0,)
* - description: todo
* - identifier: imageWidth
* - unit: [Pixel]
* - type: Integer
PARAMETER par_InComparedCumulatedAnnualZipImage_imageWidth(set_InComparedCumulatedAnnualZipImage)

* - default: 720
* - domain: (0,)
* - description: todo
* - identifier: imageHeight
* - unit: [Pixel]
* - type: Integer
PARAMETER par_InComparedCumulatedAnnualZipImage_imageHeight(set_InComparedCumulatedAnnualZipImage)

* - default: 1
* - domain: (0,)
* - description: todo
* - identifier: linewidth
* - type: Integer
PARAMETER par_InComparedCumulatedAnnualZipImage_linewidth(set_InComparedCumulatedAnnualZipImage)

* - default: 1
* - domain: [0|1]
* - description: todo
* - identifier: skipInvalidZips
* - type: Boolean
PARAMETER par_InComparedCumulatedAnnualZipImage_skipInvalidZips(set_InComparedCumulatedAnnualZipImage)

* - default: 1
* - domain: [0|1]
* - description: todo
* - identifier: showPreYear
* - type: Boolean
PARAMETER par_InComparedCumulatedAnnualZipImage_showPreYear(set_InComparedCumulatedAnnualZipImage)

* - default: 0
* - domain: [0|1]
* - description: todo
* - identifier: startAtMinValue
* - type: Boolean
PARAMETER par_InComparedCumulatedAnnualZipImage_startAtMinValue(set_InComparedCumulatedAnnualZipImage)

* - default: 1
* - domain: [0|1]
* - description: todo
* - identifier: autoTickY
* - type: Boolean
PARAMETER par_InComparedCumulatedAnnualZipImage_autoTickY(set_InComparedCumulatedAnnualZipImage)

* - default: 0
* - domain: [0,10]
* - description: todo
* - identifier: customImageId
* - type: Integer
PARAMETER par_InComparedCumulatedAnnualZipImage_customImageId(set_InComparedCumulatedAnnualZipImage)

* - description: Bestimmt die verwendete Farbpalette für die Abbildung. Falls keine ausgewählt wurde, wird das Standardfarbschema verwendet.
* - identifier: Zu nutzende Farben
* - type: Boolean
PARAMETER par_link_InComparedCumulatedAnnualZipImage_InColorPalette_colorPalette(set_InComparedCumulatedAnnualZipImage,set_InColorPalette)

* - description: todo
* - identifier: realData
* - type: Boolean
PARAMETER par_link_InComparedCumulatedAnnualZipImage_InRealAdoptionDataFile_realData(set_InComparedCumulatedAnnualZipImage,set_InRealAdoptionDataFile)

* - identifier: InCumulatedAnnualInterestImage
* - type: String
SET set_InCumulatedAnnualInterestImage(set_InLoggingResultImage2)

* - default: 1
* - domain: [0|1]
* - description: todo
* - identifier: enabled
* - type: Boolean
PARAMETER par_InCumulatedAnnualInterestImage_enabled(set_InCumulatedAnnualInterestImage)

* - default: 1
* - domain: [0|1]
* - description: todo
* - identifier: useGnuplot
* - rule: IF (par_InCumulatedAnnualInterestImage_useGnuplot == 1, par_InCumulatedAnnualInterestImage_useR = 0)
* - rule: IF (par_InCumulatedAnnualInterestImage_useGnuplot == 0, par_InCumulatedAnnualInterestImage_useGnuplot = 1)
* - rule: IF (par_InCumulatedAnnualInterestImage_useGnuplot == 0, par_InCumulatedAnnualInterestImage_useR = 0)
* - type: Boolean
PARAMETER par_InCumulatedAnnualInterestImage_useGnuplot(set_InCumulatedAnnualInterestImage)

* - default: 0
* - domain: [0|1]
* - description: todo
* - identifier: useR
* - rule: IF (par_InCumulatedAnnualInterestImage_useR == 1, par_InCumulatedAnnualInterestImage_useGnuplot = 0)
* - rule: IF (par_InCumulatedAnnualInterestImage_useR == 0, par_InCumulatedAnnualInterestImage_useGnuplot = 0)
* - rule: IF (par_InCumulatedAnnualInterestImage_useR == 0, par_InCumulatedAnnualInterestImage_useR = 1)
* - type: Boolean
PARAMETER par_InCumulatedAnnualInterestImage_useR(set_InCumulatedAnnualInterestImage)

* - default: 0
* - domain: [0|1]
* - description: todo
* - identifier: storeScript
* - type: Boolean
PARAMETER par_InCumulatedAnnualInterestImage_storeScript(set_InCumulatedAnnualInterestImage)

* - default: 0
* - domain: [0|1]
* - description: todo
* - identifier: storeData
* - type: Boolean
PARAMETER par_InCumulatedAnnualInterestImage_storeData(set_InCumulatedAnnualInterestImage)

* - default: 1
* - domain: [0|1]
* - description: todo
* - identifier: storeImage
* - type: Boolean
PARAMETER par_InCumulatedAnnualInterestImage_storeImage(set_InCumulatedAnnualInterestImage)

* - default: 1280
* - domain: (0,)
* - description: todo
* - identifier: imageWidth
* - unit: [Pixel]
* - type: Integer
PARAMETER par_InCumulatedAnnualInterestImage_imageWidth(set_InCumulatedAnnualInterestImage)

* - default: 720
* - domain: (0,)
* - description: todo
* - identifier: imageHeight
* - unit: [Pixel]
* - type: Integer
PARAMETER par_InCumulatedAnnualInterestImage_imageHeight(set_InCumulatedAnnualInterestImage)

* - default: 1
* - domain: (0,)
* - description: todo
* - identifier: linewidth
* - type: Integer
PARAMETER par_InCumulatedAnnualInterestImage_linewidth(set_InCumulatedAnnualInterestImage)

* - default: 0
* - domain: [0|1]
* - description: todo
* - identifier: Benutzerdefinierten Minimalwert (Min Y) für die Y-Achse nutzen?
* - type: Boolean
PARAMETER par_InCumulatedAnnualInterestImage_useCustomYRangeMin(set_InCumulatedAnnualInterestImage)

* - default: 0.0
* - description: todo
* - identifier: Min Y (Achse)
* - type: Float
PARAMETER par_InCumulatedAnnualInterestImage_minY(set_InCumulatedAnnualInterestImage)

* - default: 0
* - domain: [0|1]
* - description: todo
* - identifier: Benutzerdefinierten Maximalwert (Max Y) für die Y-Achse nutzen?
* - type: Boolean
PARAMETER par_InCumulatedAnnualInterestImage_useCustomYRangeMax(set_InCumulatedAnnualInterestImage)

* - default: 0.0
* - description: todo
* - identifier: Max Y (Achse)
* - type: Float
PARAMETER par_InCumulatedAnnualInterestImage_maxY(set_InCumulatedAnnualInterestImage)

* - default: 0
* - domain: [0,10]
* - description: todo
* - identifier: Benutzerdefinierte Bild-Id
* - type: Integer
PARAMETER par_InCumulatedAnnualInterestImage_customImageId(set_InCumulatedAnnualInterestImage)

* - description: Bestimmt die verwendete Farbpalette für die Abbildung. Falls keine ausgewählt wurde, wird das Standardfarbschema verwendet.
* - identifier: Zu nutzende Farben
* - type: Boolean
PARAMETER par_link_InCumulatedAnnualInterestImage_InColorPalette_colorPalette(set_InCumulatedAnnualInterestImage,set_InColorPalette)

* - identifier: InCustomAverageQuantilRangeImage
* - type: String
SET set_InCustomAverageQuantilRangeImage(set_InLoggingResultImage2)

* - default: 1
* - domain: [0|1]
* - description: todo
* - identifier: enabled
* - type: Boolean
PARAMETER par_InCustomAverageQuantilRangeImage_enabled(set_InCustomAverageQuantilRangeImage)

* - default: 1
* - domain: [0|1]
* - description: todo
* - identifier: useGnuplot
* - rule: IF (par_InCustomAverageQuantilRangeImage_useGnuplot == 1, par_InCustomAverageQuantilRangeImage_useR = 0)
* - rule: IF (par_InCustomAverageQuantilRangeImage_useGnuplot == 0, par_InCustomAverageQuantilRangeImage_useGnuplot = 1)
* - rule: IF (par_InCustomAverageQuantilRangeImage_useGnuplot == 0, par_InCustomAverageQuantilRangeImage_useR = 0)
* - type: Boolean
PARAMETER par_InCustomAverageQuantilRangeImage_useGnuplot(set_InCustomAverageQuantilRangeImage)

* - default: 0
* - domain: [0|1]
* - description: todo
* - identifier: useR
* - rule: IF (par_InCustomAverageQuantilRangeImage_useR == 1, par_InCustomAverageQuantilRangeImage_useGnuplot = 0)
* - rule: IF (par_InCustomAverageQuantilRangeImage_useR == 0, par_InCustomAverageQuantilRangeImage_useGnuplot = 0)
* - rule: IF (par_InCustomAverageQuantilRangeImage_useR == 0, par_InCustomAverageQuantilRangeImage_useR = 1)
* - type: Boolean
PARAMETER par_InCustomAverageQuantilRangeImage_useR(set_InCustomAverageQuantilRangeImage)

* - default: 0
* - domain: [0|1]
* - description: todo
* - identifier: storeScript
* - type: Boolean
PARAMETER par_InCustomAverageQuantilRangeImage_storeScript(set_InCustomAverageQuantilRangeImage)

* - default: 0
* - domain: [0|1]
* - description: todo
* - identifier: storeData
* - type: Boolean
PARAMETER par_InCustomAverageQuantilRangeImage_storeData(set_InCustomAverageQuantilRangeImage)

* - default: 1
* - domain: [0|1]
* - description: todo
* - identifier: storeImage
* - type: Boolean
PARAMETER par_InCustomAverageQuantilRangeImage_storeImage(set_InCustomAverageQuantilRangeImage)

* - default: 1
* - domain: [0|1]
* - description: todo
* - identifier: printAverage
* - type: Boolean
PARAMETER par_InCustomAverageQuantilRangeImage_printAverage(set_InCustomAverageQuantilRangeImage)

* - default: 1280
* - domain: (0,)
* - description: todo
* - identifier: imageWidth
* - unit: [Pixel]
* - type: Integer
PARAMETER par_InCustomAverageQuantilRangeImage_imageWidth(set_InCustomAverageQuantilRangeImage)

* - default: 1280
* - domain: (0,)
* - description: todo
* - identifier: imageHeight
* - unit: [Pixel]
* - type: Integer
PARAMETER par_InCustomAverageQuantilRangeImage_imageHeight(set_InCustomAverageQuantilRangeImage)

* - default: 1
* - domain: (0,)
* - description: todo
* - identifier: linewidth
* - type: Integer
PARAMETER par_InCustomAverageQuantilRangeImage_linewidth(set_InCustomAverageQuantilRangeImage)

* - default: 0
* - domain: [0,10]
* - description: todo
* - identifier: customImageId
* - type: Integer
PARAMETER par_InCustomAverageQuantilRangeImage_customImageId(set_InCustomAverageQuantilRangeImage)

* - description: Bestimmt die verwendete Farbpalette für die Abbildung. Falls keine ausgewählt wurde, wird das Standardfarbschema verwendet.
* - identifier: Zu nutzende Farben
* - type: Boolean
PARAMETER par_link_InCustomAverageQuantilRangeImage_InColorPalette_colorPalette(set_InCustomAverageQuantilRangeImage,set_InColorPalette)

* - description: todo
* - identifier: ranges
* - type: Boolean
PARAMETER par_link_InCustomAverageQuantilRangeImage_InQuantileRange_ranges(set_InCustomAverageQuantilRangeImage,set_InQuantileRange)

* - description: todo
* - identifier: loggingModules
* - type: Boolean
PARAMETER par_link_InCustomAverageQuantilRangeImage_InConsumerAgentCalculationLoggingModule2_loggingModules(set_InCustomAverageQuantilRangeImage,set_InConsumerAgentCalculationLoggingModule2)

* - identifier: InInterestOverviewImage
* - type: String
SET set_InInterestOverviewImage(set_InLoggingResultImage2)

* - default: 1
* - domain: [0|1]
* - description: todo
* - identifier: enabled
* - type: Boolean
PARAMETER par_InInterestOverviewImage_enabled(set_InInterestOverviewImage)

* - default: 1
* - domain: [0|1]
* - description: todo
* - identifier: useGnuplot
* - rule: IF (par_InInterestOverviewImage_useGnuplot == 1, par_InInterestOverviewImage_useR = 0)
* - rule: IF (par_InInterestOverviewImage_useGnuplot == 0, par_InInterestOverviewImage_useGnuplot = 1)
* - rule: IF (par_InInterestOverviewImage_useGnuplot == 0, par_InInterestOverviewImage_useR = 0)
* - type: Boolean
PARAMETER par_InInterestOverviewImage_useGnuplot(set_InInterestOverviewImage)

* - default: 0
* - domain: [0|1]
* - description: todo
* - identifier: useR
* - rule: IF (par_InInterestOverviewImage_useR == 1, par_InInterestOverviewImage_useGnuplot = 0)
* - rule: IF (par_InInterestOverviewImage_useR == 0, par_InInterestOverviewImage_useGnuplot = 0)
* - rule: IF (par_InInterestOverviewImage_useR == 0, par_InInterestOverviewImage_useR = 1)
* - type: Boolean
PARAMETER par_InInterestOverviewImage_useR(set_InInterestOverviewImage)

* - default: 0
* - domain: [0|1]
* - description: todo
* - identifier: storeScript
* - type: Boolean
PARAMETER par_InInterestOverviewImage_storeScript(set_InInterestOverviewImage)

* - default: 0
* - domain: [0|1]
* - description: todo
* - identifier: storeData
* - type: Boolean
PARAMETER par_InInterestOverviewImage_storeData(set_InInterestOverviewImage)

* - default: 1
* - domain: [0|1]
* - description: todo
* - identifier: storeImage
* - type: Boolean
PARAMETER par_InInterestOverviewImage_storeImage(set_InInterestOverviewImage)

* - default: 1280
* - domain: (0,)
* - description: todo
* - identifier: imageWidth
* - unit: [Pixel]
* - type: Integer
PARAMETER par_InInterestOverviewImage_imageWidth(set_InInterestOverviewImage)

* - default: 720
* - domain: (0,)
* - description: todo
* - identifier: imageHeight
* - unit: [Pixel]
* - type: Integer
PARAMETER par_InInterestOverviewImage_imageHeight(set_InInterestOverviewImage)

* - default: 0.8
* - domain: (0,)
* - description: todo
* - identifier: boxWidth
* - type: Float
PARAMETER par_InInterestOverviewImage_boxWidth(set_InInterestOverviewImage)

* - default: 0
* - domain: [0,10]
* - description: todo
* - identifier: customImageId
* - type: Integer
PARAMETER par_InInterestOverviewImage_customImageId(set_InInterestOverviewImage)

* - description: Bestimmt die verwendete Farbpalette für die Abbildung. Falls keine ausgewählt wurde, wird das Standardfarbschema verwendet.
* - identifier: Zu nutzende Farben
* - type: Boolean
PARAMETER par_link_InInterestOverviewImage_InColorPalette_colorPalette(set_InInterestOverviewImage,set_InColorPalette)

* - identifier: InLoggingResultImage2
* - hidden: 1
* - type: String
SET set_InLoggingResultImage2(set_InOutputImage2)

* - identifier: InQuantileRange
* - type: String
SET set_InQuantileRange(*)

* - default: 0
* - domain: [0,1]
* - description: todo
* - identifier: lowerBound
* - type: Float
PARAMETER par_InQuantileRange_lowerBound(set_InQuantileRange)

* - default: 1
* - domain: [0,1]
* - description: todo
* - identifier: upperBound
* - type: Float
PARAMETER par_InQuantileRange_upperBound(set_InQuantileRange)

* - identifier: InProcessPhaseOverviewImage
* - type: String
SET set_InProcessPhaseOverviewImage(set_InLoggingResultImage2)

* - default: 1
* - domain: [0|1]
* - description: todo
* - identifier: enabled
* - type: Boolean
PARAMETER par_InProcessPhaseOverviewImage_enabled(set_InProcessPhaseOverviewImage)

* - default: 1
* - domain: [0|1]
* - description: todo
* - identifier: useGnuplot
* - rule: IF (par_InProcessPhaseOverviewImage_useGnuplot == 1, par_InProcessPhaseOverviewImage_useR = 0)
* - rule: IF (par_InProcessPhaseOverviewImage_useGnuplot == 0, par_InProcessPhaseOverviewImage_useGnuplot = 1)
* - rule: IF (par_InProcessPhaseOverviewImage_useGnuplot == 0, par_InProcessPhaseOverviewImage_useR = 0)
* - type: Boolean
PARAMETER par_InProcessPhaseOverviewImage_useGnuplot(set_InProcessPhaseOverviewImage)

* - default: 0
* - domain: [0|1]
* - description: todo
* - identifier: useR
* - rule: IF (par_InProcessPhaseOverviewImage_useR == 1, par_InProcessPhaseOverviewImage_useGnuplot = 0)
* - rule: IF (par_InProcessPhaseOverviewImage_useR == 0, par_InProcessPhaseOverviewImage_useGnuplot = 0)
* - rule: IF (par_InProcessPhaseOverviewImage_useR == 0, par_InProcessPhaseOverviewImage_useR = 1)
* - type: Boolean
PARAMETER par_InProcessPhaseOverviewImage_useR(set_InProcessPhaseOverviewImage)

* - default: 0
* - domain: [0|1]
* - description: todo
* - identifier: storeScript
* - type: Boolean
PARAMETER par_InProcessPhaseOverviewImage_storeScript(set_InProcessPhaseOverviewImage)

* - default: 0
* - domain: [0|1]
* - description: todo
* - identifier: storeData
* - type: Boolean
PARAMETER par_InProcessPhaseOverviewImage_storeData(set_InProcessPhaseOverviewImage)

* - default: 1
* - domain: [0|1]
* - description: todo
* - identifier: storeImage
* - type: Boolean
PARAMETER par_InProcessPhaseOverviewImage_storeImage(set_InProcessPhaseOverviewImage)

* - default: 1280
* - domain: (0,)
* - description: todo
* - identifier: imageWidth
* - unit: [Pixel]
* - type: Integer
PARAMETER par_InProcessPhaseOverviewImage_imageWidth(set_InProcessPhaseOverviewImage)

* - default: 720
* - domain: (0,)
* - description: todo
* - identifier: imageHeight
* - unit: [Pixel]
* - type: Integer
PARAMETER par_InProcessPhaseOverviewImage_imageHeight(set_InProcessPhaseOverviewImage)

* - default: 0.8
* - domain: (0,)
* - description: todo
* - identifier: boxWidth
* - type: Float
PARAMETER par_InProcessPhaseOverviewImage_boxWidth(set_InProcessPhaseOverviewImage)

* - default: 0
* - domain: [0,10]
* - description: todo
* - identifier: customImageId
* - type: Integer
PARAMETER par_InProcessPhaseOverviewImage_customImageId(set_InProcessPhaseOverviewImage)

* - description: Bestimmt die verwendete Farbpalette für die Abbildung. Falls keine ausgewählt wurde, wird das Standardfarbschema verwendet.
* - identifier: Zu nutzende Farben
* - type: Boolean
PARAMETER par_link_InProcessPhaseOverviewImage_InColorPalette_colorPalette(set_InProcessPhaseOverviewImage,set_InColorPalette)

* - identifier: InOutputImage2
* - hidden: 1
* - type: String
SET set_InOutputImage2(*)

* - identifier: InSpecialAverageQuantilRangeImage
* - type: String
SET set_InSpecialAverageQuantilRangeImage(set_InLoggingResultImage2)

* - default: 1
* - domain: [0|1]
* - description: todo
* - identifier: enabled
* - type: Boolean
PARAMETER par_InSpecialAverageQuantilRangeImage_enabled(set_InSpecialAverageQuantilRangeImage)

* - default: 1
* - domain: [0|1]
* - description: todo
* - identifier: useGnuplot
* - rule: IF (par_InSpecialAverageQuantilRangeImage_useGnuplot == 1, par_InSpecialAverageQuantilRangeImage_useR = 0)
* - rule: IF (par_InSpecialAverageQuantilRangeImage_useGnuplot == 0, par_InSpecialAverageQuantilRangeImage_useGnuplot = 1)
* - rule: IF (par_InSpecialAverageQuantilRangeImage_useGnuplot == 0, par_InSpecialAverageQuantilRangeImage_useR = 0)
* - type: Boolean
PARAMETER par_InSpecialAverageQuantilRangeImage_useGnuplot(set_InSpecialAverageQuantilRangeImage)

* - default: 0
* - domain: [0|1]
* - description: todo
* - identifier: useR
* - rule: IF (par_InSpecialAverageQuantilRangeImage_useR == 1, par_InSpecialAverageQuantilRangeImage_useGnuplot = 0)
* - rule: IF (par_InSpecialAverageQuantilRangeImage_useR == 0, par_InSpecialAverageQuantilRangeImage_useGnuplot = 0)
* - rule: IF (par_InSpecialAverageQuantilRangeImage_useR == 0, par_InSpecialAverageQuantilRangeImage_useR = 1)
* - type: Boolean
PARAMETER par_InSpecialAverageQuantilRangeImage_useR(set_InSpecialAverageQuantilRangeImage)

* - default: 0
* - domain: [0|1]
* - description: todo
* - identifier: storeScript
* - type: Boolean
PARAMETER par_InSpecialAverageQuantilRangeImage_storeScript(set_InSpecialAverageQuantilRangeImage)

* - default: 0
* - domain: [0|1]
* - description: todo
* - identifier: storeData
* - type: Boolean
PARAMETER par_InSpecialAverageQuantilRangeImage_storeData(set_InSpecialAverageQuantilRangeImage)

* - default: 1
* - domain: [0|1]
* - description: todo
* - identifier: storeImage
* - type: Boolean
PARAMETER par_InSpecialAverageQuantilRangeImage_storeImage(set_InSpecialAverageQuantilRangeImage)

* - default: 1
* - domain: [0|1]
* - description: todo
* - identifier: printAverage
* - type: Boolean
PARAMETER par_InSpecialAverageQuantilRangeImage_printAverage(set_InSpecialAverageQuantilRangeImage)

* - default: 1280
* - domain: (0,)
* - description: todo
* - identifier: imageWidth
* - unit: [Pixel]
* - type: Integer
PARAMETER par_InSpecialAverageQuantilRangeImage_imageWidth(set_InSpecialAverageQuantilRangeImage)

* - default: 720
* - domain: (0,)
* - description: todo
* - identifier: imageHeight
* - unit: [Pixel]
* - type: Integer
PARAMETER par_InSpecialAverageQuantilRangeImage_imageHeight(set_InSpecialAverageQuantilRangeImage)

* - default: 1
* - domain: (0,)
* - description: todo
* - identifier: linewidth
* - type: Integer
PARAMETER par_InSpecialAverageQuantilRangeImage_linewidth(set_InSpecialAverageQuantilRangeImage)

* - default: 0
* - domain: [0,10]
* - description: todo
* - identifier: customImageId
* - type: Integer
PARAMETER par_InSpecialAverageQuantilRangeImage_customImageId(set_InSpecialAverageQuantilRangeImage)

* - description: Bestimmt die verwendete Farbpalette für die Abbildung. Falls keine ausgewählt wurde, wird das Standardfarbschema verwendet.
* - identifier: Zu nutzende Farben
* - type: Boolean
PARAMETER par_link_InSpecialAverageQuantilRangeImage_InColorPalette_colorPalette(set_InSpecialAverageQuantilRangeImage,set_InColorPalette)

* - description: todo
* - identifier: loggingModules
* - type: Boolean
PARAMETER par_link_InSpecialAverageQuantilRangeImage_InConsumerAgentCalculationLoggingModule2_loggingModules(set_InSpecialAverageQuantilRangeImage,set_InConsumerAgentCalculationLoggingModule2)

* - identifier: InProductGroupThresholdEntry
* - type: String
SET set_InProductGroupThresholdEntry(*)

* - description: Legt die Verteilungsfunktion für den Grenzwert fest.
* - identifier: Verteilungsfunktion für den Grenzwert
* - type: Boolean
PARAMETER par_link_InProductGroupThresholdEntry_InUnivariateDoubleDistribution_interestDistribution(set_InProductGroupThresholdEntry,set_InUnivariateDoubleDistribution)

* - description: Legt die Produktgruppen fest, zu denen der definierte Grenzwert gehören soll.
* - identifier: Produktgruppen
* - type: Boolean
PARAMETER par_link_InProductGroupThresholdEntry_InProductGroup_productGroups(set_InProductGroupThresholdEntry,set_InProductGroup)

* - identifier: InProductInterestSupplyScheme
* - hidden: 1
* - type: String
SET set_InProductInterestSupplyScheme(*)

* - identifier: InProductThresholdInterestSupplyScheme
* - type: String
SET set_InProductThresholdInterestSupplyScheme(set_InProductInterestSupplyScheme)

* - description: Grenzwerte ab dem Produkte der zugehörigen Produktgruppen von Interesse werden.
* - identifier: Einträge
* - type: Boolean
PARAMETER par_link_InProductThresholdInterestSupplyScheme_InProductGroupThresholdEntry_entries(set_InProductThresholdInterestSupplyScheme,set_InProductGroupThresholdEntry)

* - identifier: InAttributeName
* - type: String
SET set_InAttributeName(set_InName)

* - description: Ungenutzter Platzhalter
* - identifier: ---
* - type: Integer
PARAMETER par_InAttributeName_placeholder(set_InAttributeName)

* - identifier: InName
* - hidden: 1
* - type: String
SET set_InName(*)

* - identifier: InColor
* - hidden: 1
* - type: String
SET set_InColor(*)

* - identifier: InColorARGB
* - type: String
SET set_InColorARGB(set_InColor)

* - default: 5
* - description: Legt die Farbpriorität fest. Dieser Wert kann je nach Verwendung einen unterschiedlichen Verwendungszweck haben. Bei der Nutzung von Farbpaletten legt die Priorität die Reihenfolge der Farben fest. Auf diese Weise lassen sich Farben individuell anordnen.
* - identifier: Priorität
* - type: Integer
PARAMETER par_InColorARGB_priority(set_InColorARGB)

* - default: 255
* - domain: [0,255]
* - description: Legt den Alphawert der Farbe fest. Dieser muss zwischen 0 und 255 liegen.
* - identifier: Alpha
* - type: Integer
PARAMETER par_InColorARGB_alpha(set_InColorARGB)

* - default: 0
* - domain: [0,255]
* - description: Legt den Rotwert der Farbe fest. Dieser muss zwischen 0 und 255 liegen.
* - identifier: Rot
* - type: Integer
PARAMETER par_InColorARGB_red(set_InColorARGB)

* - default: 0
* - domain: [0,255]
* - description: Legt den Grünwert der Farbe fest. Dieser muss zwischen 0 und 255 liegen.
* - identifier: Grün
* - type: Integer
PARAMETER par_InColorARGB_green(set_InColorARGB)

* - default: 0
* - domain: [0,255]
* - description: Legt den Blauwert der Farbe fest. Dieser muss zwischen 0 und 255 liegen.
* - identifier: Blau
* - type: Integer
PARAMETER par_InColorARGB_blue(set_InColorARGB)

* - identifier: InColorPalette
* - type: String
SET set_InColorPalette(*)

* - description: todo
* - identifier: Farben
* - type: Boolean
PARAMETER par_link_InColorPalette_InColor_colors(set_InColorPalette,set_InColor)

* - identifier: InCompleteGraphTopology
* - type: String
SET set_InCompleteGraphTopology(set_InGraphTopologyScheme)

* - default: 0.0
* - description: Legt das initiale Gewicht der Kanten fest.
* - hidden: 1
* - identifier: Initiales Kantengewicht
* - type: Float
PARAMETER par_InCompleteGraphTopology_initialWeight(set_InCompleteGraphTopology)

* - identifier: InDistanceEvaluator
* - hidden: 1
* - type: String
SET set_InDistanceEvaluator(*)

* - identifier: InFreeNetworkTopology
* - type: String
SET set_InFreeNetworkTopology(set_InGraphTopologyScheme)

* - default: 0.0
* - description: Legt das initiale Gewicht der Kanten fest.
* - hidden: 1
* - identifier: Initiales Kantengewicht
* - type: Float
PARAMETER par_InFreeNetworkTopology_initialWeight(set_InFreeNetworkTopology)

* - default: 0
* - domain: [0|1]
* - description: Erlaubt es auch weniger Kanten als gewünscht zu nutzen. Sollten zum Beispiel 10 Kanten gewünscht sein je Knoten, aber für einen bestimmten Knoten nur 5 möglich sein, so werden nur 5 verwendet. Falls diese Option deaktiviert ist, wird ein Fehler geworfen.
* - identifier: Weniger Kanten erlauben?
* - type: Boolean
PARAMETER par_InFreeNetworkTopology_allowLessEdges(set_InFreeNetworkTopology)

* - description: Legt die zu nutzende Distanzfunktion für den Einfluss des Abstandes fest.
* - identifier: Distanzfunktion
* - type: Boolean
PARAMETER par_link_InFreeNetworkTopology_InDistanceEvaluator_distanceEvaluator(set_InFreeNetworkTopology,set_InDistanceEvaluator)

* - description: Legt die Anzahl defür die jeweiligen Konsumergruppen fest.
* - identifier: Konsumergruppenspezifische Kantenanzahl
* - type: Boolean
PARAMETER par_link_InFreeNetworkTopology_InNumberOfTies_numberOfTies(set_InFreeNetworkTopology,set_InNumberOfTies)

* - description: Legt die zu nutzende Affinitätenmenge für den sozialen Einfluss fest.
* - identifier: Affinitäten
* - type: Boolean
PARAMETER par_link_InFreeNetworkTopology_InAffinities_affinities(set_InFreeNetworkTopology,set_InAffinities)

* - identifier: InGraphTopologyScheme
* - hidden: 1
* - type: String
SET set_InGraphTopologyScheme(*)

* - identifier: InInverse
* - type: String
SET set_InInverse(set_InDistanceEvaluator)

* - description: Ungenutzter Platzhalter
* - identifier: ---
* - type: Integer
PARAMETER par_InInverse_placeholderInverse(set_InInverse)

* - identifier: InNoDistance
* - type: String
SET set_InNoDistance(set_InDistanceEvaluator)

* - description: Ungenutzter Platzhalter
* - identifier: ---
* - type: Integer
PARAMETER par_InNoDistance_placeholderNoDistance(set_InNoDistance)

* - identifier: InNumberOfTies
* - type: String
SET set_InNumberOfTies(*)

* - default: 0
* - domain: [0,)
* - description: Legt die Anzahl der Kanten fest.
* - identifier: Kantenanzahl
* - type: Integer
PARAMETER par_InNumberOfTies_count(set_InNumberOfTies)

* - description: Bestimmt die Konsumergruppen, welche diese Anzahl Kanten aufweisen sollen.
* - identifier: Konsumergruppen
* - type: Boolean
PARAMETER par_link_InNumberOfTies_InConsumerAgentGroup_cags(set_InNumberOfTies,set_InConsumerAgentGroup)

* - identifier: InUnlinkedGraphTopology
* - type: String
SET set_InUnlinkedGraphTopology(set_InGraphTopologyScheme)

* - description: Ungenutzter Platzhalter
* - identifier: ---
* - type: Float
PARAMETER par_InUnlinkedGraphTopology_placeholderUnlinked(set_InUnlinkedGraphTopology)

* - identifier: InPVactGlobalModerateExtremistUncertaintyWithUpdatableOpinion
* - type: String
SET set_InPVactGlobalModerateExtremistUncertaintyWithUpdatableOpinion(set_InUncertaintySupplier)

* - default: 0.125
* - domain: [0,1]
* - description: Legt den Extremistenparameter fest. Dieser Wert beschreibt den prozentualen Anteil der Exremisten.
* - identifier: Extremistenparameter
* - type: Float
PARAMETER par_InPVactGlobalModerateExtremistUncertaintyWithUpdatableOpinion_extremistParameter(set_InPVactGlobalModerateExtremistUncertaintyWithUpdatableOpinion)

* - default: 0.21
* - description: Legt den Wert der Unsicherheit für Extremisten fest.
* - identifier: Unsicherheit der Extremisten
* - type: Float
PARAMETER par_InPVactGlobalModerateExtremistUncertaintyWithUpdatableOpinion_extremistUncertainty(set_InPVactGlobalModerateExtremistUncertaintyWithUpdatableOpinion)

* - default: 0.4
* - description: Legt den Wert der Unsicherheit für Moderate fest.
* - identifier: Unsicherheit der Moderaten
* - type: Float
PARAMETER par_InPVactGlobalModerateExtremistUncertaintyWithUpdatableOpinion_moderateUncertainty(set_InPVactGlobalModerateExtremistUncertaintyWithUpdatableOpinion)

* - default: 1
* - domain: [0|1]
* - description: Legt fest, ob der untere Grenzwert inklusiv (>=) oder exklusiv (>) ist.
* - identifier: Untere Grenze inklusive?
* - type: Boolean
PARAMETER par_InPVactGlobalModerateExtremistUncertaintyWithUpdatableOpinion_lowerBoundInclusive(set_InPVactGlobalModerateExtremistUncertaintyWithUpdatableOpinion)

* - default: 1
* - domain: [0|1]
* - description: Legt fest, ob der obere Grenzwert inklusiv (<=) oder exklusiv (<) ist.
* - identifier: Obere Grenze inklusive?
* - type: Boolean
PARAMETER par_InPVactGlobalModerateExtremistUncertaintyWithUpdatableOpinion_upperBoundInclusive(set_InPVactGlobalModerateExtremistUncertaintyWithUpdatableOpinion)

* - identifier: InPVactIndividualGlobalModerateExtremistUncertaintySupplier
* - type: String
SET set_InPVactIndividualGlobalModerateExtremistUncertaintySupplier(set_InUncertaintySupplier)

* - default: 0.125
* - domain: [0,1]
* - description: Legt den Extremistenparameter fest. Dieser Wert beschreibt den prozentualen Anteil der Exremisten.
* - identifier: Extremistenparameter
* - type: Float
PARAMETER par_InPVactIndividualGlobalModerateExtremistUncertaintySupplier_extremistParameter(set_InPVactIndividualGlobalModerateExtremistUncertaintySupplier)

* - default: 0.21
* - description: Legt den Wert der Unsicherheit für Extremisten fest.
* - identifier: Unsicherheit der Extremisten
* - type: Float
PARAMETER par_InPVactIndividualGlobalModerateExtremistUncertaintySupplier_extremistUncertainty(set_InPVactIndividualGlobalModerateExtremistUncertaintySupplier)

* - default: 0.4
* - description: Legt den Wert der Unsicherheit für Moderate fest.
* - identifier: Unsicherheit der Moderaten
* - type: Float
PARAMETER par_InPVactIndividualGlobalModerateExtremistUncertaintySupplier_moderateUncertainty(set_InPVactIndividualGlobalModerateExtremistUncertaintySupplier)

* - default: 1
* - domain: [0|1]
* - description: Legt fest, ob der untere Grenzwert inklusiv (>=) oder exklusiv (>) ist.
* - identifier: Untere Grenze inklusive?
* - type: Boolean
PARAMETER par_InPVactIndividualGlobalModerateExtremistUncertaintySupplier_lowerBoundInclusive(set_InPVactIndividualGlobalModerateExtremistUncertaintySupplier)

* - default: 1
* - domain: [0|1]
* - description: Legt fest, ob der obere Grenzwert inklusiv (<=) oder exklusiv (<) ist.
* - identifier: Obere Grenze inklusive?
* - type: Boolean
PARAMETER par_InPVactIndividualGlobalModerateExtremistUncertaintySupplier_upperBoundInclusive(set_InPVactIndividualGlobalModerateExtremistUncertaintySupplier)

* - identifier: InPVactUpdatableGlobalModerateExtremistUncertainty
* - type: String
SET set_InPVactUpdatableGlobalModerateExtremistUncertainty(set_InUncertaintySupplier)

* - default: 0.125
* - domain: [0,1]
* - description: Legt den Extremistenparameter fest. Dieser Wert beschreibt den prozentualen Anteil der Exremisten.
* - identifier: Extremistenparameter
* - type: Float
PARAMETER par_InPVactUpdatableGlobalModerateExtremistUncertainty_extremistParameter(set_InPVactUpdatableGlobalModerateExtremistUncertainty)

* - default: 0.21
* - description: Legt den Wert der Unsicherheit für Extremisten fest.
* - identifier: Unsicherheit der Extremisten
* - type: Float
PARAMETER par_InPVactUpdatableGlobalModerateExtremistUncertainty_extremistUncertainty(set_InPVactUpdatableGlobalModerateExtremistUncertainty)

* - default: 0.4
* - description: Legt den Wert der Unsicherheit für Moderate fest.
* - identifier: Unsicherheit der Moderaten
* - type: Float
PARAMETER par_InPVactUpdatableGlobalModerateExtremistUncertainty_moderateUncertainty(set_InPVactUpdatableGlobalModerateExtremistUncertainty)

* - default: 1
* - domain: [0|1]
* - description: Legt fest, ob der untere Grenzwert inklusiv (>=) oder exklusiv (>) ist.
* - identifier: Untere Grenze inklusive?
* - type: Boolean
PARAMETER par_InPVactUpdatableGlobalModerateExtremistUncertainty_lowerBoundInclusive(set_InPVactUpdatableGlobalModerateExtremistUncertainty)

* - default: 1
* - domain: [0|1]
* - description: Legt fest, ob der obere Grenzwert inklusiv (<=) oder exklusiv (<) ist.
* - identifier: Obere Grenze inklusive?
* - type: Boolean
PARAMETER par_InPVactUpdatableGlobalModerateExtremistUncertainty_upperBoundInclusive(set_InPVactUpdatableGlobalModerateExtremistUncertainty)

* - identifier: InUncertaintySupplier
* - hidden: 1
* - type: String
SET set_InUncertaintySupplier(*)

* - identifier: InDisabledNodeFilterDistanceScheme
* - type: String
SET set_InDisabledNodeFilterDistanceScheme(set_InNodeDistanceFilterScheme)

* - description: Ungenutzter Platzhalter
* - identifier: ---
* - type: Float
PARAMETER par_InDisabledNodeFilterDistanceScheme_placeholder(set_InDisabledNodeFilterDistanceScheme)

* - identifier: InEntireNetworkNodeFilterDistanceScheme
* - type: String
SET set_InEntireNetworkNodeFilterDistanceScheme(set_InNodeDistanceFilterScheme)

* - description: Ungenutzter Platzhalter
* - identifier: ---
* - type: Float
PARAMETER par_InEntireNetworkNodeFilterDistanceScheme_placeholder(set_InEntireNetworkNodeFilterDistanceScheme)

* - identifier: InMaxDistanceNodeFilterDistanceScheme
* - type: String
SET set_InMaxDistanceNodeFilterDistanceScheme(set_InNodeDistanceFilterScheme)

* - default: 0.0
* - domain: [0,)
* - description: Legt die maximale Entfernung fest. Die Einheit richtet sich sowohl nach den Ausgangsdaten als auch der verwendeten Metric im räumlichen Modell.
* - identifier: Maximale Entfernung
* - type: Float
PARAMETER par_InMaxDistanceNodeFilterDistanceScheme_maxDistance(set_InMaxDistanceNodeFilterDistanceScheme)

* - default: 1
* - domain: [0|1]
* - description: Legt fest, ob der Grenzwert inklusiv (<=) oder exklusiv (<) ist.
* - identifier: Grenzwert inklusive?
* - type: Boolean
PARAMETER par_InMaxDistanceNodeFilterDistanceScheme_inclusive(set_InMaxDistanceNodeFilterDistanceScheme)

* - identifier: InNodeDistanceFilterScheme
* - hidden: 1
* - type: String
SET set_InNodeDistanceFilterScheme(set_InNodeFilterScheme)

* - identifier: InProcessModel
* - hidden: 1
* - type: String
SET set_InProcessModel(*)

* - identifier: InNodeFilterScheme
* - hidden: 1
* - type: String
SET set_InNodeFilterScheme(*)

* - identifier: InModularProcessModel2
* - hidden: 1
* - type: String
SET set_InModularProcessModel2(set_InProcessModel)

* - identifier: InModule2
* - hidden: 1
* - type: String
SET set_InModule2(*)

* - identifier: InBasicCAModularProcessModel
* - type: String
SET set_InBasicCAModularProcessModel(set_InModularProcessModel2)

* - description: todo
* - identifier: Startmodul
* - type: Boolean
PARAMETER par_link_InBasicCAModularProcessModel_InConsumerAgentEvalModule2_startModule(set_InBasicCAModularProcessModel,set_InConsumerAgentEvalModule2)

* - description: todo
* - identifier: Konfiguratoren für die Initialisierung
* - type: Boolean
PARAMETER par_link_InBasicCAModularProcessModel_InInitializationHandler_initializationHandlers(set_InBasicCAModularProcessModel,set_InInitializationHandler)

* - description: todo
* - identifier: Konfiguratoren für neue Produkte
* - type: Boolean
PARAMETER par_link_InBasicCAModularProcessModel_InNewProductHandler_newProductHandlers(set_InBasicCAModularProcessModel,set_InNewProductHandler)

* - description: todo
* - identifier: Reevalueirung zur Initialisierung
* - type: Boolean
PARAMETER par_link_InBasicCAModularProcessModel_InReevaluator2_initializationReevaluators(set_InBasicCAModularProcessModel,set_InReevaluator2)

* - description: todo
* - identifier: Reevalueirung zum Jahresbeginn
* - type: Boolean
PARAMETER par_link_InBasicCAModularProcessModel_InReevaluator2_startOfYearReevaluators(set_InBasicCAModularProcessModel,set_InReevaluator2)

* - description: todo
* - identifier: Reevalueirung zur Jahresmitte (27. Woche)
* - type: Boolean
PARAMETER par_link_InBasicCAModularProcessModel_InReevaluator2_midOfYearReevaluators(set_InBasicCAModularProcessModel,set_InReevaluator2)

* - description: todo
* - identifier: Reevalueirung zum Jahresende
* - type: Boolean
PARAMETER par_link_InBasicCAModularProcessModel_InReevaluator2_endOfYearReevaluators(set_InBasicCAModularProcessModel,set_InReevaluator2)

* - identifier: InConsumerAgentModule2
* - hidden: 1
* - type: String
SET set_InConsumerAgentModule2(set_InModule2)

* - identifier: InCommunicationModule3_actionnode3
* - type: String
SET set_InCommunicationModule3_actionnode3(set_InConsumerAgentActionModule2)

* - default: 3
* - description: todo
* - identifier: Adopter points
* - type: Integer
PARAMETER par_InCommunicationModule3_actionnode3_adopterPoints(set_InCommunicationModule3_actionnode3)

* - default: 2
* - description: todo
* - identifier: Interested points
* - type: Integer
PARAMETER par_InCommunicationModule3_actionnode3_interestedPoints(set_InCommunicationModule3_actionnode3)

* - default: 1
* - description: todo
* - identifier: Aware points
* - type: Integer
PARAMETER par_InCommunicationModule3_actionnode3_awarePoints(set_InCommunicationModule3_actionnode3)

* - default: 0
* - description: todo
* - identifier: Unknown üpoints
* - type: Integer
PARAMETER par_InCommunicationModule3_actionnode3_unknownPoints(set_InCommunicationModule3_actionnode3)

* - default: 1
* - domain: [0|1]
* - description: Diese Option aktiviert bzw. deaktiviert das relative agreement.
* - identifier: Relative agreement nutzen?
* - type: Boolean
PARAMETER par_InCommunicationModule3_actionnode3_raEnabled(set_InCommunicationModule3_actionnode3)

* - default: 1
* - domain: [0|1]
* - description: Ermöglicht das Speichern der Änderungen im realtive agreement. Der Logdateiname entspricht dem Modulnamen. Warnung: Diese Option kann zu großen Dateien führen!
* - identifier: Relative agreement loggen?
* - type: Boolean
PARAMETER par_InCommunicationModule3_actionnode3_raLoggingEnabled(set_InCommunicationModule3_actionnode3)

* - default: 1
* - domain: [0|1]
* - description: Falls ausgewählt, wird die Meinung der kommunizierenden Agenten geloggt.
* - identifier: Meinung loggen?
* - type: Boolean
PARAMETER par_InCommunicationModule3_actionnode3_raOpinionLogging(set_InCommunicationModule3_actionnode3)

* - default: 1
* - domain: [0|1]
* - description: Falls ausgewählt, wird die Unsicherheit der kommunizierenden Agenten geloggt.
* - identifier: Unsicherheit loggen?
* - type: Boolean
PARAMETER par_InCommunicationModule3_actionnode3_raUnceraintyLogging(set_InCommunicationModule3_actionnode3)

* - default: 1
* - domain: [0|1]
* - description: todo
* - identifier: Kopfzeile schreiben?
* - type: Boolean
PARAMETER par_InCommunicationModule3_actionnode3_raPrintHeader(set_InCommunicationModule3_actionnode3)

* - default: 0
* - domain: [0|1]
* - description: todo
* - identifier: Als csv speichern?
* - type: Boolean
PARAMETER par_InCommunicationModule3_actionnode3_raKeepCsv(set_InCommunicationModule3_actionnode3)

* - default: 1
* - domain: [0|1]
* - description: todo
* - identifier: Als xlsx speichern?
* - type: Boolean
PARAMETER par_InCommunicationModule3_actionnode3_raStoreXlsx(set_InCommunicationModule3_actionnode3)

* - default: 0.1
* - description: todo
* - identifier: Speed of convergence
* - type: Float
PARAMETER par_InCommunicationModule3_actionnode3_speedOfConvergence(set_InCommunicationModule3_actionnode3)

* - default: 0.1
* - description: todo
* - identifier: Attitude gap
* - type: Float
PARAMETER par_InCommunicationModule3_actionnode3_attitudeGap(set_InCommunicationModule3_actionnode3)

* - default: 0.5
* - description: todo
* - identifier: Wahrscheinllichkeit: Neutral
* - type: Float
PARAMETER par_InCommunicationModule3_actionnode3_chanceNeutral(set_InCommunicationModule3_actionnode3)

* - default: 0.25
* - description: todo
* - identifier: Wahrscheinllichkeit: Konvergenz
* - type: Float
PARAMETER par_InCommunicationModule3_actionnode3_chanceConvergence(set_InCommunicationModule3_actionnode3)

* - default: 0.25
* - description: todo
* - identifier: Wahrscheinllichkeit: Divergenz
* - type: Float
PARAMETER par_InCommunicationModule3_actionnode3_chanceDivergence(set_InCommunicationModule3_actionnode3)

* - description: todo
* - identifier: Unsicherheiten
* - type: Boolean
PARAMETER par_link_InCommunicationModule3_actionnode3_InUncertaintySupplier_uncertaintySuppliers(set_InCommunicationModule3_actionnode3,set_InUncertaintySupplier)

* - identifier: InConsumerAgentActionModule2
* - hidden: 1
* - type: String
SET set_InConsumerAgentActionModule2(set_InConsumerAgentModule2)

* - identifier: InIfElseActionModule3_actionnode3
* - type: String
SET set_InIfElseActionModule3_actionnode3(set_InConsumerAgentActionModule2)

* - description: todo
* - identifier: test
* - type: Boolean
PARAMETER par_link_InIfElseActionModule3_actionnode3_InConsumerAgentBoolModule2_testModule(set_InIfElseActionModule3_actionnode3,set_InConsumerAgentBoolModule2)

* - description: todo
* - identifier: onTrue
* - type: Boolean
PARAMETER par_link_InIfElseActionModule3_actionnode3_InConsumerAgentActionModule2_onTrueModule(set_InIfElseActionModule3_actionnode3,set_InConsumerAgentActionModule2)

* - description: todo
* - identifier: onFalse
* - type: Boolean
PARAMETER par_link_InIfElseActionModule3_actionnode3_InConsumerAgentActionModule2_onFalseModule(set_InIfElseActionModule3_actionnode3,set_InConsumerAgentActionModule2)

* - identifier: InNOPModule3_actionnode3
* - type: String
SET set_InNOPModule3_actionnode3(set_InConsumerAgentActionModule2)

* - default: 0.0
* - description: todo
* - identifier: ---
* - type: Float
PARAMETER par_InNOPModule3_actionnode3_placeholder(set_InNOPModule3_actionnode3)

* - identifier: InRewireModule3_actionnode3
* - type: String
SET set_InRewireModule3_actionnode3(set_InConsumerAgentActionModule2)

* - default: 0.0
* - description: todo
* - identifier: ---
* - type: Float
PARAMETER par_InRewireModule3_actionnode3_placeholder(set_InRewireModule3_actionnode3)

* - identifier: InConsumerAgentBoolModule2
* - hidden: 1
* - type: String
SET set_InConsumerAgentBoolModule2(set_InConsumerAgentModule2)

* - identifier: InThresholdReachedModule3_boolnode3
* - type: String
SET set_InThresholdReachedModule3_boolnode3(set_InConsumerAgentBoolModule2)

* - default: 5
* - description: todo
* - identifier: Priorität
* - type: Integer
PARAMETER par_InThresholdReachedModule3_boolnode3_priority(set_InThresholdReachedModule3_boolnode3)

* - description: todo
* - identifier: Vergleichswertmodul
* - type: Boolean
PARAMETER par_link_InThresholdReachedModule3_boolnode3_InConsumerAgentCalculationModule2_drawModule(set_InThresholdReachedModule3_boolnode3,set_InConsumerAgentCalculationModule2)

* - description: todo
* - identifier: Grenzwertmodul
* - type: Boolean
PARAMETER par_link_InThresholdReachedModule3_boolnode3_InConsumerAgentCalculationModule2_thresholdModule(set_InThresholdReachedModule3_boolnode3,set_InConsumerAgentCalculationModule2)

* - identifier: InBernoulliModule3_boolnode3
* - type: String
SET set_InBernoulliModule3_boolnode3(set_InConsumerAgentBoolModule2)

* - default: 5
* - description: todo
* - identifier: Priorität
* - type: Integer
PARAMETER par_InBernoulliModule3_boolnode3_priority(set_InBernoulliModule3_boolnode3)

* - description: todo
* - identifier: Eingabemodul
* - type: Boolean
PARAMETER par_link_InBernoulliModule3_boolnode3_InConsumerAgentCalculationModule2_inputModule(set_InBernoulliModule3_boolnode3,set_InConsumerAgentCalculationModule2)

* - identifier: InAddScalarModule3_calcnode3
* - type: String
SET set_InAddScalarModule3_calcnode3(set_InConsumerAgentCalculationModule2)

* - default: 0.0
* - description: todo
* - identifier: scalar
* - type: Float
PARAMETER par_InAddScalarModule3_calcnode3_scalar(set_InAddScalarModule3_calcnode3)

* - description: todo
* - identifier: Eingabemodul
* - type: Boolean
PARAMETER par_link_InAddScalarModule3_calcnode3_InConsumerAgentCalculationModule2_inputModule(set_InAddScalarModule3_calcnode3,set_InConsumerAgentCalculationModule2)

* - identifier: InConsumerAgentCalculationModule2
* - hidden: 1
* - type: String
SET set_InConsumerAgentCalculationModule2(set_InConsumerAgentModule2)

* - identifier: InLogisticModule3_calcnode3
* - type: String
SET set_InLogisticModule3_calcnode3(set_InConsumerAgentCalculationModule2)

* - default: 1.0
* - description: todo
* - identifier: L
* - type: Float
PARAMETER par_InLogisticModule3_calcnode3_valueL(set_InLogisticModule3_calcnode3)

* - default: 1.0
* - description: todo
* - identifier: k
* - type: Float
PARAMETER par_InLogisticModule3_calcnode3_valueK(set_InLogisticModule3_calcnode3)

* - description: todo
* - identifier: x Modul
* - type: Boolean
PARAMETER par_link_InLogisticModule3_calcnode3_InConsumerAgentCalculationModule2_xinputModule(set_InLogisticModule3_calcnode3,set_InConsumerAgentCalculationModule2)

* - description: todo
* - identifier: x0 Modul
* - type: Boolean
PARAMETER par_link_InLogisticModule3_calcnode3_InConsumerAgentCalculationModule2_x0inputModule(set_InLogisticModule3_calcnode3,set_InConsumerAgentCalculationModule2)

* - default: -1
* - description: todo
* - identifier: specialId
* - type: Integer
PARAMETER par_InLogisticModule3_calcnode3_specialId(set_InLogisticModule3_calcnode3)

* - identifier: InMulScalarModule3_calcnode3
* - type: String
SET set_InMulScalarModule3_calcnode3(set_InConsumerAgentCalculationModule2)

* - default: 1.0
* - description: todo
* - identifier: scalar
* - type: Float
PARAMETER par_InMulScalarModule3_calcnode3_scalar(set_InMulScalarModule3_calcnode3)

* - description: todo
* - identifier: Eingabemodul
* - type: Boolean
PARAMETER par_link_InMulScalarModule3_calcnode3_InConsumerAgentCalculationModule2_inputModule(set_InMulScalarModule3_calcnode3,set_InConsumerAgentCalculationModule2)

* - identifier: InProductModule3_calcnode3
* - type: String
SET set_InProductModule3_calcnode3(set_InConsumerAgentCalculationModule2)

* - description: todo
* - identifier: Eingabemodule
* - type: Boolean
PARAMETER par_link_InProductModule3_calcnode3_InConsumerAgentCalculationModule2_inputModule(set_InProductModule3_calcnode3,set_InConsumerAgentCalculationModule2)

* - identifier: InSumModule3_calcnode3
* - type: String
SET set_InSumModule3_calcnode3(set_InConsumerAgentCalculationModule2)

* - description: todo
* - identifier: Eingabemodule
* - type: Boolean
PARAMETER par_link_InSumModule3_calcnode3_InConsumerAgentCalculationModule2_inputModule(set_InSumModule3_calcnode3,set_InConsumerAgentCalculationModule2)

* - default: -1
* - description: todo
* - identifier: specialId
* - type: Integer
PARAMETER par_InSumModule3_calcnode3_specialId(set_InSumModule3_calcnode3)

* - identifier: InAnnualAvgAgentAssetNPVModule2_inputnode3
* - type: String
SET set_InAnnualAvgAgentAssetNPVModule2_inputnode3(set_InConsumerAgentInputModule2)

* - description: todo
* - identifier: pvFile
* - type: Boolean
PARAMETER par_link_InAnnualAvgAgentAssetNPVModule2_inputnode3_InPVFile_pvFile(set_InAnnualAvgAgentAssetNPVModule2_inputnode3,set_InPVFile)

* - identifier: InAnnualAvgAgentNPVModule2_inputnode3
* - type: String
SET set_InAnnualAvgAgentNPVModule2_inputnode3(set_InConsumerAgentInputModule2)

* - description: todo
* - identifier: pvFile
* - type: Boolean
PARAMETER par_link_InAnnualAvgAgentNPVModule2_inputnode3_InPVFile_pvFile(set_InAnnualAvgAgentNPVModule2_inputnode3,set_InPVFile)

* - identifier: InAnnualAvgAssetNPVModule2_inputnode3
* - type: String
SET set_InAnnualAvgAssetNPVModule2_inputnode3(set_InConsumerAgentInputModule2)

* - description: todo
* - identifier: pvFile
* - type: Boolean
PARAMETER par_link_InAnnualAvgAssetNPVModule2_inputnode3_InPVFile_pvFile(set_InAnnualAvgAssetNPVModule2_inputnode3,set_InPVFile)

* - identifier: InAnnualAvgExistingAssetNPVModule2_inputnode3
* - type: String
SET set_InAnnualAvgExistingAssetNPVModule2_inputnode3(set_InConsumerAgentInputModule2)

* - description: todo
* - identifier: pvFile
* - type: Boolean
PARAMETER par_link_InAnnualAvgExistingAssetNPVModule2_inputnode3_InPVFile_pvFile(set_InAnnualAvgExistingAssetNPVModule2_inputnode3,set_InPVFile)

* - identifier: InAnnualAvgNPVModule2_inputnode3
* - type: String
SET set_InAnnualAvgNPVModule2_inputnode3(set_InConsumerAgentInputModule2)

* - description: todo
* - identifier: pvFile
* - type: Boolean
PARAMETER par_link_InAnnualAvgNPVModule2_inputnode3_InPVFile_pvFile(set_InAnnualAvgNPVModule2_inputnode3,set_InPVFile)

* - identifier: InAssetNPVModule3_inputnode3
* - type: String
SET set_InAssetNPVModule3_inputnode3(set_InConsumerAgentInputModule2)

* - description: todo
* - identifier: pvFile
* - type: Boolean
PARAMETER par_link_InAssetNPVModule3_inputnode3_InPVFile_pvFile(set_InAssetNPVModule3_inputnode3,set_InPVFile)

* - identifier: InAttributeInputModule3_inputnode3
* - type: String
SET set_InAttributeInputModule3_inputnode3(set_InConsumerAgentInputModule2)

* - description: todo
* - identifier: attribute
* - type: Boolean
PARAMETER par_link_InAttributeInputModule3_inputnode3_InAttributeName_attribute(set_InAttributeInputModule3_inputnode3,set_InAttributeName)

* - default: -1
* - description: todo
* - identifier: specialId
* - type: Integer
PARAMETER par_InAttributeInputModule3_inputnode3_specialId(set_InAttributeInputModule3_inputnode3)

* - identifier: InAvgFinModule3_inputnode3
* - type: String
SET set_InAvgFinModule3_inputnode3(set_InConsumerAgentInputModule2)

* - default: 0.0
* - description: todo
* - identifier: ---
* - type: Float
PARAMETER par_InAvgFinModule3_inputnode3_placeholder(set_InAvgFinModule3_inputnode3)

* - identifier: InConsumerAgentInputModule2
* - hidden: 1
* - type: String
SET set_InConsumerAgentInputModule2(set_InConsumerAgentCalculationModule2)

* - identifier: InGlobalAvgAssetNPVModule3_inputnode3
* - type: String
SET set_InGlobalAvgAssetNPVModule3_inputnode3(set_InConsumerAgentInputModule2)

* - description: todo
* - identifier: pvFile
* - type: Boolean
PARAMETER par_link_InGlobalAvgAssetNPVModule3_inputnode3_InPVFile_pvFile(set_InGlobalAvgAssetNPVModule3_inputnode3,set_InPVFile)

* - identifier: InGlobalAvgExistingAssetNPVModule3_inputnode3
* - type: String
SET set_InGlobalAvgExistingAssetNPVModule3_inputnode3(set_InConsumerAgentInputModule2)

* - description: todo
* - identifier: pvFile
* - type: Boolean
PARAMETER par_link_InGlobalAvgExistingAssetNPVModule3_inputnode3_InPVFile_pvFile(set_InGlobalAvgExistingAssetNPVModule3_inputnode3,set_InPVFile)

* - identifier: InGlobalAvgNPVModule3_inputnode3
* - type: String
SET set_InGlobalAvgNPVModule3_inputnode3(set_InConsumerAgentInputModule2)

* - description: todo
* - identifier: pvFile
* - type: Boolean
PARAMETER par_link_InGlobalAvgNPVModule3_inputnode3_InPVFile_pvFile(set_InGlobalAvgNPVModule3_inputnode3,set_InPVFile)

* - identifier: InLocalShareOfAdopterModule3_inputnode3
* - type: String
SET set_InLocalShareOfAdopterModule3_inputnode3(set_InConsumerAgentInputModule2)

* - default: 0
* - description: todo
* - identifier: maxToStore
* - type: Integer
PARAMETER par_InLocalShareOfAdopterModule3_inputnode3_maxToStore(set_InLocalShareOfAdopterModule3_inputnode3)

* - description: todo
* - identifier: nodeFilterScheme
* - type: Boolean
PARAMETER par_link_InLocalShareOfAdopterModule3_inputnode3_InNodeDistanceFilterScheme_nodeFilterScheme(set_InLocalShareOfAdopterModule3_inputnode3,set_InNodeDistanceFilterScheme)

* - default: -1
* - description: todo
* - identifier: specialId
* - type: Integer
PARAMETER par_InLocalShareOfAdopterModule3_inputnode3_specialId(set_InLocalShareOfAdopterModule3_inputnode3)

* - identifier: InNaNModule3_inputnode3
* - type: String
SET set_InNaNModule3_inputnode3(set_InConsumerAgentInputModule2)

* - default: 0.0
* - description: todo
* - identifier: ---
* - type: Float
PARAMETER par_InNaNModule3_inputnode3_placeholder(set_InNaNModule3_inputnode3)

* - identifier: InNPVModule3_inputnode3
* - type: String
SET set_InNPVModule3_inputnode3(set_InConsumerAgentInputModule2)

* - description: todo
* - identifier: pvFile
* - type: Boolean
PARAMETER par_link_InNPVModule3_inputnode3_InPVFile_pvFile(set_InNPVModule3_inputnode3,set_InPVFile)

* - identifier: InSelectNPVModule3_inputnode3
* - type: String
SET set_InSelectNPVModule3_inputnode3(set_InConsumerAgentInputModule2)

* - default: 1
* - domain: [1,7]
* - description: Der zu nutzende NPV-Typ.
* - identifier: NPV-ID
* - type: Integer
PARAMETER par_InSelectNPVModule3_inputnode3_npvId(set_InSelectNPVModule3_inputnode3)

* - description: todo
* - identifier: pvFile
* - type: Boolean
PARAMETER par_link_InSelectNPVModule3_inputnode3_InPVFile_pvFile(set_InSelectNPVModule3_inputnode3,set_InPVFile)

* - fill: #CAFE12
* - color: #CAFE12
* - shape: flower
* - identifier: InSocialShareOfAdopterModule3_inputnode3
* - type: String
SET set_InSocialShareOfAdopterModule3_inputnode3(set_InConsumerAgentInputModule2)

* - default: 0.0
* - description: todo
* - identifier: ---
* - type: Float
PARAMETER par_InSocialShareOfAdopterModule3_inputnode3_placeholder(set_InSocialShareOfAdopterModule3_inputnode3)

* - default: -1
* - description: todo
* - identifier: specialId
* - type: Integer
PARAMETER par_InSocialShareOfAdopterModule3_inputnode3_specialId(set_InSocialShareOfAdopterModule3_inputnode3)

* - identifier: InValueModule3_inputnode3
* - type: String
SET set_InValueModule3_inputnode3(set_InConsumerAgentInputModule2)

* - default: 0.0
* - description: todo
* - identifier: Rückgabewert
* - type: Float
PARAMETER par_InValueModule3_inputnode3_value(set_InValueModule3_inputnode3)

* - identifier: InConsumerAgentCalculationLoggingModule2
* - hidden: 1
* - type: String
SET set_InConsumerAgentCalculationLoggingModule2(set_InConsumerAgentCalculationModule2)

* - identifier: InCsvValueLoggingModule3_calclognode3
* - type: String
SET set_InCsvValueLoggingModule3_calclognode3(set_InConsumerAgentCalculationLoggingModule2)

* - default: 1
* - domain: [0|1]
* - description: todo
* - identifier: Modul verwenden?
* - type: Boolean
PARAMETER par_InCsvValueLoggingModule3_calclognode3_enabled(set_InCsvValueLoggingModule3_calclognode3)

* - default: 0
* - domain: [0|1]
* - description: todo
* - identifier: Reevaluierungsaufrufe loggen?
* - type: Boolean
PARAMETER par_InCsvValueLoggingModule3_calclognode3_logReevaluatorCall(set_InCsvValueLoggingModule3_calclognode3)

* - default: 1
* - domain: [0|1]
* - description: todo
* - identifier: Normale Aufrufe loggen?
* - type: Boolean
PARAMETER par_InCsvValueLoggingModule3_calclognode3_logDefaultCall(set_InCsvValueLoggingModule3_calclognode3)

* - default: 1
* - domain: [0|1]
* - description: todo
* - identifier: Kopfzeile schreiben?
* - type: Boolean
PARAMETER par_InCsvValueLoggingModule3_calclognode3_printHeader(set_InCsvValueLoggingModule3_calclognode3)

* - default: 0
* - domain: [0|1]
* - description: todo
* - identifier: Als xlsx speichern?
* - type: Boolean
PARAMETER par_InCsvValueLoggingModule3_calclognode3_storeXlsx(set_InCsvValueLoggingModule3_calclognode3)

* - description: todo
* - identifier: Eingabemodul
* - type: Boolean
PARAMETER par_link_InCsvValueLoggingModule3_calclognode3_InConsumerAgentCalculationModule2_inputModule(set_InCsvValueLoggingModule3_calclognode3,set_InConsumerAgentCalculationModule2)

* - identifier: InSpecialUtilityCsvValueLoggingModule3_calcnode3
* - type: String
SET set_InSpecialUtilityCsvValueLoggingModule3_calcnode3(set_InConsumerAgentCalculationModule2)

* - description: todo
* - identifier: Utility-Modul
* - type: Boolean
PARAMETER par_link_InSpecialUtilityCsvValueLoggingModule3_calcnode3_InConsumerAgentCalculationModule2_utilityModule(set_InSpecialUtilityCsvValueLoggingModule3_calcnode3,set_InConsumerAgentCalculationModule2)

* - description: todo
* - identifier: utilityLogger
* - type: Boolean
PARAMETER par_link_InSpecialUtilityCsvValueLoggingModule3_calcnode3_InCsvValueLoggingModule3_calclognode3_utilityLogger(set_InSpecialUtilityCsvValueLoggingModule3_calcnode3,set_InCsvValueLoggingModule3_calclognode3)

* - description: todo
* - identifier: localShareLogger
* - type: Boolean
PARAMETER par_link_InSpecialUtilityCsvValueLoggingModule3_calcnode3_InCsvValueLoggingModule3_calclognode3_localShareLogger(set_InSpecialUtilityCsvValueLoggingModule3_calcnode3,set_InCsvValueLoggingModule3_calclognode3)

* - description: todo
* - identifier: socialShareLogger
* - type: Boolean
PARAMETER par_link_InSpecialUtilityCsvValueLoggingModule3_calcnode3_InCsvValueLoggingModule3_calclognode3_socialShareLogger(set_InSpecialUtilityCsvValueLoggingModule3_calcnode3,set_InCsvValueLoggingModule3_calclognode3)

* - description: todo
* - identifier: envLogger
* - type: Boolean
PARAMETER par_link_InSpecialUtilityCsvValueLoggingModule3_calcnode3_InCsvValueLoggingModule3_calclognode3_envLogger(set_InSpecialUtilityCsvValueLoggingModule3_calcnode3,set_InCsvValueLoggingModule3_calclognode3)

* - description: todo
* - identifier: novLogger
* - type: Boolean
PARAMETER par_link_InSpecialUtilityCsvValueLoggingModule3_calcnode3_InCsvValueLoggingModule3_calclognode3_novLogger(set_InSpecialUtilityCsvValueLoggingModule3_calcnode3,set_InCsvValueLoggingModule3_calclognode3)

* - description: todo
* - identifier: npvLogger
* - type: Boolean
PARAMETER par_link_InSpecialUtilityCsvValueLoggingModule3_calcnode3_InCsvValueLoggingModule3_calclognode3_npvLogger(set_InSpecialUtilityCsvValueLoggingModule3_calcnode3,set_InCsvValueLoggingModule3_calclognode3)

* - identifier: InConsumerAgentEvalModule2
* - hidden: 1
* - type: String
SET set_InConsumerAgentEvalModule2(set_InConsumerAgentModule2)

* - identifier: InRunUntilFailureModule3_evalnode3
* - type: String
SET set_InRunUntilFailureModule3_evalnode3(set_InConsumerAgentEvalModule2)

* - description: todo
* - identifier: Eingabemodul
* - type: Boolean
PARAMETER par_link_InRunUntilFailureModule3_evalnode3_InConsumerAgentModule2_inputModule(set_InRunUntilFailureModule3_evalnode3,set_InConsumerAgentModule2)

* - identifier: InConsumerAgentEvalRAModule2
* - hidden: 1
* - type: String
SET set_InConsumerAgentEvalRAModule2(set_InConsumerAgentModule2)

* - identifier: InDecisionMakingDeciderModule3_evalranode3
* - type: String
SET set_InDecisionMakingDeciderModule3_evalranode3(set_InConsumerAgentEvalRAModule2)

* - default: 0
* - domain: [0|1]
* - description: todo
* - identifier: forceEvaluation
* - type: Boolean
PARAMETER par_InDecisionMakingDeciderModule3_evalranode3_forceEvaluation(set_InDecisionMakingDeciderModule3_evalranode3)

* - description: todo
* - identifier: Finanz-Testmodul
* - type: Boolean
PARAMETER par_link_InDecisionMakingDeciderModule3_evalranode3_InConsumerAgentBoolModule2_finCheckModule(set_InDecisionMakingDeciderModule3_evalranode3,set_InConsumerAgentBoolModule2)

* - description: todo
* - identifier: Grenzwertmodul
* - type: Boolean
PARAMETER par_link_InDecisionMakingDeciderModule3_evalranode3_InConsumerAgentCalculationModule2_thresholdModule(set_InDecisionMakingDeciderModule3_evalranode3,set_InConsumerAgentCalculationModule2)

* - description: todo
* - identifier: Utilitymodul
* - type: Boolean
PARAMETER par_link_InDecisionMakingDeciderModule3_evalranode3_InConsumerAgentCalculationModule2_utilityModule(set_InDecisionMakingDeciderModule3_evalranode3,set_InConsumerAgentCalculationModule2)

* - identifier: InDoAdoptModule3_evalranode3
* - type: String
SET set_InDoAdoptModule3_evalranode3(set_InConsumerAgentEvalRAModule2)

* - description: todo
* - identifier: Eingabemodul
* - type: Boolean
PARAMETER par_link_InDoAdoptModule3_evalranode3_InConsumerAgentEvalRAModule2_inputModule(set_InDoAdoptModule3_evalranode3,set_InConsumerAgentEvalRAModule2)

* - identifier: InFeasibilityModule3_evalranode3
* - type: String
SET set_InFeasibilityModule3_evalranode3(set_InConsumerAgentEvalRAModule2)

* - description: todo
* - identifier: Eingabemodul
* - type: Boolean
PARAMETER par_link_InFeasibilityModule3_evalranode3_InConsumerAgentActionModule2_inputModule(set_InFeasibilityModule3_evalranode3,set_InConsumerAgentActionModule2)

* - identifier: InInitializationModule3_evalranode3
* - type: String
SET set_InInitializationModule3_evalranode3(set_InConsumerAgentEvalRAModule2)

* - default: 0.0
* - description: todo
* - identifier: ---
* - type: Float
PARAMETER par_InInitializationModule3_evalranode3_placeholder(set_InInitializationModule3_evalranode3)

* - identifier: InInterestModule3_evalranode3
* - type: String
SET set_InInterestModule3_evalranode3(set_InConsumerAgentEvalRAModule2)

* - description: todo
* - identifier: Eingabemodul
* - type: Boolean
PARAMETER par_link_InInterestModule3_evalranode3_InConsumerAgentActionModule2_inputModule(set_InInterestModule3_evalranode3,set_InConsumerAgentActionModule2)

* - identifier: InMainBranchingModule3_evalranode3
* - type: String
SET set_InMainBranchingModule3_evalranode3(set_InConsumerAgentEvalRAModule2)

* - description: todo
* - identifier: init
* - type: Boolean
PARAMETER par_link_InMainBranchingModule3_evalranode3_InConsumerAgentEvalRAModule2_initModule(set_InMainBranchingModule3_evalranode3,set_InConsumerAgentEvalRAModule2)

* - description: todo
* - identifier: awareness
* - type: Boolean
PARAMETER par_link_InMainBranchingModule3_evalranode3_InConsumerAgentEvalRAModule2_awarenessModule(set_InMainBranchingModule3_evalranode3,set_InConsumerAgentEvalRAModule2)

* - description: todo
* - identifier: feasiblity
* - type: Boolean
PARAMETER par_link_InMainBranchingModule3_evalranode3_InConsumerAgentEvalRAModule2_feasibilityModule(set_InMainBranchingModule3_evalranode3,set_InConsumerAgentEvalRAModule2)

* - description: todo
* - identifier: decision
* - type: Boolean
PARAMETER par_link_InMainBranchingModule3_evalranode3_InConsumerAgentEvalRAModule2_decisionModule(set_InMainBranchingModule3_evalranode3,set_InConsumerAgentEvalRAModule2)

* - description: todo
* - identifier: adopted
* - type: Boolean
PARAMETER par_link_InMainBranchingModule3_evalranode3_InConsumerAgentActionModule2_adoptedModule(set_InMainBranchingModule3_evalranode3,set_InConsumerAgentActionModule2)

* - description: todo
* - identifier: impeded
* - type: Boolean
PARAMETER par_link_InMainBranchingModule3_evalranode3_InConsumerAgentActionModule2_impededModule(set_InMainBranchingModule3_evalranode3,set_InConsumerAgentActionModule2)

* - identifier: InPhaseUpdateModule3_evalranode3
* - type: String
SET set_InPhaseUpdateModule3_evalranode3(set_InConsumerAgentEvalRAModule2)

* - description: todo
* - identifier: Eingabemodul
* - type: Boolean
PARAMETER par_link_InPhaseUpdateModule3_evalranode3_InConsumerAgentEvalRAModule2_inputModule(set_InPhaseUpdateModule3_evalranode3,set_InConsumerAgentEvalRAModule2)

* - identifier: InYearBasedAdoptionDeciderModule3_evalranode3
* - type: String
SET set_InYearBasedAdoptionDeciderModule3_evalranode3(set_InConsumerAgentEvalRAModule2)

* - default: 1
* - domain: [0|1]
* - description: todo
* - identifier: Modul verwenden?
* - type: Boolean
PARAMETER par_InYearBasedAdoptionDeciderModule3_evalranode3_enabled(set_InYearBasedAdoptionDeciderModule3_evalranode3)

* - default: 1.0
* - description: todo
* - identifier: base
* - type: Float
PARAMETER par_InYearBasedAdoptionDeciderModule3_evalranode3_base(set_InYearBasedAdoptionDeciderModule3_evalranode3)

* - default: 1.0
* - description: todo
* - identifier: factor
* - type: Float
PARAMETER par_InYearBasedAdoptionDeciderModule3_evalranode3_factor(set_InYearBasedAdoptionDeciderModule3_evalranode3)

* - description: todo
* - identifier: Eingabemodul
* - type: Boolean
PARAMETER par_link_InYearBasedAdoptionDeciderModule3_evalranode3_InConsumerAgentEvalRAModule2_inputModule(set_InYearBasedAdoptionDeciderModule3_evalranode3,set_InConsumerAgentEvalRAModule2)

* - identifier: InConsumerAgentReevaluationModule2
* - hidden: 1
* - type: String
SET set_InConsumerAgentReevaluationModule2(set_InModule2)

* - identifier: InReevaluatorModule3_reevalnode3
* - type: String
SET set_InReevaluatorModule3_reevalnode3(set_InConsumerAgentReevaluationModule2)

* - description: todo
* - identifier: Eingabemodule
* - type: Boolean
PARAMETER par_link_InReevaluatorModule3_reevalnode3_InConsumerAgentModule2_inputModule(set_InReevaluatorModule3_reevalnode3,set_InConsumerAgentModule2)

* - identifier: InConsumerAgentEvalRALoggingModule2
* - hidden: 1
* - type: String
SET set_InConsumerAgentEvalRALoggingModule2(set_InConsumerAgentEvalRAModule2)

* - identifier: InPhaseLoggingModule3_evalranode3
* - type: String
SET set_InPhaseLoggingModule3_evalranode3(set_InConsumerAgentEvalRALoggingModule2)

* - default: 1
* - domain: [0|1]
* - description: todo
* - identifier: Modul verwenden?
* - type: Boolean
PARAMETER par_InPhaseLoggingModule3_evalranode3_enabled(set_InPhaseLoggingModule3_evalranode3)

* - description: todo
* - identifier: Eingabemodul
* - type: Boolean
PARAMETER par_link_InPhaseLoggingModule3_evalranode3_InConsumerAgentEvalRAModule2_inputModule(set_InPhaseLoggingModule3_evalranode3,set_InConsumerAgentEvalRAModule2)

* - identifier: InAnnualInterestLogger
* - type: String
SET set_InAnnualInterestLogger(set_InReevaluator2)

* - default: 0.0
* - description: todo
* - identifier: ---
* - type: Float
PARAMETER par_InAnnualInterestLogger_placeholder(set_InAnnualInterestLogger)

* - identifier: InConstructionRenovationUpdater
* - type: String
SET set_InConstructionRenovationUpdater(set_InReevaluator2)

* - default: 0.0
* - description: todo
* - identifier: ---
* - type: Float
PARAMETER par_InConstructionRenovationUpdater_placeholder(set_InConstructionRenovationUpdater)

* - identifier: InDecisionMakingReevaluator
* - type: String
SET set_InDecisionMakingReevaluator(set_InReevaluator2)

* - description: todo
* - identifier: Eingabemodule
* - type: Boolean
PARAMETER par_link_InDecisionMakingReevaluator_InConsumerAgentModule2_modules(set_InDecisionMakingReevaluator,set_InConsumerAgentModule2)

* - identifier: InImpededResetter
* - type: String
SET set_InImpededResetter(set_InReevaluator2)

* - default: 0.0
* - description: todo
* - identifier: ---
* - type: Float
PARAMETER par_InImpededResetter_placeholder(set_InImpededResetter)

* - identifier: InLinearePercentageAgentAttributeUpdater
* - type: String
SET set_InLinearePercentageAgentAttributeUpdater(set_InReevaluator2)

* - default: 5
* - description: todo
* - identifier: Priorität
* - type: Integer
PARAMETER par_InLinearePercentageAgentAttributeUpdater_priority(set_InLinearePercentageAgentAttributeUpdater)

* - description: todo
* - identifier: scaler
* - type: Boolean
PARAMETER par_link_InLinearePercentageAgentAttributeUpdater_InLinearePercentageAgentAttributeScaler_scaler(set_InLinearePercentageAgentAttributeUpdater,set_InLinearePercentageAgentAttributeScaler)

* - identifier: InMultiReevaluator
* - type: String
SET set_InMultiReevaluator(set_InReevaluator2)

* - description: todo
* - identifier: Eingabemodule
* - type: Boolean
PARAMETER par_link_InMultiReevaluator_InConsumerAgentReevaluationModule2_modules(set_InMultiReevaluator,set_InConsumerAgentReevaluationModule2)

* - identifier: InUncertaintyReevaluator
* - type: String
SET set_InUncertaintyReevaluator(set_InReevaluator2)

* - default: 5
* - description: todo
* - identifier: Priorität
* - type: Integer
PARAMETER par_InUncertaintyReevaluator_priorty(set_InUncertaintyReevaluator)

* - description: todo
* - identifier: Unsicherheiten:
* - type: Boolean
PARAMETER par_link_InUncertaintyReevaluator_InUncertaintySupplier_uncertaintySuppliers(set_InUncertaintyReevaluator,set_InUncertaintySupplier)

* - identifier: InReevaluator2
* - hidden: 1
* - type: String
SET set_InReevaluator2(*)

* - identifier: InAgentAttributeScaler
* - type: String
SET set_InAgentAttributeScaler(set_InInitializationHandler)

* - default: 5
* - description: todo
* - identifier: Priorität
* - type: Integer
PARAMETER par_InAgentAttributeScaler_priority(set_InAgentAttributeScaler)

* - description: todo
* - identifier: Attribut
* - type: Boolean
PARAMETER par_link_InAgentAttributeScaler_InAttributeName_attribute(set_InAgentAttributeScaler,set_InAttributeName)

* - identifier: InInitializationHandler
* - hidden: 1
* - type: String
SET set_InInitializationHandler(*)

* - identifier: InLinearePercentageAgentAttributeScaler
* - type: String
SET set_InLinearePercentageAgentAttributeScaler(set_InInitializationHandler)

* - default: 5
* - description: todo
* - identifier: Priorität
* - type: Integer
PARAMETER par_InLinearePercentageAgentAttributeScaler_priority(set_InLinearePercentageAgentAttributeScaler)

* - default: 1.0
* - description: todo
* - identifier: m
* - type: Float
PARAMETER par_InLinearePercentageAgentAttributeScaler_mValue(set_InLinearePercentageAgentAttributeScaler)

* - default: 0.0
* - description: todo
* - identifier: n
* - type: Float
PARAMETER par_InLinearePercentageAgentAttributeScaler_nValue(set_InLinearePercentageAgentAttributeScaler)

* - description: todo
* - identifier: Attribut
* - type: Boolean
PARAMETER par_link_InLinearePercentageAgentAttributeScaler_InAttributeName_attribute(set_InLinearePercentageAgentAttributeScaler,set_InAttributeName)

* - identifier: InUncertaintySupplierInitializer
* - type: String
SET set_InUncertaintySupplierInitializer(set_InInitializationHandler)

* - default: 5
* - description: todo
* - identifier: Priorität
* - type: Integer
PARAMETER par_InUncertaintySupplierInitializer_priority(set_InUncertaintySupplierInitializer)

* - description: todo
* - identifier: Unsicherheiten
* - type: Boolean
PARAMETER par_link_InUncertaintySupplierInitializer_InUncertaintySupplier_uncertaintySuppliers(set_InUncertaintySupplierInitializer,set_InUncertaintySupplier)

* - default: 0
* - domain: [0|1]
* - description: Aktiviert bzw. deaktiviert die Nutzung der hier definierten Konstruktionsraten im Modell.
* - identifier: Werte im Modell verwenden?
* - type: Boolean
SCALAR sca_InSpecialPVactInput_useConstRates

* - default: 0.0
* - description: Stellt die Konstruktionsrate für das Jahr 2008 ein.
* - identifier: Jahr 2008
* - type: Float
SCALAR sca_InSpecialPVactInput_const2008

* - default: 0.0
* - description: Stellt die Konstruktionsrate für das Jahr 2009 ein.
* - identifier: Jahr 2009
* - type: Float
SCALAR sca_InSpecialPVactInput_const2009

* - default: 0.0
* - description: Stellt die Konstruktionsrate für das Jahr 2010 ein.
* - identifier: Jahr 2010
* - type: Float
SCALAR sca_InSpecialPVactInput_const2010

* - default: 0.0
* - description: Stellt die Konstruktionsrate für das Jahr 2011 ein.
* - identifier: Jahr 2011
* - type: Float
SCALAR sca_InSpecialPVactInput_const2011

* - default: 0.0
* - description: Stellt die Konstruktionsrate für das Jahr 2012 ein.
* - identifier: Jahr 2012
* - type: Float
SCALAR sca_InSpecialPVactInput_const2012

* - default: 0.0
* - description: Stellt die Konstruktionsrate für das Jahr 2013 ein.
* - identifier: Jahr 2013
* - type: Float
SCALAR sca_InSpecialPVactInput_const2013

* - default: 0.0
* - description: Stellt die Konstruktionsrate für das Jahr 2014 ein.
* - identifier: Jahr 2014
* - type: Float
SCALAR sca_InSpecialPVactInput_const2014

* - default: 0.0
* - description: Stellt die Konstruktionsrate für das Jahr 2015 ein.
* - identifier: Jahr 2015
* - type: Float
SCALAR sca_InSpecialPVactInput_const2015

* - default: 0.0
* - description: Stellt die Konstruktionsrate für das Jahr 2016 ein.
* - identifier: Jahr 2016
* - type: Float
SCALAR sca_InSpecialPVactInput_const2016

* - default: 0.0
* - description: Stellt die Konstruktionsrate für das Jahr 2017 ein.
* - identifier: Jahr 2017
* - type: Float
SCALAR sca_InSpecialPVactInput_const2017

* - default: 0.0
* - description: Stellt die Konstruktionsrate für das Jahr 2018 ein.
* - identifier: Jahr 2018
* - type: Float
SCALAR sca_InSpecialPVactInput_const2018

* - default: 0.0
* - description: Stellt die Konstruktionsrate für das Jahr 2019 ein.
* - identifier: Jahr 2019
* - type: Float
SCALAR sca_InSpecialPVactInput_const2019

* - default: 0.0
* - description: Stellt die Konstruktionsrate für das Jahr 2020 ein.
* - identifier: Jahr 2020
* - type: Float
SCALAR sca_InSpecialPVactInput_const2020

* - default: 0.0
* - description: Stellt die Renovierungsrate für das Jahr 2020 ein.
* - identifier: Jahr 2021
* - type: Float
SCALAR sca_InSpecialPVactInput_const2021

* - default: 0.0
* - description: Stellt die Renovierungsrate für das Jahr 2020 ein.
* - identifier: Jahr 2022
* - type: Float
SCALAR sca_InSpecialPVactInput_const2022

* - default: 0.0
* - description: Stellt die Renovierungsrate für das Jahr 2020 ein.
* - identifier: Jahr 2023
* - type: Float
SCALAR sca_InSpecialPVactInput_const2023

* - default: 0.0
* - description: Stellt die Renovierungsrate für das Jahr 2020 ein.
* - identifier: Jahr 2024
* - type: Float
SCALAR sca_InSpecialPVactInput_const2024

* - default: 0.0
* - description: Stellt die Renovierungsrate für das Jahr 2020 ein.
* - identifier: Jahr 2025
* - type: Float
SCALAR sca_InSpecialPVactInput_const2025

* - default: 0.0
* - description: Stellt die Renovierungsrate für das Jahr 2020 ein.
* - identifier: Jahr 2026
* - type: Float
SCALAR sca_InSpecialPVactInput_const2026

* - default: 0.0
* - description: Stellt die Renovierungsrate für das Jahr 2020 ein.
* - identifier: Jahr 2027
* - type: Float
SCALAR sca_InSpecialPVactInput_const2027

* - default: 0.0
* - description: Stellt die Renovierungsrate für das Jahr 2020 ein.
* - identifier: Jahr 2028
* - type: Float
SCALAR sca_InSpecialPVactInput_const2028

* - default: 0.0
* - description: Stellt die Renovierungsrate für das Jahr 2020 ein.
* - identifier: Jahr 2029
* - type: Float
SCALAR sca_InSpecialPVactInput_const2029

* - default: 0.0
* - description: Stellt die Renovierungsrate für das Jahr 2020 ein.
* - identifier: Jahr 2030
* - type: Float
SCALAR sca_InSpecialPVactInput_const2030

* - default: 0.0
* - description: Stellt die Renovierungsrate für das Jahr 2020 ein.
* - identifier: Jahr 2031
* - type: Float
SCALAR sca_InSpecialPVactInput_const2031

* - default: 0.0
* - description: Stellt die Renovierungsrate für das Jahr 2020 ein.
* - identifier: Jahr 2032
* - type: Float
SCALAR sca_InSpecialPVactInput_const2032

* - default: 0.0
* - description: Stellt die Renovierungsrate für das Jahr 2020 ein.
* - identifier: Jahr 2033
* - type: Float
SCALAR sca_InSpecialPVactInput_const2033

* - default: 0.0
* - description: Stellt die Renovierungsrate für das Jahr 2020 ein.
* - identifier: Jahr 2034
* - type: Float
SCALAR sca_InSpecialPVactInput_const2034

* - default: 0.0
* - description: Stellt die Renovierungsrate für das Jahr 2020 ein.
* - identifier: Jahr 2035
* - type: Float
SCALAR sca_InSpecialPVactInput_const2035

* - default: 0.0
* - description: Stellt die Renovierungsrate für das Jahr 2020 ein.
* - identifier: Jahr 2036
* - type: Float
SCALAR sca_InSpecialPVactInput_const2036

* - default: 0.0
* - description: Stellt die Renovierungsrate für das Jahr 2020 ein.
* - identifier: Jahr 2037
* - type: Float
SCALAR sca_InSpecialPVactInput_const2037

* - default: 0.0
* - description: Stellt die Renovierungsrate für das Jahr 2020 ein.
* - identifier: Jahr 2038
* - type: Float
SCALAR sca_InSpecialPVactInput_const2038

* - default: 0.0
* - description: Stellt die Renovierungsrate für das Jahr 2020 ein.
* - identifier: Jahr 2039
* - type: Float
SCALAR sca_InSpecialPVactInput_const2039

* - default: 0.0
* - description: Stellt die Renovierungsrate für das Jahr 2020 ein.
* - identifier: Jahr 2040
* - type: Float
SCALAR sca_InSpecialPVactInput_const2040

* - default: 0
* - domain: [0|1]
* - description: Aktiviert bzw. deaktiviert die Nutzung der hier definierten Renovierungsraten im Modell.
* - identifier: Werte im Modell verwenden?
* - type: Boolean
SCALAR sca_InSpecialPVactInput_useRenoRates

* - default: 0.0
* - description: Stellt die Renovierungsrate für das Jahr 2008 ein.
* - identifier: Jahr 2008
* - type: Float
SCALAR sca_InSpecialPVactInput_reno2008

* - default: 0.0
* - description: Stellt die Renovierungsrate für das Jahr 2009 ein.
* - identifier: Jahr 2009
* - type: Float
SCALAR sca_InSpecialPVactInput_reno2009

* - default: 0.0
* - description: Stellt die Renovierungsrate für das Jahr 2010 ein.
* - identifier: Jahr 2010
* - type: Float
SCALAR sca_InSpecialPVactInput_reno2010

* - default: 0.0
* - description: Stellt die Renovierungsrate für das Jahr 2011 ein.
* - identifier: Jahr 2011
* - type: Float
SCALAR sca_InSpecialPVactInput_reno2011

* - default: 0.0
* - description: Stellt die Renovierungsrate für das Jahr 2012 ein.
* - identifier: Jahr 2012
* - type: Float
SCALAR sca_InSpecialPVactInput_reno2012

* - default: 0.0
* - description: Stellt die Renovierungsrate für das Jahr 2013 ein.
* - identifier: Jahr 2013
* - type: Float
SCALAR sca_InSpecialPVactInput_reno2013

* - default: 0.0
* - description: Stellt die Renovierungsrate für das Jahr 2014 ein.
* - identifier: Jahr 2014
* - type: Float
SCALAR sca_InSpecialPVactInput_reno2014

* - default: 0.0
* - description: Stellt die Renovierungsrate für das Jahr 2015 ein.
* - identifier: Jahr 2015
* - type: Float
SCALAR sca_InSpecialPVactInput_reno2015

* - default: 0.0
* - description: Stellt die Renovierungsrate für das Jahr 2016 ein.
* - identifier: Jahr 2016
* - type: Float
SCALAR sca_InSpecialPVactInput_reno2016

* - default: 0.0
* - description: Stellt die Renovierungsrate für das Jahr 2017 ein.
* - identifier: Jahr 2017
* - type: Float
SCALAR sca_InSpecialPVactInput_reno2017

* - default: 0.0
* - description: Stellt die Renovierungsrate für das Jahr 2018 ein.
* - identifier: Jahr 2018
* - type: Float
SCALAR sca_InSpecialPVactInput_reno2018

* - default: 0.0
* - description: Stellt die Renovierungsrate für das Jahr 2019 ein.
* - identifier: Jahr 2019
* - type: Float
SCALAR sca_InSpecialPVactInput_reno2019

* - default: 0.0
* - description: Stellt die Renovierungsrate für das Jahr 2020 ein.
* - identifier: Jahr 2020
* - type: Float
SCALAR sca_InSpecialPVactInput_reno2020

* - default: 0.0
* - description: Stellt die Renovierungsrate für das Jahr 2020 ein.
* - identifier: Jahr 2021
* - type: Float
SCALAR sca_InSpecialPVactInput_reno2021

* - default: 0.0
* - description: Stellt die Renovierungsrate für das Jahr 2020 ein.
* - identifier: Jahr 2022
* - type: Float
SCALAR sca_InSpecialPVactInput_reno2022

* - default: 0.0
* - description: Stellt die Renovierungsrate für das Jahr 2020 ein.
* - identifier: Jahr 2023
* - type: Float
SCALAR sca_InSpecialPVactInput_reno2023

* - default: 0.0
* - description: Stellt die Renovierungsrate für das Jahr 2020 ein.
* - identifier: Jahr 2024
* - type: Float
SCALAR sca_InSpecialPVactInput_reno2024

* - default: 0.0
* - description: Stellt die Renovierungsrate für das Jahr 2020 ein.
* - identifier: Jahr 2025
* - type: Float
SCALAR sca_InSpecialPVactInput_reno2025

* - default: 0.0
* - description: Stellt die Renovierungsrate für das Jahr 2020 ein.
* - identifier: Jahr 2026
* - type: Float
SCALAR sca_InSpecialPVactInput_reno2026

* - default: 0.0
* - description: Stellt die Renovierungsrate für das Jahr 2020 ein.
* - identifier: Jahr 2027
* - type: Float
SCALAR sca_InSpecialPVactInput_reno2027

* - default: 0.0
* - description: Stellt die Renovierungsrate für das Jahr 2020 ein.
* - identifier: Jahr 2028
* - type: Float
SCALAR sca_InSpecialPVactInput_reno2028

* - default: 0.0
* - description: Stellt die Renovierungsrate für das Jahr 2020 ein.
* - identifier: Jahr 2029
* - type: Float
SCALAR sca_InSpecialPVactInput_reno2029

* - default: 0.0
* - description: Stellt die Renovierungsrate für das Jahr 2020 ein.
* - identifier: Jahr 2030
* - type: Float
SCALAR sca_InSpecialPVactInput_reno2030

* - default: 0.0
* - description: Stellt die Renovierungsrate für das Jahr 2020 ein.
* - identifier: Jahr 2031
* - type: Float
SCALAR sca_InSpecialPVactInput_reno2031

* - default: 0.0
* - description: Stellt die Renovierungsrate für das Jahr 2020 ein.
* - identifier: Jahr 2032
* - type: Float
SCALAR sca_InSpecialPVactInput_reno2032

* - default: 0.0
* - description: Stellt die Renovierungsrate für das Jahr 2020 ein.
* - identifier: Jahr 2033
* - type: Float
SCALAR sca_InSpecialPVactInput_reno2033

* - default: 0.0
* - description: Stellt die Renovierungsrate für das Jahr 2020 ein.
* - identifier: Jahr 2034
* - type: Float
SCALAR sca_InSpecialPVactInput_reno2034

* - default: 0.0
* - description: Stellt die Renovierungsrate für das Jahr 2020 ein.
* - identifier: Jahr 2035
* - type: Float
SCALAR sca_InSpecialPVactInput_reno2035

* - default: 0.0
* - description: Stellt die Renovierungsrate für das Jahr 2020 ein.
* - identifier: Jahr 2036
* - type: Float
SCALAR sca_InSpecialPVactInput_reno2036

* - default: 0.0
* - description: Stellt die Renovierungsrate für das Jahr 2020 ein.
* - identifier: Jahr 2037
* - type: Float
SCALAR sca_InSpecialPVactInput_reno2037

* - default: 0.0
* - description: Stellt die Renovierungsrate für das Jahr 2020 ein.
* - identifier: Jahr 2038
* - type: Float
SCALAR sca_InSpecialPVactInput_reno2038

* - default: 0.0
* - description: Stellt die Renovierungsrate für das Jahr 2020 ein.
* - identifier: Jahr 2039
* - type: Float
SCALAR sca_InSpecialPVactInput_reno2039

* - default: 0.0
* - description: Stellt die Renovierungsrate für das Jahr 2020 ein.
* - identifier: Jahr 2040
* - type: Float
SCALAR sca_InSpecialPVactInput_reno2040

* - identifier: InBasicProductGroup
* - type: String
SET set_InBasicProductGroup(set_InProductGroup)

* - description: Bestimmt die Attribute für diese Gruppe.
* - identifier: Attribute
* - type: Boolean
PARAMETER par_link_InBasicProductGroup_InDependentProductGroupAttribute_pgAttributes(set_InBasicProductGroup,set_InDependentProductGroupAttribute)

* - identifier: InBasicProductGroupAttribute
* - type: String
SET set_InBasicProductGroupAttribute(set_InDependentProductGroupAttribute)

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
SET set_InDependentProductGroupAttribute(set_InProductGroupAttribute)

* - identifier: InFixProduct
* - type: String
SET set_InFixProduct(*)

* - description: Legt die Produktgruppe fest, zu der dieses Produkt gehören soll.
* - identifier: Produktgruppe
* - type: Boolean
PARAMETER par_link_InFixProduct_InProductGroup_refPG(set_InFixProduct,set_InProductGroup)

* - description: Bestimmt die Attribute für dieses Fixprodukt.
* - identifier: Attribute
* - type: Boolean
PARAMETER par_link_InFixProduct_InFixProductAttribute_fixPAttrs(set_InFixProduct,set_InFixProductAttribute)

* - identifier: InFixProductAttribute
* - type: String
SET set_InFixProductAttribute(*)

* - default: 0.0
* - description: Bestimmt die Wert für dieses Attribut.
* - identifier: Wert
* - type: Float
PARAMETER par_InFixProductAttribute_fixPAvalue(set_InFixProductAttribute)

* - description: Legt das Produktgruppenattribut fest, zu dem dieses Produkt gehören soll.
* - identifier: Produktgruppenattribut
* - type: Boolean
PARAMETER par_link_InFixProductAttribute_InProductGroupAttribute_refPGA(set_InFixProductAttribute,set_InProductGroupAttribute)

* - identifier: InFixProductFindingScheme
* - type: String
SET set_InFixProductFindingScheme(set_InProductFindingScheme)

* - description: Legt das Fixprodukt fest, welches von diesem Schema verwendet werden soll.
* - identifier: Fixprodukt
* - type: Boolean
PARAMETER par_link_InFixProductFindingScheme_InFixProduct_refFixProduct(set_InFixProductFindingScheme,set_InFixProduct)

* - identifier: InIndependentProductGroupAttribute
* - hidden: 1
* - type: String
SET set_InIndependentProductGroupAttribute(set_InProductGroupAttribute)

* - identifier: InNameSplitProductGroupAttribute
* - type: String
SET set_InNameSplitProductGroupAttribute(set_InIndependentProductGroupAttribute)

* - description: Legt die Verteilungsfunktion fest, auf deren Basis die Attributwerte gezogen werden.
* - identifier: Verteilungsfunktion
* - type: Boolean
PARAMETER par_link_InNameSplitProductGroupAttribute_InUnivariateDoubleDistribution_dist(set_InNameSplitProductGroupAttribute,set_InUnivariateDoubleDistribution)

* - identifier: InProductFindingScheme
* - hidden: 1
* - type: String
SET set_InProductFindingScheme(*)

* - identifier: InProductGroup
* - hidden: 1
* - type: String
SET set_InProductGroup(*)

* - identifier: InProductGroupAttribute
* - hidden: 1
* - type: String
SET set_InProductGroupAttribute(*)

* - identifier: InNewProductHandler
* - hidden: 1
* - type: String
SET set_InNewProductHandler(*)

* - identifier: InPVactAttributeBasedInitialAdoption
* - type: String
SET set_InPVactAttributeBasedInitialAdoption(set_InNewProductHandler)

* - default: 0
* - description: todo
* - identifier: Priorität
* - type: Integer
PARAMETER par_InPVactAttributeBasedInitialAdoption_priority(set_InPVactAttributeBasedInitialAdoption)

* - identifier: InPVactDefaultAwarenessHandler
* - type: String
SET set_InPVactDefaultAwarenessHandler(set_InNewProductHandler)

* - default: 0
* - description: todo
* - identifier: Priorität
* - type: Integer
PARAMETER par_InPVactDefaultAwarenessHandler_priority(set_InPVactDefaultAwarenessHandler)

* - identifier: InPVactDefaultAwarenessInterestHandler
* - type: String
SET set_InPVactDefaultAwarenessInterestHandler(set_InNewProductHandler)

* - default: 0
* - description: todo
* - identifier: Priorität
* - type: Integer
PARAMETER par_InPVactDefaultAwarenessInterestHandler_priority(set_InPVactDefaultAwarenessInterestHandler)

* - identifier: InPVactDefaultInterestHandler
* - type: String
SET set_InPVactDefaultInterestHandler(set_InNewProductHandler)

* - default: 0
* - description: todo
* - identifier: Priorität
* - type: Integer
PARAMETER par_InPVactDefaultInterestHandler_priority(set_InPVactDefaultInterestHandler)

* - identifier: InPVactDependentInterestScaler
* - type: String
SET set_InPVactDependentInterestScaler(set_InNewProductHandler)

* - default: 0
* - description: todo
* - identifier: Priorität
* - type: Integer
PARAMETER par_InPVactDependentInterestScaler_priority(set_InPVactDependentInterestScaler)

* - description: Diese Funktion
* - identifier: Verteilungsfunktion
* - type: Boolean
PARAMETER par_link_InPVactDependentInterestScaler_InUnivariateDoubleDistribution_distribution(set_InPVactDependentInterestScaler,set_InUnivariateDoubleDistribution)

* - identifier: InPVactFileBasedConsumerGroupBasedInitialAdoptionWithRealData
* - type: String
SET set_InPVactFileBasedConsumerGroupBasedInitialAdoptionWithRealData(set_InNewProductHandler)

* - default: 0
* - description: todo
* - identifier: Priorität
* - type: Integer
PARAMETER par_InPVactFileBasedConsumerGroupBasedInitialAdoptionWithRealData_priority(set_InPVactFileBasedConsumerGroupBasedInitialAdoptionWithRealData)

* - description: Bestimmt die Datei mit den realen Adoptionsdaten.
* - identifier: Eingabedatei
* - type: Boolean
PARAMETER par_link_InPVactFileBasedConsumerGroupBasedInitialAdoptionWithRealData_InRealAdoptionDataFile_file(set_InPVactFileBasedConsumerGroupBasedInitialAdoptionWithRealData,set_InRealAdoptionDataFile)

* - identifier: InPVactFileBasedWeightedConsumerGroupBasedInitialAdoptionWithRealData
* - type: String
SET set_InPVactFileBasedWeightedConsumerGroupBasedInitialAdoptionWithRealData(set_InNewProductHandler)

* - default: 0
* - domain: [0|1]
* - description: Ermöglicht das automatische Skalieren der Werte. Die Skalierung selber orientiert sich dabei an der Agentenpopulation.
* - identifier: Automatische Skalierung
* - type: Boolean
PARAMETER par_InPVactFileBasedWeightedConsumerGroupBasedInitialAdoptionWithRealData_scale(set_InPVactFileBasedWeightedConsumerGroupBasedInitialAdoptionWithRealData)

* - default: 0
* - domain: [0|1]
* - description: Falls aufgrund arithmetisch Fehler initiale Adopter fehlen, werden diese nachträglich bestimmt.
* - identifier: Automatische Fehlerkorrektur
* - type: Boolean
PARAMETER par_InPVactFileBasedWeightedConsumerGroupBasedInitialAdoptionWithRealData_fixError(set_InPVactFileBasedWeightedConsumerGroupBasedInitialAdoptionWithRealData)

* - default: 0
* - description: todo
* - identifier: Priorität
* - type: Integer
PARAMETER par_InPVactFileBasedWeightedConsumerGroupBasedInitialAdoptionWithRealData_priority(set_InPVactFileBasedWeightedConsumerGroupBasedInitialAdoptionWithRealData)

* - description: Bestimmt die Datei mit den realen Adoptionsdaten.
* - identifier: Eingabedatei
* - type: Boolean
PARAMETER par_link_InPVactFileBasedWeightedConsumerGroupBasedInitialAdoptionWithRealData_InRealAdoptionDataFile_file(set_InPVactFileBasedWeightedConsumerGroupBasedInitialAdoptionWithRealData,set_InRealAdoptionDataFile)

* - identifier: InFileBasedPVactMilieuSupplier
* - type: String
SET set_InFileBasedPVactMilieuSupplier(set_InSpatialDistributionWithCollection)

* - description: Legt die Datei fest, aus der die räumlichen Informationen gelesen werden sollen.
* - identifier: Tabellarische Datei
* - type: Boolean
PARAMETER par_link_InFileBasedPVactMilieuSupplier_InSpatialTableFile_file(set_InFileBasedPVactMilieuSupplier,set_InSpatialTableFile)

* - identifier: InFileBasedSelectGroupSpatialInformationSupplier
* - type: String
SET set_InFileBasedSelectGroupSpatialInformationSupplier(set_InSpatialDistributionWithCollection)

* - description: Bestimmt den Schlüssel für die Auswahl der X-Position in der tabellarischen Datei (Spaltenname).
* - identifier: X-Schlüssel
* - type: Boolean
PARAMETER par_link_InFileBasedSelectGroupSpatialInformationSupplier_InAttributeName_xPositionKey(set_InFileBasedSelectGroupSpatialInformationSupplier,set_InAttributeName)

* - description: Bestimmt den Schlüssel für die Auswahl der Y-Position in der tabellarischen Datei (Spaltenname).
* - identifier: Y-Schlüssel
* - type: Boolean
PARAMETER par_link_InFileBasedSelectGroupSpatialInformationSupplier_InAttributeName_yPositionKey(set_InFileBasedSelectGroupSpatialInformationSupplier,set_InAttributeName)

* - description: Bestimmt den Schlüssel für die Auswahl der ID in der tabellarischen Datei (Spaltenname).
* - identifier: ID-Schlüssel
* - type: Boolean
PARAMETER par_link_InFileBasedSelectGroupSpatialInformationSupplier_InAttributeName_idKey(set_InFileBasedSelectGroupSpatialInformationSupplier,set_InAttributeName)

* - description: Legt die Datei fest, aus der die räumlichen Informationen gelesen werden sollen.
* - identifier: Tabellarische Datei
* - type: Boolean
PARAMETER par_link_InFileBasedSelectGroupSpatialInformationSupplier_InSpatialTableFile_file(set_InFileBasedSelectGroupSpatialInformationSupplier,set_InSpatialTableFile)

* - description: Bestimmt den Schlüssel für die Auswahl der Milieuinformation in der tabellarischen Datei (Spaltenname).
* - identifier: Auswahlschlüssel
* - type: Boolean
PARAMETER par_link_InFileBasedSelectGroupSpatialInformationSupplier_InAttributeName_selectKey(set_InFileBasedSelectGroupSpatialInformationSupplier,set_InAttributeName)

* - description: Bestimmt den Schlüssel für die Gruppierung und Wichtung anhand der tabellarischen Datei (Spaltenname).
* - identifier: Gruppierungsschlüssel
* - type: Boolean
PARAMETER par_link_InFileBasedSelectGroupSpatialInformationSupplier_InAttributeName_groupKey(set_InFileBasedSelectGroupSpatialInformationSupplier,set_InAttributeName)

* - identifier: InFileBasedSelectSpatialInformationSupplier
* - type: String
SET set_InFileBasedSelectSpatialInformationSupplier(set_InSpatialDistributionWithCollection)

* - description: Bestimmt den Schlüssel für die Auswahl der X-Position in der tabellarischen Datei (Spaltenname).
* - identifier: X-Schlüssel
* - type: Boolean
PARAMETER par_link_InFileBasedSelectSpatialInformationSupplier_InAttributeName_xPositionKey(set_InFileBasedSelectSpatialInformationSupplier,set_InAttributeName)

* - description: Bestimmt den Schlüssel für die Auswahl der Y-Position in der tabellarischen Datei (Spaltenname).
* - identifier: Y-Schlüssel
* - type: Boolean
PARAMETER par_link_InFileBasedSelectSpatialInformationSupplier_InAttributeName_yPositionKey(set_InFileBasedSelectSpatialInformationSupplier,set_InAttributeName)

* - description: Bestimmt den Schlüssel für die Auswahl der ID in der tabellarischen Datei (Spaltenname).
* - identifier: ID-Schlüssel
* - type: Boolean
PARAMETER par_link_InFileBasedSelectSpatialInformationSupplier_InAttributeName_idKey(set_InFileBasedSelectSpatialInformationSupplier,set_InAttributeName)

* - description: Legt die Datei fest, aus der die räumlichen Informationen gelesen werden sollen.
* - identifier: Tabellarische Datei
* - type: Boolean
PARAMETER par_link_InFileBasedSelectSpatialInformationSupplier_InSpatialTableFile_file(set_InFileBasedSelectSpatialInformationSupplier,set_InSpatialTableFile)

* - description: Bestimmt den Schlüssel für die Auswahl der Information in der tabellarischen Datei (Spaltenname).
* - identifier: Auswahlschlüssel
* - type: Boolean
PARAMETER par_link_InFileBasedSelectSpatialInformationSupplier_InAttributeName_selectKey(set_InFileBasedSelectSpatialInformationSupplier,set_InAttributeName)

* - identifier: InFileBasedSpatialInformationSupplier
* - type: String
SET set_InFileBasedSpatialInformationSupplier(set_InSpatialDistributionWithCollection)

* - description: Bestimmt den Schlüssel für die Auswahl der X-Position in der tabellarischen Datei (Spaltenname).
* - identifier: X-Schlüssel
* - type: Boolean
PARAMETER par_link_InFileBasedSpatialInformationSupplier_InAttributeName_xPositionKey(set_InFileBasedSpatialInformationSupplier,set_InAttributeName)

* - description: Bestimmt den Schlüssel für die Auswahl der Y-Position in der tabellarischen Datei (Spaltenname).
* - identifier: Y-Schlüssel
* - type: Boolean
PARAMETER par_link_InFileBasedSpatialInformationSupplier_InAttributeName_yPositionKey(set_InFileBasedSpatialInformationSupplier,set_InAttributeName)

* - description: Bestimmt den Schlüssel für die Auswahl der ID in der tabellarischen Datei (Spaltenname).
* - identifier: ID-Schlüssel
* - type: Boolean
PARAMETER par_link_InFileBasedSpatialInformationSupplier_InAttributeName_idKey(set_InFileBasedSpatialInformationSupplier,set_InAttributeName)

* - description: Legt die Datei fest, aus der die räumlichen Informationen gelesen werden sollen.
* - identifier: Tabellarische Datei
* - type: Boolean
PARAMETER par_link_InFileBasedSpatialInformationSupplier_InSpatialTableFile_file(set_InFileBasedSpatialInformationSupplier,set_InSpatialTableFile)

* - identifier: InSpatialDistribution
* - hidden: 1
* - type: String
SET set_InSpatialDistribution(*)

* - identifier: InSpatialDistributionWithCollection
* - hidden: 1
* - type: String
SET set_InSpatialDistributionWithCollection(set_InSpatialDistribution)

* - identifier: InSpace2D
* - type: String
SET set_InSpace2D(set_InSpatialModel)

* - default: 0
* - domain: [0|1]
* - description: Nutzt die Manhattenmetrik für die Berechnung der Abstände.
* - identifier: Manhattenmetrik
* - rule: IF (par_InSpace2D_useManhatten == 1, par_InSpace2D_useEuclid = 0)
* - rule: IF (par_InSpace2D_useManhatten == 1, par_InSpace2D_useEuclid2 = 0)
* - rule: IF (par_InSpace2D_useManhatten == 1, par_InSpace2D_useMaximum = 0)
* - rule: IF (par_InSpace2D_useManhatten == 1, par_InSpace2D_useHaversineM = 0)
* - rule: IF (par_InSpace2D_useManhatten == 1, par_InSpace2D_useHaversineKM = 0)
* - rule: IF (par_InSpace2D_useManhatten == 0, par_InSpace2D_useManhatten = 1)
* - rule: IF (par_InSpace2D_useManhatten == 0, par_InSpace2D_useEuclid = 0)
* - rule: IF (par_InSpace2D_useManhatten == 0, par_InSpace2D_useEuclid2 = 0)
* - rule: IF (par_InSpace2D_useManhatten == 0, par_InSpace2D_useMaximum = 0)
* - rule: IF (par_InSpace2D_useManhatten == 0, par_InSpace2D_useHaversineM = 0)
* - rule: IF (par_InSpace2D_useManhatten == 0, par_InSpace2D_useHaversineKM = 0)
* - type: Boolean
PARAMETER par_InSpace2D_useManhatten(set_InSpace2D)

* - default: 0
* - domain: [0|1]
* - description: Nutzt die euklidische Metrik für die Berechnung der Abstände.
* - identifier: Euklidische Metrik
* - rule: IF (par_InSpace2D_useEuclid == 1, par_InSpace2D_useManhatten = 0)
* - rule: IF (par_InSpace2D_useEuclid == 1, par_InSpace2D_useEuclid2 = 0)
* - rule: IF (par_InSpace2D_useEuclid == 1, par_InSpace2D_useMaximum = 0)
* - rule: IF (par_InSpace2D_useEuclid == 1, par_InSpace2D_useHaversineM = 0)
* - rule: IF (par_InSpace2D_useEuclid == 1, par_InSpace2D_useHaversineKM = 0)
* - rule: IF (par_InSpace2D_useEuclid == 0, par_InSpace2D_useManhatten = 0)
* - rule: IF (par_InSpace2D_useEuclid == 0, par_InSpace2D_useEuclid = 1)
* - rule: IF (par_InSpace2D_useEuclid == 0, par_InSpace2D_useEuclid2 = 0)
* - rule: IF (par_InSpace2D_useEuclid == 0, par_InSpace2D_useMaximum = 0)
* - rule: IF (par_InSpace2D_useEuclid == 0, par_InSpace2D_useHaversineM = 0)
* - rule: IF (par_InSpace2D_useEuclid == 0, par_InSpace2D_useHaversineKM = 0)
* - type: Boolean
PARAMETER par_InSpace2D_useEuclid(set_InSpace2D)

* - default: 0
* - domain: [0|1]
* - description: Nutzt die quadratische euklidische Metrik für die Berechnung der Abstände. Im Vergleich zur normalen euklidischen Distanz sqrt((x2 - x1)^2 * (y2 - y1)^2) wird hier die Wurzel weggelassen und der Abstand mittels (x2 - x1)^2 * (y2 - y1)^2 berechnet.
* - identifier: Euklidische Metrik (quadratisch)
* - rule: IF (par_InSpace2D_useEuclid2 == 1, par_InSpace2D_useManhatten = 0)
* - rule: IF (par_InSpace2D_useEuclid2 == 1, par_InSpace2D_useEuclid = 0)
* - rule: IF (par_InSpace2D_useEuclid2 == 1, par_InSpace2D_useMaximum = 0)
* - rule: IF (par_InSpace2D_useEuclid2 == 1, par_InSpace2D_useHaversineM = 0)
* - rule: IF (par_InSpace2D_useEuclid2 == 1, par_InSpace2D_useHaversineKM = 0)
* - rule: IF (par_InSpace2D_useEuclid2 == 0, par_InSpace2D_useManhatten = 0)
* - rule: IF (par_InSpace2D_useEuclid2 == 0, par_InSpace2D_useEuclid = 0)
* - rule: IF (par_InSpace2D_useEuclid2 == 0, par_InSpace2D_useEuclid2 = 1)
* - rule: IF (par_InSpace2D_useEuclid2 == 0, par_InSpace2D_useMaximum = 0)
* - rule: IF (par_InSpace2D_useEuclid2 == 0, par_InSpace2D_useHaversineM = 0)
* - rule: IF (par_InSpace2D_useEuclid2 == 0, par_InSpace2D_useHaversineKM = 0)
* - type: Boolean
PARAMETER par_InSpace2D_useEuclid2(set_InSpace2D)

* - default: 0
* - domain: [0|1]
* - description: Nutzt die Maximumsmetrik für die Berechnung der Abstände.
* - identifier: Maximumsmetrik
* - rule: IF (par_InSpace2D_useMaximum == 1, par_InSpace2D_useManhatten = 0)
* - rule: IF (par_InSpace2D_useMaximum == 1, par_InSpace2D_useEuclid = 0)
* - rule: IF (par_InSpace2D_useMaximum == 1, par_InSpace2D_useEuclid2 = 0)
* - rule: IF (par_InSpace2D_useMaximum == 1, par_InSpace2D_useHaversineM = 0)
* - rule: IF (par_InSpace2D_useMaximum == 1, par_InSpace2D_useHaversineKM = 0)
* - rule: IF (par_InSpace2D_useMaximum == 0, par_InSpace2D_useManhatten = 0)
* - rule: IF (par_InSpace2D_useMaximum == 0, par_InSpace2D_useEuclid = 0)
* - rule: IF (par_InSpace2D_useMaximum == 0, par_InSpace2D_useEuclid2 = 0)
* - rule: IF (par_InSpace2D_useMaximum == 0, par_InSpace2D_useMaximum = 1)
* - rule: IF (par_InSpace2D_useMaximum == 0, par_InSpace2D_useHaversineM = 0)
* - rule: IF (par_InSpace2D_useMaximum == 0, par_InSpace2D_useHaversineKM = 0)
* - type: Boolean
PARAMETER par_InSpace2D_useMaximum(set_InSpace2D)

* - default: 0
* - domain: [0|1]
* - description: Nutzt die Haversine-Formel für die Berechnung der Entfernungen. Die Berechnungen finden auf Meterbasis statt.
* - identifier: Haversine (Meter)
* - rule: IF (par_InSpace2D_useHaversineM == 1, par_InSpace2D_useManhatten = 0)
* - rule: IF (par_InSpace2D_useHaversineM == 1, par_InSpace2D_useEuclid = 0)
* - rule: IF (par_InSpace2D_useHaversineM == 1, par_InSpace2D_useEuclid2 = 0)
* - rule: IF (par_InSpace2D_useHaversineM == 1, par_InSpace2D_useMaximum = 0)
* - rule: IF (par_InSpace2D_useHaversineM == 1, par_InSpace2D_useHaversineKM = 0)
* - rule: IF (par_InSpace2D_useHaversineM == 0, par_InSpace2D_useManhatten = 0)
* - rule: IF (par_InSpace2D_useHaversineM == 0, par_InSpace2D_useEuclid = 0)
* - rule: IF (par_InSpace2D_useHaversineM == 0, par_InSpace2D_useEuclid2 = 0)
* - rule: IF (par_InSpace2D_useHaversineM == 0, par_InSpace2D_useMaximum = 0)
* - rule: IF (par_InSpace2D_useHaversineM == 0, par_InSpace2D_useHaversineM = 1)
* - rule: IF (par_InSpace2D_useHaversineM == 0, par_InSpace2D_useHaversineKM = 0)
* - type: Boolean
PARAMETER par_InSpace2D_useHaversineM(set_InSpace2D)

* - default: 1
* - domain: [0|1]
* - description: Nutzt die Haversine-Formel für die Berechnung der Entfernungen. Die Berechnungen finden auf Kilometerbasis statt.
* - identifier: Haversine (Kilometer)
* - rule: IF (par_InSpace2D_useHaversineKM == 1, par_InSpace2D_useManhatten = 0)
* - rule: IF (par_InSpace2D_useHaversineKM == 1, par_InSpace2D_useEuclid = 0)
* - rule: IF (par_InSpace2D_useHaversineKM == 1, par_InSpace2D_useEuclid2 = 0)
* - rule: IF (par_InSpace2D_useHaversineKM == 1, par_InSpace2D_useMaximum = 0)
* - rule: IF (par_InSpace2D_useHaversineKM == 1, par_InSpace2D_useHaversineM = 0)
* - rule: IF (par_InSpace2D_useHaversineKM == 0, par_InSpace2D_useManhatten = 0)
* - rule: IF (par_InSpace2D_useHaversineKM == 0, par_InSpace2D_useEuclid = 0)
* - rule: IF (par_InSpace2D_useHaversineKM == 0, par_InSpace2D_useEuclid2 = 0)
* - rule: IF (par_InSpace2D_useHaversineKM == 0, par_InSpace2D_useMaximum = 0)
* - rule: IF (par_InSpace2D_useHaversineKM == 0, par_InSpace2D_useHaversineM = 0)
* - rule: IF (par_InSpace2D_useHaversineKM == 0, par_InSpace2D_useHaversineKM = 1)
* - type: Boolean
PARAMETER par_InSpace2D_useHaversineKM(set_InSpace2D)

* - identifier: InSpatialModel
* - hidden: 1
* - type: String
SET set_InSpatialModel(*)

* - identifier: InTimeModel
* - hidden: 1
* - type: String
SET set_InTimeModel(*)

* - identifier: InUnitStepDiscreteTimeModel
* - type: String
SET set_InUnitStepDiscreteTimeModel(set_InTimeModel)

* - default: 1
* - domain: (0,)
* - description: Legt die Zeitdauer fest, welche pro diskreten Schritt vergeht.
* - identifier: Zeitdauer
* - type: Integer
PARAMETER par_InUnitStepDiscreteTimeModel_amountOfTime(set_InUnitStepDiscreteTimeModel)

* - default: 0
* - domain: [0|1]
* - description: Verwendet Millisekunden als Einheit für die Zeitdauer.
* - identifier: Millisekunden
* - rule: IF (par_InUnitStepDiscreteTimeModel_useMs == 1, par_InUnitStepDiscreteTimeModel_useSec = 0)
* - rule: IF (par_InUnitStepDiscreteTimeModel_useMs == 1, par_InUnitStepDiscreteTimeModel_useMin = 0)
* - rule: IF (par_InUnitStepDiscreteTimeModel_useMs == 1, par_InUnitStepDiscreteTimeModel_useH = 0)
* - rule: IF (par_InUnitStepDiscreteTimeModel_useMs == 1, par_InUnitStepDiscreteTimeModel_useD = 0)
* - rule: IF (par_InUnitStepDiscreteTimeModel_useMs == 1, par_InUnitStepDiscreteTimeModel_useW = 0)
* - rule: IF (par_InUnitStepDiscreteTimeModel_useMs == 1, par_InUnitStepDiscreteTimeModel_useM = 0)
* - rule: IF (par_InUnitStepDiscreteTimeModel_useMs == 0, par_InUnitStepDiscreteTimeModel_useMs = 1)
* - rule: IF (par_InUnitStepDiscreteTimeModel_useMs == 0, par_InUnitStepDiscreteTimeModel_useSec = 0)
* - rule: IF (par_InUnitStepDiscreteTimeModel_useMs == 0, par_InUnitStepDiscreteTimeModel_useMin = 0)
* - rule: IF (par_InUnitStepDiscreteTimeModel_useMs == 0, par_InUnitStepDiscreteTimeModel_useH = 0)
* - rule: IF (par_InUnitStepDiscreteTimeModel_useMs == 0, par_InUnitStepDiscreteTimeModel_useD = 0)
* - rule: IF (par_InUnitStepDiscreteTimeModel_useMs == 0, par_InUnitStepDiscreteTimeModel_useW = 0)
* - rule: IF (par_InUnitStepDiscreteTimeModel_useMs == 0, par_InUnitStepDiscreteTimeModel_useM = 0)
* - type: Boolean
PARAMETER par_InUnitStepDiscreteTimeModel_useMs(set_InUnitStepDiscreteTimeModel)

* - default: 0
* - domain: [0|1]
* - description: Verwendet Sekunden als Einheit für die Zeitdauer.
* - identifier: Sekunden
* - rule: IF (par_InUnitStepDiscreteTimeModel_useSec == 1, par_InUnitStepDiscreteTimeModel_useMs = 0)
* - rule: IF (par_InUnitStepDiscreteTimeModel_useSec == 1, par_InUnitStepDiscreteTimeModel_useMin = 0)
* - rule: IF (par_InUnitStepDiscreteTimeModel_useSec == 1, par_InUnitStepDiscreteTimeModel_useH = 0)
* - rule: IF (par_InUnitStepDiscreteTimeModel_useSec == 1, par_InUnitStepDiscreteTimeModel_useD = 0)
* - rule: IF (par_InUnitStepDiscreteTimeModel_useSec == 1, par_InUnitStepDiscreteTimeModel_useW = 0)
* - rule: IF (par_InUnitStepDiscreteTimeModel_useSec == 1, par_InUnitStepDiscreteTimeModel_useM = 0)
* - rule: IF (par_InUnitStepDiscreteTimeModel_useSec == 0, par_InUnitStepDiscreteTimeModel_useMs = 0)
* - rule: IF (par_InUnitStepDiscreteTimeModel_useSec == 0, par_InUnitStepDiscreteTimeModel_useSec = 1)
* - rule: IF (par_InUnitStepDiscreteTimeModel_useSec == 0, par_InUnitStepDiscreteTimeModel_useMin = 0)
* - rule: IF (par_InUnitStepDiscreteTimeModel_useSec == 0, par_InUnitStepDiscreteTimeModel_useH = 0)
* - rule: IF (par_InUnitStepDiscreteTimeModel_useSec == 0, par_InUnitStepDiscreteTimeModel_useD = 0)
* - rule: IF (par_InUnitStepDiscreteTimeModel_useSec == 0, par_InUnitStepDiscreteTimeModel_useW = 0)
* - rule: IF (par_InUnitStepDiscreteTimeModel_useSec == 0, par_InUnitStepDiscreteTimeModel_useM = 0)
* - type: Boolean
PARAMETER par_InUnitStepDiscreteTimeModel_useSec(set_InUnitStepDiscreteTimeModel)

* - default: 0
* - domain: [0|1]
* - description: Verwendet Minuten als Einheit für die Zeitdauer.
* - identifier: Minuten
* - rule: IF (par_InUnitStepDiscreteTimeModel_useMin == 1, par_InUnitStepDiscreteTimeModel_useMs = 0)
* - rule: IF (par_InUnitStepDiscreteTimeModel_useMin == 1, par_InUnitStepDiscreteTimeModel_useSec = 0)
* - rule: IF (par_InUnitStepDiscreteTimeModel_useMin == 1, par_InUnitStepDiscreteTimeModel_useH = 0)
* - rule: IF (par_InUnitStepDiscreteTimeModel_useMin == 1, par_InUnitStepDiscreteTimeModel_useD = 0)
* - rule: IF (par_InUnitStepDiscreteTimeModel_useMin == 1, par_InUnitStepDiscreteTimeModel_useW = 0)
* - rule: IF (par_InUnitStepDiscreteTimeModel_useMin == 1, par_InUnitStepDiscreteTimeModel_useM = 0)
* - rule: IF (par_InUnitStepDiscreteTimeModel_useMin == 0, par_InUnitStepDiscreteTimeModel_useMs = 0)
* - rule: IF (par_InUnitStepDiscreteTimeModel_useMin == 0, par_InUnitStepDiscreteTimeModel_useSec = 0)
* - rule: IF (par_InUnitStepDiscreteTimeModel_useMin == 0, par_InUnitStepDiscreteTimeModel_useMin = 1)
* - rule: IF (par_InUnitStepDiscreteTimeModel_useMin == 0, par_InUnitStepDiscreteTimeModel_useH = 0)
* - rule: IF (par_InUnitStepDiscreteTimeModel_useMin == 0, par_InUnitStepDiscreteTimeModel_useD = 0)
* - rule: IF (par_InUnitStepDiscreteTimeModel_useMin == 0, par_InUnitStepDiscreteTimeModel_useW = 0)
* - rule: IF (par_InUnitStepDiscreteTimeModel_useMin == 0, par_InUnitStepDiscreteTimeModel_useM = 0)
* - type: Boolean
PARAMETER par_InUnitStepDiscreteTimeModel_useMin(set_InUnitStepDiscreteTimeModel)

* - default: 0
* - domain: [0|1]
* - description: Verwendet Stunden als Einheit für die Zeitdauer.
* - identifier: Stunden
* - rule: IF (par_InUnitStepDiscreteTimeModel_useH == 1, par_InUnitStepDiscreteTimeModel_useMs = 0)
* - rule: IF (par_InUnitStepDiscreteTimeModel_useH == 1, par_InUnitStepDiscreteTimeModel_useSec = 0)
* - rule: IF (par_InUnitStepDiscreteTimeModel_useH == 1, par_InUnitStepDiscreteTimeModel_useMin = 0)
* - rule: IF (par_InUnitStepDiscreteTimeModel_useH == 1, par_InUnitStepDiscreteTimeModel_useD = 0)
* - rule: IF (par_InUnitStepDiscreteTimeModel_useH == 1, par_InUnitStepDiscreteTimeModel_useW = 0)
* - rule: IF (par_InUnitStepDiscreteTimeModel_useH == 1, par_InUnitStepDiscreteTimeModel_useM = 0)
* - rule: IF (par_InUnitStepDiscreteTimeModel_useH == 0, par_InUnitStepDiscreteTimeModel_useMs = 0)
* - rule: IF (par_InUnitStepDiscreteTimeModel_useH == 0, par_InUnitStepDiscreteTimeModel_useSec = 0)
* - rule: IF (par_InUnitStepDiscreteTimeModel_useH == 0, par_InUnitStepDiscreteTimeModel_useMin = 0)
* - rule: IF (par_InUnitStepDiscreteTimeModel_useH == 0, par_InUnitStepDiscreteTimeModel_useH = 1)
* - rule: IF (par_InUnitStepDiscreteTimeModel_useH == 0, par_InUnitStepDiscreteTimeModel_useD = 0)
* - rule: IF (par_InUnitStepDiscreteTimeModel_useH == 0, par_InUnitStepDiscreteTimeModel_useW = 0)
* - rule: IF (par_InUnitStepDiscreteTimeModel_useH == 0, par_InUnitStepDiscreteTimeModel_useM = 0)
* - type: Boolean
PARAMETER par_InUnitStepDiscreteTimeModel_useH(set_InUnitStepDiscreteTimeModel)

* - default: 0
* - domain: [0|1]
* - description: Verwendet Tage als Einheit für die Zeitdauer.
* - identifier: Tage
* - rule: IF (par_InUnitStepDiscreteTimeModel_useD == 1, par_InUnitStepDiscreteTimeModel_useMs = 0)
* - rule: IF (par_InUnitStepDiscreteTimeModel_useD == 1, par_InUnitStepDiscreteTimeModel_useSec = 0)
* - rule: IF (par_InUnitStepDiscreteTimeModel_useD == 1, par_InUnitStepDiscreteTimeModel_useMin = 0)
* - rule: IF (par_InUnitStepDiscreteTimeModel_useD == 1, par_InUnitStepDiscreteTimeModel_useH = 0)
* - rule: IF (par_InUnitStepDiscreteTimeModel_useD == 1, par_InUnitStepDiscreteTimeModel_useW = 0)
* - rule: IF (par_InUnitStepDiscreteTimeModel_useD == 1, par_InUnitStepDiscreteTimeModel_useM = 0)
* - rule: IF (par_InUnitStepDiscreteTimeModel_useD == 0, par_InUnitStepDiscreteTimeModel_useMs = 0)
* - rule: IF (par_InUnitStepDiscreteTimeModel_useD == 0, par_InUnitStepDiscreteTimeModel_useSec = 0)
* - rule: IF (par_InUnitStepDiscreteTimeModel_useD == 0, par_InUnitStepDiscreteTimeModel_useMin = 0)
* - rule: IF (par_InUnitStepDiscreteTimeModel_useD == 0, par_InUnitStepDiscreteTimeModel_useH = 0)
* - rule: IF (par_InUnitStepDiscreteTimeModel_useD == 0, par_InUnitStepDiscreteTimeModel_useD = 1)
* - rule: IF (par_InUnitStepDiscreteTimeModel_useD == 0, par_InUnitStepDiscreteTimeModel_useW = 0)
* - rule: IF (par_InUnitStepDiscreteTimeModel_useD == 0, par_InUnitStepDiscreteTimeModel_useM = 0)
* - type: Boolean
PARAMETER par_InUnitStepDiscreteTimeModel_useD(set_InUnitStepDiscreteTimeModel)

* - default: 1
* - domain: [0|1]
* - description: Verwendet Wochen als Einheit für die Zeitdauer.
* - identifier: Wochen
* - rule: IF (par_InUnitStepDiscreteTimeModel_useW == 1, par_InUnitStepDiscreteTimeModel_useMs = 0)
* - rule: IF (par_InUnitStepDiscreteTimeModel_useW == 1, par_InUnitStepDiscreteTimeModel_useSec = 0)
* - rule: IF (par_InUnitStepDiscreteTimeModel_useW == 1, par_InUnitStepDiscreteTimeModel_useMin = 0)
* - rule: IF (par_InUnitStepDiscreteTimeModel_useW == 1, par_InUnitStepDiscreteTimeModel_useH = 0)
* - rule: IF (par_InUnitStepDiscreteTimeModel_useW == 1, par_InUnitStepDiscreteTimeModel_useD = 0)
* - rule: IF (par_InUnitStepDiscreteTimeModel_useW == 1, par_InUnitStepDiscreteTimeModel_useM = 0)
* - rule: IF (par_InUnitStepDiscreteTimeModel_useW == 0, par_InUnitStepDiscreteTimeModel_useMs = 0)
* - rule: IF (par_InUnitStepDiscreteTimeModel_useW == 0, par_InUnitStepDiscreteTimeModel_useSec = 0)
* - rule: IF (par_InUnitStepDiscreteTimeModel_useW == 0, par_InUnitStepDiscreteTimeModel_useMin = 0)
* - rule: IF (par_InUnitStepDiscreteTimeModel_useW == 0, par_InUnitStepDiscreteTimeModel_useH = 0)
* - rule: IF (par_InUnitStepDiscreteTimeModel_useW == 0, par_InUnitStepDiscreteTimeModel_useD = 0)
* - rule: IF (par_InUnitStepDiscreteTimeModel_useW == 0, par_InUnitStepDiscreteTimeModel_useW = 1)
* - rule: IF (par_InUnitStepDiscreteTimeModel_useW == 0, par_InUnitStepDiscreteTimeModel_useM = 0)
* - type: Boolean
PARAMETER par_InUnitStepDiscreteTimeModel_useW(set_InUnitStepDiscreteTimeModel)

* - default: 0
* - domain: [0|1]
* - description: Verwendet Monate als Einheit für die Zeitdauer.
* - identifier: Monate
* - rule: IF (par_InUnitStepDiscreteTimeModel_useM == 1, par_InUnitStepDiscreteTimeModel_useMs = 0)
* - rule: IF (par_InUnitStepDiscreteTimeModel_useM == 1, par_InUnitStepDiscreteTimeModel_useSec = 0)
* - rule: IF (par_InUnitStepDiscreteTimeModel_useM == 1, par_InUnitStepDiscreteTimeModel_useMin = 0)
* - rule: IF (par_InUnitStepDiscreteTimeModel_useM == 1, par_InUnitStepDiscreteTimeModel_useH = 0)
* - rule: IF (par_InUnitStepDiscreteTimeModel_useM == 1, par_InUnitStepDiscreteTimeModel_useD = 0)
* - rule: IF (par_InUnitStepDiscreteTimeModel_useM == 1, par_InUnitStepDiscreteTimeModel_useW = 0)
* - rule: IF (par_InUnitStepDiscreteTimeModel_useM == 0, par_InUnitStepDiscreteTimeModel_useMs = 0)
* - rule: IF (par_InUnitStepDiscreteTimeModel_useM == 0, par_InUnitStepDiscreteTimeModel_useSec = 0)
* - rule: IF (par_InUnitStepDiscreteTimeModel_useM == 0, par_InUnitStepDiscreteTimeModel_useMin = 0)
* - rule: IF (par_InUnitStepDiscreteTimeModel_useM == 0, par_InUnitStepDiscreteTimeModel_useH = 0)
* - rule: IF (par_InUnitStepDiscreteTimeModel_useM == 0, par_InUnitStepDiscreteTimeModel_useD = 0)
* - rule: IF (par_InUnitStepDiscreteTimeModel_useM == 0, par_InUnitStepDiscreteTimeModel_useW = 0)
* - rule: IF (par_InUnitStepDiscreteTimeModel_useM == 0, par_InUnitStepDiscreteTimeModel_useM = 1)
* - type: Boolean
PARAMETER par_InUnitStepDiscreteTimeModel_useM(set_InUnitStepDiscreteTimeModel)

* - default: 0
* - domain: [0|1]
* - description: Falls aktiviert, wird das Netzwerk am Ende der Simulation gespeichert.
* - identifier: Finales Netzwerk speichern?
* - type: Boolean
SCALAR sca_InGraphvizGeneral_storeEndImage

* - default: 0
* - domain: [0|1]
* - description: Ermöglicht das Speichern der dot-Datei. Diese Datei enthält die Konstruktionsinformationen des Netzwerkes.
* - identifier: Finale dot-Datei speichern?
* - type: Boolean
SCALAR sca_InGraphvizGeneral_storeDotFile

* - default: 1000
* - domain: (0,)
* - description: Definiert den Bildbreite für die Netzwerkbilder. Falls der Wert 0 ist, bestimmt Graphviz die optimale Bildgröße automatisch.
* - identifier: Bevorzugte Bildbreite
* - unit: [Pixel]
* - type: Float
SCALAR sca_InGraphvizGeneral_preferredImageWidth

* - default: 1000
* - domain: (0,)
* - description: Definiert den Bildhöhe für die Netzwerkbilder. Falls der Wert 0 ist, bestimmt Graphviz die optimale Bildgröße automatisch.
* - identifier: Bevorzugte Bildhöhe
* - unit: [Pixel]
* - type: Float
SCALAR sca_InGraphvizGeneral_preferredImageHeight

* - default: 1
* - domain: [0|1]
* - description: Nutzt für die Grapherzeugung ein positionsbasiertes Layout. Das genaue Layout ist NEATO kombiniert mit -n. Dieses Layout setzt vorraus, dass die Agenten eine Position besitzen. Zusätzlich benötigt es eine eingestellte Bildgröße. Falls diese fehlt, wird ein (ca.) 1000x1000 Pixel großes Bild erzeugt. Dieses Layout kann zu Verzerrungen führen, da es unter Umständen (z.B. bei Geoinformationen) die Daten in das gewünschte Ausgabeformat transformiert. Um das Seitenverhältnis beizubehalten, kann die entsprechende Option genutzt werden.
* - identifier: Positionsbasiertes Layout
* - rule: IF (par_InGraphvizGeneral_positionBasedLayout == 1, par_InGraphvizGeneral_freeLayout = 0)
* - rule: IF (par_InGraphvizGeneral_positionBasedLayout == 0, par_InGraphvizGeneral_positionBasedLayout = 1)
* - rule: IF (par_InGraphvizGeneral_positionBasedLayout == 0, par_InGraphvizGeneral_freeLayout = 0)
* - type: Boolean
SCALAR sca_InGraphvizGeneral_positionBasedLayout

* - default: 0
* - domain: [0|1]
* - description: Nutzt für die Grapherzeugung ein kräftebasierentes Layout. Das genaue Layout ist SFDP mit der overlap-Option prism.
* - identifier: Freies Layout
* - rule: IF (par_InGraphvizGeneral_freeLayout == 1, par_InGraphvizGeneral_positionBasedLayout = 0)
* - rule: IF (par_InGraphvizGeneral_freeLayout == 0, par_InGraphvizGeneral_positionBasedLayout = 0)
* - rule: IF (par_InGraphvizGeneral_freeLayout == 0, par_InGraphvizGeneral_freeLayout = 1)
* - type: Boolean
SCALAR sca_InGraphvizGeneral_freeLayout

* - default: 0
* - domain: [0|1]
* - description: Diese Option erlaubt es das Seitenverhältnis der Daten beizubehalten. Die Entscheidung, ob die Länge oder die Breite des Bildes verändert wird, hängt von der finalen Bildfläche ab. Das Verfahren nutzt die Variante mit der kleineren Bildfläche.
* - identifier: Seitenverhätnis beibehalten
* - type: Boolean
SCALAR sca_InGraphvizGeneral_keepAspectRatio

* - default: 0
* - domain: [0|1]
* - description: Diese Option erlaubt die Nutzung des positionbasierten Layouts ohne gültige Positionen. Agenten ohne Position wird der Punkt (0,0) zugeordnet.
* - identifier: Erlaube fehlende Positionen
* - type: Boolean
SCALAR sca_InGraphvizGeneral_useDefaultPositionIfMissing

* - identifier: InConsumerAgentGroupColor
* - type: String
SET set_InConsumerAgentGroupColor(*)

* - default: 0
* - description: Die zu nutzende Farbe für die Gruppen. Für die Farbe muss der dezimal-kodierte rgba-Wert genutzt werden.
* - identifier: Farbe (rgba)
* - type: Integer
PARAMETER par_InConsumerAgentGroupColor_rgba(set_InConsumerAgentGroupColor)

* - description: Die Gruppen, welche die ausgewählte Farbe verwenden sollen.
* - identifier: Konsumergruppen
* - type: Boolean
PARAMETER par_link_InConsumerAgentGroupColor_InConsumerAgentGroup_groups(set_InConsumerAgentGroupColor,set_InConsumerAgentGroup)

* - default: 42
* - description: Setzt den Seed für den Zufallsgenerator der Simulation. Falls er auf -1 gesetzt wird, wird ein zufälliger Seed generiert.
* - identifier: Zufallsgenerator (seed)
* - type: Integer
SCALAR sca_InGeneral_seed

* - default: 5
* - description: Setzt den Timeout der Simulation. Diese Einstellung dient dazu die Simulation zu beenden, falls sie unerwartet abstürzt. Wird der Timeout auf 0 gesetzt, wird die Funktion deaktiviert. Es wird empfohlen den Standardwert eingestellt zu lassen.
* - identifier: Timeout
* - type: Integer
SCALAR sca_InGeneral_timeout

* - default: 0
* - domain: [0|1]
* - description: Die Länge des Timeouts wird in Millisekunden angegeben.
* - identifier: Timeout in Millisekunden
* - rule: IF (sca_InGeneral_timeoutUseMs == 1, sca_InGeneral_timeoutUseSec = 0)
* - rule: IF (sca_InGeneral_timeoutUseMs == 1, sca_InGeneral_timeoutUseMin = 0)
* - rule: IF (sca_InGeneral_timeoutUseMs == 0, sca_InGeneral_timeoutUseMs = 1)
* - rule: IF (sca_InGeneral_timeoutUseMs == 0, sca_InGeneral_timeoutUseSec = 0)
* - rule: IF (sca_InGeneral_timeoutUseMs == 0, sca_InGeneral_timeoutUseMin = 0)
* - type: Boolean
SCALAR sca_InGeneral_timeoutUseMs

* - default: 0
* - domain: [0|1]
* - description: Die Länge des Timeouts wird in Sekunden angegeben.
* - identifier: Timeout in Sekunden
* - rule: IF (sca_InGeneral_timeoutUseSec == 1, sca_InGeneral_timeoutUseMs = 0)
* - rule: IF (sca_InGeneral_timeoutUseSec == 1, sca_InGeneral_timeoutUseMin = 0)
* - rule: IF (sca_InGeneral_timeoutUseSec == 0, sca_InGeneral_timeoutUseMs = 0)
* - rule: IF (sca_InGeneral_timeoutUseSec == 0, sca_InGeneral_timeoutUseSec = 1)
* - rule: IF (sca_InGeneral_timeoutUseSec == 0, sca_InGeneral_timeoutUseMin = 0)
* - type: Boolean
SCALAR sca_InGeneral_timeoutUseSec

* - default: 1
* - domain: [0|1]
* - description: Die Länge des Timeouts wird in Minuten angegeben.
* - identifier: Timeout in Minuten
* - rule: IF (sca_InGeneral_timeoutUseMin == 1, sca_InGeneral_timeoutUseMs = 0)
* - rule: IF (sca_InGeneral_timeoutUseMin == 1, sca_InGeneral_timeoutUseSec = 0)
* - rule: IF (sca_InGeneral_timeoutUseMin == 0, sca_InGeneral_timeoutUseMs = 0)
* - rule: IF (sca_InGeneral_timeoutUseMin == 0, sca_InGeneral_timeoutUseSec = 0)
* - rule: IF (sca_InGeneral_timeoutUseMin == 0, sca_InGeneral_timeoutUseMin = 1)
* - type: Boolean
SCALAR sca_InGeneral_timeoutUseMin

* - default: 0
* - domain: [0|1]
* - description: Ermöglicht die Nutzung des alternativen Startjahrs.
* - identifier: [Spezialoption] Startjahr aktivieren?
* - type: Boolean
SCALAR sca_InGeneral_enableFirstSimulationYear

* - default: 0
* - description: Diese Einstellung überschreibt das tatsächliche Startjahr der IRPsim-Einstellungen. Diese Option wurde eingebaut, um eine Seinsitivitätenanalyse über die Jahre zu nutzen.
* - identifier: [Spezialoption] Startjahr
* - type: Integer
SCALAR sca_InGeneral_firstSimulationYear

* - default: 0
* - description: ([Wichtig] Diese Option darf nur bei puren IRPact-Aufrufen (keine Modellkopplungen) genutzt werden, da sonst die Zeitkonsistenz verletzt wird!) Setzt das letzte zu simulierende Jahr der Simulation. Es wird dabei immer mindestens ein Jahr simuliert. Sollte der Wert also kleiner sein als das Ausgangsjahr, so wird dennoch das Ausgangsjahr simuliert. Beispiel: Ist das Ausgangsjahr = 2010 und letzte Simulationsjahr = 2013, dann werden die Jahre 2010, 2011, 2012 und 2013 simuliert.
* - identifier: [Spezialoption] Letzte zu simulierende Jahr
* - type: Integer
SCALAR sca_InGeneral_lastSimulationYear

* - description: Einlesen des zu optimierenden Jahres
* - hidden: 1
* - identifier: Jahreszahl
* - type: Integer
SCALAR sca_a

* - default: 0.25
* - domain: (0,)
* - description: Einlesen der Zeitschrittlänge der Simulationszeitreihen (bezogen auf eine Stunde Bsp. 15 Min = 0.25)
* - hidden: 1
* - identifier: Zeitschrittlänge
* - type: Float
SCALAR sca_delta_ii

* - default: 0
* - domain: [0|1]
* - description: Falls gesetzt, werden automatisch spezielle Einstellungen für PVact bei der Initialisierung ergänzt. Dies betrifft aktuell Produkte und die Prozessmodellzuordnung.
* - identifier: PVact
* - type: Boolean
SCALAR sca_InGeneral_runPVAct

* - default: 0
* - domain: [0|1]
* - description: Falls gesetzt, wird die optact-Demo gestartet. Anderenfalls wird IRPact gestartet.
* - identifier: [Testoption] optact-Testmodell ausführen
* - type: Boolean
SCALAR sca_InGeneral_runOptActDemo

* - default: -1
* - description: todo
* - identifier: Ausführungsmodus
* - type: Integer
SCALAR sca_InGeneral_runMode

* - default: -1
* - description: todo
* - identifier: Spezialszenario
* - type: Integer
SCALAR sca_InGeneral_scenarioMode

* - default: 1
* - domain: [0|1]
* - description: Falls diese Option aktiviert ist, wird der Grund für einen potentiellen Fehler in den Ausgabeinformationen ausgegeben.
* - identifier: Fehleranzeige in der Ausgabe
* - type: Boolean
SCALAR sca_InGeneral_passErrorMessageToOutput

* - default: 1
* - domain: [0|1]
* - description: Falls diese Option aktiviert ist, wird bei einem potentiellen Fehler der Stacktrace als Bild ausgegeben. Dieser Ansatz ermöglicht eine sofortige Fehleranalyse im Ergebnis.
* - identifier: Stacktrace als Bild ausgeben
* - type: Boolean
SCALAR sca_InGeneral_printStacktraceImage

* - default: 1
* - domain: [0|1]
* - description: Falls diese Option aktiviert ist, wird die Nachricht 'Kein Fehler aufgetreten!' in der Ausgabe als Bild angegeben. Anderenfalls wird kein Bild erstellt.
* - identifier: Ersatzbild bei keinem Fehler erstellen
* - type: Boolean
SCALAR sca_InGeneral_printNonErrorImage

* - default: 0
* - domain: [0|1]
* - description: todo
* - identifier: Kopiere Log
* - type: Boolean
SCALAR sca_InGeneral_copyLogIfPossible

* - default: 0
* - domain: [0|1]
* - description: Deaktiviert die komplette Persistierung. Dadurch wird jedes Stützjahr als komplett neue Simulation behandelt.
* - identifier: Deaktiviere Persistierung
* - type: Boolean
SCALAR sca_InGeneral_skipPersist

* - default: 0
* - domain: [0|1]
* - description: Falls aktiviert, werden Ausgaben auch an die Konsole übergeben. (Hinweis: Dies Option hat nur Auswirkungen, falls die cmd-Option 'logPath' ohne 'logConsoleAndFile' gesetzt ist. In diesem Fall wird 'logConsoleAndFile' hinzugefügt.)
* - identifier: Erzwinge Konsolenausgabe
* - type: Boolean
SCALAR sca_InGeneral_forceLogToConsole

* - default: 0
* - description: 0: disabled, 1: OutOfMemory, 2: Stackoverflow
* - identifier: [DEV] debug task
* - type: Integer
SCALAR sca_InGeneral_debugTask

* - default: 0
* - domain: [0,)
* - description: Anzahl Threads = Paralellisierung1 * Parallelisierung2
* - identifier: Parallelisierung 1
* - type: Integer
SCALAR sca_InGeneral_outerParallelism

* - default: 0
* - domain: [0,)
* - description: Anzahl Threads = Paralellisierung1 * Parallelisierung2
* - identifier: Parallelisierung 2
* - type: Integer
SCALAR sca_InGeneral_innerParallelism

* - default: 0
* - domain: [0|1]
* - description: Erstellt eine Adoptionsanalyse.json für die programmatische Auswertung. Diese beinhaltet die kumulierten Adoptionsdaten untergliedert nach Jahr und Postleitzahl.
* - identifier: Adoptionsanalyse
* - type: Boolean
SCALAR sca_InGeneral_logAdoptionAnalysis

* - default: 0
* - domain: [0|1]
* - description: Gibt zu jedem Agenten alle Adoptionsinformationen aus. Nichtadopter werden ebenfalls ausgegeben.
* - identifier: Komplettausgabe
* - type: Boolean
SCALAR sca_InGeneral_logResultAdoptionsAll

* - default: 0
* - domain: [0|1]
* - description: Performance.xlsx
* - identifier: Performance
* - type: Boolean
SCALAR sca_InGeneral_logPerformance

* - default: 0
* - domain: [0|1]
* - description: Setzt das Logginglevel auf OFF. Achtung: Damit wird das komplette Logging deaktiviert inklusive potentieller Fehlermeldungen.
* - identifier: Level: OFF
* - rule: IF (sca_InGeneral_levelOff == 1, sca_InGeneral_levelTrace = 0)
* - rule: IF (sca_InGeneral_levelOff == 1, sca_InGeneral_levelDebug = 0)
* - rule: IF (sca_InGeneral_levelOff == 1, sca_InGeneral_levelInfo = 0)
* - rule: IF (sca_InGeneral_levelOff == 1, sca_InGeneral_levelWarn = 0)
* - rule: IF (sca_InGeneral_levelOff == 1, sca_InGeneral_levelError = 0)
* - rule: IF (sca_InGeneral_levelOff == 1, sca_InGeneral_levelAll = 0)
* - rule: IF (sca_InGeneral_levelOff == 0, sca_InGeneral_levelOff = 1)
* - rule: IF (sca_InGeneral_levelOff == 0, sca_InGeneral_levelTrace = 0)
* - rule: IF (sca_InGeneral_levelOff == 0, sca_InGeneral_levelDebug = 0)
* - rule: IF (sca_InGeneral_levelOff == 0, sca_InGeneral_levelInfo = 0)
* - rule: IF (sca_InGeneral_levelOff == 0, sca_InGeneral_levelWarn = 0)
* - rule: IF (sca_InGeneral_levelOff == 0, sca_InGeneral_levelError = 0)
* - rule: IF (sca_InGeneral_levelOff == 0, sca_InGeneral_levelAll = 0)
* - type: Boolean
SCALAR sca_InGeneral_levelOff

* - default: 0
* - domain: [0|1]
* - description: Setzt das Logginglevel auf TRACE.
* - identifier: Level: TRACE
* - rule: IF (sca_InGeneral_levelTrace == 1, sca_InGeneral_levelOff = 0)
* - rule: IF (sca_InGeneral_levelTrace == 1, sca_InGeneral_levelDebug = 0)
* - rule: IF (sca_InGeneral_levelTrace == 1, sca_InGeneral_levelInfo = 0)
* - rule: IF (sca_InGeneral_levelTrace == 1, sca_InGeneral_levelWarn = 0)
* - rule: IF (sca_InGeneral_levelTrace == 1, sca_InGeneral_levelError = 0)
* - rule: IF (sca_InGeneral_levelTrace == 1, sca_InGeneral_levelAll = 0)
* - rule: IF (sca_InGeneral_levelTrace == 0, sca_InGeneral_levelOff = 0)
* - rule: IF (sca_InGeneral_levelTrace == 0, sca_InGeneral_levelTrace = 1)
* - rule: IF (sca_InGeneral_levelTrace == 0, sca_InGeneral_levelDebug = 0)
* - rule: IF (sca_InGeneral_levelTrace == 0, sca_InGeneral_levelInfo = 0)
* - rule: IF (sca_InGeneral_levelTrace == 0, sca_InGeneral_levelWarn = 0)
* - rule: IF (sca_InGeneral_levelTrace == 0, sca_InGeneral_levelError = 0)
* - rule: IF (sca_InGeneral_levelTrace == 0, sca_InGeneral_levelAll = 0)
* - type: Boolean
SCALAR sca_InGeneral_levelTrace

* - default: 0
* - domain: [0|1]
* - description: Setzt das Logginglevel auf DEBUG.
* - identifier: Level: DEBUG
* - rule: IF (sca_InGeneral_levelDebug == 1, sca_InGeneral_levelOff = 0)
* - rule: IF (sca_InGeneral_levelDebug == 1, sca_InGeneral_levelTrace = 0)
* - rule: IF (sca_InGeneral_levelDebug == 1, sca_InGeneral_levelInfo = 0)
* - rule: IF (sca_InGeneral_levelDebug == 1, sca_InGeneral_levelWarn = 0)
* - rule: IF (sca_InGeneral_levelDebug == 1, sca_InGeneral_levelError = 0)
* - rule: IF (sca_InGeneral_levelDebug == 1, sca_InGeneral_levelAll = 0)
* - rule: IF (sca_InGeneral_levelDebug == 0, sca_InGeneral_levelOff = 0)
* - rule: IF (sca_InGeneral_levelDebug == 0, sca_InGeneral_levelTrace = 0)
* - rule: IF (sca_InGeneral_levelDebug == 0, sca_InGeneral_levelDebug = 1)
* - rule: IF (sca_InGeneral_levelDebug == 0, sca_InGeneral_levelInfo = 0)
* - rule: IF (sca_InGeneral_levelDebug == 0, sca_InGeneral_levelWarn = 0)
* - rule: IF (sca_InGeneral_levelDebug == 0, sca_InGeneral_levelError = 0)
* - rule: IF (sca_InGeneral_levelDebug == 0, sca_InGeneral_levelAll = 0)
* - type: Boolean
SCALAR sca_InGeneral_levelDebug

* - default: 1
* - domain: [0|1]
* - description: Setzt das Logginglevel auf INFO.
* - identifier: Level: INFO
* - rule: IF (sca_InGeneral_levelInfo == 1, sca_InGeneral_levelOff = 0)
* - rule: IF (sca_InGeneral_levelInfo == 1, sca_InGeneral_levelTrace = 0)
* - rule: IF (sca_InGeneral_levelInfo == 1, sca_InGeneral_levelDebug = 0)
* - rule: IF (sca_InGeneral_levelInfo == 1, sca_InGeneral_levelWarn = 0)
* - rule: IF (sca_InGeneral_levelInfo == 1, sca_InGeneral_levelError = 0)
* - rule: IF (sca_InGeneral_levelInfo == 1, sca_InGeneral_levelAll = 0)
* - rule: IF (sca_InGeneral_levelInfo == 0, sca_InGeneral_levelOff = 0)
* - rule: IF (sca_InGeneral_levelInfo == 0, sca_InGeneral_levelTrace = 0)
* - rule: IF (sca_InGeneral_levelInfo == 0, sca_InGeneral_levelDebug = 0)
* - rule: IF (sca_InGeneral_levelInfo == 0, sca_InGeneral_levelInfo = 1)
* - rule: IF (sca_InGeneral_levelInfo == 0, sca_InGeneral_levelWarn = 0)
* - rule: IF (sca_InGeneral_levelInfo == 0, sca_InGeneral_levelError = 0)
* - rule: IF (sca_InGeneral_levelInfo == 0, sca_InGeneral_levelAll = 0)
* - type: Boolean
SCALAR sca_InGeneral_levelInfo

* - default: 0
* - domain: [0|1]
* - description: Setzt das Logginglevel auf WARN.
* - identifier: Level: WARN
* - rule: IF (sca_InGeneral_levelWarn == 1, sca_InGeneral_levelOff = 0)
* - rule: IF (sca_InGeneral_levelWarn == 1, sca_InGeneral_levelTrace = 0)
* - rule: IF (sca_InGeneral_levelWarn == 1, sca_InGeneral_levelDebug = 0)
* - rule: IF (sca_InGeneral_levelWarn == 1, sca_InGeneral_levelInfo = 0)
* - rule: IF (sca_InGeneral_levelWarn == 1, sca_InGeneral_levelError = 0)
* - rule: IF (sca_InGeneral_levelWarn == 1, sca_InGeneral_levelAll = 0)
* - rule: IF (sca_InGeneral_levelWarn == 0, sca_InGeneral_levelOff = 0)
* - rule: IF (sca_InGeneral_levelWarn == 0, sca_InGeneral_levelTrace = 0)
* - rule: IF (sca_InGeneral_levelWarn == 0, sca_InGeneral_levelDebug = 0)
* - rule: IF (sca_InGeneral_levelWarn == 0, sca_InGeneral_levelInfo = 0)
* - rule: IF (sca_InGeneral_levelWarn == 0, sca_InGeneral_levelWarn = 1)
* - rule: IF (sca_InGeneral_levelWarn == 0, sca_InGeneral_levelError = 0)
* - rule: IF (sca_InGeneral_levelWarn == 0, sca_InGeneral_levelAll = 0)
* - type: Boolean
SCALAR sca_InGeneral_levelWarn

* - default: 0
* - domain: [0|1]
* - description: Setzt das Logginglevel auf ERROR.
* - identifier: Level: ERROR
* - rule: IF (sca_InGeneral_levelError == 1, sca_InGeneral_levelOff = 0)
* - rule: IF (sca_InGeneral_levelError == 1, sca_InGeneral_levelTrace = 0)
* - rule: IF (sca_InGeneral_levelError == 1, sca_InGeneral_levelDebug = 0)
* - rule: IF (sca_InGeneral_levelError == 1, sca_InGeneral_levelInfo = 0)
* - rule: IF (sca_InGeneral_levelError == 1, sca_InGeneral_levelWarn = 0)
* - rule: IF (sca_InGeneral_levelError == 1, sca_InGeneral_levelAll = 0)
* - rule: IF (sca_InGeneral_levelError == 0, sca_InGeneral_levelOff = 0)
* - rule: IF (sca_InGeneral_levelError == 0, sca_InGeneral_levelTrace = 0)
* - rule: IF (sca_InGeneral_levelError == 0, sca_InGeneral_levelDebug = 0)
* - rule: IF (sca_InGeneral_levelError == 0, sca_InGeneral_levelInfo = 0)
* - rule: IF (sca_InGeneral_levelError == 0, sca_InGeneral_levelWarn = 0)
* - rule: IF (sca_InGeneral_levelError == 0, sca_InGeneral_levelError = 1)
* - rule: IF (sca_InGeneral_levelError == 0, sca_InGeneral_levelAll = 0)
* - type: Boolean
SCALAR sca_InGeneral_levelError

* - default: 0
* - domain: [0|1]
* - description: Setzt das Logginglevel auf ALL.
* - identifier: Level: ALL
* - rule: IF (sca_InGeneral_levelAll == 1, sca_InGeneral_levelOff = 0)
* - rule: IF (sca_InGeneral_levelAll == 1, sca_InGeneral_levelTrace = 0)
* - rule: IF (sca_InGeneral_levelAll == 1, sca_InGeneral_levelDebug = 0)
* - rule: IF (sca_InGeneral_levelAll == 1, sca_InGeneral_levelInfo = 0)
* - rule: IF (sca_InGeneral_levelAll == 1, sca_InGeneral_levelWarn = 0)
* - rule: IF (sca_InGeneral_levelAll == 1, sca_InGeneral_levelError = 0)
* - rule: IF (sca_InGeneral_levelAll == 0, sca_InGeneral_levelOff = 0)
* - rule: IF (sca_InGeneral_levelAll == 0, sca_InGeneral_levelTrace = 0)
* - rule: IF (sca_InGeneral_levelAll == 0, sca_InGeneral_levelDebug = 0)
* - rule: IF (sca_InGeneral_levelAll == 0, sca_InGeneral_levelInfo = 0)
* - rule: IF (sca_InGeneral_levelAll == 0, sca_InGeneral_levelWarn = 0)
* - rule: IF (sca_InGeneral_levelAll == 0, sca_InGeneral_levelError = 0)
* - rule: IF (sca_InGeneral_levelAll == 0, sca_InGeneral_levelAll = 1)
* - type: Boolean
SCALAR sca_InGeneral_levelAll

* - default: 0
* - domain: [0|1]
* - description: Aktiviert alle log-Operationen in allen Komponenten.
* - identifier: Alles loggen?
* - type: Boolean
SCALAR sca_InGeneral_logAll

* - default: 0
* - domain: [0|1]
* - description: Aktiviert alle log-Operationen von IRPact.
* - identifier: IRPact loggen?
* - type: Boolean
SCALAR sca_InGeneral_logAllIRPact

* - default: 0
* - domain: [0|1]
* - description: Aktiviert alle log-Operationen von IRPtools.
* - identifier: IRPtools-Bibliothek loggen?
* - type: Boolean
SCALAR sca_InGeneral_logAllTools

* - default: 0
* - domain: [0|1]
* - description: Aktiviert das Logging des Initialisierungsprozesses. Dieser umfasst alles vom Start bis zum eigentlichen Start der Simulation.
* - identifier: Initialisierung loggen?
* - type: Boolean
SCALAR sca_InGeneral_logInitialization

* - default: 0
* - domain: [0|1]
* - description: Aktiviert das Logging des Simulationsprozesses.
* - identifier: Simulationsprozess loggen?
* - type: Boolean
SCALAR sca_InGeneral_logSimulation

* - default: 0
* - domain: [0|1]
* - description: Phasenuebersicht.xlsx
* - identifier: Phasenübersicht
* - type: Boolean
SCALAR sca_InGeneral_logPhaseOverview

* - default: 0
* - domain: [0|1]
* - description: Interesse.xlsx
* - identifier: Interessensentwicklung
* - type: Boolean
SCALAR sca_InGeneral_logInterest

* - default: 0
* - domain: [0|1]
* - description: Evaluierung.xlsx
* - identifier: Evaluierungsdaten
* - type: Boolean
SCALAR sca_InGeneral_logEvaluation

* - default: 0.1
* - description: Bestimmt die Bereichsgrößen bei den Evaluierungsdaten.
* - identifier: Bereichsgröße (Evaluierungswerte)
* - type: Float
SCALAR sca_InGeneral_evaluationBucketSize

* - identifier: InInformation
* - type: String
SET set_InInformation(*)

* - default: 0.0
* - description: Ungenutzter Platzhalter
* - identifier: ---
* - type: Float
PARAMETER par_InInformation_placeholder(set_InInformation)

* - identifier: InIRPactVersion
* - type: String
SET set_InIRPactVersion(*)

* - default: 0
* - description: Ungenutzter Platzhalter
* - identifier: ---
* - type: Integer
PARAMETER par_InIRPactVersion_placeholder(set_InIRPactVersion)

* - identifier: InScenarioVersion
* - type: String
SET set_InScenarioVersion(*)

* - default: 0
* - description: Ungenutzter Platzhalter
* - identifier: ---
* - type: Integer
PARAMETER par_InScenarioVersion_placeholder(set_InScenarioVersion)

* - identifier: InTestData
* - type: String
SET set_InTestData(*)

* - description: Testwert 1
* - identifier: Testwert 1
* - type: Float
PARAMETER par_InTestData_testValue1(set_InTestData)

* - description: Testwert 2
* - identifier: Testwert 2
* - type: Float
PARAMETER par_InTestData_testValue2(set_InTestData)

* - default: 1
* - domain: [0|1]
* - description: xor test
* - identifier: g0 v0
* - rule: IF (par_InTestData_value01 == 1, par_InTestData_value02 = 0)
* - rule: IF (par_InTestData_value01 == 0, par_InTestData_value01 = 1)
* - rule: IF (par_InTestData_value01 == 0, par_InTestData_value02 = 0)
* - type: Boolean
PARAMETER par_InTestData_value01(set_InTestData)

* - default: 0
* - domain: [0|1]
* - description: xor test
* - identifier: g0 v1
* - rule: IF (par_InTestData_value02 == 1, par_InTestData_value01 = 0)
* - rule: IF (par_InTestData_value02 == 0, par_InTestData_value01 = 0)
* - rule: IF (par_InTestData_value02 == 0, par_InTestData_value02 = 1)
* - type: Boolean
PARAMETER par_InTestData_value02(set_InTestData)

* - default: 1
* - domain: [0|1]
* - description: default
* - identifier: g1 v0 (default)
* - type: Boolean
PARAMETER par_InTestData_value11(set_InTestData)

* - default: 0
* - domain: [0|1]
* - description: default
* - identifier: g1 v1
* - type: Boolean
PARAMETER par_InTestData_value12(set_InTestData)

* - default: 0
* - domain: [0|1]
* - description: default
* - identifier: g1 v2
* - type: Boolean
PARAMETER par_InTestData_value13(set_InTestData)

* - default: 0
* - domain: [0|1]
* - description: default
* - identifier: g1 v3
* - type: Boolean
PARAMETER par_InTestData_value14(set_InTestData)

* - default: 1
* - domain: [0|1]
* - description: unselectable
* - identifier: g2 v0
* - rule: IF (par_InTestData_value21 == 1, par_InTestData_value22 = 0)
* - rule: IF (par_InTestData_value21 == 1, par_InTestData_value23 = 0)
* - rule: IF (par_InTestData_value21 == 1, par_InTestData_value24 = 0)
* - rule: IF (par_InTestData_value21 == 1, par_InTestData_value25 = 0)
* - rule: IF (par_InTestData_value21 == 1, par_InTestData_value26 = 0)
* - rule: IF (par_InTestData_value21 == 1, par_InTestData_value27 = 0)
* - rule: IF (par_InTestData_value21 == 0, par_InTestData_value21 = 1)
* - rule: IF (par_InTestData_value21 == 0, par_InTestData_value22 = 0)
* - rule: IF (par_InTestData_value21 == 0, par_InTestData_value23 = 0)
* - rule: IF (par_InTestData_value21 == 0, par_InTestData_value24 = 0)
* - rule: IF (par_InTestData_value21 == 0, par_InTestData_value25 = 0)
* - rule: IF (par_InTestData_value21 == 0, par_InTestData_value26 = 0)
* - rule: IF (par_InTestData_value21 == 0, par_InTestData_value27 = 0)
* - type: Boolean
PARAMETER par_InTestData_value21(set_InTestData)

* - default: 0
* - domain: [0|1]
* - description: unselectable
* - identifier: g2 v1
* - rule: IF (par_InTestData_value22 == 1, par_InTestData_value21 = 0)
* - rule: IF (par_InTestData_value22 == 1, par_InTestData_value23 = 0)
* - rule: IF (par_InTestData_value22 == 1, par_InTestData_value24 = 0)
* - rule: IF (par_InTestData_value22 == 1, par_InTestData_value25 = 0)
* - rule: IF (par_InTestData_value22 == 1, par_InTestData_value26 = 0)
* - rule: IF (par_InTestData_value22 == 1, par_InTestData_value27 = 0)
* - rule: IF (par_InTestData_value22 == 0, par_InTestData_value21 = 0)
* - rule: IF (par_InTestData_value22 == 0, par_InTestData_value22 = 1)
* - rule: IF (par_InTestData_value22 == 0, par_InTestData_value23 = 0)
* - rule: IF (par_InTestData_value22 == 0, par_InTestData_value24 = 0)
* - rule: IF (par_InTestData_value22 == 0, par_InTestData_value25 = 0)
* - rule: IF (par_InTestData_value22 == 0, par_InTestData_value26 = 0)
* - rule: IF (par_InTestData_value22 == 0, par_InTestData_value27 = 0)
* - type: Boolean
PARAMETER par_InTestData_value22(set_InTestData)

* - default: 0
* - domain: [0|1]
* - description: unselectable
* - identifier: g2 v2
* - rule: IF (par_InTestData_value23 == 1, par_InTestData_value21 = 0)
* - rule: IF (par_InTestData_value23 == 1, par_InTestData_value22 = 0)
* - rule: IF (par_InTestData_value23 == 1, par_InTestData_value24 = 0)
* - rule: IF (par_InTestData_value23 == 1, par_InTestData_value25 = 0)
* - rule: IF (par_InTestData_value23 == 1, par_InTestData_value26 = 0)
* - rule: IF (par_InTestData_value23 == 1, par_InTestData_value27 = 0)
* - rule: IF (par_InTestData_value23 == 0, par_InTestData_value21 = 0)
* - rule: IF (par_InTestData_value23 == 0, par_InTestData_value22 = 0)
* - rule: IF (par_InTestData_value23 == 0, par_InTestData_value23 = 1)
* - rule: IF (par_InTestData_value23 == 0, par_InTestData_value24 = 0)
* - rule: IF (par_InTestData_value23 == 0, par_InTestData_value25 = 0)
* - rule: IF (par_InTestData_value23 == 0, par_InTestData_value26 = 0)
* - rule: IF (par_InTestData_value23 == 0, par_InTestData_value27 = 0)
* - type: Boolean
PARAMETER par_InTestData_value23(set_InTestData)

* - default: 0
* - domain: [0|1]
* - description: unselectable
* - identifier: g2 v3
* - rule: IF (par_InTestData_value24 == 1, par_InTestData_value21 = 0)
* - rule: IF (par_InTestData_value24 == 1, par_InTestData_value22 = 0)
* - rule: IF (par_InTestData_value24 == 1, par_InTestData_value23 = 0)
* - rule: IF (par_InTestData_value24 == 1, par_InTestData_value25 = 0)
* - rule: IF (par_InTestData_value24 == 1, par_InTestData_value26 = 0)
* - rule: IF (par_InTestData_value24 == 1, par_InTestData_value27 = 0)
* - rule: IF (par_InTestData_value24 == 0, par_InTestData_value21 = 0)
* - rule: IF (par_InTestData_value24 == 0, par_InTestData_value22 = 0)
* - rule: IF (par_InTestData_value24 == 0, par_InTestData_value23 = 0)
* - rule: IF (par_InTestData_value24 == 0, par_InTestData_value24 = 1)
* - rule: IF (par_InTestData_value24 == 0, par_InTestData_value25 = 0)
* - rule: IF (par_InTestData_value24 == 0, par_InTestData_value26 = 0)
* - rule: IF (par_InTestData_value24 == 0, par_InTestData_value27 = 0)
* - type: Boolean
PARAMETER par_InTestData_value24(set_InTestData)

* - default: 0
* - domain: [0|1]
* - description: unselectable
* - identifier: g2 v4
* - rule: IF (par_InTestData_value25 == 1, par_InTestData_value21 = 0)
* - rule: IF (par_InTestData_value25 == 1, par_InTestData_value22 = 0)
* - rule: IF (par_InTestData_value25 == 1, par_InTestData_value23 = 0)
* - rule: IF (par_InTestData_value25 == 1, par_InTestData_value24 = 0)
* - rule: IF (par_InTestData_value25 == 1, par_InTestData_value26 = 0)
* - rule: IF (par_InTestData_value25 == 1, par_InTestData_value27 = 0)
* - rule: IF (par_InTestData_value25 == 0, par_InTestData_value21 = 0)
* - rule: IF (par_InTestData_value25 == 0, par_InTestData_value22 = 0)
* - rule: IF (par_InTestData_value25 == 0, par_InTestData_value23 = 0)
* - rule: IF (par_InTestData_value25 == 0, par_InTestData_value24 = 0)
* - rule: IF (par_InTestData_value25 == 0, par_InTestData_value25 = 1)
* - rule: IF (par_InTestData_value25 == 0, par_InTestData_value26 = 0)
* - rule: IF (par_InTestData_value25 == 0, par_InTestData_value27 = 0)
* - type: Boolean
PARAMETER par_InTestData_value25(set_InTestData)

* - default: 0
* - domain: [0|1]
* - description: unselectable
* - identifier: g2 v5
* - rule: IF (par_InTestData_value26 == 1, par_InTestData_value21 = 0)
* - rule: IF (par_InTestData_value26 == 1, par_InTestData_value22 = 0)
* - rule: IF (par_InTestData_value26 == 1, par_InTestData_value23 = 0)
* - rule: IF (par_InTestData_value26 == 1, par_InTestData_value24 = 0)
* - rule: IF (par_InTestData_value26 == 1, par_InTestData_value25 = 0)
* - rule: IF (par_InTestData_value26 == 1, par_InTestData_value27 = 0)
* - rule: IF (par_InTestData_value26 == 0, par_InTestData_value21 = 0)
* - rule: IF (par_InTestData_value26 == 0, par_InTestData_value22 = 0)
* - rule: IF (par_InTestData_value26 == 0, par_InTestData_value23 = 0)
* - rule: IF (par_InTestData_value26 == 0, par_InTestData_value24 = 0)
* - rule: IF (par_InTestData_value26 == 0, par_InTestData_value25 = 0)
* - rule: IF (par_InTestData_value26 == 0, par_InTestData_value26 = 1)
* - rule: IF (par_InTestData_value26 == 0, par_InTestData_value27 = 0)
* - type: Boolean
PARAMETER par_InTestData_value26(set_InTestData)

* - default: 0
* - domain: [0|1]
* - description: unselectable
* - identifier: g2 v6
* - rule: IF (par_InTestData_value27 == 1, par_InTestData_value21 = 0)
* - rule: IF (par_InTestData_value27 == 1, par_InTestData_value22 = 0)
* - rule: IF (par_InTestData_value27 == 1, par_InTestData_value23 = 0)
* - rule: IF (par_InTestData_value27 == 1, par_InTestData_value24 = 0)
* - rule: IF (par_InTestData_value27 == 1, par_InTestData_value25 = 0)
* - rule: IF (par_InTestData_value27 == 1, par_InTestData_value26 = 0)
* - rule: IF (par_InTestData_value27 == 0, par_InTestData_value21 = 0)
* - rule: IF (par_InTestData_value27 == 0, par_InTestData_value22 = 0)
* - rule: IF (par_InTestData_value27 == 0, par_InTestData_value23 = 0)
* - rule: IF (par_InTestData_value27 == 0, par_InTestData_value24 = 0)
* - rule: IF (par_InTestData_value27 == 0, par_InTestData_value25 = 0)
* - rule: IF (par_InTestData_value27 == 0, par_InTestData_value26 = 0)
* - rule: IF (par_InTestData_value27 == 0, par_InTestData_value27 = 1)
* - type: Boolean
PARAMETER par_InTestData_value27(set_InTestData)

* - default: 0.0
* - description: test für sensitivität
* - identifier: sensi 1
* - type: Float
PARAMETER par_InTestData_sensi1(set_InTestData)

* - default: 0.0
* - description: test für sensitivität
* - identifier: sensi 2
* - type: Float
PARAMETER par_InTestData_sensi2(set_InTestData)

* - default: 0.0
* - description: test für sensitivität
* - identifier: sensi 3
* - type: Float
PARAMETER par_InTestData_sensi3(set_InTestData)

* - description: Einlesen des zu optimierenden Jahres
* - hidden: 1
* - identifier: Jahreszahl
* - type: Integer
SET set_a(*)

* - description: Einlesen der zu optimierenden Jahre
* - hidden: 1
* - identifier: Jahreszahl
* - type: Integer
SET set_a_total(*)

* - description: Bitte geben Sie hier das absolute jährliche Wachstum der Kundengruppe an
* - identifier: Absolutes Wachstum der Kundengruppe pro Jahr
* - type: Float
PARAMETER par_S_DS_growth_absolute(set_a_total,set_side_cust)

* - description: Einlesen des Optimierungshorizonts
* - hidden: 1
* - identifier: Simulationshorizont
* - type: TimeSeries
SET set_ii(set_ii_0)

* - description: Einlesen des Optimierungshorizonts
* - hidden: 1
* - identifier: Simulationshorizont
* - type: TimeSeries
SET set_ii_0(*)

* - description: Einlesen der Monate
* - hidden: 1
* - identifier: Monate
* - type: String
SET set_jj(*)

* - description: Einlesen des Initialisierungszeitpunkt des Optimierungshorizonts
* - hidden: 1
* - identifier: Initialisierungszeitpunkt des Optimierungshorizonts
* - type: String
SET set_optinitial(set_optstore)

* - description: Einlesen des Optimierungszeitpunkte
* - hidden: 1
* - identifier: Speicherhorizont
* - type: String
SET set_optsteps(set_ii)

* - description: Einlesen des Speicherhorizonts
* - hidden: 1
* - identifier: Speicherhorizont
* - type: TimeSeries
SET set_optstore(set_ii)

* - description: Marktteilnehmer
* - hidden: 1
* - identifier: MT
* - type: String
SET set_side(*)

* - description: Kundengruppe
* - identifier: Kundengruppe
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

* - description: Einlesen des Optimierungshorizonts
* - hidden: 1
* - identifier: Simulationshorizont
* - type: TimeSeries
SET set_t(set_ii)

* - identifier: OutInformation
* - type: String
SET set_OutInformation(*)

* - identifier: OutConsumerAgentGroup
* - type: String
SET set_OutConsumerAgentGroup(*)

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
* - identifier: AgentGroup_numberOfAgents
* - type: Integer
PARAMETER par_AgentGroup_numberOfAgents(set_AgentGroup)

* - description: Farbe, welche diese Gruppe im Graphen haben soll. Wichtig: es wird nur der erste Wert verwendet! Falls keine Farbe gewählt wird, ist die Farbe schwarz.
* - identifier: AgentGroup_agentColor
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
* - identifier: IWattsStrogatzModel_wsmK
* - type: Integer
PARAMETER par_IWattsStrogatzModel_wsmK(set_IWattsStrogatzModel)

* - description: IWattsStrogatzModel_wsmBeta
* - identifier: IWattsStrogatzModel_wsmBeta
* - type: Float
PARAMETER par_IWattsStrogatzModel_wsmBeta(set_IWattsStrogatzModel)

* - description: IWattsStrogatzModel_wsmSelfReferential
* - identifier: IWattsStrogatzModel_wsmSelfReferential
* - type: Boolean
PARAMETER par_IWattsStrogatzModel_wsmSelfReferential(set_IWattsStrogatzModel)

* - description: IWattsStrogatzModel_wsmSeed
* - identifier: IWattsStrogatzModel_wsmSeed
* - type: Integer
PARAMETER par_IWattsStrogatzModel_wsmSeed(set_IWattsStrogatzModel)

* - description: Soll diese Topology verwendet werden? Hinweis: es wird nur die erste gültige genutzt! Der Rest wird ignoriert!
* - identifier: IWattsStrogatzModel_wsmUseThis
* - type: Boolean
PARAMETER par_IWattsStrogatzModel_wsmUseThis(set_IWattsStrogatzModel)

* - identifier: IFreeMultiGraphTopology
* - type: String
SET set_IFreeMultiGraphTopology(set_IGraphTopology)

* - description: IFreeMultiGraphTopology_ftEdgeCount
* - identifier: IFreeMultiGraphTopology_ftEdgeCount
* - type: Integer
PARAMETER par_IFreeMultiGraphTopology_ftEdgeCount(set_IFreeMultiGraphTopology)

* - description: IFreeMultiGraphTopology_ftSelfReferential
* - identifier: IFreeMultiGraphTopology_ftSelfReferential
* - type: Boolean
PARAMETER par_IFreeMultiGraphTopology_ftSelfReferential(set_IFreeMultiGraphTopology)

* - description: IFreeMultiGraphTopology_ftSeed
* - identifier: IFreeMultiGraphTopology_ftSeed
* - type: Integer
PARAMETER par_IFreeMultiGraphTopology_ftSeed(set_IFreeMultiGraphTopology)

* - description: Soll diese Topology verwendet werden? Hinweis: es wird nur die erste gültige genutzt! Der Rest wird ignoriert!
* - identifier: IFreeMultiGraphTopology_ftUseThis
* - type: Boolean
PARAMETER par_IFreeMultiGraphTopology_ftUseThis(set_IFreeMultiGraphTopology)

* - identifier: GraphvizColor
* - type: String
SET set_GraphvizColor(*)

* - description: RGBA Code der Farbe im Dezimalsystem.
* - identifier: RGBA-Wert
* - type: Integer
PARAMETER par_GraphvizColor_rgba(set_GraphvizColor)
