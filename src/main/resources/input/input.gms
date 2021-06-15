* - identifier: InAffinities
* - type: String
SET set_InAffinities(*)

* - description: Die Affinit�te, welche in dieser Menge enthalten sein sollen.
* - identifier: Eintr�ge
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

* - description: Der Affinit�tswert der Ausgangsgruppe gegen�ber der Zielgruppe.
* - identifier: Wert
* - type: Float
PARAMETER par_InComplexAffinityEntry_affinityValue(set_InComplexAffinityEntry)

* - identifier: InNameSplitAffinityEntry
* - type: String
SET set_InNameSplitAffinityEntry(set_InAffinityEntry)

* - description: Der Affinit�tswert der Ausgangsgruppe gegen�ber der Zielgruppe.
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
SET set_InGeneralConsumerAgentAnnualGroupAttribute(set_InDependentConsumerAgentGroupAttribute,set_InConsumerAgentGroupAttribute)

* - description: Name des Attributes f�r das Gruppenattribut.
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

* - description: Zu dieser Gruppe geh�rendes Attribut.
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

* - identifier: InGeneralConsumerAgentGroupAttribute
* - type: String
SET set_InGeneralConsumerAgentGroupAttribute(set_InDependentConsumerAgentGroupAttribute,set_InConsumerAgentGroupAttribute)

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
SET set_InIndependentConsumerAgentGroupAttribute(set_InConsumerAgentGroupAttribute)

* - identifier: InNameSplitConsumerAgentGroupAttribute
* - type: String
SET set_InNameSplitConsumerAgentGroupAttribute(set_InIndependentConsumerAgentGroupAttribute,set_InConsumerAgentGroupAttribute)

* - description: Legt die Verteilungsfunktion fest, auf deren Basis die Attributwerte gezogen werden.
* - identifier: Verteilungsfunktion
* - type: Boolean
PARAMETER par_link_InNameSplitConsumerAgentGroupAttribute_InUnivariateDoubleDistribution_dist(set_InNameSplitConsumerAgentGroupAttribute,set_InUnivariateDoubleDistribution)

* - identifier: InPVactConsumerAgentGroup
* - type: String
SET set_InPVactConsumerAgentGroup(set_InConsumerAgentGroup)

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

* - description: rateOfConvergence
* - identifier: rateOfConvergence
* - type: Boolean
PARAMETER par_link_InPVactConsumerAgentGroup_InUnivariateDoubleDistribution_rateOfConvergence(set_InPVactConsumerAgentGroup,set_InUnivariateDoubleDistribution)

* - description: initialProductInterest
* - identifier: initialProductInterest
* - type: Boolean
PARAMETER par_link_InPVactConsumerAgentGroup_InUnivariateDoubleDistribution_initialProductInterest(set_InPVactConsumerAgentGroup,set_InUnivariateDoubleDistribution)

* - description: initialProductAwareness
* - identifier: initialProductAwareness
* - type: Boolean
PARAMETER par_link_InPVactConsumerAgentGroup_InUnivariateDoubleDistribution_initialProductAwareness(set_InPVactConsumerAgentGroup,set_InUnivariateDoubleDistribution)

* - description: constructionRate
* - identifier: constructionRate
* - type: Boolean
PARAMETER par_link_InPVactConsumerAgentGroup_InUnivariateDoubleDistribution_constructionRate(set_InPVactConsumerAgentGroup,set_InUnivariateDoubleDistribution)

* - description: renovationRate
* - identifier: renovationRate
* - type: Boolean
PARAMETER par_link_InPVactConsumerAgentGroup_InUnivariateDoubleDistribution_renovationRate(set_InPVactConsumerAgentGroup,set_InUnivariateDoubleDistribution)

* - description: spatialDistribution
* - identifier: spatialDistribution
* - type: Boolean
PARAMETER par_link_InPVactConsumerAgentGroup_InSpatialDistribution_spatialDistribution(set_InPVactConsumerAgentGroup,set_InSpatialDistribution)

* - identifier: InFixConsumerAgentPopulation
* - type: String
SET set_InFixConsumerAgentPopulation(set_InAgentPopulation)

* - description: Legt die Anzahl der Agenten bzw. die Populationsgr��e f�r diese Konsumergruppe fest.
* - identifier: Anzahl der Agenten
* - type: Integer
PARAMETER par_InFixConsumerAgentPopulation_size(set_InFixConsumerAgentPopulation)

* - description: Legt die Konsumergruppen fest, welche diese Populationsgr��e haben sollen.
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

