package com.sea.seaconnect.model.ResponseModel

data class PojoSendConnection(
    val code: Int,
    val `data`: ConnectionRequestData,
    val message: String,
    val success: Boolean
)

data class ConnectionRequestData(
    val _id: String,
    val authorized_id: String,
    val is_reviwed: Boolean,
    val status: String,
    val to_user_id: String
)