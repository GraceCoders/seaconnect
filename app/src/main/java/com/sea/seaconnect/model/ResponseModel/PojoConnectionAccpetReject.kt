package com.sea.seaconnect.model.ResponseModel

data class PojoConnectionAccpetReject(
    val code: Int,
    val `data`: Data,
    val message: String,
    val success: Boolean
)

data class Data(
    val _id: String,
    val authorized_id: String,
    val is_reviwed: Boolean,
    val status: String,
    val to_user_id: String
)