* - description: Legt die maximale Populationsgr��e f�r alle hier verwendeten Konsumergruppen fest. Falls der Wert kleiner 0 ist, wird die maximal m�gliche Anzahl verwendet.
* - identifier: Gew�nschte Anzahl der Agenten
* - type: Integer
PARAMETER par_InFileBasedConsumerAgentPopulation_desiredSize(set_InFileBasedConsumerAgentPopulation)

* - description: Legt fest, ob die gesamte Populationsgr��e aus der Datei verwendet werden soll. Falls diese Funktion gesetzt ist, wird die gew�nschte Anzahl der Agenten ignoriert.
* - identifier: Maximal m�gliche Anzahl nutzen?
* - type: Boolean
PARAMETER par_InFileBasedConsumerAgentPopulation_useAll(set_InFileBasedConsumerAgentPopulation)

* - description: Falls diese Funktion aktiviert ist, muss die gew�nschte Anzahl Agenten existieren. Ist zum Beispiel eine Population von 10000 gew�nscht, aber die Datei enth�lt nur 1000 Agenten, so wird ein Fehler geworfen. Falls die Funktion deaktiviert ist, wird die Population im Notfall angepasst.
* - identifier: Populationsgr��e erzwingen?
* - type: Boolean
PARAMETER par_InFileBasedConsumerAgentPopulation_requiresDesiredTotalSize(set_InFileBasedConsumerAgentPopulation)

* - description: Legt die Konsumergruppen fest, welche ber�cksichtigt werden sollen.
* - identifier: Konsumergruppen
* - type: Boolean
PARAMETER par_link_InFileBasedConsumerAgentPopulation_InConsumerAgentGroup_cags(set_InFileBasedConsumerAgentPopulation,set_InConsumerAgentGroup)

* - description: Tabellarische Eingabedatei, aus der die Informationen gelesen werden sollen.
* - identifier: Tabellarische Datei
* - type: Boolean
PARAMETER par_link_InFileBasedConsumerAgentPopulation_InSpatialTableFile_file(set_InFileBasedConsumerAgentPopulation,set_InSpatialTableFile)

* - description: Schl�ssel, nachdem die Datei gefiltert wird. Wichtig: Die entsprechenden Daten in der Datei m�ssen dem Konsumergruppennamen entsprechen.
* - identifier: Auswahlschl�ssel
* - type: Boolean
PARAMETER par_link_InFileBasedConsumerAgentPopulation_InAttributeName_selectKey(set_InFileBasedConsumerAgentPopulation,set_InAttributeName)

* - identifier: InFileBasedPVactConsumerAgentPopulation
* - type: String
SET set_InFileBasedPVactConsumerAgentPopulation(set_InAgentPopulation)

* - description: Legt die maximale Populationsgr��e f�r alle hier verwendeten Konsumergruppen fest. Falls der Wert kleiner 0 ist, wird die maximal m�gliche Anzahl verwendet.
* - identifier: Gew�nschte Anzahl der Agenten
* - type: Integer
PARAMETER par_InFileBasedPVactConsumerAgentPopulation_desiredSize(set_InFileBasedPVactConsumerAgentPopulation)

* - description: Legt fest, ob die gesamte Populationsgr��e aus der Datei verwendet werden soll. Falls diese Funktion gesetzt ist, wird die gew�nschte Anzahl der Agenten ignoriert.
* - identifier: Maximal m�gliche Anzahl nutzen?
* - type: Boolean
PARAMETER par_InFileBasedPVactConsumerAgentPopulation_useAll(set_InFileBasedPVactConsumerAgentPopulation)

* - description: Falls diese Funktion aktiviert ist, muss die gew�nschte Anzahl Agenten existieren. Ist zum Beispiel eine Population von 10000 gew�nscht, aber die Datei enth�lt nur 1000 Agenten, so wird ein Fehler geworfen. Falls die Funktion deaktiviert ist, wird die Population im Notfall angepasst.
* - identifier: Populationsgr��e erzwingen?
* - type: Boolean
PARAMETER par_InFileBasedPVactConsumerAgentPopulation_requiresDesiredTotalSize(set_InFileBasedPVactConsumerAgentPopulation)

* - description: Legt die Konsumergruppen fest, welche ber�cksichtigt werden sollen.
* - identifier: Konsumergruppen
* - type: Boolean
PARAMETER par_link_InFileBasedPVactConsumerAgentPopulation_InConsumerAgentGroup_cags(set_InFileBasedPVactConsumerAgentPopulation,set_InConsumerAgentGroup)

