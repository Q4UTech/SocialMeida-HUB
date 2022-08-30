package com.jatpack.socialmediahub.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.jatpack.socialmediahub.R
import com.jatpack.socialmediahub.model.NotificatioListItem
import com.jatpack.socialmediahub.util.SetClick
import com.squareup.picasso.Picasso
import java.io.File

class NotificationAdapter(
    private var context: Context,
    var notificationList: ArrayList<NotificatioListItem>,
    var listener: SetClick
) :
    RecyclerView.Adapter<NotificationAdapter.MyViewHolder>() {
    var checkStatus = BooleanArray(notificationList.size)
    var tempList = ArrayList<File>()
    var isLongClickEnabled = false
    var visible = false
    private var listenerSelection: CounterSlection? = null

    interface CounterSlection {
        fun selectItems(itemSlectionCount: Int)
    }

    fun setCheckedListener(listenerSelection: CounterSlection) {
        this.listenerSelection = listenerSelection
    }

    class MyViewHolder(item: View) : RecyclerView.ViewHolder(item) {
        val img = item.findViewById<ImageView>(R.id.img)
        val content = item.findViewById<TextView>(R.id.tv_notification_content)
        val checkBox = item.findViewById<CheckBox>(R.id.dataTime)


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.notification_item_layout, parent, false)
        )
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        holder.content.text = notificationList[position].content
        Glide.with(context).load(notificationList[position].image).into(holder.img)


    }


    override fun getItemCount(): Int {
        Log.d("TAG", "getItemCount: " + notificationList.size)
        return notificationList.size
    }

    fun updateList(newList: ArrayList<NotificatioListItem>) {
        this.notificationList = newList
        Log.d("TAG", "updateList: " + notificationList.size)
        notifyDataSetChanged()

    }
}