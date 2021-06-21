package com.sea.seaconnect.model.ResponseModel.signupModel


import com.google.gson.annotations.SerializedName

data class Data(
    @SerializedName("access_token")
    val accessToken: String = "",
    @SerializedName("email")
    val email: String = "",
    @SerializedName("first_name")
    val firstName: String = "",
    @SerializedName("_id")
    val id: String = "",
    @SerializedName("mobile_number")
    val mobileNumber: String = "",
    @SerializedName("profile_image")
    val profileImage: String = "",
    @SerializedName("profile_image_link")
    val profileImageLink: String = "",
    @SerializedName("user_name")
    val userName: String = ""
)