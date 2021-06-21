package com.sea.seaconnect.view.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.sea.seaconnect.R
import com.sea.seaconnect.controller.utills.Constants
import com.sea.seaconnect.model.ResponseModel.ConnectionListData
import kotlinx.android.synthetic.main.item_connections.view.*


class Connections_Adapter(val activity: FragmentActivity?, var data: List<ConnectionListData>) :
    RecyclerView.Adapter<Connections_Adapter.MyViewHolderFav>() {
    var onItemClick: ((pos: Int, view: View, connectionListData: ConnectionListData) -> Unit)? =
        null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolderFav {
        return MyViewHolderFav(
            LayoutInflater.from(activity).inflate(R.layout.item_connections, parent, false)
        )
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: MyViewHolderFav, position: Int) {
        if (data[position].chat != null) {
            holder.itemView.tvChat.text = data[position].chat.message
            holder.itemView.tvTime.text = data[position].chat.time

        }
        Glide.with(activity!!)
            .load(Constants.PROFILE_URL_API + data[position].userDetail.profile_image)
            .into(holder.itemView.cvImage)
        holder.itemView.tvName.text = data[position].userDetail.first_name
    }

    inner class MyViewHolderFav(itemView: View) : RecyclerView.ViewHolder(itemView),
        View.OnClickListener {
        override fun onClick(v: View) {
            onItemClick?.invoke(adapterPosition, v, data[adapterPosition])
        }

        init {
            itemView.setOnClickListener(this)
        }
    }
}
