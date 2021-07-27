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

* - description: Einzigartige ID der Daten.
* - hidden: 1
* - identifier: BinaryPersistDataID
* - type: Integer
PARAMETER par_BinaryPersistData_id(set_BinaryPersistData)

* - identifier: InBernoulliDistribution
* - type: String
SET set_InBernoulliDistribution(set_InUnivariateDoubleDistribution)

* - domain: [0|1]
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

* - identifier: InBoundedUniformDoubleDistribution
* - type: String
SET set_InBoundedUniformDoubleDistribution(set_InUnivariateDoubleDistribution)

* - description: Legt die Untergrenze fest.
* - identifier: Untergrenze (inklusiv)
* - type: Float
PARAMETER par_InBoundedUniformDoubleDistribution_lowerBound(set_InBoundedUniformDoubleDistribution)

* - description: Legt die Obergrenze fest.
* - identifier: Obergrenze (exklusiv)
* - type: Float
PARAMETER par_InBoundedUniformDoubleDistribution_upperBound(set_InBoundedUniformDoubleDistribution)

* - identifier: InBoundedUniformIntegerDistribution
* - type: String
SET set_InBoundedUniformIntegerDistribution(set_InUnivariateDoubleDistribution)

* - description: Legt die Untergrenze fest.
* - identifier: Untergrenze (inklusiv)
* - type: Integer
PARAMETER par_InBoundedUniformIntegerDistribution_lowerBound(set_InBoundedUniformIntegerDistribution)

* - description: Legt die Obergrenze fest.
* - identifier: Obergrenze (exklusiv)
* - type: Integer
PARAMETER par_InBoundedUniformIntegerDistribution_upperBound(set_InBoundedUniformIntegerDistribution)

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

* - identifier: InSpatialTableFile
* - type: String
SET set_InSpatialTableFile(set_InFile)

* - description: Ungenutzter Platzhalter
* - identifier: ---
* - type: Float
PARAMETER par_InSpatialTableFile_placeholderInSpatialFile(set_InSpatialTableFile)

* - default: JaehrlicheAdoptionenPLZ, JaehrlicheAdoptionenPLZVergleich, JaehrlicheAdoptionenPhase
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
* - rule: IF (par_InGenericOutputImage_annualZip == 1, par_InGenericOutputImage_cumulativeAnnualPhase = 0)
* - rule: IF (par_InGenericOutputImage_annualZip == 0, par_InGenericOutputImage_annualZip = 1)
* - rule: IF (par_InGenericOutputImage_annualZip == 0, par_InGenericOutputImage_annualZipWithReal = 0)
* - rule: IF (par_InGenericOutputImage_annualZip == 0, par_InGenericOutputImage_cumulativeAnnualPhase = 0)
* - type: Boolean
PARAMETER par_InGenericOutputImage_annualZip(set_InGenericOutputImage)

* - default: 0
* - domain: [0|1]
* - description: Stellt die jährlichen Adoptionen, aufgeschlüsselt nach PLZ, in einer line chart dar. Zusätzlich werden die realen Adoptionen mit dargestellt.
* - identifier: Darstellung: jährliche Adoptionen im Vergleich zu realen Daten (PLZ)
* - rule: IF (par_InGenericOutputImage_annualZipWithReal == 1, par_InGenericOutputImage_annualZip = 0)
* - rule: IF (par_InGenericOutputImage_annualZipWithReal == 1, par_InGenericOutputImage_cumulativeAnnualPhase = 0)
* - rule: IF (par_InGenericOutputImage_annualZipWithReal == 0, par_InGenericOutputImage_annualZip = 0)
* - rule: IF (par_InGenericOutputImage_annualZipWithReal == 0, par_InGenericOutputImage_annualZipWithReal = 1)
* - rule: IF (par_InGenericOutputImage_annualZipWithReal == 0, par_InGenericOutputImage_cumulativeAnnualPhase = 0)
* - type: Boolean
PARAMETER par_InGenericOutputImage_annualZipWithReal(set_InGenericOutputImage)

* - default: 0
* - domain: [0|1]
* - description: Stellt die kumulierten jährlichen Adoptionen, aufgeschlüsselt nach der Adoptionsphase, in einer stacked bar chart dar.
* - identifier: Darstellung: kumulierte jährliche Adoptionen (Phase)
* - rule: IF (par_InGenericOutputImage_cumulativeAnnualPhase == 1, par_InGenericOutputImage_annualZip = 0)
* - rule: IF (par_InGenericOutputImage_cumulativeAnnualPhase == 1, par_InGenericOutputImage_annualZipWithReal = 0)
* - rule: IF (par_InGenericOutputImage_cumulativeAnnualPhase == 0, par_InGenericOutputImage_annualZip = 0)
* - rule: IF (par_InGenericOutputImage_cumulativeAnnualPhase == 0, par_InGenericOutputImage_annualZipWithReal = 0)
* - rule: IF (par_InGenericOutputImage_cumulativeAnnualPhase == 0, par_InGenericOutputImage_cumulativeAnnualPhase = 1)
* - type: Boolean
PARAMETER par_InGenericOutputImage_cumulativeAnnualPhase(set_InGenericOutputImage)

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

* - default: 1
* - domain: (0,)
* - description: Gibt die Linienstärke an. Dieser Wert hat nur auf line plots Einfluss.
* - identifier: Linienstärke
* - type: Float
PARAMETER par_InGenericOutputImage_linewidth(set_InGenericOutputImage)

* - identifier: InGnuPlotOutputImage
* - type: String
SET set_InGnuPlotOutputImage(set_InOutputImage)

* - default: 1
* - domain: [0|1]
* - description: Stellt die jährlichen Adoptionen, aufgeschlüsselt nach PLZ, in einer line chart dar.
* - identifier: Darstellung: jährliche Adoptionen (PLZ)
* - rule: IF (par_InGnuPlotOutputImage_annualZip == 1, par_InGnuPlotOutputImage_annualZipWithReal = 0)
* - rule: IF (par_InGnuPlotOutputImage_annualZip == 1, par_InGnuPlotOutputImage_cumulativeAnnualPhase = 0)
* - rule: IF (par_InGnuPlotOutputImage_annualZip == 0, par_InGnuPlotOutputImage_annualZip = 1)
* - rule: IF (par_InGnuPlotOutputImage_annualZip == 0, par_InGnuPlotOutputImage_annualZipWithReal = 0)
* - rule: IF (par_InGnuPlotOutputImage_annualZip == 0, par_InGnuPlotOutputImage_cumulativeAnnualPhase = 0)
* - type: Boolean
PARAMETER par_InGnuPlotOutputImage_annualZip(set_InGnuPlotOutputImage)

* - default: 0
* - domain: [0|1]
* - description: Stellt die jährlichen Adoptionen, aufgeschlüsselt nach PLZ, in einer line chart dar. Zusätzlich werden die realen Adoptionen mit dargestellt.
* - identifier: Darstellung: jährliche Adoptionen im Vergleich zu realen Daten (PLZ)
* - rule: IF (par_InGnuPlotOutputImage_annualZipWithReal == 1, par_InGnuPlotOutputImage_annualZip = 0)
* - rule: IF (par_InGnuPlotOutputImage_annualZipWithReal == 1, par_InGnuPlotOutputImage_cumulativeAnnualPhase = 0)
* - rule: IF (par_InGnuPlotOutputImage_annualZipWithReal == 0, par_InGnuPlotOutputImage_annualZip = 0)
* - rule: IF (par_InGnuPlotOutputImage_annualZipWithReal == 0, par_InGnuPlotOutputImage_annualZipWithReal = 1)
* - rule: IF (par_InGnuPlotOutputImage_annualZipWithReal == 0, par_InGnuPlotOutputImage_cumulativeAnnualPhase = 0)
* - type: Boolean
PARAMETER par_InGnuPlotOutputImage_annualZipWithReal(set_InGnuPlotOutputImage)

* - default: 0
* - domain: [0|1]
* - description: Stellt die kumulierten jährlichen Adoptionen, aufgeschlüsselt nach der Adoptionsphase, in einer stacked bar chart dar.
* - identifier: Darstellung: kumulierte jährliche Adoptionen (Phase)
* - rule: IF (par_InGnuPlotOutputImage_cumulativeAnnualPhase == 1, par_InGnuPlotOutputImage_annualZip = 0)
* - rule: IF (par_InGnuPlotOutputImage_cumulativeAnnualPhase == 1, par_InGnuPlotOutputImage_annualZipWithReal = 0)
* - rule: IF (par_InGnuPlotOutputImage_cumulativeAnnualPhase == 0, par_InGnuPlotOutputImage_annualZip = 0)
* - rule: IF (par_InGnuPlotOutputImage_cumulativeAnnualPhase == 0, par_InGnuPlotOutputImage_annualZipWithReal = 0)
* - rule: IF (par_InGnuPlotOutputImage_cumulativeAnnualPhase == 0, par_InGnuPlotOutputImage_cumulativeAnnualPhase = 1)
* - type: Boolean
PARAMETER par_InGnuPlotOutputImage_cumulativeAnnualPhase(set_InGnuPlotOutputImage)

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

