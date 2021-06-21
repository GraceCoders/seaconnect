package com.sea.seaconnect.view.activity

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.annotation.Nullable
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.sea.seaconnect.R
import com.sea.seaconnect.controller.Utills.AppPreferences
import com.sea.seaconnect.controller.utills.Constants
import com.sea.seaconnect.controller.utills.SnackbarUtil
import com.sea.seaconnect.model.ResponseModel.PojoSendConnection
import com.sea.seaconnect.model.ResponseModel.PojoUserProfileData
import com.sea.seaconnect.model.ResponseModel.UserData
import com.sea.seaconnect.view.adapter.Photos_Adapter
import com.sea.seaconnect.view.adapter.Review_Adapter
import com.sea.seaconnect.viewModel.ProfileViewModel
import kotlinx.android.synthetic.main.activity_choose_interest.*
import kotlinx.android.synthetic.main.activity_profile_detail.*
import java.text.DecimalFormat

class ProfileDetailActivity : AppCompatActivity(), View.OnClickListener {

    private var viewModel: ProfileViewModel? = null

    private var auth_token = ""
    private var mUserId = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile_detail)
        initView()
    }

    private fun initView() {

        auth_token = AppPreferences.init(this).getString(Constants.TOKEN)
        viewModel = ViewModelProviders.of(this).get(ProfileViewModel::class.java)

        //................................. get data from intent................................//
        if (intent!!.extras != null) {
            mUserId = intent.extras!!.getString(Constants.USERID, "")
            user_data_api(mUserId)
        }


        //................................. Set click listener................................//
        tv_keep_looking.setOnClickListener(this)
        tv_connect.setOnClickListener(this)
        iv_back.setOnClickListener(this)
        tv_yes.setOnClickListener(this)
        tv_no.setOnClickListener(this)
        tv_may_be_later.setOnClickListener(this)

    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.tv_keep_looking -> {
                onBackPressed()
            }
            R.id.tv_connect -> {
                ll_connect.visibility = View.GONE
                ll_yes_no.visibility = View.VISIBLE
            }
            R.id.tv_yes -> {

                send_connection_request()
            }
            R.id.tv_no -> {
                ll_connect.visibility = View.GONE
            }
            R.id.tv_may_be_later -> {
                ll_connect.visibility = View.VISIBLE
                ll_yes_no.visibility = View.GONE
            }
            R.id.iv_back -> {
                onBackPressed()
            }
        }
    }


    //..........................................User Profile By ID api...........................................

    private fun user_data_api(mUserId: String) {
        clLoader_profile.visibility = View.VISIBLE
        viewModel!!.getProfileData(mUserId, auth_token).observe(
            this,
            object : Observer<PojoUserProfileData?> {
                override fun onChanged(@Nullable pojoUserProfileData: PojoUserProfileData?) {
                    clLoader_profile.visibility = View.GONE

                    val statusCode = pojoUserProfileData?.code
                    if (statusCode == 200) {
                        liMain.visibility = View.VISIBLE
                        ll_connect.visibility = View.VISIBLE


                        setData(pojoUserProfileData.data)
                    } else {
                        showSnackBar(pojoUserProfileData!!.message)

                    }
                }


            })


    }    //..........................................Send Connection Request api...........................................

    private fun send_connection_request() {
        clLoader_profile.visibility = View.VISIBLE
        viewModel!!.sendConnectionRequest(mUserId, auth_token).observe(
            this,
            object : Observer<PojoSendConnection?> {
                override fun onChanged(@Nullable pojoUserProfileData: PojoSendConnection?) {
                    clLoader_profile.visibility = View.GONE

                    val statusCode = pojoUserProfileData?.code
                    if (statusCode == 200) {
                        Toast.makeText(
                            this@ProfileDetailActivity,
                            getString(R.string.request_send),
                            Toast.LENGTH_SHORT
                        ).show()
                        finish()


                    } else {
                        showSnackBar(pojoUserProfileData!!.message)

                    }
                }


            })


    }

    //set data on view
    private fun setData(data: UserData) {

        Glide.with(this).load(data.profile_image_link).into(cvUserImage)
        tvUserName.text = data.first_name + " ("
        if (data.avgRating.isNotEmpty()) {

            tvRating.text = DecimalFormat("##.#").format(data.avgRating[0]) + " )"
        }
        tvBio.text = data.bio

        //.................................adapter for photos.................................//
        if (data.userImages.isNotEmpty()) {
            tvPhotos.visibility=View.VISIBLE
            rv_photos?.layoutManager = GridLayoutManager(this, 2)
            rv_photos?.adapter = Photos_Adapter(this, data.userImages)
            rv_photos?.isNestedScrollingEnabled = false
        }
        else
        {tvPhotos.visibility=View.GONE

        }
        //...........................adapter for reviews .....................................//

        if (data.reviews.isNotEmpty()) {


            rv_reviews?.layoutManager = LinearLayoutManager(this)
            rv_reviews?.adapter = Review_Adapter(this, data.reviews)
//        (rootView?.rv_message?.adapter as Message_Adapter).onItemClick = { pos, view ->
//        }
            rv_reviews?.isNestedScrollingEnabled = false
        }
    }

    private fun showSnackBar(message: String?) {
        SnackbarUtil.showWarningShortSnackbar(this, message)
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }
}
