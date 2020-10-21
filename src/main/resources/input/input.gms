* - type: Float
* - description: Mehrwertsteuer
* - identifier: MWST
SCALAR sca_Tax_PS_vat

* - type: String
* - description: Marktteilnehmer
* - identifier: MT
SET set_side(*)

* - type: String
* - description: Kundengruppe in IRPact
* - identifier: KG
SET set_side_cust(set_side)

* - type: Integer
* - default: 10
* - description: Anzahl der Kunden
* - identifier: KGA
PARAMETER par_S_DS(set_side_cust)

* - type: Integer
* - default: 5
* - description: Erhöht die Anzahl der Kunden in der Gruppe um den gewünschten Wert.
* - identifier: KGAM
PARAMETER par_kg_modifier(set_side_cust)

* - type: TimeSeries
* - description: Simulationshorizont
* - identifier: SH
* - hidden: 1
SET set_ii(*)

* - type: Float
* - unit: [EUR]
* - description: Stromsparte je Kundengruppe
* - identifier: SK
PARAMETER par_IuO_ESector_CustSide(set_ii,set_side)
