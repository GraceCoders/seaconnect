package com.sea.seaconnect.model.ResponseModel.HomeApiRespone


import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class UserDetail(
    @SerializedName("age_range")
    val ageRange: String = "",
    @SerializedName("bio")
    val bio: String = "",
    @SerializedName("dob")
    val dob: String = "",
    @SerializedName("email")
    val email: String = "",
    @SerializedName("first_name")
    val firstName: String = "",
    @SerializedName("gender")
    val gender: String = "",
    @SerializedName("_id")
    val id: String = "",
    @SerializedName("interested_in")
    val interestedIn: String = "",
    @SerializedName("latitude")
    val latitude: String = "",
    @SerializedName("location")
    val location: Location = Location(),
    @SerializedName("longitude")
    val longitude: String = "",
    @SerializedName("looking_for")
    val lookingFor: String = "",
    @SerializedName("mobile_number")
    val mobileNumber: String = "",
    @SerializedName("password")
    val password: String = "",
    @SerializedName("profile_image")
    val profileImage: String = "",
    @SerializedName("user_name")
    val userName: String = "",
    @SerializedName("avg_rating")
    val avg_rating: Double = 0.0,
    @SerializedName("__v")
    val v: Int = 0
):Parcelable