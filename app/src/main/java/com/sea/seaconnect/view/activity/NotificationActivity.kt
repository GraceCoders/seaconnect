package com.sea.seaconnect.view.activity

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.annotation.Nullable
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.sea.seaconnect.R
import com.sea.seaconnect.controller.Utills.AppPreferences
import com.sea.seaconnect.controller.utills.Constants
import com.sea.seaconnect.model.ResponseModel.NotificationDataList
import com.sea.seaconnect.model.ResponseModel.PojoNotificationList
import com.sea.seaconnect.model.ResponseModel.PojoResetNotificationCount
import com.sea.seaconnect.view.adapter.Notification_Adapter
import com.sea.seaconnect.viewModel.NotificationViewModel
import kotlinx.android.synthetic.main.activity_choose_interest.*
import kotlinx.android.synthetic.main.activity_notification.*


class NotificationActivity : AppCompatActivity(), View.OnClickListener {
    var auth_token = ""
    private var noti_list: ArrayList<NotificationDataList> = ArrayList()

    private var viewModel: NotificationViewModel? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_notification)
        initView()
    }

    private fun initView() {
        auth_token = AppPreferences.init(this).getString(Constants.TOKEN)
        viewModel = ViewModelProviders.of(this).get(NotificationViewModel::class.java)
        iv_back.setOnClickListener(this)


//        (rootView?.rv_message?.adapter as Message_Adapter).onItemClick = { pos, view ->
//        }
        rv_notifications?.isNestedScrollingEnabled = false

        // hit api
        Notification_list_api()
        reset_notification_count_api()
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.iv_back -> {
                onBackPressed()
            }
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }

    //..........................................notification list api...........................................

    private fun Notification_list_api() {
        clLoader_notification.visibility = View.VISIBLE
        viewModel?.getNotificationList(auth_token)?.observe(
            this,
            object : Observer<PojoNotificationList?> {
                override fun onChanged(@Nullable interestlist: PojoNotificationList?) {
                    clLoader_notification.visibility = View.GONE
                    if (interestlist!!.code == 200) {
                        tvNotification.visibility = View.GONE

                        noti_list = interestlist.data as ArrayList<NotificationDataList>
                        if (noti_list.isNotEmpty()) {
                            tvNotification.visibility = View.GONE
                            //...........................adapter for notifications .....................................//

                            rv_notifications?.layoutManager =
                                LinearLayoutManager(this@NotificationActivity)
                            rv_notifications?.adapter =
                                Notification_Adapter(this@NotificationActivity, noti_list)
                        } else {
                            tvNotification.visibility = View.VISIBLE

                        }

                    } else {
                        Toast.makeText(
                            this@NotificationActivity,
                            interestlist.message,
                            Toast.LENGTH_SHORT
                        ).show()
                    }


                }
            })


    }


    //..........................................notification count reset  api...........................................

    private fun reset_notification_count_api() {
        viewModel?.resetNotificationCount(auth_token)?.observe(
            this,
            object : Observer<PojoResetNotificationCount?> {
                override fun onChanged(@Nullable interestlist: PojoResetNotificationCount?) {


                }
            })


    }

}
