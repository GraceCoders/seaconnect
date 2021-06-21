package com.spasime.spasime.controller.services


import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.sea.seaconnect.controller.Utills.App
import com.sea.seaconnect.controller.Utills.AppPreferences
import com.sea.seaconnect.controller.services.APIClient
import com.sea.seaconnect.controller.services.APIInterface
import com.sea.seaconnect.controller.utills.Constants
import com.sea.seaconnect.model.PojoImagesList
import com.sea.seaconnect.model.RequestModel.HomeApiRequest.*
import com.sea.seaconnect.model.RequestModel.HomeApiRequest.ConnectionRequest.PostConnectionAccpetReject
import com.sea.seaconnect.model.ResponseModel.*
import com.sea.seaconnect.model.ResponseModel.HomeApiRespone.HomeApiResponse
import com.sea.seaconnect.model.ResponseModel.Intereaction_list.Intereaction_list_response
import com.sea.seaconnect.model.ResponseModel.loginmodel.LoginResponseModel
import com.sea.seaconnect.model.ResponseModel.signupModel.SignupResponse
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File


object APIRepository {
    private var apiInterface: APIInterface = APIClient.retrofit(Constants.BASE_URL_API)
    var part: MultipartBody.Part? = null


    //login
    fun login(
        requestObject: JSONObject
    ): LiveData<LoginResponseModel> {
        val mutableLiveData: MutableLiveData<LoginResponseModel> = MutableLiveData()
        val requestBody: RequestBody =
            requestObject.toString().toRequestBody("application/json".toMediaType())
        val call = apiInterface.login(requestBody)
        call.enqueue(object : Callback<LoginResponseModel> {
            override fun onResponse(
                call: Call<LoginResponseModel>,
                response: Response<LoginResponseModel>
            ) {
                if (response.isSuccessful) {
                    try {
                        val loginResponse: LoginResponseModel? = response.body()
                        if (response.body()?.code == 200) {

                            AppPreferences.init(App.getAppContext())
                                .putString(Constants.EMAIL, loginResponse?.data?.email)
                            AppPreferences.init(App.getAppContext())
                                .putString(Constants.USER_ID, loginResponse?.data?.id)
                            AppPreferences.init(App.getAppContext())
                                .putString(Constants.F_NAME, loginResponse?.data?.firstName)
                            AppPreferences.init(App.getAppContext())
                                .putString(Constants.USERNAME, loginResponse?.data?.userName)
                            AppPreferences.init(App.getAppContext())
                                .putString(Constants.TOKEN, loginResponse?.data?.accessToken)
                            AppPreferences.init(App.getAppContext()).putString(
                                Constants.PROFILE_IMAGE,
                                loginResponse?.data?.profileImageLink.toString()
                            )
                        }
                        mutableLiveData.value = loginResponse
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                } else {

                }
            }

            override fun onFailure(call: Call<LoginResponseModel>, t: Throwable) {
                t.printStackTrace()
            }
        })

        return mutableLiveData
    }

    //getwishlist
    fun getHomeList(
        mToken: String,
        requestObject: HomeApiRequest
    ): LiveData<HomeApiResponse> {
        val mutableLiveData: MutableLiveData<HomeApiResponse> = MutableLiveData()
        val call = apiInterface.getHomeList("Bearer " + mToken, requestObject)
        call.enqueue(object : Callback<HomeApiResponse> {
            override fun onResponse(
                call: Call<HomeApiResponse>,
                response: Response<HomeApiResponse>
            ) {
                if (response.isSuccessful) {
                    try {
                        val homeApiResponse: HomeApiResponse? = response.body()
                        mutableLiveData.value = homeApiResponse
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                } else {
                }
            }

            override fun onFailure(call: Call<HomeApiResponse>, t: Throwable) {
                t.printStackTrace()
            }
        })

        return mutableLiveData
    }

    //getwishlist
    fun getInterestList(
        mToken: String?
    ): LiveData<Intereaction_list_response> {
        val mutableLiveData: MutableLiveData<Intereaction_list_response> = MutableLiveData()
        val call = apiInterface.getInterestList("Bearer " + mToken)
        call.enqueue(object : Callback<Intereaction_list_response> {
            override fun onResponse(
                call: Call<Intereaction_list_response>,
                response: Response<Intereaction_list_response>
            ) {
                if (response.isSuccessful) {
                    try {
                        val wishlistResponse: Intereaction_list_response? = response.body()
                        mutableLiveData.value = wishlistResponse
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                } else {
                }
            }

            override fun onFailure(call: Call<Intereaction_list_response>, t: Throwable) {
                t.printStackTrace()
            }
        })

        return mutableLiveData
    }


    //getwishlist
    fun getUserByID(
        mToken: String?, userID: String
    ): LiveData<PojoUserProfileData> {
        val mutableLiveData: MutableLiveData<PojoUserProfileData> = MutableLiveData()
        val call = apiInterface.getUserByID(userID, "Bearer " + mToken)
        call.enqueue(object : Callback<PojoUserProfileData> {
            override fun onResponse(
                call: Call<PojoUserProfileData>,
                response: Response<PojoUserProfileData>
            ) {
                if (response.isSuccessful) {
                    try {
                        val UserData: PojoUserProfileData? = response.body()
                        mutableLiveData.value = UserData
//                        AppPreferences.init(App.getAppContext())
//                            .putString(Constants.EMAIL, UserData?.data?.email)
//
//                        AppPreferences.init(App.getAppContext())
//                            .putString(Constants.USER_ID, UserData?.data?.id)
//                        AppPreferences.init(App.getAppContext())
//                            .putString(Constants.F_NAME, UserData?.data?.first_name)
//                        AppPreferences.init(App.getAppContext())
//                            .putString(Constants.USERNAME, UserData?.data?.user_name)
//
//                        AppPreferences.init(App.getAppContext()).putString(
//                            Constants.PROFILE_IMAGE,
//                           Constants.PROFILE_URL_API+ UserData?.data?.profile_image.toString()

                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                } else {
                }
            }

            override fun onFailure(call: Call<PojoUserProfileData>, t: Throwable) {
                t.printStackTrace()
            }
        })

        return mutableLiveData
    }


    //Register
    fun signup(
        user_name: String,
        first_name: String,
        email: String,
        passwords: String,
        confirm_password: String?,
        gender: String?,
        interested_in: String?,
        age_range: String?,
        mobile_number: String?,
        bio: String?,
        mImagefile: File?
    ): LiveData<SignupResponse> {
        val mutableLiveData: MutableLiveData<SignupResponse> = MutableLiveData()
        val user_name: RequestBody = user_name.toRequestBody("text/plain".toMediaType())
        val first_name: RequestBody = first_name.toRequestBody("text/plain".toMediaType())
        val email: RequestBody = email.toRequestBody("text/plain".toMediaType())
        val password: RequestBody = passwords.toRequestBody("text/plain".toMediaType())
        val confirm_password: RequestBody =
            confirm_password.toString().toRequestBody("text/plain".toMediaType())
        val gender: RequestBody = gender.toString().toRequestBody("text/plain".toMediaType())
        val interested_in: RequestBody =
            interested_in.toString().toRequestBody("text/plain".toMediaType())
        val age_range: RequestBody =
            age_range.toString().toRequestBody("text/plain".toMediaType())
        val mobile_number: RequestBody =
            mobile_number.toString().toRequestBody("text/plain".toMediaType())
        val bio: RequestBody = bio.toString().toRequestBody("text/plain".toMediaType())
        val part = MultipartBody.Part.createFormData(
            "profile_image", mImagefile?.name, mImagefile!!
                .asRequestBody("image/jpeg".toMediaType())
        )
        val call = apiInterface.signup(
            user_name,
            first_name,
            email,
            password,
            confirm_password,
            gender,
            interested_in,
            age_range,
            mobile_number,
            bio,
            part
        )
        call!!.enqueue(object : Callback<SignupResponse?> {
            override fun onResponse(
                call: Call<SignupResponse?>,
                response: Response<SignupResponse?>
            ) {
                if (response.isSuccessful) {
                    try {
                        val signupResponse: SignupResponse? = response.body()
                        if (response.body()?.code == 200) {
                            AppPreferences.init(App.getAppContext())
                                .putString(Constants.EMAIL, signupResponse?.data?.email)
                            AppPreferences.init(App.getAppContext())
                                .putString(Constants.PASSWORD, passwords)
                            AppPreferences.init(App.getAppContext())
                                .putString(Constants.USER_ID, signupResponse?.data?.id)
                            AppPreferences.init(App.getAppContext())
                                .putString(Constants.F_NAME, signupResponse?.data?.firstName)
                            AppPreferences.init(App.getAppContext())
                                .putString(Constants.USERNAME, signupResponse?.data?.userName)
                            AppPreferences.init(App.getAppContext())
                                .putString(Constants.TOKEN, signupResponse?.data?.accessToken)
                            AppPreferences.init(App.getAppContext()).putString(
                                Constants.PROFILE_IMAGE,
                                Constants.PROFILE_URL_API + signupResponse?.data?.profileImageLink.toString()
                            )
                            mutableLiveData.value = signupResponse
                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                } else {
                    mutableLiveData.value = response.body()
                }
            }

            override fun onFailure(call: Call<SignupResponse?>, t: Throwable) {
                t.printStackTrace()
            }
        })

        return mutableLiveData
    }


    //send connection request
    fun sendConnectionRequest(
        mToken: String?, sendConnectionRequest: SendConnectionRequest,
    ): LiveData<PojoSendConnection> {
        val mutableLiveData: MutableLiveData<PojoSendConnection> = MutableLiveData()
        val call = apiInterface.sendConnectionRequest("Bearer " + mToken, sendConnectionRequest)
        call.enqueue(object : Callback<PojoSendConnection> {
            override fun onResponse(
                call: Call<PojoSendConnection>,
                response: Response<PojoSendConnection>
            ) {
                if (response.isSuccessful) {
                    try {
                        val pojoSendConnection: PojoSendConnection? = response.body()
                        mutableLiveData.value = pojoSendConnection
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                } else {
                }
            }

            override fun onFailure(call: Call<PojoSendConnection>, t: Throwable) {
                t.printStackTrace()
            }
        })

        return mutableLiveData
    }

    //upload multiple user images
    fun uploadMultipleUserImages(
        mToken: String?, filePaths: MutableList<PojoImagesList>,
    ): LiveData<PojoSendConnection> {
        val mutableLiveData: MutableLiveData<PojoSendConnection> = MutableLiveData()
        val builder = MultipartBody.Builder()
        builder.setType(MultipartBody.FORM)

        for (i in filePaths.indices) {

            builder.addFormDataPart(
                "user_images",
                filePaths[i].file.name,
                filePaths[i].file.asRequestBody("multipart/form-data".toMediaTypeOrNull())
            )
        }

        val requestBody: MultipartBody = builder.build()


        val call = apiInterface.updateMultipleImages("Bearer " + mToken, requestBody)
        call.enqueue(object : Callback<PojoSendConnection> {
            override fun onResponse(
                call: Call<PojoSendConnection>,
                response: Response<PojoSendConnection>
            ) {
                if (response.isSuccessful) {
                    try {
                        val pojoSendConnection: PojoSendConnection? = response.body()
                        mutableLiveData.value = pojoSendConnection
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                } else {
                }
            }

            override fun onFailure(call: Call<PojoSendConnection>, t: Throwable) {
                t.printStackTrace()
            }
        })

        return mutableLiveData
    }


    //upload profile
    fun updateProfile(
        mToken: String?,
        first_name: String,
        email: String,
        gender: String?,
        interested_in: String?,
        age_range: String?,
        mobile_number: String?,
        bio: String?,
        dob: String,
        mUserId: String,
        password: String,
        userName: String,
    ): LiveData<SignupResponse> {
        val mutableLiveData: MutableLiveData<SignupResponse> = MutableLiveData()
        val updateProfile = UpdateProfile(
            age_range!!,
            dob,
            email,
            first_name,
            gender!!,
            mUserId,
            interested_in!!,
            bio!!,
            mobile_number!!, password, userName
        )

        val call = apiInterface.updateProfile("Bearer " + mToken, updateProfile)
        call.enqueue(object : Callback<SignupResponse> {
            override fun onResponse(
                call: Call<SignupResponse>,
                response: Response<SignupResponse>
            ) {
                if (response.isSuccessful) {
                    try {
                        val signupResponse: SignupResponse? = response.body()
                        mutableLiveData.value = signupResponse
                        AppPreferences.init(App.getAppContext())
                            .putString(Constants.EMAIL, signupResponse?.data?.email)

                        AppPreferences.init(App.getAppContext())
                            .putString(Constants.USER_ID, signupResponse?.data?.id)
                        AppPreferences.init(App.getAppContext())
                            .putString(Constants.F_NAME, signupResponse?.data?.firstName)
                        AppPreferences.init(App.getAppContext())
                            .putString(Constants.USERNAME, signupResponse?.data?.userName)

                        AppPreferences.init(App.getAppContext()).putString(
                            Constants.PROFILE_IMAGE,
                            Constants.PROFILE_URL_API + signupResponse?.data?.profileImage.toString()
                        )
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                } else {
                }
            }

            override fun onFailure(call: Call<SignupResponse>, t: Throwable) {
                t.printStackTrace()
            }
        })

        return mutableLiveData
    }


    //update profile
    fun updateProfileImage(
        authToken: String, id: String,
        mImagefile: File?
    ): LiveData<SignupResponse> {
        val mutableLiveData: MutableLiveData<SignupResponse> = MutableLiveData()
        val id: RequestBody = id.toRequestBody("text/plain".toMediaType())

        val part = MultipartBody.Part.createFormData(
            "profile_image", mImagefile?.name, mImagefile!!
                .asRequestBody("image/jpeg".toMediaType())
        )
        val call = apiInterface.updateImage(
            authToken,
            id,
            part
        )
        call!!.enqueue(object : Callback<SignupResponse?> {
            override fun onResponse(
                call: Call<SignupResponse?>,
                response: Response<SignupResponse?>
            ) {
                if (response.isSuccessful) {
                    try {
                        val signupResponse: SignupResponse? = response.body()
                        if (response.body()?.code == 200) {

                            mutableLiveData.value = signupResponse
                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                } else {
                    mutableLiveData.value = response.body()
                }
            }

            override fun onFailure(call: Call<SignupResponse?>, t: Throwable) {
                t.printStackTrace()
            }
        })

        return mutableLiveData
    }

    //connection request
    fun getConnectionRequestList(
        mToken: String?,
    ): LiveData<PojoConnectionReuqestList> {
        val mutableLiveData: MutableLiveData<PojoConnectionReuqestList> = MutableLiveData()

        val call = apiInterface.getConnectionRequestList("Bearer " + mToken)
        call.enqueue(object : Callback<PojoConnectionReuqestList> {
            override fun onResponse(
                call: Call<PojoConnectionReuqestList>,
                response: Response<PojoConnectionReuqestList>
            ) {
                if (response.isSuccessful) {
                    try {
                        val pojoConnectionReuqestList: PojoConnectionReuqestList? = response.body()
                        mutableLiveData.value = pojoConnectionReuqestList
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                } else {
                }
            }

            override fun onFailure(call: Call<PojoConnectionReuqestList>, t: Throwable) {
                t.printStackTrace()
            }
        })

        return mutableLiveData
    }


    //connection request response
    fun connectionRequestResponse(
        mToken: String?, status: String, connectionID: String
    ): LiveData<PojoConnectionAccpetReject> {
        val mutableLiveData: MutableLiveData<PojoConnectionAccpetReject> = MutableLiveData()
        var postConnectionAccpetReject = PostConnectionAccpetReject(connectionID, status)
        val call =
            apiInterface.connectionrequestResponse("Bearer " + mToken, postConnectionAccpetReject)
        call.enqueue(object : Callback<PojoConnectionAccpetReject> {
            override fun onResponse(
                call: Call<PojoConnectionAccpetReject>,
                response: Response<PojoConnectionAccpetReject>
            ) {
                if (response.isSuccessful) {
                    try {
                        val pojoConnectionReuqestList: PojoConnectionAccpetReject? = response.body()
                        mutableLiveData.value = pojoConnectionReuqestList
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                } else {
                }
            }

            override fun onFailure(call: Call<PojoConnectionAccpetReject>, t: Throwable) {
                t.printStackTrace()
            }
        })

        return mutableLiveData
    }

    // get connection  response
    fun getConnectionList(
        mToken: String?, status: String
    ): LiveData<PojoConnectionList> {
        val mutableLiveData: MutableLiveData<PojoConnectionList> = MutableLiveData()
        var postConnectionOnStatus = PostConnectionOnStatus(status)
        val call =
            apiInterface.getConnectionList("Bearer " + mToken, postConnectionOnStatus)
        call.enqueue(object : Callback<PojoConnectionList> {
            override fun onResponse(
                call: Call<PojoConnectionList>,
                response: Response<PojoConnectionList>
            ) {
                if (response.isSuccessful) {
                    try {
                        val pojoConnectionReuqestList: PojoConnectionList? = response.body()
                        mutableLiveData.value = pojoConnectionReuqestList
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                } else {
                }
            }

            override fun onFailure(call: Call<PojoConnectionList>, t: Throwable) {
                t.printStackTrace()
            }
        })

        return mutableLiveData
    }

    // get notification list
    fun getNotification(
        mToken: String?
    ): LiveData<PojoNotificationList> {
        val mutableLiveData: MutableLiveData<PojoNotificationList> = MutableLiveData()
        val call =
            apiInterface.getNotificationList("Bearer " + mToken)
        call.enqueue(object : Callback<PojoNotificationList> {
            override fun onResponse(
                call: Call<PojoNotificationList>,
                response: Response<PojoNotificationList>
            ) {
                if (response.isSuccessful) {
                    try {
                        val pojoConnectionReuqestList: PojoNotificationList? = response.body()
                        mutableLiveData.value = pojoConnectionReuqestList
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                } else {
                }
            }

            override fun onFailure(call: Call<PojoNotificationList>, t: Throwable) {
                t.printStackTrace()
            }
        })

        return mutableLiveData
    }


    // reset notification count
    fun resetNotificationCount(
        mToken: String?
    ): LiveData<PojoResetNotificationCount> {
        val mutableLiveData: MutableLiveData<PojoResetNotificationCount> = MutableLiveData()
        val call =
            apiInterface.resetNotificationCount("Bearer " + mToken)
        call.enqueue(object : Callback<PojoResetNotificationCount> {
            override fun onResponse(
                call: Call<PojoResetNotificationCount>,
                response: Response<PojoResetNotificationCount>
            ) {
                if (response.isSuccessful) {
                    try {
                        val pojoConnectionReuqestList: PojoResetNotificationCount? = response.body()
                        mutableLiveData.value = pojoConnectionReuqestList
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                } else {
                }
            }

            override fun onFailure(call: Call<PojoResetNotificationCount>, t: Throwable) {
                t.printStackTrace()
            }
        })

        return mutableLiveData
    }

    // get  notification count
    fun getNotificationCount(
        mToken: String?
    ): LiveData<PojoNotificationCount> {
        val mutableLiveData: MutableLiveData<PojoNotificationCount> = MutableLiveData()
        val call =
            apiInterface.getNotificationCount("Bearer " + mToken)
        call.enqueue(object : Callback<PojoNotificationCount> {
            override fun onResponse(
                call: Call<PojoNotificationCount>,
                response: Response<PojoNotificationCount>
            ) {
                if (response.isSuccessful) {
                    try {
                        val pojoNotificationCount: PojoNotificationCount? = response.body()
                        mutableLiveData.value = pojoNotificationCount
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                } else {
                }
            }

            override fun onFailure(call: Call<PojoNotificationCount>, t: Throwable) {
                t.printStackTrace()
            }
        })

        return mutableLiveData
    }

    //save fcm token
    fun saveFcmToken(
        mToken: String?, fcmToken: String
    ): LiveData<PojoNotificationCount> {
        val mutableLiveData: MutableLiveData<PojoNotificationCount> = MutableLiveData()
        var postFcmToken = PostFcmToken(token = fcmToken)
        val call =
            apiInterface.saveFcmToken("Bearer " + mToken, postFcmToken)
        call.enqueue(object : Callback<PojoNotificationCount> {
            override fun onResponse(
                call: Call<PojoNotificationCount>,
                response: Response<PojoNotificationCount>
            ) {
                if (response.isSuccessful) {
                    try {
                        val pojoNotificationCount: PojoNotificationCount? = response.body()
                        mutableLiveData.value = pojoNotificationCount
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                } else {
                }
            }

            override fun onFailure(call: Call<PojoNotificationCount>, t: Throwable) {
                t.printStackTrace()
            }
        })

        return mutableLiveData
    }
    //add review
    fun addReview(
        mToken: String?,
        postReview: PostReview
    ): LiveData<PojoReview> {
        val mutableLiveData: MutableLiveData<PojoReview> = MutableLiveData()
        val call =
            apiInterface.addReview("Bearer " + mToken, postReview)
        call.enqueue(object : Callback<PojoReview> {
            override fun onResponse(
                call: Call<PojoReview>,
                response: Response<PojoReview>
            ) {
                if (response.isSuccessful) {
                    try {
                        val pojoNotificationCount: PojoReview? = response.body()
                        mutableLiveData.value = pojoNotificationCount
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                } else {
                }
            }

            override fun onFailure(call: Call<PojoReview>, t: Throwable) {
                t.printStackTrace()
            }
        })

        return mutableLiveData
    }

 //add review
    fun deleteImage(
        mToken: String?,
        imageId: String
    ): LiveData<PojoReview> {
        val mutableLiveData: MutableLiveData<PojoReview> = MutableLiveData()
     var imageID=PojoDeleteImage(imageId)
        val call =
            apiInterface.deleteImage("Bearer " + mToken, imageID)
        call.enqueue(object : Callback<PojoReview> {
            override fun onResponse(
                call: Call<PojoReview>,
                response: Response<PojoReview>
            ) {
                if (response.isSuccessful) {
                    try {
                        val pojoNotificationCount: PojoReview? = response.body()
                        mutableLiveData.value = pojoNotificationCount
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                } else {
                }
            }

            override fun onFailure(call: Call<PojoReview>, t: Throwable) {
                t.printStackTrace()
            }
        })

        return mutableLiveData
    }


}