* - description: Tabellarische Eingabedatei, aus der die Informationen gelesen werden sollen.
* - identifier: Tabellarische Datei
* - type: Boolean
PARAMETER par_link_InFileBasedPVactConsumerAgentPopulation_InSpatialTableFile_file(set_InFileBasedPVactConsumerAgentPopulation,set_InSpatialTableFile)

* - description: Bin�re Daten f�r diverse Funktionalit�ten
* - identifier: Bin�re Daten
* - type: String
SET set_VisibleBinaryData(*)

* - description: Spezielle ID der Daten, Verwendungszweck und Funktionsweise ist von den Daten selber abh�ngig.
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

* - domain: [0,1]
* - description: Wahrscheinlichkeit p
* - identifier: p
* - type: Float
PARAMETER par_InBernoulliDistribution_p(set_InBernoulliDistribution)

* - default: 1
* - description: Dieser Wert wird mit der Wahrscheinlichkeit p zur�ck gegeben.
* - identifier: x
* - type: Float
PARAMETER par_InBernoulliDistribution_trueValue(set_InBernoulliDistribution)

* - default: 0
* - description: Dieser Wert wird mit der Wahrscheinlichkeit 1-p zur�ck gegeben.
* - identifier: y
* - type: Float
PARAMETER par_InBernoulliDistribution_falseValue(set_InBernoulliDistribution)

* - identifier: InBooleanDistribution
* - type: String
SET set_InBooleanDistribution(set_InUnivariateDoubleDistribution)

* - default: 1
* - description: Der Wert, welcher zur�ckgegeben wird, wenn die Funktion intern 1 (WAHR) ergibt.
* - identifier: x
* - type: Float
PARAMETER par_InBooleanDistribution_trueValue(set_InBooleanDistribution)

* - default: 0
* - description: Der Wert, welcher zur�ckgegeben wird, wenn die Funktion intern 0 (FALSCH) ergibt.
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
* - identifier: Obergrenze (exklusiv)
* - type: Float
PARAMETER par_InBoundedNormalDistribution_upperBound(set_InBoundedNormalDistribution)

* - identifier: InDiracUnivariateDistribution
* - type: String
SET set_InDiracUnivariateDistribution(set_InUnivariateDoubleDistribution)

* - description: Legt den konstante R�ckgabewert fest.
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

* - default: AnnualAdoptions, ComparedAnnualAdoptions, AnnualCumulativeAdoptions
* - identifier: InGenericOutputImage
* - type: String
SET set_InGenericOutputImage(set_InOutputImage)

* - default: 1
* - domain: [1, 2]
* - description: Bestimmt das Programm, mit dem die Bilder erzeugt werden sollen. IRPact unterst�tzt folgende Programme: R (1) und gnuplot (2).
* - identifier: Visualisierungsprogramm
* - type: Integer
PARAMETER par_InGenericOutputImage_engine(set_InGenericOutputImage)

* - domain: [0, 3]
* - description: Gibt an, welche Daten visualisiert werden sollen. Aktuell unterst�tzt: 
* - identifier: Datenvisualisierung (Name noch anpassen)
* - type: Integer
PARAMETER par_InGenericOutputImage_mode(set_InGenericOutputImage)

* - description: Speichert auf Wunsch das Skript f�r die Bilderzeugung.
* - identifier: Skript speichern?
* - type: Boolean
PARAMETER par_InGenericOutputImage_storeScript(set_InGenericOutputImage)

* - description: Speichert auf Wunsch die Daten f�r die Bilderzeugung.
* - identifier: Daten speichern?
* - type: Boolean
PARAMETER par_InGenericOutputImage_storeData(set_InGenericOutputImage)

* - description: Erzeugt und speichert auf Wunsch das erzeugte Bild.
* - identifier: Bild speichern?
* - type: Boolean
PARAMETER par_InGenericOutputImage_storeImage(set_InGenericOutputImage)

* - default: 1
* - domain: (0,)
* - description: Gibt die Linienst�rke an. Dieser Wert hat nur auf line plots Einfluss.
* - identifier: Linienst�rke
* - type: Float
PARAMETER par_InGenericOutputImage_linewidth(set_InGenericOutputImage)

* - identifier: InGnuPlotOutputImage
* - type: String
SET set_InGnuPlotOutputImage(set_InOutputImage)

* - domain: [0, 3]
* - description: Gibt an, welche Daten visualisiert werden sollen.
* - identifier: Datenvisualisierung (Name noch anpassen)
* - type: Integer
PARAMETER par_InGnuPlotOutputImage_mode(set_InGnuPlotOutputImage)

