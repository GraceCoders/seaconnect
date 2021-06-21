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
import com.sea.seaconnect.view.`interface`.BlockClickListener
import kotlinx.android.synthetic.main.item_blocked_connections.view.*
import kotlinx.android.synthetic.main.item_connections.view.*
import kotlinx.android.synthetic.main.item_connections.view.cvImage
import kotlinx.android.synthetic.main.item_connections.view.tvName


class Blocked_User_Adapter(
    val activity: FragmentActivity?,
    var data: List<ConnectionListData>,
  var   param: BlockClickListener
) :
    RecyclerView.Adapter<Blocked_User_Adapter.MyViewHolderFav>() {
    var onItemClick: ((pos: Int, view: View, connectionListData: ConnectionListData) -> Unit)? =
        null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolderFav {
        return MyViewHolderFav(
            LayoutInflater.from(activity).inflate(R.layout.item_blocked_connections, parent, false)
        )
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: MyViewHolderFav, position: Int) {

        Glide.with(activity!!)
            .load(Constants.PROFILE_URL_API + data[position].userDetail.profile_image)
            .into(holder.itemView.cvImage)
        holder.itemView.tvName.text = data[position].userDetail._id

        holder.itemView.tvUnblock.setOnClickListener {
            param.onUnblock("unblock",data[position]._id)
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
