package com.sea.seaconnect.view.activity

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.database.Cursor
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.TextView
import androidx.annotation.Nullable
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.sea.seaconnect.R
import com.sea.seaconnect.controller.utills.SnackbarUtil
import com.sea.seaconnect.controller.utills.Validations
import com.sea.seaconnect.model.ResponseModel.signupModel.SignupResponse
import com.sea.seaconnect.viewModel.SignupViewModel
import kotlinx.android.synthetic.main.activity_signup.*
import pub.devrel.easypermissions.EasyPermissions
import java.io.File


class SignupActivity : AppCompatActivity(),View.OnClickListener {
    var mGender:String?=null
    var mInterested:String?=null
    var mAge:String?=null
    var mImagefile: File? = null
    private  var viewModel: SignupViewModel?=null
    private val PICK_FROM_GALLARY = 1
    var statusCode:Int?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
        setContentView(R.layout.activity_signup)
        initView()
    }

    private fun initView() {
        ll_signup.setOnClickListener(this)
        tv_sign_in.setOnClickListener(this)
        ll_upload.setOnClickListener(this)

        val genderSp = resources.getStringArray(R.array.Gender)
        val genderSpMeet = resources.getStringArray(R.array.GenderMeet)
        val ageRange = resources.getStringArray(R.array.AgeRange)
        viewModel = ViewModelProviders.of(this).get(SignupViewModel::class.java)


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
                        if(sp_gender?.selectedItem.toString().equals("Man"))
                        {
                            mGender="Male"
                        }
                        else if(sp_gender?.selectedItem.toString().equals("Woman"))
                        {
                            mGender="Female"
                        }
                        else if(sp_gender?.selectedItem.toString().equals("TransGender"))
                        {
                            mGender="Transgender"
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
                        if(sp_gender_meet?.selectedItem.toString().equals("Man"))
                        {
                            mInterested="Male"
                        }
                        else if(sp_gender_meet?.selectedItem.toString().equals("Woman"))
                        {
                            mInterested="Female"
                        }
                        else if(sp_gender_meet?.selectedItem.toString().equals("TransGender"))
                        {
                            mInterested="Transgender"
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
                android.R.layout.simple_spinner_dropdown_item,ageRange
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
                        mAge=sp_age?.selectedItem.toString()
                    }

                }
                override fun onNothingSelected(parent: AdapterView<*>) {
                }
            }
        }



    }

    override fun onClick(v: View?) {

        when(v?.id)
        {
            R.id.ll_signup ->
            {

                if(Validations.isValidateSignup(this,et_user_name,et_first_name,et_email_id,et_pwd,et_confirm_pwd,et_mobile_number,et_looking_for))
                {
                    if(!mInterested.isNullOrEmpty() && !mGender.isNullOrEmpty() && !mAge.isNullOrEmpty())
                    {
                        if(mImagefile!=null)
                        {
                            signUpApi()
                        }
                    }
                    else
                    {
                        showSnackBar(getString(R.string.error_all_field_required))
                    }


                }


            }
            R.id.tv_sign_in ->
            {

                val i = Intent(this, LoginActivity::class.java)
                startActivity(i)
            }
            R.id.ll_upload ->
            {
                pickPicture()
            }
        }

    }



    //...........................................hit signup api.......................................

    private fun signUpApi() {
        cl_signup.visibility = View.VISIBLE
        viewModel?.SignupUser(et_user_name.text.toString(),et_first_name.text.toString().trim(),et_email_id.text.toString().trim(),et_pwd.text.toString().trim(),et_confirm_pwd.text.toString().trim(),mGender,mInterested,mAge,et_mobile_number.text.toString(),et_looking_for.text.toString(),mImagefile)?.observe(
            this,
            object : Observer<SignupResponse> {
                override fun onChanged(@Nullable registerResponse: SignupResponse?) {
                    cl_signup.visibility = View.GONE
                    statusCode=registerResponse?.code

                    if(statusCode==200)
                    {

                        val intent = Intent(applicationContext, ChooseInterestActivity::class.java)
                        startActivity(intent)
                        finishAffinity()
                    }
                    else
                    {
                        showSnackBar(registerResponse?.message)
                    }

                }
            })

    }



    private fun showSnackBar(message: String?) {
        cl_signup.visibility = View.GONE

        SnackbarUtil.showWarningShortSnackbar(this,message)
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
                iv_profile_image?.setImageURI(selectedImage)
            }
        }
    }

    //.................................getting real path of image....................................

    fun getRealPathFromURI(uri: Uri?): String? {
        @SuppressLint("Recycle") val cursor: Cursor =
            this.getContentResolver()?.query(uri!!, null, null, null, null)!!
        cursor.moveToFirst()
        val idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA)
        return cursor.getString(idx)
    }





}


