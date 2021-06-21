package com.sea.seaconnect.controller.utills

import android.app.Activity
import android.text.TextUtils
import android.util.Patterns
import android.view.View
import android.widget.EditText
import android.widget.TextView
import com.google.android.material.textfield.TextInputEditText

import com.sea.seaconnect.R
import com.sea.seaconnect.controller.CustomViews.MyEditTextRegular
import com.sea.seaconnect.controller.CustomViews.MyTextViewRegular
import com.sea.seaconnect.view.activity.EditProfileActivity
import com.sea.seaconnect.view.activity.SignupActivity


object Validations {

    /**
     * @param editText - EditText field which need to be validated
     * @return - Returns true if editText is Null or empty
     */
    fun isNullOrEmpty(editText: EditText): Boolean {
        return (editText.text == null
                || editText.text.toString().trim { it <= ' ' }.length == 0)
    }    fun isNullOrEmptys(editText: TextView): Boolean {
        return (editText.text == null
                || editText.text.toString().trim { it <= ' ' }.length == 0)
    }
    private fun validateEmailAddress(
        applicationContext: Activity,
        view: View,
        errMessage: String
    ): Boolean {
        val email = (view as EditText).text.toString().trim { it <= ' ' }
        if (email.contains("[a-zA-Z0-9._-]+") || email.contains("@")) {
            if (email.isEmpty() || !isValidEmail(email)) {



                SnackbarUtil.showWarningShortSnackbar(applicationContext, errMessage)
                // requestFocus(applicationContext, ((EditText) view));
                return true
            }
        } else {
            SnackbarUtil.showWarningShortSnackbar(applicationContext, errMessage)
            // requestFocus(applicationContext, ((EditText) view));
            return true
        }
        return false
    }

