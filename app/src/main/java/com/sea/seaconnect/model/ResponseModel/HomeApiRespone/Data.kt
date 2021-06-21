package com.sea.seaconnect.model.ResponseModel.HomeApiRespone


import com.google.gson.annotations.SerializedName

data class Data(
    @SerializedName("interactionData")
    val interactionData: List<InteractionData> = listOf(),
    @SerializedName("notificationCount")
    val notificationCount: Int = 0
)