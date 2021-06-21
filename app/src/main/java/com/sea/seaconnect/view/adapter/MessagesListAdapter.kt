package com.sea.seaconnect.view.adapter

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.RecyclerView
import com.sea.seaconnect.R
import com.sea.seaconnect.controller.Utills.AppPreferences
import com.sea.seaconnect.controller.utills.Constants
import com.sea.seaconnect.model.RequestModel.HomeApiRequest.Chat.Message
import kotlinx.android.synthetic.main.row_load_earlier_messages.view.*
import kotlinx.android.synthetic.main.row_receiver_text_message.view.*
import kotlinx.android.synthetic.main.row_sender_text_message.view.*

@Suppress("DEPRECATION")
class MessagesListAdapter(private val mMessagesFragment: Activity) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        private const val ROW_TYPE_SENDER_TEXT_MESSAGE = 0
        private const val ROW_TYPE_RECEIVER_TEXT_MESSAGE = 1
        private const val ROW_TYPE_LOAD_EARLIER_MESSAGES = 5

        const val MESSAGE_TYPE_TEXT_MESSAGE = 1
    }

    private var mMessagesList = mutableListOf<Message>()
    private var isLoadEarlierMessages: Boolean = false


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {

            ROW_TYPE_SENDER_TEXT_MESSAGE -> {
                SenderTextMessageViewHolder(
                    parent
                        .inflate(layoutRes = R.layout.row_sender_text_message)
                )
            }
            else -> {
                ReceiverTextMessageViewHolder(
                    parent
                        .inflate(layoutRes = R.layout.row_receiver_text_message)
                )
            }
        }
    }

    override fun getItemCount(): Int {
        return mMessagesList.size
    }


    override fun getItemViewType(position: Int): Int {
        return if (mMessagesList[position].from_user_id == AppPreferences.init(mMessagesFragment).getString(Constants.USER_ID)
        ) {
            0
        } else {

            1
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (getItemViewType(position)) {
            ROW_TYPE_LOAD_EARLIER_MESSAGES -> {
                (holder as LoaderEarlierMessagesViewHolder).bindLoader()
            }
            ROW_TYPE_SENDER_TEXT_MESSAGE -> {
                (holder as SenderTextMessageViewHolder)
                    .bindSenderTextMessage(mMessagesList[position])
            }
            else -> {
                (holder as ReceiverTextMessageViewHolder)
                    .bindReceiverTextMessage(mMessagesList[position])
            }
        }
    }

    fun addToMessagesList(message: Message? = null, messagesList: List<Message>? = null) {
        when {
            null == message && null == messagesList -> return
            null != message -> {
                this.mMessagesList.add(message)
                notifyItemInserted(this.mMessagesList.size)
            }
            else -> {
                mMessagesList.clear()
                this.mMessagesList.addAll(messagesList!!)
                notifyDataSetChanged()
            }
        }
    }

    inner class LoaderEarlierMessagesViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {

        init {
            itemView.tvLoadEarlierMsgs.setOnClickListener {
                notifyItemChanged(adapterPosition)
            }
        }

        fun bindLoader() {
            if (isLoadEarlierMessages) {
                itemView.tvLoadEarlierMsgs.visibility = View.VISIBLE
            } else {
                itemView.tvLoadEarlierMsgs.visibility = View.GONE
            }
        }

    }

    inner class SenderTextMessageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bindSenderTextMessage(message: Message) {
            with(message) {
                itemView.tvSenderMessage.text = this.message

            }

        }
    }

    inner class ReceiverTextMessageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bindReceiverTextMessage(message: Message) {

            with(message) {


                itemView.tvReceiverMessage.text = this.message


            }
        }

    }


    fun ViewGroup.inflate(@LayoutRes layoutRes: Int, attachToRoot: Boolean = false): View {
        return LayoutInflater.from(context).inflate(layoutRes, this, attachToRoot)
    }

}
