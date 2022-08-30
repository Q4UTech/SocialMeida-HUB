package com.jatpack.socialmediahub.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.jatpack.socialmediahub.R
import com.jatpack.socialmediahub.model.BottomList
import com.jatpack.socialmediahub.model.NotificatioListItem
import com.jatpack.socialmediahub.util.SetClick
import com.squareup.picasso.Picasso
import java.io.File

class BottomStatusAdapter(
    private var context: Context,
    var notificationList: ArrayList<BottomList>,
    var listener: SetClick
) :
    RecyclerView.Adapter<BottomStatusAdapter.MyViewHolder>() {
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
        val content = item.findViewById<TextView>(R.id.name)
        val checkBox = item.findViewById<CheckBox>(R.id.dataTime)
        val rl = item.findViewById<RelativeLayout>(R.id.rl)
        val img_check = item.findViewById<ImageView>(R.id.img_check)


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.bottom_item_layout, parent, false)
        )
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        holder.content.text = notificationList[position].name
        //   Picasso.get().load(notificationList[position].img).into(holder.img)
        Glide.with(context).load(notificationList[position].img).into(holder.img)
        if (checkStatus[position]) {
            holder.img_check.visibility = View.VISIBLE
        } else {
            holder.img_check.visibility = View.GONE
        }
        holder.rl.setOnClickListener {
            Log.d("TAG", "getItemCount12: " + position)
            for (i in checkStatus.indices) {
                if (!checkStatus[position]) {
                    checkStatus[position] = true

                } else {
                    checkStatus[position] = false

                }

            }
            notifyDataSetChanged()
            listener.onClick(it, position)
        }

    }


    override fun getItemCount(): Int {

        return notificationList.size
    }

    fun updateList(newList: ArrayList<BottomList>) {
        this.notificationList = newList
        Log.d("TAG", "updateList: " + notificationList.size)
        notifyDataSetChanged()

    }
}