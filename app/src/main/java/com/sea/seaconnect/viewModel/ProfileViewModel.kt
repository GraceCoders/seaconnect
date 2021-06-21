package com.sea.seaconnect.viewModel

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.sea.seaconnect.model.PojoImagesList
import com.sea.seaconnect.model.RequestModel.HomeApiRequest.SendConnectionRequest
import com.sea.seaconnect.model.ResponseModel.PojoReview
import com.sea.seaconnect.model.ResponseModel.PojoSendConnection
import com.sea.seaconnect.model.ResponseModel.PojoUserProfileData
import com.sea.seaconnect.model.ResponseModel.signupModel.SignupResponse
import com.spasime.spasime.controller.services.APIRepository
import java.io.File

class ProfileViewModel : AndroidViewModel {
    private lateinit var context: Context

    constructor(application: Application) : super(application) {
        this.context = application
    }

    fun getProfileData(UserId: String, mToken: String): LiveData<PojoUserProfileData> {
        var mutableLiveData: MutableLiveData<PojoUserProfileData> = MutableLiveData()
        return APIRepository.getUserByID(mToken, UserId)
        return mutableLiveData
    }

    fun sendConnectionRequest(UserId: String, mToken: String): LiveData<PojoSendConnection> {
        var mutableLiveData: MutableLiveData<PojoSendConnection> = MutableLiveData()
        return APIRepository.sendConnectionRequest(mToken, SendConnectionRequest(UserId))
        return mutableLiveData

    }

    fun updateMultipleImages(
        imageList: MutableList<PojoImagesList>,
        mToken: String
    ): LiveData<PojoSendConnection> {
        var mutableLiveData: MutableLiveData<PojoSendConnection> = MutableLiveData()
        return APIRepository.uploadMultipleUserImages(mToken, imageList)
        return mutableLiveData
    }
    fun deleteImage(
       imageId:String,
        mToken: String
    ): LiveData<PojoReview> {
        var mutableLiveData: MutableLiveData<PojoReview> = MutableLiveData()
        return APIRepository.deleteImage(mToken, imageId)
        return mutableLiveData
    }

    fun updateProfile(
        mToken: String,
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
        var mutableLiveData: MutableLiveData<SignupResponse> = MutableLiveData()
        return APIRepository.updateProfile(
            mToken,
            first_name,
            email,
            gender,
            interested_in,
            age_range,
            mobile_number,
            bio,
            dob,
            mUserId,
            password, userName
        )
        return mutableLiveData
    }

    fun updateProfileImage(
        mToken: String,
        id: String,
        userImage: File
    ): LiveData<SignupResponse> {
        var mutableLiveData: MutableLiveData<SignupResponse> = MutableLiveData()
        return APIRepository.updateProfileImage(mToken, id, userImage)
        return mutableLiveData
    }
}