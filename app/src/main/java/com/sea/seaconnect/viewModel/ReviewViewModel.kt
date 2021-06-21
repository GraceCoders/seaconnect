package com.sea.seaconnect.viewModel

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.sea.seaconnect.model.RequestModel.HomeApiRequest.PostReview
import com.sea.seaconnect.model.ResponseModel.PojoNotificationCount
import com.sea.seaconnect.model.ResponseModel.PojoReview
import com.spasime.spasime.controller.services.APIRepository

class ReviewViewModel : AndroidViewModel {
    private lateinit var context: Context

    constructor(application: Application) : super(application) {
        this.context = application
    }
    fun addReview(mToken: String, postReview: PostReview): LiveData<PojoReview> {
        var mutableLiveData: MutableLiveData<PojoReview> = MutableLiveData()
        return APIRepository.addReview(mToken,postReview)
        return mutableLiveData
    }
}