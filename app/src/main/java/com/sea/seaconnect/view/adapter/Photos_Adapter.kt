package com.sea.seaconnect.view.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.sea.seaconnect.R
import com.sea.seaconnect.model.ResponseModel.UserImage
import kotlinx.android.synthetic.main.item_photos.view.*

class Photos_Adapter(val activity: FragmentActivity?, var userImages: List<UserImage>) :
    RecyclerView.Adapter<Photos_Adapter.MyViewHolderFav>() {
    var onItemClick: ((pos: Int, view: View) -> Unit)? = null
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolderFav {
        return MyViewHolderFav(
            LayoutInflater.from(activity).inflate(R.layout.item_photos, parent, false)
        )
    }

    override fun getItemCount(): Int {
        return userImages.size
    }

    override fun onBindViewHolder(holder: MyViewHolderFav, position: Int) {

        Glide.with(activity!!).load(userImages[position].user_image_link)
            .into(holder.itemView.iv_profile_image)
    }

    inner class MyViewHolderFav(itemView: View) : RecyclerView.ViewHolder(itemView),
        View.OnClickListener {
        override fun onClick(v: View) {
            onItemClick?.invoke(adapterPosition, v)
        }

        init {
            itemView.setOnClickListener(this)
        }
    }
}
