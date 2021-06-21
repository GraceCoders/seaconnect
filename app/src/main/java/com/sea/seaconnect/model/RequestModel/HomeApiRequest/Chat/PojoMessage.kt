package com.sea.seaconnect.model.RequestModel.HomeApiRequest.Chat


data class PojoMessage(
    val connectionId: String=" ",
    val result: List<Message> = mutableListOf()
)

data class Message(
    val __v: Int=0,
    val _id: String="",
    val chat_id: Int=0,
    val connection_id: String="",
    val createdAt: String="",
    val date: String="",
    val from_user_id: String="",
    val is_read: Boolean=false,
    val message: String="",
    val sentByMe: Int=0,
    val time: String="",
    val to_user_id: String="",
    val updatedAt: String=""
)