* - default: 1
* - domain: (0,)
* - description: Gibt die Linienstärke an. Dieser Wert hat nur auf line plots Einfluss.
* - identifier: Linienstärke
* - type: Float
PARAMETER par_InGnuPlotOutputImage_linewidth(set_InGnuPlotOutputImage)

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
* - rule: IF (par_InROutputImage_annualZip == 1, par_InROutputImage_cumulativeAnnualPhase = 0)
* - rule: IF (par_InROutputImage_annualZip == 0, par_InROutputImage_annualZip = 1)
* - rule: IF (par_InROutputImage_annualZip == 0, par_InROutputImage_annualZipWithReal = 0)
* - rule: IF (par_InROutputImage_annualZip == 0, par_InROutputImage_cumulativeAnnualPhase = 0)
* - type: Boolean
PARAMETER par_InROutputImage_annualZip(set_InROutputImage)

* - default: 0
* - domain: [0|1]
* - description: Stellt die jährlichen Adoptionen, aufgeschlüsselt nach PLZ, in einer line chart dar. Zusätzlich werden die realen Adoptionen mit dargestellt.
* - identifier: Darstellung: jährliche Adoptionen im Vergleich zu realen Daten (PLZ)
* - rule: IF (par_InROutputImage_annualZipWithReal == 1, par_InROutputImage_annualZip = 0)
* - rule: IF (par_InROutputImage_annualZipWithReal == 1, par_InROutputImage_cumulativeAnnualPhase = 0)
* - rule: IF (par_InROutputImage_annualZipWithReal == 0, par_InROutputImage_annualZip = 0)
* - rule: IF (par_InROutputImage_annualZipWithReal == 0, par_InROutputImage_annualZipWithReal = 1)
* - rule: IF (par_InROutputImage_annualZipWithReal == 0, par_InROutputImage_cumulativeAnnualPhase = 0)
* - type: Boolean
PARAMETER par_InROutputImage_annualZipWithReal(set_InROutputImage)

* - default: 0
* - domain: [0|1]
* - description: Stellt die kumulierten jährlichen Adoptionen, aufgeschlüsselt nach der Adoptionsphase, in einer stacked bar chart dar.
* - identifier: Darstellung: kumulierte jährliche Adoptionen (Phase)
* - rule: IF (par_InROutputImage_cumulativeAnnualPhase == 1, par_InROutputImage_annualZip = 0)
* - rule: IF (par_InROutputImage_cumulativeAnnualPhase == 1, par_InROutputImage_annualZipWithReal = 0)
* - rule: IF (par_InROutputImage_cumulativeAnnualPhase == 0, par_InROutputImage_annualZip = 0)
* - rule: IF (par_InROutputImage_cumulativeAnnualPhase == 0, par_InROutputImage_annualZipWithReal = 0)
* - rule: IF (par_InROutputImage_cumulativeAnnualPhase == 0, par_InROutputImage_cumulativeAnnualPhase = 1)
* - type: Boolean
PARAMETER par_InROutputImage_cumulativeAnnualPhase(set_InROutputImage)

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

* - default: 1
* - domain: (0,)
* - description: Gibt die Linienstärke an. Dieser Wert hat nur auf line plots Einfluss.
* - identifier: Linienstärke
* - type: Float
PARAMETER par_InROutputImage_linewidth(set_InROutputImage)

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

* - identifier: InCompleteGraphTopology
* - type: String
SET set_InCompleteGraphTopology(set_InGraphTopologyScheme)

* - description: Legt das initiale Gewicht der Kanten fest.
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

* - description: Legt das initiale Gewicht der Kanten fest.
* - hidden: 1
* - identifier: Initiales Kantengewicht
* - type: Float
PARAMETER par_InFreeNetworkTopology_initialWeight(set_InFreeNetworkTopology)

* - description: Erlaubt es auch weniger Kanten als gewünscht zu nutzen. Sollten zum Beispiel 10 Kanten gewünscht sein je Knoten, aber für einen bestimmten Knoten nur 5 möglich sein, so werden nur 5 verwendet. Falls diese Option deaktiviert ist, wird ein Fehler geworfen.
* - identifier: Weniger Kanten erlauben?
* - type: Boolean
PARAMETER par_InFreeNetworkTopology_allowLessEdges(set_InFreeNetworkTopology)

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
SET set_InUnlinkedGraphTopology(set_InGraphTopologyScheme)

* - description: Ungenutzter Platzhalter
* - identifier: ---
* - type: Float
PARAMETER par_InUnlinkedGraphTopology_placeholderUnlinked(set_InUnlinkedGraphTopology)

* - identifier: InGlobalDeffuantUncertainty
* - type: String
SET set_InGlobalDeffuantUncertainty(set_InUncertainty)

* - default: -1
* - description: Legt den Extremistenparameter fest. Dieser Wert beschreibt den prozentualen Anteil der Exremisten.
* - identifier: Extremistenparameter
* - type: Float
PARAMETER par_InGlobalDeffuantUncertainty_extremistParameter(set_InGlobalDeffuantUncertainty)

* - default: 0.35
* - description: Legt den Wert der Unsicherheit für Extremisten fest.
* - identifier: Unsicherheit der Extremisten
* - type: Float
PARAMETER par_InGlobalDeffuantUncertainty_extremistUncertainty(set_InGlobalDeffuantUncertainty)

* - default: 1.4
* - description: Legt den Wert der Unsicherheit für Moderate fest.
* - identifier: Unsicherheit der Moderaten
* - type: Float
PARAMETER par_InGlobalDeffuantUncertainty_moderateUncertainty(set_InGlobalDeffuantUncertainty)

* - default: 0
* - description: Legt fest, ob der untere Grenzwert inklusiv (>=) oder exklusiv (>) ist.
* - identifier: Untere Grenze inklusive?
* - type: Boolean
PARAMETER par_InGlobalDeffuantUncertainty_lowerBoundInclusive(set_InGlobalDeffuantUncertainty)

* - default: 0
* - description: Legt fest, ob der obere Grenzwert inklusiv (<=) oder exklusiv (<) ist.
* - identifier: Obere Grenze inklusive?
* - type: Boolean
PARAMETER par_InGlobalDeffuantUncertainty_upperBoundInclusive(set_InGlobalDeffuantUncertainty)

* - description: Legt die Agentengruppen fest, auf deren Basis die Unsicherheit berechnet werden soll.
* - identifier: Betrachtete Agentengruppen
* - type: Boolean
PARAMETER par_link_InGlobalDeffuantUncertainty_InConsumerAgentGroup_cags(set_InGlobalDeffuantUncertainty,set_InConsumerAgentGroup)

* - description: Legt die Attribute fest, die mit dieser Unsicherheit verbunden werden sollen.
* - identifier: Betrachtete Attribute
* - type: Boolean
PARAMETER par_link_InGlobalDeffuantUncertainty_InAttributeName_attributeNames(set_InGlobalDeffuantUncertainty,set_InAttributeName)

* - identifier: InGroupBasedDeffuantUncertainty
* - type: String
SET set_InGroupBasedDeffuantUncertainty(set_InUncertainty)

* - default: -1
* - description: Legt den Extremistenparameter fest. Dieser Wert beschreibt den prozentualen Anteil der Exremisten.
* - identifier: Extremistenparameter
* - type: Float
PARAMETER par_InGroupBasedDeffuantUncertainty_extremistParameter(set_InGroupBasedDeffuantUncertainty)

* - default: 0.35
* - description: Legt den Wert der Unsicherheit für Extremisten fest.
* - identifier: Unsicherheit der Extremisten
* - type: Float
PARAMETER par_InGroupBasedDeffuantUncertainty_extremistUncertainty(set_InGroupBasedDeffuantUncertainty)

* - default: 1.4
* - description: Legt den Wert der Unsicherheit für Moderate fest.
* - identifier: Unsicherheit der Moderaten
* - type: Float
PARAMETER par_InGroupBasedDeffuantUncertainty_moderateUncertainty(set_InGroupBasedDeffuantUncertainty)

* - default: 0
* - description: Legt fest, ob der untere Grenzwert inklusiv (>=) oder exklusiv (>) ist.
* - identifier: Untere Grenze inklusive?
* - type: Boolean
PARAMETER par_InGroupBasedDeffuantUncertainty_lowerBoundInclusive(set_InGroupBasedDeffuantUncertainty)