* - description: Speichert auf Wunsch das Skript f�r die Bilderzeugung.
* - identifier: Skript speichern?
* - type: Boolean
PARAMETER par_InGnuPlotOutputImage_storeScript(set_InGnuPlotOutputImage)

* - description: Speichert auf Wunsch die Daten f�r die Bilderzeugung.
* - identifier: Daten speichern?
* - type: Boolean
PARAMETER par_InGnuPlotOutputImage_storeData(set_InGnuPlotOutputImage)

* - description: Erzeugt und speichert auf Wunsch das erzeugte Bild.
* - identifier: Bild speichern?
* - type: Boolean
PARAMETER par_InGnuPlotOutputImage_storeImage(set_InGnuPlotOutputImage)

* - default: 1
* - domain: (0,)
* - identifier: linewidth
* - type: Float
PARAMETER par_InGnuPlotOutputImage_linewidth(set_InGnuPlotOutputImage)

* - identifier: InOutputImage
* - hidden: 1
* - type: String
SET set_InOutputImage(*)

* - identifier: InROutputImage
* - type: String
SET set_InROutputImage(set_InOutputImage)

* - domain: [0, 3]
* - description: Gibt an, welche Daten visualisiert werden sollen.
* - identifier: Datenvisualisierung (Name noch anpassen)
* - type: Integer
PARAMETER par_InROutputImage_mode(set_InROutputImage)

* - description: Speichert auf Wunsch das Skript f�r die Bilderzeugung.
* - identifier: Skript speichern?
* - type: Boolean
PARAMETER par_InROutputImage_storeScript(set_InROutputImage)

* - description: Speichert auf Wunsch die Daten f�r die Bilderzeugung.
* - identifier: Daten speichern?
* - type: Boolean
PARAMETER par_InROutputImage_storeData(set_InROutputImage)

* - description: Erzeugt und speichert auf Wunsch das erzeugte Bild.
* - identifier: Bild speichern?
* - type: Boolean
PARAMETER par_InROutputImage_storeImage(set_InROutputImage)

* - default: 1
* - domain: (0,)
* - identifier: linewidth
* - type: Float
PARAMETER par_InROutputImage_linewidth(set_InROutputImage)

* - identifier: InConsumerAgentGroupColor
* - type: String
SET set_InConsumerAgentGroupColor(*)

* - description: Die Gruppen, welche die ausgew�hlte Farbe verwenden soll.
* - identifier: Konsumergruppe
* - type: Boolean
PARAMETER par_link_InConsumerAgentGroupColor_InConsumerAgentGroup_group(set_InConsumerAgentGroupColor,set_InConsumerAgentGroup)

* - description: Die f�r diese Gruppe verwendete Farbe.
* - identifier: Farbe
* - type: Boolean
PARAMETER par_link_InConsumerAgentGroupColor_GraphvizColor_color(set_InConsumerAgentGroupColor,set_GraphvizColor)

* - identifier: InProductGroupThresholdEntry
* - type: String
SET set_InProductGroupThresholdEntry(*)

* - description: Legt die Verteilungsfunktion f�r den Grenzwert fest.
* - identifier: Verteilungsfunktion f�r den Grenzwert
* - type: Boolean
PARAMETER par_link_InProductGroupThresholdEntry_InUnivariateDoubleDistribution_interestDistribution(set_InProductGroupThresholdEntry,set_InUnivariateDoubleDistribution)

* - description: Legt die Produktgruppen fest, zu denen der definierte Grenzwert geh�ren soll.
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

* - description: Grenzwerte ab dem Produkte der zugeh�rigen Produktgruppen von Interesse werden.
* - identifier: Eintr�ge
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

* - description: Legt die zu nutzende Distanzfunktion f�r den Einfluss des Abstandes fest.
* - identifier: Distanzfunktion
* - type: Boolean
PARAMETER par_link_InFreeNetworkTopology_InDistanceEvaluator_distanceEvaluator(set_InFreeNetworkTopology,set_InDistanceEvaluator)

* - description: Legt die Anzahl def�r die jeweiligen Konsumergruppen fest.
* - identifier: Konsumergruppenspezifische Kantenanzahl
* - type: Boolean
PARAMETER par_link_InFreeNetworkTopology_InNumberOfTies_numberOfTies(set_InFreeNetworkTopology,set_InNumberOfTies)

* - description: Legt die zu nutzende Affinit�tenmenge f�r den sozialen Einfluss fest.
* - identifier: Affinit�ten
* - type: Boolean
PARAMETER par_link_InFreeNetworkTopology_InAffinities_affinities(set_InFreeNetworkTopology,set_InAffinities)

