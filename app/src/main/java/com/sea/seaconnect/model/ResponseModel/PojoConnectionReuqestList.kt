package com.sea.seaconnect.model.ResponseModel

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

data class PojoConnectionReuqestList(
    val code: Int,
    val `data`: List<ConnectionRequestDatas>,
    val message: String,
    val success: Boolean
)

data class ConnectionRequestDatas(
    val _id: String,
    val authorized_id: String,
    val is_reviwed: Boolean,
    val senderDetail: SenderDetail,
    val status: String,
    val to_user_id: String
)

data class SenderDetail(
    val __v: Int,
    val _id: String,
    val age_range: String,
    val bio: String,
    val dob: String,
    val email: String,
    val first_name: String,
    val gender: String,
    val interested_in: String,
    val latitude: String,
    val location: Location,
    val longitude: String,
    val mobile_number: String,
    val password: String,
    val profile_image: String,
    val user_name: String
)
@Parcelize
data class Location(
    val coordinates: List<Double>,
    val type: String
): Parcelable