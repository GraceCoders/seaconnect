package com.sea.seaconnect.view.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.sea.seaconnect.R
import com.sea.seaconnect.controller.utills.Constants
import com.sea.seaconnect.model.ResponseModel.Review
import kotlinx.android.synthetic.main.item_reviews.view.*
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.*

class Review_Adapter(
    val activity: FragmentActivity?,
    var reviews: List<Review>

) : RecyclerView.Adapter<Review_Adapter.MyViewHolderFav>() {

    var SERVER_TIME_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"

    var onItemClick: ((pos: Int, view: View) -> Unit)? = null
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolderFav {
        return MyViewHolderFav(
            LayoutInflater.from(activity).inflate(R.layout.item_reviews, parent, false)
        )
    }

    override fun getItemCount(): Int {
        return reviews.size
    }

    override fun onBindViewHolder(holder: MyViewHolderFav, position: Int) {
        Glide.with(activity!!)
            .load(Constants.PROFILE_URL_API + reviews[position].reviewdBy.profile_image)
            .into(holder.itemView.cvUserImage)
        holder.itemView.tvName.text = reviews[position].reviewdBy.first_name
        holder.itemView.tvReviews.text = reviews[position].display_rating
        holder.itemView.tvRating.text =  DecimalFormat("##.#").format(reviews[position].display_rating)
        holder.itemView.tvDate.text = changeDateFormat(
            reviews[position].createdAt,
            SERVER_TIME_FORMAT,
            "mm/dd/yyyy"
        )
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

    // change date format
    fun changeDateFormat(dob: String, currentFormat: String, requiredFormat: String): String {
        try {

            return SimpleDateFormat(requiredFormat, Locale.US)
                .format(SimpleDateFormat(currentFormat, Locale.US).parse(dob))
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return ""
    }
}