* - default: 0
* - description: Legt fest, ob der obere Grenzwert inklusiv (<=) oder exklusiv (<) ist.
* - identifier: Obere Grenze inklusive?
* - type: Boolean
PARAMETER par_InGroupBasedDeffuantUncertainty_upperBoundInclusive(set_InGroupBasedDeffuantUncertainty)

* - description: Legt die Agentengruppe fest, auf deren Basis die Unsicherheit berechnet werden soll.
* - identifier: Betrachtete Agentengruppen
* - type: Boolean
PARAMETER par_link_InGroupBasedDeffuantUncertainty_InConsumerAgentGroup_cags(set_InGroupBasedDeffuantUncertainty,set_InConsumerAgentGroup)

* - description: Legt die Attribute fest, die mit dieser Unsicherheit verbunden werden sollen.
* - identifier: Betrachtete Attribute
* - type: Boolean
PARAMETER par_link_InGroupBasedDeffuantUncertainty_InAttributeName_attributeNames(set_InGroupBasedDeffuantUncertainty,set_InAttributeName)

* - identifier: InPVactGlobalDeffuantUncertainty
* - type: String
SET set_InPVactGlobalDeffuantUncertainty(set_InUncertainty)

* - default: -1
* - domain: [0,1]
* - description: Legt den Extremistenparameter fest. Dieser Wert beschreibt den prozentualen Anteil der Exremisten.
* - identifier: Extremistenparameter
* - type: Float
PARAMETER par_InPVactGlobalDeffuantUncertainty_extremistParameter(set_InPVactGlobalDeffuantUncertainty)

* - default: 0.35
* - description: Legt den Wert der Unsicherheit für Extremisten fest.
* - identifier: Unsicherheit der Extremisten
* - type: Float
PARAMETER par_InPVactGlobalDeffuantUncertainty_extremistUncertainty(set_InPVactGlobalDeffuantUncertainty)

* - default: 1.4
* - description: Legt den Wert der Unsicherheit für Moderate fest.
* - identifier: Unsicherheit der Moderaten
* - type: Float
PARAMETER par_InPVactGlobalDeffuantUncertainty_moderateUncertainty(set_InPVactGlobalDeffuantUncertainty)

* - default: 0
* - description: Legt fest, ob der untere Grenzwert inklusiv (>=) oder exklusiv (>) ist.
* - identifier: Untere Grenze inklusive?
* - type: Boolean
PARAMETER par_InPVactGlobalDeffuantUncertainty_lowerBoundInclusive(set_InPVactGlobalDeffuantUncertainty)

* - default: 0
* - description: Legt fest, ob der obere Grenzwert inklusiv (<=) oder exklusiv (<) ist.
* - identifier: Obere Grenze inklusive?
* - type: Boolean
PARAMETER par_InPVactGlobalDeffuantUncertainty_upperBoundInclusive(set_InPVactGlobalDeffuantUncertainty)

* - description: Legt die Agentengruppe fest, auf deren Basis die Unsicherheit berechnet werden soll.
* - identifier: Betrachtete Agentengruppen
* - type: Boolean
PARAMETER par_link_InPVactGlobalDeffuantUncertainty_InConsumerAgentGroup_cags(set_InPVactGlobalDeffuantUncertainty,set_InConsumerAgentGroup)

* - identifier: InPVactGroupBasedDeffuantUncertainty
* - type: String
SET set_InPVactGroupBasedDeffuantUncertainty(set_InUncertainty)

* - default: -1
* - description: Legt den Extremistenparameter fest. Dieser Wert beschreibt den prozentualen Anteil der Exremisten.
* - identifier: Extremistenparameter
* - type: Float
PARAMETER par_InPVactGroupBasedDeffuantUncertainty_extremistParameter(set_InPVactGroupBasedDeffuantUncertainty)

* - default: 0.35
* - description: Legt den Wert der Unsicherheit für Extremisten fest.
* - identifier: Unsicherheit der Extremisten
* - type: Float
PARAMETER par_InPVactGroupBasedDeffuantUncertainty_extremistUncertainty(set_InPVactGroupBasedDeffuantUncertainty)

* - default: 1.4
* - description: Legt den Wert der Unsicherheit für Moderate fest.
* - identifier: Unsicherheit der Moderaten
* - type: Float
PARAMETER par_InPVactGroupBasedDeffuantUncertainty_moderateUncertainty(set_InPVactGroupBasedDeffuantUncertainty)

* - default: 0
* - description: Legt fest, ob der untere Grenzwert inklusiv (>=) oder exklusiv (>) ist.
* - identifier: Untere Grenze inklusive?
* - type: Boolean
PARAMETER par_InPVactGroupBasedDeffuantUncertainty_lowerBoundInclusive(set_InPVactGroupBasedDeffuantUncertainty)

* - default: 0
* - description: Legt fest, ob der obere Grenzwert inklusiv (<=) oder exklusiv (<) ist.
* - identifier: Obere Grenze inklusive?
* - type: Boolean
PARAMETER par_InPVactGroupBasedDeffuantUncertainty_upperBoundInclusive(set_InPVactGroupBasedDeffuantUncertainty)

* - description: Legt die Agentengruppe fest, auf deren Basis die Unsicherheit berechnet werden soll.
* - identifier: Betrachtete Agentengruppen
* - type: Boolean
PARAMETER par_link_InPVactGroupBasedDeffuantUncertainty_InConsumerAgentGroup_cags(set_InPVactGroupBasedDeffuantUncertainty,set_InConsumerAgentGroup)

* - identifier: InUncertainty
* - hidden: 1
* - type: String
SET set_InUncertainty(*)

* - identifier: InDisabledProcessPlanNodeFilterScheme
* - type: String
SET set_InDisabledProcessPlanNodeFilterScheme(set_InRAProcessPlanNodeFilterScheme)

* - description: Ungenutzter Platzhalter
* - identifier: ---
* - type: Float
PARAMETER par_InDisabledProcessPlanNodeFilterScheme_placeholder(set_InDisabledProcessPlanNodeFilterScheme)

* - identifier: InEntireNetworkNodeFilterScheme
* - type: String
SET set_InEntireNetworkNodeFilterScheme(set_InRAProcessPlanNodeFilterScheme)

* - description: Ungenutzter Platzhalter
* - identifier: ---
* - type: Float
PARAMETER par_InEntireNetworkNodeFilterScheme_placeholder(set_InEntireNetworkNodeFilterScheme)

* - identifier: InRAProcessModel
* - type: String
SET set_InRAProcessModel(set_InProcessModel)

* - default: 0.25
* - description: Legt den Einfluss der finanziellen Komponente fest.
* - identifier: Einfluss finanzielle Komponente (a)
* - type: Float
PARAMETER par_InRAProcessModel_a(set_InRAProcessModel)

* - default: 0.25
* - description: Legt den Einfluss der novelty Komponente fest.
* - identifier: Einfluss novelty Komponente (b)
* - type: Float
PARAMETER par_InRAProcessModel_b(set_InRAProcessModel)

* - default: 0.25
* - description: Legt den Umwelteinfluss fest.
* - identifier: Umwelteinfluss (c)
* - type: Float
PARAMETER par_InRAProcessModel_c(set_InRAProcessModel)

* - default: 0.25
* - description: Legt den Einfluss der sozialen Komponente fest.
* - identifier: Einfluss soziale Komponente (d)
* - type: Float
PARAMETER par_InRAProcessModel_d(set_InRAProcessModel)

* - default: 3
* - description: Legt den Einfluss für die Kommunikation mit Adoptern fest.
* - identifier: Einfluss der Kommunikation mit Adoptern
* - type: Integer
PARAMETER par_InRAProcessModel_adopterPoints(set_InRAProcessModel)

* - default: 2
* - description: Legt den Einfluss für die Kommunikation mit Interessenten fest.
* - identifier: Einfluss der Kommunikation mit Interessenten
* - type: Integer
PARAMETER par_InRAProcessModel_interestedPoints(set_InRAProcessModel)

* - default: 1
* - description: Legt den Einfluss für die Kommunikation mit Bewussten fest.
* - identifier: Einfluss der Kommunikation mit Bewussten
* - type: Integer
PARAMETER par_InRAProcessModel_awarePoints(set_InRAProcessModel)

* - default: 0
* - description: Legt den Einfluss für die Kommunikation mit Unwissenden fest.
* - identifier: Einfluss der Kommunikation mit Unwissenden
* - type: Integer
PARAMETER par_InRAProcessModel_unknownPoints(set_InRAProcessModel)

* - default: 0.125
* - description: Legt den Einflussfaktor 'a' der logistischen Funktion fest: logistic(a * -x).
* - identifier: Einflussfaktor der logistischen Funktion
* - type: Float
PARAMETER par_InRAProcessModel_logisticFactor(set_InRAProcessModel)