    private fun isValidEmail(email: String): Boolean {
        return !TextUtils.isEmpty(email) && Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    private fun validateUsername(
        applicationContext: Activity,
        mEtUsername: EditText,
        errMessage: String
    ): Boolean {
        if (isNullOrEmpty(mEtUsername)) {
            SnackbarUtil.showWarningShortSnackbar(applicationContext, errMessage)
            mEtUsername.isFocusable = true
            mEtUsername.requestFocus()
            return true
        }
        return false
    }
    private fun validateTestname(
        applicationContext: Activity,
        mEtUsername: MyTextViewRegular,
        errMessage: String
    ): Boolean {
        if (isNullOrEmptys(mEtUsername)) {
            SnackbarUtil.showWarningShortSnackbar(applicationContext, errMessage)
            mEtUsername.isFocusable = true
            mEtUsername.requestFocus()
            return true
        }
        return false
    }



    fun isValidateLogin(applicationContext: Activity, mEtUsername: EditText?, mEtPassword: EditText?): Boolean {
        if (validateEmailAddress(applicationContext, mEtUsername!!, applicationContext.resources.getString(R.string.error_empty_user_id))) {
            return false
        }
        if (validateUsername(applicationContext, mEtPassword!!, applicationContext.resources.getString(R.string.error_empty_password))) {
            return false
        }
        return true
    }
//
//    fun isValidateOffer(
//        mContext: Context?,
//        etOfferTitle: TextInputEditText,
//        etPrice: TextInputEditText,
//        etMagic: TextInputEditText,
//        etImportentInformation: TextInputEditText
//    ): Boolean {
//        if (validateUsername(mContext as Activity, etOfferTitle!!, mContext.resources.getString(R.string.error_all_field_required))) {
//            return false
//        }
//        if (validateUsername(mContext, etPrice!!, mContext.resources.getString(R.string.error_all_field_required))) {
//            return false
//        }
//        if (validateUsername(mContext, etMagic!!, mContext.resources.getString(R.string.error_all_field_required))) {
//            return false
//        }
//        if (validateUsername(mContext, etImportentInformation!!, mContext.resources.getString(R.string.error_all_field_required))) {
//            return false
//        }
//        return true
//    }
//
//    fun isValidateEmail(applicationContext: Activity,mEtEmail: EditText?):Boolean
//    {
//        if (validateEmailAddress(applicationContext, mEtEmail!!, applicationContext.resources.getString(R.string.error_empty_user_id))) {
//            return false
//        }
//
//        return true
//    }
//
//
//
//    fun isValidateSimpleName(applicationContext: Activity,mEtName: EditText?):Boolean
//    {
//        if (validateUsername(applicationContext, mEtName!!, applicationContext.resources.getString(R.string.error_empty_first_name))) {
//            return false
//        }
//        return true
//    }
//
//
//
//    fun  isValidatePassword(applicationContext: Activity,mEtpassword: EditText?):Boolean
//    {
//        if (validateUsername(applicationContext, mEtpassword!!, applicationContext.resources.getString(R.string.error_empty_password))) {
//            return false
//        }
//        return true
//    }
//
    fun isValidateSignup(
    applicationContext: SignupActivity,
    etUserName: TextInputEditText,
    etFirstName: TextInputEditText,
    etEmailId: TextInputEditText,
    etPwd: TextInputEditText,
    etConfirmPwd: TextInputEditText,
    etMobileNumber: TextInputEditText,
    etLookingFor: MyEditTextRegular
): Boolean {
    if (validateUsername(applicationContext, etUserName!!, applicationContext.resources.getString(
            R.string.error_empty_user_name))) {
        return false
    }
        if (validateUsername(applicationContext, etFirstName!!, applicationContext.resources.getString(
                R.string.error_empty_first_name))) {
            return false
        }

        if (validateEmailAddress(applicationContext, etEmailId!!, applicationContext.resources.getString(R.string.error_empty_user_id))) {
            return false
        }
        if (validateUsername(applicationContext, etPwd!!, applicationContext.resources.getString(R.string.error_empty_password))) {
            return false
        }
    if (validateUsername(applicationContext, etConfirmPwd!!, applicationContext.resources.getString(R.string.error_empty_cfm_password))) {
        return false
    }
    if (validateUsername(applicationContext, etMobileNumber!!, applicationContext.resources.getString(R.string.error_mobile_number))) {
        return false
    }
    if (validateUsername(applicationContext, etLookingFor!!, applicationContext.resources.getString(R.string.error_all_field_required))) {
        return false
    }
        return true
    }

    fun isValidateUpdateProfile(
        applicationContext: EditProfileActivity,
        etFirstName: TextInputEditText,
        etMobileNumber: TextInputEditText,
        etLookingFor: MyEditTextRegular,
        tv_dob: MyTextViewRegular
    ): Boolean {

        if (validateUsername(applicationContext, etFirstName!!, applicationContext.resources.getString(
                R.string.error_empty_first_name))) {
            return false
        }



    if (validateUsername(applicationContext, etMobileNumber!!, applicationContext.resources.getString(R.string.error_mobile_number))) {
        return false
    }
    if (validateUsername(applicationContext, etLookingFor!!, applicationContext.resources.getString(R.string.error_Bio))) {
        return false
    }

        return true
    }
//
//
//    fun isValidateProfile(
//        applicationContext: VendorProfileActivity,
//        etStoreName: MyEditTextRegular,
//        etAddress: MyEditTextRegular,
//        etContactDetails: MyEditTextRegular,
//        etAbout: MyEditTextRegular
//    ): Boolean {
//        if (validateUsername(applicationContext, etStoreName!!, applicationContext.resources.getString(
//                R.string.error_store_name))) {
//            return false
//        }
//        if (validateUsername(applicationContext, etAddress!!, applicationContext.resources.getString(R.string.error_store_address))) {
//            return false
//        }
//        if (validateUsername(applicationContext, etContactDetails!!, applicationContext.resources.getString(R.string.error_store_contact_details))) {
//            return false
//        }
//        if (validateUsername(applicationContext, etAbout!!, applicationContext.resources.getString(R.string.error_store_about))) {
//            return false
//        }
//        return true
//    }



}