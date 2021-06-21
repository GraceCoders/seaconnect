package com.sea.seaconnect.controller.services


import com.sea.seaconnect.controller.utills.Constants
import com.sea.seaconnect.model.RequestModel.HomeApiRequest.*
import com.sea.seaconnect.model.RequestModel.HomeApiRequest.ConnectionRequest.PostConnectionAccpetReject
import com.sea.seaconnect.model.ResponseModel.*
import com.sea.seaconnect.model.ResponseModel.HomeApiRespone.HomeApiResponse
import com.sea.seaconnect.model.ResponseModel.Intereaction_list.Intereaction_list_response
import com.sea.seaconnect.model.ResponseModel.loginmodel.LoginResponseModel
import com.sea.seaconnect.model.ResponseModel.signupModel.SignupResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*


/**
 * Created by nishant on 03/07/20.
 */
interface APIInterface {


    //login
    @POST(Constants.API_LOGIN)
    fun login(
        @Body requestBody: RequestBody
    ): Call<LoginResponseModel>

    //homeList
    @POST(Constants.API_HOME_LIST)
    fun getHomeList(
        @Header("Authorization") authorization: String?,
        @Body requestBody: HomeApiRequest
    ): Call<HomeApiResponse>


    //contact us
    @Headers("Content-Type: application/json")
    @GET(Constants.API_INTEREST_LIST)
    fun getInterestList(
        @Header("Authorization") authorization: String?
    ): Call<Intereaction_list_response>

    //signup
    @Multipart
    @POST(Constants.API_SIGNUP)
    fun signup(
        @Part("user_name") user_name: RequestBody?,
        @Part("first_name") first_name: RequestBody?,
        @Part("email") email: RequestBody?,
        @Part("password") password: RequestBody?,
        @Part("confirm_password") confirm_password: RequestBody?,
        @Part("gender") gender: RequestBody?,
        @Part("interested_in") interested_in: RequestBody?,
        @Part("age_range") age_range: RequestBody?,
        @Part("mobile_number") price: RequestBody?,
        @Part("bio") closing_days: RequestBody?,
        @Part profile_image: MultipartBody.Part?

    ): Call<SignupResponse?>?

    //update user Image
    @Multipart
    @POST(Constants.API_UPDATE_PROFILE_IMAGE)
    fun updateImage(
        @Header("Authorization") authorization: String?,

        @Part("id") user_name: RequestBody?,
        @Part profile_image: MultipartBody.Part?

    ): Call<SignupResponse?>?

    //get user by Id
    @Headers("Content-Type: application/json")
    @GET(Constants.API_USER_BY_ID + "{id}")
    fun getUserByID(
        @Path("id") id: String,
        @Header("Authorization") authorization: String?
    ): Call<PojoUserProfileData>

    //send connection request
    @POST(Constants.API_SEND_CONNECTION_REQUEST)
    fun sendConnectionRequest(
        @Header("Authorization") authorization: String?,
        @Body requestBody: SendConnectionRequest
    ): Call<PojoSendConnection>

    //update multiple images
    @POST(Constants.API_MULTIPLE_IMAGES)
    fun updateMultipleImages(
        @Header("Authorization") authorization: String?,
        @Body profile_image: okhttp3.MultipartBody

    ): Call<PojoSendConnection>

    //update profile
    @POST(Constants.API_UPDATE_PROFILE)
    fun updateProfile(
        @Header("Authorization") authorization: String?,
        @Body profileData: com.sea.seaconnect.model.RequestModel.HomeApiRequest.UpdateProfile

    ): Call<SignupResponse>

    //get Request connection list
    @Headers("Content-Type: application/json")
    @GET(Constants.API_CONNECTION_REQUEST_LIST)
    fun getConnectionRequestList(
        @Header("Authorization") authorization: String?,

        ): Call<PojoConnectionReuqestList>


    // connection request response
    @Headers("Content-Type: application/json")
    @POST(Constants.API_CONNECTION_REQUEST_RESPONSE)
    fun connectionrequestResponse(
        @Header("Authorization") authorization: String?,
        @Body postConnectionAccpetReject: PostConnectionAccpetReject
    ): Call<PojoConnectionAccpetReject>


    //get  connection list
    @Headers("Content-Type: application/json")
    @POST(Constants.API_CONNECTION_LIST)
    fun getConnectionList(
        @Header("Authorization") authorization: String?,
        @Body postConnectionAccpetReject: PostConnectionOnStatus
    ): Call<PojoConnectionList>

    //get  notification list
    @Headers("Content-Type: application/json")
    @GET(Constants.API_NOTIFICATION_LIST)
    fun getNotificationList(
        @Header("Authorization") authorization: String?
    ): Call<PojoNotificationList>

    // reset notification count
    @Headers("Content-Type: application/json")
    @GET(Constants.API_NOTIFICATION_COUNT_RESET)
    fun resetNotificationCount(
        @Header("Authorization") authorization: String?
    ): Call<PojoResetNotificationCount>

    //get  notification count
    @Headers("Content-Type: application/json")
    @GET(Constants.API_NOTIFICATION_COUNT)
    fun getNotificationCount(
        @Header("Authorization") authorization: String?
    ): Call<PojoNotificationCount>

    // save fcm token
    @Headers("Content-Type: application/json")
    @POST(Constants.API_fCM_TOKEN)
    fun saveFcmToken(
        @Header("Authorization") authorization: String?,
        @Body postConnectionAccpetReject: PostFcmToken
    ): Call<PojoNotificationCount>

    @Headers("Content-Type: application/json")
    @POST(Constants.API_ADD_REVIEW)
    fun addReview(
        @Header("Authorization") authorization: String?,
        @Body postReview: PostReview
    ): Call<PojoReview> @Headers("Content-Type: application/json")
    @POST(Constants.API_DELETE_IMAGE)
    fun deleteImage(
        @Header("Authorization") authorization: String?,
        @Body postReview: PojoDeleteImage
    ): Call<PojoReview>
}