* - default: 0.5
* - description: speed of convergence
* - identifier: speed of convergence
* - type: Float
PARAMETER par_InRAProcessModel_speedOfConvergence(set_InRAProcessModel)

* - default: 1.75
* - description: attitude gap
* - identifier: attitude gap
* - type: Float
PARAMETER par_InRAProcessModel_attitudeGap(set_InRAProcessModel)

* - default: 0.5
* - domain: [0,1]
* - description: Wahrscheinlichkeit für einen neutralen Ausgang.
* - identifier: Wahrscheinlichkeit Neutral
* - type: Float
PARAMETER par_InRAProcessModel_chanceNeutral(set_InRAProcessModel)

* - default: 0.25
* - domain: [0,1]
* - description: Wahrscheinlichkeit, dass der normale relative agreement algorithm genutzt wird.
* - identifier: Wahrscheinlichkeit Konvergenz
* - type: Float
PARAMETER par_InRAProcessModel_chanceConvergence(set_InRAProcessModel)

* - default: 0.25
* - domain: [0,1]
* - description: Wahrscheinlichkeit, dass der reverse relative agreement algorithm genutzt wird.
* - identifier: Wahrscheinlichkeit Divergenz
* - type: Float
PARAMETER par_InRAProcessModel_chanceDivergence(set_InRAProcessModel)

* - description: Legt den Filter fest, um 'sichtbare' Haushalte zu identifizieren.
* - identifier: Netzwerkfilter für räumliche Sicht
* - type: Boolean
PARAMETER par_link_InRAProcessModel_InRAProcessPlanNodeFilterScheme_nodeFilterScheme(set_InRAProcessModel,set_InRAProcessPlanNodeFilterScheme)

* - description: Legt die zu nutzende PV-Datei fest.
* - identifier: PV-Datei
* - type: Boolean
PARAMETER par_link_InRAProcessModel_InPVFile_pvFile(set_InRAProcessModel,set_InPVFile)

* - description: Legt die Unsicherheiten, welche von diesem Modell verwendet werden sollen.
* - identifier: Unsicherheiten
* - type: Boolean
PARAMETER par_link_InRAProcessModel_InUncertainty_uncertainties(set_InRAProcessModel,set_InUncertainty)

* - identifier: InRAProcessPlanMaxDistanceFilterScheme
* - type: String
SET set_InRAProcessPlanMaxDistanceFilterScheme(set_InRAProcessPlanNodeFilterScheme)

* - description: Legt die maximale Entfernung fest. Die Einheit richtet sich sowohl nach den Ausgangsdaten als auch der verwendeten Metric im räumlichen Modell.
* - identifier: Maximale Entfernung
* - type: Float
PARAMETER par_InRAProcessPlanMaxDistanceFilterScheme_maxDistance(set_InRAProcessPlanMaxDistanceFilterScheme)

* - description: Legt fest, ob der Grenzwert inklusiv (<=) oder exklusiv (<) ist.
* - identifier: Grenzwert inklusive?
* - type: Boolean
PARAMETER par_InRAProcessPlanMaxDistanceFilterScheme_inclusive(set_InRAProcessPlanMaxDistanceFilterScheme)

* - identifier: InRAProcessPlanNodeFilterScheme
* - hidden: 1
* - type: String
SET set_InRAProcessPlanNodeFilterScheme(set_InProcessPlanNodeFilterScheme)

* - identifier: InProcessModel
* - hidden: 1
* - type: String
SET set_InProcessModel(*)

* - identifier: InProcessPlanNodeFilterScheme
* - hidden: 1
* - type: String
SET set_InProcessPlanNodeFilterScheme(*)

* - identifier: InComponent
* - hidden: 1
* - type: String
SET set_InComponent(*)

* - identifier: InDefaultDoActionComponent
* - type: String
SET set_InDefaultDoActionComponent(set_InEvaluableComponent)

* - default: 3
* - description: todo
* - identifier: adopter points
* - type: Integer
PARAMETER par_InDefaultDoActionComponent_adopterPoints(set_InDefaultDoActionComponent)

* - default: 2
* - description: todo
* - identifier: interested points
* - type: Integer
PARAMETER par_InDefaultDoActionComponent_interestedPoints(set_InDefaultDoActionComponent)

* - default: 1
* - description: todo
* - identifier: aware points
* - type: Integer
PARAMETER par_InDefaultDoActionComponent_awarePoints(set_InDefaultDoActionComponent)

* - default: 0
* - description: todo
* - identifier: unknown points
* - type: Integer
PARAMETER par_InDefaultDoActionComponent_unknownPoints(set_InDefaultDoActionComponent)

* - identifier: InDefaultHandleDecisionMakingComponent
* - type: String
SET set_InDefaultHandleDecisionMakingComponent(set_InEvaluableComponent)

* - default: 0.25
* - description: todo
* - identifier: a
* - type: Float
PARAMETER par_InDefaultHandleDecisionMakingComponent_a(set_InDefaultHandleDecisionMakingComponent)

* - default: 0.25
* - description: todo
* - identifier: b
* - type: Float
PARAMETER par_InDefaultHandleDecisionMakingComponent_b(set_InDefaultHandleDecisionMakingComponent)

* - default: 0.25
* - description: todo
* - identifier: c
* - type: Float
PARAMETER par_InDefaultHandleDecisionMakingComponent_c(set_InDefaultHandleDecisionMakingComponent)

* - default: 0.25
* - description: todo
* - identifier: d
* - type: Float
PARAMETER par_InDefaultHandleDecisionMakingComponent_d(set_InDefaultHandleDecisionMakingComponent)

* - default: 0.5
* - description: todo
* - identifier: weightFT
* - type: Float
PARAMETER par_InDefaultHandleDecisionMakingComponent_weightFT(set_InDefaultHandleDecisionMakingComponent)

* - default: 0.5
* - description: todo
* - identifier: weightNPV
* - type: Float
PARAMETER par_InDefaultHandleDecisionMakingComponent_weightNPV(set_InDefaultHandleDecisionMakingComponent)

* - default: 0.5
* - description: todo
* - identifier: weightSocial
* - type: Float
PARAMETER par_InDefaultHandleDecisionMakingComponent_weightSocial(set_InDefaultHandleDecisionMakingComponent)

* - default: 0.5
* - description: todo
* - identifier: weightLocal
* - type: Float
PARAMETER par_InDefaultHandleDecisionMakingComponent_weightLocal(set_InDefaultHandleDecisionMakingComponent)

* - default: 0.125
* - description: todo
* - identifier: logistic factor
* - type: Float
PARAMETER par_InDefaultHandleDecisionMakingComponent_logisticFactor(set_InDefaultHandleDecisionMakingComponent)

* - description: todo
* - identifier: pvFile
* - type: Boolean
PARAMETER par_link_InDefaultHandleDecisionMakingComponent_InPVFile_pvFile(set_InDefaultHandleDecisionMakingComponent,set_InPVFile)

* - description: todo
* - identifier: nodeFilterScheme
* - type: Boolean
PARAMETER par_link_InDefaultHandleDecisionMakingComponent_InRAProcessPlanNodeFilterScheme_nodeFilterScheme(set_InDefaultHandleDecisionMakingComponent,set_InRAProcessPlanNodeFilterScheme)

* - identifier: InDefaultHandleFeasibilityComponent
* - type: String
SET set_InDefaultHandleFeasibilityComponent(set_InEvaluableComponent)

* - default: 3
* - description: todo
* - identifier: adopter points
* - type: Integer
PARAMETER par_InDefaultHandleFeasibilityComponent_adopterPoints(set_InDefaultHandleFeasibilityComponent)

* - default: 2
* - description: todo
* - identifier: interested points
* - type: Integer
PARAMETER par_InDefaultHandleFeasibilityComponent_interestedPoints(set_InDefaultHandleFeasibilityComponent)

* - default: 1
* - description: todo
* - identifier: aware points
* - type: Integer
PARAMETER par_InDefaultHandleFeasibilityComponent_awarePoints(set_InDefaultHandleFeasibilityComponent)

* - default: 0
* - description: todo
* - identifier: unknown points
* - type: Integer
PARAMETER par_InDefaultHandleFeasibilityComponent_unknownPoints(set_InDefaultHandleFeasibilityComponent)

* - identifier: InDefaultHandleInterestComponent
* - type: String
SET set_InDefaultHandleInterestComponent(set_InEvaluableComponent)

* - default: 3
* - description: todo
* - identifier: adopter points
* - type: Integer
PARAMETER par_InDefaultHandleInterestComponent_adopterPoints(set_InDefaultHandleInterestComponent)

