package com.sea.seaconnect.model.ResponseModel

data class PojoNotificationList(
    val code: Int,
    val `data`: List<NotificationDataList>,
    val message: String,
    val success: Boolean
)

data class NotificationDataList(
    val __v: Int,
    val _id: String,
    val authorized_id: String,
    val createdAt: String,
    val message: String,
    val title: String,
    val to_user_id: String,
    val type: String,
    val updatedAt: String
)