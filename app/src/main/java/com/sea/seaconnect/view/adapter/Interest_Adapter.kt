package com.sea.seaconnect.view.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.sea.seaconnect.R
import com.sea.seaconnect.model.ResponseModel.Intereaction_list.Data
import kotlinx.android.synthetic.main.item_interest.view.*


class Interest_Adapter (val activity: FragmentActivity?,interestList: ArrayList<Data>): RecyclerView.Adapter<Interest_Adapter.MyViewHolderFav>() {
    private var interestList: ArrayList<Data>? = interestList
    var onItemClick: ((pos: Int, view: View) -> Unit)? = null
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolderFav {
        return MyViewHolderFav(LayoutInflater.from(activity).inflate(R.layout.item_interest, parent, false))
    }

    override fun getItemCount(): Int {
        return interestList!!.size
    }

    override fun onBindViewHolder(holder: MyViewHolderFav, position: Int) {
        val pos = position
        holder.itemView.tv_interest.text = interestList?.get(position)?.name
        holder.itemView.setOnClickListener(View.OnClickListener {
            if (holder.itemView.check_box.isChecked) {
                holder.itemView.check_box.isChecked = false
                onItemClick?.invoke(pos, it)
            } else {
                holder.itemView.check_box.isChecked = true
                onItemClick?.invoke(pos, it)
            }


        })

    }
    inner class MyViewHolderFav(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {

        override fun onClick(v: View) {

            onItemClick?.invoke(adapterPosition, v)
        }

        init {
            itemView.setOnClickListener(this)
            itemView.check_box.isEnabled=false

        }
    }

    //.............................multiple selection check box issue on scrolling recycler view.................................

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }


}


