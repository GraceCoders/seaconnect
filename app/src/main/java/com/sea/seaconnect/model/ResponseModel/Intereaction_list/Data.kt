package com.sea.seaconnect.model.ResponseModel.Intereaction_list


import com.google.gson.annotations.SerializedName

data class Data(
    @SerializedName("_id")
    val id: String = "",
    @SerializedName("name")
    val name: String = ""
)