package com.sea.seaconnect.model.RequestModel.HomeApiRequest

data class PostReview(
    val connection_id: String="",
    val `private`: List<Private> = mutableListOf(),
    val `public`: List<Public> = mutableListOf(),
    val to_user_id: String="",
    val display_review: String="",
    val display_rating: String=""

)

data class Private(
    val question: String="",
    val review: String="",
)

data class Public(
    val question: String="",
    val review: String="",
    val type: String=""
)
