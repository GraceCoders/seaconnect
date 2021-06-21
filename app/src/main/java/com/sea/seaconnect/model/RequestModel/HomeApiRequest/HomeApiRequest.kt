package com.sea.seaconnect.model.RequestModel.HomeApiRequest


import com.google.gson.annotations.SerializedName

data class HomeApiRequest(
    @SerializedName("interactions")
    var interactions: List<String> = listOf(),
    @SerializedName("latitude")
    var latitude: String = "",
    @SerializedName("longitude")
    var longitude: String = ""
)