* - description: AdaptionRate
* - type: String
* - identifier: AdaptionRate
SET set_AdaptionRate(*)

* - description: group
* - type: Boolean
* - identifier: group
PARAMETER par_out_link_AdaptionRate_AgentGroup_group(set_AdaptionRate,set_AgentGroup)

* - description: need
* - type: Boolean
* - identifier: need
PARAMETER par_out_link_AdaptionRate_Need_need(set_AdaptionRate,set_Need)

* - description: rate
* - type: Float
* - identifier: rate
PARAMETER par_out_AdaptionRate_rate(set_AdaptionRate)
