package com.sea.seaconnect.view.activity

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.annotation.Nullable
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.sea.seaconnect.R
import com.sea.seaconnect.controller.Utills.App
import com.sea.seaconnect.controller.Utills.AppPreferences
import com.sea.seaconnect.controller.utills.Constants
import com.sea.seaconnect.controller.utills.SnackbarUtil
import com.sea.seaconnect.controller.utills.Validations
import com.sea.seaconnect.model.ResponseModel.loginmodel.LoginResponseModel
import com.sea.seaconnect.viewModel.LoginViewModel
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity(),View.OnClickListener {
    private  var viewModel: LoginViewModel?=null
    var statusCode:Int?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
        setContentView(R.layout.activity_login)
        initView()
    }

    private fun initView() {
        viewModel = ViewModelProviders.of(this).get(LoginViewModel::class.java)
        ll_login.setOnClickListener(this)
        tv_sign_up.setOnClickListener(this)

    }

    override fun onClick(v: View?) {

        when(v?.id)
        {
            R.id.ll_login ->
            {
                if(Validations.isValidateLogin(this,et_email,et_pwd))
                {
                    LoginApi()
                }
            }
            R.id.tv_sign_up ->
            {
                val i = Intent(this, SignupActivity::class.java)
                startActivity(i)
            }
        }

    }

    //.....................................Login api.....................................

    private fun LoginApi() {

        clLoader.visibility = View.VISIBLE
        viewModel?.login(et_email.text.toString().trim(),et_pwd.text.toString().trim())?.observe(
            this,
            object : Observer<LoginResponseModel> {
                override fun onChanged(@Nullable loginResponse: LoginResponseModel?) {
                    clLoader.visibility = View.GONE
                    statusCode=loginResponse?.code

                    if(statusCode==200)
                    {
                        AppPreferences.init(App.getAppContext())
                            .putString(Constants.PASSWORD,et_pwd.text.toString().trim()!!)
                        val intent = Intent(applicationContext,ChooseInterestActivity ::class.java)
                        startActivity(intent)
                        finishAffinity()
                    }
                    else
                    {
                        showSnackBar(loginResponse?.message)
                    }

                }
            })


    }

    private fun showSnackBar(message: String?) {
        clLoader.visibility = View.GONE

        SnackbarUtil.showWarningShortSnackbar(this,message)
    }


    //................................hide keyboard on touch.................................................
    fun hideSoftKeyboard(activity: Activity) {
        val inputMethodManager =
            activity.getSystemService(
                Activity.INPUT_METHOD_SERVICE
            ) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(
            activity.currentFocus!!.windowToken, 0
        )
    }

    override fun dispatchTouchEvent(ev: MotionEvent): Boolean {
        val view = currentFocus
        val ret = super.dispatchTouchEvent(ev)
        if (view is EditText) {
            val w = currentFocus
            val scrcoords = IntArray(2)
            w!!.getLocationOnScreen(scrcoords)
            val x = ev.rawX + w.left - scrcoords[0]
            val y = ev.rawY + w.top - scrcoords[1]
            if (ev.action == MotionEvent.ACTION_UP
                && (x < w.left || x >= w.right || y < w.top || y > w.bottom)
            ) {
                hideSoftKeyboard(this)
            }
        }
        return ret
    }
}
