package com.example.team16_medassist.model

import com.squareup.moshi.Json


data class MapMatricesModel (

    @Json(name = "destination_addresses") var destinationAddresses : List<String>,
    @Json(name = "origin_addresses") var originAddresses : List<String>,
    @Json(name = "rows") var rows : List<Rows>,
    @Json(name = "status") var status : String

)


data class Distance (

    @Json(name = "text") var text : String,
    @Json(name = "value") var value : Int

)

data class Duration (

    @Json(name = "text") var text : String,
    @Json(name = "value") var value : Int

)

data class Elements (

    @Json(name = "distance") var distance : Distance,
    @Json(name = "duration") var duration : Duration,
    @Json(name = "status") var status : String

)

data class Rows (

    @Json(name = "elements") var elements : List<Elements>

)