* - description: Legt das initiale Gewicht der Kanten fest.
* - identifier: Initiales Kantengewicht
* - type: Float
PARAMETER par_InFreeNetworkTopology_initialWeight(set_InFreeNetworkTopology)

* - description: Erlaubt es auch weniger Kanten als gew�nscht zu nutzen. Sollten zum Beispiel 10 Kanten gew�nscht sein je Knoten, aber f�r einen bestimmten Knoten nur 5 m�glich sein, so werden nur 5 verwendet. Falls diese Option deaktiviert ist, wird ein Fehler geworfen.
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

* - identifier: InAutoUncertaintyGroupAttribute
* - type: String
SET set_InAutoUncertaintyGroupAttribute(set_InUncertaintyGroupAttribute)

* - description: Ungenutzter Platzhalter
* - identifier: ---
* - type: Float
PARAMETER par_InAutoUncertaintyGroupAttribute_placeholderAutoUncert(set_InAutoUncertaintyGroupAttribute)

* - identifier: InDisabledProcessPlanNodeFilterScheme
* - type: String
SET set_InDisabledProcessPlanNodeFilterScheme(set_InRAProcessPlanNodeFilterScheme,set_InProcessPlanNodeFilterScheme)

* - description: Ungenutzter Platzhalter
* - identifier: ---
* - type: Float
PARAMETER par_InDisabledProcessPlanNodeFilterScheme_placeholder(set_InDisabledProcessPlanNodeFilterScheme)

* - identifier: InEntireNetworkNodeFilterScheme
* - type: String
SET set_InEntireNetworkNodeFilterScheme(set_InRAProcessPlanNodeFilterScheme,set_InProcessPlanNodeFilterScheme)

* - description: Ungenutzter Platzhalter
* - identifier: ---
* - type: Float
PARAMETER par_InEntireNetworkNodeFilterScheme_placeholder(set_InEntireNetworkNodeFilterScheme)

* - identifier: InIndividualAttributeBasedUncertaintyGroupAttribute
* - type: String
SET set_InIndividualAttributeBasedUncertaintyGroupAttribute(set_InUncertaintyGroupAttribute)

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
SET set_InIndividualAttributeBasedUncertaintyWithConvergenceGroupAttribute(set_InUncertaintyGroupAttribute)

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
SET set_InNameBasedUncertaintyGroupAttribute(set_InUncertaintyGroupAttribute)

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
SET set_InNameBasedUncertaintyWithConvergenceGroupAttribute(set_InUncertaintyGroupAttribute)

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
SET set_InPVactUncertaintyGroupAttribute(set_InUncertaintyGroupAttribute)

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
SET set_InRAProcessModel(set_InProcessModel)

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

* - description: Legt den Filter fest, um 'sichtbare' Haushalte zu identifizieren.
* - identifier: Netzwerkfilter f�r r�umliche Sicht
* - type: Boolean
PARAMETER par_link_InRAProcessModel_InRAProcessPlanNodeFilterScheme_nodeFilterScheme(set_InRAProcessModel,set_InRAProcessPlanNodeFilterScheme)

* - description: Legt die zu nutzende PV-Datei fest.
* - identifier: PV-Datei
* - type: Boolean
PARAMETER par_link_InRAProcessModel_InPVFile_pvFile(set_InRAProcessModel,set_InPVFile)

* - description: Bestimmt die Unsicherheiten, welche von diesem Modell verwendet werden.
* - identifier: Unsicherheiten
* - type: Boolean
PARAMETER par_link_InRAProcessModel_InUncertaintyGroupAttribute_uncertaintyGroupAttributes(set_InRAProcessModel,set_InUncertaintyGroupAttribute)

* - identifier: InRAProcessPlanMaxDistanceFilterScheme
* - type: String
SET set_InRAProcessPlanMaxDistanceFilterScheme(set_InRAProcessPlanNodeFilterScheme,set_InProcessPlanNodeFilterScheme)

* - description: Legt die maximale Entfernung fest. Die Einheit richtet sich sowohl nach den Ausgangsdaten als auch der verwendeten Metric im r�umlichen Modell.
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

* - identifier: InUncertaintyGroupAttribute
* - hidden: 1
* - type: String
SET set_InUncertaintyGroupAttribute(*)

* - identifier: InProcessModel
* - hidden: 1
* - type: String
SET set_InProcessModel(*)

* - identifier: InProcessPlanNodeFilterScheme
* - hidden: 1
* - type: String
SET set_InProcessPlanNodeFilterScheme(*)

