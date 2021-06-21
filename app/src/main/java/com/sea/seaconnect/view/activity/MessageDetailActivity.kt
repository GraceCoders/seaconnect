package com.sea.seaconnect.view.activity

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.PopupMenu
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.sea.seaconnect.R
import com.sea.seaconnect.controller.Utills.AppPreferences
import com.sea.seaconnect.controller.utills.Constants
import com.sea.seaconnect.controller.utills.SnackbarUtil
import com.sea.seaconnect.model.ResponseModel.ConnectionListData
import com.sea.seaconnect.model.ResponseModel.PojoConnectionAccpetReject
import com.sea.seaconnect.view.adapter.MessagesListAdapter
import com.sea.seaconnect.viewModel.ChatViewModel
import kotlinx.android.synthetic.main.activity_connection_request.*
import kotlinx.android.synthetic.main.activity_message_detail.*
import kotlinx.android.synthetic.main.activity_message_detail.iv_back


class MessageDetailActivity : AppCompatActivity(), View.OnClickListener {
    private var viewModel: ChatViewModel? = null
    var connectionId = ""
    var userID = ""
    var auth_token = ""

    private val mChatListAdapter by lazy { MessagesListAdapter(this) }
    private lateinit var mLinearLayoutManager: LinearLayoutManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_message_detail)
        initView()
    }

    private fun initView() {  
        auth_token = AppPreferences.init(this).getString(Constants.TOKEN)

        viewModel = ViewModelProviders.of(this).get(ChatViewModel::class.java)
        mLinearLayoutManager = LinearLayoutManager(this)
        rvMessages.layoutManager = mLinearLayoutManager
        rvMessages.adapter = mChatListAdapter

        // Observe messages list
        viewModel!!.getMessagesList().observe(this, Observer {
            mChatListAdapter.addToMessagesList(null, it!!.asReversed())
            mLinearLayoutManager.scrollToPosition(mChatListAdapter.itemCount - 1)
        })

        // Observe for a new message
        viewModel!!.getNewMessage().observe(this, Observer {
            mChatListAdapter.addToMessagesList(it, null)
            setReyclerViewSelection()
        })

        if (intent.extras != null) {
            val connectionData: ConnectionListData =
                intent.getParcelableExtra("UserDatas") as ConnectionListData

            Glide.with(this)
                .load(Constants.PROFILE_URL_API + connectionData.userDetail.profile_image)
                .into(cvChatUserImage)
            tvName.text = connectionData.userDetail.first_name
            connectionId = connectionData._id
            userID = connectionData.userDetail._id
            viewModel?.setUpChatSocket(connectionId)

        }
        iv_back.setOnClickListener(this)
        ivSend.setOnClickListener(this)
        ivMore.setOnClickListener(this)
    }

    private fun setReyclerViewSelection() {
        if (mChatListAdapter.itemCount - 20 <= this.mLinearLayoutManager
                .findFirstCompletelyVisibleItemPosition()
        ) {
            mLinearLayoutManager.scrollToPosition(mChatListAdapter.itemCount - 1)
        } else {
            mLinearLayoutManager.scrollToPosition(
                mLinearLayoutManager
                    .findFirstCompletelyVisibleItemPosition()
            )
        }
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.iv_back -> {
                onBackPressed()
            }
            R.id.ivMore -> {
                val popup = PopupMenu(this, ivMore)
                popup.menuInflater.inflate(R.menu.options, popup.menu)
                popup.setOnMenuItemClickListener(object : PopupMenu.OnMenuItemClickListener {

                    override fun onMenuItemClick(item: MenuItem?): Boolean {

                        connection_request_response("block", connectionId)
                        return true

                    }
                })

                popup.show()
            }

            R.id.ivSend -> {
                if (etMessage.text.toString().trim() != "") {
                    viewModel?.sendMessage(connectionId, userID, etMessage.text.toString().trim())

                    etMessage.setText("")
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        viewModel?.disconnectChatSocket()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }

//---------------------------- get connection resonse api --------------------------------------//

    private fun connection_request_response(status: String, connectionID: String) {
        clLoader_chat.visibility = View.VISIBLE
        viewModel?.connectionRequest(auth_token, status, connectionID)?.observe(
            this,
            object : Observer<PojoConnectionAccpetReject?> {


                override fun onChanged(t: PojoConnectionAccpetReject?) {

                    if (t!!.code == 200) {

                        finish()
                    } else {
                        clLoader_chat.visibility = View.GONE

                        showSnackBar(t.message)
                    }

                }
            })

    }

    private fun showSnackBar(message: String?) {
        SnackbarUtil.showWarningShortSnackbar(this, message)
    }
}
