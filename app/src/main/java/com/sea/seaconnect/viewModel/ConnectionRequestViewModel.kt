package com.sea.seaconnect.viewModel

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.sea.seaconnect.model.ResponseModel.PojoConnectionAccpetReject
import com.sea.seaconnect.model.ResponseModel.PojoConnectionList
import com.sea.seaconnect.model.ResponseModel.PojoConnectionReuqestList
import com.spasime.spasime.controller.services.APIRepository

class ConnectionRequestViewModel : AndroidViewModel {
    private lateinit var context: Context

    constructor(application: Application) : super(application) {
        this.context = application
    }
    fun getConnectionRequests( mToken: String): LiveData<PojoConnectionReuqestList> {
        var mutableLiveData: MutableLiveData<PojoConnectionReuqestList> = MutableLiveData()
        return APIRepository.getConnectionRequestList(mToken)
        return mutableLiveData

    }

    fun connectionRequest( mToken: String,status:String,connectionID:String): LiveData<PojoConnectionAccpetReject> {
        var mutableLiveData: MutableLiveData<PojoConnectionAccpetReject> = MutableLiveData()
        return APIRepository.connectionRequestResponse(mToken,status,connectionID)
        return mutableLiveData
    }

    fun connectionsList( mToken: String,status:String): LiveData<PojoConnectionList> {
        var mutableLiveData: MutableLiveData<PojoConnectionList> = MutableLiveData()
        return APIRepository.getConnectionList(mToken,status)
        return mutableLiveData
    }
}