package com.sea.seaconnect.controller.utills

import android.R
import android.app.Activity
import androidx.core.content.ContextCompat
import com.google.android.material.snackbar.Snackbar
import com.sea.seaconnect.controller.Utills.App

object SnackbarUtil {
    fun showWarningShortSnackbar(activity: Activity, message: String?) {
        val snackbar = Snackbar.make(
            activity.findViewById(R.id.content),
            message!!,
            4500
        )

        snackbar.show()
    }


}