* - identifier: InBasicProductGroup
* - type: String
SET set_InBasicProductGroup(set_InProductGroup)

* - description: Bestimmt die Attribute f�r diese Gruppe.
* - identifier: Attribute
* - type: Boolean
PARAMETER par_link_InBasicProductGroup_InDependentProductGroupAttribute_pgAttributes(set_InBasicProductGroup,set_InDependentProductGroupAttribute)

* - identifier: InBasicProductGroupAttribute
* - type: String
SET set_InBasicProductGroupAttribute(set_InDependentProductGroupAttribute,set_InProductGroupAttribute)

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
SET set_InFixProductAttribute(*)

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
SET set_InNameSplitProductGroupAttribute(set_InIndependentProductGroupAttribute,set_InProductGroupAttribute)

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
SET set_InFileBasedPVactMilieuSupplier(set_InSpatialDistributionWithCollection,set_InSpatialDistribution)

* - identifier: file
* - type: Boolean
PARAMETER par_link_InFileBasedPVactMilieuSupplier_InSpatialTableFile_file(set_InFileBasedPVactMilieuSupplier,set_InSpatialTableFile)

* - identifier: InFileBasedPVactMilieuZipSupplier
* - type: String
SET set_InFileBasedPVactMilieuZipSupplier(set_InSpatialDistributionWithCollection,set_InSpatialDistribution)

* - identifier: file
* - type: Boolean
PARAMETER par_link_InFileBasedPVactMilieuZipSupplier_InSpatialTableFile_file(set_InFileBasedPVactMilieuZipSupplier,set_InSpatialTableFile)

* - identifier: InFileBasedSelectGroupSpatialInformationSupplier
* - type: String
SET set_InFileBasedSelectGroupSpatialInformationSupplier(set_InSpatialDistributionWithCollection,set_InSpatialDistribution)

* - description: Bestimmt den Schl�ssel f�r die Auswahl der X-Position in der tabellarischen Datei (Spaltenname).
* - identifier: X-Schl�ssel
* - type: Boolean
PARAMETER par_link_InFileBasedSelectGroupSpatialInformationSupplier_InAttributeName_xPositionKey(set_InFileBasedSelectGroupSpatialInformationSupplier,set_InAttributeName)

* - description: Bestimmt den Schl�ssel f�r die Auswahl der Y-Position in der tabellarischen Datei (Spaltenname).
* - identifier: Y-Schl�ssel
* - type: Boolean
PARAMETER par_link_InFileBasedSelectGroupSpatialInformationSupplier_InAttributeName_yPositionKey(set_InFileBasedSelectGroupSpatialInformationSupplier,set_InAttributeName)

* - description: Bestimmt den Schl�ssel f�r die Auswahl der ID in der tabellarischen Datei (Spaltenname).
* - identifier: ID-Schl�ssel
* - type: Boolean
PARAMETER par_link_InFileBasedSelectGroupSpatialInformationSupplier_InAttributeName_idKey(set_InFileBasedSelectGroupSpatialInformationSupplier,set_InAttributeName)

* - identifier: file
* - type: Boolean
PARAMETER par_link_InFileBasedSelectGroupSpatialInformationSupplier_InSpatialTableFile_file(set_InFileBasedSelectGroupSpatialInformationSupplier,set_InSpatialTableFile)

* - description: Bestimmt den Schl�ssel f�r die Auswahl der Milieuinformation in der tabellarischen Datei (Spaltenname).
* - identifier: Auswahlschl�ssel
* - type: Boolean
PARAMETER par_link_InFileBasedSelectGroupSpatialInformationSupplier_InAttributeName_selectKey(set_InFileBasedSelectGroupSpatialInformationSupplier,set_InAttributeName)

* - description: Bestimmt den Schl�ssel f�r die Gruppierung und Wichtung anhand der tabellarischen Datei (Spaltenname).
* - identifier: Gruppierungsschl�ssel
* - type: Boolean
PARAMETER par_link_InFileBasedSelectGroupSpatialInformationSupplier_InAttributeName_groupKey(set_InFileBasedSelectGroupSpatialInformationSupplier,set_InAttributeName)

* - identifier: InFileBasedSelectSpatialInformationSupplier
* - type: String
SET set_InFileBasedSelectSpatialInformationSupplier(set_InSpatialDistributionWithCollection,set_InSpatialDistribution)

* - description: Bestimmt den Schl�ssel f�r die Auswahl der X-Position in der tabellarischen Datei (Spaltenname).
* - identifier: X-Schl�ssel
* - type: Boolean
PARAMETER par_link_InFileBasedSelectSpatialInformationSupplier_InAttributeName_xPositionKey(set_InFileBasedSelectSpatialInformationSupplier,set_InAttributeName)

