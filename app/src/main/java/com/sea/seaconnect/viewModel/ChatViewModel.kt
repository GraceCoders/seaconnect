package com.sea.seaconnect.viewModel

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import com.sea.seaconnect.controller.Utills.AppPreferences
import com.sea.seaconnect.controller.utills.Constants
import com.sea.seaconnect.model.RequestModel.HomeApiRequest.Chat.Message
import com.sea.seaconnect.model.RequestModel.HomeApiRequest.Chat.PojoMessage
import com.sea.seaconnect.model.ResponseModel.PojoConnectionAccpetReject
import com.spasime.spasime.controller.services.APIRepository
import io.socket.client.IO
import io.socket.client.Socket
import io.socket.engineio.client.transports.WebSocket
import org.json.JSONObject


class ChatViewModel : AndroidViewModel {
    private lateinit var context: Context
    private var mNewMessage = MutableLiveData<Message>()
    private var PojoMessage = PojoMessage()

    companion object {


        private const val EVENT_RECEIVE_MESSAGE_RESPONSE = "getMessagesResponse"
        private const val EVENT_ADD_MESSAGE_RESPONSE = "addMessageResponse"
        private const val EVENT_ADD_MESSAGE = "addMessage"
        private const val EVENT_RECEIVE_MESSAGE = "getMessages"

        private const val EVENT_PARAM_SENDER_ID = "from_user_id"
        private const val EVENT_PARAM_RECEIVER_ID = "to_user_id"

        private const val EVENT_PARAM_MESSAGE = "message"
        private const val EVENT_PARAM_CONVERSATION_ID = "connectionId"
        private const val CHAT_PARAM_CONVERSATION_ID = "connection_id"

    }

    var mConnectionID = ""
    private var mChatSocket: Socket? = null
    private var mMessagesList = MutableLiveData<List<Message>>()

    constructor(application: Application) : super(application) {
        this.context = application
    }

    fun setUpChatSocket(connectionId: String) {
        mConnectionID = connectionId
        // Get Socket
        val opts = IO.Options()
        opts.transports = arrayOf(WebSocket.NAME)

        mChatSocket = IO.socket("http://45.90.108.137:3003", opts)

        mChatSocket?.connect()


        // Register for events on Socket
        mChatSocket?.on(Socket.EVENT_CONNECT) {
            Log.v("Connected ChatViewModel", "connect")


            joinOrLeaveConversation(connectionId)
        }

        mChatSocket?.on(Socket.EVENT_DISCONNECT) {
            Log.v("ChatViewModel", it[0].toString())
        }

        mChatSocket?.on(Socket.EVENT_CONNECT_ERROR) {
            Log.v("ChatViewModel error ", it[0].toString())
        }


        // Listen to new messages
        mChatSocket?.on(EVENT_RECEIVE_MESSAGE_RESPONSE) {
            if (null != it && it.isNotEmpty()) {
                PojoMessage = Gson().fromJson(it[0].toString(), PojoMessage::class.java)
                Log.e("TAG", "setUpChatSocket12: " + PojoMessage.toString())
                    mMessagesList.postValue(PojoMessage.result)



            }

        }   // Listen to new messages
        mChatSocket?.on(EVENT_ADD_MESSAGE_RESPONSE) {
            if (null != it && it.isNotEmpty()) {
                val data = (Gson().fromJson(it[0].toString(), Message::class.java))

                    mNewMessage.postValue(Gson().fromJson(it[0].toString(), Message::class.java))
                    Log.e("TAG", "setUpChatSocket11: " + mNewMessage)



            }

        }


    }

    private fun joinOrLeaveConversation(connnectionID: String) {
        val jsonObject = JSONObject()
        jsonObject.put(EVENT_PARAM_CONVERSATION_ID, connnectionID)
        mChatSocket?.emit(EVENT_RECEIVE_MESSAGE, jsonObject)


    }

    fun sendMessage(conversationId: String, receiverId: String, message: String) {


        // Create message object
        val messageObject = Message(
            message = message,
            connection_id = conversationId,
            to_user_id = receiverId,
//            sentByMe = 1,
            from_user_id = AppPreferences.init(context).getString(Constants.USER_ID)
        )

        // Set value to notify observer

        sendMessageToServer(
            conversationId,
            receiverId,
            messageObject,
            AppPreferences.init(context).getString(Constants.USER_ID)
        )
    }

    private fun sendMessageToServer(
        conversationId: String,
        receiverId: String,
        newMessage: Message,
        mSenderId: String
    ) {
        val jsonObject = JSONObject()
        jsonObject.put(CHAT_PARAM_CONVERSATION_ID, conversationId)
        jsonObject.put(EVENT_PARAM_SENDER_ID, mSenderId)
        jsonObject.put(EVENT_PARAM_RECEIVER_ID, receiverId)
        jsonObject.put(EVENT_PARAM_MESSAGE, newMessage.message)
        Log.e("TAG", "sendMessageToServer: "+jsonObject.toString() )
        mChatSocket?.emit(EVENT_ADD_MESSAGE, jsonObject)
    }

    fun disconnectChatSocket() {
        mChatSocket?.disconnect()
        mChatSocket?.off(Socket.EVENT_CONNECT)
        mChatSocket?.off(Socket.EVENT_DISCONNECT)
        mChatSocket?.off(Socket.EVENT_CONNECT_ERROR)
        mChatSocket?.off(Socket.EVENT_CONNECT_TIMEOUT)
        mChatSocket?.off(EVENT_RECEIVE_MESSAGE)
    }

    fun getMessagesList() = mMessagesList
    fun getNewMessage() = mNewMessage


    fun connectionRequest(
        mToken: String,
        status: String,
        connectionID: String
    ): LiveData<PojoConnectionAccpetReject> {
        var mutableLiveData: MutableLiveData<PojoConnectionAccpetReject> = MutableLiveData()
        return APIRepository.connectionRequestResponse(mToken, status, connectionID)
        return mutableLiveData
    }

}