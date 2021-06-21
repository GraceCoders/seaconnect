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
import com.sea.seaconnect.model.ResponseModel.UserDetail
import kotlinx.android.synthetic.main.item_review_queaue.view.*

class Quaue_Adapter(val activity: FragmentActivity?,var  data: List<ConnectionListData>): RecyclerView.Adapter<Quaue_Adapter.MyViewHolderFav>() {
    var onItemClick: ((pos: Int, view: View,userDetail: UserDetail,mConnectionId:String) -> Unit)? = null
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolderFav {
        return MyViewHolderFav(LayoutInflater.from(activity).inflate(R.layout.item_review_queaue, parent, false))
    }

    override fun getItemCount(): Int {
        return  data.size
    }

    override fun onBindViewHolder(holder: MyViewHolderFav, position: Int) {
Glide.with(activity!!).load(Constants.PROFILE_URL_API+data[position].userDetail.profile_image).into(holder.itemView.userImage)
        holder.itemView.tvName.text=data[position].userDetail.first_name
    }
    inner class MyViewHolderFav(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {
        override fun onClick(v: View) {
            onItemClick?.invoke(adapterPosition, v,data[adapterPosition].userDetail,data[adapterPosition]._id)
        }

        init {
            itemView.setOnClickListener(this)
        }
    }
}