* - default: 2
* - description: todo
* - identifier: interested points
* - type: Integer
PARAMETER par_InDefaultHandleInterestComponent_interestedPoints(set_InDefaultHandleInterestComponent)

* - default: 1
* - description: todo
* - identifier: aware points
* - type: Integer
PARAMETER par_InDefaultHandleInterestComponent_awarePoints(set_InDefaultHandleInterestComponent)

* - default: 0
* - description: todo
* - identifier: unknown points
* - type: Integer
PARAMETER par_InDefaultHandleInterestComponent_unknownPoints(set_InDefaultHandleInterestComponent)

* - identifier: InDoNothingComponent
* - type: String
SET set_InDoNothingComponent(set_InEvaluableComponent)

* - default: 0
* - description: todo
* - identifier: ---
* - type: Float
PARAMETER par_InDoNothingComponent_placeholder(set_InDoNothingComponent)

* - identifier: InEvaluableComponent
* - hidden: 1
* - type: String
SET set_InEvaluableComponent(set_InComponent)

* - identifier: InSumAttributeComponent
* - type: String
SET set_InSumAttributeComponent(set_InValueComponent)

* - default: 1
* - description: todo
* - identifier: Wichtung
* - type: Float
PARAMETER par_InSumAttributeComponent_weight(set_InSumAttributeComponent)

* - description: todo
* - identifier: Attribute
* - type: Boolean
PARAMETER par_link_InSumAttributeComponent_InAttributeName_attributeNames(set_InSumAttributeComponent,set_InAttributeName)

* - identifier: InSumIntermediateComponent
* - type: String
SET set_InSumIntermediateComponent(set_InValueComponent)

* - default: 1
* - description: todo
* - identifier: Wichtung
* - type: Float
PARAMETER par_InSumIntermediateComponent_weight(set_InSumIntermediateComponent)

* - description: todo
* - identifier: Komponenten
* - type: Boolean
PARAMETER par_link_InSumIntermediateComponent_InValueComponent_components(set_InSumIntermediateComponent,set_InValueComponent)

* - identifier: InSumThresholdComponent
* - type: String
SET set_InSumThresholdComponent(set_InEvaluableComponent)

* - default: 1
* - description: todo
* - identifier: Wichtung
* - type: Float
PARAMETER par_InSumThresholdComponent_weight(set_InSumThresholdComponent)

* - default: 0
* - description: todo
* - identifier: Grenzwert
* - type: Float
PARAMETER par_InSumThresholdComponent_threshold(set_InSumThresholdComponent)

* - description: todo
* - identifier: Komponenten
* - type: Boolean
PARAMETER par_link_InSumThresholdComponent_InValueComponent_components(set_InSumThresholdComponent,set_InValueComponent)

* - identifier: InValueComponent
* - hidden: 1
* - type: String
SET set_InValueComponent(set_InComponent)

* - identifier: InModularRAProcessModel
* - type: String
SET set_InModularRAProcessModel(set_InProcessModel)

* - default: 0.5
* - description: todo
* - identifier: speed of convergence
* - type: Float
PARAMETER par_InModularRAProcessModel_speedOfConvergence(set_InModularRAProcessModel)

* - description: todo
* - identifier: attitude gap
* - type: Float
PARAMETER par_InModularRAProcessModel_attitudeGap(set_InModularRAProcessModel)

* - default: 0.5
* - domain: [0,1]
* - description: todo
* - identifier: chance neutral
* - type: Float
PARAMETER par_InModularRAProcessModel_chanceNeutral(set_InModularRAProcessModel)

* - default: 0.25
* - domain: [0,1]
* - description: todo
* - identifier: chance convergence
* - type: Float
PARAMETER par_InModularRAProcessModel_chanceConvergence(set_InModularRAProcessModel)

* - default: 0.25
* - domain: [0,1]
* - description: todo
* - identifier: chance divergence
* - type: Float
PARAMETER par_InModularRAProcessModel_chanceDivergence(set_InModularRAProcessModel)

* - description: todo
* - identifier: interest component
* - type: Boolean
PARAMETER par_link_InModularRAProcessModel_InEvaluableComponent_interestComponent(set_InModularRAProcessModel,set_InEvaluableComponent)

* - description: todo
* - identifier: feasibility component
* - type: Boolean
PARAMETER par_link_InModularRAProcessModel_InEvaluableComponent_feasibilityComponent(set_InModularRAProcessModel,set_InEvaluableComponent)

* - description: todo
* - identifier: decision making component
* - type: Boolean
PARAMETER par_link_InModularRAProcessModel_InEvaluableComponent_decisionMakingComponent(set_InModularRAProcessModel,set_InEvaluableComponent)

* - description: todo
* - identifier: action component
* - type: Boolean
PARAMETER par_link_InModularRAProcessModel_InEvaluableComponent_actionComponent(set_InModularRAProcessModel,set_InEvaluableComponent)

* - identifier: uncertainties
* - type: Boolean
PARAMETER par_link_InModularRAProcessModel_InUncertainty_uncertainties(set_InModularRAProcessModel,set_InUncertainty)

* - color: Blue
* - shape: gear
* - identifier: InAddModule_calcgraphnode
* - type: String
SET set_InAddModule_calcgraphnode(set_InConsumerAgentCalculationModule)

* - default: 1
* - description: todo
* - identifier: Wichtung
* - type: Float
PARAMETER par_InAddModule_calcgraphnode_weight(set_InAddModule_calcgraphnode)

* - description: todo
* - identifier: Erster Summand
* - type: Boolean
PARAMETER par_link_InAddModule_calcgraphnode_InConsumerAgentCalculationModule_first_graphedge(set_InAddModule_calcgraphnode,set_InConsumerAgentCalculationModule)

* - description: todo
* - identifier: Zweiter Summand
* - type: Boolean
PARAMETER par_link_InAddModule_calcgraphnode_InConsumerAgentCalculationModule_second_graphedge(set_InAddModule_calcgraphnode,set_InConsumerAgentCalculationModule)

* - color: Green
* - shape: square
* - identifier: InAttributeInputModule_inputgraphnode
* - type: String
SET set_InAttributeInputModule_inputgraphnode(set_InConsumerAgentCalculationModule)

* - default: 1
* - description: todo
* - identifier: Wichtung
* - type: Float
PARAMETER par_InAttributeInputModule_inputgraphnode_weight(set_InAttributeInputModule_inputgraphnode)

* - description: todo
* - identifier: Referenziertes Attribut
* - type: Boolean
PARAMETER par_link_InAttributeInputModule_inputgraphnode_InAttributeName_attribute(set_InAttributeInputModule_inputgraphnode,set_InAttributeName)

* - color: Green
* - shape: square
* - identifier: InDisaggregatedFinancialModule_inputgraphnode
* - type: String
SET set_InDisaggregatedFinancialModule_inputgraphnode(set_InConsumerAgentCalculationModule)

* - default: 1
* - description: todo
* - identifier: Wichtung
* - type: Float
PARAMETER par_InDisaggregatedFinancialModule_inputgraphnode_weight(set_InDisaggregatedFinancialModule_inputgraphnode)

* - default: 0.125
* - description: todo
* - identifier: Logistikfaktor
* - type: Float
PARAMETER par_InDisaggregatedFinancialModule_inputgraphnode_logisticFactor(set_InDisaggregatedFinancialModule_inputgraphnode)

* - color: Green
* - shape: square
* - identifier: InDisaggregatedNPVModule_inputgraphnode
* - type: String
SET set_InDisaggregatedNPVModule_inputgraphnode(set_InConsumerAgentCalculationModule)

* - default: 1
* - description: todo
* - identifier: Wichtung
* - type: Float
PARAMETER par_InDisaggregatedNPVModule_inputgraphnode_weight(set_InDisaggregatedNPVModule_inputgraphnode)

* - default: 0.125
* - description: todo
* - identifier: Logistikfaktor
* - type: Float
PARAMETER par_InDisaggregatedNPVModule_inputgraphnode_logisticFactor(set_InDisaggregatedNPVModule_inputgraphnode)

* - description: todo
* - identifier: PV-Datei
* - type: Boolean
PARAMETER par_link_InDisaggregatedNPVModule_inputgraphnode_InPVFile_pvFile(set_InDisaggregatedNPVModule_inputgraphnode,set_InPVFile)

* - color: Blue
* - shape: gear
* - identifier: InLogisticModule_calcgraphnode
* - type: String
SET set_InLogisticModule_calcgraphnode(set_InConsumerAgentCalculationModule)

* - default: 1
* - description: todo
* - identifier: Wichtung
* - type: Float
PARAMETER par_InLogisticModule_calcgraphnode_weight(set_InLogisticModule_calcgraphnode)

