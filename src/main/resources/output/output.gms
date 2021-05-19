* - description: todo
* - identifier: Adoptionen
* - type: Integer
PARAMETER par_out_InGeneralConsumerAgentGroup_adoptionsThisYear(set_InGeneralConsumerAgentGroup)

* - description: todo
* - identifier: Adoptionen (kumulativ)
* - type: Integer
PARAMETER par_out_InGeneralConsumerAgentGroup_adoptionsCumulativ(set_InGeneralConsumerAgentGroup)

* - description: todo
* - identifier: Adoptionen (Anteil)
* - type: Float
PARAMETER par_out_InGeneralConsumerAgentGroup_adoptionShareThisYear(set_InGeneralConsumerAgentGroup)

* - description: todo
* - identifier: Adoptionen (kumulierter anteil)
* - type: Float
PARAMETER par_out_InGeneralConsumerAgentGroup_adoptionShareCumulativ(set_InGeneralConsumerAgentGroup)

* - identifier: OutAnnualAdoptionData
* - type: String
SET set_OutAnnualAdoptionData(set_OutEntity)

* - description: todo
* - identifier: Jahr
* - type: Integer
PARAMETER par_out_OutAnnualAdoptionData_year(set_OutAnnualAdoptionData)

* - description: todo
* - identifier: Adoptionen
* - type: Integer
PARAMETER par_out_map_OutAnnualAdoptionData_InConsumerAgentGroup_adoptionsThisYear(set_OutAnnualAdoptionData,set_InConsumerAgentGroup)

* - description: todo
* - identifier: Adoptionen (kumulativ)
* - type: Integer
PARAMETER par_out_map_OutAnnualAdoptionData_InConsumerAgentGroup_adoptionsCumulativ(set_OutAnnualAdoptionData,set_InConsumerAgentGroup)

* - description: todo
* - identifier: Adoptionen (Anteil)
* - type: Float
PARAMETER par_out_map_OutAnnualAdoptionData_InConsumerAgentGroup_adoptionShareThisYear(set_OutAnnualAdoptionData,set_InConsumerAgentGroup)

* - description: todo
* - identifier: Adoptionen (kumulierter anteil)
* - type: Float
PARAMETER par_out_map_OutAnnualAdoptionData_InConsumerAgentGroup_adoptionShareCumulativ(set_OutAnnualAdoptionData,set_InConsumerAgentGroup)

* - identifier: OutEntity
* - hidden: 1
* - type: String
SET set_OutEntity(*)

* - description: Einzigartige ID der Daten.
* - hidden: 1
* - identifier: BinaryPersistDataID
* - type: Integer
PARAMETER par_out_BinaryPersistData_id(set_BinaryPersistData)

* - description: Neue Anzahl der Kunden
* - identifier: OUTKGA
* - type: Integer
PARAMETER par_out_S_DS(set_side_cust)

* - description: Summe der Zeitreihe oder ID multipliziert mit der Mwst.
* - identifier: SNS
* - type: Float
PARAMETER par_out_IuOSonnentankNetzversorgung_Summe(set_side_cust)
