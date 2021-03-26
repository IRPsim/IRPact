* - identifier: OutAdoptionResult
* - type: String
SET set_OutAdoptionResult(*)

* - identifier: adoptions
* - type: Integer
PARAMETER par_out_OutAdoptionResult_adoptions(set_OutAdoptionResult)

* - identifier: adoptionShare
* - type: Float
PARAMETER par_out_OutAdoptionResult_adoptionShare(set_OutAdoptionResult)

* - identifier: id
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
