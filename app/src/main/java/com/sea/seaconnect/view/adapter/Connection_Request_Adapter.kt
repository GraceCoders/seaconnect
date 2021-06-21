package com.sea.seaconnect.view.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.sea.seaconnect.R
import com.sea.seaconnect.controller.utills.Constants
import com.sea.seaconnect.model.ResponseModel.ConnectionRequestDatas
import com.sea.seaconnect.view.`interface`.ConnectionRequestListener
import kotlinx.android.synthetic.main.item_connection_request.view.*

class Connection_Request_Adapter(
    val activity: FragmentActivity?,
    var connectionList: ArrayList<ConnectionRequestDatas>,
    var mConnectionRequestListener: ConnectionRequestListener
) : RecyclerView.Adapter<Connection_Request_Adapter.MyViewHolderFav>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolderFav {
        return MyViewHolderFav(
            LayoutInflater.from(activity).inflate(
                R.layout.item_connection_request, parent, false
            )
        )
    }

    override fun getItemCount(): Int {
        return connectionList.size
    }

    override fun onBindViewHolder(holder: MyViewHolderFav, position: Int) {
        Glide.with(activity!!)
            .load(Constants.PROFILE_URL_API + connectionList[position].senderDetail.profile_image)
            .into(holder.itemView.cvImageView)
        holder.itemView.tvName.text = connectionList[position].senderDetail.first_name
        holder.itemView.btnAccept.setOnClickListener {

            mConnectionRequestListener.onConnectionClick("accepted", connectionList[position]._id)
        }
        holder.itemView.btnReject.setOnClickListener {

            mConnectionRequestListener.onConnectionClick("declined", connectionList[position]._id)
        }
    }

    inner class MyViewHolderFav(itemView: View) : RecyclerView.ViewHolder(itemView),
        View.OnClickListener {
        override fun onClick(v: View) {

        }

        init {
            itemView.setOnClickListener(this)
        }
    }
}
