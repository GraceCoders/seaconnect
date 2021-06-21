package com.sea.seaconnect.model.RequestModel.HomeApiRequest

data class UpdateProfile(
    val age_range: String,
    val dob: String,
    val email: String,
    val first_name: String,
    val gender: String,
    val id: String,
    val interested_in: String,
    val bio: String,
    val mobile_number: String,
    val password: String,
    val user_name: String
)