* - description: todo
* - identifier: Eingabemodul
* - type: Boolean
PARAMETER par_link_InLogisticModule_calcgraphnode_InConsumerAgentCalculationModule_input_graphedge(set_InLogisticModule_calcgraphnode,set_InConsumerAgentCalculationModule)

* - color: Green
* - shape: square
* - identifier: InNPVModule_inputgraphnode
* - type: String
SET set_InNPVModule_inputgraphnode(set_InConsumerAgentCalculationModule)

* - default: 1
* - description: todo
* - identifier: Wichtung
* - type: Float
PARAMETER par_InNPVModule_inputgraphnode_weight(set_InNPVModule_inputgraphnode)

* - description: todo
* - identifier: PV-Datei
* - type: Boolean
PARAMETER par_link_InNPVModule_inputgraphnode_InPVFile_pvFile(set_InNPVModule_inputgraphnode,set_InPVFile)

* - color: Blue
* - shape: gear
* - identifier: InWeightedModule_calcgraphnode
* - type: String
SET set_InWeightedModule_calcgraphnode(set_InConsumerAgentCalculationModule)

* - default: 1
* - description: todo
* - identifier: Wichtung
* - type: Float
PARAMETER par_InWeightedModule_calcgraphnode_weight(set_InWeightedModule_calcgraphnode)

* - description: todo
* - identifier: Eingabemodul
* - type: Boolean
PARAMETER par_link_InWeightedModule_calcgraphnode_InConsumerAgentCalculationModule_input_graphedge(set_InWeightedModule_calcgraphnode,set_InConsumerAgentCalculationModule)

* - color: Red
* - shape: diamond
* - identifier: InDefaultActionModule_evalgraphnode
* - type: String
SET set_InDefaultActionModule_evalgraphnode(set_InConsumerAgentEvaluationModule)

* - default: 3
* - description: todo
* - identifier: adopter points
* - type: Integer
PARAMETER par_InDefaultActionModule_evalgraphnode_adopterPoints(set_InDefaultActionModule_evalgraphnode)

* - default: 2
* - description: todo
* - identifier: interested points
* - type: Integer
PARAMETER par_InDefaultActionModule_evalgraphnode_interestedPoints(set_InDefaultActionModule_evalgraphnode)

* - default: 1
* - description: todo
* - identifier: aware points
* - type: Integer
PARAMETER par_InDefaultActionModule_evalgraphnode_awarePoints(set_InDefaultActionModule_evalgraphnode)

* - default: 0
* - description: todo
* - identifier: unknown points
* - type: Integer
PARAMETER par_InDefaultActionModule_evalgraphnode_unknownPoints(set_InDefaultActionModule_evalgraphnode)

* - default: 0.5
* - description: todo
* - identifier: speed of convergence
* - type: Float
PARAMETER par_InDefaultActionModule_evalgraphnode_speedOfConvergence(set_InDefaultActionModule_evalgraphnode)

* - description: todo
* - identifier: attitude gap
* - type: Float
PARAMETER par_InDefaultActionModule_evalgraphnode_attitudeGap(set_InDefaultActionModule_evalgraphnode)

* - default: 0.5
* - domain: [0,1]
* - description: todo
* - identifier: chance neutral
* - type: Float
PARAMETER par_InDefaultActionModule_evalgraphnode_chanceNeutral(set_InDefaultActionModule_evalgraphnode)

* - default: 0.25
* - domain: [0,1]
* - description: todo
* - identifier: chance convergence
* - type: Float
PARAMETER par_InDefaultActionModule_evalgraphnode_chanceConvergence(set_InDefaultActionModule_evalgraphnode)

* - default: 0.25
* - domain: [0,1]
* - description: todo
* - identifier: chance divergence
* - type: Float
PARAMETER par_InDefaultActionModule_evalgraphnode_chanceDivergence(set_InDefaultActionModule_evalgraphnode)

* - description: todo
* - identifier: uncertainties
* - type: Boolean
PARAMETER par_link_InDefaultActionModule_evalgraphnode_InUncertainty_uncertainties(set_InDefaultActionModule_evalgraphnode,set_InUncertainty)

* - color: Red
* - shape: diamond
* - identifier: InDefaultDecisionMakingModule_evalgraphnode
* - type: String
SET set_InDefaultDecisionMakingModule_evalgraphnode(set_InConsumerAgentEvaluationModule)

* - default: 0.25
* - description: Legt den Einfluss der finanziellen Komponente fest.
* - identifier: Einfluss finanzielle Komponente (a)
* - type: Float
PARAMETER par_InDefaultDecisionMakingModule_evalgraphnode_a(set_InDefaultDecisionMakingModule_evalgraphnode)

* - default: 0.25
* - description: Legt den Einfluss der novelty Komponente fest.
* - identifier: Einfluss novelty Komponente (b)
* - type: Float
PARAMETER par_InDefaultDecisionMakingModule_evalgraphnode_b(set_InDefaultDecisionMakingModule_evalgraphnode)

* - default: 0.25
* - description: Legt den Umwelteinfluss fest.
* - identifier: Umwelteinfluss (c)
* - type: Float
PARAMETER par_InDefaultDecisionMakingModule_evalgraphnode_c(set_InDefaultDecisionMakingModule_evalgraphnode)

* - default: 0.25
* - description: Legt den Einfluss der sozialen Komponente fest.
* - identifier: Einfluss soziale Komponente (d)
* - type: Float
PARAMETER par_InDefaultDecisionMakingModule_evalgraphnode_d(set_InDefaultDecisionMakingModule_evalgraphnode)

* - default: 0.5
* - description: todo
* - identifier: Wichtung fin. 
* - type: Float
PARAMETER par_InDefaultDecisionMakingModule_evalgraphnode_weightFT(set_InDefaultDecisionMakingModule_evalgraphnode)

* - default: 0.5
* - description: todo
* - identifier: Wichtung Barwert
* - type: Float
PARAMETER par_InDefaultDecisionMakingModule_evalgraphnode_weightNPV(set_InDefaultDecisionMakingModule_evalgraphnode)

* - default: 0.5
* - description: todo
* - identifier: Wichtung soziales Netz
* - type: Float
PARAMETER par_InDefaultDecisionMakingModule_evalgraphnode_weightSocial(set_InDefaultDecisionMakingModule_evalgraphnode)

* - default: 0.5
* - description: todo
* - identifier: Wichtung lokales Netz
* - type: Float
PARAMETER par_InDefaultDecisionMakingModule_evalgraphnode_weightLocal(set_InDefaultDecisionMakingModule_evalgraphnode)

* - default: 0.125
* - description: todo
* - identifier: Wichtung lokales Netz
* - type: Float
PARAMETER par_InDefaultDecisionMakingModule_evalgraphnode_logisticFactor(set_InDefaultDecisionMakingModule_evalgraphnode)

* - description: todo
* - identifier: pv file
* - type: Boolean
PARAMETER par_link_InDefaultDecisionMakingModule_evalgraphnode_InPVFile_pvFile(set_InDefaultDecisionMakingModule_evalgraphnode,set_InPVFile)

* - description: todo
* - identifier: node filter scheme
* - type: Boolean
PARAMETER par_link_InDefaultDecisionMakingModule_evalgraphnode_InRAProcessPlanNodeFilterScheme_nodeFilterScheme(set_InDefaultDecisionMakingModule_evalgraphnode,set_InRAProcessPlanNodeFilterScheme)

* - color: Red
* - shape: diamond
* - identifier: InDefaultFeasibilityModule_evalgraphnode
* - type: String
SET set_InDefaultFeasibilityModule_evalgraphnode(set_InConsumerAgentEvaluationModule)

* - default: 3
* - description: todo
* - identifier: adopter points
* - type: Integer
PARAMETER par_InDefaultFeasibilityModule_evalgraphnode_adopterPoints(set_InDefaultFeasibilityModule_evalgraphnode)

* - default: 2
* - description: todo
* - identifier: interested points
* - type: Integer
PARAMETER par_InDefaultFeasibilityModule_evalgraphnode_interestedPoints(set_InDefaultFeasibilityModule_evalgraphnode)

* - default: 1
* - description: todo
* - identifier: aware points
* - type: Integer
PARAMETER par_InDefaultFeasibilityModule_evalgraphnode_awarePoints(set_InDefaultFeasibilityModule_evalgraphnode)

* - default: 0
* - description: todo
* - identifier: unknown points
* - type: Integer
PARAMETER par_InDefaultFeasibilityModule_evalgraphnode_unknownPoints(set_InDefaultFeasibilityModule_evalgraphnode)

* - default: 0.5
* - description: todo
* - identifier: speed of convergence
* - type: Float
PARAMETER par_InDefaultFeasibilityModule_evalgraphnode_speedOfConvergence(set_InDefaultFeasibilityModule_evalgraphnode)

