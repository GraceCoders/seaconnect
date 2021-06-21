package com.sea.seaconnect.view.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.sea.seaconnect.R
import com.sea.seaconnect.model.ResponseModel.NotificationDataList
import kotlinx.android.synthetic.main.item_notifications.view.*
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*


class Notification_Adapter(
    val activity: FragmentActivity,
 var    noti_list: ArrayList<NotificationDataList>
) : RecyclerView.Adapter<Notification_Adapter.MyViewHolderFav>() {
    var onItemClick: ((pos: Int, view: View) -> Unit)? = null
    var elapsedDays: Long =
        0
    var elapsedHours:kotlin.Long =  0
    var elapsedMinutes:kotlin.Long = 0
    var elapsedSeconds:kotlin.Long = 0
    var elapsedWeeks:kotlin.Long = 0
    var SERVER_TIME_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolderFav {
        return MyViewHolderFav(LayoutInflater.from(activity).inflate(R.layout.item_notifications, parent, false))
    }

    override fun getItemCount(): Int {
        return  noti_list.size
    }

    override fun onBindViewHolder(holder: MyViewHolderFav, position: Int) {
//Glide.with(activity).load(noti_list[position].)holder.itemView.cvUserImage
        holder.itemView.tvMessage.text=noti_list[position].title

        val tsLong = java.lang.Long.valueOf(System.currentTimeMillis() / 1000)

        val formatter = SimpleDateFormat("dd/MM/yyyy HH:mm:ss")
       var  dateString = changeDateFormat(
           noti_list[position].createdAt,
            SERVER_TIME_FORMAT,
           "dd/MM/yyyy HH:mm:ss"
        )
        val currentdate: String = formatter.format(Date((tsLong * 1000).toString().toLong()))

        try {
            val date1: Date = formatter.parse(dateString)
            val date2: Date = formatter.parse(currentdate)
            printDifference(date1, date2)
        } catch (e: ParseException) {
            e.printStackTrace()
        }






        if (elapsedWeeks == 0L) {
            if (elapsedDays == 0L) {
                if (elapsedHours == 0L) {
                    if (elapsedMinutes == 0L) {
                        if (elapsedSeconds < 0) {
                            holder.itemView.time.setText("0 s")
                        } else {
                            holder.itemView.time.setText("$elapsedSeconds s")
                        }
                    } else {
                        holder.itemView.time.setText("$elapsedMinutes min ag")
                    }
                } else {
                    holder.itemView.time.setText("$elapsedHours h")
                }
            } else {
                Log.e("DAYS", "ELAPSED$elapsedDays")
                holder.itemView.time.setText("$elapsedDays d")
            }
        } else {
            holder.itemView.time.setText("$elapsedWeeks w")
        }
    }
    inner class MyViewHolderFav(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {
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

    fun printDifference(startDate: Date, endDate: Date) {
//milliseconds
        var different = endDate.time - startDate.time
        println("startDate : $startDate")
        println("endDate : $endDate")
        println("different : $different")
        val secondsInMilli: Long = 1000
        val minutesInMilli = secondsInMilli * 60
        val hoursInMilli = minutesInMilli * 60
        val daysInMilli = hoursInMilli * 24
        val weekInMilli = daysInMilli * 7
        elapsedWeeks = different / weekInMilli
        different = different % weekInMilli
        elapsedDays = different / daysInMilli
        different = different % daysInMilli
        elapsedHours = different / hoursInMilli
        different = different % hoursInMilli
        elapsedMinutes = different / minutesInMilli
        different = different % minutesInMilli
        elapsedSeconds = different / secondsInMilli
        Log.e(
            "DAYS",
            "DAYS" + elapsedDays + " Hours: " + elapsedHours + " Min" + elapsedMinutes + "Sec" + elapsedSeconds
        )
    }


}
