package com.sea.seaconnect.model.ResponseModel.Intereaction_list


import com.google.gson.annotations.SerializedName

data class Intereaction_list_response(
    @SerializedName("code")
    val code: Int = 0,
    @SerializedName("data")
    val `data`: List<Data> = listOf(),
    @SerializedName("message")
    val message: String = "",
    @SerializedName("success")
    val success: Boolean = false
)