* - description: todo
* - identifier: attitude gap
* - type: Float
PARAMETER par_InDefaultFeasibilityModule_evalgraphnode_attitudeGap(set_InDefaultFeasibilityModule_evalgraphnode)

* - default: 0.5
* - domain: [0,1]
* - description: todo
* - identifier: chance neutral
* - type: Float
PARAMETER par_InDefaultFeasibilityModule_evalgraphnode_chanceNeutral(set_InDefaultFeasibilityModule_evalgraphnode)

* - default: 0.25
* - domain: [0,1]
* - description: todo
* - identifier: chance convergence
* - type: Float
PARAMETER par_InDefaultFeasibilityModule_evalgraphnode_chanceConvergence(set_InDefaultFeasibilityModule_evalgraphnode)

* - default: 0.25
* - domain: [0,1]
* - description: todo
* - identifier: chance divergence
* - type: Float
PARAMETER par_InDefaultFeasibilityModule_evalgraphnode_chanceDivergence(set_InDefaultFeasibilityModule_evalgraphnode)

* - description: todo
* - identifier: uncertainties
* - type: Boolean
PARAMETER par_link_InDefaultFeasibilityModule_evalgraphnode_InUncertainty_uncertainties(set_InDefaultFeasibilityModule_evalgraphnode,set_InUncertainty)

* - color: Red
* - shape: diamond
* - identifier: InDefaultInterestModule_evalgraphnode
* - type: String
SET set_InDefaultInterestModule_evalgraphnode(set_InConsumerAgentEvaluationModule)

* - default: 3
* - description: todo
* - identifier: adopter points
* - type: Integer
PARAMETER par_InDefaultInterestModule_evalgraphnode_adopterPoints(set_InDefaultInterestModule_evalgraphnode)

* - default: 2
* - description: todo
* - identifier: interested points
* - type: Integer
PARAMETER par_InDefaultInterestModule_evalgraphnode_interestedPoints(set_InDefaultInterestModule_evalgraphnode)

* - default: 1
* - description: todo
* - identifier: aware points
* - type: Integer
PARAMETER par_InDefaultInterestModule_evalgraphnode_awarePoints(set_InDefaultInterestModule_evalgraphnode)

* - default: 0
* - description: todo
* - identifier: unknown points
* - type: Integer
PARAMETER par_InDefaultInterestModule_evalgraphnode_unknownPoints(set_InDefaultInterestModule_evalgraphnode)

* - default: 0.5
* - description: todo
* - identifier: speed of convergence
* - type: Float
PARAMETER par_InDefaultInterestModule_evalgraphnode_speedOfConvergence(set_InDefaultInterestModule_evalgraphnode)

* - description: todo
* - identifier: attitude gap
* - type: Float
PARAMETER par_InDefaultInterestModule_evalgraphnode_attitudeGap(set_InDefaultInterestModule_evalgraphnode)

* - default: 0.5
* - domain: [0,1]
* - description: todo
* - identifier: chance neutral
* - type: Float
PARAMETER par_InDefaultInterestModule_evalgraphnode_chanceNeutral(set_InDefaultInterestModule_evalgraphnode)

* - default: 0.25
* - domain: [0,1]
* - description: todo
* - identifier: chance convergence
* - type: Float
PARAMETER par_InDefaultInterestModule_evalgraphnode_chanceConvergence(set_InDefaultInterestModule_evalgraphnode)

* - default: 0.25
* - domain: [0,1]
* - description: todo
* - identifier: chance divergence
* - type: Float
PARAMETER par_InDefaultInterestModule_evalgraphnode_chanceDivergence(set_InDefaultInterestModule_evalgraphnode)

* - description: todo
* - identifier: uncertainties
* - type: Boolean
PARAMETER par_link_InDefaultInterestModule_evalgraphnode_InUncertainty_uncertainties(set_InDefaultInterestModule_evalgraphnode,set_InUncertainty)

* - color: Red
* - shape: diamond
* - identifier: InStageEvaluationModule_evalgraphnode
* - type: String
SET set_InStageEvaluationModule_evalgraphnode(set_InConsumerAgentEvaluationModule)

* - description: todo
* - identifier: awareness module
* - type: Boolean
PARAMETER par_link_InStageEvaluationModule_evalgraphnode_InConsumerAgentEvaluationModule_awarenessModule_graphedge(set_InStageEvaluationModule_evalgraphnode,set_InConsumerAgentEvaluationModule)

* - description: todo
* - identifier: feasibility module
* - type: Boolean
PARAMETER par_link_InStageEvaluationModule_evalgraphnode_InConsumerAgentEvaluationModule_feasibilityModule_graphedge(set_InStageEvaluationModule_evalgraphnode,set_InConsumerAgentEvaluationModule)

* - description: todo
* - identifier: decision making module
* - type: Boolean
PARAMETER par_link_InStageEvaluationModule_evalgraphnode_InConsumerAgentEvaluationModule_decisionMakingModule_graphedge(set_InStageEvaluationModule_evalgraphnode,set_InConsumerAgentEvaluationModule)

* - description: todo
* - identifier: adopted module
* - type: Boolean
PARAMETER par_link_InStageEvaluationModule_evalgraphnode_InConsumerAgentEvaluationModule_adoptedModule_graphedge(set_InStageEvaluationModule_evalgraphnode,set_InConsumerAgentEvaluationModule)

* - description: todo
* - identifier: impeded module
* - type: Boolean
PARAMETER par_link_InStageEvaluationModule_evalgraphnode_InConsumerAgentEvaluationModule_impededModule_graphedge(set_InStageEvaluationModule_evalgraphnode,set_InConsumerAgentEvaluationModule)

* - color: Red
* - shape: diamond
* - identifier: InSumThresholdEvaluationModule_evalgraphnode
* - type: String
SET set_InSumThresholdEvaluationModule_evalgraphnode(set_InConsumerAgentEvaluationModule)

* - default: 1
* - description: todo
* - identifier: Grenzwert
* - type: Float
PARAMETER par_InSumThresholdEvaluationModule_evalgraphnode_threshold(set_InSumThresholdEvaluationModule_evalgraphnode)

* - default: 1
* - domain: [0|1]
* - description: todo
* - identifier: Adoption wenn unter Grenzwert?
* - type: Boolean
PARAMETER par_InSumThresholdEvaluationModule_evalgraphnode_adoptIfBelowThreshold(set_InSumThresholdEvaluationModule_evalgraphnode)

* - description: todo
* - identifier: Eingabekomponenten
* - type: Boolean
PARAMETER par_link_InSumThresholdEvaluationModule_evalgraphnode_InConsumerAgentCalculationModule_input_graphedge(set_InSumThresholdEvaluationModule_evalgraphnode,set_InConsumerAgentCalculationModule)

* - identifier: InConsumerAgentCalculationModule
* - hidden: 1
* - type: String
SET set_InConsumerAgentCalculationModule(set_InConsumerAgentModule)

* - identifier: InConsumerAgentEvaluationModule
* - hidden: 1
* - type: String
SET set_InConsumerAgentEvaluationModule(set_InConsumerAgentModule)

* - identifier: InConsumerAgentModule
* - hidden: 1
* - type: String
SET set_InConsumerAgentModule(set_InModule)

* - identifier: InConsumerAgentModularProcessModel
* - hidden: 1
* - type: String
SET set_InConsumerAgentModularProcessModel(set_InModularProcessModel)

* - identifier: InSimpleConsumerAgentMPM
* - type: String
SET set_InSimpleConsumerAgentMPM(set_InConsumerAgentModularProcessModel)

* - description: todo
* - identifier: Startmodul
* - type: Boolean
PARAMETER par_link_InSimpleConsumerAgentMPM_InConsumerAgentEvaluationModule_startModule(set_InSimpleConsumerAgentMPM,set_InConsumerAgentEvaluationModule)

* - identifier: InModularProcessModel
* - hidden: 1
* - type: String
SET set_InModularProcessModel(set_InProcessModel)

* - identifier: InModule
* - hidden: 1
* - type: String
SET set_InModule(*)

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

* - description: Legt das Produktgruppenattribut fest, zu dem dieses Produkt gehören soll.
* - identifier: Produktgruppenattribut
* - type: Boolean
PARAMETER par_link_InFixProductAttribute_InProductGroupAttribute_refPGA(set_InFixProductAttribute,set_InProductGroupAttribute)

* - description: Bestimmt die Wert für dieses Attribut.
* - identifier: Wert
* - type: Float
PARAMETER par_InFixProductAttribute_fixPAvalue(set_InFixProductAttribute)

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

* - identifier: InFileBasedPVactMilieuSupplier
* - type: String
SET set_InFileBasedPVactMilieuSupplier(set_InSpatialDistributionWithCollection)

* - identifier: file
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

