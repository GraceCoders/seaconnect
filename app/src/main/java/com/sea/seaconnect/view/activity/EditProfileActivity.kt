package com.sea.seaconnect.view.activity

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.app.DatePickerDialog
import android.content.Intent
import android.database.Cursor
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.TextView
import androidx.annotation.Nullable
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import com.bumptech.glide.Glide
import com.sea.seaconnect.R
import com.sea.seaconnect.controller.Utills.AppPreferences
import com.sea.seaconnect.controller.utills.Constants
import com.sea.seaconnect.controller.utills.SnackbarUtil
import com.sea.seaconnect.model.PojoImagesList
import com.sea.seaconnect.model.ResponseModel.PojoSendConnection
import com.sea.seaconnect.model.ResponseModel.PojoUserProfileData
import com.sea.seaconnect.model.ResponseModel.UserImage
import com.sea.seaconnect.view.`interface`.OnImageClickListener
import com.sea.seaconnect.view.adapter.User_Photos_Adapter
import com.sea.seaconnect.viewModel.ProfileViewModel
import kotlinx.android.synthetic.main.activity_connection.*
import kotlinx.android.synthetic.main.activity_edit_profile.*
import kotlinx.android.synthetic.main.activity_edit_profile.et_first_name
import kotlinx.android.synthetic.main.activity_edit_profile.iv_back
import kotlinx.android.synthetic.main.activity_edit_profile.sp_age
import kotlinx.android.synthetic.main.activity_edit_profile.sp_gender
import kotlinx.android.synthetic.main.activity_edit_profile.sp_gender_meet
import kotlinx.android.synthetic.main.activity_profile_detail.*
import kotlinx.android.synthetic.main.activity_signup.*
import kotlinx.android.synthetic.main.item_images.*
import kotlinx.android.synthetic.main.spinner_dropdown_item.view.*
import pub.devrel.easypermissions.EasyPermissions
import java.io.File
import java.util.*

