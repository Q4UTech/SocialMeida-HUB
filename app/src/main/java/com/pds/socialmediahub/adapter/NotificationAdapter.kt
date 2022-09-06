package com.pds.socialmediahub.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.widget.AppCompatCheckBox
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.pds.socialmediahub.R
import com.pds.socialmediahub.model.NotificatioListItem
import com.pds.socialmediahub.util.SetClick
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
        val checkBox = item.findViewById<AppCompatCheckBox>(R.id.checkbox)


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
        if (notificationList[position].isSelected) {
            holder.checkBox.isChecked = true
        } else {
            holder.checkBox.isChecked = false
        }
        holder.checkBox.setOnClickListener {
            checkStatus[position] = !checkStatus[position]
        }

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