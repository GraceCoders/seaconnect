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
import com.sea.seaconnect.model.ResponseModel.ConnectionRequestDatas
import com.sea.seaconnect.model.ResponseModel.PojoConnectionList
import com.sea.seaconnect.view.adapter.Connections_Adapter
import com.sea.seaconnect.viewModel.ConnectionRequestViewModel
import kotlinx.android.synthetic.main.activity_connection.*
import kotlinx.android.synthetic.main.activity_connection.tvNoRequest
import kotlinx.android.synthetic.main.activity_connection_request.*
import kotlinx.android.synthetic.main.activity_notification.iv_back
import kotlinx.android.synthetic.main.activity_review_queaue.*

class ConnectionActivity : AppCompatActivity(), View.OnClickListener {
    private var viewModel: ConnectionRequestViewModel? = null
    var auth_token = ""
    private var connectionList: ArrayList<ConnectionRequestDatas> = ArrayList()

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
        liConnectionReuest.visibility=View.VISIBLE

    }

    override fun onResume() {
        super.onResume()

        // hit api
        connection_list("accepted")
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

                        if (pojoConnectionList.data != null) {
                            tvNoRequest.visibility = View.GONE

                            //...........................adapter for notifications .....................................//

                            rv_coonections?.layoutManager = LinearLayoutManager(this@ConnectionActivity)
                            rv_coonections?.adapter = Connections_Adapter(this@ConnectionActivity,pojoConnectionList.data)
                            (rv_coonections?.adapter as Connections_Adapter).onItemClick = { pos, view, userData ->


                                val intent = Intent(this@ConnectionActivity, MessageDetailActivity::class.java)
                                intent.putExtra("UserDatas", userData)
                                startActivity(intent)


                            }

                            rv_queaue?.isNestedScrollingEnabled = false
                        } else {
                            tvNoRequest.visibility = View.VISIBLE
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

}