class EditProfileActivity : AppCompatActivity(), View.OnClickListener {
    private var viewModel: ProfileViewModel? = null
    private var auth_token = ""
    private var mUserId = ""
    var mGender: String? = null
    var mInterested: String? = null
    var mAge: String? = null
    var mImagefile: File? = null
    private val PICK_FROM_GALLARY = 1
    var isImageList = false
    var mUserImage: File? = null
    var mImagesList = mutableListOf<PojoImagesList>()
    var mMainImagesList = mutableListOf<UserImage>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_profile)
        initView()
    }

    private fun initView() {
        auth_token = AppPreferences.init(this).getString(Constants.TOKEN)
        mUserId = AppPreferences.init(this).getString(Constants.USER_ID)
        viewModel = ViewModelProviders.of(this).get(ProfileViewModel::class.java)

        //set click listener
        iv_calender.setOnClickListener(this)
        iv_back.setOnClickListener(this)
        cvEditUserImage.setOnClickListener(this)
        tvSave.setOnClickListener(this)

        val genderSp = resources.getStringArray(R.array.Gender)
        val genderSpMeet = resources.getStringArray(R.array.GenderMeet)
        val ageRange = resources.getStringArray(R.array.AgeRange)


//.........................................gender spinner..................................................//

        if (sp_gender != null) {
            val adapter = ArrayAdapter(
                this,
                android.R.layout.simple_spinner_dropdown_item, genderSp
            )
            sp_gender.adapter = adapter
            sp_gender.onItemSelectedListener = object :
                AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>,
                    view: View, position: Int, id: Long
                ) {
                    (view as TextView).setTextColor(Color.WHITE)
                    if (position == 0) {
                    } else {
                        if (sp_gender_meet?.selectedItem.toString().equals("Man")) {
                            mGender = "Male"
                        } else if (sp_gender_meet?.selectedItem.toString().equals("Woman")) {
                            mGender = "Female"
                        } else if (sp_gender_meet?.selectedItem.toString().equals("TransGender")) {
                            mGender = "Transgender"
                        }
                        //             genderSp[position]
                    }

                }

                override fun onNothingSelected(parent: AdapterView<*>) {
                }
            }
        }


        //......................................age person you want to meet.....................................//

        if (sp_gender_meet != null) {
            val adapter = ArrayAdapter(
                this,
                android.R.layout.simple_spinner_dropdown_item, genderSpMeet
            )
            sp_gender_meet.adapter = adapter
            sp_gender_meet.onItemSelectedListener = object :
                AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>,
                    view: View, position: Int, id: Long
                ) {

                    (view as TextView).setTextColor(Color.WHITE)
                    if (position == 0) {
                    } else {
                        if (sp_gender_meet?.selectedItem.toString().equals("Man")) {
                            mInterested = "Male"
                        } else if (sp_gender_meet?.selectedItem.toString().equals("Woman")) {
                            mInterested = "Female"
                        } else if (sp_gender_meet?.selectedItem.toString().equals("TransGender")) {
                            mInterested = "Transgender"
                        }
                    }

                }

                override fun onNothingSelected(parent: AdapterView<*>) {
                }
            }
        }

        //>>>>>>......................................age range.....................................//

        if (sp_age != null) {
            val adapter = ArrayAdapter(
                this,
                android.R.layout.simple_spinner_dropdown_item, ageRange
            )
            sp_age.adapter = adapter
            sp_age.onItemSelectedListener = object :
                AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>,
                    view: View, position: Int, id: Long
                ) {

                    (view as TextView).setTextColor(Color.WHITE)
                    if (position == 0) {
                    } else {
                        mAge = sp_age?.selectedItem.toString()

                    }

                }

                override fun onNothingSelected(parent: AdapterView<*>) {
                }
            }
        }

        // get data from api
        user_data_api()


    }

    override fun onClick(v: View?) {

        when (v?.id) {
            R.id.iv_calender -> {
                openCalender()
            }
            R.id.iv_back -> {
                onBackPressed()
            }
            R.id.cvEditUserImage -> {
                pickPicture()
                isImageList = false
            }
            R.id.tvSave -> {


                if (et_first_name.textDirection.toString().trim() == "") {
                    showSnackBar(getString(R.string.error_empty_first_name))

                } else if (tvMobile.textDirection.toString().trim() == "") {
                    showSnackBar(getString(R.string.mobile_number))

                } else if (etBio.textDirection.toString().trim() == "") {
                    showSnackBar(getString(R.string.error_Bio))

                } else if (tv_dob.textDirection.toString().trim() == "") {
                    showSnackBar(getString(R.string.error_DOB))

                } else {
                    if (!mInterested.isNullOrEmpty() && !mGender.isNullOrEmpty() && !mAge.isNullOrEmpty()) {
                        if (mImagesList.isNotEmpty()) {
                            updateUserImagesApi()
                        }

                        if (mUserImage != null) {
                            updateProfileImageApi()
                        }
                        updateProfileApi()

                    } else {
                        showSnackBar(getString(R.string.error_all_field_required))
                    }
                }
            }


        }
    }


    //.....................................update Profile............................................

    fun updateProfileApi() {
        clLoaderEditProfile.visibility = View.VISIBLE
        viewModel!!.updateProfile(
            auth_token,
            et_first_name.text.toString().trim(),
            et_email.text.toString().trim(),
            mGender,
            mInterested,
            mAge,
            tvMobile.text.toString(),
            etBio.text.toString(),
            tv_dob.text.toString(),
            mUserId,
            AppPreferences.init(this@EditProfileActivity).getString(Constants.PASSWORD),
            AppPreferences.init(this@EditProfileActivity).getString(Constants.USERNAME)
        )
            .observe(
                this,
                { pojoUserProfileData ->
                    clLoaderEditProfile.visibility = View.GONE

                    val statusCode = pojoUserProfileData?.code
                    if (statusCode == 200) {
                        finish()

                    } else {
                        showSnackBar(pojoUserProfileData!!.message)

                    }
                })
    }


    //.....................................update Profile Image ............................................

    fun updateProfileImageApi() {
        viewModel!!.updateProfileImage(
            auth_token,
            mUserId, mUserImage!!
        )
            .observe(
                this,
                { pojoUserProfileData ->

                    val statusCode = pojoUserProfileData?.code
                    if (statusCode == 200) {

                    } else {
                        showSnackBar(pojoUserProfileData!!.message)

                    }
                })
    }
    //.....................................update user images api............................................

    fun updateUserImagesApi() {

        viewModel!!.updateMultipleImages(mImagesList, auth_token)
            .observe(
                this,
                object : Observer<PojoSendConnection?> {
                    override fun onChanged(@Nullable pojoUserProfileData: PojoSendConnection?) {
                        clLoaderEditProfile.visibility = View.GONE

                        val statusCode = pojoUserProfileData?.code
                        if (statusCode == 200) {


                        } else {
                            showSnackBar(pojoUserProfileData!!.message)

                        }
                    }


                })
    }
    //.....................................calender for dob............................................

    private fun openCalender() {
        val c = Calendar.getInstance()
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)


        val dpd = DatePickerDialog(
            this,
            DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
                val month = monthOfYear + 1

                // Display Selected date in textbox
                tv_dob.text = "$dayOfMonth/$month/$year"

            },
            year,
            month,
            day
        )


        dpd.show()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }


    private fun user_data_api() {
        clLoaderEditProfile.visibility = View.VISIBLE
        viewModel!!.getProfileData(mUserId, auth_token).observe(
            this,
            object : Observer<PojoUserProfileData?> {
                override fun onChanged(@Nullable pojoUserProfileData: PojoUserProfileData?) {
                    clLoaderEditProfile.visibility = View.GONE

                    val statusCode = pojoUserProfileData?.code
                    if (statusCode == 200) {
                        llMain.visibility = View.VISIBLE
                        RlImage.visibility = View.VISIBLE

                        Glide.with(this@EditProfileActivity)
                            .load(pojoUserProfileData.data.profile_image_link).into(cvEditUserImage)
                        et_first_name.setText(pojoUserProfileData.data.first_name)
                        et_email.setText(pojoUserProfileData.data.email)
                        tvMobile.setText(pojoUserProfileData.data.mobile_number)
                        tv_dob.text = pojoUserProfileData.data.dob
                        etBio.setText(pojoUserProfileData.data.bio)
                        mAge = pojoUserProfileData.data.age_range
                        mMainImagesList.clear()
                        mMainImagesList.addAll(pojoUserProfileData.data.userImages)
//.................... user image adapter .........................................//
                        rvImages?.layoutManager = GridLayoutManager(this@EditProfileActivity, 3)
                        rvImages?.adapter =
                            User_Photos_Adapter(this@EditProfileActivity, mMainImagesList, object :
                                OnImageClickListener {
                                override fun onImageDelete(imageId: String, postion: String) {                            viewModel?.deleteImage(imageId, auth_token)
                                    viewModel?.deleteImage(imageId, auth_token)
                                }

                                override fun onLocalImageDelete(position: String) {
                                    mImagesList.removeAt(position.toInt())

                                }

                            })
                        rvImages?.isNestedScrollingEnabled = false
                        (rvImages?.adapter as User_Photos_Adapter).onItemClick = { pos, view ->

                            isImageList = true
                            pickPicture()


                        }
                        val genderSp = resources.getStringArray(R.array.Gender)
                        val genderSpMeet = resources.getStringArray(R.array.GenderMeet)
                        val ageRange = resources.getStringArray(R.array.AgeRange)


                        when (pojoUserProfileData.data.gender) {
                            "Male" -> {
                                sp_gender.setSelection(1)
                                mGender = "Male"
                            }
                            "Female" -> {
                                sp_gender.setSelection(2)
                                mGender = "Female"
                            }
                            else -> {
                                sp_gender.setSelection(3)
                                mGender = "Transgender"
                            }
                        }
                        when (pojoUserProfileData.data.interested_in) {
                            "Male" -> {
                                mInterested = "Male"

                                sp_gender_meet.setSelection(1)
                            }
                            "Female" -> {
                                sp_gender_meet.setSelection(2)
                                mInterested = "Female"

                            }
                            else -> {
                                sp_gender_meet.setSelection(3)
                                mInterested = "Transgender"

                            }
                        }





                        for (i in genderSpMeet.indices) {
                            if (genderSpMeet[i] == pojoUserProfileData.data.interested_in) {
                                sp_gender_meet.setSelection(i)
                            }
                        }
                        for (i in ageRange.indices) {
                            if (ageRange[i] == pojoUserProfileData.data.age_range) {
                                sp_age.setSelection(i)
                            }
                        }

                    } else {
                        showSnackBar(pojoUserProfileData!!.message)

                    }
                }


            })


    }

    private fun showSnackBar(message: String?) {
        SnackbarUtil.showWarningShortSnackbar(this, message)
    }
    //>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>image from camera>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

    private fun pickPicture() {
        if (EasyPermissions.hasPermissions(
                applicationContext,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            )
        ) {
            val galleryIntent = Intent(
                Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI
            )
            startActivityForResult(
                galleryIntent, PICK_FROM_GALLARY
            )
        } else {
            EasyPermissions.requestPermissions(
                this,
                "Need Permission to access your Gallery and Camera", PICK_FROM_GALLARY,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            )
        }

    }


    //>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>on activity result for camera and gallery>>>>>>>>>>>>>>>>>>>>>>>>>

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            PICK_FROM_GALLARY -> if (resultCode == Activity.RESULT_OK) {
                val selectedImage = data?.data
                mImagefile = File(getRealPathFromURI(selectedImage))
                Log.e("imagefile", mImagefile.toString())
                if (isImageList) {
                    val pojoImagesList = PojoImagesList(mImagefile!!)
                    val ImagesList = UserImage(imageURl = mImagefile!!, isNotUploaded = true)

                    mImagesList.add(pojoImagesList)
                    mMainImagesList.add(ImagesList)
                    rvImages?.adapter = User_Photos_Adapter(this, mMainImagesList, object :
                        OnImageClickListener {
                        override fun onImageDelete(imageId: String, postion: String) {
                            viewModel?.deleteImage(imageId, auth_token)
                        }

                        override fun onLocalImageDelete(position: String) {
                            mImagesList.removeAt(position.toInt())

                        }

                    })
                    rvImages?.isNestedScrollingEnabled = false
                    (rvImages?.adapter as User_Photos_Adapter).onItemClick = { pos, view ->

                        isImageList = true
                        pickPicture()


                    }
                } else {
                    mUserImage = File(getRealPathFromURI(selectedImage))
                    Log.e("imagefile", mUserImage.toString())
                    cvEditUserImage?.setImageURI(selectedImage)
                }
            }
        }
    }

    //.................................getting real path of image....................................

    fun getRealPathFromURI(uri: Uri?): String? {
        @SuppressLint("Recycle") val cursor: Cursor =
            this.contentResolver?.query(uri!!, null, null, null, null)!!
        cursor.moveToFirst()
        val idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA)
        return cursor.getString(idx)
    }

}