* - identifier: file
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

* - identifier: file
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

* - identifier: file
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
* - domain: [0,)
* - description: Definiert den Bildbreite für die Netzwerkbilder. Falls der Wert 0 ist, bestimmt Graphviz die optimale Bildgröße automatisch.
* - identifier: Bevorzugte Bildbreite
* - unit: [Pixel]
* - type: Float
SCALAR sca_InGraphvizGeneral_preferredImageWidth

* - default: 1000
* - domain: [0,)
* - description: Definiert den Bildhöhe für die Netzwerkbilder. Falls der Wert 0 ist, bestimmt Graphviz die optimale Bildgröße automatisch.
* - identifier: Bevorzugte Bildhöhe
* - unit: [Pixel]
* - type: Float
SCALAR sca_InGraphvizGeneral_preferredImageHeight

* - default: 1
* - domain: [0|1]
* - description: Nutzt für die Grapherzeugung ein positionsbasiertes Layout. Das genaue Layout ist NEATO kombiniert mit -n. Dieses Layout setzt vorraus, dass die Agenten eine Position besitzen. Zusätzlich benötigt es eine eingestellte Bildgröße. Falls diese fehlt, wird ein (ca.) 1000x1000 Pixel großes Bild erzeugt. Dieses Layout kann zu Verzerrungen führen, da es unter Umständen (z.B. bei Geoinformationen) die Daten in das gewünschte Ausgabeformat transformiert. Um das Seitenverhältnis beizubehalten, kann die entsprechende Option genutzt werden.
* - identifier: Positionsbasiertes Layout
* - rule: IF (sca_InGraphvizGeneral_positionBasedLayout == 1, sca_InGraphvizGeneral_freeLayout = 0)
* - rule: IF (sca_InGraphvizGeneral_positionBasedLayout == 0, sca_InGraphvizGeneral_positionBasedLayout = 1)
* - rule: IF (sca_InGraphvizGeneral_positionBasedLayout == 0, sca_InGraphvizGeneral_freeLayout = 0)
* - type: Boolean
SCALAR sca_InGraphvizGeneral_positionBasedLayout

* - default: 0
* - domain: [0|1]
* - description: Nutzt für die Grapherzeugung ein kräftebasierentes Layout. Das genaue Layout ist SFDP mit der overlap-Option prism.
* - identifier: Freies Layout
* - rule: IF (sca_InGraphvizGeneral_freeLayout == 1, sca_InGraphvizGeneral_positionBasedLayout = 0)
* - rule: IF (sca_InGraphvizGeneral_freeLayout == 0, sca_InGraphvizGeneral_positionBasedLayout = 0)
* - rule: IF (sca_InGraphvizGeneral_freeLayout == 0, sca_InGraphvizGeneral_freeLayout = 1)
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

* - default: 1
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
SCALAR sca_InGeneral_printNoErrorImage

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

* - identifier: logGraphUpdate
* - type: Boolean
SCALAR sca_InGeneral_logGraphUpdate

* - identifier: logRelativeAgreement
* - type: Boolean
SCALAR sca_InGeneral_logRelativeAgreement

* - identifier: logInterestUpdate
* - type: Boolean
SCALAR sca_InGeneral_logInterestUpdate

* - identifier: logShareNetworkLocal
* - type: Boolean
SCALAR sca_InGeneral_logShareNetworkLocal

* - identifier: logFinancalComponent
* - type: Boolean
SCALAR sca_InGeneral_logFinancalComponent

* - identifier: logCalculateDecisionMaking
* - type: Boolean
SCALAR sca_InGeneral_logCalculateDecisionMaking

* - domain: [0|1]
* - description: Gibt die Adoptionsergebnisse gruppiert nach der PLZ aus.
* - identifier: Adoptionen nach PLZ
* - type: Boolean
SCALAR sca_InGeneral_logResultAdoptionsZip

* - domain: [0|1]
* - description: Gibt die Adoptionsergebnisse gruppiert nach dem PLZ und Phase aus.
* - identifier: kumulierte Adoptionen nach PLZ und Phase
* - type: Boolean
SCALAR sca_InGeneral_logResultAdoptionsZipPhase

* - domain: [0|1]
* - description: Gibt zu jedem Agenten alle Adoptionsinformationen aus. Nichtadopter werden ebenfalls ausgegeben.
* - identifier: Komplettausgabe
* - type: Boolean
SCALAR sca_InGeneral_logResultAdoptionsAll

* - identifier: logScriptAdoptionsZip
* - type: Boolean
SCALAR sca_InGeneral_logScriptAdoptionsZip

* - identifier: logScriptAdoptionsZipPhase
* - type: Boolean
SCALAR sca_InGeneral_logScriptAdoptionsZipPhase

* - identifier: InInformation
* - type: String
SET set_InInformation(*)

* - description: Ungenutzter Platzhalter
* - identifier: ---
* - type: Float
PARAMETER par_InInformation_placeholder(set_InInformation)

* - identifier: InIRPactVersion
* - type: String
SET set_InIRPactVersion(*)

* - description: Ungenutzter Platzhalter
* - identifier: ---
* - type: Integer
PARAMETER par_InIRPactVersion_placeholder(set_InIRPactVersion)

* - identifier: InScenarioVersion
* - type: String
SET set_InScenarioVersion(*)

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
* - description: xor test
* - identifier: g0 v0
* - rule: IF (par_InTestData_value01 == 1, par_InTestData_value02 = 0)
* - rule: IF (par_InTestData_value01 == 0, par_InTestData_value02 = 1)
* - type: Boolean
PARAMETER par_InTestData_value01(set_InTestData)

* - description: xor test
* - identifier: g0 v1
* - rule: IF (par_InTestData_value02 == 1, par_InTestData_value01 = 0)
* - rule: IF (par_InTestData_value02 == 0, par_InTestData_value01 = 1)
* - type: Boolean
PARAMETER par_InTestData_value02(set_InTestData)

* - default: 1
* - description: default
* - identifier: g1 v0 (default)
* - rule: IF (par_InTestData_value11 == 1, par_InTestData_value12 = 0)
* - rule: IF (par_InTestData_value11 == 1, par_InTestData_value13 = 0)
* - rule: IF (par_InTestData_value11 == 1, par_InTestData_value14 = 0)
* - rule: IF (par_InTestData_value11 == 0, par_InTestData_value11 = 1)
* - rule: IF (par_InTestData_value11 == 0, par_InTestData_value12 = 0)
* - rule: IF (par_InTestData_value11 == 0, par_InTestData_value13 = 0)
* - rule: IF (par_InTestData_value11 == 0, par_InTestData_value14 = 0)
* - type: Boolean
PARAMETER par_InTestData_value11(set_InTestData)

* - description: default
* - identifier: g1 v1
* - rule: IF (par_InTestData_value12 == 1, par_InTestData_value11 = 0)
* - rule: IF (par_InTestData_value12 == 1, par_InTestData_value13 = 0)
* - rule: IF (par_InTestData_value12 == 1, par_InTestData_value14 = 0)
* - rule: IF (par_InTestData_value12 == 0, par_InTestData_value11 = 1)
* - rule: IF (par_InTestData_value12 == 0, par_InTestData_value13 = 0)
* - rule: IF (par_InTestData_value12 == 0, par_InTestData_value14 = 0)
* - type: Boolean
PARAMETER par_InTestData_value12(set_InTestData)

* - description: default
* - identifier: g1 v2
* - rule: IF (par_InTestData_value13 == 1, par_InTestData_value11 = 0)
* - rule: IF (par_InTestData_value13 == 1, par_InTestData_value12 = 0)
* - rule: IF (par_InTestData_value13 == 1, par_InTestData_value14 = 0)
* - rule: IF (par_InTestData_value13 == 0, par_InTestData_value11 = 1)
* - rule: IF (par_InTestData_value13 == 0, par_InTestData_value12 = 0)
* - rule: IF (par_InTestData_value13 == 0, par_InTestData_value14 = 0)
* - type: Boolean
PARAMETER par_InTestData_value13(set_InTestData)

* - description: default
* - identifier: g1 v3
* - rule: IF (par_InTestData_value14 == 1, par_InTestData_value11 = 0)
* - rule: IF (par_InTestData_value14 == 1, par_InTestData_value12 = 0)
* - rule: IF (par_InTestData_value14 == 1, par_InTestData_value13 = 0)
* - rule: IF (par_InTestData_value14 == 0, par_InTestData_value11 = 1)
* - rule: IF (par_InTestData_value14 == 0, par_InTestData_value12 = 0)
* - rule: IF (par_InTestData_value14 == 0, par_InTestData_value13 = 0)
* - type: Boolean
PARAMETER par_InTestData_value14(set_InTestData)

* - default: 1
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
