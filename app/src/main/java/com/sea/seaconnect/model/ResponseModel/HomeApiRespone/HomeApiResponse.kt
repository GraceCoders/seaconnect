package com.sea.seaconnect.model.ResponseModel.HomeApiRespone


import com.google.gson.annotations.SerializedName

data class HomeApiResponse(
    @SerializedName("code")
    val code: Int = 0,
    @SerializedName("data")
    val `data`: Data = Data(),
    @SerializedName("message")
    val message: String = "",
    @SerializedName("success")
    val success: Boolean = false
)