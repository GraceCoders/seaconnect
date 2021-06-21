package com.sea.seaconnect.view.activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.sea.seaconnect.R
import com.sea.seaconnect.controller.Utills.AppPreferences
import com.sea.seaconnect.controller.utills.Constants
import com.sea.seaconnect.controller.utills.SnackbarUtil
import com.sea.seaconnect.model.ResponseModel.PojoConnectionAccpetReject
import com.sea.seaconnect.model.ResponseModel.PojoConnectionList
import com.sea.seaconnect.view.`interface`.BlockClickListener
import com.sea.seaconnect.view.adapter.Blocked_User_Adapter
import com.sea.seaconnect.viewModel.ConnectionRequestViewModel
import kotlinx.android.synthetic.main.activity_connection.*
import kotlinx.android.synthetic.main.activity_connection_request.*
import kotlinx.android.synthetic.main.activity_notification.iv_back
import kotlinx.android.synthetic.main.activity_review_queaue.*

class BLockedUserListActivity : AppCompatActivity(), View.OnClickListener {
    private var viewModel: ConnectionRequestViewModel? = null
    var auth_token = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_connection)
        initView()
    }

    private fun initView() {
        auth_token = AppPreferences.init(this).getString(Constants.TOKEN)
        viewModel = ViewModelProviders.of(this).get(ConnectionRequestViewModel::class.java)
        iv_back.setOnClickListener(this)
        liConnectionReuest.setOnClickListener(this)


        tvHeader.text = getString(R.string.blocked_user)
        tvNoRequest.text = getString(R.string.no_blocked_user)

        // hit api
        connection_list("block")
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.iv_back -> {
                onBackPressed()
            }
            R.id.liConnectionReuest -> {
                val i = Intent(this, ConnectionRequestActivity::class.java)
                startActivity(i)
            }
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }

    //---------------------------- get connection list api --------------------------------------//
    private fun connection_list(status: String) {
        clLoader_Connection_request.visibility = View.VISIBLE
        viewModel?.connectionsList(auth_token, status)?.observe(
            this,
            object : Observer<PojoConnectionList?> {


                override fun onChanged(pojoConnectionList: PojoConnectionList?) {
                    clLoader_Connection_request.visibility = View.GONE

                    if (pojoConnectionList!!.code == 200) {
                        rv_coonections.visibility = View.GONE

                        if (pojoConnectionList.data != null) {
                            tvNoRequest.visibility = View.GONE
                            rv_coonections.visibility = View.VISIBLE

                            //...........................adapter for notifications .....................................//

                            rv_coonections?.layoutManager =
                                LinearLayoutManager(this@BLockedUserListActivity)
                            rv_coonections?.adapter = Blocked_User_Adapter(
                                this@BLockedUserListActivity,
                                pojoConnectionList.data,
                                object :
                                    BlockClickListener {
                                    override fun onUnblock(status: String, connectionID: String) {
                                        connection_request_response(status, connectionID)
                                    }

                                })

                            rv_queaue?.isNestedScrollingEnabled = false
                        } else {
                            rv_coonections.visibility = View.GONE

                            tvNoRequest.visibility = View.VISIBLE
                        }
                    } else {
                        rv_coonections.visibility = View.GONE

                        showSnackBar(pojoConnectionList.message)
                    }

                }
            })

    }

    private fun showSnackBar(message: String?) {
        SnackbarUtil.showWarningShortSnackbar(this, message)
    }
//---------------------------- get connection resonse api --------------------------------------//

    private fun connection_request_response(status: String, connectionID: String) {
        clLoader_request.visibility = View.VISIBLE
        viewModel?.connectionRequest(auth_token, status, connectionID)?.observe(
            this,
            object : Observer<PojoConnectionAccpetReject?> {


                override fun onChanged(t: PojoConnectionAccpetReject?) {
                    clLoader_request.visibility = View.GONE

                    if (t!!.code == 200) {
                        connection_list("block")
                    } else {
                        clLoader_request.visibility = View.GONE

                        showSnackBar(t.message)
                    }

                }
            })

    }
}