* - description: Bestimmt den Schl�ssel f�r die Auswahl der Y-Position in der tabellarischen Datei (Spaltenname).
* - identifier: Y-Schl�ssel
* - type: Boolean
PARAMETER par_link_InFileBasedSelectSpatialInformationSupplier_InAttributeName_yPositionKey(set_InFileBasedSelectSpatialInformationSupplier,set_InAttributeName)

* - description: Bestimmt den Schl�ssel f�r die Auswahl der ID in der tabellarischen Datei (Spaltenname).
* - identifier: ID-Schl�ssel
* - type: Boolean
PARAMETER par_link_InFileBasedSelectSpatialInformationSupplier_InAttributeName_idKey(set_InFileBasedSelectSpatialInformationSupplier,set_InAttributeName)

* - identifier: file
* - type: Boolean
PARAMETER par_link_InFileBasedSelectSpatialInformationSupplier_InSpatialTableFile_file(set_InFileBasedSelectSpatialInformationSupplier,set_InSpatialTableFile)

* - description: Bestimmt den Schl�ssel f�r die Auswahl der Information in der tabellarischen Datei (Spaltenname).
* - identifier: Auswahlschl�ssel
* - type: Boolean
PARAMETER par_link_InFileBasedSelectSpatialInformationSupplier_InAttributeName_selectKey(set_InFileBasedSelectSpatialInformationSupplier,set_InAttributeName)

* - identifier: InFileBasedSpatialInformationSupplier
* - type: String
SET set_InFileBasedSpatialInformationSupplier(set_InSpatialDistributionWithCollection,set_InSpatialDistribution)

* - description: Bestimmt den Schl�ssel f�r die Auswahl der X-Position in der tabellarischen Datei (Spaltenname).
* - identifier: X-Schl�ssel
* - type: Boolean
PARAMETER par_link_InFileBasedSpatialInformationSupplier_InAttributeName_xPositionKey(set_InFileBasedSpatialInformationSupplier,set_InAttributeName)

* - description: Bestimmt den Schl�ssel f�r die Auswahl der Y-Position in der tabellarischen Datei (Spaltenname).
* - identifier: Y-Schl�ssel
* - type: Boolean
PARAMETER par_link_InFileBasedSpatialInformationSupplier_InAttributeName_yPositionKey(set_InFileBasedSpatialInformationSupplier,set_InAttributeName)

* - description: Bestimmt den Schl�ssel f�r die Auswahl der ID in der tabellarischen Datei (Spaltenname).
* - identifier: ID-Schl�ssel
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
SET set_InSpatialModel(*)

* - identifier: InTimeModel
* - hidden: 1
* - type: String
SET set_InTimeModel(*)

* - identifier: InUnitStepDiscreteTimeModel
* - type: String
SET set_InUnitStepDiscreteTimeModel(set_InTimeModel)

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

* - identifier: InAboutPlaceholder
* - type: String
SET set_InAboutPlaceholder(*)

* - description: Ungenutzter Platzhalter
* - identifier: ---
* - type: Integer
PARAMETER par_InAboutPlaceholder_placeholder(set_InAboutPlaceholder)

* - description: Setzt den Seed f�r den Zufallsgenerator der Simulation. Falls er auf -1 gesetzt wird, wird ein zuf�lliger Seed generiert.
* - identifier: Zufallsgenerator (seed)
* - type: Integer
SCALAR sca_InGeneral_seed

* - description: Setzt den Timeout der Simulation in Millisekunden. Diese Einstellung dient dazu die Simulation zu beenden, falls sie unerwartet abst�rzt. Im Laufe der Simulation wird der Timeout unentwegt zur�ck gesetzt. Sollte es zu einem unerwarteten Fehler kommen und die R�cksetzung ausbleiben, wird die Simulation abgebrochen und beendet. Werte kleiner 1 deaktivieren den Timeout vollst�ndig.
* - identifier: Timeout
* - unit: [ms]
* - type: Integer
SCALAR sca_InGeneral_timeout

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
* - description: Einlesen der Zeitschrittl�nge der Simulationszeitreihen (bezogen auf eine Stunde Bsp. 15 Min = 0.25)
* - hidden: 1
* - identifier: Zeitschrittl�nge
* - type: Float
SCALAR sca_delta_ii

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

* - description: todo
* - identifier: Ausf�hrungsmodus
* - type: Integer
SCALAR sca_InGeneral_runMode

