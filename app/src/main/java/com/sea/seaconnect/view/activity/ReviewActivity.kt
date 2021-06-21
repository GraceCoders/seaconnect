package com.sea.seaconnect.view.activity

import android.os.Bundle
import android.view.View
import android.widget.RadioButton
import android.widget.Toast
import androidx.annotation.Nullable
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.bumptech.glide.Glide
import com.sea.seaconnect.R
import com.sea.seaconnect.controller.Utills.AppPreferences
import com.sea.seaconnect.controller.utills.Constants
import com.sea.seaconnect.model.RequestModel.HomeApiRequest.PostReview
import com.sea.seaconnect.model.RequestModel.HomeApiRequest.Private
import com.sea.seaconnect.model.RequestModel.HomeApiRequest.Public
import com.sea.seaconnect.model.ResponseModel.PojoReview
import com.sea.seaconnect.model.ResponseModel.UserDetail
import com.sea.seaconnect.viewModel.ReviewViewModel
import kotlinx.android.synthetic.main.activity_review.*


class ReviewActivity : AppCompatActivity(), View.OnClickListener {
    private var viewModel: ReviewViewModel? = null
    var auth_token = ""
    var mOtherUserID = ""
    var mConnectionId = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_review)
        initView()
    }

    private fun initView() {
        auth_token = AppPreferences.init(this).getString(Constants.TOKEN)
        viewModel = ViewModelProviders.of(this).get(ReviewViewModel::class.java)
        if (intent.extras != null) {
            val connectionData: UserDetail =
                intent.getParcelableExtra("UserDatas") as UserDetail

            mOtherUserID = connectionData._id
            mConnectionId = intent.getStringExtra("connectionID")
            Glide.with(this).load(Constants.PROFILE_URL_API + connectionData.profile_image)
                .into(cvOtherUser)
            Glide.with(this).load(AppPreferences.init(this).getString(Constants.PROFILE_IMAGE))
                .into(cvMyImage)
        }
        iv_back.setOnClickListener(this)
        btnSubmit.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.iv_back -> {
                onBackPressed()
            }
            R.id.btnSubmit -> {

                val publicList = mutableListOf<Public>()
                publicList.clear()
                val public =
                    Public("Were user’s photos accurate?", rbPhotos.rating.toString(), "rating")
                val public1 = Public(
                    "Was the bio consistent with how they presented?",
                    rbBio.rating.toString(),
                    "rating"
                )
                val public2 = Public(
                    "How would you rate your interaction experience?",
                    rbIntrectionReviews.rating.toString(),
                    "rating"
                )
                val public3 = Public(
                    "How could interaction have been better?",
                    etFeedBack.text.toString().trim(),
                    "other"
                )
                publicList.add(public)
                publicList.add(public1)
                publicList.add(public2)
                publicList.add(public3)

                val privateList = mutableListOf<Private>()
                privateList.clear()
                val selectedId: Int = radioGroup.checkedRadioButtonId
                val radioRating = findViewById<View>(selectedId) as RadioButton
                var rating = "1"
                if (radioRating.text == "Yes") {
                    rating = "1"
                } else {
                    rating = "0"

                }
                val private =
                    Private("Would you meet this person again?", rating)
                val private1 =
                    Private(
                        "Do you have any feedback for the other user?",
                        etOtherFeedback.text.toString().trim()
                    )
                privateList.add(private)
                privateList.add(private1)
                var overallRating = rbBio.rating + rbPhotos.rating + rbIntrectionReviews.rating
                overallRating /= 3
                val postReview = PostReview(
                    mConnectionId,
                    privateList,
                    publicList,
                    AppPreferences.init(this).getString(Constants.USER_ID),
                    etFeedBack.text.toString().trim(), overallRating.toString()
                )
                applyRatingApi(postReview)
            }

        }

    }

    //..........................................interest list api...........................................

    private fun applyRatingApi(postReview: PostReview) {
        clLoader_rating.visibility = View.VISIBLE
        viewModel?.addReview(auth_token, postReview)?.observe(
            this,
            object : Observer<PojoReview?> {
                override fun onChanged(@Nullable pojoRating: PojoReview?) {
                    clLoader_rating.visibility = View.GONE
                    Toast.makeText(this@ReviewActivity, pojoRating!!.message, Toast.LENGTH_SHORT)
                        .show()
                    finish()
                }
            })


    }


    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }
}
