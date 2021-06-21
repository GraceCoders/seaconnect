package com.sea.seaconnect.viewModel

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.sea.seaconnect.model.RequestModel.HomeApiRequest.HomeApiRequest
import com.sea.seaconnect.model.ResponseModel.HomeApiRespone.HomeApiResponse
import com.sea.seaconnect.model.ResponseModel.PojoNotificationCount
import com.spasime.spasime.controller.services.APIRepository

class HomeViewModel : AndroidViewModel {
    private lateinit var context: Context

    constructor(application: Application) : super(application) {
        this.context = application
    }
    private var mFcmToken = ""




    fun homeApi(
        mToken: String,
        idList: ArrayList<String>,
        currentLat: String,
        currentLong: String
    ): LiveData<HomeApiResponse> {
        var mutableLiveData: MutableLiveData<HomeApiResponse> = MutableLiveData()
        when {

            else -> {
                val requestObject = HomeApiRequest()
                requestObject.interactions=idList
                requestObject.latitude=currentLat
                requestObject.longitude=currentLong
                return APIRepository.getHomeList(mToken,requestObject)
            }
        }
        return mutableLiveData
    }

    fun getNotificationCount( mToken: String): LiveData<PojoNotificationCount> {
        var mutableLiveData: MutableLiveData<PojoNotificationCount> = MutableLiveData()
        return APIRepository.getNotificationCount(mToken)
        return mutableLiveData
    }


    fun savFcmToken( mToken: String,fcmToken:String): LiveData<PojoNotificationCount> {
        var mutableLiveData: MutableLiveData<PojoNotificationCount> = MutableLiveData()
        return APIRepository.saveFcmToken(mToken,fcmToken)
        return mutableLiveData
    }
}