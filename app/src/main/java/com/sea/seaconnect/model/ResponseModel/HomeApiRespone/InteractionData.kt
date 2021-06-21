package com.sea.seaconnect.model.ResponseModel.HomeApiRespone


import com.google.gson.annotations.SerializedName

data class InteractionData(
    @SerializedName("_id")
    val id: String = "",
    @SerializedName("interaction_id")
    val interactionId: String = "",
    @SerializedName("userDetail")
    val userDetail: UserDetail = UserDetail(),
    @SerializedName("user_id")
    val userId: String = ""
)