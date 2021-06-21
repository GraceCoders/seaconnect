package com.sea.seaconnect.controller.utills

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

object PermissionCheckUtil : AppCompatActivity() {
    val PERMISSION_REQUEST_CODE = 123

    private var mContext: Context? = null

    private var permissionCheckCallback: onPermissionCheckCallback? = null

    private fun PermissionCheckUtil(
        context: Context,
        callback: onPermissionCheckCallback
    ) {
        mContext = context
        permissionCheckCallback = callback
        permissionsCheck()
    }

    fun permissionsCheck() {
        if (Build.VERSION.SDK_INT >= 23) {
            if ((ContextCompat.checkSelfPermission(
                    mContext!!,
                    Manifest.permission.READ_EXTERNAL_STORAGE
                )
                        == PackageManager.PERMISSION_GRANTED) && (ContextCompat.checkSelfPermission(
                    mContext!!,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                )
                        == PackageManager.PERMISSION_GRANTED) && (ContextCompat.checkSelfPermission(
                    mContext!!,
                    Manifest.permission.CAMERA
                )
                        == PackageManager.PERMISSION_GRANTED) && (ContextCompat.checkSelfPermission(
                    mContext!!,
                    Manifest.permission.RECEIVE_SMS
                )
                        == PackageManager.PERMISSION_GRANTED) && (ContextCompat.checkSelfPermission(
                    mContext!!,
                    Manifest.permission.READ_SMS
                )
                        == PackageManager.PERMISSION_GRANTED) && (ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.ACCESS_FINE_LOCATION
                )
                        == PackageManager.PERMISSION_GRANTED) && (ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                )
                        == PackageManager.PERMISSION_GRANTED) && (ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.CALL_PHONE
                )
                        == PackageManager.PERMISSION_GRANTED)
            ) {
                permissionCheckCallback!!.onPermissionSuccess()
            } else {
                ActivityCompat.requestPermissions(
                    (mContext as Activity?)!!, arrayOf(
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.CAMERA,
                        Manifest.permission.RECEIVE_SMS,
                        Manifest.permission.READ_SMS,
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION
                    ), PERMISSION_REQUEST_CODE
                )
            }
        } else {
            permissionCheckCallback!!.onPermissionSuccess()
        }
    }

    fun create(
        context: Context,
        callback: onPermissionCheckCallback
    ) {
        PermissionCheckUtil(context, callback)
    }

    interface onPermissionCheckCallback {
        fun onPermissionSuccess()
    }
}