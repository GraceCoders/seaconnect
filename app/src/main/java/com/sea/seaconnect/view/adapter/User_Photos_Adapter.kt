package com.sea.seaconnect.view.adapter

import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.sea.seaconnect.R
import com.sea.seaconnect.model.ResponseModel.UserImage
import com.sea.seaconnect.view.`interface`.OnImageClickListener
import kotlinx.android.synthetic.main.item_images.view.*

class User_Photos_Adapter(
    val activity: FragmentActivity?,
    var userImages: MutableList<UserImage>,
var     param: OnImageClickListener
) :
    RecyclerView.Adapter<User_Photos_Adapter.MyViewHolderFav>() {
    var onItemClick: ((pos: Int, view: View) -> Unit)? = null
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolderFav {
        return MyViewHolderFav(
            LayoutInflater.from(activity).inflate(
                R.layout.item_images, parent, false
            )
        )
    }

    override fun getItemCount(): Int {
        return userImages.size + 1
    }

    override fun onBindViewHolder(holder: MyViewHolderFav, position: Int) {

        if(position==0)
        {
            holder.itemView.rlImages.visibility=View.GONE
            holder.itemView.ivDelete.visibility=View.GONE
            holder.itemView.ivAdd.visibility=View.VISIBLE
        }else
        {
            if(userImages[position-1].isNotUploaded && userImages[position-1].imageURl!=null)
            {
                val uri = Uri.fromFile( userImages[position-1].imageURl)
                Glide.with(activity!!).load(uri)
                    .into(holder.itemView.iv_profile_image)

            }
            else
            {
                Glide.with(activity!!).load( userImages[position-1].user_image_link)
                    .into(holder.itemView.iv_profile_image)

            }

            holder.itemView.rlImages.setOnClickListener {
                if(userImages[position-1].isNotUploaded && userImages[position-1].imageURl!=null)
                {

                    userImages.removeAt(position-1)
                    notifyDataSetChanged()
                }
                else
                {
                    param.onImageDelete(userImages[position-1]._id,position.toString())
                    userImages.removeAt(position-1)
                    notifyDataSetChanged()

                }
            }
            holder.itemView.rlImages.visibility=View.VISIBLE
            holder.itemView.ivDelete.visibility=View.VISIBLE
            holder.itemView.ivAdd.visibility=View.GONE
        }

    }

    inner class MyViewHolderFav(itemView: View) : RecyclerView.ViewHolder(itemView),
        View.OnClickListener {
        override fun onClick(v: View) {
            if(adapterPosition==0)
            {
            onItemClick?.invoke(adapterPosition, v)}
        }

        init {
            itemView.setOnClickListener(this)
        }
    }
}
