package com.sea.seaconnect.viewModel

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.sea.seaconnect.model.ResponseModel.Intereaction_list.Intereaction_list_response
import com.sea.seaconnect.model.ResponseModel.PojoNotificationList
import com.sea.seaconnect.model.ResponseModel.PojoResetNotificationCount
import com.spasime.spasime.controller.services.APIRepository

class NotificationViewModel : AndroidViewModel {
    private lateinit var context: Context

    constructor(application: Application) : super(application) {
        this.context = application
    }
    fun getNotificationList( mToken: String): LiveData<PojoNotificationList> {
        var mutableLiveData: MutableLiveData<PojoNotificationList> = MutableLiveData()
        return APIRepository.getNotification(mToken!!)
        return mutableLiveData
    }    fun resetNotificationCount( mToken: String): LiveData<PojoResetNotificationCount> {
        var mutableLiveData: MutableLiveData<PojoResetNotificationCount> = MutableLiveData()
        return APIRepository.resetNotificationCount(mToken!!)
        return mutableLiveData
    }
}