* - description: todo
* - identifier: Spezialszenario
* - type: Integer
SCALAR sca_InGeneral_scenarioMode

* - domain: [0,6]
* - description: Setzt das zu nutzende Logging-Level in IRPact, folgende Werte werden unterst�tzt: 0 = OFF, 1 = TRACE, 2 = DEBUG, 3 = INFO, 4 = WARN, 5 = ERROR, 6 = ALL. Das Level ist der Hauptfilter f�r alle log-Operationen.
* - identifier: Logging-Level
* - type: Integer
SCALAR sca_InGeneral_logLevel

* - domain: [0|1]
* - description: Aktiviert alle log-Operationen in allen Komponenten.
* - identifier: Alles loggen?
* - type: Boolean
SCALAR sca_InGeneral_logAll

* - domain: [0|1]
* - description: Aktiviert alle log-Operationen von IRPact.
* - identifier: IRPact loggen?
* - type: Boolean
SCALAR sca_InGeneral_logAllIRPact

* - domain: [0|1]
* - description: Aktiviert alle log-Operationen von IRPtools.
* - identifier: IRPtools-Bibliothek loggen?
* - type: Boolean
SCALAR sca_InGeneral_logAllTools

* - domain: [0|1]
* - description: Aktiviert das Logging des Initialisierungsprozesses. Dieser umfasst alles vom Start bis zum eigentlichen Start der Simulation.
* - identifier: Initialisierung loggen?
* - type: Boolean
SCALAR sca_InGeneral_logInitialization

* - domain: [0|1]
* - description: Aktiviert das Logging des Simulationsprozesses.
* - identifier: Simulationsprozess loggen?
* - type: Boolean
SCALAR sca_InGeneral_logSimulation

* - domain: [0|1]
* - description: Gibt an, ob die Graph�nderungen ausgegeben werden sollen.
* - identifier: Gibt die Graph�nderungen aus [GU]
* - type: Boolean
SCALAR sca_InGeneral_logGraphUpdate

* - domain: [0|1]
* - description: Gibt an, ob die Werte vom relative agreement Algorithmus ausgegeben werden sollen.
* - identifier: Gibt die 'relative agreement' Werte aus [RA]
* - type: Boolean
SCALAR sca_InGeneral_logRelativeAgreement

* - domain: [0|1]
* - description: Gibt an, ob �nderungen des Produktinteresses ausgegeben werden sollen.
* - identifier: Gibt �nderungen beim Produktinteresse aus [IU]
* - type: Boolean
SCALAR sca_InGeneral_logInterestUpdate

* - domain: [0|1]
* - description: Gibt an, ob die globalen und lokalen Adopteranteile ausgegeben werden sollen.
* - identifier: Gibt die globalen und lokalen Anteile aus [SNL]
* - type: Boolean
SCALAR sca_InGeneral_logShareNetworkLocal

* - domain: [0|1]
* - description: Gibt an, ob die Berechnung der finanziellen Komponente ausgegen werden soll.
* - identifier: Gibt die finanzielle Komponente aus [FC]
* - type: Boolean
SCALAR sca_InGeneral_logFinancalComponent

* - domain: [0|1]
* - description: Gibt an, ob die Berechnung beim decision making ausgegen werden sollen.
* - identifier: Gibt die Berechnungen beim decision making aus [DM]
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

* - domain: [0|1]
* - description: Gibt die Adoptionsergebnisse gruppiert nach der PLZ aus. Zus�tzlich wird das Script f�r eine line chart ausgegeben.
* - identifier: Adoptionen nach PLZ (line)
* - type: Boolean
SCALAR sca_InGeneral_logScriptAdoptionsZip

* - domain: [0|1]
* - description: Gibt die Adoptionsergebnisse gruppiert nach dem PLZ und Phase aus. Zus�tzlich wird das Skript f�r eine bar chart ausgegeben.
* - identifier: kumulierte Adoptionen nach PLZ und Phase (stacked bar)
* - type: Boolean
SCALAR sca_InGeneral_logScriptAdoptionsZipPhase

* - description: Die Version von IRPact, welche bei der Erstellung des Szenarios aktuell war.
* - hidden: 1
* - identifier: IRPact Version
* - type: String
SET set_InVersion(*)

* - description: Ungenutzter Platzhalter
* - identifier: ---
* - type: Integer
PARAMETER par_InVersion_placeholderVersion(set_InVersion)

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

* - description: Bitte geben Sie hier das absolute j�hrliche Wachstum der Kundengruppe an
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
* - description: Erh�ht die Anzahl der Kunden in der Gruppe um den gew�nschten Wert.
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
