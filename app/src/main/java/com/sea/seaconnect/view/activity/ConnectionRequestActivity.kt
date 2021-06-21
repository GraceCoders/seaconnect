package com.sea.seaconnect.view.activity

import android.os.Bundle
import android.view.View
import androidx.annotation.Nullable
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.sea.seaconnect.R
import com.sea.seaconnect.controller.Utills.AppPreferences
import com.sea.seaconnect.controller.utills.Constants
import com.sea.seaconnect.controller.utills.SnackbarUtil
import com.sea.seaconnect.model.ResponseModel.ConnectionRequestDatas
import com.sea.seaconnect.model.ResponseModel.PojoConnectionAccpetReject
import com.sea.seaconnect.model.ResponseModel.PojoConnectionReuqestList
import com.sea.seaconnect.view.`interface`.ConnectionRequestListener
import com.sea.seaconnect.view.adapter.Connection_Request_Adapter
import com.sea.seaconnect.viewModel.ConnectionRequestViewModel
import kotlinx.android.synthetic.main.activity_choose_interest.*
import kotlinx.android.synthetic.main.activity_connection.*
import kotlinx.android.synthetic.main.activity_connection_request.*
import kotlinx.android.synthetic.main.activity_connection_request.iv_back

class ConnectionRequestActivity : AppCompatActivity(), View.OnClickListener {
    private var viewModel: ConnectionRequestViewModel? = null
    var auth_token = ""
    private var connectionList: ArrayList<ConnectionRequestDatas> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_connection_request)
        initView()
    }


    fun initView() {
        auth_token = AppPreferences.init(this).getString(Constants.TOKEN)
        viewModel = ViewModelProviders.of(this).get(ConnectionRequestViewModel::class.java)
        // Set click listener
        iv_back.setOnClickListener(this)
        get_connection_request_api()

    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.iv_back -> {
                finish()
            }
        }
    }

    //..........................................connection request list api...........................................

    private fun get_connection_request_api() {
        clLoader_request.visibility = View.VISIBLE
        viewModel?.getConnectionRequests(auth_token)?.observe(
            this,
            object : Observer<PojoConnectionReuqestList?> {
                override fun onChanged(@Nullable interestlist: PojoConnectionReuqestList?) {
                    clLoader_request.visibility = View.GONE
                    rv_connect_request.visibility = View.GONE

                    if (interestlist!!.data != null) {
                        connectionList = interestlist.data as ArrayList<ConnectionRequestDatas>
                        if (connectionList.isNotEmpty()) {
                            tvNoRequest1.visibility = View.GONE
                            rv_connect_request.visibility = View.VISIBLE


                            rv_connect_request?.adapter = Connection_Request_Adapter(
                                this@ConnectionRequestActivity,
                                connectionList, object : ConnectionRequestListener {
                                    override fun onConnectionClick(status: String, userID: String) {
                                        connection_request_response(status, userID)
                                    }

                                }
                            )

                            rv_connect_request?.isNestedScrollingEnabled = false

                        } else {
                            tvNoRequest1.visibility = View.VISIBLE
                            rv_connect_request.visibility = View.GONE

                        }
                    } else {
                        tvNoRequest1.visibility = View.VISIBLE
                        rv_connect_request.visibility = View.GONE
                        rv_connect_request.visibility = View.GONE

                    }


                }
            })

    }
//---------------------------- get connection resonse api --------------------------------------//

    private fun connection_request_response(status: String, connectionID: String) {
        clLoader_request.visibility = View.VISIBLE
        viewModel?.connectionRequest(auth_token, status, connectionID)?.observe(
            this,
            object : Observer<PojoConnectionAccpetReject?> {


                override fun onChanged(t: PojoConnectionAccpetReject?) {

                    if (t!!.code == 200) {
                        get_connection_request_api()
                    } else {
                        clLoader_request.visibility = View.GONE

                        showSnackBar(t.message)
                    }

                }
            })

    }

    private fun showSnackBar(message: String?) {
        SnackbarUtil.showWarningShortSnackbar(this, message)
    }
}