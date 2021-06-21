package com.sea.seaconnect.model.ResponseModel

import java.io.File

data class PojoUserProfileData(
    val code: Int=0,
    val `data`: UserData=UserData(),
    val message: String="",
    val success: Boolean=false
)

data class UserData(
    val _id: String="",
    val age_range: String="",
    val avgRating: List<Double> = mutableListOf(),
    val bio: String="",
    val dob: String="",
    val email: String="",
    val first_name: String="",
    val gender: String="",
    val id: String="",
    val interested_in: String="",
    val latitude: String="",
    val longitude: String="",
    val mobile_number: String="",
    val profile_image: String="",
    val profile_image_link: String="",
    val reviews: List<Review> = ArrayList<Review>(),
    val userImages: List<UserImage> = mutableListOf(),
    val user_name: String=""
)

data class Review(
    val _id: String="",
    val authorized_id: String="",
    val avg_rating: String="",
    val createdAt: String="",
    val question: String="",
    val review: String="",
    val review_type: String="",
    val reviewdBy: ReviewdBy,
    val display_rating: String="",
    val display_review: String=""
)

data class ReviewdBy(
    val bio: String="",
    val first_name: String="",
    val profile_image: String="",
    val user_name: String=""
)


data class UserImage(
    val __v: Int=0,
    val _id: String="",
    val id: String="",
    val image_path: String="",
    val user_id: String="",
    val user_image_link: String="",
    val isNotUploaded: Boolean=false,
    var imageURl:File?=null
)




