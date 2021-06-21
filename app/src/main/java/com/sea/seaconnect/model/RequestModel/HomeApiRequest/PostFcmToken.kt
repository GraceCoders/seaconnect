package com.sea.seaconnect.model.RequestModel.HomeApiRequest

data class PostFcmToken(
    val platform: String="android",
    val token: String
)