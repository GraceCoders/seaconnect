package com.sea.seaconnect.model.ResponseModel

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

data class PojoConnectionList(
    val code: Int,
    val `data`: List<ConnectionListData>,
    val message: String,
    val success: Boolean
)
@Parcelize
data class ConnectionListData(
    val _id: String,
    val authorized_id: String,
    val is_reviwed: Boolean,
    val messageCount: Int,
    val status: String,
    val chat: Chat,

    val to_user_id: String,
    val userDetail: UserDetail
):Parcelable

@Parcelize
data class UserDetail(
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
):Parcelable

@Parcelize
data class Chat(
    val __v: Int=0,
    val _id: String="",
    val chat_id: Int=0,
    val connection_id: String="",
    val createdAt: String="",
    val date: String="",
    val from_user_id: String="",
    val message: String="",
    val time: String="",
    val to_user_id: String="",
    val updatedAt: String=""
): Parcelable
