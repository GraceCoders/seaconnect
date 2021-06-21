package com.sea.seaconnect.view.activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.sea.seaconnect.R
import com.sea.seaconnect.controller.Utills.AppPreferences
import com.sea.seaconnect.controller.utills.Constants
import com.sea.seaconnect.controller.utills.SnackbarUtil
import com.sea.seaconnect.model.ResponseModel.PojoConnectionList
import com.sea.seaconnect.view.adapter.Quaue_Adapter
import com.sea.seaconnect.viewModel.ConnectionRequestViewModel
import kotlinx.android.synthetic.main.activity_connection.*
import kotlinx.android.synthetic.main.activity_notification.*
import kotlinx.android.synthetic.main.activity_notification.iv_back
import kotlinx.android.synthetic.main.activity_review_queaue.*

class ReviewQueaueActivity : AppCompatActivity(), View.OnClickListener {
    private var viewModel: ConnectionRequestViewModel? = null
    var auth_token = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_review_queaue)
        initView()
    }

    private fun initView() {

        auth_token = AppPreferences.init(this).getString(Constants.TOKEN)
        viewModel = ViewModelProviders.of(this).get(ConnectionRequestViewModel::class.java)
        iv_back.setOnClickListener(this)
        connection_list("accepted")

    }

    //---------------------------- get connection list api --------------------------------------//
    private fun connection_list(status: String) {
        clLoader_Connection_Review.visibility = View.VISIBLE
        viewModel?.connectionsList(auth_token, status)?.observe(
            this,
            object : Observer<PojoConnectionList?> {


                override fun onChanged(pojoConnectionList: PojoConnectionList?) {
                    clLoader_Connection_Review.visibility = View.GONE

                    if (pojoConnectionList!!.code == 200) {

                        if (pojoConnectionList.data != null) {
                            tvNoReview.visibility = View.GONE

                            //...........................adapter for review Queaue .....................................//

                            rv_queaue?.layoutManager = GridLayoutManager(
                                this@ReviewQueaueActivity,
                                2
                            )
                            rv_queaue?.adapter =
                                Quaue_Adapter(this@ReviewQueaueActivity, pojoConnectionList.data)
                            (rv_queaue?.adapter as Quaue_Adapter).onItemClick =
                                { pos, view, userdetail,connectionID ->


                                    val intent = Intent(
                                        this@ReviewQueaueActivity,
                                        ReviewActivity::class.java
                                    )
                                    intent.putExtra("UserDatas", userdetail)
                                    intent.putExtra("connectionID", connectionID)

                                    startActivity(intent)


                                }
                            rv_queaue?.isNestedScrollingEnabled = false
                        } else {
                            tvNoReview.visibility = View.VISIBLE
                        }
                    } else {

                        showSnackBar(pojoConnectionList.message)
                    }

                }
            })

    }

    private fun showSnackBar(message: String?) {
        SnackbarUtil.showWarningShortSnackbar(this, message)
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
}
