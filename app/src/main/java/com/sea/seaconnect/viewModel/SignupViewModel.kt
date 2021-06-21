package com.sea.seaconnect.viewModel

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.sea.seaconnect.model.ResponseModel.signupModel.SignupResponse
import com.spasime.spasime.controller.services.APIRepository
import java.io.File

class SignupViewModel : AndroidViewModel {
    private lateinit var context: Context

    constructor(application: Application) : super(application) {
        this.context = application
    }
    fun SignupUser(
        user_name: String,
        first_name: String,
        email: String,
        password: String,
        confirm_password: String?,
        gender: String?,
        interested_in: String?,
        age_range: String?,
        mobile_number: String?,
        bio: String?,
        mImagefile: File?
    ): LiveData<SignupResponse> {
        var mutableLiveData: MutableLiveData<SignupResponse> = MutableLiveData()
        when {
            else -> {
                return APIRepository.signup(user_name,first_name,email,password,confirm_password,gender,interested_in,age_range,mobile_number,bio,mImagefile)
            }
        }
        return mutableLiveData
    }
}