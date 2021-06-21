package com.sea.seaconnect.viewModel

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.sea.seaconnect.model.ResponseModel.Intereaction_list.Intereaction_list_response
import com.spasime.spasime.controller.services.APIRepository

class InterestViewModel : AndroidViewModel {
    private lateinit var context: Context

    constructor(application: Application) : super(application) {
        this.context = application
    }
    fun getInterest( mToken: String): LiveData<Intereaction_list_response> {
        var mutableLiveData: MutableLiveData<Intereaction_list_response> = MutableLiveData()
        return APIRepository.getInterestList(mToken)
        return mutableLiveData
    }
}